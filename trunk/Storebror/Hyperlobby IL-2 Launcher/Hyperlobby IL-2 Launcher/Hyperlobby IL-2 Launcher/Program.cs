using System;
using System.Collections.Generic;
using System.Windows.Forms;

namespace Hyperlobby_IL_2_Launcher
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new HL_IL2_Launcher());
        }
    }
}
