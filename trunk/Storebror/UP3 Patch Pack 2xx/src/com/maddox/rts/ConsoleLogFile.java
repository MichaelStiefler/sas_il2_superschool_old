package com.maddox.rts;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;

import com.maddox.il2.engine.Config;

class ConsoleLogFile implements ConsoleOut {

    public void flush() {
        this.f.flush();
        // TODO: +++ Instant Log by SAS~Storebror +++
        if (!Config.cur.bInstantLog) {
            return;
        }
        this.f.close();
        try {
            this.f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(this.fileName, 0), true));
        } catch (IOException theIOException) {
        }
        // TODO: --- Instant Log by SAS~Storebror ---
    }

    public void println(String s) {
        this.f.println(s);
        // TODO: +++ Instant Log by SAS~Storebror +++
        if (Config.cur.bInstantLog) this.flush();
        // TODO: --- Instant Log by SAS~Storebror ---
    }

    public void type(String s) {
        if (Console.bTypeTimeInLogFile) {
            Calendar calendar = Calendar.getInstance();
            this.f.print("[" + this.shortDate.format(calendar.getTime()) + "]\t");
        }
        this.f.print(s);
        // TODO: +++ Instant Log by SAS~Storebror +++
        if (Config.cur.bInstantLog) this.flush();
        // TODO: --- Instant Log by SAS~Storebror ---
    }

    public void close() {
        Calendar calendar = Calendar.getInstance();
        this.f.println();
        this.f.println("[" + this.longDate.format(calendar.getTime()) + "] -------------- END log session -------------");
        this.f.close();
    }

    public ConsoleLogFile(String s) throws FileNotFoundException {
        // TODO: +++ Instant Log by SAS~Storebror +++
        this.fileName = s;
        // TODO: --- Instant Log by SAS~Storebror ---
        this.f = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(s, 0), true));
        Calendar calendar = Calendar.getInstance();
        this.longDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        this.shortDate = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        this.f.println();
        this.f.println("[" + this.longDate.format(calendar.getTime()) + "] ------------ BEGIN log session -------------");
        this.f.println();
    }

    private PrintWriter f;
    private DateFormat  longDate;
    private DateFormat  shortDate;
    // TODO: +++ Instant Log by SAS~Storebror +++
    private String      fileName;
    // TODO: --- Instant Log by SAS~Storebror ---
}
