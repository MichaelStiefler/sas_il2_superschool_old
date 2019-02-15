package com.sas1946.AircraftClassFileParser;

import java.io.File;
import java.nio.file.Paths;

public class Main {
    private static String currentPath;
    
    public static void main(String[] args) {
        ClassFileParser classFileParser = new ClassFileParser();
        currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        classFileParser.parseFiles(currentPath + File.separator + "aircraft_source");        
    }
}
