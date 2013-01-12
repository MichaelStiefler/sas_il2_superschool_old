package com.maddox.il2.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.maddox.TexImage.TexImage;
import com.maddox.rts.SFSInputStream;

public class BMPLoader
{
    private InputStream is;
    private int curPos = 0;
    private int bitmapOffset; // starting position of image data
    private int width; // image width in pixels
	private int height; // image height in pixels
    private short bitsPerPixel; // 1, 4, 8, or 24 (no color map)
    private int compression; // 0 (none), 1 (8-bit RLE), or 2 (4-bit RLE)
    private int actualSizeOfBitmap;
    private int scanLineSize;
    private int actualColorsUsed;
    private byte r[], g[], b[]; // color palette
    private int noOfEntries;
    private byte[] byteData; // Unpacked data
    private int[] intData; // Unpacked data

	public BMPLoader()
    {
    }

    protected int readInt() throws IOException
    {
        int b1 = is.read();
        int b2 = is.read();
        int b3 = is.read();
        int b4 = is.read();
        curPos += 4;
        return ((b4 << 24) + (b3 << 16) + (b2 << 8) + (b1 << 0));
    }

    protected short readShort() throws IOException
    {
        int b1 = is.read();
        int b2 = is.read();
        curPos += 4;
        return (short) ((b2 << 8) + b1);
    }

    protected void getFileHeader() throws IOException, Exception
    {
        // Actual contents (14 bytes):
        short fileType = 0x4d42;// always "BM"
        int fileSize; // size of file in bytes
        short reserved1 = 0; // always 0
        short reserved2 = 0; // always 0
        fileType = readShort();
        if (fileType != 0x4d42) throw new Exception("Not a BMP file"); // wrong file type
        fileSize = readInt();
        reserved1 = readShort();
        reserved2 = readShort();
        bitmapOffset = readInt();
    }

    protected void getBitmapHeader() throws IOException
    {
        // Actual contents (40 bytes):
        int size; // size of this header in bytes
        short planes; // no. of color planes: always 1
        int sizeOfBitmap; // size of bitmap in bytes (may be 0: if so, calculate)
        int horzResolution; // horizontal resolution, pixels/meter (may be 0)
        int vertResolution; // vertical resolution, pixels/meter (may be 0)
        int colorsUsed; // no. of colors in palette (if 0, calculate)
        int colorsImportant; // no. of important colors (appear first in palette) (0 means all are important)
        boolean topDown;
        int noOfPixels;
        size = readInt();
        width = readInt();
        height = readInt();
        planes = readShort();
        bitsPerPixel = readShort();
        compression = readInt();
        sizeOfBitmap = readInt();
        horzResolution = readInt();
        vertResolution = readInt();
        colorsUsed = readInt();
        colorsImportant = readInt();
        topDown = (height < 0);
        noOfPixels = width * height;
        // Scan line is padded with zeroes to be a multiple of four bytes
        scanLineSize = ((width * bitsPerPixel + 31) / 32) * 4;
        if (sizeOfBitmap != 0) actualSizeOfBitmap = sizeOfBitmap;
        else
        // a value of 0 doesn't mean zero - it means we have to calculate it
        actualSizeOfBitmap = scanLineSize * height;
        if (colorsUsed != 0) actualColorsUsed = colorsUsed;
        else
        // a value of 0 means we determine this based on the bits per pixel
        if (bitsPerPixel < 16) actualColorsUsed = 1 << bitsPerPixel;
        else actualColorsUsed = 0; // no palette
    }

    protected void getPalette() throws IOException
    {
        noOfEntries = actualColorsUsed;
        //IJ.write("noOfEntries: " + noOfEntries);
        if (noOfEntries > 0)
        {
            r = new byte[noOfEntries];
            g = new byte[noOfEntries];
            b = new byte[noOfEntries];
            int reserved;
            for (int i = 0; i < noOfEntries; i++)
            {
                b[i] = (byte) is.read();
                g[i] = (byte) is.read();
                r[i] = (byte) is.read();
                reserved = is.read();
                curPos += 4;
            }
        }
    }

