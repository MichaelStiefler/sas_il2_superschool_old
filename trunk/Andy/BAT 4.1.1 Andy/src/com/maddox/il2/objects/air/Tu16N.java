

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class Tu16N extends Tu16
    implements TypeTransport
{
    public Tu16N()
    {
    }
        protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap2_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap3_D0", 0.0F, -35F * f, 0.0F);
        if(((FlightModelMain) (super.FM)).CT.Weapons[2] != null)
        {
            hierMesh().chunkSetAngles("Flap1_D0", 0.0F, -25F * f, 0.0F);
            hierMesh().chunkSetAngles("Flap4_D0", 0.0F, -25F * f, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("Flap1_D0", 0.0F, -35F * f, 0.0F);
            hierMesh().chunkSetAngles("Flap4_D0", 0.0F, -35F * f, 0.0F);
        }
    }
        public void update(float f)
    {
        if(FM.getSpeed() > 5F)
        {

            }
       
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            if(FM.EI.engines[0].getPowerOutput() > 0.8F && FM.EI.engines[0].getStage() == 6)
            {
                if(FM.EI.engines[0].getPowerOutput() > 0.95F)
                    FM.AS.setSootState(this, 0, 3);
                else
                    FM.AS.setSootState(this, 0, 2);
            } else
            {
                FM.AS.setSootState(this, 0, 0);
            }
            if(FM.EI.engines[1].getPowerOutput() > 0.8F && FM.EI.engines[1].getStage() == 6)
            {
                if(FM.EI.engines[1].getPowerOutput() > 0.95F)
                    FM.AS.setSootState(this, 1, 3);
                else
                    FM.AS.setSootState(this, 1, 2);
            } else
            {
                FM.AS.setSootState(this, 1, 0);
            }
        }
              super.update(f);
    }
    private float oldctl[] = {
        -1F, -1F
    };
    private float curctl[] = {
        -1F, -1F
    };  
        
    static 
    {
        Class class1 = com.maddox.il2.objects.air.Tu16N.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Tu16N");
        Property.set(class1, "meshName", "3DO/Plane/Tu16N(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1960F);
        Property.set(class1, "yearExpired", 1991F);
        Property.set(class1, "FlightModel", "FlightModels/Tu_16A.fmd:TU_16");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitTu16.class
        });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            12, 12
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN06", "_MGUN07"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 2;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(12, "MGunVYakNS23", 315);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(12, "MGunVYakNS23", 315);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int i = 0; i < byte0; i++)
                a_lweaponslot[i] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}