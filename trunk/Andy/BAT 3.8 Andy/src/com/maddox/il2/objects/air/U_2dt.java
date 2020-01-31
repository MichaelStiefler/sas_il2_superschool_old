
package com.maddox.il2.objects.air;

import com.maddox.rts.*;

import java.io.IOException;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;

public class U_2dt extends U_2xyz
{

    public U_2dt()
    {  
    	skiAngleL = 0.0F;
        skiAngleR = 0.0F;
        spring = 0.15F;
    }
    
    public void onAircraftLoaded()
    {        
    	super.onAircraftLoaded();
    	    	    
    	FM.CT.bHasBrakeControl = false; 

    	if(thisWeaponsName.endsWith("Bomb") || thisWeaponsName.startsWith("Cargo"))
        {
            hierMesh().chunkVisible("BombRack_D0", true);                 
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
    
    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        if(regiment.country().equals(PaintScheme.countryRussia))
        {
            int i = Mission.getMissionDate(true);
            if(i > 0)
            	if(i > 0x1287d91)	   //19430801	
                    return "43_";
            	else if(i > 0x1282EA9) //19410601
                    return "41_";
            	else if(i > 0x12805A5) //19400101
                    return "40_";
                else
                    return "";
        }
        return "";
    }

    public void update(float f)
	{            	    										    	
    	super.update(f);     
    	        
      if ((hierMesh().isChunkVisible("WingRIn_CAP")) || (hierMesh().isChunkVisible("WingLIn_CAP")))
       	hierMesh().chunkVisible("BombRack_D0", false);         
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
            hierMesh().chunkVisible("Pilot2_D0", false);            
            break;
        }
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

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }
    
    private float skiAngleL;
    private float skiAngleR;
    private float spring;
                
    static 
    {    	    	
        Class class1 = com.maddox.il2.objects.air.U_2dt.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "U-2");
        Property.set(class1, "meshName", "3do/plane/U_2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1945.8F);
        Property.set(class1, "FlightModel", "FlightModels/U-2.fmd:DT_U-2_FM");        
        Property.set(class1, "cockpitClass", new Class[] {
        	com.maddox.il2.objects.air.CockpitU_2.class, com.maddox.il2.objects.air.CockpitU_2_Bombardier.class
        });        
        
        weaponTriggersRegister(class1, new int[] {
            3, 3, 3, 3, 9, 9, 3, 3, 3
        });
        
        weaponHooksRegister(class1, new String[] {
            "_ExternalBomb01", "_ExternalBomb02", 
            "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
            "_ExternalBomb05", "_ExternalBomb06", "_ExternalBail02"
        });
     
    }
}
