using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.IO;

namespace _3doShrinker
{
    class MatScanner
    {
        private string basePath;
        private string rootPath;
        private Delegate protoMethod;
        private Delegate statusMethod;
        private ManualResetEvent stopThreads;
        public Dictionary<String, String> distinctMatList = new Dictionary<String, String>();
        private Dictionary<String, String> tgaHashesList;
        public AutoResetEvent scanFinished = new AutoResetEvent(false);

        public MatScanner(
            string theBasePath, 
            string theRootPath, 
            Dictionary<String, String> theTgaHashesList, 
            Delegate theProtoMethod, 
            Delegate theStatusMethod, 
            ManualResetEvent theStopThreadsEvent)
        {
            this.basePath = theBasePath;
            this.rootPath = theRootPath;
            this.tgaHashesList = theTgaHashesList;
            this.protoMethod = theProtoMethod;
            this.statusMethod = theStatusMethod;
            this.stopThreads = theStopThreadsEvent;
            this.distinctMatList.Clear();
            Thread scanThread = new Thread(new ThreadStart(this.scanMatFilesThread));
            scanThread.IsBackground = true;
            scanThread.Priority = ThreadPriority.Lowest;
            scanThread.Start();
        }

        private void Proto(string theLine)
        {
            this.protoMethod.DynamicInvoke(theLine);
        }

        private void ShowStatus()
        {
            this.statusMethod.DynamicInvoke();
        }

        private void scanMatFilesThread()
        {
            string[] theFiles = Directory.GetFiles(this.rootPath, "*.mat", SearchOption.AllDirectories);
            foreach (string theFilePath in theFiles)
            {
                if (this.stopThreads.WaitOne(0)) return;
                string relativePath = theFilePath.Remove(0, this.basePath.Length);
                string md5Mat = Shrinker.GetMD5HashFromFile(theFilePath);
                string md5TgaInMat = tgaInMatHash(theFilePath);
                string md5;
                if (md5TgaInMat == null)
                    md5 = md5Mat;
                else
                    md5 = Shrinker.CombineHashes(md5Mat, md5TgaInMat);
                Shrinker.TheShrinkerStatus.StatusLine = Path.GetDirectoryName(relativePath);
                if (!this.distinctMatList.ContainsKey(md5))
                {
                    this.distinctMatList.Add(md5, relativePath);
                    this.Proto(md5 + ": " + relativePath);
                    Shrinker.TheShrinkerStatus.NumDistinct++;
                    Shrinker.TheShrinkerStatus.SizeDistinct += (new FileInfo(theFilePath)).Length;
                    this.ShowStatus();
                }
                else
                {
                    Shrinker.TheShrinkerStatus.NumDuplicate++;
                    Shrinker.TheShrinkerStatus.SizeDuplicate += (new FileInfo(theFilePath)).Length;
                    this.ShowStatus();
                }
            }
            this.scanFinished.Set();
        }

        private string tgaInMatHash(string matFile)
        {
            string retVal = null;
            string matPath = Path.GetDirectoryName(matFile) + "\\";
            string matPathRelative = matPath.Remove(0, this.basePath.Length);
            StreamReader scanFile = new StreamReader(matFile);
            string scanLine;
            while ((scanLine = scanFile.ReadLine()) != null)
            {
                scanLine = scanLine.Trim().ToLower();
                if (scanLine.StartsWith("texturename"))
                {
                    if (scanLine.Length < 12) continue;

                    // Create an absolute path "below 3do folder" for the file, strip ".." and the like
                    string tgaFileRaw = matPathRelative + scanLine.Remove(0, 12).Trim();
                    string tgaFileFullPath = Path.GetFullPath(tgaFileRaw);
                    string tgaRootPath = Path.GetFullPath("\\");
                    string tgaFile = "\\" + tgaFileFullPath.Remove(0, tgaRootPath.Length);
                    tgaFile = tgaFile.ToLower();
                    // ---
                    string hashedTga;
                    if (this.tgaHashesList.ContainsKey(tgaFile)) 
                    {
                        hashedTga = this.tgaHashesList[tgaFile];
                    }
                    else // The texture being referred to can't be found, use a "ghost" hash instead
                    {
                        //hashedTga = Shrinker.RandomHash(); // use a random hash so the file is distinct
                        hashedTga = Shrinker.GetMD5HashFromFile(tgaFile);
                        //this.Proto("### MISSING FILE ### " + tgaFile);
                        continue;
                    }
                    if (retVal == null)
                        retVal = hashedTga;
                    else
                        retVal = Shrinker.CombineHashes(retVal, hashedTga);
                }
            }
            return retVal;
        }
    }
}
