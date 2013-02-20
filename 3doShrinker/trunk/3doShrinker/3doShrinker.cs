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
        private const int REFRESH_MS = 100;
        private static int lastProtoRefresh = Environment.TickCount;
        private static int lastStatusRefresh = Environment.TickCount;
        private ManualResetEvent stopThreads = new ManualResetEvent(false);

        private Dictionary<String, String> distinctTgaList = new Dictionary<String, String>();
        private Dictionary<String, String> tgaHashesList = new Dictionary<String, String>();
        private Dictionary<String, String> distinctMatList = new Dictionary<String, String>();
        private static Dictionary<String, String> hashListAbsolut = new Dictionary<String, String>();

        public static Object BackupFileLock = new Object();

        private FlickerFreeListBox listBoxProto;

        private Thread baseWorkerThread;

        public static ShrinkerStatus TheShrinkerStatus = new ShrinkerStatus();

        public class ShrinkerStatus
        {
            private Object shrinkerStatusLockObject = new Object();
            private int numDistinct = 0;
            public int NumDistinct
            {
                get { return numDistinct; }
                set
                {
                    lock (shrinkerStatusLockObject)
                    {
                        numDistinct = value;
                    }
                }
            }
            private long sizeDistinct = 0;
            public long SizeDistinct
            {
                get { return sizeDistinct; }
                set
                {
                    lock (shrinkerStatusLockObject)
                    {
                        sizeDistinct = value;
                    }
                }
            }
            private int numDuplicate = 0;
            public int NumDuplicate
            {
                get { return numDuplicate; }
                set
                {
                    lock (shrinkerStatusLockObject)
                    {
                        numDuplicate = value;
                    }
                }
            }
            private long sizeDuplicate = 0;
            public long SizeDuplicate
            {
                get { return sizeDuplicate; }
                set
                {
                    lock (shrinkerStatusLockObject)
                    {
                        sizeDuplicate = value;
                    }
                }
            }
            private string statusLine = "";
            public string StatusLine
            {
                get { return statusLine; }
                set
                {
                    lock (shrinkerStatusLockObject)
                    {
                        statusLine = value;
                    }
                }
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
            if (Environment.TickCount - REFRESH_MS > lastProtoRefresh)
            {
                lastProtoRefresh = Environment.TickCount;
                this.listBoxProto.Refresh();
            }
            Application.DoEvents();
#if DEBUG
            System.Diagnostics.Debug.WriteLine(theLine);
#endif
        }

        private delegate void DoSetStatus();
        private void SetStatus()
        {
            if (stopThreads.WaitOne(0)) return;
            if (this.InvokeRequired)
            {
                // we were called on a worker thread
                // marshal the call to the user interface thread
                this.Invoke(new DoSetStatus(SetStatus),
                            new object[] { });
                return;
            }

            // this code can only be reached
            // by the user interface thread
            this.toolStripStatusLabelDistinct.Text = TheShrinkerStatus.NumDistinct.ToString();
            this.toolStripStatusLabelDistinctSize.Text = string.Format("({0} MB)", TheShrinkerStatus.SizeDistinct / 1024 / 1024);
            this.toolStripStatusLabelDuplicate.Text = TheShrinkerStatus.NumDuplicate.ToString();
            this.toolStripStatusLabelDuplicateSize.Text = string.Format("({0} MB)", TheShrinkerStatus.SizeDuplicate / 1024 / 1024);
            this.toolStripStatusLabelFolder.Text = TheShrinkerStatus.StatusLine;
            if (Environment.TickCount - REFRESH_MS > lastStatusRefresh)
            {
                lastStatusRefresh = Environment.TickCount;
                this.statusStrip1.Refresh();
            }
            Application.DoEvents();
        }

        private delegate void DoSetStatus(string theStatus);
        private void SetStatus(string theStatus)
        {
            if (stopThreads.WaitOne(0)) return;
            if (this.InvokeRequired)
            {
                // we were called on a worker thread
                // marshal the call to the user interface thread
                this.Invoke(new DoSetStatus(SetStatus),
                            new object[] { theStatus });
                return;
            }

            // this code can only be reached
            // by the user interface thread
            TheShrinkerStatus.StatusLine = theStatus;
            this.SetStatus();
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
            TheShrinkerStatus.NumDistinct = TheShrinkerStatus.NumDuplicate = 0;
            TheShrinkerStatus.SizeDistinct = TheShrinkerStatus.SizeDuplicate = 0;
            this.stopThreads.Reset();
            this.distinctTgaList.Clear();
            hashListAbsolut.Clear();
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
            this.SetControlsEnabled(true, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
        }

        private void buttonLoadBase_Click(object sender, EventArgs e)
        {
            this.SetControlsEnabled(false, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            TheShrinkerStatus.NumDistinct = TheShrinkerStatus.NumDuplicate = 0;
            TheShrinkerStatus.SizeDistinct = TheShrinkerStatus.SizeDuplicate = 0;
            this.stopThreads.Reset();
            this.distinctTgaList.Clear();
            this.baseWorkerThread = new Thread(new ThreadStart(this.loadBaseFiles));
            this.baseWorkerThread.Start();
        }

        private void buttonRemoveMatDups_Click(object sender, EventArgs e)
        {
            TheShrinkerStatus.NumDistinct = TheShrinkerStatus.NumDuplicate = 0;
            TheShrinkerStatus.SizeDistinct = TheShrinkerStatus.SizeDuplicate = 0;
            this.SetControlsEnabled(false, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            this.stopThreads.Reset();
            this.baseWorkerThread = new Thread(new ThreadStart(this.removeMatDups));
            this.baseWorkerThread.Start();
        }

        private void buttonRemoveTgaDups_Click(object sender, EventArgs e)
        {
            TheShrinkerStatus.NumDistinct = TheShrinkerStatus.NumDuplicate = 0;
            TheShrinkerStatus.SizeDistinct = TheShrinkerStatus.SizeDuplicate = 0;
            this.SetControlsEnabled(false, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            this.stopThreads.Reset();
            this.baseWorkerThread = new Thread(new ThreadStart(this.removeTgaDups));
            this.baseWorkerThread.Start();
        }

        internal static string GetMD5HashFromFile(string fileName)
        {
            if (hashListAbsolut.ContainsKey(fileName)) return hashListAbsolut[fileName];
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

        internal static string CombineHashes(string hash1, string hash2)
        {
            string toHash = hash1 + hash2;
            MD5 md5 = new MD5CryptoServiceProvider();
            byte[] retVal = md5.ComputeHash(Encoding.ASCII.GetBytes(toHash));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < retVal.Length; i++)
            {
                sb.Append(retVal[i].ToString("x2"));
            }
            return sb.ToString();
        }

        internal static string RandomHash()
        {
            RNGCryptoServiceProvider rng = new RNGCryptoServiceProvider();
            byte[] retVal = new byte[16];
            rng.GetBytes(retVal);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < retVal.Length; i++)
            {
                sb.Append(retVal[i].ToString("x2"));
            }
            return sb.ToString();
        }

        private void scanBaseFolder()
        {
            List<AutoResetEvent> theWorkerThreadEndEvents = new List<AutoResetEvent>();
            List<TgaScanner> theTgaWorkers = new List<TgaScanner>();
            List<MatScanner> theMatWorkers = new List<MatScanner>();
            try
            {
                if (stopThreads.WaitOne(0)) return;

                string[] subDirsTga = Directory.GetDirectories(this.basePath);
                foreach (string subDir in subDirsTga)
                {
                    TgaScanner tgaScanner = new TgaScanner(this.basePath, subDir, (Action<string>)this.Proto, (Action)this.SetStatus, this.stopThreads);
                    theWorkerThreadEndEvents.Add(tgaScanner.scanFinished);
                    theTgaWorkers.Add(tgaScanner);
                }

                WaitHandle.WaitAll(theWorkerThreadEndEvents.ToArray());

                TheShrinkerStatus.StatusLine = "Merging hashes";
                this.SetStatus();
                this.Proto("Merging distinct .tga hashes");

                this.distinctTgaList.Clear();
                foreach (TgaScanner theWorker in theTgaWorkers)
                {
                    foreach (var distinctTga in theWorker.distinctTgaList)
                        if (!this.distinctTgaList.ContainsKey(distinctTga.Key))
                            this.distinctTgaList.Add(distinctTga.Key, distinctTga.Value);
                    foreach (var tgaHash in theWorker.tgaHashesList)
                        this.tgaHashesList.Add(tgaHash.Key, tgaHash.Value);
                }
                
                theWorkerThreadEndEvents.Clear();

                string[] subDirsMat = Directory.GetDirectories(this.basePath);
                foreach (string subDir in subDirsMat)
                {
                    MatScanner matScanner = new MatScanner(this.basePath, subDir, this.tgaHashesList, (Action<string>)this.Proto, (Action)this.SetStatus, this.stopThreads);
                    theWorkerThreadEndEvents.Add(matScanner.scanFinished);
                    theMatWorkers.Add(matScanner);
                }

                WaitHandle.WaitAll(theWorkerThreadEndEvents.ToArray());

                TheShrinkerStatus.StatusLine = "Merging hashes";
                this.SetStatus();
                this.Proto("Merging distinct .mat hashes");

                this.distinctMatList.Clear();
                foreach (MatScanner theWorker in theMatWorkers)
                {
                    foreach (var distinctMat in theWorker.distinctMatList)
                        if (!this.distinctMatList.ContainsKey(distinctMat.Key))
                            this.distinctMatList.Add(distinctMat.Key, distinctMat.Value);
                }

                this.SetStatus("Creating Hash Cache");
                StreamWriter cacheFile = File.CreateText(this.hashCacheFile());
                cacheFile.AutoFlush = false;
                cacheFile.WriteLine(string.Format("{0};{1};{2};{3}", TheShrinkerStatus.NumDistinct, TheShrinkerStatus.SizeDistinct, TheShrinkerStatus.NumDuplicate, TheShrinkerStatus.SizeDuplicate));
                foreach (KeyValuePair<string,string> distinctTga in this.distinctTgaList)
                {
                    cacheFile.WriteLine(distinctTga.Key + ";" + distinctTga.Value);
                }
                cacheFile.WriteLine("##########");
                foreach (KeyValuePair<string, string> distinctMat in this.distinctMatList)
                {
                    cacheFile.WriteLine(distinctMat.Key + ";" + distinctMat.Value);
                }
                cacheFile.WriteLine("##########");
                foreach (KeyValuePair<string, string> tgaHash in this.tgaHashesList)
                {
                    cacheFile.WriteLine(tgaHash.Value + ";" + tgaHash.Key);
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
            int iTemp;
            long lTemp;
            int.TryParse(cacheFileLineTokens[0], out iTemp);
            TheShrinkerStatus.NumDistinct = iTemp;
            long.TryParse(cacheFileLineTokens[1], out lTemp);
            TheShrinkerStatus.SizeDistinct = lTemp;
            int.TryParse(cacheFileLineTokens[2], out iTemp);
            TheShrinkerStatus.NumDuplicate = iTemp;
            long.TryParse(cacheFileLineTokens[3], out lTemp);
            TheShrinkerStatus.SizeDuplicate = lTemp;
            TheShrinkerStatus.StatusLine = "loading Base Files";
            this.SetStatus();

            this.distinctTgaList.Clear();
            this.distinctMatList.Clear();
            this.tgaHashesList.Clear();
            while ((cacheFileLine = cacheFile.ReadLine()) != null)
            {
                if (cacheFileLine.CompareTo("##########") == 0) break;
                cacheFileLineTokens = cacheFileLine.Split(";".ToCharArray(), 2);
                this.distinctTgaList.Add(cacheFileLineTokens[0], cacheFileLineTokens[1]);
            }
            while ((cacheFileLine = cacheFile.ReadLine()) != null)
            {
                if (cacheFileLine.CompareTo("##########") == 0) break;
                cacheFileLineTokens = cacheFileLine.Split(";".ToCharArray(), 2);
                this.distinctMatList.Add(cacheFileLineTokens[0], cacheFileLineTokens[1]);
            }
            while ((cacheFileLine = cacheFile.ReadLine()) != null)
            {
                if (cacheFileLine.CompareTo("##########") == 0) break;
                cacheFileLineTokens = cacheFileLine.Split(";".ToCharArray(), 2);
                this.tgaHashesList.Add(cacheFileLineTokens[1], cacheFileLineTokens[0]);
            }
            cacheFile.Close();
            this.Proto("### DONE ###");
            this.SetControlsEnabled(true, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
        }

        private void removeMatDups()
        {

            List<AutoResetEvent> theWorkerThreadEndEvents = new List<AutoResetEvent>();
            List<MatDupRemover> theWorkers = new List<MatDupRemover>();
            try
            {
                if (stopThreads.WaitOne(0)) return;

                this.Proto("Removing .mat duplicates");

                string[] subDirsTga = Directory.GetDirectories(this.modPath);
                foreach (string subDir in subDirsTga)
                {
                    MatDupRemover matDupRemover = new MatDupRemover(
                        this.modPath, 
                        subDir, 
                        this.distinctMatList,
                        this.tgaHashesList,
                        (Action<string>)this.Proto,
                        (Action)this.SetStatus,
                        this.stopThreads);
                    theWorkerThreadEndEvents.Add(matDupRemover.scanFinished);
                    theWorkers.Add(matDupRemover);
                }

                WaitHandle.WaitAll(theWorkerThreadEndEvents.ToArray());
                TheShrinkerStatus.StatusLine = "Finished.";
            }
            catch (Exception e)
            {
                this.Proto("### ERROR in removeMatDups() ###");
                this.SetStatus("Error in removeMatDups.");
                Debug.WriteLine(e.ToString());
            }
            finally
            {
                this.Proto(TheShrinkerStatus.NumDuplicate + " .mat (" + TheShrinkerStatus.SizeDuplicate / 1024 + "kb) removed.");
                this.Proto("### DONE ###");
                this.SetStatus("Done.");
                this.SetControlsEnabled(true, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            }
        }

        private void removeTgaDups()
        {
            List<AutoResetEvent> theWorkerThreadEndEvents = new List<AutoResetEvent>();
            List<TgaDupRemover> theWorkers = new List<TgaDupRemover>();
            try
            {
                if (stopThreads.WaitOne(0)) return;

                this.Proto("Removing .tga duplicates");

                string[] subDirsTga = Directory.GetDirectories(this.modPath);
                foreach (string subDir in subDirsTga)
                {
                    TgaDupRemover tgaDupRemover = new TgaDupRemover(
                        this.modPath,
                        subDir,
                        this.distinctTgaList,
                        (Action<string>)this.Proto,
                        (Action)this.SetStatus,
                        this.stopThreads);
                    theWorkerThreadEndEvents.Add(tgaDupRemover.scanFinished);
                    theWorkers.Add(tgaDupRemover);
                }

                WaitHandle.WaitAll(theWorkerThreadEndEvents.ToArray());
                TheShrinkerStatus.StatusLine = "Finished.";
            }
            catch
            {
                this.Proto("### ERROR in removeTgaDups() ###");
                this.SetStatus("Error in removeTgaDups.");
            }
            finally
            {
                this.Proto(TheShrinkerStatus.NumDuplicate + " .tga (" + TheShrinkerStatus.SizeDuplicate / 1024 + "kb) removed.");
                this.Proto("### DONE ###");
                this.SetStatus("Done.");
                this.SetControlsEnabled(true, this.buttonSelectBaseFolder, this.buttonSelectModFolder, this.buttonLoadBase, this.buttonScanBase, this.buttonRemoveMatDups, this.buttonRemoveTgaDups);
            }
        }

        public static string RelativePath(string absPath, string relTo)
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
