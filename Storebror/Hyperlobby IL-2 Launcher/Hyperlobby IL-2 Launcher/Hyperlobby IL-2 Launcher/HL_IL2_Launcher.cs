using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Text;
using System.Windows.Forms;
using System.Collections;
using Org.Mentalis.Files;
using System.Diagnostics;

namespace Hyperlobby_IL_2_Launcher
{
    public partial class HL_IL2_Launcher : Form
    {
        public struct GameDef
        {
            public Button button;
            public string name;
            public string path;
            public string player;
            public bool startManually;
        }

        List<GameDef> theGameDefs = new List<GameDef>();
        Font buttonFont;
        string[] commandLineArgs;
        IniReader iniReader;

        public HL_IL2_Launcher()
        {
            InitializeComponent();
        }

        private void HL_IL2_Launcher_Load(object sender, EventArgs e)
        {
            this.commandLineArgs = Environment.GetCommandLineArgs();
            if (this.commandLineArgs.Length == 4)
            {
                if (!this.commandLineArgs[2].Equals("-name", StringComparison.InvariantCultureIgnoreCase))
                {
                    this.Close();
                    return;
                }
            }
            else if (this.commandLineArgs.Length != 1)
            {
                this.Close();
                return;
            }

            string appPath = AppDomain.CurrentDomain.BaseDirectory;
            string iniFile = appPath + "hl_il2_launcher.ini";
            iniReader = new IniReader(iniFile);
            int opacity = iniReader.ReadInteger("Common", "opacity", 100);
            string fontName = iniReader.ReadString("Common", "fontname");
            int fontSize = iniReader.ReadInteger("Common", "fontsize", 24);
            bool fontBold = iniReader.ReadInteger("Common", "fontbold", 1) != 0;
            bool fontItalic = iniReader.ReadInteger("Common", "fontitalic", 0) != 0;
            FontStyle fontStyle = FontStyle.Regular;
            if (fontBold) fontStyle |= FontStyle.Bold;
            if (fontItalic) fontStyle |= FontStyle.Italic;
            if (fontName.Length < 1 || fontName.Equals("default", StringComparison.InvariantCultureIgnoreCase))
                buttonFont = new Font(FontFamily.GenericSansSerif, fontSize, fontStyle, GraphicsUnit.Point);
            else
                buttonFont = new Font(fontName, fontSize, fontStyle, GraphicsUnit.Point);
            this.Opacity = ((double)opacity) / 100.0;

            TableLayoutPanel table = new TableLayoutPanel();
            table.Location = new Point(1, 1);
            table.Size = new Size(1, 1);
            table.AutoSize = true;
            table.Name = "Buttons";
            table.ColumnCount = 1;
            table.RowCount = 0;
            table.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            table.GrowStyle = System.Windows.Forms.TableLayoutPanelGrowStyle.AddRows;
            this.Controls.Add(table);

            this.AutoSize = true;
            this.AutoSizeMode = AutoSizeMode.GrowAndShrink;
            ArrayList sections = iniReader.GetSectionNames();

            for (int index = 0; index < 100; index++)
            {
                string appName = String.Format("Game_{0:D2}", index);
                if (!sections.Contains(appName)) continue;
                iniReader.Section = appName;
                GameDef gameDef = new GameDef();
                gameDef.name = iniReader.ReadString("name");
                if (gameDef.name == null || gameDef.name.Length < 1) continue;
                gameDef.button = new Button();

                gameDef.button.Click += new System.EventHandler(button_Click);
                gameDef.button.Enter += new System.EventHandler(button_MouseEnter);
                gameDef.button.MouseEnter += new System.EventHandler(button_MouseEnter);
                gameDef.button.GotFocus += new System.EventHandler(button_GotFocus);
                gameDef.button.LostFocus += new System.EventHandler(button_LostFocus);

                gameDef.button.FlatStyle = FlatStyle.Flat;
                int borderSize = (int)fontSize/5;
                if (borderSize > 10) borderSize = 10;
                if (borderSize < 1) borderSize = 1;
                gameDef.button.FlatAppearance.BorderSize = borderSize;
                gameDef.button.FlatAppearance.BorderColor = System.Drawing.Color.Blue;
                gameDef.button.Font = this.buttonFont;
                gameDef.button.Text = gameDef.name;
                gameDef.button.TextAlign = ContentAlignment.MiddleCenter;
                gameDef.button.Padding = new System.Windows.Forms.Padding(3);
                gameDef.button.AutoSize = true;
                gameDef.button.AutoSizeMode = AutoSizeMode.GrowAndShrink;
                gameDef.button.AutoEllipsis = false;
                gameDef.button.Dock = DockStyle.Fill;
                table.Controls.Add(gameDef.button);
                gameDef.path = iniReader.ReadString("path");
                if (gameDef.path == null || gameDef.path.Length < 1) continue;
                if (gameDef.path.EndsWith("\\")) gameDef.path = gameDef.path.TrimEnd("\\".ToCharArray());
                if (gameDef.path.EndsWith("/")) gameDef.path = gameDef.path.TrimEnd("/".ToCharArray());
                gameDef.player = iniReader.ReadString("player");
                if (gameDef.player.Contains(" "))
                {
                    gameDef.player = "\"" + gameDef.player + "\"";
                }
                gameDef.startManually = iniReader.ReadInteger("autoconnect", 1) == 0;
                theGameDefs.Add(gameDef);
            }

        }

