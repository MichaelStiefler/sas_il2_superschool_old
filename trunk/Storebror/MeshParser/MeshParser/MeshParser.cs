using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Reflection;
using System.IO;
using System.Threading;
using System.Diagnostics;
using System.Globalization;
using System.Text.RegularExpressions;


namespace MeshParser
{
    public partial class MeshParser : Form
    {
        public MeshParser()
        {
            InitializeComponent();
        }

        private Thread theMeshParserThread;
        private bool isThreadRunning = false;

        private delegate void DoProto(string theLine);
        private void Proto(string theLine)
        {
            if (this.InvokeRequired)
            {
                this.BeginInvoke(new DoProto(Proto), new object[] { theLine });
                return;
            }
            this.listBoxProto.TopIndex = this.listBoxProto.Items.Add(theLine);
            //this.listBoxProto.Refresh();
        }

        private delegate void DoParserDone();
        private void ParserDone()
        {
            if (this.InvokeRequired)
            {
                this.BeginInvoke(new DoParserDone(ParserDone), new object[] { });
                return;
            }
            this.Proto("Done.");
            this.buttonStart_Click(this, EventArgs.Empty);
        }

        private void buttonMeshFolderBrowse_Click(object sender, EventArgs e)
        {
            if (this.meshFolderBrowserDialog.ShowDialog() == DialogResult.OK)
            {
                string thePathName = this.meshFolderBrowserDialog.SelectedPath;
                if (thePathName.StartsWith(Application.StartupPath))
                {
                    thePathName = "." + thePathName.Remove(0, Application.StartupPath.Length);
                }
                this.textBoxMeshFolder.Text = thePathName;
            }
        }

        private void buttonHeaderFile_Click(object sender, EventArgs e)
        {
            if (this.openHeaderFileDialog.ShowDialog() == DialogResult.OK)
            {
                string theFileName = this.openHeaderFileDialog.FileName;
                if (theFileName.StartsWith(Application.StartupPath))
                {
                    theFileName = "." + theFileName.Remove(0, Application.StartupPath.Length);
                }
                this.textBoxHeaderFile.Text = theFileName;
            }
        }

        private void checkBoxUseHeaderFile_CheckedChanged(object sender, EventArgs e)
        {
            this.textBoxHeaderFile.Enabled = this.checkBoxUseHeaderFile.Checked;
            this.buttonHeaderFile.Enabled = this.checkBoxUseHeaderFile.Checked;
        }

        private void MeshParser_Load(object sender, EventArgs e)
        {
            this.DoubleBuffered = true;
        }

        private void MeshParser_Shown(object sender, EventArgs e)
        {
            string the3doPath = Application.StartupPath + "\\3do";
            string theHeaderFile = Application.StartupPath + "\\MeshParserHeader.txt";
            if (Directory.Exists(the3doPath))
            {
                this.textBoxMeshFolder.Text = ".\\3do";
                this.meshFolderBrowserDialog.SelectedPath = the3doPath;
            }
            if (File.Exists(theHeaderFile))
            {
                this.textBoxHeaderFile.Text = ".\\MeshParserHeader.txt";
                this.openHeaderFileDialog.FileName = theHeaderFile;
                this.openHeaderFileDialog.InitialDirectory = Application.StartupPath;
                this.checkBoxUseHeaderFile.Checked = true;
            }
        }

        private void buttonStart_Click(object sender, EventArgs e)
        {
            if (this.isThreadRunning)
            {
                this.theMeshParserThread.Abort();
                this.buttonStart.Text = "Start";
                this.isThreadRunning = false;
                this.textBoxMeshFolder.Enabled = true;
                this.checkBoxMeshFolderSubdirs.Enabled = true;
                this.buttonMeshFolderBrowse.Enabled = true;
            }
            else
            {
                this.theMeshParserThread = new Thread(new ThreadStart(this.MeshParserThread));
                this.theMeshParserThread.IsBackground = true;
                this.theMeshParserThread.Start();
                this.buttonStart.Text = "Stop";
                this.textBoxMeshFolder.Enabled = false;
                this.checkBoxMeshFolderSubdirs.Enabled = false;
                this.buttonMeshFolderBrowse.Enabled = false;
                this.isThreadRunning = true;
            }
        }

