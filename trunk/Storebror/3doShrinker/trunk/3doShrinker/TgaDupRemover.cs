using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.IO;

namespace _3doShrinker
{
    class TgaDupRemover
    {
        private string modPath;
        private string rootPath;
        private Delegate protoMethod;
        private Delegate statusMethod;
        private ManualResetEvent stopThreads;
        public Dictionary<String, String> distinctTgaList;
        public AutoResetEvent scanFinished = new AutoResetEvent(false);

        public TgaDupRemover(
            string theModPath, 
            string theRootPath,
            Dictionary<String, String> theDistinctTgaList,
            Delegate theProtoMethod, 
            Delegate theStatusMethod, 
            ManualResetEvent theStopThreadsEvent)
        {
            this.modPath = theModPath;
            this.rootPath = theRootPath;
            this.distinctTgaList = theDistinctTgaList;
            this.protoMethod = theProtoMethod;
            this.statusMethod = theStatusMethod;
            this.stopThreads = theStopThreadsEvent;
            Thread scanThread = new Thread(new ThreadStart(this.scanTgaFilesThread));
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

        private void scanTgaFilesThread()
        {
            string[] theFiles = Directory.GetFiles(this.rootPath, "*.tga", SearchOption.AllDirectories);
            foreach (string theFilePath in theFiles)
            {
                if (this.stopThreads.WaitOne(0)) return;
                string relativePath = theFilePath.Remove(0, this.modPath.Length);
                string md5 = Shrinker.GetMD5HashFromFile(theFilePath);
                Shrinker.TheShrinkerStatus.StatusLine = Path.GetDirectoryName(relativePath);
                if (!this.distinctTgaList.ContainsKey(md5))
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
                        if (String.Compare(relativePath, this.distinctTgaList[md5], true) == 0)
                        {
                            Shrinker.TheShrinkerStatus.NumDistinct++;
                            Shrinker.TheShrinkerStatus.SizeDistinct += (new FileInfo(theFilePath)).Length;
                            this.ShowStatus();
                            continue; // we're removing duplicates from own base path, don't remove the distinct files themselves.
                        }
                    }
                    string backupFile = Directory.GetParent(this.modPath) + Shrinker.BackupFolder + relativePath;
                    string backupFolder = Path.GetDirectoryName(backupFile);
                    Shrinker.TheShrinkerStatus.NumDuplicate++;
                    Shrinker.TheShrinkerStatus.SizeDuplicate += (new FileInfo(theFilePath)).Length;
                    Directory.CreateDirectory(backupFolder);
                    this.changeTgaReferences(theFilePath, backupFolder, relativePath, this.distinctTgaList[md5]);
                    lock (Shrinker.BackupFileLock)
                    {
                        if (!File.Exists(backupFile)) File.Move(theFilePath, backupFile);
                    }
                    this.ShowStatus();
                    this.Proto(string.Format("Removed {0} (duplicate of {1})", relativePath, this.distinctTgaList[md5]));
                }
            }
            this.scanFinished.Set();
        }

        private bool changeTgaReferences(string tgaFile, string backupFolder, string relativePathDuplicate, string relativePathDistinct)
        {
            string matFolder = Path.GetDirectoryName(tgaFile);
            string tgaName = Path.GetFileName(tgaFile);
            string tgaNameDistinctRelative = Shrinker.RelativePath(Path.GetDirectoryName(relativePathDuplicate), Path.GetDirectoryName(relativePathDistinct)) + Path.GetFileName(relativePathDistinct);
            string[] filePaths = Directory.GetFiles(matFolder, "*.mat", SearchOption.TopDirectoryOnly);
            foreach (string theMatFile in filePaths)
            {
                if (!containsTgaReference(theMatFile, tgaName)) continue;
                if (!changeTgaReference(theMatFile, backupFolder, tgaName, tgaNameDistinctRelative)) return false;
            }
            return true;
        }

        private bool containsTgaReference(string theMatFile, string theTga)
        {
            bool bRet = false;
            StreamReader scanFile = new StreamReader(theMatFile);
            string scanLine;
            string searchString = theTga.ToLower();
            while ((scanLine = scanFile.ReadLine()) != null)
            {
                if (scanLine.ToLower().Contains(searchString))
                {
                    bRet = true;
                    break;
                }
            }
            scanFile.Close();
            return bRet;
        }


        private bool changeTgaReference(string matFile, string backupFolder, string tgaName, string tgaNameDistinctRelative)
        {
            string backupFile = backupFolder + "\\" + Path.GetFileName(matFile);
            lock (Shrinker.BackupFileLock)
            {
                if (!File.Exists(backupFile)) File.Move(matFile, backupFile);
            }
            StreamReader oldMat = new StreamReader(backupFile);
            StreamWriter newMat = File.CreateText(matFile);

            string scanLine;
            string newLine;
            string searchString = tgaName.ToLower();
            while ((scanLine = oldMat.ReadLine()) != null)
            {
                newLine = scanLine;
                if (scanLine.ToLower().Contains(searchString)) newLine = "  TextureName " + tgaNameDistinctRelative;
                newMat.WriteLine(newLine);
            }
            newMat.Close();
            oldMat.Close();
            return true;
        }

    }
}
