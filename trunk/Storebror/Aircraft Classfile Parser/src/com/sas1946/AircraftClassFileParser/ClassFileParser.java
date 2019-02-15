package com.sas1946.AircraftClassFileParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class ClassFileParser {
    public void parseFiles(String directoryName) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                if (file.getName().matches("^.*\\.java")) {
                    System.out.println(file.getAbsolutePath());
//                    if (file.getName().equals("AR_234B2NJ.java")) {
//                        this.parseFile(file);
//                        break;
//                    }
                    this.parseFile(file);
                }
            } else if (file.isDirectory()) {
                this.parseFiles(file.getAbsolutePath());
            }
        }
    }

    private void parseFile(File file) {
        try {
            List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
            
            boolean weaponTriggersRegister = false;
            boolean weaponHooksRegister = false;
            String packageName = null;
            String methodName = null;
            int curBracketLevel = 0;
            int oldBracketLevel = 0;
           
            for (int listIndex=0; listIndex<list.size(); listIndex++) {
                String line = list.get(listIndex);
                
                // Get Package Name
                if (line.trim().startsWith("package")) {
                    packageName = getPackageName(line);
                } else if (packageName != null) {
                    // Trim full qualified names
                    if (line.contains(packageName + ".") && !line.contains("\"" + packageName + ".") && !line.trim().startsWith("import")) {
                        line = line.replace(packageName + ".", "");
                    }
                }
                
                // Change all non-Array Cockpit Class declarations (single cockpit planes) to Array Style
                line = line.replaceAll("^(.*Property\\.set\\(.+,\\s\\\"cockpitClass\\\",\\s)Cockpit(.+)\\);", "$1new Class[] { Cockpit$2 });");
                
                
//                if (line.trim().matches("^Property.set\\([^,]*,\\s\\\"cockpitClass\\\",\\sCockpit.*\\);")) {
//                    line = line.substring(0, line.indexOf(" Cockpit")) + " new Class[] {" + line.substring(line.indexOf(" Cockpit"), line.lastIndexOf(")")) + "} );";
//                }
                
                // Remove surplus comments after switch cases
                line = line.replaceAll("^(\\s*case\\s\\d+:)\\s\\/\\/\\s\\'.+\\'", "$1");
                
                // Replace "CLASS.THIS()" be real class reference
                line = line.replaceAll("^(\\s+)Class\\s((\\w|\\d)+)\\s=\\sCLASS\\.THIS\\(\\);", "$1Class $2 = " + file.getName().replaceFirst("[.][^.]+$", "") + ".class;");
                
                // Remove surplus FlightModelMain typecast
                line = line.replace("((FlightModelMain) (super.FM))", "this.FM");
                
                // Remove surplus Tuple3d typecast 1
                line = line.replace("((Tuple3d) (this.FM.Loc))", "this.FM.Loc");

                // Remove surplus Tuple3d typecast 2
                line = line.replace("((Tuple3d) (point3d))", "point3d");

                // Set Math.PI where it applies
                line = line.replace("3.1415926535897931D", "Math.PI");

                // If the current line might be a method declaration, store the method name
                if (curBracketLevel == 1 && !line.trim().isEmpty() && !line.contains(";") && !line.contains("//") && !line.contains("/*")) {
                    if (line.contains("(") && line.contains(")")) {
                        String[] parts = line.trim().substring(0, line.trim().indexOf("(")).split(" ");
                        methodName = parts[parts.length-1];
                    }
                }
                
                if (methodName != null) {
                    // Change all super calls to this calls, except for real super method calls
                    line = line.replaceAll("^(.*)super\\.((?!" + methodName + "\\().*)", "$1this.$2");
                }

                list.set(listIndex, line);
                oldBracketLevel = curBracketLevel;
                curBracketLevel += occurances(line, "{");
                curBracketLevel -= occurances(line, "}");
                
                // If we reached the end of a method, reset method name to null
                if (oldBracketLevel > curBracketLevel && curBracketLevel == 1)
                    methodName = null;
                
                while (line.matches("^.*static\\sClass\\s+class\\$com\\$maddox.*;")) {
                    list.remove(listIndex);
                    line = list.get(listIndex);
                }
                    
                if (line.trim().startsWith("// Decompiled by Jad"))
                    removeCommentBlock(list, listIndex);
                else if (line.trim().startsWith("// Referenced classes"))
                    removeCommentBlock(list, listIndex);
                else if (line.trim().startsWith("static Class _mthclass")
                        || line.trim().startsWith("private static final float floatindex(float")
                        || line.trim().startsWith("private static float floatindex(float f"))
                    removeMethod(list, listIndex);
                
                if (line.contains("weaponTriggersRegister"))
                    weaponTriggersRegister = true;
                if (line.contains("weaponHooksRegister"))
                    weaponHooksRegister = true;
                if (weaponTriggersRegister && weaponHooksRegister) {
                    removeWeaponSlots(list, listIndex);
                    weaponTriggersRegister = weaponHooksRegister = false;
                }
            }
            
            
            Files.write(file.toPath(),list,Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void removeCommentBlock(List<String>list, int startIndex) {
        while (startIndex < list.size() && list.get(startIndex).trim().startsWith("//")) {
            list.remove(startIndex);
        }
        while (startIndex < list.size() && list.get(startIndex).trim().isEmpty()) {
            list.remove(startIndex);
        }
    }

    private void removeMethod(List<String>list, int startIndex) {
        String line;
        int bracketCounter = list.get(startIndex).contains("{")?1:0;
        list.remove(startIndex);
        if (bracketCounter == 0) {
            while (true) {
                line = list.get(startIndex);
                list.remove(startIndex);
                bracketCounter += occurances(line, "{");
                bracketCounter -= occurances(line, "}");
                if (bracketCounter > 0)
                    break;
            }
        }
        while (true) {
            line = list.get(startIndex);
            list.remove(startIndex);
            bracketCounter += occurances(line, "{");
            bracketCounter -= occurances(line, "}");
            if (bracketCounter < 1) break;
        }
        while (startIndex < list.size() && list.get(startIndex).trim().isEmpty()) {
            list.remove(startIndex);
        }
    }
    
    private int occurances(String haystack, String needle) {
        int count = 0;
        int lastIndex = 0;
        while(lastIndex != -1){
            lastIndex = haystack.indexOf(needle,lastIndex);
            if(lastIndex != -1){
                count ++;
                lastIndex += needle.length();
            }
        }
        return count;
    }
    
    private void removeWeaponSlots(List<String>list, int startIndex) {
        while (!list.get(startIndex++).contains(";"));
        int bracketCounter = 1;
        String line;
        while (true) {
            line = list.get(startIndex);
            bracketCounter += occurances(line, "{");
            bracketCounter -= occurances(line, "}");
            if (bracketCounter < 1) break;
            list.remove(startIndex);
        }
        while (startIndex < list.size() && list.get(startIndex).trim().isEmpty()) {
            list.remove(startIndex);
        }
    }
    
    private String getPackageName(String line) {
        String packageName = line.trim();
        packageName = packageName.replace("package ", "");
        return packageName.substring(0, packageName.length() - 1);
    }
    
}
