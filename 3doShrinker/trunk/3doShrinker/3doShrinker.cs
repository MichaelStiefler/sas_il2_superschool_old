using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Threading;
using System.Security.Cryptography;
#if DEBUG
using System.Diagnostics;
#endif

namespace _3doShrinker
{
    public partial class Shrinker : Form
    {
        private string basePath = "";
        private string modPath = "";
        private const int MAX_PROTO = 29;
        private ManualResetEvent stopThreads = new ManualResetEvent(false);

        //private List<String> baseFileMD5 = new List<String>();
        //private List<String> baseFileRelativePath = new List<String>();
        //private List<File3do> fileEquivalents = new List<File3do>();
        private Dictionary<String, String> distinctTgaList = new Dictionary<String, String>();
        private Dictionary<String, List<String>> baseMatList = new Dictionary<String, List<String>>();
        private Dictionary<String, String> hashListAbsolut = new Dictionary<String, String>();
        private Dictionary<String, List<String>> tgaFilesInMat = new Dictionary<String, List<String>>();

        private Object distinctTgaListLockObject = new Object();
        public Dictionary<String, String> DistinctTgaList
        {
            get
            {
                return this.distinctTgaList;
            }
            set
            {
                lock (distinctTgaListLockObject) this.distinctTgaList = value;
            }
        }

        private Object baseMatListLockObject = new Object();
        public Dictionary<String, List<String>> BaseMatList
        {
            get
            {
                return this.baseMatList;
            }
            set
            {
                lock (baseMatListLockObject) this.baseMatList = value;
            }
        }

        private Object hashListAbsolutLockObject = new Object();
        public Dictionary<String, String> HashListAbsolut
        {
            get
            {
                return this.hashListAbsolut;
            }
            set
            {
                lock (hashListAbsolutLockObject) this.hashListAbsolut = value;
            }
        }

        private Object tgaFilesInMatLockObject = new Object();
        public Dictionary<String, List<String>> TgaFilesInMat
        {
            get
            {
                return this.tgaFilesInMat;
            }
            set
            {
                lock (tgaFilesInMatLockObject) this.tgaFilesInMat = value;
            }
        }

        private FlickerFreeListBox listBoxProto;

        private Thread baseWorkerThread;

        private string currentFolder = "";

        private int numDistinct = 0;
        private int numDuplicate = 0;
        private int numDuplicateRemoved = 0;
        private long sizeDistinct = 0;
        private long sizeDuplicate = 0;
        private long sizeDuplicateRemoved = 0;

        public string CurrentFolder
        {
            get
            {
                return this.currentFolder;
            }
            set
            {
                Monitor.Enter(this);
                this.currentFolder = value;
                Monitor.Exit(this);
            }
        }

        private int comparisonThreadsRunning = 0;
        public int ComparisonThreadsRunning
        {
            get
            {
                return this.comparisonThreadsRunning;
            }
            set
            {
                Monitor.Enter(this);
                this.comparisonThreadsRunning = value;
                Monitor.Exit(this);
            }
        }
        private string hashCacheFile() { return this.basePath + "\\hash.cache"; }

        //private struct File3do {
        //    public string relativePath;
        //    public string equivalentPath;
        //}

        public Shrinker()
        {
            InitializeComponent();
            this.DoubleBuffered = true;
            // 
            // listBoxProto
            // 
            this.listBoxProto = new FlickerFreeListBox();
            this.listBoxProto.FormattingEnabled = true;
            this.listBoxProto.Location = new System.Drawing.Point(12, 97);
            this.listBoxProto.Name = "listBoxProto";
            this.listBoxProto.Size = new System.Drawing.Size(661, 381);
            this.listBoxProto.TabIndex = 5;
            this.listBoxProto.Font = new System.Drawing.Font("Courier New", 8);
            this.Controls.Add(this.listBoxProto);

#if DEBUG
            this.folderBrowserDialog3do.SelectedPath = "U:\\IL2\\3DoShrinkTest\\3DO";
            this.textBox3doBase.Text = "U:\\IL2\\Extracted_SFS_IL2_412\\3DO";
            this.basePath = "U:\\IL2\\Extracted_SFS_IL2_412\\3DO";
            this.textBox3doMod.Text = "U:\\IL2\\3DoShrinkTest\\3DO";
            this.modPath = "U:\\IL2\\3DoShrinkTest\\3DO";
            this.buttonScanBase.Enabled = true;
            if (File.Exists(this.hashCacheFile())) this.buttonLoadBase.Enabled = true;
#endif
        }

        private delegate void DoProto(string theLine);
        private void Proto(string theLine)
        {
            if (stopThreads.WaitOne(0)) return;
            if (this.InvokeRequired)
            {
                // we were called on a worker thread
                // marshal the call to the user interface thread
                this.Invoke(new DoProto(Proto),
                            new object[] { theLine });
                return;
            }

            // this code can only be reached
            // by the user interface thread
            this.listBoxProto.SuspendLayout();
            if (listBoxProto.Items.Count >= MAX_PROTO)
            {
                listBoxProto.Items.RemoveAt(0);
            }
            this.listBoxProto.TopIndex = this.listBoxProto.Items.Add(theLine);
            this.listBoxProto.ResumeLayout();
#if DEBUG
            System.Diagnostics.Debug.WriteLine(theLine);
#endif
        }

