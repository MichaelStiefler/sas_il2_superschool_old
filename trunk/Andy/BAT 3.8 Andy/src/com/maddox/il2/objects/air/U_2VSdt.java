package com.maddox.il2.objects.air;

import com.maddox.rts.*;

import java.io.IOException;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;

public class U_2VSdt extends U_2xyz
{

    public U_2VSdt()
    {
    	fSightCurForwardAngle = 9F;                   
        skiAngleL = 0.0F;
        skiAngleR = 0.0F;
        spring = 0.15F;
        Spare1 = true;
        Spare2 = true;
        Spare3 = true;
    }
    
    public void onAircraftLoaded()
    {        
    	super.onAircraftLoaded();
    	    	    	
    	FM.CT.bHasBrakeControl = false; 
    	
        super.moveGunner();
        
        if(thisWeaponsName.endsWith("Bomb") || thisWeaponsName.startsWith("Cargo"))
        {
            hierMesh().chunkVisible("BombRack_D0", true);                 
        } 
                
        if(thisWeaponsName.endsWith("RS82") || thisWeaponsName.endsWith("RS132"))
        {
            hierMesh().chunkVisible("RSRack_D0", true);       
        }     
            
        if(Config.isUSE_RENDER() && World.cur().camouflage == 1)
        {
            hierMesh().chunkVisible("GearL1_D0", false);            
            hierMesh().chunkVisible("GearR1_D0", false);
            
            hierMesh().chunkVisible("SkiC_D0", true);
            hierMesh().chunkVisible("SkiL0_D0", true);
            hierMesh().chunkVisible("SkiL1_D0", true);
            hierMesh().chunkVisible("SkiL2_D0", true);
            hierMesh().chunkVisible("SkiL3_D0", true);
            hierMesh().chunkVisible("SkiL4_D0", true);
            hierMesh().chunkVisible("SkiL5_D0", true);
            hierMesh().chunkVisible("SkiR0_D0", true);
            hierMesh().chunkVisible("SkiR1_D0", true);
            hierMesh().chunkVisible("SkiR2_D0", true);
            hierMesh().chunkVisible("SkiR3_D0", true);
            hierMesh().chunkVisible("SkiR4_D0", true);
            hierMesh().chunkVisible("SkiR5_D0", true);             
        }        
    }
                 
