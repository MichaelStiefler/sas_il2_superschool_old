// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 06.10.2019 10:45:40
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   F_291.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, PaintSchemeBMPar00, TypeTransport, NetAircraft, 
//            PaintScheme, Aircraft

public class F_291 extends com.maddox.il2.objects.air.Scheme1
    implements com.maddox.il2.objects.air.TypeTransport
{

    public F_291()
    {
        Payload = 0;
    }

    protected void moveFlap(float f)
    {
        float f1 = -15F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -8F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -8F * f, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -25F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -25F * f, 0.0F);
    }

    public void moveSteering(float f)
    {
    }

    public void moveWheelSink()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.bIsEnableToBailout = false;
        if(super.thisWeaponsName.startsWith("6xPass"))
        {
            hierMesh().chunkVisible("Pass1", true);
            hierMesh().chunkVisible("Pass2", true);
            hierMesh().chunkVisible("Pass3", true);
            hierMesh().chunkVisible("Pass4", true);
            hierMesh().chunkVisible("Pass5", true);
            hierMesh().chunkVisible("Pass6", true);
        }
        if(super.thisWeaponsName.startsWith("6xPara"))
        {
            hierMesh().chunkVisible("PassSeats", false);
            hierMesh().chunkVisible("ParaBench", true);
            hierMesh().chunkVisible("Para1", true);
            hierMesh().chunkVisible("Para2", true);
            hierMesh().chunkVisible("Para3", true);
            hierMesh().chunkVisible("Para4", true);
            hierMesh().chunkVisible("Para5", true);
            hierMesh().chunkVisible("Para6", true);
            Payload = 1;
        }
        if(super.thisWeaponsName.startsWith("6xCarg"))
        {
            hierMesh().chunkVisible("PassSeats", false);
            hierMesh().chunkVisible("ParaBench", true);
            hierMesh().chunkVisible("Cargo1", true);
            hierMesh().chunkVisible("Cargo2", true);
            hierMesh().chunkVisible("Cargo3", true);
            hierMesh().chunkVisible("Cargo4", true);
            hierMesh().chunkVisible("Cargo5", true);
            hierMesh().chunkVisible("Cargo6", true);
            hierMesh().chunkVisible("LoadmasterIn", true);
            Payload = 2;
        }
    }

    public void update(float f)
    {
        com.maddox.il2.ai.World.cur().diffCur.Torque_N_Gyro_Effects = false;
        super.update(f);
        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].addVside *= 9.9999999999999995E-008D;
        if(Payload == 1 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() <= 5 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() >= 0)
        {
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 5)
                hierMesh().chunkVisible("Para6", false);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 4)
                hierMesh().chunkVisible("Para5", false);
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
        if(Payload == 2 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() <= 5 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() >= 0)
        {
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 5)
                hierMesh().chunkVisible("Cargo6", false);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][0].countBullets() == 4)
                hierMesh().chunkVisible("Cargo5", false);
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

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEngineStates[0] > 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.39F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(this, 0, 1);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEngineStates[1] > 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.39F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(this, 1, 1);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateTankStates[0] > 4 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.035F)
                nextDMGLevel(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEffectChunks[0] + "0", 0, this);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateTankStates[1] > 4 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.035F)
                nextDMGLevel(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEffectChunks[1] + "0", 0, this);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateTankStates[2] > 4 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.035F)
                nextDMGLevel(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEffectChunks[2] + "0", 0, this);
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateTankStates[3] > 4 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.035F)
                nextDMGLevel(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEffectChunks[3] + "0", 0, this);
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
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 2)
                hitChunk("WingLMid", shot);
        } else
        if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 2)
            hitChunk("WingRMid", shot);
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

    private int Payload;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.F_291.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Farman F-291");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/F.291(Multi1)/hier.him");
        com.maddox.rts.Property.set(class1, "originCountry", com.maddox.il2.objects.air.PaintScheme.countryFrance);
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1935F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1948F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/F-291.fmd:F-291_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitF_291.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.5265F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_BombSpawn1", "_BombSpawn2"
        });
    }
}