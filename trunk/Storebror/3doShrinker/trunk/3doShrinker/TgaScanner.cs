using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.IO;

namespace _3doShrinker
{
    class TgaScanner
    {
        private string basePath;
        private string rootPath;
        private Delegate protoMethod;
        private Delegate statusMethod;
        private ManualResetEvent stopThreads;
        public Dictionary<String, String> distinctTgaList = new Dictionary<String, String>();
        public Dictionary<String, String> tgaHashesList = new Dictionary<String, String>();
        public AutoResetEvent scanFinished = new AutoResetEvent(false);

        public TgaScanner(
            string theBasePath, 
            string theRootPath, 
            Delegate theProtoMethod, 
            Delegate theStatusMethod, 
            ManualResetEvent theStopThreadsEvent)
        {
            this.basePath = theBasePath;
            this.rootPath = theRootPath;
            this.protoMethod = theProtoMethod;
            this.statusMethod = theStatusMethod;
            this.stopThreads = theStopThreadsEvent;
            this.distinctTgaList.Clear();
            this.tgaHashesList.Clear();
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
                string relativePath = theFilePath.Remove(0, this.basePath.Length);
                string md5 = Shrinker.GetMD5HashFromFile(theFilePath);
                Shrinker.TheShrinkerStatus.StatusLine = Path.GetDirectoryName(relativePath);
                if (!this.distinctTgaList.ContainsKey(md5))
                {
                    this.distinctTgaList.Add(md5, relativePath);
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
                this.tgaHashesList.Add(relativePath.ToLower(), md5);
            }
            this.scanFinished.Set();
        }
    }
}
