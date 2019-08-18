// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.05.2019 14:50:17
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   O_47Fxyz.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.fm.Squares;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.io.PrintStream;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, TypeScout, TypeSailPlane, PaintScheme, 
//            Aircraft, Cockpit

public class O_47Fxyz extends com.maddox.il2.objects.air.Scheme1
    implements com.maddox.il2.objects.air.TypeScout, com.maddox.il2.objects.air.TypeSeaPlane
{

    public O_47Fxyz()
    {
        bGunUp = false;
        btme = -1L;
        fGunPos = 0.0F;
        bCanopyInitState = false;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.63F);
        hierMesh().chunkSetLocate("Blister1_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.15F, 0.99F, 0.0F, 0.63F);
        hierMesh().chunkSetLocate("Blister2_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        if(com.maddox.il2.engine.Config.isUSE_RENDER())
        {
            if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null)
                com.maddox.il2.game.Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh1, float f1)
    {
    }

    protected void moveGear(float f)
    {
        com.maddox.il2.objects.air.O_47Fxyz.moveGear(hierMesh(), f);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapL_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("FlapR_D0", 0.0F, 0.0F, 45F * f);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 4; i++)
            if(super.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        case 0: // '\0'
            if(f < -40F)
            {
                f = -40F;
                flag = false;
            }
            if(f > 40F)
            {
                f = 40F;
                flag = false;
            }
            if(f1 < -3F)
            {
                f1 = -3F;
                flag = false;
            }
            if(f1 > 50F)
            {
                f1 = 50F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 2: // '\002'
            super.FM.turret[0].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;

        case 2: // '\002'
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;
        }
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(com.maddox.il2.ai.World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F)
                    {
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 280000F)
                            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 100000F)
                            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                    }
                    getEnergyPastArmor(com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 0.66F)
                    {
                        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, com.maddox.il2.ai.World.Rnd().nextInt(1, (int)(shot.power / 32200F)));
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 1000000F)
                            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("oil1") && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, 0);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 1);
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.11F)
                        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 2);
                }
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 2)
                hitChunk("CF", shot);
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.19F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.19F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
        } else
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xWingLIn") && chunkDamageVisible("WingLIn") < 2)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xWingRIn") && chunkDamageVisible("WingRIn") < 2)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xWingLOut") && chunkDamageVisible("WingLOut") < 2)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xWingROut") && chunkDamageVisible("WingROut") < 2)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 2)
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 2)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xstrutin"))
        {
            if(s.startsWith("xstrutinl") && chunkDamageVisible("StrutinL") < 2)
                hitChunk("StrutinL", shot);
            if(s.startsWith("xstrutinr") && chunkDamageVisible("StrutinR") < 2)
                hitChunk("StrutinR", shot);
        } else
        if(s.startsWith("xstruts"))
        {
            if(s.startsWith("xstrutsl") && chunkDamageVisible("StrutsL") < 2)
                hitChunk("StrutsL", shot);
            if(s.startsWith("xstrutsr") && chunkDamageVisible("StrutsR") < 2)
                hitChunk("StrutsR", shot);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int j;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else
            {
                j = s.charAt(5) - 49;
            }
            hitFlesh(j, shot, byte0);
        }
    }

    public void update(float f)
    {
        if((super.FM instanceof com.maddox.il2.fm.RealFlightModel) && ((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode())
        {
            float f1 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f1 < 300F && f1 > 30F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = (1500F - f1) / 3000F;
            float f5 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f5 < 1000F && f5 > 301F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = (1500F - f5) / 8000F;
            float f6 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f6 > 1001F && f6 < 1500F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.07F;
            float f7 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f7 > 1501F && f7 < 2000F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.05F;
            float f8 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f8 > 2001F && f8 < 2500F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.04F;
            float f9 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f9 > 2501F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.03F;
        }
        if(super.FM.getSpeedKMH() > 250F && super.FM.getVertSpeed() > 0.0F && super.FM.getAltitude() < 5000F)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).producedAF.x += 20F * (250F - super.FM.getSpeedKMH());
        if(super.FM.isPlayers() && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).Sq.squareElevators > 0.0F)
        {
            if(super.FM.getSpeedKMH() > 380F && super.FM.getSpeedKMH() < 500F)
            {
                super.FM.SensPitch = 0.3F;
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).producedAM.y -= 400F * (300F - super.FM.getSpeedKMH());
            }
            if(super.FM.getSpeedKMH() >= 501F)
            {
                super.FM.SensPitch = 0.2F;
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).producedAM.y -= 200F * (300F - super.FM.getSpeedKMH());
            } else
            {
                super.FM.SensPitch = 0.45F;
            }
        }
        if(!bCanopyInitState && super.FM.isStationedOnGround() && super.FM.isPlayers())
        {
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setCockpitDoor((com.maddox.il2.objects.air.Aircraft)((com.maddox.il2.engine.Interpolate) (super.FM)).actor, 1);
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.cockpitDoorControl = 1.0F;
            bCanopyInitState = true;
            java.lang.System.out.println("*** Initial canopy state: " + (((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.getCockpitDoor() == 1.0F ? "open" : "closed"));
        }
        if(!bGunUp)
        {
            if(fGunPos > 0.0F)
            {
                fGunPos -= 0.2F * f;
                super.FM.turret[0].bIsOperable = false;
                hierMesh().chunkVisible("Turret1A_D0", false);
                hierMesh().chunkVisible("Turret1B_D0", false);
                hierMesh().chunkVisible("Turdown_D0", true);
                hierMesh().chunkVisible("Turupps_D0", false);
                hierMesh().chunkVisible("Cover_D0", true);
                hierMesh().chunkVisible("Hatch_D0", false);
            }
        } else
        if(fGunPos < 1.0F)
        {
            fGunPos += 0.2F * f;
            if(fGunPos > 0.8F && fGunPos < 0.9F)
            {
                super.FM.turret[0].bIsOperable = true;
                hierMesh().chunkVisible("Turret1A_D0", true);
                hierMesh().chunkVisible("Turret1B_D0", true);
                hierMesh().chunkVisible("Turdown_D0", false);
                hierMesh().chunkVisible("Turupps_D0", true);
                hierMesh().chunkVisible("Cover_D0", false);
                hierMesh().chunkVisible("Hatch_D0", true);
            }
        }
        if(fGunPos < 0.6F)
        {
            resetYPRmodifier();
            com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(fGunPos, 0.0F, 0.6F, 0.0F, -0.5F);
            hierMesh().chunkSetLocate("Blister3_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        }
        if(super.FM.turret[0].bIsAIControlled)
        {
            if(super.FM.turret[0].target != null && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astatePilotStates[2] < 90)
                bGunUp = true;
            if(com.maddox.rts.Time.current() > btme)
            {
                btme = com.maddox.rts.Time.current() + com.maddox.il2.ai.World.Rnd().nextLong(5000L, 12000L);
                if(super.FM.turret[0].target == null && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astatePilotStates[2] < 90)
                    bGunUp = false;
            }
        }
        super.update(f);
    }

    private boolean bCanopyInitState;
    boolean bGunUp;
    public long btme;
    public float fGunPos;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.O_47Fxyz.class;
        com.maddox.rts.Property.set(class1, "originCountry", com.maddox.il2.objects.air.PaintScheme.countryUSA);
    }
}