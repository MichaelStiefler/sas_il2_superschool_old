package com.maddox.rts;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;

class ConsoleLogFile
    implements ConsoleOut
{

    public void flush()
    {
        f.flush();
        f.close();
        try {
          f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(this.fileName, 0), true));
        } catch (java.io.IOException theIOException) {}
    }

    public void println(String s)
    {
        f.println(s);
        this.flush();
    }

    public void type(String s)
    {
        if(Console.bTypeTimeInLogFile)
        {
            Calendar calendar = Calendar.getInstance();
            f.print("[" + shortDate.format(calendar.getTime()) + "]\t");
        }
        f.print(s);
        this.flush();
    }

    public void close()
    {
        Calendar calendar = Calendar.getInstance();
        f.println("[" + longDate.format(calendar.getTime()) + "] -------------- END log session -------------");
        f.close();
    }

    public ConsoleLogFile(String s)
        throws FileNotFoundException
    {
        this.fileName = s;
        f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(s, 0), true));
        Calendar calendar = Calendar.getInstance();
        longDate = DateFormat.getDateTimeInstance(2, 2);
        shortDate = DateFormat.getTimeInstance(2);
        f.println("[" + longDate.format(calendar.getTime()) + "] ------------ BEGIN log session -------------");
    }

    private PrintWriter f;
    private DateFormat longDate;
    private DateFormat shortDate;
    private String fileName;
}