    protected void unpack(byte[] rawData, int rawOffset, int[] intData, int intOffset, int w)
    {
        int j = intOffset;
        int k = rawOffset;
        int mask = 0xff;
        for (int i = 0; i < w; i++)
        {
            int b0 = (((int) (rawData[k++])) & mask);
            int b1 = (((int) (rawData[k++])) & mask) << 8;
            int b2 = (((int) (rawData[k++])) & mask) << 16;
            intData[j] = 0xff000000 | b0 | b1 | b2;
            j++;
        }
    }

    protected void unpack(byte[] rawData, int rawOffset, int bpp, byte[] byteData, int byteOffset, int w) throws Exception
    {
        int j = byteOffset;
        int k = rawOffset;
        byte mask;
        int pixPerByte;
        switch (bpp)
        {
        case 1:
            mask = (byte) 0x01;
            pixPerByte = 8;
            break;
        case 4:
            mask = (byte) 0x0f;
            pixPerByte = 2;
            break;
        case 8:
            mask = (byte) 0xff;
            pixPerByte = 1;
            break;
        default:
            throw new Exception("Unsupported bits-per-pixel value");
        }
        for (int i = 0;;)
        {
            int shift = 8 - bpp;
            for (int ii = 0; ii < pixPerByte; ii++)
            {
                byte br = rawData[k];
                br >>= shift;
                byteData[j] = (byte) (br & mask);
                //System.out.println("Setting byteData[" + j + "]=" + Test.byteToHex(byteData[j]));
                j++;
                i++;
                if (i == w) return;
                shift -= bpp;
            }
            k++;
        }
    }

    protected int readScanLine(byte[] b, int off, int len) throws IOException
    {
        int bytesRead = 0;
        int l = len;
        int r = 0;
        while (len > 0)
        {
            bytesRead = is.read(b, off, len);
            if (bytesRead == -1) return r == 0 ? -1 : r;
            if (bytesRead == len) return l;
            len -= bytesRead;
            off += bytesRead;
            r += bytesRead;
        }
        return l;
    }

    protected void getPixelData() throws IOException, Exception
    {
        byte[] rawData; // the raw unpacked data
        // Skip to the start of the bitmap data (if we are not already there)
        long skip = bitmapOffset - curPos;
        if (skip > 0)
        {
            is.skip(skip);
            curPos += skip;
        }
        int len = scanLineSize;
        if (bitsPerPixel > 8) intData = new int[width * height];
        else byteData = new byte[width * height];
        rawData = new byte[actualSizeOfBitmap];
        int rawOffset = 0;
        int offset = (height - 1) * width;
        for (int i = height - 1; i >= 0; i--)
        {
            int n = readScanLine(rawData, rawOffset, len);
            if (n < len) throw new Exception("Scan line ended prematurely after " + n + " bytes");
            if (bitsPerPixel > 8)
            {
                // Unpack and create one int per pixel
                unpack(rawData, rawOffset, intData, offset, width);
            }
            else
            {
                // Unpack and create one byte per pixel
                unpack(rawData, rawOffset, bitsPerPixel, byteData, offset, width);
            }
            rawOffset += len;
            offset -= width;
        }
    }
    
    public void readHeaders(InputStream is) throws IOException, Exception
    {
        this.is = is;
        getFileHeader();
        getBitmapHeader();
//        if (compression != 0) throw new Exception("BMP Compression not supported");
//        getPalette();
//        getPixelData();
    }
    
    public void readContent(InputStream is) throws IOException, Exception
    {
        this.is = is;
//        getFileHeader();
//        getBitmapHeader();
        if (compression != 0) throw new Exception("BMP Compression not supported");
        getPalette();
        getPixelData();
    }

