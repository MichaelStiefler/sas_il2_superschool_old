package com.sas1946.fac.tools;

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

    public RConSettings getrConSettings() {
        return this.rConSettings;
    }

    public void setrConSettings(RConSettings rConSettings) {
        this.rConSettings = rConSettings;
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
        instance.rConSettings = new RConSettings(prefs.node("rcon").get("address", null), prefs.node("rcon").getInt("port", 8991), prefs.node("rcon").get("user", null), prefs.node("rcon").get("pass", null));
    }
}
