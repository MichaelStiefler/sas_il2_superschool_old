// This file is part of the SAS IL-2 Sturmovik 1946 4.12
// Flyable AI Aircraft Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited at: 2013/06/11

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class HS_123 extends Scheme1 implements TypeScout, TypeFighter, TypeTNBFighter, TypeDiveBomber, TypeStormovik {

	public HS_123() {
		suspR = 0.0F;
		suspL = 0.0F;
		suspension = 0.0F;
		needsToOpenBombays = false;
		blisterRemoved = false;
		sideDoorOpened = false;
		bChangedPit = true;
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	public void moveCockpitDoor(float f) {
		if (f > 0.01F)
			sideDoorOpened = true;
		else
			sideDoorOpened = false;
		hierMesh().chunkSetAngles("blister0_D0", 0.0F, -f * 177.7F, 0.0F);
		if (FM.getSpeedKMH() > 314F && f < 0.6F && !blisterRemoved)
			doRemoveBlisters();
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	public void hitDaSilk() {
		super.hitDaSilk();
		if (!sideDoorOpened && FM.AS.bIsAboutToBailout && !FM.AS.isPilotDead(0)) {
			sideDoorOpened = true;
			FM.CT.bHasCockpitDoorControl = true;
			FM.CT.forceCockpitDoor(0.0F);
			FM.AS.setCockpitDoor(this, 1);
		}
	}

	private final void doRemoveBlisters() {
		blisterRemoved = true;
		FM.CT.bHasCockpitDoorControl = false;
		if (hierMesh().chunkFindCheck("blister0_D0") != -1) {
			hierMesh().hideSubTrees("blister0_D0");
			Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("blister0_D0"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (thisWeaponsName.startsWith("10xSC10"))
			needsToOpenBombays = true;
	}

	protected void moveBayDoor(float f) {
		if (!needsToOpenBombays) {
			return;
		} else {
			hierMesh().chunkSetAngles("BayDoor1_D0", 0.0F, -85F * f, 0.0F);
			hierMesh().chunkSetAngles("BayDoor2_D0", 0.0F, -85F * f, 0.0F);
			return;
		}
	}

	protected void moveFlap(float f) {
		float f1 = -60F * f;
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	protected void moveRudder(float f) {
		hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
	}

	public void moveSteering(float f) {
		hierMesh().chunkSetAngles("GearC2_D0", 0.0F, f, 0.0F);
	}

	public void moveWheelSink() {
		if (FM.Gears.onGround())
			suspension = suspension + 0.008F;
		else
			suspension = suspension - 0.008F;
		if (suspension < 0.0F) {
			suspension = 0.0F;
			if (!FM.isPlayers())
				FM.Gears.bTailwheelLocked = true;
		}
		if (suspension > 0.1F)
			suspension = 0.1F;
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		float f = Aircraft.cvt(FM.getSpeed(), 0.0F, 25F, 0.0F, 1.0F);
		suspL = FM.Gears.gWheelSinking[0] * f + suspension;
		Aircraft.xyz[2] = Aircraft.cvt(suspL, 0.0F, 0.24F, 0.0F, 0.24F);
		hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
		suspR = FM.Gears.gWheelSinking[1] * f + suspension;
		Aircraft.xyz[2] = Aircraft.cvt(suspR, 0.0F, 0.24F, 0.0F, 0.24F);
		hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		default:
			break;

		case 19: // '\023'
			FM.Gears.hitCentreGear();
			break;

		case 3: // '\003'
			if (World.Rnd().nextInt(0, 99) < 1) {
				FM.AS.hitEngine(this, 0, 4);
				hitProp(0, j, actor);
				FM.EI.engines[0].setEngineStuck(actor);
				return cut("engine1");
			} else {
				FM.AS.setEngineDies(this, 0);
				return false;
			}
		}
		return super.cutFM(i, j, actor);
	}

	public boolean cut(String s) {
		boolean flag = super.cut(s);
		if (s.equalsIgnoreCase("WingLIn"))
			hierMesh().chunkVisible("WingLMid_CAP", true);
		else if (s.equalsIgnoreCase("WingRIn"))
			hierMesh().chunkVisible("WingRMid_CAP", true);
		return flag;
	}

	protected void moveElevator(float f) {
		hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -20F * f, 0.0F);
		hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
	}

	protected void moveAileron(float f) {
		hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 30F * f, 0.0F);
		hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 30F * f, 0.0F);
	}

	public void update(float f) {
		super.update(f);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (FM.getAltitude() < 3000F)
			hierMesh().chunkVisible("HMask1_D0", false);
		else
			hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			hierMesh().chunkVisible("Head1_D1", true);
			break;
		}
	}

	public void doRemoveBodyFromPlane(int i) {
		super.doRemoveBodyFromPlane(i);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1") || s.endsWith("p2"))
					getEnergyPastArmor(9.96F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
				return;
			}
			if (s.startsWith("xxcontrols")) {
				if (s.endsWith("1")) {
					if (World.Rnd().nextFloat() < 0.2F)
						if (World.Rnd().nextFloat() < 0.5F) {
							FM.AS.setControlsDamage(shot.initiator, 1);
							Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#1)");
						} else {
							FM.AS.setControlsDamage(shot.initiator, 2);
							Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#1)");
						}
				} else if ((s.endsWith("2") || s.endsWith("3")) && World.Rnd().nextFloat() < 0.2F) {
					FM.AS.setControlsDamage(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Arone Controls Out.. (#9)");
				}
				return;
			}
			if (s.startsWith("xxeng")) {
				Aircraft.debugprintln(this, "*** Engine Module: Hit..");
				if (s.endsWith("prop"))
					Aircraft.debugprintln(this, "*** Prop hit");
				else if (s.endsWith("case")) {
					if (getEnergyPastArmor(2.1F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 175000F) {
							FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							FM.AS.hitEngine(shot.initiator, 0, 2);
							Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
						}
						FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
					}
					getEnergyPastArmor(12.7F, shot);
				} else if (s.endsWith("cyls")) {
					if (getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.12F) {
						FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 48000F) {
							FM.AS.hitEngine(shot.initiator, 0, 3);
							Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
						}
						if (World.Rnd().nextFloat() < 0.005F) {
							FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
						}
						getEnergyPastArmor(22.5F, shot);
					}
				} else if (s.endsWith("oil1")) {
					FM.AS.hitOil(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
				} else if (s.endsWith("eqpt")) {
					Aircraft.debugprintln(this, "*** Engine Module: Supercharger Hit..");
					if (getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F) {
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
						Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
					}
				}
			} else if (s.endsWith("gear")) {
				Aircraft.debugprintln(this, "*** Engine Module: Gear Hit..");
				if (getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F) {
					FM.AS.setEngineStuck(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Engine Module: gear hit, engine stuck..");
				}
			} else if (s.startsWith("xxtank")) {
				if (getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F) {
					if (FM.AS.astateTankStates[0] == 0) {
						Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
						FM.AS.hitTank(shot.initiator, 0, 2);
					}
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F) {
						FM.AS.hitTank(shot.initiator, 0, 2);
						Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
					}
				}
			} else if (s.startsWith("xxlock")) {
				Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
				if (s.startsWith("xxlockr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
					nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
				} else if (s.startsWith("xxlockvl") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
				} else if (s.startsWith("xxlockvr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
				} else if (s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: AroneL Lock Shot Off..");
					nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
				} else if (s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: AroneR Lock Shot Off..");
					nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
				}
			} else if (s.startsWith("xxeqpt")) {
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
			} else {
				if (s.startsWith("xxbomb")) {
					if (needsToOpenBombays && World.Rnd().nextFloat() < 0.001F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets()) {
						debugprintln(this, "*** Bomb Payload Detonates..");
						FM.AS.hitTank(shot.initiator, 0, 10);
						nextDMGLevels(3, 2, "CF_D0", shot.initiator);
					}
					return;
				}
				if (s.startsWith("xxmgun")) {
					if (s.endsWith("01")) {
						Aircraft.debugprintln(this, "*** Fixed Gun #1: Disabled..");
						FM.AS.setJamBullets(0, 0);
					}
					if (s.endsWith("02")) {
						Aircraft.debugprintln(this, "*** Fixed Gun #2: Disabled..");
						FM.AS.setJamBullets(0, 1);
					}
					getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
				} else if (s.startsWith("xxspar")) {
					Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
					if (s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(9.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
						Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
						nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
					} else if (s.startsWith("xxspark")) {
						if (chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
							debugprintln(this, "*** Keel1 Spars Damaged..");
							nextDMGLevels(1, 2, "Keel1_D" + chunkDamageVisible("Keel1"), shot.initiator);
						}
					} else if (s.startsWith("xxsparsl")) {
						if (chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
							debuggunnery("*** StabL Spars Damaged..");
							nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
						}
					} else if (s.startsWith("xxsparsr")) {
						if (chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
							debuggunnery("*** StabR Spars Damaged..");
							nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
						}
					} else if (s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
						Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
						nextDMGLevels(1, 2, "WingLIn_D" + chunkDamageVisible("WingLIn"), shot.initiator);
					} else if (s.startsWith("xxsparri") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
						Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
						nextDMGLevels(1, 2, "WingRIn_D" + chunkDamageVisible("WingRIn"), shot.initiator);
					} else if (s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
						Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
						nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
					} else if (s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
						Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
						nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
					} else if (s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
						Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
						nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
					} else if (s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
						Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
						nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
					}
				}
			}
			return;
		}
		if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int i;
			if (s.endsWith("a")) {
				byte0 = 1;
				i = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				i = s.charAt(6) - 49;
			} else {
				i = s.charAt(5) - 49;
			}
			Aircraft.debugprintln(this, "*** hitFlesh..");
			hitFlesh(i, shot, byte0);
		} else if (s.startsWith("XCockpit")) {
			if (World.Rnd().nextFloat() < 0.2F)
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
			else if (World.Rnd().nextFloat() < 0.4F)
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
			else if (World.Rnd().nextFloat() < 0.6F)
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
			else if (World.Rnd().nextFloat() < 0.8F)
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
			else
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
		} else if (s.startsWith("xcf")) {
			if (chunkDamageVisible("CF") < 3)
				hitChunk("CF", shot);
		} else if (s.startsWith("XEngine")) {
			if (chunkDamageVisible("Engine1") < 2)
				hitChunk("Engine1", shot);
		} else if (!s.startsWith("XProp1"))
			if (s.startsWith("xtail") || s.startsWith("XTail")) {
				if (chunkDamageVisible("Tail1") < 3)
					hitChunk("Tail1", shot);
			} else if (s.startsWith("xkeel")) {
				if (chunkDamageVisible("Keel1") < 2)
					hitChunk("Keel1", shot);
			} else if (s.startsWith("xrudder")) {
				if (chunkDamageVisible("Rudder1") < 1)
					hitChunk("Rudder1", shot);
			} else if (s.startsWith("xstab")) {
				if (s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
					hitChunk("StabL", shot);
				if (s.startsWith("xstabr") && chunkDamageVisible("StabR") < 2)
					hitChunk("StabR", shot);
			} else if (s.startsWith("xvator")) {
				if (s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
					hitChunk("VatorL", shot);
				if (s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
					hitChunk("VatorR", shot);
			} else if (s.startsWith("xwing")) {
				Aircraft.debugprintln(this, "*** xWing: " + s);
				if (s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
					hitChunk("WingLIn", shot);
				if (s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
					hitChunk("WingRIn", shot);
				if (s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
					hitChunk("WingLMid", shot);
				if (s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
					hitChunk("WingRMid", shot);
				if (s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
					hitChunk("WingLOut", shot);
				if (s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
					hitChunk("WingROut", shot);
			} else if (s.startsWith("xarone")) {
				if (s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
					hitChunk("AroneL", shot);
				if (s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
					hitChunk("AroneR", shot);
			}
	}

	public abstract void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException;

	public abstract void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException;

	public abstract void typeDiveBomberAdjDiveAngleMinus();

	public abstract void typeDiveBomberAdjDiveAnglePlus();

	public abstract void typeDiveBomberAdjDiveAngleReset();

	public abstract void typeDiveBomberAdjVelocityMinus();

	public abstract void typeDiveBomberAdjVelocityPlus();

	public abstract void typeDiveBomberAdjVelocityReset();

	public abstract void typeDiveBomberAdjAltitudeMinus();

	public abstract void typeDiveBomberAdjAltitudePlus();

	public abstract void typeDiveBomberAdjAltitudeReset();

	public abstract boolean typeDiveBomberToggleAutomation();

	public boolean bChangedPit;
	protected float kangle;
	private float suspR;
	private float suspL;
	private float suspension;
	private boolean needsToOpenBombays;
	public boolean blisterRemoved;
	private boolean sideDoorOpened;

	static {
		Class class1 = HS_123.class;
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
	}
}
