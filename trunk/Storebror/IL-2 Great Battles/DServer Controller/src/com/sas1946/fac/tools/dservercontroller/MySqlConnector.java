package com.sas1946.fac.tools.dservercontroller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnector {
    private MySqlConnector() {
    }

    private static MySqlConnector instance;
    private static Connection     conn = null;

    public static MySqlConnector getInstance() {
        return instance;
    }

    static {
        instance = new MySqlConnector();
        try {
            MySqlSettings sqlset = Settings.getInstance().getMySqlSettings();
            conn = DriverManager.getConnection("jdbc:mysql://" + sqlset.getAddress() + ":" + sqlset.getPort() + "/" + sqlset.getDbName() + "?user=" + sqlset.getUser() + "&password=" + sqlset.getPass());
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-2);
        }
    }
}
