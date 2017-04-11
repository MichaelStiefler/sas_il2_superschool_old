package com.maddox.il2.objects.ships;

import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.vehicles.radios.TypeHasHayRake;
import com.maddox.rts.SectFile;

public abstract class ShipAshe extends Ship
{
    public static class HMSFurious extends BigshipGeneric
        implements TgtShip, TypeHasHayRake
    {
        public HMSFurious(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSArkRoyal extends BigshipGeneric
        implements TgtShip, TypeHasHayRake
    {
        public HMSArkRoyal(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSEagle extends BigshipGeneric
        implements TgtShip, TypeHasHayRake
    {
        public HMSEagle(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class RMCaioDuilio extends BigshipGeneric
        implements TgtShip
    {
        public RMCaioDuilio(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSFiji extends BigshipGeneric
        implements TgtShip
    {
        public HMSFiji(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSWarspite extends BigshipGeneric
        implements TgtShip
    {
        public HMSWarspite(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSNelson extends BigshipGeneric
        implements TgtShip
    {
        public HMSNelson(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSTartar extends BigshipGeneric
        implements TgtShip
    {
        public HMSTartar(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSNubian extends BigshipGeneric
        implements TgtShip
    {
        public HMSNubian(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSMatabele extends BigshipGeneric
        implements TgtShip
    {
        public HMSMatabele(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSKipling extends BigshipGeneric
        implements TgtShip
    {
        public HMSKipling(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMASNapier extends BigshipGeneric
        implements TgtShip
    {
        public HMASNapier(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSJavelin extends BigshipGeneric
        implements TgtShip
    {
        public HMSJavelin(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSJupiter extends BigshipGeneric
        implements TgtShip
    {
        public HMSJupiter(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSCossack extends BigshipGeneric
        implements TgtShip
    {
        public HMSCossack(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
        {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }
    
    private static String[] bigShips = {
            "HMSCossack",
            "HMSJupiter",
            "HMSJavelin",
            "HMASNapier",
            "HMSKipling",
            "HMSMatabele",
            "HMSNubian",
            "HMSTartar",
            "HMSWarspite",
            "HMSNelson",
            "HMSFiji",
            "RMCaioDuilio",
            "HMSEagle",
            "HMSArkRoyal",
            "HMSFurious"};

    private static void Debug(int debugLevel, String logLine, boolean newLine) {
        if(debugLevel > DEBUG_LEVEL) return;
        if (newLine)
            System.out.println(logLine);
        else
            System.out.print(logLine);
    }
    
    private static int DEBUG_LEVEL = 0;
    
    static 
    {
        String skippedNames = "";
        SectFile sectFile = Statics.getShipsFile();
        for (int i=0; i<bigShips.length; i++) {
            String s = sectFile.get(bigShips[i], "Mesh");
            if ((s == null) || (s.length() <= 0))
            {
                Debug(1, bigShips[i] + " loading skipped, ship doesn't exist!", true);
                skippedNames += ", " + bigShips[i];
            } else
            {
                Debug(2, "Loading " + bigShips[i] + "...", false);
                try {
                    Class class1 = Class.forName("com.maddox.il2.objects.ships.ShipAshe$" + bigShips[i]);
                    new BigshipGeneric.SPAWN(class1);
                    Debug(2, " success!", true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (skippedNames.length() != 0) System.out.println("Ships skipped from ShipAshe since they don't exist in game: " + skippedNames.substring(2));
    }
}
