package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Property;

public class OV_10A extends Scheme1 implements TypeStormovik {

    public OV_10A() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -90F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -90F * f);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, -96F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, 96F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, 95F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, 95F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, -95F), 0.0F);
    }

    protected void moveGear(float f) {
        OV_10A.moveGear(this.hierMesh(), f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 20F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 20F * f, 0.0F, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("Vator_D0", 0.0F, 0.0F, 30F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, -30F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, 30F * f);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("FlapL1_D0", 0.0F, 0.0F, -40F * f);
        this.hierMesh().chunkSetAngles("FlapL2_D0", 0.0F, 0.0F, -30F * f);
        this.hierMesh().chunkSetAngles("FlapR2_D0", 0.0F, 0.0F, -30F * f);
        this.hierMesh().chunkSetAngles("FlapR1_D0", 0.0F, 0.0F, -40F * f);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(8.26F / (Math.abs((float) Aircraft.v1.x) + 1E-005F), shot);
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(5.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        ((FlightModelMain) (super.FM)).EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(22.5F, shot);
                } else if (s.startsWith("xxeng1cyls")) {
                    if ((this.getEnergyPastArmor(World.Rnd().nextFloat(1.5F, 23.9F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.12F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.005F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.endsWith("eqpt")) {
                    if ((this.getEnergyPastArmor(2.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Magneto 0 Destroyed..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine Module: Magneto 1 Destroyed..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Controls Cut..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            Aircraft.debugprintln(this, "*** Engine Module: Throttle Controls Cut..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                            Aircraft.debugprintln(this, "*** Engine Module: Mix Controls Cut..");
                        }
                    }
                } else if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
            }
            if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(1.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.4F)) {
                    if (this.FM.AS.astateTankStates[i] == 0) {
                        this.debuggunnery("Fuel Tank (" + i + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, i, 1);
                        this.FM.AS.doSetTankState(shot.initiator, i, 1);
                    }
                    if ((World.Rnd().nextFloat() < 0.003F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.2F))) {
                        this.FM.AS.hitTank(shot.initiator, i, 4);
                        this.debuggunnery("Fuel Tank (" + i + "): Hit..");
                    }
                }
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
            if (World.Rnd().nextFloat() < 0.07F) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            }
            if (World.Rnd().nextFloat() < 0.07F) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            }
            if (World.Rnd().nextFloat() < 0.07F) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            if (World.Rnd().nextFloat() < 0.07F) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            }
            if (World.Rnd().nextFloat() < 0.07F) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            }
        } else if (s.startsWith("xeng1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xeng2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xtail1")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xtail2")) {
            if (this.chunkDamageVisible("Tail2") < 3) {
                this.hitChunk("Tail2", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 1) {
                this.hitChunk("Rudder2", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (this.chunkDamageVisible("Stab") < 2) {
                this.hitChunk("Stab", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvator") && (this.chunkDamageVisible("Vator") < 1)) {
                this.hitChunk("Vator", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.startsWith("xwingrmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else {
                j = s.charAt(5) - 49;
            }
            this.hitFlesh(j, shot, byte0);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    static {
        Class class1 = com.maddox.il2.objects.air.OV_10A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "OV_10A");
        Property.set(class1, "meshName", "3DO/Plane/OV-10A/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 2020F);
        Property.set(class1, "FlightModel", "FlightModels/OV10.fmd:OV10_FM");
        Property.set(class1, "LOSElevation", 0.82595F);
        Property.set(class1, "cockpitClass", new Class[] { com.maddox.il2.objects.air.CockpitOV10.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16" });
    }
}
