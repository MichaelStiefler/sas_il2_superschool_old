package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class Hawk75H extends Scheme1 implements TypeStormovik {

    public Hawk75H() {
        this.kangle = 0.0F;
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Pilot1_D0", false);
                    this.hierMesh().chunkVisible("Head1_D0", false);
                    this.hierMesh().chunkVisible("HMask1_D0", false);
                    this.hierMesh().chunkVisible("Pilot1_D1", true);
                }
                break;
        }
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -40F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if (explosion.chunkName != null) {
            if (explosion.chunkName.startsWith("WingL")) {
                if (World.Rnd().nextFloat() < 0.02F) {
                    this.FM.AS.setJamBullets(0, 2);
                }
                if (World.Rnd().nextFloat() < 0.02F) {
                    this.FM.AS.setJamBullets(0, 3);
                }
            }
            if (explosion.chunkName.startsWith("WingR")) {
                if (World.Rnd().nextFloat() < 0.02F) {
                    this.FM.AS.setJamBullets(0, 4);
                }
                if (World.Rnd().nextFloat() < 0.02F) {
                    this.FM.AS.setJamBullets(0, 5);
                }
            }
        }
        super.msgExplosion(explosion);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(15F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                if (s.endsWith("1")) {
                    if (World.Rnd().nextFloat() < 0.3F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.3F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                } else if (s.endsWith("2")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Ailerones Controls Out..");
                    }
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.3F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.3F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                } else if (s.endsWith("3") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                if (s.startsWith("xxengkart") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                    if (World.Rnd().nextFloat() < (shot.power / 200000F)) {
                        this.FM.AS.setEngineStuck(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 28000F)) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    }
                    this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                }
                if (s.startsWith("xxengcy") && (this.getEnergyPastArmor(0.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.75F))) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (this.FM.AS.astateEngineStates[0] < 1) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if (i == 1) {
                    i = 2;
                } else if (i == 2) {
                    i = 1;
                }
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.11F)) {
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                    }
                }
                return;
            }
            if (s.startsWith("xxmgun")) {
                byte byte0 = 0;
                int k = s.charAt(6) - 48;
                switch (k) {
                    case 1:
                        byte0 = 2;
                        break;

                    case 2:
                        byte0 = 1;
                        break;

                    case 3:
                        byte0 = 3;
                        break;

                    case 4:
                        byte0 = 0;
                        break;

                    case 5:
                        byte0 = 4;
                        break;

                    case 6:
                        byte0 = 5;
                        break;
                }
                this.FM.AS.setJamBullets(0, byte0);
                this.getEnergyPastArmor(25.1F, shot);
                return;
            }
            if (s.startsWith("xxammo")) {
                int j = s.charAt(6) - 48;
                if (World.Rnd().nextFloat(0.0F, 2000F) < shot.power) {
                    switch (j) {
                        default:
                            break;

                        case 1:
                            this.FM.AS.setJamBullets(0, 1);
                            break;

                        case 2:
                            if (World.Rnd().nextFloat() < 0.5F) {
                                this.FM.AS.setJamBullets(0, 2);
                            } else {
                                this.FM.AS.setJamBullets(0, 3);
                            }
                            break;

                        case 3:
                            this.FM.AS.setJamBullets(0, 0);
                            break;

                        case 4:
                            if (World.Rnd().nextFloat() < 0.5F) {
                                this.FM.AS.setJamBullets(0, 4);
                            } else {
                                this.FM.AS.setJamBullets(0, 5);
                            }
                            break;
                    }
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.endsWith("li1") && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.endsWith("ri1") && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.endsWith("lm1") && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.endsWith("rm1") && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.endsWith("lo1") && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(4.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.endsWith("ro1") && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(4.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxpar")) {
                if ((s.endsWith("sl1") || s.endsWith("sl2")) && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if ((s.endsWith("sr1") || s.endsWith("sr2")) && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if ((s.endsWith("k1") || s.endsWith("k2")) && (this.chunkDamageVisible("Keel1") > 1) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                if (s.endsWith("al")) {
                    if (this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** AroneL Lock Damaged..");
                        this.nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
                    }
                } else if (s.endsWith("ar")) {
                    if (this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** AroneR Lock Damaged..");
                        this.nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
                    }
                } else if (s.endsWith("sl1") || s.endsWith("sl2")) {
                    if (this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** VatorL Lock Damaged..");
                        this.nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
                    }
                } else if (s.endsWith("sr1") || s.endsWith("sr2")) {
                    if (this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** VatorR Lock Damaged..");
                        this.nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
                    }
                } else if ((s.endsWith("k1") || s.endsWith("k2")) && (this.getEnergyPastArmor(0.35F, shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder1 Lock Damaged..");
                    this.nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            this.hitChunk("CF", shot);
        }
        if (!s.startsWith("xcockpit")) {
            if (s.startsWith("xeng")) {
                this.hitChunk("Engine1", shot);
            } else if (s.startsWith("xtail")) {
                this.hitChunk("Tail1", shot);
            } else if (s.startsWith("xkeel")) {
                this.hitChunk("Keel1", shot);
            } else if (s.startsWith("xrudder")) {
                this.hitChunk("Rudder1", shot);
            } else if (s.startsWith("xstabl")) {
                this.hitChunk("StabL", shot);
            } else if (s.startsWith("xstabr")) {
                this.hitChunk("StabR", shot);
            } else if (s.startsWith("xvatorl")) {
                this.hitChunk("VatorL", shot);
            } else if (s.startsWith("xvatorr")) {
                this.hitChunk("VatorR", shot);
            } else if (s.startsWith("xwing")) {
                if (s.startsWith("xwinglin")) {
                    this.hitChunk("WingLIn", shot);
                }
                if (s.startsWith("xwingrin")) {
                    this.hitChunk("WingRIn", shot);
                }
                if (s.startsWith("xwinglmid")) {
                    this.hitChunk("WingLMid", shot);
                }
                if (s.startsWith("xwingrmid")) {
                    this.hitChunk("WingRMid", shot);
                }
                if (s.startsWith("xwinglout")) {
                    this.hitChunk("WingLOut", shot);
                }
                if (s.startsWith("xwingrout")) {
                    this.hitChunk("WingROut", shot);
                }
            } else if (s.startsWith("xarone")) {
                if (s.startsWith("xaronel")) {
                    this.hitChunk("AroneL", shot);
                }
                if (s.startsWith("xaroner")) {
                    this.hitChunk("AroneR", shot);
                }
            } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
                byte byte1 = 0;
                int l;
                if (s.endsWith("a")) {
                    byte1 = 1;
                    l = s.charAt(6) - 49;
                } else if (s.endsWith("b")) {
                    byte1 = 2;
                    l = s.charAt(6) - 49;
                } else {
                    l = s.charAt(5) - 49;
                }
                this.hitFlesh(l, shot, byte1);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 11:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                this.hierMesh().chunkVisible("Wire_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        for (int i = 1; i < 12; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -10F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        super.update(f);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_MGUN03") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("MadsenR_D0", false);
        }
        if (this.getGunByHookName("_MGUN04") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("MadsenL_D0", false);
        }
    }

    private float kangle;

    static {
        Class class1 = Hawk75H.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hawk75H");
        Property.set(class1, "meshName", "3DO/Plane/Hawk75H(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Hawk75H.fmd:Hawk75H_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_36.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12" });
    }
}
