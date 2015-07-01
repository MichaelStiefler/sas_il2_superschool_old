// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   Console.java

package com.maddox.rts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.maddox.il2.ai.EventLog;
import com.maddox.il2.engine.Config;

// Referenced classes of package com.maddox.rts:
//            ConsoleOut, Console, HomePath

class ConsoleLogFile implements ConsoleOut {

	public void flush() {
		// TODO: ++ Instant, persistent and rotating logs Mod ++
		//consoleLogPrintWriter.flush();
		this.flushAndCheckIPRLLogFileSize();
		// TODO: -- Instant, persistent and rotating logs Mod --
	}

	// TODO: ++ Instant, persistent and rotating logs Mod ++
	public void flushAndCheckIPRLLogFileSize() {
		if (this.consoleLogPrintWriter == null)
			return;
		this.consoleLogPrintWriter.flush();
		this.consoleLogPrintWriter.close();
		
		if (!historySettingsInitialized) {
			if (Config.cur != null) {
				if (Config.cur.ini != null) {
					try {
						EventLog.checkIPRLModSettings();
						historySettingsInitialized = true;
					} catch (Exception e) {}
				}
			}
		}

		if (historySettingsInitialized && EventLog.maxNumHistoryFiles > 0) {
			File eventLogFile = new File(HomePath.toFileSystemName(fileName, 0));
			if (eventLogFile.length() > EventLog.maxLogLen) {
				String historyFileName = fileName;
				if (fileName.indexOf(".") > 0) {
					historyFileName = fileName.substring(0, fileName.lastIndexOf("."));
				}
				historyFileName += "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date(eventLogFile.lastModified()));
				if (fileName.indexOf(".") > 0) {
					historyFileName += fileName.substring(fileName.lastIndexOf("."), fileName.length());
				}
				new File(HomePath.toFileSystemName(EventLog.historyLogFolder, 0)).mkdirs();
				eventLogFile.renameTo(new File(HomePath.toFileSystemName(EventLog.historyLogFolder, 0) + "\\" + historyFileName));
			}
		}
		try {
			consoleLogPrintWriter = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(this.fileName, 0), true));
		} catch (Exception e) {
		}
	}

	// TODO: -- Instant, persistent and rotating logs Mod --

	public void println(String s) {
		consoleLogPrintWriter.println(s);
		this.flush();
	}

	public void type(String s) {
		if (Console.bTypeTimeInLogFile) {
			Calendar calendar = Calendar.getInstance();
			consoleLogPrintWriter.print("[" + shortDate.format(calendar.getTime()) + "]\t");
		}
		consoleLogPrintWriter.print(s);
		this.flush();
	}

	public void close() {
		Calendar calendar = Calendar.getInstance();
		consoleLogPrintWriter.println("[" + longDate.format(calendar.getTime())
				+ "] -------------- END log session -------------");
		consoleLogPrintWriter.close();
	}

	public ConsoleLogFile(String s) throws FileNotFoundException {
		this.fileName = s;
		consoleLogPrintWriter = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(s, 0), true));
		Calendar calendar = Calendar.getInstance();
		longDate = DateFormat.getDateTimeInstance(2, 2);
		shortDate = DateFormat.getTimeInstance(2);
		consoleLogPrintWriter.println("[" + longDate.format(calendar.getTime())
				+ "] ------------ BEGIN log session -------------");
	}

	// TODO: ++ Instant, persistent and rotating logs Mod ++
	private boolean historySettingsInitialized = false;
	// TODO: -- Instant, persistent and rotating logs Mod --
	// private static final boolean useCalendar = true;
	private PrintWriter consoleLogPrintWriter;
	private DateFormat longDate;
	private DateFormat shortDate;
	private String fileName;
}
