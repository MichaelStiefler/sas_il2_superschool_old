package com.maddox.il2.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.FObj;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.HomePath;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SFSInputStream;

public class GUIRoot extends GWindowRoot {

	public GUIRoot() {
		backgroundCountry = null;
	}

	public void setBackCountry(String s, String s1) {
		setBackCountry(s, s1, "");
	}

	public void setBackCountry(String s, String s1, String s2) {
		if (s == null || s1 == null) {
			backgroundCountry = null;
		} else {
			String s3 = "";
			if (isWide())
				s3 = "wide_";
			String s4 = RTSConf.cur.locale.getLanguage();
			String s5 = null;
			if (!"us".equalsIgnoreCase(s4) && !"en".equalsIgnoreCase(s4)) {
				s5 = "missions/" + s + "/" + s1 + "/" + s3 + "background_" + s2 + "_" + s4 + ".mat";
				if (!existSFSFile(s5))
					s5 = "missions/" + s + "/" + s1 + "/" + s3 + "background_" + s4 + ".mat";
				if (!existSFSFile(s5))
					s5 = null;
			}
			if (s5 == null)
				s5 = "missions/" + s + "/" + s1 + "/" + s3 + "background_" + s2 + ".mat";
			if (!existSFSFile(s5))
				s5 = "missions/" + s + "/" + s1 + "/" + s3 + "background.mat";
			if (!existSFSFile(s5))
				s5 = "missions/" + s + "/" + s1 + "/background.mat";
			Object obj = FObj.Get(s5);
			if (obj != null)
				backgroundCountry = GTexture.New(s5);
		}
	}

	protected boolean isWide() {
		float f = root.win.dx / root.win.dy;
		return f > 1.5F;
	}

	public void render() {
		if (RendersMain.getRenderFocus() == (Render)Actor.getByName("renderGUI")) {
			setCanvasColorWHITE();
			GTexture gtexture = null;
			if (backgroundCountry != null)
				gtexture = backgroundCountry;
			else if (background != null)
				gtexture = background;
			if (gtexture != null)
				if (win.dx / win.dy < 1.0F && gtexture.size.dx >= gtexture.size.dy) {
					float f = win.dy * (gtexture.size.dx / gtexture.size.dy);
					float f1 = gtexture.size.dx / 2.0F - f / 2.0F;
					draw(f1, 0.0F, f, win.dy, gtexture);
				} else {
					draw(0.0F, 0.0F, win.dx, win.dy, gtexture);
				}
		}
	}
	
	private void drawGUIBackgroundFromJpg() {
		String s = "";
		if (isWide())
			s = "wide_";
		String s1 = Config.cur.getBackgroundCountry(Config.cur.windowsUIBackground);
		String jpgImageFileName = "GUI/menu/" + s + "background_" + s1 + ".jpg";
		System.out.println("JPG Background: " + jpgImageFileName);
		SFSInputStream jpgFile;
		int jpgFileLength;
		byte jpgFileContents[] = null;
		try {
			jpgFile = new SFSInputStream(jpgImageFileName);
			jpgFileLength = jpgFile.available();
			jpgFileContents = new byte[jpgFileLength];
			jpgFile.read(jpgFileContents);
			jpgFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String bgPath = "PaintSchemes/" + s + "Temp";
		String tgaImageFileName = bgPath + "/" + s + "background_" + s1 + ".tga";
		String tgaImageFileNameShort = s + "background_" + s1 + ".tga";
		String matImageFileName = bgPath + "/" + s + "background_" + s1 + ".mat";
		
		int createSplashResult = Main3D.createSplash(jpgFileContents, tgaImageFileName);
		jpgFileContents = null;
		if (createSplashResult == 0) {
			try {	
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(matImageFileName, 0))));
				pw.println("[ClassInfo]");
				pw.println("  ClassName TMaterial");
				pw.println("[Layer0]");
				pw.println("  TextureName " + tgaImageFileNameShort);
				pw.println("  tfNoWriteZ 1");
				pw.println("  tfNoDegradation 1");
				pw.println("  tfMinLinear 1");
				pw.println("  tfMagLinear 1");
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			background = GTexture.New(matImageFileName);
		}
	}
	

	public void created() {
//		String s = "";
//		if (isWide())
//			s = "wide_";
//		String s1 = Config.cur.getBackgroundCountry(Config.cur.windowsUIBackground);
//		background = GTexture.New("GUI/" + s + "background_" + s1 + ".mat");
//		String s2 = null;
//		if (s2 == null)
//			s2 = "missions/" + s + "background_" + s1 + ".mat";
//		Object obj = null;
//		if (existSFSFile(s2))
//			obj = FObj.Get(s2);
//		if (obj != null)
//			background = GTexture.New(s2);
		
		this.drawGUIBackgroundFromJpg();
		super.created();
	}

	private boolean existSFSFile(String s) {
		try {
			SFSInputStream sfsinputstream = new SFSInputStream(s);
			sfsinputstream.close();
			return true;
		} catch (Exception exception) {
		}
		return false;
	}

	public void resolutionChanged() {
		super.resolutionChanged();
		created();
	}

	GTexture background;
	GTexture backgroundCountry;
}
