

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


public class Xian_JHU_6 extends Tu16
    implements TypeBomber, TypeTransport
{
    public Xian_JHU_6()
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
        public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            if(f1 < -1F)
            {
                f1 = -1F;
                flag = false;
            }
            if(f1 > 80F)
            {
                f1 = 80F;
                flag = false;
            }
            break;

        case 1: // '\001'
            if(f1 < -80F)
            {
                f1 = -80F;
                flag = false;
            }
            if(f1 > 1.0F)
            {
                f1 = 1.0F;
                flag = false;
            }
            break;

        case 2: // '\002'
            if(f < -35F)
            {
                f = -35F;
                flag = false;
            }
            if(f > 35F)
            {
                f = 35F;
                flag = false;
            }
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunVYakNS23", 315);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(10, "MGunVYakNS23", 315);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(10, "MGunVYakNS23", 315);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(11, "MGunVYakNS23", 315);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(11, "MGunVYakNS23", 315);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
        }
        catch(Exception exception) { }
        return a_lweaponslot;
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f1)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput1)
        throws IOException
    {
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
        Class class1 = com.maddox.il2.objects.air.Xian_JHU_6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Xian-JHU-6");
        Property.set(class1, "meshName", "3DO/Plane/Xian-JHU-6(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());

        Property.set(class1, "yearService", 1960F);
        Property.set(class1, "yearExpired", 1991F);
        Property.set(class1, "FlightModel", "FlightModels/Tu_16A.fmd:TU_16");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitTu16.class
        });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 10, 11, 11, 12, 12, 3, 3, 3, 
            3, 3, 3, 3, 2, 2, 2, 2, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", 
            "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalDev01", "_ExternalDev02"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 20;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunVYakNS23", 315);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(10, "MGunVYakNS23", 315);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(10, "MGunVYakNS23", 315);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(11, "MGunVYakNS23", 315);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(11, "MGunVYakNS23", 315);
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "None";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int j = 0; j < byte0; j++)
                a_lweaponslot[j] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}