/*Spawn place holder class - created for MDS purposes*/
package com.maddox.il2.objects.air;

public class Spawnplaceholder extends com.maddox.il2.objects.air.B_24J100
{
    public Spawnplaceholder()
    {
    }

    static java.lang.Class _mthclass$(java.lang.String s)
    {
        try
        {
            return java.lang.Class.forName(s);
        }
        catch(java.lang.ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Spawnplaceholder.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "yearService", 1940F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1995.5F);
    }
}