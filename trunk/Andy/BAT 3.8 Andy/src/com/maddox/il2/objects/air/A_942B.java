// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 06.10.2019 10:43:07
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   A_942B.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.fm.Squares;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, PaintSchemeBMPar04, TypeTransport, TypeSailPlane, 
//            Aircraft, NetAircraft, PaintScheme

public class A_942B extends com.maddox.il2.objects.air.Scheme1
    implements com.maddox.il2.objects.air.TypeTransport, com.maddox.il2.objects.air.TypeSailPlane
{

    public A_942B()
    {
        Payload = 0;
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
            com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** hitFlesh..");
            hitFlesh(i, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, com.maddox.il2.engine.Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
            hitProp(0, j, actor);
            return super.cutFM(34, j, actor);

        case 13: // '\r'
            killPilot(this, 0);
            return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("HMask1_D0", false);
            break;
        }
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

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float f)
    {
    }

    protected void moveGear(float f)
    {
    }

    public void moveWheelSink()
    {
    }

    public void moveSteering(float f)
    {
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapL_D0", 0.0F, -50F * f, 0.0F);
        hierMesh().chunkSetAngles("FlapR_D0", 0.0F, -50F * f, 0.0F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.bIsEnableToBailout = false;
        if(super.thisWeaponsName.startsWith("4xPass"))
        {
            hierMesh().chunkVisible("Pass1", true);
            hierMesh().chunkVisible("Pass2", true);
            hierMesh().chunkVisible("Pass3", true);
            hierMesh().chunkVisible("Pass4", true);
        }
        if(super.thisWeaponsName.startsWith("4xPara"))
        {
            hierMesh().chunkVisible("PassSeats", false);
            hierMesh().chunkVisible("ParaBench", true);
            hierMesh().chunkVisible("Para1", true);
            hierMesh().chunkVisible("Para2", true);
            hierMesh().chunkVisible("Para3", true);
            hierMesh().chunkVisible("Para4", true);
            Payload = 1;
        }
        if(super.thisWeaponsName.startsWith("4xCarg"))
        {
            hierMesh().chunkVisible("PassSeats", false);
            hierMesh().chunkVisible("ParaBench", true);
            hierMesh().chunkVisible("Cargo1", true);
            hierMesh().chunkVisible("Cargo2", true);
            hierMesh().chunkVisible("Cargo3", true);
            hierMesh().chunkVisible("Cargo4", true);
            hierMesh().chunkVisible("LoadmasterIn", true);
            Payload = 2;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    public void update(float f)
    {
        super.update(f);
        if((super.FM instanceof com.maddox.il2.fm.RealFlightModel) && ((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode())
        {
            float f1 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f1 < 300F && f1 > 30F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = (1500F - f1) / 3000F;
            float f2 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f2 < 1000F && f2 > 301F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = (1500F - f2) / 8000F;
            float f3 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f3 > 1001F && f3 < 1500F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.07F;
            float f4 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f4 > 1501F && f4 < 2000F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.05F;
            float f5 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f5 > 2001F && f5 < 2500F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.04F;
            float f6 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getRPM();
            if(f6 > 2501F)
                ((com.maddox.il2.fm.RealFlightModel)super.FM).producedShakeLevel = 0.03F;
        }
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
        if(Payload == 1 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() <= 3 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() >= 0)
        {
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 3)
                hierMesh().chunkVisible("Para4", false);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 2)
                hierMesh().chunkVisible("Para3", false);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 1)
                hierMesh().chunkVisible("Para2", false);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 0)
            {
                hierMesh().chunkVisible("Para1", false);
                Payload = 0;
            }
        }
        if(Payload == 2 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() <= 3 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() >= 0)
        {
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 3)
                hierMesh().chunkVisible("Cargo4", false);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 2)
                hierMesh().chunkVisible("Cargo3", false);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 1)
                hierMesh().chunkVisible("Cargo2", false);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 0)
            {
                hierMesh().chunkVisible("Cargo1", false);
                hierMesh().chunkVisible("LoadmasterIn", false);
                hierMesh().chunkVisible("LoadmasterOut", true);
                Payload = 0;
            }
        }
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

    private static com.maddox.JGP.Point3d tmpp = new Point3d();
    private int Payload;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.A_942B.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Fairchild-91B");
        com.maddox.rts.Property.set(class1, "meshName", "3do/Plane/Fairchild-91B(Multi1)/hier.him");
        com.maddox.rts.Property.set(class1, "originCountry", com.maddox.il2.objects.air.PaintScheme.countryUSA);
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
        com.maddox.rts.Property.set(class1, "yearService", 1935F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1960F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/A-942B.fmd:A-942B_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitA_942B.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.73425F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_BombSpawn1", "_BombSpawn2"
        });
    }
}