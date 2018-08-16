package com.sas1946.fac.tools.dservercontroller;

public class MySqlSettings {
    private String address;
    private int    port;
    private String user;
    private String pass;
    private String dbName;
    private String timezone;

    public MySqlSettings(String address, int port, String user, String pass, String dbName, String timezone) {
        this.setAddress(address);
        this.setPort(port);
        this.setUser(user);
        this.setPass(pass);
        this.setDbName(dbName);
        this.setTimezone(timezone);
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    public String getDbName() {
        return this.dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