    protected void moveFan(float f)
    {
        if(Config.isUSE_RENDER())
        {
            boolean flag = false;
            float f1 = Aircraft.cvt(FM.getSpeed(), 20F, 50F, 1.0F, 0.0F);
            float f2 = Aircraft.cvt(FM.getSpeed(), 0.0F, 20F, 0.0F, 0.5F);
                        
            
            if(FM.Gears.gWheelSinking[0] > 0.0F)
            {
                flag = true;
                skiAngleL = 0.5F * skiAngleL + 0.5F * FM.Or.getTangage();
                if(skiAngleL > 20F)
                    skiAngleL = skiAngleL - spring;
                hierMesh().chunkSetAngles("SkiL0_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - skiAngleL, World.Rnd().nextFloat(f2, f2));
            } else
            {
            	if((double)skiAngleL > (double)(f1 * -10F) + 0.01D)
                {
                    skiAngleL = skiAngleL - spring;
                    flag = true;
                } else
                if((double)skiAngleL < (double)(f1 * -10F) - 0.01D)
                {
                    skiAngleL = skiAngleL + spring;
                    flag = true;
                }
                
                hierMesh().chunkSetAngles("SkiL0_D0", 0.0F, -skiAngleL, 0.0F);
            }
            if(FM.Gears.gWheelSinking[1] > 0.0F)
            {
                flag = true;
                skiAngleR = 0.5F * skiAngleR + 0.5F * FM.Or.getTangage();
                if(skiAngleR > 20F)
                    skiAngleR = skiAngleR - spring;
                hierMesh().chunkSetAngles("SkiR0_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - skiAngleR, World.Rnd().nextFloat(f2, f2));
                if(FM.Gears.gWheelSinking[0] == 0.0F && FM.Or.getRoll() < 365F && FM.Or.getRoll() > 355F)
                {
                    skiAngleL = skiAngleR;
                    hierMesh().chunkSetAngles("SkiL0_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - skiAngleL, World.Rnd().nextFloat(f2, f2));
                }
            } else
            {
                if((double)skiAngleR > (double)(f1 * -10F) + 0.01D)
                {
                    skiAngleR = skiAngleR - spring;
                    flag = true;
                } else
                if((double)skiAngleR < (double)(f1 * -10F) - 0.01D)
                {
                    skiAngleR = skiAngleR + spring;
                    flag = true;
                }
                hierMesh().chunkSetAngles("SkiR0_D0", 0.0F, -skiAngleR, 0.0F);
            }
            if(!flag && f1 == 0.0F)
            {
                super.moveFan(f);
                return;
            }
            hierMesh().chunkSetAngles("SkiC_D0", 0.0F, (skiAngleL + skiAngleR) / 2.0F, 0.0F);
            
            float f3 = skiAngleL / 20F;
            float f4 = skiAngleL / 60F;
            if (skiAngleL < 0)
            	hierMesh().chunkSetAngles("SkiL2_D0", 0.0F, f3 * 10.4F, 0.0F);
            else hierMesh().chunkSetAngles("SkiL2_D0", 0.0F, f3 * -6F, f3 * -8F);
            resetYPRmodifier();
            Aircraft.xyz[2] = -f4;
            hierMesh().chunkSetLocate("SkiL1_D0", Aircraft.xyz, Aircraft.ypr);
            
            hierMesh().chunkSetAngles("SkiL5_D0", 0.0F, f3 * 8.7F, 0.0F);
            resetYPRmodifier();
            Aircraft.xyz[2] = f4;
            if (skiAngleL > 0) hierMesh().chunkSetLocate("SkiL3_D0", Aircraft.xyz, Aircraft.ypr);
            
            float f5 = skiAngleR / 20F;
            float f6 = skiAngleR / 60F;
            if (skiAngleR < 0)
            	hierMesh().chunkSetAngles("SkiR2_D0", 0.0F, f5 * 10.4F, 0.0F);
            else hierMesh().chunkSetAngles("SkiR2_D0", 0.0F, f5 * -6F, f5 * 8F);
            resetYPRmodifier();
            Aircraft.xyz[2] = -f6;
            hierMesh().chunkSetLocate("SkiR1_D0", Aircraft.xyz, Aircraft.ypr);
            
            hierMesh().chunkSetAngles("SkiR5_D0", 0.0F, f5 * 8.7F, 0.0F);
            resetYPRmodifier();
            Aircraft.xyz[2] = -f6;
            if (skiAngleR > 0) hierMesh().chunkSetLocate("SkiR3_D0", Aircraft.xyz, Aircraft.ypr);
        }
        super.moveFan(f);
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
                if(i > 0x1282EA9)
                    return "41_";
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
        
        if ((hierMesh().isChunkVisible("WingRIn_CAP")) || (hierMesh().isChunkVisible("WingLIn_CAP")))
        {
        	hierMesh().chunkVisible("BombRack_D0", false); 
        	hierMesh().chunkVisible("RSRack_D0", false);
        }
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
    
    private float skiAngleL;
    private float skiAngleR;
    private float spring;
    
    public static boolean Spare1;
    public static boolean Spare2;
    public static boolean Spare3;
            
    static 
    {    	    	
        Class class1 = com.maddox.il2.objects.air.U_2VSdt.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "U-2");
        Property.set(class1, "meshName", "3do/plane/U_2VS/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1945.8F);
        Property.set(class1, "FlightModel", "FlightModels/U-2VS.fmd:DT_U-2_FM");        
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[]{CockpitU_2VS.class, CockpitU_2VS_Bombardier.class, CockpitU_2VS_TGunner.class});        
        
        weaponTriggersRegister(class1, new int[] {
            10, 3, 3, 3, 3, 
            3, 3, 9, 9, 
            3, 3, 3, 
            2, 2, 2, 2, 
            2, 2, 2, 2
        });
        
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04",
            "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
            "_ExternalBomb05", "_ExternalBomb06", "_ExternalBail02",
            "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04",
            "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08"
        });
                            
    }
}
