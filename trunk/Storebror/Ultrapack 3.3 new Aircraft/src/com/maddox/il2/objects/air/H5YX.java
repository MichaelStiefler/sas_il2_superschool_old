package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class H5YX extends Scheme2
    implements TypeBomber, TypeScout, TypeSailPlane, TypeTransport
{

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update(float f)
    {
        this.update(f);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 2; j++)
                if(this.FM.Gears.clpGearEff[i][j] != null)
                {
                    tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }

        }

    }

    protected void moveGear(float f)
    {
        Aircraft.moveGear(hierMesh(), f);
    }

    protected void moveFlap(float f1)
    {
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

        case 0:
            if(f < -90F)
            {
                f = -90F;
                flag = false;
            }
            if(f > 90F)
            {
                f = 90F;
                flag = false;
            }
            if(f1 < -10F)
            {
                f1 = -10F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;

        case 1:
            if(f < -90F)
            {
                f = -90F;
                flag = false;
            }
            if(f > 45F)
            {
                f = 45F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;

        case 2:
            if(f < -45F)
            {
                f = -45F;
                flag = false;
            }
            if(f > 90F)
            {
                f = 90F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xstabl"))
            hitChunk("StabL", shot);
        else
        if(s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("xvatorl"))
            hitChunk("VatorL", shot);
        else
        if(s.startsWith("xvatorr"))
            hitChunk("VatorR", shot);
        else
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F)
                this.FM.AS.hitTank(shot.initiator, 0, 1);
            if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F)
                this.FM.AS.hitTank(shot.initiator, 1, 1);
        } else
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F)
                this.FM.AS.hitTank(shot.initiator, 2, 1);
            if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F)
                this.FM.AS.hitTank(shot.initiator, 3, 1);
        } else
        if(s.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
        } else
        if(s.startsWith("xwingrmid"))
        {
            if(chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
        } else
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
        } else
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xaronel"))
            hitChunk("AroneL", shot);
        else
        if(s.startsWith("xaroner"))
            hitChunk("AroneR", shot);
        else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            if(getEnergyPastArmor(1.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.5F)
            {
                this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                if(this.FM.AS.astateEngineStates[0] < 1)
                {
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                }
                if(World.Rnd().nextFloat() < shot.power / 960000F)
                    this.FM.AS.hitEngine(shot.initiator, 0, 3);
                getEnergyPastArmor(25F, shot);
            }
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            if(getEnergyPastArmor(1.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[1].getCylindersRatio() * 0.5F)
            {
                this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                if(this.FM.AS.astateEngineStates[1] < 1)
                {
                    this.FM.AS.hitEngine(shot.initiator, 1, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 1, 1);
                }
                if(World.Rnd().nextFloat() < shot.power / 960000F)
                    this.FM.AS.hitEngine(shot.initiator, 1, 3);
                getEnergyPastArmor(25F, shot);
            }
        } else
        if(s.startsWith("xgearl"))
            hitChunk("GearL2", shot);
        else
        if(s.startsWith("xgearr"))
            hitChunk("GearR2", shot);
        else
        if(s.startsWith("xturret"))
        {
            if(s.startsWith("xturret1"))
                this.FM.AS.setJamBullets(10, 0);
            if(s.startsWith("xturret2"))
                this.FM.AS.setJamBullets(11, 0);
            if(s.startsWith("xturret3"))
                this.FM.AS.setJamBullets(12, 0);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else
            {
                i = s.charAt(5) - 49;
            }
            hitFlesh(i, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19:
            killPilot(this, 5);
            killPilot(this, 6);
            break;

        case 33:
            hierMesh().chunkVisible("Wire01_D0", false);
            hitProp(0, j, actor);
            hitProp(1, j, actor);
            super.cutFM(36, j, actor);
            // fall through

        case 34:
        case 35:
            hierMesh().chunkVisible("Wire02_D0", false);
            break;

        case 36:
            hierMesh().chunkVisible("Wire02_D0", false);
            hitProp(0, j, actor);
            hitProp(1, j, actor);
            super.cutFM(33, j, actor);
            // fall through

        case 37:
        case 38:
            hierMesh().chunkVisible("Wire01_D0", false);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 7; i++)
            if(i != 5)
                if(this.FM.getAltitude() < 3000F)
                    hierMesh().chunkVisible("HMask" + i + "_D0", false);
                else
                    hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public abstract void typeBomberUpdate(float f);

    public abstract boolean typeBomberToggleAutomation();

    public abstract void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException;

    public abstract void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException;

    public abstract void typeBomberAdjSpeedReset();

    public abstract void typeBomberAdjSpeedPlus();

    public abstract void typeBomberAdjSpeedMinus();

    public abstract void typeBomberAdjSideslipReset();

    public abstract void typeBomberAdjSideslipPlus();

    public abstract void typeBomberAdjSideslipMinus();

    public abstract void typeBomberAdjDistanceReset();

    public abstract void typeBomberAdjDistancePlus();

    public abstract void typeBomberAdjDistanceMinus();

    public abstract void typeBomberAdjAltitudeReset();

    public abstract void typeBomberAdjAltitudePlus();

    public abstract void typeBomberAdjAltitudeMinus();

    private static Point3d tmpp = new Point3d();

    static 
    {
        Class class1 = PBYX.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
