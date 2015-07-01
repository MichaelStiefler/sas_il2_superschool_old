/*
 * P-51R by Barnesy and CWatson @ http://freeil2modding.free-forum.net
 * 
 * Modified by SAS~Storebror 2014/08
 */

package com.maddox.il2.objects.air;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.rts.Finger;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.util.HashMapInt;

public class P_51R extends Scheme1 implements TypeFighter, TypeBNZFighter, TypeStormovik {

	public P_51R() {
		this.flapps = 0.0F;
		this.hasNo2Boost = false;
		this.engineAccelerationNoBoost = 0.0F;
		this.lastNo2Boost = 0L;
		this.lastNo2BoostFlames = 0L;
		this.numNo2Bottles = 0;
		this.wMax = 0.0F;
		this.wWEP = 0.0F;
		this.engineAfterburnerBoostFactor = 0.0F;
		this.oldNo2Active = false;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.checkBoostNo2Properties();
		this.FM.AS.setSpreadAngle(0);
	}

	public float getEyeLevelCorrection() {
		return 0.05F;
	}

	private void checkBoostNo2Properties() {
		Motor theMotor = this.FM.EI.engines[0];
		if (this.thisWeaponsName.indexOf("NO2_Boost") == -1) {
			Reflection.setBoolean(theMotor, "bHasAfterburnerControl", false);
			return;
		}
		this.hasNo2Boost = true;
		this.numNo2Bottles = Integer.parseInt(this.thisWeaponsName.substring(0, this.thisWeaponsName.indexOf("x")));
		this.FM.M.massEmpty += NO2_EMPTY_BOTTLE_MASS * numNo2Bottles;
		this.FM.M.nitro = NO2_MASS_PER_BOTTLE * (float)this.numNo2Bottles;
		this.engineAccelerationNoBoost = Reflection.getFloat(theMotor, "engineAcceleration");
		this.engineAfterburnerBoostFactor = Reflection.getFloat(theMotor, "engineAfterburnerBoostFactor");
		Reflection.setFloat(theMotor, "engineBoostFactor", 1.05F);
		Reflection.setFloat(theMotor, "tWaterMaxRPM", NO2_TWATER_MAX_RPM);
		this.wMax = Reflection.getFloat(theMotor, "wMax");
		this.wWEP = NO2_BOOST_RPM * (float)Math.PI * 2.0F / 60.0F;
		Reflection.setBoolean(theMotor, "bHasAfterburnerControl", true);
		Reflection.setInt(theMotor, "afterburnerType", 5);
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		float f3 = f <= 0.5F ? cvt(f, 0.0F, 0.2F, 0.0F, 95F) : cvt(f, 0.8F, 1.0F, 95F, 0.0F);
		float f4 = f1 <= 0.5F ? cvt(f1, 0.0F, 0.2F, 0.0F, 95F) : cvt(f1, 0.8F, 1.0F, 95F, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -105F * f2, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 90F * f2, 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 90F * f2, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -85F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f3, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 85F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f4, 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
		resetYPRmodifier();
		xyz[1] = cvt(f, 0.0F, 0.4F, 0.0F, -0.1F);
		hierMesh().chunkSetLocate("GearL9_D0", xyz, ypr);
	}

	// ************************************************************************************************
	// Gear code for backward compatibility, older base game versions don't indepently move their gears
	public static void moveGear(HierMesh hiermesh, float f) {
		moveGear(hiermesh, f, f, f); // re-route old style function calls to new code
	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f);
	}

	// ************************************************************************************************

	public void moveSteering(float f) {
	}

