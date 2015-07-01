using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.IO;

namespace _3doShrinker
{
    class MatDupRemover
    {
        private string modPath;
        private string rootPath;
        private Delegate protoMethod;
        private Delegate statusMethod;
        private ManualResetEvent stopThreads;
        private Dictionary<String, String> distinctMatList;
        private Dictionary<String, String> tgaHashesList;
        public AutoResetEvent scanFinished = new AutoResetEvent(false);

        public MatDupRemover(
            string theModPath, 
            string theRootPath, 
            Dictionary<String, String> theDistinctMatList, 
            Dictionary<String, String> theTgaHashesList, 
            Delegate theProtoMethod, 
            Delegate theStatusMethod, 
            ManualResetEvent theStopThreadsEvent)
        {
            this.modPath = theModPath;
            this.rootPath = theRootPath;
            this.distinctMatList = theDistinctMatList;
            this.tgaHashesList = theTgaHashesList;
            this.protoMethod = theProtoMethod;
            this.statusMethod = theStatusMethod;
            this.stopThreads = theStopThreadsEvent;
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
                string relativePath = theFilePath.Remove(0, this.modPath.Length);
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
                    //this.Proto(md5 + ": " + relativePath);
                    Shrinker.TheShrinkerStatus.NumDistinct++;
                    Shrinker.TheShrinkerStatus.SizeDistinct += (new FileInfo(theFilePath)).Length;
                    this.ShowStatus();
                }
                else
                {
                    if (Shrinker.IsSelfScanning)
                    {
                        if (String.Compare(relativePath, this.distinctMatList[md5], true) == 0)
                        {
                            Shrinker.TheShrinkerStatus.NumDistinct++;
                            Shrinker.TheShrinkerStatus.SizeDistinct += (new FileInfo(theFilePath)).Length;
                            this.ShowStatus();
                            continue; // we're removing duplicates from own base path, don't remove the distinct files themselves.
                        }
                    }
                    string backupFolder = Path.GetDirectoryName(Directory.GetParent(this.modPath) + Shrinker.BackupFolder + relativePath);
                    this.makeMatBasedOn(theFilePath, backupFolder, relativePath, this.distinctMatList[md5]);
                    Shrinker.TheShrinkerStatus.NumDuplicate++;
                    Shrinker.TheShrinkerStatus.SizeDuplicate += (new FileInfo(theFilePath)).Length;
                    this.ShowStatus();
                    this.Proto(string.Format("Linked {0} to {1}", relativePath, this.distinctMatList[md5]));
                }
            }
            this.scanFinished.Set();
        }

        private string tgaInMatHash(string matFile)
        {
            string retVal = null;
            string matPath = Path.GetDirectoryName(matFile) + "\\";
            string matPathRelative = matPath.Remove(0, this.modPath.Length);

            StreamReader scanFile = new StreamReader(matFile);
            string scanLine;
            while ((scanLine = scanFile.ReadLine()) != null)
            {
                scanLine = scanLine.Trim().ToLower();
                if (scanLine.StartsWith("texturename"))
                {
                    if (scanLine.Length < 12) continue;
                    string tgaFileNoPath = scanLine.Remove(0, 12).Trim();
                    string tgaFileRaw = matPathRelative + tgaFileNoPath;
                    string tgaFileFullPath = Path.GetFullPath(tgaFileRaw);
                    string tgaRootPath = Path.GetFullPath("\\");
                    string tgaFile = "\\" + tgaFileFullPath.Remove(0, tgaRootPath.Length);
                    string hashedTga;
                    if (File.Exists(matPath + tgaFileNoPath))
                    {
                        hashedTga = Shrinker.GetMD5HashFromFile(matPath + tgaFileNoPath);
                    }
                    else if (this.tgaHashesList.ContainsKey(tgaFile)) 
                    {
                        hashedTga = this.tgaHashesList[tgaFile];
                    }
                    else // The texture being referred to can't be found, use a "ghost" hash instead
                    {
                        //hashedTga = Shrinker.RandomHash();#
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
            scanFile.Close();
            return retVal;
        }

        private void makeMatBasedOn(string matFile, string backupFolder, string relativePathDuplicate, string relativePathDistinct)
        {
            string backupFile = backupFolder + "\\" + Path.GetFileName(matFile);
            Directory.CreateDirectory(backupFolder);
            lock (Shrinker.BackupFileLock)
            {
                if (!File.Exists(backupFile)) File.Move(matFile, backupFile);
            }
            string matNameDistinctRelative = Shrinker.RelativePath(Path.GetDirectoryName(relativePathDuplicate), Path.GetDirectoryName(relativePathDistinct)) + Path.GetFileName(relativePathDistinct);
            //Debug.WriteLine(string.Format("making .mat {0} based on {1}", matFile, matNameDistinctRelative));
            StreamWriter newMat = File.CreateText(matFile);
            newMat.WriteLine("[ClassInfo]");
            newMat.WriteLine("  ClassName TMaterial");
            newMat.Write("  BasedOn ");
            newMat.WriteLine(matNameDistinctRelative);
            newMat.Close();
        }
    }
}
