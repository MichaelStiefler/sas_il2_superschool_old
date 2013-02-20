namespace _3doShrinker
{
    partial class Shrinker
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            this.folderBrowserDialog3do = new System.Windows.Forms.FolderBrowserDialog();
            this.buttonSelectBaseFolder = new System.Windows.Forms.Button();
            this.textBox3doBase = new System.Windows.Forms.TextBox();
            this.textBox3doMod = new System.Windows.Forms.TextBox();
            this.buttonSelectModFolder = new System.Windows.Forms.Button();
            this.buttonScanBase = new System.Windows.Forms.Button();
            this.buttonStopAction = new System.Windows.Forms.Button();
            this.statusStrip1 = new System.Windows.Forms.StatusStrip();
            this.toolStripStatusLabelDistinctCaption = new System.Windows.Forms.ToolStripStatusLabel();
            this.toolStripStatusLabelDistinct = new System.Windows.Forms.ToolStripStatusLabel();
            this.toolStripStatusLabelDistinctSize = new System.Windows.Forms.ToolStripStatusLabel();
            this.toolStripStatusLabelDuplicatesCaption = new System.Windows.Forms.ToolStripStatusLabel();
            this.toolStripStatusLabelDuplicate = new System.Windows.Forms.ToolStripStatusLabel();
            this.toolStripStatusLabelDuplicateSize = new System.Windows.Forms.ToolStripStatusLabel();
            this.toolStripStatusLabelFolder = new System.Windows.Forms.ToolStripStatusLabel();
            this.buttonLoadBase = new System.Windows.Forms.Button();
            this.buttonRemoveMatDups = new System.Windows.Forms.Button();
            this.buttonRemoveTgaDups = new System.Windows.Forms.Button();
            this.statusStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // buttonSelectBaseFolder
            // 
            this.buttonSelectBaseFolder.Location = new System.Drawing.Point(540, 12);
            this.buttonSelectBaseFolder.Name = "buttonSelectBaseFolder";
            this.buttonSelectBaseFolder.Size = new System.Drawing.Size(133, 23);
            this.buttonSelectBaseFolder.TabIndex = 0;
            this.buttonSelectBaseFolder.Text = "Select 3do base folder";
            this.buttonSelectBaseFolder.UseVisualStyleBackColor = true;
            this.buttonSelectBaseFolder.Click += new System.EventHandler(this.buttonSelectBaseFolder_Click);
            // 
            // textBox3doBase
            // 
            this.textBox3doBase.Location = new System.Drawing.Point(12, 13);
            this.textBox3doBase.Name = "textBox3doBase";
            this.textBox3doBase.Size = new System.Drawing.Size(522, 20);
            this.textBox3doBase.TabIndex = 1;
            // 
            // textBox3doMod
            // 
            this.textBox3doMod.Location = new System.Drawing.Point(12, 42);
            this.textBox3doMod.Name = "textBox3doMod";
            this.textBox3doMod.Size = new System.Drawing.Size(522, 20);
            this.textBox3doMod.TabIndex = 3;
            // 
            // buttonSelectModFolder
            // 
            this.buttonSelectModFolder.Location = new System.Drawing.Point(540, 41);
            this.buttonSelectModFolder.Name = "buttonSelectModFolder";
            this.buttonSelectModFolder.Size = new System.Drawing.Size(133, 23);
            this.buttonSelectModFolder.TabIndex = 2;
            this.buttonSelectModFolder.Text = "Select 3do mod folder";
            this.buttonSelectModFolder.UseVisualStyleBackColor = true;
            this.buttonSelectModFolder.Click += new System.EventHandler(this.buttonSelectModFolder_Click);
            // 
            // buttonScanBase
            // 
            this.buttonScanBase.Location = new System.Drawing.Point(135, 70);
            this.buttonScanBase.Name = "buttonScanBase";
            this.buttonScanBase.Size = new System.Drawing.Size(118, 23);
            this.buttonScanBase.TabIndex = 4;
            this.buttonScanBase.Text = "Scan Base Folder";
            this.buttonScanBase.UseVisualStyleBackColor = true;
            this.buttonScanBase.Click += new System.EventHandler(this.buttonScanBase_Click);
            // 
            // buttonStopAction
            // 
            this.buttonStopAction.Location = new System.Drawing.Point(540, 70);
            this.buttonStopAction.Name = "buttonStopAction";
            this.buttonStopAction.Size = new System.Drawing.Size(133, 23);
            this.buttonStopAction.TabIndex = 6;
            this.buttonStopAction.Text = "Stop Current Task";
            this.buttonStopAction.UseVisualStyleBackColor = true;
            this.buttonStopAction.Click += new System.EventHandler(this.buttonStopAction_Click);
            // 
            // statusStrip1
            // 
            this.statusStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.toolStripStatusLabelDistinctCaption,
            this.toolStripStatusLabelDistinct,
            this.toolStripStatusLabelDistinctSize,
            this.toolStripStatusLabelDuplicatesCaption,
            this.toolStripStatusLabelDuplicate,
            this.toolStripStatusLabelDuplicateSize,
            this.toolStripStatusLabelFolder});
            this.statusStrip1.Location = new System.Drawing.Point(0, 504);
            this.statusStrip1.Name = "statusStrip1";
            this.statusStrip1.Size = new System.Drawing.Size(685, 24);
            this.statusStrip1.TabIndex = 7;
            this.statusStrip1.Text = "statusStrip1";
            // 
            // toolStripStatusLabelDistinctCaption
            // 
            this.toolStripStatusLabelDistinctCaption.BorderStyle = System.Windows.Forms.Border3DStyle.Etched;
            this.toolStripStatusLabelDistinctCaption.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
            this.toolStripStatusLabelDistinctCaption.Name = "toolStripStatusLabelDistinctCaption";
            this.toolStripStatusLabelDistinctCaption.Size = new System.Drawing.Size(76, 19);
            this.toolStripStatusLabelDistinctCaption.Text = "Distinct Files:";
            // 
            // toolStripStatusLabelDistinct
            // 
            this.toolStripStatusLabelDistinct.AutoSize = false;
            this.toolStripStatusLabelDistinct.BorderStyle = System.Windows.Forms.Border3DStyle.Etched;
            this.toolStripStatusLabelDistinct.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
            this.toolStripStatusLabelDistinct.Name = "toolStripStatusLabelDistinct";
            this.toolStripStatusLabelDistinct.Size = new System.Drawing.Size(40, 19);
            this.toolStripStatusLabelDistinct.Text = "0";
            this.toolStripStatusLabelDistinct.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // toolStripStatusLabelDistinctSize
            // 
            this.toolStripStatusLabelDistinctSize.AutoSize = false;
            this.toolStripStatusLabelDistinctSize.BorderStyle = System.Windows.Forms.Border3DStyle.Etched;
            this.toolStripStatusLabelDistinctSize.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
            this.toolStripStatusLabelDistinctSize.Name = "toolStripStatusLabelDistinctSize";
            this.toolStripStatusLabelDistinctSize.Size = new System.Drawing.Size(60, 19);
            this.toolStripStatusLabelDistinctSize.Text = "(0 MB)";
            this.toolStripStatusLabelDistinctSize.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // toolStripStatusLabelDuplicatesCaption
            // 
            this.toolStripStatusLabelDuplicatesCaption.BorderSides = System.Windows.Forms.ToolStripStatusLabelBorderSides.Left;
            this.toolStripStatusLabelDuplicatesCaption.BorderStyle = System.Windows.Forms.Border3DStyle.Etched;
            this.toolStripStatusLabelDuplicatesCaption.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
            this.toolStripStatusLabelDuplicatesCaption.Name = "toolStripStatusLabelDuplicatesCaption";
            this.toolStripStatusLabelDuplicatesCaption.Size = new System.Drawing.Size(90, 19);
            this.toolStripStatusLabelDuplicatesCaption.Text = "Duplicate Files:";
            // 
            // toolStripStatusLabelDuplicate
            // 
            this.toolStripStatusLabelDuplicate.AutoSize = false;
            this.toolStripStatusLabelDuplicate.BorderStyle = System.Windows.Forms.Border3DStyle.Etched;
            this.toolStripStatusLabelDuplicate.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
            this.toolStripStatusLabelDuplicate.Name = "toolStripStatusLabelDuplicate";
            this.toolStripStatusLabelDuplicate.Size = new System.Drawing.Size(40, 19);
            this.toolStripStatusLabelDuplicate.Text = "0";
            this.toolStripStatusLabelDuplicate.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // toolStripStatusLabelDuplicateSize
            // 
            this.toolStripStatusLabelDuplicateSize.AutoSize = false;
            this.toolStripStatusLabelDuplicateSize.BorderSides = System.Windows.Forms.ToolStripStatusLabelBorderSides.Right;
            this.toolStripStatusLabelDuplicateSize.BorderStyle = System.Windows.Forms.Border3DStyle.Etched;
            this.toolStripStatusLabelDuplicateSize.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
            this.toolStripStatusLabelDuplicateSize.Name = "toolStripStatusLabelDuplicateSize";
            this.toolStripStatusLabelDuplicateSize.Size = new System.Drawing.Size(60, 19);
            this.toolStripStatusLabelDuplicateSize.Text = "(0 MB)";
            this.toolStripStatusLabelDuplicateSize.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // toolStripStatusLabelFolder
            // 
            this.toolStripStatusLabelFolder.BorderStyle = System.Windows.Forms.Border3DStyle.Etched;
            this.toolStripStatusLabelFolder.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
            this.toolStripStatusLabelFolder.ImageAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.toolStripStatusLabelFolder.Name = "toolStripStatusLabelFolder";
            this.toolStripStatusLabelFolder.Size = new System.Drawing.Size(304, 19);
            this.toolStripStatusLabelFolder.Spring = true;
            this.toolStripStatusLabelFolder.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // buttonLoadBase
            // 
            this.buttonLoadBase.Enabled = false;
            this.buttonLoadBase.Location = new System.Drawing.Point(11, 70);
            this.buttonLoadBase.Name = "buttonLoadBase";
            this.buttonLoadBase.Size = new System.Drawing.Size(118, 23);
            this.buttonLoadBase.TabIndex = 8;
            this.buttonLoadBase.Text = "Load Base Hashes";
            this.buttonLoadBase.UseVisualStyleBackColor = true;
            this.buttonLoadBase.Click += new System.EventHandler(this.buttonLoadBase_Click);
            // 
            // buttonRemoveMatDups
            // 
            this.buttonRemoveMatDups.Enabled = false;
            this.buttonRemoveMatDups.Location = new System.Drawing.Point(259, 70);
            this.buttonRemoveMatDups.Name = "buttonRemoveMatDups";
            this.buttonRemoveMatDups.Size = new System.Drawing.Size(118, 23);
            this.buttonRemoveMatDups.TabIndex = 9;
            this.buttonRemoveMatDups.Text = "Remove .mat Dups";
            this.buttonRemoveMatDups.UseVisualStyleBackColor = true;
            this.buttonRemoveMatDups.Click += new System.EventHandler(this.buttonRemoveMatDups_Click);
            // 
            // buttonRemoveTgaDups
            // 
            this.buttonRemoveTgaDups.Enabled = false;
            this.buttonRemoveTgaDups.Location = new System.Drawing.Point(383, 70);
            this.buttonRemoveTgaDups.Name = "buttonRemoveTgaDups";
            this.buttonRemoveTgaDups.Size = new System.Drawing.Size(118, 23);
            this.buttonRemoveTgaDups.TabIndex = 10;
            this.buttonRemoveTgaDups.Text = "Remove .tga Dups";
            this.buttonRemoveTgaDups.UseVisualStyleBackColor = true;
            this.buttonRemoveTgaDups.Click += new System.EventHandler(this.buttonRemoveTgaDups_Click);
            // 
            // Shrinker
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(685, 528);
            this.Controls.Add(this.buttonRemoveTgaDups);
            this.Controls.Add(this.buttonRemoveMatDups);
            this.Controls.Add(this.buttonLoadBase);
            this.Controls.Add(this.statusStrip1);
            this.Controls.Add(this.buttonStopAction);
            this.Controls.Add(this.buttonScanBase);
            this.Controls.Add(this.textBox3doMod);
            this.Controls.Add(this.buttonSelectModFolder);
            this.Controls.Add(this.textBox3doBase);
            this.Controls.Add(this.buttonSelectBaseFolder);
            this.Name = "Shrinker";
            this.Text = "3do Shrinker";
            this.statusStrip1.ResumeLayout(false);
            this.statusStrip1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.FolderBrowserDialog folderBrowserDialog3do;
        private System.Windows.Forms.Button buttonSelectBaseFolder;
        private System.Windows.Forms.TextBox textBox3doBase;
        private System.Windows.Forms.TextBox textBox3doMod;
        private System.Windows.Forms.Button buttonSelectModFolder;
        private System.Windows.Forms.Button buttonScanBase;
        private System.Windows.Forms.Button buttonStopAction;
        private System.Windows.Forms.StatusStrip statusStrip1;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabelDistinctCaption;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabelDuplicateSize;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabelDuplicatesCaption;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabelDuplicate;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabelFolder;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabelDistinct;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabelDistinctSize;
        private System.Windows.Forms.Button buttonLoadBase;
        private System.Windows.Forms.Button buttonRemoveMatDups;
        private System.Windows.Forms.Button buttonRemoveTgaDups;
    }
}

