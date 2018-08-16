package com.sas1946.fac.tools.dservercontroller;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;

public class Settings {

    private Settings() {
    }

    private static Settings instance;

    private RConSettings    rConSettings;
    private MySqlSettings   mySqlSettings;

    public RConSettings getrConSettings() {
        return this.rConSettings;
    }

    public void setrConSettings(RConSettings rConSettings) {
        this.rConSettings = rConSettings;
    }

    public MySqlSettings getMySqlSettings() {
        return mySqlSettings;
    }

    public void setMySqlSettings(MySqlSettings mySqlSettings) {
        this.mySqlSettings = mySqlSettings;
    }

    public static Settings getInstance() {
        return instance;
    }

    static {
        instance = new Settings();
        Ini ini = null;
        try {
            ini = new Ini(new File("dsc.ini"));
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        final Preferences prefs = new IniPreferences(ini);
        instance.rConSettings = new RConSettings(
                prefs.node("rcon").get("address", null),
                prefs.node("rcon").getInt("port", 8991),
                prefs.node("rcon").get("user", null),
                prefs.node("rcon").get("pass", null));
        instance.mySqlSettings = new MySqlSettings(
                prefs.node("mysql").get("address", "localhost"),
                prefs.node("mysql").getInt("port", 3306),
                prefs.node("mysql").get("user", null),
                prefs.node("mysql").get("pass", null),
                prefs.node("mysql").get("database", "dservercontroller"),
                prefs.node("mysql").get("timezone", "UTC"));
    }
}
