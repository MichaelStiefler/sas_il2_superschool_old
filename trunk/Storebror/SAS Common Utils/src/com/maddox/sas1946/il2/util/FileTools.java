package com.maddox.sas1946.il2.util;

import java.io.File;
import java.io.IOException;

/**
 * "FileTools" Class of the "SAS Common Utils"
 * <p>
 * This Class is used to provide helper methods for File Handling.
 * <p>
 * 
 * @version 1.1.2
 * @since 1.1.2
 * @author SAS~Storebror
 */
public class FileTools {

    /**
     * Override default Constructor to avoid instanciation
     * @throws Exception
     * @since 1.1.2
     */
    private FileTools() throws Exception{
        throw new Exception("Class com.maddox.sas1946.il2.util.CommonTools cannot be instanciated!");
    }
    
    /**
     * Checks whether a file / directory represented by the given path is a symbolic link.
     * @param path
     *            The path of the file / directory in question
     * @return Whether or not the file / directory represents a symbolic link.
     * @since 1.1.2
     */
    public final static boolean isSymbolicLink(String path) {
        return doIsSymbolicLink(path);
    }
   
    /**
     * Checks whether a file / directory is a symbolic link.
     * @param file
     *            The file / directory in question
     * @return Whether or not the file / directory represents a symbolic link.
     * @since 1.1.2
     */
    public final static boolean isSymbolicLink(File file) {
        return doIsSymbolicLink(file);
    }

    /**
     * Resolves a file / directory path representing a symbolic link to it's real file / directory path on the given file system.
     * @param path
     *            The path of the file / directory representing a symbolic link
     * @return The real file / directory path on the given file system.
     * @since 1.1.2
     */
    public final static String resolveSymbolicLink(String path) {
        return doResolveSymbolicLink(path);
    }
    
    /**
     * Resolves a file / directory representing a symbolic link to it's real file / directory on the given file system.
     * @param file
     *            The file / directory in question
     * @return The real file / directory on the given file system.
     * @since 1.1.2
     */
    public final static File resolveSymbolicLink(File file) {
        return doResolveSymbolicLink(file);
    }

    // *****************************************************************************************************************************************************************************************************
    // Private implementation section.
    // Do whatever you like here but keep it private to this class.
    // *****************************************************************************************************************************************************************************************************

    private final static boolean doIsSymbolicLink(File file) {
        try {
            return doIsSymbolicLink(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private final static boolean doIsSymbolicLink(String path) {
        if (!loadNative()) return false;
        String canonicalPath;
        try {
            canonicalPath = new File(path).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return jniIsSymbolicLink(canonicalPath);
    }
    
    private final static String doResolveSymbolicLink(String path) {
        if (!loadNative()) return path;
        String canonicalPath;
        try {
            canonicalPath = new File(path).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return path;
        }
        return jniResolveSymbolicLink(canonicalPath);
    }
    
    private final static File doResolveSymbolicLink(File file) {
        String resolvedPath;
        try {
            resolvedPath=doResolveSymbolicLink(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            resolvedPath=doResolveSymbolicLink(file.getPath());
        }
        return new File(resolvedPath);
    }

    private long start;
    
    // *****************************************************************************************************************************************************************************************************
    // Native Methods implementation section.
    // Do whatever you like here but keep it private to this class.
    // *****************************************************************************************************************************************************************************************************

    private final static boolean loadNative() {
        if (nativeLoaded) return true;
        if (initAttemptDone) return false;
        initAttemptDone = true;
        try {
            System.loadLibrary("SAS Common Utils");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        nativeLoaded = true;
        return true;
    }

    private static boolean nativeLoaded = false;
    private static boolean initAttemptDone = false;
    private static native boolean jniIsSymbolicLink(String path); 
    private static native String jniResolveSymbolicLink(String path); 
}