        private delegate void DoSetDistinctFiles(int theNumber);
        private void SetDistinctFiles(int theNumber)
        {
            if (stopThreads.WaitOne(0)) return;
            if (this.InvokeRequired)
            {
                // we were called on a worker thread
                // marshal the call to the user interface thread
                this.Invoke(new DoSetDistinctFiles(SetDistinctFiles),
                            new object[] { theNumber });
                return;
            }

            // this code can only be reached
            // by the user interface thread
            this.toolStripStatusLabelDistinct.Text = theNumber.ToString();
        }

        private delegate void DoSetDuplicateFiles(int theNumber);
        private void SetDuplicateFiles(int theNumber)
        {
            if (stopThreads.WaitOne(0)) return;
            if (this.InvokeRequired)
            {
                // we were called on a worker thread
                // marshal the call to the user interface thread
                this.Invoke(new DoSetDuplicateFiles(SetDuplicateFiles),
                            new object[] { theNumber });
                return;
            }

            // this code can only be reached
            // by the user interface thread
            this.toolStripStatusLabelDuplicate.Text = theNumber.ToString();
        }

        private delegate void DoSetDistinctSize(long theNumber);
        private void SetDistinctSize(long theNumber)
        {
            if (stopThreads.WaitOne(0)) return;
            if (this.InvokeRequired)
            {
                // we were called on a worker thread
                // marshal the call to the user interface thread
                this.Invoke(new DoSetDistinctSize(SetDistinctSize),
                            new object[] { theNumber });
                return;
            }

            // this code can only be reached
            // by the user interface thread
            this.toolStripStatusLabelDistinctSize.Text = string.Format("({0} MB)", theNumber / 1024 / 1024);
        }

        private delegate void DoSetDuplicateSize(long theNumber);
        private void SetDuplicateSize(long theNumber)
        {
            if (stopThreads.WaitOne(0)) return;
            if (this.InvokeRequired)
            {
                // we were called on a worker thread
                // marshal the call to the user interface thread
                this.Invoke(new DoSetDuplicateSize(SetDuplicateSize),
                            new object[] { theNumber });
                return;
            }

            // this code can only be reached
            // by the user interface thread
            this.toolStripStatusLabelDuplicateSize.Text = string.Format("({0} MB)", theNumber / 1024 / 1024);
        }

        private delegate void DoSetCurrentFolder(string theFolder);
        private void SetStatus(string theFolder)
        {
            if (theFolder.CompareTo(this.CurrentFolder) == 0) return;
            if (stopThreads.WaitOne(0)) return;
            if (this.InvokeRequired)
            {
                // we were called on a worker thread
                // marshal the call to the user interface thread
                this.Invoke(new DoSetCurrentFolder(SetStatus),
                            new object[] { theFolder });
                return;
            }

            // this code can only be reached
            // by the user interface thread
            this.toolStripStatusLabelFolder.Text = theFolder;
            this.CurrentFolder = theFolder;
        }

        private delegate void DoSetControlsEnabled(bool enableControl, params Control[] theControls);
        private void SetControlsEnabled(bool enableControl, params Control[] theControls)
        {
            if (stopThreads.WaitOne(0)) return;
            if (this.InvokeRequired)
            {
                // we were called on a worker thread
                // marshal the call to the user interface thread
                this.Invoke(new DoSetControlsEnabled(SetControlsEnabled),
                            new object[] { enableControl, theControls });
                return;
            }

            // this code can only be reached
            // by the user interface thread
            foreach (Control c in theControls) c.Enabled = enableControl;
        }

        private void buttonSelectBaseFolder_Click(object sender, EventArgs e)
        {
            if (this.folderBrowserDialog3do.ShowDialog() == DialogResult.OK)
            {
                this.basePath = this.folderBrowserDialog3do.SelectedPath;
            }
            this.textBox3doBase.Text = this.basePath;
            if (this.modPath != "") this.buttonScanBase.Enabled = true;
            if (File.Exists(this.hashCacheFile())) this.buttonLoadBase.Enabled = true;
        }

        private void buttonSelectModFolder_Click(object sender, EventArgs e)
        {
            if (this.folderBrowserDialog3do.ShowDialog() == DialogResult.OK)
            {
                this.modPath = this.folderBrowserDialog3do.SelectedPath;
            }
            this.textBox3doMod.Text = this.modPath;
            if (this.basePath != "") this.buttonScanBase.Enabled = true;
        }