	public void moveWheelSink() {
		resetYPRmodifier();
		xyz[1] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.2F, 0.0F, 0.2F);
		hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
		xyz[1] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.2F, 0.0F, 0.2F);
		hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
	}

	public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
		hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		float f1 = (float)Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
		hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
		hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	protected void moveFan(float f) {
		hierMesh().chunkFind(Aircraft.Props[1][0]);
		int i = 0;
		for (int j = 0; j < 2; j++) {
			if (oldProp[j] < 2) {
				i = Math.abs((int)(FM.EI.engines[0].getw() * 0.06F));
				if (i >= 1)
					i = 1;
				if (i != oldProp[j] && hierMesh().isChunkVisible(Aircraft.Props[j][oldProp[j]])) {
					hierMesh().chunkVisible(Aircraft.Props[j][oldProp[j]], false);
					oldProp[j] = i;
					hierMesh().chunkVisible(Aircraft.Props[j][i], true);
				}
			}
			if (i == 0) {
				propPos[j] = (propPos[j] + 57.3F * FM.EI.engines[0].getw() * f) % 360F;
			} else {
				float f1 = 57.3F * FM.EI.engines[0].getw();
				f1 %= 2880F;
				f1 /= 2880F;
				if (f1 <= 0.5F)
					f1 *= 2.0F;
				else
					f1 = f1 * 2.0F - 2.0F;
				f1 *= 1200F;
				propPos[j] = (propPos[j] + f1 * f) % 360F;
			}
			if (j == 0)
				hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, -propPos[j], 0.0F);
			else
				hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, propPos[j], 0.0F);
		}
	}

	protected void moveFlap(float f) {
		float f1 = -52F * f;
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
	}

	public void doWoundPilot(int i, float f) {
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			break;
		}
	}

	public void hitProp(int i, int j, Actor actor) {
		if (i > FM.EI.getNum() - 1 || oldProp[i] == 2)
			return;
		if (isChunkAnyDamageVisible("Prop" + (i + 1)) || isChunkAnyDamageVisible("PropRot" + (i + 1))) {
			hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
			hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
			hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
		}
		super.hitProp(i, j, actor);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				debuggunnery("Armor: Hit..");
				if (s.endsWith("p1"))
					getEnergyPastArmor(World.Rnd().nextFloat(5F, 28.096F), shot);
				else if (s.endsWith("p2"))
					getEnergyPastArmor(13.350000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
				else if (s.endsWith("p3")) {
					getEnergyPastArmor((double)World.Rnd().nextFloat(20F, 40F) / (Math.abs(v1.x) + 9.9999997473787516E-005D),
							shot);
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
					if (shot.power <= 0.0F)
						doRicochetBack(shot);
				}
				return;
			}
			if (s.startsWith("xxcontrols")) {
				debuggunnery("Controls: Hit..");
				int i = s.charAt(10) - 48;
				if (s.endsWith("10"))
					i = 10;
				switch (i) {
				default:
					break;

				case 1: // '\001'
					if (getEnergyPastArmor(2.2F, shot) <= 0.0F)
						break;
					debuggunnery("Controls: Control Column: Hit, Controls Destroyed..");
					FM.AS.setControlsDamage(shot.initiator, 2);
					FM.AS.setControlsDamage(shot.initiator, 1);
					FM.AS.setControlsDamage(shot.initiator, 0);
					if (World.Rnd().nextFloat() < 0.25F)
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
					if (World.Rnd().nextFloat() < 0.25F)
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
					break;

				case 2: // '\002'
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
					break;

				case 3: // '\003'
				case 4: // '\004'
					if (World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F) {
						debuggunnery("Controls: Ailerones Controls: Out..");
						FM.AS.setControlsDamage(shot.initiator, 0);
					}
					break;

				case 5: // '\005'
				case 6: // '\006'
				case 8: // '\b'
				case 9: // '\t'
					if (getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F) {
						debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
						FM.AS.setControlsDamage(shot.initiator, 1);
					}
					break;

				case 7: // '\007'
				case 10: // '\n'
					if (getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F) {
						debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
						FM.AS.setControlsDamage(shot.initiator, 2);
					}
					break;
				}
				return;
			}
			if (s.startsWith("xxeng1")) {
				debuggunnery("Engine Module: Hit..");
				if (s.endsWith("prop") && getEnergyPastArmor(0.1F, shot) > 0.0F)
					FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
				if (s.endsWith("case") && getEnergyPastArmor(0.1F, shot) > 0.0F) {
					if (World.Rnd().nextFloat() < shot.power / 200000F) {
						FM.AS.setEngineStuck(shot.initiator, 0);
						debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
					}
					if (World.Rnd().nextFloat() < shot.power / 50000F) {
						FM.AS.hitEngine(shot.initiator, 0, 2);
						debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
					}
					if (World.Rnd().nextFloat() < shot.power / 28000F) {
						FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
						debuggunnery("Engine Module: Engine Crank Case Hit - Cylinder Feed Out, "
								+ FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
					}
					FM.EI.engines[0].setReadyness(shot.initiator,
							FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
					debuggunnery("Engine Module: Engine Crank Case Hit - Readyness Reduced to "
							+ FM.EI.engines[0].getReadyness() + "..");
				}
				if (s.endsWith("cyls") && getEnergyPastArmor(0.45F, shot) > 0.0F
						&& World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F) {
					FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
					debuggunnery("Engine Module: Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/"
							+ FM.EI.engines[0].getCylinders() + " Left..");
					if (FM.AS.astateEngineStates[0] < 1)
						FM.AS.hitEngine(shot.initiator, 0, 1);
					if (World.Rnd().nextFloat() < shot.power / 24000F) {
						FM.AS.hitEngine(shot.initiator, 0, 3);
						debuggunnery("Engine Module: Engine Cylinders Hit - Engine Fires..");
					}
					getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("supc") && getEnergyPastArmor(0.05F, shot) > 0.0F)
					FM.EI.engines[0].setKillCompressor(shot.initiator);
				if (s.endsWith("eqpt")) {
					if (World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
						FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
					if (World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
						FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
					if (World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
						FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
					if (World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
						FM.EI.engines[0].setEngineStuck(shot.initiator);
					if (World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
						FM.EI.engines[0].setEngineStops(shot.initiator);
				}
				if (s.endsWith("oil1"))
					FM.AS.hitOil(shot.initiator, 0);
				if (s.endsWith("wat1") && World.Rnd().nextFloat() < 0.05F)
					FM.AS.hitOil(shot.initiator, 0);
				if (s.startsWith("xxeng1mag")) {
					int j = s.charAt(9) - 49;
					FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
					debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
				}
				return;
			}
			if (s.startsWith("xxlock")) {
				debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
					nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
				}
				if (s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: VatorR Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
				}
				if (s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: AroneL Lock Shot Off..");
					nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
				}
				if (s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: AroneR Lock Shot Off..");
					nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxmgun0")) {
				int k = s.charAt(7) - 49;
				if (getEnergyPastArmor(0.5F, shot) > 0.0F) {
					debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
					FM.AS.setJamBullets(0, k);
					getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
				}
				return;
			}
			if (s.startsWith("xxoil")) {
				if (getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					FM.AS.hitOil(shot.initiator, 0);
					getEnergyPastArmor(0.22F, shot);
					debuggunnery("Engine Module: Oil Tank Pierced..");
				}
				return;
			}
			if (s.startsWith("xxtank")) {
				int l = s.charAt(6) - 49;
				if (getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					if (FM.AS.astateTankStates[l] == 0) {
						debuggunnery("Fuel Tank (" + l + "): Pierced..");
						FM.AS.hitTank(shot.initiator, l, 1);
						FM.AS.doSetTankState(shot.initiator, l, 1);
					}
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F) {
						FM.AS.hitTank(shot.initiator, l, 2);
						debuggunnery("Fuel Tank (" + l + "): Hit..");
					}
				}
			}
			return;
		}
		if (s.startsWith("xblister"))
			FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
		if (s.startsWith("xcf"))
			hitChunk("CF", shot);
		else if (s.startsWith("xeng")) {
			if (chunkDamageVisible("Engine1") < 2)
				hitChunk("Engine1", shot);
		} else if (s.startsWith("xtail")) {
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
			if (s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
				hitChunk("StabR", shot);
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
				hitChunk("VatorL", shot);
			if (s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
				hitChunk("VatorR", shot);
		} else if (s.startsWith("xwing")) {
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
		} else if (s.startsWith("xgear")) {
			if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
				debuggunnery("Hydro System: Disabled..");
				FM.AS.setInternalDamage(shot.initiator, 0);
			}
			if (s.endsWith("2") && World.Rnd().nextFloat() < 0.1F
					&& getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F) {
				debuggunnery("Undercarriage: Stuck..");
				FM.AS.setInternalDamage(shot.initiator, 3);
			}
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int i1;
			if (s.endsWith("a")) {
				byte0 = 1;
				i1 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				i1 = s.charAt(6) - 49;
			} else {
				i1 = s.charAt(5) - 49;
			}
			hitFlesh(i1, shot, byte0);
		}
	}

	public Point3d getTankBurnLightPoint(int i, Hook hook) {
		Loc loc = new Loc();
		Loc loc1 = new Loc();
		if (hook.chunkName().toLowerCase().startsWith("cf")) {
			hook.computePos(this, loc, loc1);
			Point3d point3d = loc1.getPoint();
			if (point3d.z > 0.0D)
				loc = new Loc(new Point3d(0.0D, 0.0D, 0.5D));
			else
				loc = new Loc(new Point3d(-1D, 0.0D, -1.5D));
			loc1 = new Loc();
		} else {
			loc = new Loc(new Point3d(-1D, 0.0D, 0.5D));
		}
		hook.computePos(this, loc, loc1);
		Point3d point3d1 = loc1.getPoint();
		return point3d1;
	}

	public void doEjectCatapult() {
		new MsgAction(false, this) {

			public void doAction(Object obj) {
				Aircraft aircraft = (Aircraft)obj;
				if (Actor.isValid(aircraft)) {
					Loc loc = new Loc();
					Loc loc1 = new Loc();
					Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
					HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
					((Actor)(aircraft)).pos.getAbs(loc1);
					hooknamed.computePos(aircraft, loc1, loc);
					loc.transform(vector3d);
					new EjectionSeat(1, loc, vector3d, aircraft);
				}
			}

		};
	}

	public void update(float f) {
		World.cur().diffCur.Torque_N_Gyro_Effects = false;
		float f1 = FM.EI.engines[0].getControlRadiator();
		if (Math.abs(flapps - f1) > 0.01F) {
			flapps = f1;
			hierMesh().chunkSetAngles("Oil_D0", 0.0F, 15F * f1, 0.0F);
			hierMesh().chunkSetAngles("Water_D0", 0.0F, -12F + 15F * f1, 0.0F);
		}
		if (this.hasNo2Boost) {
			this.setBoostNo2Status();
		} else {
			this.checkBoostNitroAvailable();
		}
		super.update(f);
		this.debugTemps();
	}

	private void setBoostNo2Status() {
		if (this != World.getPlayerAircraft() || NetMissionTrack.isPlaying()) {
			// Replicate changed No2 Boost State to the Net
			this.replicateNetBoostFromNet();
		} else {
			this.checkNo2ControlLock();
			this.checkNo2BoostTimeout();
			this.checkNo2LeanTempRaise();
			this.checkNo2BottlesAvailable();
		}
		this.showNo2BoostExhaustFlames();
		this.checkNo2BoostStateChanged();
		this.checkBoostNo2EngineAccelleration();
	}

	private void checkBoostNitroAvailable() {
		if (this.FM.EI.engines[0].getControlAfterburner()) {
			if (this.FM.M.nitro == 0.0F) {
				this.FM.EI.engines[0].setControlAfterburner(false);
				if (this == World.getPlayerAircraft()) {
					if (Main3D.cur3D().aircraftHotKeys.isAfterburner()) {
						Main3D.cur3D().aircraftHotKeys.setAfterburner(false);
					}
				}
			}
		}
	}

	private void checkNo2ControlLock() {
		Motor theMotor = this.FM.EI.engines[0];
		if (this == World.getPlayerAircraft()) {
			if (theMotor.getControlAfterburner()) {
				if (theMotor.isHasControlAfterburner()) {
					if (this.numNo2Bottles > 0) {
						Reflection.setBoolean(theMotor, "bHasAfterburnerControl", false);
						this.lastNo2Boost = Time.current();
					} else {
						Main3D.cur3D().aircraftHotKeys.setAfterburner(false);
					}
				}
			}
		}
	}

	private void checkNo2BoostTimeout() {
		Motor theMotor = this.FM.EI.engines[0];
		if (theMotor.getControlAfterburner()) {
			if (Time.current() - this.lastNo2Boost > NO2_BOOST_TIME && this.numNo2Bottles > 0) {
				if (this == World.getPlayerAircraft()) {
					Reflection.setBoolean(theMotor, "bHasAfterburnerControl", true);
					if (Main3D.cur3D().aircraftHotKeys.isAfterburner()) {
						Main3D.cur3D().aircraftHotKeys.setAfterburner(false);
					}
				}
				theMotor.setControlAfterburner(false);
				this.numNo2Bottles--;
				this.FM.M.nitro = NO2_MASS_PER_BOTTLE * (float)this.numNo2Bottles;
			}
		}
	}

	private void checkNo2LeanTempRaise() {
		Motor theMotor = this.FM.EI.engines[0];
		if (theMotor.getControlAfterburner()) {
			if (Time.current() - this.lastNo2Boost <= NO2_BOOST_TIME && this.numNo2Bottles > 0) {
				if (theMotor.getControlThrottle() < 1.0F) {
					theMotor.tOilIn += (1.0F - theMotor.getControlThrottle()) * 0.1F;
					theMotor.tWaterOut += (1.0F - theMotor.getControlThrottle()) * 0.1F;
				}
			}
		}
	}

	private void checkNo2BottlesAvailable() {
		Motor theMotor = this.FM.EI.engines[0];
		if (this.numNo2Bottles < 1) {
			if (theMotor.getControlAfterburner())
				theMotor.setControlAfterburner(false);
			if (theMotor.isHasControlAfterburner())
				Reflection.setBoolean(theMotor, "bHasAfterburnerControl", false);
		}
	}

	private void showNo2BoostExhaustFlames() {
		if (this.oldNo2Active && Time.current() - this.lastNo2BoostFlames > NO2_BOOST_FLAMES_INTERVAL) {
			this.lastNo2BoostFlames = Time.current();
			for (int i = 1; i < 32; i++) {
				try {
					Hook hook = this.findHook("_Engine1EF_" + (i >= 10 ? "" + i : "0" + i));
					if (hook != null)
						Eff3DActor.New(this, hook, null, 1.0F, "3DO/Effects/Aircraft/EngineBoostFlames.eff", -1F);
				} catch (Exception exception) {
				}
			}
		}
	}
	
	private void showNo2BoostExhaustSmoke() {
		for (int i = 1; i < 32; i++) {
			try {
				Hook hook = this.findHook("_Engine1EF_" + (i >= 10 ? "" + i : "0" + i));
				if (hook != null)
					Eff3DActor.New(this, hook, null, 1.0F,
							"3DO/Effects/Aircraft/EngineStart" + World.Rnd().nextInt(1, 3) + ".eff", -1F);
			} catch (Exception exception) {
			}
		}
	}

	private void checkNo2BoostStateChanged() {
		Motor theMotor = this.FM.EI.engines[0];
		boolean newNo2Active = theMotor.getControlAfterburner();
		if (newNo2Active != this.oldNo2Active) {
			Reflection.setFloat(theMotor, "propAngleDeviceAfterburnerParam", newNo2Active ? this.wWEP : this.wMax);
			Reflection.setFloat(theMotor, "wWEP", newNo2Active ? this.wWEP : this.wMax);
			Reflection.setFloat(theMotor, "engineAfterburnerBoostFactor", newNo2Active ? NO2_BOOST_FACTOR
					: this.engineAfterburnerBoostFactor);
			if (newNo2Active) {
				this.lastNo2BoostFlames = Time.current();
				this.showNo2BoostExhaustSmoke();
			}
			if (this==World.getPlayerAircraft() && !NetMissionTrack.isPlaying()) {
				// Replicate changed No2 Boost State to the Net
				this.replicateNetBoostToNet(newNo2Active);
			}
		}
		this.oldNo2Active = newNo2Active;
	}

	private void checkBoostNo2EngineAccelleration() {
		Motor theMotor = this.FM.EI.engines[0];
		boolean increaseAccelleration = theMotor.getControlAfterburner() && theMotor.getRPM() < NO2_BOOST_RPM;
		Reflection.setFloat((Object)theMotor, "engineAcceleration",
				increaseAccelleration ? this.engineAccelerationNoBoost * 20F : this.engineAccelerationNoBoost);
	}

	private void replicateNetBoostToNet(boolean boostEnable) {
		this.FM.AS.setSpreadAngle(boostEnable?1:0);
		this.FM.AS.replicateSpreadAngleToNet();
	}
	
	private void replicateNetBoostFromNet() {
		boolean boostEnable = this.FM.AS.getSpreadAngle() > 0;
		this.FM.EI.engines[0].setControlAfterburner(boostEnable);
		Reflection.setBoolean(Main3D.cur3D().aircraftHotKeys, "bAfterburner", boostEnable); // Required for working HUD Log
		if (Config.isUSE_RENDER() && this == World.getPlayerAircraft()) { // Manually replay HUD Log Right Bottom Message (screwed by TD)
			Main3D.cur3D().hud._logRightBottom(boostEnable ? "BoostWepTP" + this.FM.EI.engines[0].getAfterburnerType()
					: null);
		}
	}

	private void debugTemps() { // Helper for Overheat Settings fine tuning
		if (!_DEBUG_TEMPS)
			return;
		Motor theMotor = this.FM.EI.engines[0];
		HUD.training(twoPlaces.format(theMotor.tWaterOut) + "(" + twoPlaces.format(theMotor.tWaterCritMax) + "), "
				+ twoPlaces.format(theMotor.tOilOut) + "(" + twoPlaces.format(theMotor.tOilCritMax) + "), N="
				+ twoPlaces.format(this.FM.M.nitro) + ", R=" + twoPlaces.format(theMotor.getRPM()));
	}

	static void debugPrintln(String logLine) { // log.lst output for debug
		if (!_DEBUG)
			return;
		System.out.println(logLine);
	}

	static void debugPrint(String logLine) { // log.lst output for debug (no line break)
		if (!_DEBUG)
			return;
		System.out.print(logLine);
	}

	private static final boolean _DEBUG = false;
	private static final boolean _DEBUG_TEMPS = false;
	private static DecimalFormat twoPlaces = new DecimalFormat("#.##");
	private static final float NO2_BOOST_RPM = 3700F;
	private static final float NO2_MASS_PER_BOTTLE = 4.5359237F;
	private static final float NO2_EMPTY_BOTTLE_MASS = 12.0F;
	private static final long NO2_BOOST_TIME = 10000L;
	private static final float NO2_BOOST_FACTOR = 1.8F;
	private static final float NO2_TWATER_MAX_RPM = 120F;
	private static final float NO2_BOOST_FLAMES_INTERVAL = 500L;
	private float flapps;
	private boolean hasNo2Boost;
	private float engineAccelerationNoBoost;
	private long lastNo2Boost;
	private long lastNo2BoostFlames;
	private int numNo2Bottles;
	private float wMax;
	private float wWEP;
	private float engineAfterburnerBoostFactor;
	private boolean oldNo2Active;

	static {
		Class class1 = P_51R.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Racer");
		Property.set(class1, "meshName", "3DO/Plane/P_51R(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1945F);
		Property.set(class1, "yearExpired", 1980.5F);
		Property.set(class1, "FlightModel", "FlightModels/P_51R.fmd:P_51R");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_51R.class });
		Property.set(class1, "LOSElevation", 1.03F);
		Property.set(class1, "originCountry", PaintScheme.countryUSA);
		Aircraft.weaponTriggersRegister(class1, new int[] {});
		Aircraft.weaponHooksRegister(class1, new String[] {});
		try {
			ArrayList arraylist = new ArrayList();
			Property.set(class1, "weaponsList", arraylist);
			HashMapInt hashmapint = new HashMapInt();
			Property.set(class1, "weaponsMap", hashmapint);
			Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[0];
			String s = "default";
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			a_lweaponslot = new Aircraft._WeaponSlot[0];
			s = "1xNO2_Boost";
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			a_lweaponslot = new Aircraft._WeaponSlot[0];
			s = "3xNO2_Boost";
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			a_lweaponslot = new Aircraft._WeaponSlot[0];
			s = "5xNO2_Boost";
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			a_lweaponslot = new Aircraft._WeaponSlot[0];
			s = "10xNO2_Boost";
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
		} catch (Exception exception) {
		}
	}
}