    public void read(InputStream is) throws IOException, Exception
    {
        this.is = is;
        getFileHeader();
        getBitmapHeader();
        if (compression != 0) throw new Exception("BMP Compression not supported");
        getPalette();
        getPixelData();
    }
    
    public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public short getBitsPerPixel() {
		return bitsPerPixel;
	}

	public void setBitsPerPixel(short bitsPerPixel) {
		this.bitsPerPixel = bitsPerPixel;
	}

	public int getCompression() {
		return compression;
	}

	public void setCompression(int compression) {
		this.compression = compression;
	}

	public int getActualSizeOfBitmap() {
		return actualSizeOfBitmap;
	}

	public void setActualSizeOfBitmap(int actualSizeOfBitmap) {
		this.actualSizeOfBitmap = actualSizeOfBitmap;
	}

	public int getActualColorsUsed() {
		return actualColorsUsed;
	}

	public void setActualColorsUsed(int actualColorsUsed) {
		this.actualColorsUsed = actualColorsUsed;
	}
	
    public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

	public int[] getIntData() {
		return intData;
	}

	public void setIntData(int[] intData) {
		this.intData = intData;
	}
	
	public static byte getR(int pixelData) {
		int pixelR = pixelData & 0x00FF0000;
		return (byte)(pixelR >> 16);
	}

	public static byte getG(int pixelData) {
		int pixelG = pixelData & 0x0000FF00;
		return (byte)(pixelG >> 8);
	}
	
	public static byte getB(int pixelData) {
		int pixelB = pixelData & 0x000000FF;
		return (byte)(pixelB);
	}
	