        private void buttonScanBase_Click(object sender, EventArgs e)
        {
            this.SetControlsEnabled(false, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            this.numDistinct = this.numDuplicate = 0;
            this.sizeDistinct = this.sizeDuplicate = 0;
            this.stopThreads.Reset();
            //this.baseFileMD5.Clear();
            //this.baseFileRelativePath.Clear();
            this.DistinctTgaList.Clear();
            this.BaseMatList.Clear();
            this.HashListAbsolut.Clear();
            this.TgaFilesInMat.Clear();
            this.baseWorkerThread = new Thread(new ThreadStart(this.scanBaseFolder));
            this.baseWorkerThread.Start();
        }

        private void buttonStopAction_Click(object sender, EventArgs e)
        {
            stopThreads.Set();
            Thread.Sleep(100);
            if (this.baseWorkerThread != null) this.baseWorkerThread.Abort();
            this.baseWorkerThread = null;
            this.buttonScanBase.Enabled = true;
            this.SetControlsEnabled(true, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase);
        }

        private void buttonLoadBase_Click(object sender, EventArgs e)
        {
            this.SetControlsEnabled(false, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            this.numDistinct = this.numDuplicate = 0;
            this.sizeDistinct = this.sizeDuplicate = 0;
            this.stopThreads.Reset();
            //this.baseFileMD5.Clear();
            //this.baseFileRelativePath.Clear();
            this.DistinctTgaList.Clear();
            this.BaseMatList.Clear();
            this.TgaFilesInMat.Clear();
            this.baseWorkerThread = new Thread(new ThreadStart(this.loadBaseFiles));
            this.baseWorkerThread.Start();
        }

        private void buttonRemoveMatDups_Click(object sender, EventArgs e)
        {
            this.numDistinct = this.numDuplicate = this.numDuplicateRemoved = 0;
            this.sizeDistinct = this.sizeDuplicate = this.sizeDuplicateRemoved = 0;
            this.SetControlsEnabled(false, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            this.stopThreads.Reset();
            this.baseWorkerThread = new Thread(new ThreadStart(this.removeMatDups));
            this.baseWorkerThread.Start();
        }

        private void buttonRemoveTgaDups_Click(object sender, EventArgs e)
        {
            this.numDistinct = this.numDuplicate = this.numDuplicateRemoved = 0;
            this.sizeDistinct = this.sizeDuplicate = this.sizeDuplicateRemoved = 0;
            this.SetControlsEnabled(false, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            this.stopThreads.Reset();
            this.baseWorkerThread = new Thread(new ThreadStart(this.removeTgaDups));
            this.baseWorkerThread.Start();
        }

        private string GetMD5HashFromFile(string fileName)
        {
            if (this.HashListAbsolut.ContainsKey(fileName)) return this.HashListAbsolut[fileName];
            FileStream file = new FileStream(fileName, FileMode.Open, FileAccess.Read, FileShare.Read);
            MD5 md5 = new MD5CryptoServiceProvider();
            byte[] retVal = md5.ComputeHash(file);
            file.Close();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < retVal.Length; i++)
            {
                sb.Append(retVal[i].ToString("x2"));
            }
            return sb.ToString();
        }

        private void addToBaseTgaList(string[] theFiles)
        {
            ComparisonThreadsRunning++;
            foreach (string theFilePath in theFiles)
            {
                if (stopThreads.WaitOne(0)) return;
                string relativePath = theFilePath.Remove(0, this.basePath.Length);
                string md5 = GetMD5HashFromFile(theFilePath);
                if (!this.DistinctTgaList.ContainsKey(md5))
                {
                    this.DistinctTgaList.Add(md5, relativePath);
                    this.Proto(md5 + ": " + relativePath);
                    this.numDistinct++;
                    this.SetDistinctFiles(this.numDistinct);
                    this.sizeDistinct += (new FileInfo(theFilePath)).Length;
                    this.SetDistinctSize(this.sizeDistinct);
                }
                else
                {
                    this.numDuplicate++;
                    this.SetDuplicateFiles(this.numDuplicate);
                    this.sizeDuplicate += (new FileInfo(theFilePath)).Length;
                    this.SetDuplicateSize(this.sizeDuplicate);
                }
                this.SetStatus(Path.GetDirectoryName(relativePath));
            }
            ComparisonThreadsRunning--;
        }

        private void addToBaseMatList(string[] theFiles)
        {
            ComparisonThreadsRunning++;
            foreach (string theFilePath in theFiles)
            {
                if (stopThreads.WaitOne(0)) return;
                string relativePath = theFilePath.Remove(0, this.basePath.Length);
                string md5 = this.GetMD5HashFromFile(theFilePath);
                bool bIsDistinct = true;

                if (this.BaseMatList.ContainsKey(md5))
                {
                    List<string> theComparableMatFiles = new List<string>(this.BaseMatList[md5]);
                    foreach (string theComparableMatFile in theComparableMatFiles)
                    {
                        if (isSameTextures(this.basePath + relativePath, this.basePath + theComparableMatFile))
                        {
                            bIsDistinct = false;
                            break;
                        }
                    }
                }

                if (bIsDistinct)
                {
                    if (!this.BaseMatList.ContainsKey(md5))
                        this.BaseMatList.Add(md5, new List<String>());
                    this.BaseMatList[md5].Add(relativePath);
                    this.Proto(md5 + ": " + relativePath);
                    this.numDistinct++;
                    this.SetDistinctFiles(this.numDistinct);
                    this.sizeDistinct += (new FileInfo(theFilePath)).Length;
                    this.SetDistinctSize(this.sizeDistinct);
                }
                else
                {
                    this.numDuplicate++;
                    this.SetDuplicateFiles(this.numDuplicate);
                    this.sizeDuplicate += (new FileInfo(theFilePath)).Length;
                    this.SetDuplicateSize(this.sizeDuplicate);
                }
                this.SetStatus(Path.GetDirectoryName(relativePath));
            }
            ComparisonThreadsRunning--;
        }

        private void scanBaseFolder()
        {
            //if (File.Exists(this.hashCacheFile())) File.Delete(this.hashCacheFile());
            try
            {
                this.SetStatus("Scanning base Folder...");
                string[] subDirs = Directory.GetDirectories(this.basePath);
                foreach (string subDir in subDirs)
                {
                    string[] filePaths = Directory.GetFiles(subDir, "*.mat",
                                                 SearchOption.AllDirectories);
                    Thread thread = new Thread(delegate() { this.addToBaseMatList(filePaths); });
                    thread.Start();
                }


                //string[] filePaths = Directory.GetFiles(this.basePath, "*.mat",
                //                             SearchOption.AllDirectories);
                //this.addToBaseMatList(filePaths);
                if (stopThreads.WaitOne(0)) return;

                subDirs = Directory.GetDirectories(this.basePath);
                foreach (string subDir in subDirs)
                {
                    string[] filePaths = Directory.GetFiles(subDir, "*.tga",
                                                 SearchOption.AllDirectories);
                    Thread thread = new Thread(delegate() { this.addToBaseTgaList(filePaths); });
                    thread.Start();
                }




                //filePaths = Directory.GetFiles(this.basePath, "*.tga",
                //                             SearchOption.AllDirectories);
                //this.addToBaseTgaList(filePaths);
                // 


                do
                {
                    Thread.Sleep(1000);
                } while (this.ComparisonThreadsRunning > 0);



                this.SetStatus("Creating Hash Cache");
                StreamWriter cacheFile = File.CreateText(this.hashCacheFile());
                cacheFile.AutoFlush = false;
                cacheFile.WriteLine(string.Format("{0};{1};{2};{3}", this.numDistinct, this.sizeDistinct, this.numDuplicate, this.sizeDuplicate));
                foreach (var distinctMat in this.BaseMatList)
                {
                    foreach (string theMatFile in distinctMat.Value)
                    {
                        cacheFile.WriteLine(distinctMat.Key + ";" + theMatFile);
                    }
                }
                cacheFile.WriteLine("##########");
                foreach (var distinctTga in this.DistinctTgaList)
                {
                    cacheFile.WriteLine(distinctTga.Key + ";" + distinctTga.Value);
                }
                cacheFile.Close();
            }
            catch (Exception e)
            {
                this.Proto("### ERROR in scanBaseFolder() ###");
                this.SetStatus("Error in scanBaseFolder.");
                Debug.WriteLine(e.ToString());
            }
            finally
            {
                this.Proto("### DONE ###");
                this.SetStatus("Done.");
                this.SetControlsEnabled(true, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            }
        }

        private void loadBaseFiles()
        {
            this.Proto("Loading Base Hash Cache...");
            StreamReader cacheFile = new StreamReader(this.hashCacheFile());
            string cacheFileLine = cacheFile.ReadLine();
            string[] cacheFileLineTokens = cacheFileLine.Split(";".ToCharArray(), 4);
            int.TryParse(cacheFileLineTokens[0], out this.numDistinct);
            long.TryParse(cacheFileLineTokens[1], out this.sizeDistinct);
            int.TryParse(cacheFileLineTokens[2], out this.numDuplicate);
            long.TryParse(cacheFileLineTokens[3], out this.sizeDuplicate);
            this.SetDistinctFiles(this.numDistinct);
            this.SetDistinctSize(this.sizeDistinct);
            this.SetDuplicateFiles(this.numDuplicate);
            this.SetDuplicateSize(this.sizeDuplicate);

            this.DistinctTgaList.Clear();
            while ((cacheFileLine = cacheFile.ReadLine()) != null)
            {
                if (cacheFileLine.CompareTo("##########") == 0) break;
                cacheFileLineTokens = cacheFileLine.Split(";".ToCharArray(), 2);
                this.DistinctTgaList.Add(cacheFileLineTokens[0], cacheFileLineTokens[1]);
            }
            while ((cacheFileLine = cacheFile.ReadLine()) != null)
            {
                cacheFileLineTokens = cacheFileLine.Split(";".ToCharArray(), 2);
                if (!this.BaseMatList.ContainsKey(cacheFileLineTokens[0]))
                    this.BaseMatList.Add(cacheFileLineTokens[0], new List<String>());
                this.BaseMatList[cacheFileLineTokens[0]].Add(cacheFileLineTokens[1]);
            }
            cacheFile.Close();
            this.Proto("### DONE ###");
            this.SetControlsEnabled(true, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
        }

        private void removeMatDups()
        {
            try
            {
                this.SetStatus("Scanning mod Folder...");
                string[] filePaths = Directory.GetFiles(this.modPath, "*.mat",
                                             SearchOption.AllDirectories);
                this.removeMatDuplicateFiles(filePaths);
            }
            catch (Exception e)
            {
                this.Proto("### ERROR in removeMatDups() ###");
                this.SetStatus("Error in removeMatDups.");
                Debug.WriteLine(e.ToString());
            }
            finally
            {
                this.Proto(this.numDuplicateRemoved + " .mat (" + this.sizeDuplicateRemoved / 1024 + "kb) removed.");
                this.Proto("### DONE ###");
                this.SetStatus("Done.");
                this.SetControlsEnabled(true, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            }
        }

        private void removeMatDuplicateFiles(string[] theFiles)
        {
            foreach (string theFilePath in theFiles)
            {
                foreach (TraceListener tl in Debug.Listeners) tl.Flush();
                //Debug.WriteLine(string.Format("removeMatDuplicateFiles {0}", theFilePath));
                if (stopThreads.WaitOne(0)) return;
                string relativePath = theFilePath.Remove(0, this.modPath.Length);
                this.SetStatus(Path.GetDirectoryName(relativePath));
                string md5 = GetMD5HashFromFile(theFilePath);
                if (this.DistinctTgaList.ContainsKey(md5))
                {
                    //Debug.WriteLine(string.Format("is in base list: {0}", this.DistinctTgaList[md5]));
                    this.numDuplicate++;
                    this.sizeDuplicate += (new FileInfo(theFilePath)).Length;
                    this.SetDuplicateSize(this.sizeDuplicate);
                    this.SetDuplicateFiles(numDuplicate);
                    string backupFile = Directory.GetParent(this.modPath) + "\\3do_backup" + relativePath;
                    string backupFolder = Path.GetDirectoryName(backupFile);
                    //Debug.WriteLine(string.Format("backupFile: {0} backupFolder: {1}", backupFile, backupFolder));
                    if (File.Exists(basePath + relativePath))
                    {
                        if (this.GetMD5HashFromFile(basePath + relativePath).CompareTo(md5) == 0)
                        {
                            //Debug.WriteLine(string.Format("is identical copy of: {0}", basePath + relativePath));
                            Directory.CreateDirectory(backupFolder);
                            File.Move(theFilePath, backupFile);
                            this.numDuplicateRemoved++;
                            this.sizeDuplicateRemoved += (new FileInfo(theFilePath)).Length;
                            this.Proto("Removed " + relativePath + " (identical copy in identical base folder)");
                            continue;
                        }
                    }

                    if (isSameTextures(theFilePath, basePath + this.DistinctTgaList[md5]))
                    {
                        this.numDuplicateRemoved++;
                        this.sizeDuplicateRemoved += (new FileInfo(theFilePath)).Length;
                        this.makeMatBasedOn(theFilePath, backupFolder, relativePath, this.DistinctTgaList[md5]);
                        this.Proto(relativePath + " linked to " + this.DistinctTgaList[md5]);
                    }
                    else
                    {
                        this.Proto(relativePath + " contains links to different textures than " + this.DistinctTgaList[md5]);
                    }

                    //if (isInBinaryMeshes(theFilePath))
                    //{
                    //    Debug.WriteLine(string.Format("is referenced by binary Mesh"));
                    //    this.Proto("Can't Remove " + relativePath + " (used in binary Mesh)");
                    //    continue;
                    //}
                    //Directory.CreateDirectory(backupFolder);
                    //Debug.WriteLine(string.Format("Changing references in .mat files in folder of {0} from {1} to {2}", theFilePath, relativePath, this.distinctFileList[md5]));
                    //if (changeMatReferences(theFilePath, backupFolder, relativePath, this.distinctFileList[md5]))
                    //{
                    //    this.numDuplicateRemoved++;
                    //    this.sizeDuplicateRemoved += (new FileInfo(theFilePath)).Length;
                    //    File.Move(theFilePath, backupFile);
                    //    this.Proto("Removed " + relativePath + " ( copy of " + this.distinctFileList[md5] + " )");
                    //}
                    //else
                    //{
                    //    this.Proto("Error while replacing mesh references to " + relativePath);
                    //}
                }
                else
                {
                    //Debug.WriteLine(string.Format("is distinct File"));
                    this.numDistinct++;
                    this.sizeDistinct += (new FileInfo(theFilePath)).Length;
                    this.SetDistinctSize(this.sizeDistinct);
                    this.SetDistinctFiles(this.numDistinct);
                }
            }
        }

        private void makeMatBasedOn(string matFile, string backupFolder, string relativePathDuplicate, string relativePathDistinct)
        {
            string backupFile = backupFolder + "\\" + Path.GetFileName(matFile);
            Directory.CreateDirectory(backupFolder);
            if (!File.Exists(backupFile)) File.Move(matFile, backupFile);
            string matNameDistinctRelative = this.RelativePath(Path.GetDirectoryName(relativePathDuplicate), Path.GetDirectoryName(relativePathDistinct)) + "\\" + Path.GetFileName(relativePathDistinct);
            //Debug.WriteLine(string.Format("making .mat {0} based on {1}", matFile, matNameDistinctRelative));
            StreamWriter newMat = File.CreateText(matFile);
            newMat.WriteLine("[ClassInfo]");
            newMat.WriteLine("  ClassName TMaterial");
            newMat.Write("  BasedOn ");
            newMat.WriteLine(matNameDistinctRelative);
            newMat.Close();
        }

        private bool isSameTextures(string matFile, string distinctFile)
        {
            //Debug.WriteLine(string.Format("isSameTextures checking {0}", matFile));
            bool bRet = true;
            string matPath = Path.GetDirectoryName(matFile) + "\\";
            string distinctPath = Path.GetDirectoryName(distinctFile) + "\\";
            string distinctPathRelative = distinctPath.Remove(0, this.basePath.Length);
            if (!this.TgaFilesInMat.ContainsKey(matFile))
            {
                this.TgaFilesInMat.Add(matFile, new List<String>());
                StreamReader scanFile = new StreamReader(matFile);
                string scanLine;
                while ((scanLine = scanFile.ReadLine()) != null)
                {
                    scanLine = scanLine.Trim().ToLower();
                    if (scanLine.StartsWith("texturename"))
                    {
                        if (scanLine.Length < 12) continue;
                        string tgaFile = scanLine.Remove(0, 12).Trim();
                        this.TgaFilesInMat[matFile].Add(tgaFile);
                    }
                }
                scanFile.Close();
            }

            foreach (string tgaFile in this.TgaFilesInMat[matFile])
            {
                if (File.Exists(distinctPath + tgaFile)
                    && File.Exists(matPath + tgaFile)
                    && GetMD5HashFromFile(distinctPath + tgaFile).CompareTo(GetMD5HashFromFile(matPath + tgaFile)) == 0)
                    continue;
                bRet = false;
                break;
            }
            return bRet;
        }


        //private bool changeMatReferences(string matFile, string backupFolder, string relativePathDuplicate, string relativePathDistinct)
        //{
        //    string meshFolder = Path.GetDirectoryName(matFile);
        //    string matName = Path.GetFileNameWithoutExtension(matFile);
        //    string matNameDistinctRelative = this.RelativePath(Path.GetDirectoryName(relativePathDuplicate), Path.GetDirectoryName(relativePathDistinct)) + "\\" + Path.GetFileNameWithoutExtension(relativePathDistinct);
        //    Debug.WriteLine(string.Format("changeMatReferences meshFolder={0} matName={1} matNameDistinctRelative={2}", meshFolder, matName, matNameDistinctRelative));
        //    string[] filePaths = Directory.GetFiles(meshFolder, "*.msh", SearchOption.TopDirectoryOnly);
        //    foreach (string theMeshFile in filePaths)
        //    {
        //        Debug.WriteLine(string.Format("checking Mesh file {0}", theMeshFile));
        //        if (isBinaryMesh(theMeshFile)) continue;
        //        Debug.WriteLine(string.Format("{0} is not a binary Mesh", theMeshFile));
        //        if (!containsMatReference(theMeshFile, matName)) continue;
        //        Debug.WriteLine(string.Format("{0} contains a reference to {1}", theMeshFile, matName));
        //        if (!changeMatReference(theMeshFile, backupFolder, matName, matNameDistinctRelative)) return false;
        //        Debug.WriteLine(string.Format("references to {0} in {1} changed to {2}", matName, theMeshFile, matNameDistinctRelative));
        //    }
        //    return true;
        //}

        //private bool isBinaryMesh(string theMeshFile)
        //{
        //    Debug.WriteLine(string.Format("Checking whether {0} is a binary Mesh", theMeshFile));
        //    StreamReader scanFile = new StreamReader(theMeshFile);
        //    bool bRet = true;
        //    if ((scanFile.Read() != 0x01) || (scanFile.Read() != 0x42) || (scanFile.Read() != 0x53) || (scanFile.Read() != 0x00))
        //    {
        //        Debug.WriteLine(string.Format("Header doesn't match"));
        //        bRet = false;
        //    }
        //    scanFile.Close();
        //    return bRet;
        //}

        //private bool containsMatReference(string theMeshFile, string theMat)
        //{
        //    Debug.WriteLine(string.Format("Checking whether {0} contains a reference to {1}", theMeshFile, theMat));
        //    bool bRet = false;
        //    StreamReader scanFile = new StreamReader(theMeshFile);
        //    string scanLine;
        //    string searchString = theMat.ToLower();
        //    bool bInMaterials = false;
        //    while ((scanLine = scanFile.ReadLine()) != null)
        //    {
        //        if ((!bInMaterials) && (scanLine.ToLower().Contains("[materials]"))) bInMaterials = true;
        //        else if ((bInMaterials) && (scanLine.Trim().StartsWith("["))) break;
        //        else if ((bInMaterials) && (scanLine.ToLower().Contains(searchString)))
        //        {
        //            Debug.WriteLine(string.Format("{0} contains a reference to {1}!", theMeshFile, theMat));
        //            bRet = true;
        //            break;
        //        }
        //    }
        //    scanFile.Close();
        //    return bRet;
        //}

        //private bool changeMatReference(string meshFile, string backupFolder, string matName, string matNameDistinctRelative)
        //{
        //    Debug.WriteLine(string.Format("Changing all references to {0} in {1} to {2}", matName, meshFile, matNameDistinctRelative));
        //    string backupFile = backupFolder + "\\" + Path.GetFileName(meshFile);
        //    if (!File.Exists(backupFile)) File.Move(meshFile, backupFile);
        //    StreamReader oldMesh = new StreamReader(backupFile);
        //    StreamWriter newMesh = File.CreateText(meshFile);

        //    string scanLine;
        //    string newLine;
        //    string searchString = matName.ToLower();
        //    bool bInMaterials = false;
        //    while ((scanLine = oldMesh.ReadLine()) != null)
        //    {
        //        newLine = scanLine;
        //        if ((!bInMaterials) && (scanLine.ToLower().Contains("[materials]")))
        //        {
        //            Debug.WriteLine(string.Format("[Materials] Section found!"));
        //            bInMaterials = true;
        //        }
        //        else if (bInMaterials)
        //        {
        //            Debug.WriteLine(string.Format("In [Materials] Section, line= {0}", scanLine));
        //            if (scanLine.Trim().StartsWith("["))
        //            {
        //                Debug.WriteLine(string.Format("End of [Materials] Section found!"));
        //                bInMaterials = false;
        //            }
        //            if (scanLine.ToLower().Contains(searchString))
        //            {
        //                Debug.WriteLine(string.Format("Changing line {0} to {1}", scanLine, matNameDistinctRelative));
        //                newLine = matNameDistinctRelative;
        //            }
        //        }
        //        newMesh.WriteLine(newLine);
        //    }
        //    newMesh.Close();
        //    oldMesh.Close();
        //    return true;
        //}

        //private bool isInBinaryMeshes(string theFile)
        //{
        //    Debug.WriteLine(string.Format("Checking whether {0} appears in binary meshes of same folder", theFile));
        //    string theFilePath = Path.GetDirectoryName(theFile);
        //    string theFileName = Path.GetFileNameWithoutExtension(theFile);
        //    string[] filePaths = Directory.GetFiles(theFilePath, "*.msh", SearchOption.TopDirectoryOnly);
        //    foreach (string theMeshFile in filePaths)
        //    {
        //        Debug.WriteLine(string.Format("Checking mesh {0}", theMeshFile));
        //        if (isBinaryMeshWithLinkToMat(theMeshFile, theFileName)) return true;
        //    }
        //    return false;
        //}

        //private bool isBinaryMeshWithLinkToMat(string theMeshFile, string theMat)
        //{
        //    Debug.WriteLine(string.Format("Checking whether {0} appears in (binary) mesh {1}", theMat, theMeshFile));
        //    if (!isBinaryMesh(theMeshFile)) return false;
        //    Debug.WriteLine(string.Format("{0} is binary mesh", theMeshFile));

        //    bool bRet = binaryFileContainsString(theMeshFile, theMat);
        //    Debug.WriteLine(string.Format("Check returned {0}", bRet));
        //    return bRet;


        //    //StreamReader scanFile = new StreamReader(@"U:\IL2\Extracted_SFS_IL2_412\3DO\Plane\A5M4(Ja)\CF_D2.msh");
        //    //string scanLine;
        //    //while ((scanLine = scanFile.ReadLine()) != null)
        //    //{
        //    //    if (scanLine.ToLower().Contains(theMat.ToLower()))
        //    //    {
        //    //        Debug.WriteLine(string.Format("{0} found in binary mesh {1}!", theMat, theMeshFile));
        //    //        scanFile.Close();
        //    //        return true;
        //    //    }
        //    //}
        //    //scanFile.Close();
        //    //return false;
        //}




        private void removeTgaDups()
        {
            try
            {
                this.SetStatus("Scanning mod Folder...");
                string[] filePaths = Directory.GetFiles(this.modPath, "*.tga",
                                             SearchOption.AllDirectories);
                this.removeTgaDuplicateFiles(filePaths);
            }
            catch
            {
                this.Proto("### ERROR in removeTgaDups() ###");
                this.SetStatus("Error in removeTgaDups.");
            }
            finally
            {
                this.Proto(this.numDuplicateRemoved + " .tga (" + this.sizeDuplicateRemoved / 1024 + "kb) removed.");
                this.Proto("### DONE ###");
                this.SetStatus("Done.");
                this.SetControlsEnabled(true, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            }
        }

        private void removeTgaDuplicateFiles(string[] theFiles)
        {
            foreach (string theFilePath in theFiles)
            {
                if (stopThreads.WaitOne(0)) return;
                string relativePath = theFilePath.Remove(0, this.modPath.Length);
                this.SetStatus(Path.GetDirectoryName(relativePath));
                string md5 = GetMD5HashFromFile(theFilePath);
                if (this.DistinctTgaList.ContainsKey(md5))
                {
                    this.numDuplicate++;
                    this.sizeDuplicate += (new FileInfo(theFilePath)).Length;
                    this.SetDuplicateSize(this.sizeDuplicate);
                    this.SetDuplicateFiles(numDuplicate);
                    string backupFile = Directory.GetParent(this.modPath) + "\\3do_backup" + relativePath;
                    string backupFolder = Path.GetDirectoryName(backupFile);
                    if (File.Exists(basePath + relativePath))
                    {
                        if (GetMD5HashFromFile(basePath + relativePath).CompareTo(GetMD5HashFromFile(theFilePath)) == 0)
                        {
                            Directory.CreateDirectory(backupFolder);
                            File.Move(theFilePath, backupFile);
                            this.numDuplicateRemoved++;
                            this.sizeDuplicateRemoved += (new FileInfo(theFilePath)).Length;
                            this.Proto("Removed " + relativePath + " (identical copy in identical base folder)");
                            continue;
                        }
                    }
                    Directory.CreateDirectory(backupFolder);
                    if (changeTgaReferences(theFilePath, backupFolder, relativePath, this.DistinctTgaList[md5]))
                    {
                        this.numDuplicateRemoved++;
                        this.sizeDuplicateRemoved += (new FileInfo(theFilePath)).Length;
                        File.Move(theFilePath, backupFile);
                        this.Proto("Removed " + relativePath + " ( copy of " + this.DistinctTgaList[md5] + " )");
                    }
                    else
                    {
                        this.Proto("Error while replacing mesh references to " + relativePath);
                    }
                }
                else
                {
                    this.numDistinct++;
                    this.sizeDistinct += (new FileInfo(theFilePath)).Length;
                    this.SetDistinctSize(this.sizeDistinct);
                    this.SetDistinctFiles(this.numDistinct);
                }
            }
        }

        private bool changeTgaReferences(string tgaFile, string backupFolder, string relativePathDuplicate, string relativePathDistinct)
        {
            string matFolder = Path.GetDirectoryName(tgaFile);
            string tgaName = Path.GetFileName(tgaFile);
            string tgaNameDistinctRelative = this.RelativePath(Path.GetDirectoryName(relativePathDuplicate), Path.GetDirectoryName(relativePathDistinct)) + "\\" + Path.GetFileName(relativePathDistinct);
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
            if (!File.Exists(backupFile)) File.Move(matFile, backupFile);
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








        //private bool binaryFileContainsString(string filePath, string stringToLookFor)
        //{

        //    // convert the string to a binary (ASCII) representation
        //    byte[] bufferToLookFor = System.Text.Encoding.ASCII.GetBytes(stringToLookFor.ToUpper());

        //    // open the file in binary mode
        //    using (Stream stream = new FileStream(filePath, FileMode.Open,
        //    FileAccess.Read))
        //    {
        //        byte[] readBuffer = new byte[(new FileInfo(filePath)).Length + 1024]; // our input buffer, 1k safety ahead
        //        int bytesRead = 0; // number of bytes read
        //        int offset = 0; // offset inside read-buffer
        //        long filePos = 0; // position inside the file before read operation
        //        while ((bytesRead = stream.Read(readBuffer, offset,
        //        readBuffer.Length - offset)) > 0)
        //        {
        //            for (int i = 0; i < bytesRead + offset - bufferToLookFor.Length; i++)
        //            {
        //                readBuffer[i] = System.Text.Encoding.ASCII.GetBytes(System.Text.Encoding.ASCII.GetString(readBuffer, i, 1).ToUpper())[0];
        //            }

        //            for (int i = 0; i < bytesRead + offset - bufferToLookFor.Length; i++)
        //            {
        //                bool match = true;
        //                for (int j = 0; j < bufferToLookFor.Length; j++)
        //                {
        //                    if (bufferToLookFor[j] != readBuffer[i + j])
        //                    {
        //                        match = false;
        //                        break;
        //                    }
        //                }
        //                if (match)
        //                {
        //                    return true;
        //                }
        //            }
        //            // store file position before next read
        //            filePos = stream.Position;

        //            // store the last few characters to ensure matches on "chunk boundaries"
        //            offset = bufferToLookFor.Length;
        //            for (int i = 0; i < offset; i++)
        //                readBuffer[i] = readBuffer[readBuffer.Length - offset + i];
        //        }
        //    }
        //    return false;
        //}

        public string RelativePath(string absPath, string relTo)
        {
            string[] absDirs = absPath.Split('\\');
            string[] relDirs = relTo.Split('\\');
            // Get the shortest of the two paths
            int len = absDirs.Length < relDirs.Length ? absDirs.Length : relDirs.Length;
            // Use to determine where in the loop we exited
            int lastCommonRoot = -1; int index;
            // Find common root 
            for (index = 0; index < len; index++)
            {
                if (absDirs[index] == relDirs[index])
                    lastCommonRoot = index;
                else
                    break;
            }
            // If we didn't find a common prefix then throw 
            if (lastCommonRoot == -1)
            {
                throw new ArgumentException("Paths do not have a common base");
            }
            // Build up the relative path 
            StringBuilder relativePath = new StringBuilder();
            // Add on the .. 
            for (index = lastCommonRoot + 1; index < absDirs.Length; index++)
            {
                if (absDirs[index].Length > 0) relativePath.Append("..\\");
            }
            // Add on the folders 
            for (index = lastCommonRoot + 1; index < relDirs.Length - 1; index++)
            {
                relativePath.Append(relDirs[index] + "\\");
            }
            relativePath.Append(relDirs[relDirs.Length - 1]);
            return relativePath.ToString();
        }
    }

    internal class FlickerFreeListBox : System.Windows.Forms.ListBox
    {
        public FlickerFreeListBox()
        {
            this.SetStyle(
                ControlStyles.OptimizedDoubleBuffer |
                ControlStyles.ResizeRedraw |
                ControlStyles.UserPaint,
                true);
            this.DrawMode = DrawMode.OwnerDrawFixed;
        }
        protected override void OnDrawItem(DrawItemEventArgs e)
        {
            if (this.Items.Count > 0)
            {
                e.DrawBackground();
                e.Graphics.DrawString(this.Items[e.Index].ToString(), e.Font, new SolidBrush(this.ForeColor), new PointF(e.Bounds.X, e.Bounds.Y));
            }
            base.OnDrawItem(e);
        }
        protected override void OnPaint(PaintEventArgs e)
        {
            Region iRegion = new Region(e.ClipRectangle);
            e.Graphics.FillRegion(new SolidBrush(this.BackColor), iRegion);
            if (this.Items.Count > 0)
            {
                for (int i = 0; i < this.Items.Count; ++i)
                {
                    System.Drawing.Rectangle irect = this.GetItemRectangle(i);
                    if (e.ClipRectangle.IntersectsWith(irect))
                    {
                        if ((this.SelectionMode == SelectionMode.One && this.SelectedIndex == i)
                        || (this.SelectionMode == SelectionMode.MultiSimple && this.SelectedIndices.Contains(i))
                        || (this.SelectionMode == SelectionMode.MultiExtended && this.SelectedIndices.Contains(i)))
                        {
                            OnDrawItem(new DrawItemEventArgs(e.Graphics, this.Font,
                                irect, i,
                                DrawItemState.Selected, this.ForeColor,
                                this.BackColor));
                        }
                        else
                        {
                            OnDrawItem(new DrawItemEventArgs(e.Graphics, this.Font,
                                irect, i,
                                DrawItemState.Default, this.ForeColor,
                                this.BackColor));
                        }
                        iRegion.Complement(irect);
                    }
                }
            }
            base.OnPaint(e);
        }
    }
}
