// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 06.02.2020 17:38:42
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Goose.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.fm.Squares;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme2, TypeScout, TypeSailPlane, TypeTransport, 
//            PaintScheme, Aircraft

public class Goose extends com.maddox.il2.objects.air.Scheme2
    implements com.maddox.il2.objects.air.TypeScout, com.maddox.il2.objects.air.TypeSeaPlane, com.maddox.il2.objects.air.TypeTransport
{

    public Goose()
    {
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(super.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("HMask2_D0", false);
            break;
        }
    }

    public void update(float f)
    {
        if((super.FM instanceof com.maddox.il2.fm.RealFlightModel) && ((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode())
        {
            float f1 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f1 < 300F && f1 > 30F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = (1500F - f1) / 3000F;
            float f3 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f3 < 1000F && f3 > 301F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = (1500F - f3) / 8000F;
            float f5 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f5 > 1001F && f5 < 1500F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.07F;
            float f7 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f7 > 1501F && f7 < 2000F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.05F;
            float f9 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f9 > 2001F && f9 < 2500F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.04F;
            float f11 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f11 > 2501F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.03F;
        }
        if((super.FM instanceof com.maddox.il2.fm.RealFlightModel) && ((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode())
        {
            float f2 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[1].getRPM();
            if(f2 < 300F && f2 > 30F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = (1500F - f2) / 3000F;
            float f4 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[1].getRPM();
            if(f4 < 1000F && f4 > 301F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = (1500F - f4) / 8000F;
            float f6 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[1].getRPM();
            if(f6 > 1001F && f6 < 1500F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.07F;
            float f8 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[1].getRPM();
            if(f8 > 1501F && f8 < 2000F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.05F;
            float f10 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[1].getRPM();
            if(f10 > 2001F && f10 < 2500F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.04F;
            float f12 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[1].getRPM();
            if(f12 > 2501F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.03F;
        }
        super.update(f);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 2; j++)
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j] != null)
                {
                    tmpp.set(((com.maddox.il2.engine.Actor) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j])).pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    ((com.maddox.il2.engine.Actor) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j])).pos.setAbs(tmpp);
                    ((com.maddox.il2.engine.Actor) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j])).pos.reset();
                }

        }

        if(super.FM.getSpeedKMH() > 270F && super.FM.getVertSpeed() > 0.0F && super.FM.getAltitude() < 5000F)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).producedAF.x += 20F * (270F - super.FM.getSpeedKMH());
        if(super.FM.isPlayers() && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).Sq.squareElevators > 0.0F)
        {
            if(super.FM.getSpeedKMH() > 300F && super.FM.getSpeedKMH() < 400F)
            {
                super.FM.SensPitch = 0.35F;
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).producedAM.y -= 400F * (300F - super.FM.getSpeedKMH());
            }
            if(super.FM.getSpeedKMH() >= 400F)
            {
                super.FM.SensPitch = 0.22F;
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).producedAM.y -= 250F * (300F - super.FM.getSpeedKMH());
            } else
            {
                super.FM.SensPitch = 0.45F;
            }
        }
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 2)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 2)
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
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xflap"))
        {
            if(s.startsWith("xflapl") && chunkDamageVisible("FlapL") < 2)
                hitChunk("FlapL", shot);
            if(s.startsWith("xflapr") && chunkDamageVisible("FalpR") < 2)
                hitChunk("FlapR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 2)
                hitChunk("WingLIn", shot);
            if(shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 1);
            if(shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 1, 1);
        } else
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 2)
                hitChunk("WingRIn", shot);
            if(shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 1);
            if(shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 3, 1);
        } else
        if(s.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 2)
                hitChunk("WingLMid", shot);
        } else
        if(s.startsWith("xwingrmid"))
        {
            if(chunkDamageVisible("WingRMid") < 2)
                hitChunk("WingRMid", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 2)
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 2)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            if(getEnergyPastArmor(1.45F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 0.5F)
            {
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, com.maddox.il2.ai.World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEngineStates[0] < 1)
                {
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.doSetEngineState(shot.initiator, 0, 1);
                }
                if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 960000F)
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                getEnergyPastArmor(25F, shot);
            }
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            if(getEnergyPastArmor(1.45F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[1].getCylindersRatio() * 0.5F)
            {
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[1].setCyliderKnockOut(shot.initiator, com.maddox.il2.ai.World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEngineStates[1] < 1)
                {
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 1);
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.doSetEngineState(shot.initiator, 1, 1);
                }
                if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 960000F)
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 3);
                getEnergyPastArmor(25F, shot);
            }
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int l;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else
            {
                l = s.charAt(5) - 49;
            }
            com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** hitFlesh..");
            hitFlesh(l, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, com.maddox.il2.engine.Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            killPilot(this, 5);
            killPilot(this, 6);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveAileron(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -24F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -16F * f, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -16F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -24F * f, 0.0F);
        }
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -27F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -25F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -25F * f, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -18F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -18F * f, 0.0F);
        }
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh1, float f1)
    {
    }

    protected void moveGear(float f1)
    {
    }

    public void moveWheelSink()
    {
    }

    public void moveSteering(float f1)
    {
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapL_D0", 0.0F, -50F * f, 0.0F);
        hierMesh().chunkSetAngles("FlapR_D0", 0.0F, -50F * f, 0.0F);
    }

    private static com.maddox.JGP.Point3d tmpp = new Point3d();

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Goose.class;
        com.maddox.rts.Property.set(class1, "originCountry", com.maddox.il2.objects.air.PaintScheme.countryUSA);
    }
}