    private static void createEmptyBmp() {
    	System.out.println("BmpUtils createEmptyBmp");
    	File f = new File(emptyBmpFile);
    	if(f.exists()) {
    		if (f.length() == 0x100436) return; // empty bmp file with correct length exists already so we've got nothing to do here.
    		f.delete(); // delete file if size doesn't match.
    	}
    	f = new File(emptyBmpPath);
    	f.mkdirs(); // Ensure that placeholder path exists
    	byte bmpHeader[] = {
    			0x42, 0x4D, 0x36, 0x04, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x36, 0x04, 0x00, 0x00, 0x28, 0x00,
    			0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x01, 0x00, 0x08, 0x00, 0x00, 0x00,
    			0x00, 0x00, 0x00, 0x00, 0x10, 0x00, (byte) 0xC2, 0x1E, 0x00, 0x00, (byte) 0xC2, 0x1E, 0x00, 0x00, 0x00, 0x00,
    			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
    	};
    	int numZeros = 0x1003FD;
        FileOutputStream bmpFileOutputStream = null;
		try {
			bmpFileOutputStream = new FileOutputStream(emptyBmpFile);
			bmpFileOutputStream.write(bmpHeader);
			while (numZeros-- > 0) bmpFileOutputStream.write(0);
			bmpFileOutputStream.flush();
			bmpFileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static boolean bmp8PalTo4TGA4(String s, String s1, String s2)
    {
    	System.out.println("BMPLoader bmp8PalTo4TGA4 s=" + s + " s1=" + s1 + " s2=" + s2);
    	SFSInputStream localSFSInputStream = null;
    	BMPLoader bmpLoader = new BMPLoader();
		try {
			localSFSInputStream = new SFSInputStream(s);
			bmpLoader.readHeaders(localSFSInputStream);
			localSFSInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (bmpLoader.getBitsPerPixel() == 24) {
			return bmp24PalTo4TGA4(s, s1, s2);
    	}
		return false;
    }

    public static int squareSizeBMP8Pal(String s){
    	try {
    		SFSInputStream localSFSInputStream = new SFSInputStream(s);
    		BMPLoader bmpLoader = new BMPLoader();
    		bmpLoader.read(localSFSInputStream);
    	    int height = bmpLoader.getHeight();
    	    int width = bmpLoader.getWidth();
    	    int bpp = bmpLoader.getBitsPerPixel();
    	    localSFSInputStream.close();
    		
        	System.out.println("BmpUtils squareSizeBMP8Pal s=" + s + " height=" + height + " width=" + width + " bpp=" + bpp);
        	if (height!=width) return -1;
        	if (height > 1024) { // high res texture test
            	System.out.println("BmpUtils squareSizeBMP8Pal s=" + s + " will be reported to be a 1024x1024 texture!");
            	return 1024;
        	}
        	return height;
    	} catch (Exception e) {
        	System.out.println("BmpUtils squareSizeBMP8Pal Exception");
        	e.printStackTrace();
    	}
    	
    	return 1024;
    }
    
    public static void scaleDouble(TexImage theTexImage)
            throws Exception
        {
            TexImage teximage = null;
            int newImageWidth = 0;
            int newImageHeight = 0;
            int oldImageRow = 0;
            int newImageRow = 0;
            int oldImageColumn = 0;
            int newImageColumn = 0;
            switch(theTexImage.type)
            {
            default:
                throw new Exception("scaleDouble(): type of image not supported");

            case 6407: 
            case 6408: 
            case 32849: 
            case 32856: 
                newImageWidth = theTexImage.sx * 2;
                newImageHeight = theTexImage.sy * 2;
//                teximage = new TexImage(theTexImage, 0, 0, newImageWidth, newImageHeight);
                teximage = new TexImage();
                teximage.type = theTexImage.type;
                teximage.Palette = theTexImage.Palette;
                teximage.BytesPerEntry = theTexImage.BytesPerEntry;
                teximage.BPP = theTexImage.BPP;
                teximage.sx = newImageWidth;
                teximage.sy = newImageHeight;
                teximage.image = new byte[newImageWidth * newImageHeight * teximage.BPP];
                
                for(newImageRow = oldImageRow = 0; newImageRow < newImageHeight;)
                {
                    int interpolateY = oldImageRow;
                    if (interpolateY < theTexImage.sy - 1) interpolateY++;
                    for(newImageColumn = oldImageColumn = 0; newImageColumn < newImageWidth;)
                    {
                        int interpolateX = oldImageColumn;
                        if (interpolateX < theTexImage.sx - 1) interpolateX++;

                        teximage.R(newImageColumn, newImageRow, theTexImage.intR(oldImageColumn, oldImageRow));
                        teximage.R(newImageColumn + 1, newImageRow, theTexImage.intR(oldImageColumn, oldImageRow) + theTexImage.intR(interpolateX, oldImageRow) >> 1);
                        teximage.R(newImageColumn, newImageRow + 1, theTexImage.intR(oldImageColumn, oldImageRow) + theTexImage.intR(oldImageColumn, interpolateY) >> 1);
                        teximage.R(newImageColumn + 1, newImageRow + 1, theTexImage.intR(oldImageColumn, oldImageRow) + theTexImage.intR(interpolateX, interpolateY) >> 1);

                        teximage.G(newImageColumn, newImageRow, theTexImage.intG(oldImageColumn, oldImageRow));
                        teximage.G(newImageColumn + 1, newImageRow, theTexImage.intG(oldImageColumn, oldImageRow) + theTexImage.intG(interpolateX, oldImageRow) >> 1);
                        teximage.G(newImageColumn, newImageRow + 1, theTexImage.intG(oldImageColumn, oldImageRow) + theTexImage.intG(oldImageColumn, interpolateY) >> 1);
                        teximage.G(newImageColumn + 1, newImageRow + 1, theTexImage.intG(oldImageColumn, oldImageRow) + theTexImage.intG(interpolateX, interpolateY) >> 1);
                        
                        teximage.B(newImageColumn, newImageRow, theTexImage.intB(oldImageColumn, oldImageRow));
                        teximage.B(newImageColumn + 1, newImageRow, theTexImage.intB(oldImageColumn, oldImageRow) + theTexImage.intB(interpolateX, oldImageRow) >> 1);
                        teximage.B(newImageColumn, newImageRow + 1, theTexImage.intB(oldImageColumn, oldImageRow) + theTexImage.intB(oldImageColumn, interpolateY) >> 1);
                        teximage.B(newImageColumn + 1, newImageRow + 1, theTexImage.intB(oldImageColumn, oldImageRow) + theTexImage.intB(interpolateX, interpolateY) >> 1);
                        
                        newImageColumn += 2;
                        oldImageColumn++;
                    }

                    if(theTexImage.BPP > 3)
                    {
                        for(newImageColumn = oldImageColumn = 0; newImageColumn < newImageWidth;)
                        {
                            int interpolateX = oldImageColumn;
                            if (interpolateX < theTexImage.sx - 1) interpolateX++;

                            teximage.A(newImageColumn, newImageRow, theTexImage.intA(oldImageColumn, oldImageRow));
                            teximage.A(newImageColumn + 1, newImageRow, theTexImage.intA(oldImageColumn, oldImageRow) + theTexImage.intA(interpolateX, oldImageRow) >> 1);
                            teximage.A(newImageColumn, newImageRow + 1, theTexImage.intA(oldImageColumn, oldImageRow) + theTexImage.intA(oldImageColumn, interpolateY) >> 1);
                            teximage.A(newImageColumn + 1, newImageRow + 1, theTexImage.intA(oldImageColumn, oldImageRow) + theTexImage.intA(interpolateX, interpolateY) >> 1);
                            
                            newImageColumn += 2;
                            oldImageColumn++;
                        }

                    }
                    newImageRow += 2;
                    oldImageRow++;
                }

                break;

            case 6406: 
            case 6409: 
                newImageWidth = theTexImage.sx * 2;
                newImageHeight = theTexImage.sy * 2;
//                teximage = new TexImage(theTexImage, 0, 0, newImageWidth, newImageHeight);

                teximage = new TexImage();
                teximage.type = theTexImage.type;
                teximage.Palette = theTexImage.Palette;
                teximage.BytesPerEntry = theTexImage.BytesPerEntry;
                teximage.BPP = theTexImage.BPP;
                teximage.sx = newImageWidth;
                teximage.sy = newImageHeight;
                teximage.image = new byte[newImageWidth * newImageHeight * teximage.BPP];
                for (newImageRow = oldImageRow = 0; newImageRow < newImageHeight; oldImageRow++) {
                    int interpolateY = oldImageRow;
                    if (interpolateY < theTexImage.sy - 1) interpolateY++;
                    for (newImageColumn = oldImageColumn = 0; newImageColumn < newImageWidth; oldImageColumn++) {
                        int interpolateX = oldImageColumn;
                        if (interpolateX < theTexImage.sx - 1) interpolateX++;
                        
                        teximage.I(newImageColumn, newImageRow, theTexImage.intI(oldImageColumn, oldImageRow));
                        teximage.I(newImageColumn + 1, newImageRow, theTexImage.intI(oldImageColumn, oldImageRow) + theTexImage.intI(interpolateX, oldImageRow) >> 1);
                        teximage.I(newImageColumn, newImageRow + 1, theTexImage.intI(oldImageColumn, oldImageRow) + theTexImage.intI(oldImageColumn, interpolateY) >> 1);
                        teximage.I(newImageColumn + 1, newImageRow + 1, theTexImage.intI(oldImageColumn, oldImageRow) + theTexImage.intI(interpolateX, interpolateY) >> 1);

                        newImageColumn += 2;
                    }
                    newImageRow += 2;
                }
            }
            theTexImage.image = teximage.image;
            teximage.image = null;
            theTexImage.sx = newImageWidth;
            theTexImage.sy = newImageHeight;
        }
   
    
    public static void setR(TexImage theTexImage, int theRow, int theColumn, byte theValue) {
    	theTexImage.image[(theRow * theTexImage.sx + theColumn) * theTexImage.BPP] = theValue;
    }
    
    public static void setG(TexImage theTexImage, int theRow, int theColumn, byte theValue) {
    	theTexImage.image[(theRow * theTexImage.sx + theColumn) * theTexImage.BPP + 1] = theValue;
    }
    
    public static void setB(TexImage theTexImage, int theRow, int theColumn, byte theValue) {
    	theTexImage.image[(theRow * theTexImage.sx + theColumn) * theTexImage.BPP + 2] = theValue;
    }
    
    public static void safeTGA(TexImage theTexImage, String thePath) {
        FileOutputStream tgaFileOutputStream = null;
		try {
			tgaFileOutputStream = new FileOutputStream(thePath);
			theTexImage.SaveTGA(tgaFileOutputStream);
			tgaFileOutputStream.flush();
			tgaFileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static boolean loadDefaultTexture(TexImage theTexImage, String s, String s1, String s2)
    {
    	try {
			theTexImage.LoadTGA(s1+"/skin1o.tgb");
			return true;
    	} catch (Exception e) {
        	System.out.println("BmpUtils loadDefaultTexture");
        	createEmptyBmp(); // ensure that empty placeholder bitmap exists.
    		if (!BmpUtils.bmp8PalTo4TGA4(emptyBmpFile, s1, s2)) return false;
    		try {
    			theTexImage.LoadTGA(s2+"/skin1o.tgb");
    			System.out.println("BmpUtils empty TGA creation succeeded.");
    			return true;
    		} catch (Exception e2) {
    			System.out.println("BmpUtils empty TGA creation failed.");
    			e2.printStackTrace();
    			return false;
    		}
    	}
    }
    
    public static boolean bmp24PalTo4TGA4(String s, String s1, String s2)
    {
    	System.out.println("BmpUtils bmp24PalTo4TGA4 s=" + s + " s1=" + s1 + " s2=" + s2);
    	TexImage theTexImage = new TexImage();
    	SFSInputStream bmpSFSInputStream = null;
    	BMPLoader bmpLoader = new BMPLoader();
		try {
			loadDefaultTexture(theTexImage, s, s1, s2);
			bmpSFSInputStream = new SFSInputStream(s);
			bmpLoader.read(bmpSFSInputStream);
			while (bmpLoader.getWidth() > theTexImage.sx) {
		    	System.out.println("BmpUtils bmp24PalTo4TGA4 enlarging default skin (current size: " + theTexImage.sx + ", target size: " + bmpLoader.getWidth() + ").");
				scaleDouble(theTexImage);
			}
			int pixelDataIndex = 0;
			for (int row = bmpLoader.getHeight() -1 ; row >= 0; row--) {
				for (int column = 0; column < bmpLoader.getWidth(); column++) {
					int pixelData = bmpLoader.getIntData()[pixelDataIndex++];
					setR(theTexImage, bmpLoader.getHeight() - row - 1, column, BMPLoader.getR(pixelData));
					setG(theTexImage, bmpLoader.getHeight() - row - 1, column, BMPLoader.getG(pixelData));
					setB(theTexImage, bmpLoader.getHeight() - row - 1, column, BMPLoader.getB(pixelData));
				}
			}
			safeTGA(theTexImage, s2+"/skin1o.tgb");
			while (theTexImage.sx > 512) theTexImage.scaleHalf();
			safeTGA(theTexImage, s2+"/skin1o.tga");
			while (theTexImage.sx > 128) theTexImage.scaleHalf();
			safeTGA(theTexImage, s2+"/skin1p.tga");
			while (theTexImage.sx > 16) theTexImage.scaleHalf();
			safeTGA(theTexImage, s2+"/skin1q.tga");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }    

    private static final String emptyBmpPath = "PaintSchemes/Cache/placeholder/";
    private static final String emptyBmpFile = emptyBmpPath + "empty.bmp";

	
}