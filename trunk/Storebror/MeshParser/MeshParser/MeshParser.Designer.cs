namespace MeshParser
{
    partial class MeshParser
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
            this.meshFolderBrowserDialog = new System.Windows.Forms.FolderBrowserDialog();
            this.textBoxMeshFolder = new System.Windows.Forms.TextBox();
            this.buttonMeshFolderBrowse = new System.Windows.Forms.Button();
            this.labelMeshFolder = new System.Windows.Forms.Label();
            this.buttonStart = new System.Windows.Forms.Button();
            this.checkBoxMeshFolderSubdirs = new System.Windows.Forms.CheckBox();
            this.listBoxProto = new MeshParser.FlickerFreeListBox();
            this.checkBoxUseHeaderFile = new System.Windows.Forms.CheckBox();
            this.textBoxHeaderFile = new System.Windows.Forms.TextBox();
            this.buttonHeaderFile = new System.Windows.Forms.Button();
            this.openHeaderFileDialog = new System.Windows.Forms.OpenFileDialog();
            this.SuspendLayout();
            // 
            // textBoxMeshFolder
            // 
            this.textBoxMeshFolder.Location = new System.Drawing.Point(83, 427);
            this.textBoxMeshFolder.Name = "textBoxMeshFolder";
            this.textBoxMeshFolder.Size = new System.Drawing.Size(317, 20);
            this.textBoxMeshFolder.TabIndex = 1;
            // 
            // buttonMeshFolderBrowse
            // 
            this.buttonMeshFolderBrowse.Location = new System.Drawing.Point(526, 425);
            this.buttonMeshFolderBrowse.Name = "buttonMeshFolderBrowse";
            this.buttonMeshFolderBrowse.Size = new System.Drawing.Size(75, 23);
            this.buttonMeshFolderBrowse.TabIndex = 2;
            this.buttonMeshFolderBrowse.Text = "Browse";
            this.buttonMeshFolderBrowse.UseVisualStyleBackColor = true;
            this.buttonMeshFolderBrowse.Click += new System.EventHandler(this.buttonMeshFolderBrowse_Click);
            // 
            // labelMeshFolder
            // 
            this.labelMeshFolder.AutoSize = true;
            this.labelMeshFolder.Location = new System.Drawing.Point(12, 430);
            this.labelMeshFolder.Name = "labelMeshFolder";
            this.labelMeshFolder.Size = new System.Drawing.Size(65, 13);
            this.labelMeshFolder.TabIndex = 3;
            this.labelMeshFolder.Text = "Mesh Folder";
            // 
            // buttonStart
            // 
            this.buttonStart.Location = new System.Drawing.Point(607, 425);
            this.buttonStart.Name = "buttonStart";
            this.buttonStart.Size = new System.Drawing.Size(75, 48);
            this.buttonStart.TabIndex = 4;
            this.buttonStart.Text = "Start";
            this.buttonStart.UseVisualStyleBackColor = true;
            this.buttonStart.Click += new System.EventHandler(this.buttonStart_Click);
            // 
            // checkBoxMeshFolderSubdirs
            // 
            this.checkBoxMeshFolderSubdirs.AutoSize = true;
            this.checkBoxMeshFolderSubdirs.Checked = true;
            this.checkBoxMeshFolderSubdirs.CheckState = System.Windows.Forms.CheckState.Checked;
            this.checkBoxMeshFolderSubdirs.Location = new System.Drawing.Point(406, 429);
            this.checkBoxMeshFolderSubdirs.Name = "checkBoxMeshFolderSubdirs";
            this.checkBoxMeshFolderSubdirs.Size = new System.Drawing.Size(114, 17);
            this.checkBoxMeshFolderSubdirs.TabIndex = 5;
            this.checkBoxMeshFolderSubdirs.Text = "Include Subfolders";
            this.checkBoxMeshFolderSubdirs.UseVisualStyleBackColor = true;
            // 
            // listBoxProto
            // 
            this.listBoxProto.DrawMode = System.Windows.Forms.DrawMode.OwnerDrawFixed;
            this.listBoxProto.FormattingEnabled = true;
            this.listBoxProto.Location = new System.Drawing.Point(12, 13);
            this.listBoxProto.Name = "listBoxProto";
            this.listBoxProto.Size = new System.Drawing.Size(670, 407);
            this.listBoxProto.TabIndex = 6;
            // 
            // checkBoxUseHeaderFile
            // 
            this.checkBoxUseHeaderFile.AutoSize = true;
            this.checkBoxUseHeaderFile.Location = new System.Drawing.Point(15, 453);
            this.checkBoxUseHeaderFile.Name = "checkBoxUseHeaderFile";
            this.checkBoxUseHeaderFile.Size = new System.Drawing.Size(102, 17);
            this.checkBoxUseHeaderFile.TabIndex = 7;
            this.checkBoxUseHeaderFile.Text = "Use Header File";
            this.checkBoxUseHeaderFile.UseVisualStyleBackColor = true;
            this.checkBoxUseHeaderFile.CheckedChanged += new System.EventHandler(this.checkBoxUseHeaderFile_CheckedChanged);
            // 
            // textBoxHeaderFile
            // 
            this.textBoxHeaderFile.Enabled = false;
            this.textBoxHeaderFile.Location = new System.Drawing.Point(123, 451);
            this.textBoxHeaderFile.Name = "textBoxHeaderFile";
            this.textBoxHeaderFile.Size = new System.Drawing.Size(397, 20);
            this.textBoxHeaderFile.TabIndex = 8;
            // 
            // buttonHeaderFile
            // 
            this.buttonHeaderFile.Enabled = false;
            this.buttonHeaderFile.Location = new System.Drawing.Point(526, 450);
            this.buttonHeaderFile.Name = "buttonHeaderFile";
            this.buttonHeaderFile.Size = new System.Drawing.Size(75, 23);
            this.buttonHeaderFile.TabIndex = 9;
            this.buttonHeaderFile.Text = "Browse";
            this.buttonHeaderFile.UseVisualStyleBackColor = true;
            this.buttonHeaderFile.Click += new System.EventHandler(this.buttonHeaderFile_Click);
            // 
            // openHeaderFileDialog
            // 
            this.openHeaderFileDialog.DefaultExt = "txt";
            this.openHeaderFileDialog.Filter = "Mesh Parser Header File|MeshParserHeader.txt|Text Files|*.txt|All Files|*.*";
            // 
            // MeshParser
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(694, 480);
            this.Controls.Add(this.buttonHeaderFile);
            this.Controls.Add(this.textBoxHeaderFile);
            this.Controls.Add(this.checkBoxUseHeaderFile);
            this.Controls.Add(this.listBoxProto);
            this.Controls.Add(this.checkBoxMeshFolderSubdirs);
            this.Controls.Add(this.buttonStart);
            this.Controls.Add(this.labelMeshFolder);
            this.Controls.Add(this.buttonMeshFolderBrowse);
            this.Controls.Add(this.textBoxMeshFolder);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MeshParser";
            this.Text = "SAS MeshParser";
            this.Load += new System.EventHandler(this.MeshParser_Load);
            this.Shown += new System.EventHandler(this.MeshParser_Shown);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        //private FlickerFreeListBox listBoxProto;
        private System.Windows.Forms.FolderBrowserDialog meshFolderBrowserDialog;
        private System.Windows.Forms.TextBox textBoxMeshFolder;
        private System.Windows.Forms.Button buttonMeshFolderBrowse;
        private System.Windows.Forms.Label labelMeshFolder;
        private System.Windows.Forms.Button buttonStart;
        private System.Windows.Forms.CheckBox checkBoxMeshFolderSubdirs;
        private FlickerFreeListBox listBoxProto;
        private System.Windows.Forms.CheckBox checkBoxUseHeaderFile;
        private System.Windows.Forms.TextBox textBoxHeaderFile;
        private System.Windows.Forms.Button buttonHeaderFile;
        private System.Windows.Forms.OpenFileDialog openHeaderFileDialog;
    }
}

