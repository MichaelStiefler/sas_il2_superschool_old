package com.sas1946.fac.tools.dservercontroller;

public class RConSettings {
    private String address;
    private int    port;
    private String user;
    private String pass;

    public RConSettings(String address, int port, String user, String pass) {
        this.setAddress(address);
        this.setPort(port);
        this.setUser(user);
        this.setPass(pass);
    }

    public RConSettings(String address, int port) {
        this(address, port, null, null);
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
}
