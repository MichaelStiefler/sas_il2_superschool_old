// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.05.2019 17:06:40
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   POTEZ_630.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, PaintSchemeBMPar02, TypeFighter, TypeBNZFighter, 
//            TypeStormovik, Aircraft, Cockpit, NetAircraft, 
//            PaintScheme

public class POTEZ_630 extends com.maddox.il2.objects.air.Scheme1
    implements com.maddox.il2.objects.air.TypeFighter, com.maddox.il2.objects.air.TypeBNZFighter, com.maddox.il2.objects.air.TypeStormovik
{

    public POTEZ_630()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.endsWith("2x250"))
        {
            hierMesh().chunkVisible("Rack_L", true);
            hierMesh().chunkVisible("Rack_R", true);
        } else
        {
            hierMesh().chunkVisible("Rack_L", false);
            hierMesh().chunkVisible("Rack_R", false);
        }
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 92F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 92F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -70F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -70F * f);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, -89F * f);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, -89F * f);
    }

    protected void moveGear(float f)
    {
        com.maddox.il2.objects.air.POTEZ_630.moveGear(hierMesh(), f);
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
        hierMesh().chunkSetAngles("Rudder2_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapWL_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("FlapWR_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("FlapCL_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("FlapCR_D0", 0.0F, 0.0F, 45F * f);
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

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.49F);
        hierMesh().chunkSetLocate("Blister1_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        if(com.maddox.il2.engine.Config.isUSE_RENDER())
        {
            if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null)
                com.maddox.il2.game.Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        case 0: // '\0'
            if(f < -45F)
            {
                f = -45F;
                flag = false;
            }
            if(f > 45F)
            {
                f = 45F;
                flag = false;
            }
            if(f1 < -3F)
            {
                f1 = -3F;
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

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 4; i++)
            if(super.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                    getEnergyPastArmor(14.2F / (1E-005F + (float)java.lang.Math.abs(((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.v1)).x)), shot);
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int i = s.charAt(5) - 49;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(1.7F, shot) > 0.0F)
                    {
                        if(com.maddox.il2.ai.World.Rnd().nextFloat(20000F, 140000F) < shot.power)
                            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, i);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat(10000F, 50000F) < shot.power)
                            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, i, 2);
                    } else
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.04F)
                        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    else
                        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[i].setReadyness(shot.initiator, ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - 0.02F);
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[i].getCylindersRatio() * 0.9878F)
                    {
                        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[i].setCyliderKnockOut(shot.initiator, com.maddox.il2.ai.World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 48000F)
                            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.05F, shot) > 0.0F)
                        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[i].setKillCompressor(shot.initiator);
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("oil1") && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, 0);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(1.2F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.4F)
                {
                    if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateTankStates[j] == 0)
                    {
                        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j, 1);
                        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.003F || shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.2F)
                        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j, 4);
                }
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.25F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.25F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.25F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateCockpitState | 4);
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.25F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateCockpitState | 8);
        } else
        if(s.startsWith("xtail1"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xkeel2"))
        {
            if(chunkDamageVisible("Keel2") < 2)
                hitChunk("Keel2", shot);
        } else
        if(s.startsWith("xrudder1"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xrudder2"))
        {
            if(chunkDamageVisible("Rudder2") < 1)
                hitChunk("Rudder2", shot);
        } else
        if(s.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
        } else
        if(s.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
        } else
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
        } else
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
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
        {
            if(chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
        } else
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.startsWith("xgear") && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(com.maddox.il2.ai.World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int k;
            if(s.endsWith("a") || s.endsWith("a2"))
            {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else
            if(s.endsWith("b") || s.endsWith("b2"))
            {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else
            {
                k = s.charAt(5) - 49;
            }
            hitFlesh(k, shot, byte0);
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

    static 
    {
        java.lang.Class class1 = com.maddox.rts.CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Potez630");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/POTEZ-630/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        com.maddox.rts.Property.set(class1, "originCountry", com.maddox.il2.objects.air.PaintScheme.countryFrance);
        com.maddox.rts.Property.set(class1, "yearService", 1937F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1943F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/PO630.fmd:PO630_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitPotez_630.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.5265F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 10, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_CANNON01", "_CANNON02", "_MGUN01", "_ExternalBomb01", "_ExternalBomb02"
        });
    }
}