package com.maddox.il2.objects.air;

import com.maddox.rts.*;
import java.io.IOException;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.*;

public class U_2VSLNBdt extends U_2xyz
{

    public U_2VSLNBdt()
    {
    	fSightCurForwardAngle = 9F;            
        
        Spare1 = true;
        Spare2 = true;
        Spare3 = true;
        Spare4 = true;
    }
    
    public void onAircraftLoaded()
    {        
    	super.onAircraftLoaded();
    	    	
    	FM.CT.bHasBrakeControl = false; 
    	
        super.moveGunner();
    }
               
    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        if(i == 2)
        {
            super.doRemoveBodyFromPlane(3);
            gunnerEjected = true;
        }
    }
            
    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        if(regiment.country().equals(PaintScheme.countryRussia))
        {
            int i = Mission.getMissionDate(true);
            if(i > 0)
                if(i > 0x1287d91)
                    return "43_";
                else
                    return "";
        }
        return "";
    }
        
	public void update(float f)
	{            	    										
		super.update(f);     
		
        gunnerAiming();	
        gunnerTarget();	
        
	}
          
	public void doWoundPilot(int i, float f)
    {
        super.doWoundPilot(i, f);
        
    	switch(i)
        {
        case 1: // '\001'
            FM.turret[0].setHealth(f);
            if(f <= 0.0F)
                gunnerDead = true;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        super.doMurderPilot(i);
        
    	switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D1", hierMesh().isChunkVisible("Pilot2_D0"));
            hierMesh().chunkVisible("Pilot3_D1", hierMesh().isChunkVisible("Pilot3_D0"));
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot3_D0", false);
            gunnerDead = true;
            break;
        }
    }
    
    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
         fSightCurForwardAngle = 9F;
    }

    public void typeBomberAdjDistancePlus()
    {
    	 fSightCurForwardAngle++;
         if(fSightCurForwardAngle > 70F)
             fSightCurForwardAngle = 70F;
         if(fSightCurForwardAngle <= 33F)    
         HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
             new Float(fSightCurForwardAngle * 1.0F)
         });

    }

    public void typeBomberAdjDistanceMinus()
    {
    	fSightCurForwardAngle--;
        if(fSightCurForwardAngle < 9F)
            fSightCurForwardAngle = 9F;
        if(fSightCurForwardAngle <= 33F)      
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Float(fSightCurForwardAngle * 1.0F)
        });

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

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
       netmsgguaranted.writeFloat(fSightCurForwardAngle);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
       fSightCurForwardAngle = netmsginput.readFloat();
    }

    public float fSightCurForwardAngle;    
    
    public static boolean Spare1;
    public static boolean Spare2;
    public static boolean Spare3;
    public static boolean Spare4;
            
    static 
    {    	    	
        Class class1 = com.maddox.il2.objects.air.U_2VSLNBdt.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "U-2");
        Property.set(class1, "meshName", "3do/plane/U_2VSLNB/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1967.8F);
        Property.set(class1, "FlightModel", "FlightModels/U-2VSLNB.fmd:DT_U-2_FM");        
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[]{CockpitU_2VSLNB.class, CockpitU_2VSLNB_Bombardier.class, CockpitU_2VSLNB_TGunner.class});        
        
        weaponTriggersRegister(class1, new int[] {
            10, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3
        });
        
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04",
            "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
            "_ExternalBomb07", "_ExternalBomb08", "_ExternalBail02",
            "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14",
        });
                 
    }
}
