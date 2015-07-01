using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using System.Diagnostics;

namespace _3doShrinker
{
    static class Program
    {
        /// <summary>
        /// Der Haupteinstiegspunkt für die Anwendung.
        /// </summary>
        [STAThread]
        static void Main()
        {
            TextWriterTraceListener traceFile = new TextWriterTraceListener(System.IO.File.CreateText("DebugLog.txt"));
            Debug.Listeners.Add(traceFile);
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Shrinker());
            foreach (TraceListener tl in Debug.Listeners) tl.Flush();
        }
    }
}
