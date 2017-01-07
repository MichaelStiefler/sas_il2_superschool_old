package com.sas1946;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;

public final class SASClassResolver {

    private static String myJarFileName = "";

    private static boolean isHexadecimal(String s) {
        try {
            Integer.parseInt(s, 16);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

    private static boolean isDecimal(String s) {
        try {
            Integer.parseInt(s, 10);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

    private static String getJarFileName() {
        try {
            return new File(SASClassResolver.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getName();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void listFiles(List<String> list, File file) {
        File afile[] = file.listFiles();
        for (int i = 0; i < afile.length; i++)
            if (afile[i].isFile())
                list.add(afile[i].getPath());
    }

    private static boolean resolveClass(File file) {
        String fileName = "";
        boolean supposedToBeClass = false;
        try {
            fileName = file.getName();
            if (fileName.indexOf(".") == 0) {
                if (fileName.length() == 16 && isHexadecimal(fileName) || isDecimal(fileName)) {
                    if (file.getName().trim().compareToIgnoreCase(myJarFileName.trim()) != 0)
                        supposedToBeClass = true;
                }
            }
            JavaClass javaclass = (new ClassParser(file.getPath())).parse();
            if (javaclass.getMajor() < 45 || javaclass.getMajor() > 47) {
                println("File " + file.getPath() + " resolves to Java Class " + javaclass.getClassName() + " but has wrong Major Verion " + javaclass.getMajor());
                return false;
            }
            String s = javaclass.getClassName().replace('.', File.separatorChar) + ".class";
            File file1 = new File(s);
            file1.getParentFile().mkdirs();
            boolean retVal = file.renameTo(file1);
            if (!retVal) {
                println("Couldn't rename file " + file.getPath() + " to " + file1.getPath());
            }
            return retVal;
        } catch (Exception e) {
            if (supposedToBeClass) {
                println("Attempting to resolve class for " + file.getPath() + " caused the following Exception:");
                printException(e);
                return false;
            }
        }
        if (supposedToBeClass)
            println("Cannot resolve class: " + file.getName());
        return false;
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void print(String s) {
        System.out.print(s);
    }

    private static void printException(Exception exception) {
        println("Exception: " + exception.getClass().getName());
        println("Message: " + exception.getMessage());
        println("");
        exception.printStackTrace();
    }

    private static void showHeader() {
        println("                   ┌───────────────────────────────────────────────────────┐");
        println("                   │                                                       │");
        println("                   │                SAS Class Resolver v1.0                │");
        println("                   │                                                       │");
        println("                   ├───────────────────────────────────────────────────────┤");
        println("                   │                                                       │");
        println("                   │                    Featured by the                    │");
        println("                   │                Special Aircraft Service               │");
        println("                   │                 http://www.sas1946.com                │");
        println("                   │          Compatible with all IL-2 game versions       │");
        println("                   │                                                       │");
        println("                   │                   Released under the                  │");
        println("                   │    \"Do whatever the fuck you want with it\" license    │");
        println("                   │      see http://storebror.it.cx/sas/dwtfywwi.txt      │");
        println("                   │                                                       │");
        println("                   └───────────────────────────────────────────────────────┘");
        println("");
    }

    private static void showHelp() {
        println("┌──────────────────────────────────────────────────────────────────────────────────────────────┐");
        println("│                                                                                              │");
        println("│ Usage:                                                                                       │");
        println("│ java -jar SASClassResolver.jar [<path of hashed files> or <hashed file>]                     │");
        println("│                                                                                              │");
        println("├──────────────────────────────────────────────────────────────────────────────────────────────┤");
        println("│                                                                                              │");
        println("│ If path/file is omitted, current path will be used.                                          │");
        println("│                                                                                              │");
        println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
        println("");
    }

    public static void main(String args[]) {
        showHeader();
        if (args.length < 1)
            args = new String[] { "." };
        if (args.length == 1) {
            myJarFileName = getJarFileName();
            try {
                File file = new File(args[0]);
                if (file.isDirectory()) {
                    ArrayList<String> arraylist = new ArrayList<String>();
                    listFiles(arraylist, file);
                    for (int i = 0; i < arraylist.size(); i++)
                        resolveClass(new File(arraylist.get(i).toString()));
                } else
                    resolveClass(file);
            } catch (Exception exception) {
                printException(exception);
            }
        } else {
            showHelp();
        }
    }
}
