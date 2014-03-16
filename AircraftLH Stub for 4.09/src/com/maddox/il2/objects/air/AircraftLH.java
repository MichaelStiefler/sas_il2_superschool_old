package com.maddox.il2.objects.air;

import java.util.HashMap;
import java.util.Map;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;

public abstract class AircraftLH extends Aircraft
{

    public AircraftLH()
    {
        bWantBeaconKeys = false;
        headPos = new float[3];
        headOr = new float[3];
    }

    public void destroy()
    {
    	System.out.println("AircraftLH Stub destroy start");
        if(this == World.getPlayerAircraft())
        {
            clearInfo();
        }
        super.destroy();
    	System.out.println("AircraftLH Stub destroy end");
    }

    public static void clearInfo()
    {
        infoMap.clear();
    }

    public static Map getInfoList()
    {
        return infoMap;
    }

    public static void setInfo(Object fuze, String s, String s1, float f)
    {
    }

    protected static float floatindex(float f, float af[])
    {
        int i = (int)f;
        if(i >= af.length - 1)
            return af[af.length - 1];
        if(i < 0)
            return af[0];
        if(i == 0)
        {
            if(f > 0.0F)
                return af[0] + f * (af[1] - af[0]);
            else
                return af[0];
        } else
        {
            return af[i] + (f % (float)i) * (af[i + 1] - af[i]);
        }
    }

    public void update(float f)
    {
        super.update(f);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    public void beaconPlus()
    {
    }

    public void beaconMinus()
    {
    }

    public void beaconSet(int i)
    {
    }

    public void auxPlus(int i)
    {
    }

    public void auxMinus(int i)
    {
    }

    public void auxPressed(int i)
    {
    }

    protected void hitFlesh(int i, Shot shot, int j)
    {
        int l = (int)(shot.power * 0.0035F * World.Rnd().nextFloat(0.5F, 1.5F));
        switch(j)
        {
        default:
            break;

        case 0: // '\0'
            if(World.Rnd().nextFloat() < 0.05F)
                return;
            if(shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
                HUD.logCenter("H E A D S H O T");
            l *= 30;
            break;

        case 1: // '\001'
            if(World.Rnd().nextFloat() < 0.08F)
            {
                l *= 2;
                break;
            }
            break;

        case 2: // '\002'
            l = (int)((float)l / 1.5F);
            break;
        }
        debuggunnery("*** Pilot " + i + " hit for " + l + "% (" + (int)shot.power + " J)");
        FM.AS.hitPilot(shot.initiator, i, l);
        if(FM.AS.astatePilotStates[i] > 95 && j == 0)
            debuggunnery("*** Headshot!.");
    }

    public void movePilotsHead(float f, float f1)
    {
        if(Config.isUSE_RENDER() && (headTp < f1 || headTm > f1 || headYp < f || headYm > f))
        {
            headTp = f1 + 0.0005F;
            headTm = f1 - 0.0005F;
            headYp = f + 0.0005F;
            headYm = f - 0.0005F;
            f *= 0.7F;
            f1 *= 0.7F;
            tmpOrLH.setYPR(0.0F, 0.0F, 0.0F);
            tmpOrLH.increment(0.0F, f, 0.0F);
            tmpOrLH.increment(f1, 0.0F, 0.0F);
            tmpOrLH.increment(0.0F, 0.0F, -0.2F * f1 + 0.05F * f);
            headOr[0] = tmpOrLH.getYaw();
            headOr[1] = tmpOrLH.getPitch();
            headOr[2] = tmpOrLH.getRoll();
            headPos[0] = 0.0005F * Math.abs(f);
            headPos[1] = -0.0001F * Math.abs(f);
            headPos[2] = 0.0F;
            hierMesh().chunkSetLocate("Head1_D0", headPos, headOr);
        }
    }

    public void doWreck(String s)
    {
        if(hierMesh().chunkFindCheck(s) != -1)
        {
            hierMesh().hideSubTrees(s);
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind(s));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public static float[] getArmingtraveltoaltscale() {
		return armingTravelToAltScale;
	}
    
    static {
    	hasFuzeModeSelector = false;
    }

	private static Map infoMap = new HashMap();
    public static int hudLogCompassId = HUD.makeIdLog();
    public static boolean hasFuzeModeSelector = false;
    private static final float armingTravelToAltScale[] = {
        0.0F, 8F, 13F, 17F, 29F, 43F, 55F, 66F, 78F, 92F, 
        116F, 152F, 186F, 223F, 260F, 300F
    };
    public boolean bWantBeaconKeys;
    private float headPos[];
    private float headOr[];
    private static Orient tmpOrLH = new Orient();
    private float headYp;
    private float headTp;
    private float headYm;
    private float headTm;

}