        private void setServerIP(GameDef gameDef)
        {
            string iniFile = gameDef.path + "\\conf.ini";
            IniReader confIniReader = new IniReader(iniFile);
            string[] addressPort = this.commandLineArgs[1].Split(new Char[] { ':' });
            if (addressPort.Length > 1)
            {
                confIniReader.Write("NET", "remoteHost", addressPort[0]);
                confIniReader.Write("NET", "remotePort", addressPort[1]);
            }
        }

        private void startIl2(GameDef gameDef)
        {
            this.Visible = false;
            this.Update();

            bool startAgain = true;
            do
            {
                // Prepare the process to run
                ProcessStartInfo start = new ProcessStartInfo();
                // Enter in the command line arguments, everything you would enter after the executable name itself
                if (this.commandLineArgs.Length > 1)
                {
                    if (gameDef.startManually)
                    {
                        this.setServerIP(gameDef);
                    }
                    else
                    {
                        start.Arguments = this.commandLineArgs[1] + " -name ";
                        if (gameDef.player.Length < 1)
                            start.Arguments += this.commandLineArgs[3];
                        else
                            start.Arguments += gameDef.player;
                    }
                }
                // Enter the executable to run, including the complete path
                start.FileName = "il2fb.exe";
                // Do you want to show a console window?
                start.WorkingDirectory = gameDef.path;
                start.UseShellExecute = true;
                start.ErrorDialog = true;

                // Run the external process & wait for it to finish
                using (Process proc = Process.Start(start))
                {
                    proc.WaitForExit();

                    // Retrieve the app's exit code
                    int exitCode = proc.ExitCode;
                    Debug.WriteLine("EXIT Code for " + start.WorkingDirectory + "\\il2fb.exe:" + exitCode);
                    if (exitCode == 0)
                    {
                        startAgain = false;
                    }
                    else
                    {
                        DialogResult dr = MessageBox.Show("Launching IL-2 failed (Exit Code=" + exitCode + "), do you want to retry launching the game?",
                            "IL-2 Launch failed",
                            MessageBoxButtons.YesNo,
                            MessageBoxIcon.Error,
                            MessageBoxDefaultButton.Button1);
                        if (dr != DialogResult.Yes) startAgain = false;
                    }
                }
            } while (startAgain);

            this.Visible = true;
            this.Update();
        }

        private void button_Click(Object sender, System.EventArgs e)
        {
            for (int index = 0; index < theGameDefs.Count; index++)
            {
                if (sender.Equals(theGameDefs[index].button))
                {
                    this.startIl2(theGameDefs[index]);
                }
            }
        }

        private void button_MouseEnter(object sender, System.EventArgs e)
        {
            ((Button)sender).Focus();
        }

        private void button_GotFocus(object sender, System.EventArgs e)
        {
            ((Button)sender).BackColor = Color.Blue;
        }

        private void button_LostFocus(object sender, System.EventArgs e)
        {
            ((Button)sender).BackColor = Color.Black;
        }

    }
}
