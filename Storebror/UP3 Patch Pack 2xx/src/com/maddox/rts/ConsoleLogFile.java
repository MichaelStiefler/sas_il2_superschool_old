package com.maddox.rts;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;

class ConsoleLogFile implements ConsoleOut {

    public void flush() {
        f.flush();
    }

    public void println(String s) {
        f.println(s);
    }

    public void type(String s) {
//        if (NetEnv.isServer()) {
//            if (s.startsWith("chat ")) return;
//            if (s.startsWith("timeout ")) return;
//            if (s.startsWith("Chat: Server: ")) return;
//        }
        if (Console.bTypeTimeInLogFile) {
            Calendar calendar = Calendar.getInstance();
            f.print("[" + shortDate.format(calendar.getTime()) + "]\t");
        }
        f.print(s);
//        if (!s.endsWith("\n"))
//            f.println(); // Patch Pack 107, always add line break at end of log line!
    }

    public void close() {
        Calendar calendar = Calendar.getInstance();
        f.println("[" + longDate.format(calendar.getTime()) + "] -------------- END log session -------------");
        f.close();
    }

    public ConsoleLogFile(String s) throws FileNotFoundException {
        f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(s, 0), true));
        Calendar calendar = Calendar.getInstance();
        longDate = DateFormat.getDateTimeInstance(2, 2);
        shortDate = DateFormat.getTimeInstance(2);
        f.println("[" + longDate.format(calendar.getTime()) + "] ------------ BEGIN log session -------------");
    }

//    private static final boolean useCalendar = true;
    private PrintWriter f;
    private DateFormat  longDate;
    private DateFormat  shortDate;
}