        private void MeshParserThread()
        {
            string[] filePathsHim = Directory.GetFiles(this.textBoxMeshFolder.Text, "*.him", SearchOption.AllDirectories);
            string[] filePathsMsh = Directory.GetFiles(this.textBoxMeshFolder.Text, "*.msh", SearchOption.AllDirectories);
            try
            {
                foreach (string theFilePath in filePathsHim) ParseFile(theFilePath);
                foreach (string theFilePath in filePathsMsh) ParseFile(theFilePath);
            }
            catch
            {

            }
            this.ParserDone();
        }

        string RemoveComments(string str, string delimiter)
        {
            return Regex.Replace(str, delimiter + ".+", string.Empty).Trim();
        }

        private void ParseFile(string theFilePath)
        {
            string line = null;
            List<string> lines = new List<string>();
            StreamReader reader = new StreamReader(theFilePath);

            if (reader.Peek() == 1)
            {
                this.Proto("File " + theFilePath + " is binary file, remains unchanged.");
                return;
            }
            this.Proto("File " + theFilePath + " is text file, parsing contents.");

            int iParserStart = 0;

            if (File.Exists(this.openHeaderFileDialog.FileName))
            {
                StreamReader file = new StreamReader(this.openHeaderFileDialog.FileName);
                while ((line = file.ReadLine()) != null)
                {
                    if (line.Trim().Length == 0) continue;
                    lines.Add(line);
                    iParserStart++;
                }
                file.Close();
            }

            while ((line = reader.ReadLine()) != null)
            {
                line = line.Trim();
                if (line.StartsWith("//") || (line.Length == 0)) continue;
                line = RemoveComments(line, "//");
                if (line.StartsWith("[") && line.EndsWith("]")) lines.Add("");
                lines.Add(line);
            }
            reader.Close();
            bool inMaterials = false;

            for (int i = iParserStart; i < lines.Count; i++)
            {
                string tempLine = lines[i].Trim();
                inMaterials = tempLine.StartsWith("[Materials]");
                if (inMaterials)
                    lines[i] = tempLine;
                else
                    lines[i] = this.ParseLine(tempLine);
            }

            StreamWriter writer = new StreamWriter(theFilePath);
            for (int i = 0; i < lines.Count; i++)
            {
                writer.WriteLine(lines[i]);
            }
            writer.Close();
        }
        private string ParseLine(string theLine)
        {
            StringBuilder sbRetVal = new StringBuilder();
            string[] lineToken = theLine.Split(' ');
            NumberStyles theNumberStyles = NumberStyles.Any;
            int numNumeric = 0;
            foreach (string theLineToken in lineToken)
            {
                if (sbRetVal.Length != 0) sbRetVal.Append(" ");
                double numericToken;
                if (double.TryParse(theLineToken, theNumberStyles, CultureInfo.InvariantCulture, out numericToken))
                {
                    numNumeric++;
                    double roundedNumericToken = Math.Round(numericToken);
                    if (Math.Abs(numericToken - roundedNumericToken) < 0.0001)
                    {
                        int iNumericToken = Convert.ToInt32(roundedNumericToken);
                        sbRetVal.Append(iNumericToken.ToString());
                        continue;
                    }
                }
                sbRetVal.Append(theLineToken);
            }
            string sRetVal = sbRetVal.ToString().Trim();
            if ((numNumeric == 6) && sRetVal.EndsWith("0 0 0"))
            {
                sRetVal = sRetVal.Remove(sRetVal.Length - 1, 1);
                sRetVal += "1";
            }
            return sRetVal;
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


}
