package com.sas1946.fac.tools.dservercontroller;

public class MySqlSettings {
    private String address;
    private int    port;
    private String user;
    private String pass;
    private String dbName;

    public MySqlSettings(String address, int port, String user, String pass, String dbName) {
        this.setAddress(address);
        this.setPort(port);
        this.setUser(user);
        this.setPass(pass);
        this.setDbName(dbName);
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
}
