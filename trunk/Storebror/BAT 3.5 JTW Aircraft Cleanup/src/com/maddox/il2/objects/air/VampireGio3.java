package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class VampireGio3 extends Scheme1
    implements TypeFighter, TypeBNZFighter, TypeStormovik
{

    public VampireGio3()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(this.thisWeaponsName.endsWith("default"))
        {
            hierMesh().chunkVisible("Pilon1_D0", false);
            hierMesh().chunkVisible("Pilon2_D0", false);
            hierMesh().chunkVisible("Pilon3_D0", false);
            hierMesh().chunkVisible("Pilon4_D0", false);
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -67F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -91F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -87F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -91F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -87F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -20F * f, 0.0F);
        if(this.FM.CT.getGear() > 0.8F)
            hierMesh().chunkSetAngles("GearC6_D0", 0.0F, 0.0F, -40F * f);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("BrakeL_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("BrakeR_D0", 0.0F, -90F * f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(12F, 19F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p2"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(12.7F, 12.7F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(20F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch(i)
                {
                case 1:
                case 2:
                    if(getEnergyPastArmor(0.99F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3:
                case 4:
                    if(getEnergyPastArmor(1.22F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    if(getEnergyPastArmor(1.22F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled..");
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    this.FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if(!s.endsWith("exht"));
            } else
            if(s.startsWith("xxhyd"))
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxmgun0"))
            {
                int j = s.charAt(7) - 49;
                if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
                    this.FM.AS.setJamBullets(0, j);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxpnm"))
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            else
            if(s.startsWith("xxspar"))
            {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if(s.startsWith("xxsparli") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spar Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D" + chunkDamageVisible("WingLIn"), shot.initiator);
                }
                if(s.startsWith("xxsparri") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spar Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D" + chunkDamageVisible("WingRIn"), shot.initiator);
                }
                if(s.startsWith("xxsparlm") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spar Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                }
                if(s.startsWith("xxsparrm") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spar Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                }
                if(s.startsWith("xxsparlo") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spar Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                }
                if(s.startsWith("xxsparro") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spar Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                }
                if(s.startsWith("xxspark") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor((double)(6.8F * World.Rnd().nextFloat(1.0F, 1.5F)) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Keel Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D" + chunkDamageVisible("Keel1"), shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y + ((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            } else
            if(s.startsWith("xxtank"))
            {
                int k = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(this.FM.AS.astateTankStates[k] == 0)
                    {
                        debuggunnery("Fuel Tank (" + k + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, k, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F)
                    {
                        this.FM.AS.hitTank(shot.initiator, k, 2);
                        debuggunnery("Fuel Tank (" + k + "): Hit..");
                    }
                }
            }
        } else
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
        {
            hitChunk("CF", shot);
            if(s.startsWith("xcockpit"))
            {
                if(point3d.x > 2D)
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                } else
                {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
            } else
            if(point3d.x > 2.5D)
            {
                if(World.Rnd().nextFloat() < 0.2F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            } else
            if(point3d.y > 0.0D)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
            else
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xnose"))
        {
            if(chunkDamageVisible("Nose") < 2)
                hitChunk("Nose", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl"))
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr"))
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
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
            hitFlesh(l, shot, byte0);
        }
    }

    public void update(float f)
    {
        if(Config.isUSE_RENDER() && this.FM.AS.isMaster())
            if(this.FM.EI.engines[0].getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6)
            {
                if(this.FM.EI.engines[0].getPowerOutput() > 0.95F)
                    this.FM.AS.setSootState(this, 0, 3);
                else
                    this.FM.AS.setSootState(this, 0, 2);
            } else
            {
                this.FM.AS.setSootState(this, 0, 0);
            }
        super.update(f);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19:
            this.FM.EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);

        case 11:
            cut("StabL");
            cut("StabR");
            this.FM.cut(17, j, actor);
            this.FM.cut(18, j, actor);
            break;

        case 13:
            return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(this.FM.getAltitude() < 500F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    static 
    {
        Class class1 = VampireGio3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Vampire");
        Property.set(class1, "meshName", "3DO/Plane/Vampire3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/VampireGio.fmd:VampireGio_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitVampireGio.class
        });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 3, 3, 2, 2, 
            2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalRock01", "_ExternalRock02", 
            "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08"
        });
    }
}