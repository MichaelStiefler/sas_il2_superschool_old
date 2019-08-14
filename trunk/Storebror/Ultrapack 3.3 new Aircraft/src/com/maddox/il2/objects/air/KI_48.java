package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class KI_48 extends Scheme2 {

	public KI_48() {
		this.suspR = 0.0F;
		this.suspL = 0.0F;
		this.tme = 0L;
		this.topGunnerPosition = 1.0F;
		this.curTopGunnerPosition = 1.0F;
	}

	protected void moveFlap(float f) {
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 50F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 50F * f, 0.0F);
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		float f3 = Aircraft.floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear3Scale);
		float f4 = Aircraft.floatindex(Aircraft.cvt(f1, 0.0F, 1.0F, 0.0F, 4F), gear3Scale);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -45.5F * f3, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -45.5F * f4, 0.0F);
		float f5 = Aircraft.floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear7Scale);
		float f6 = Aircraft.floatindex(Aircraft.cvt(f1, 0.0F, 1.0F, 0.0F, 4F), gear7Scale);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, -54F * f5, 0.0F);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -54F * f6, 0.0F);
		hiermesh.chunkSetAngles("GearL9_D0", 0.0F, -60F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR9_D0", 0.0F, -60F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 105F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 105F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 75F * f2, 0.0F);
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.xyz[1] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		Aircraft.xyz[1] = f1 * -0.2F;
		hiermesh.chunkSetLocate("GearR8_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[1] = f * -0.2F;
		hiermesh.chunkSetLocate("GearL8_D0", Aircraft.xyz, Aircraft.ypr);
	}

	protected void moveAileron(float f) {
		if (f > 0.0F) {
			this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 25F * f, 0.0F);
			this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -15F * f, 0.0F);
		} else {
			this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 15F * f, 0.0F);
			this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -25F * f, 0.0F);
		}
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
	}

	protected void moveElevator(float f) {
		if (f > 0.0F) {
			this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -25F * f, 0.0F);
			this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -25F * f, 0.0F);
		} else {
			this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -18F * f, 0.0F);
			this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -18F * f, 0.0F);
		}
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(this.hierMesh(), f, f1, f2);
	}

	public void moveWheelSink() {
		this.suspL = 0.9F * this.suspL + 0.1F * this.FM.Gears.gWheelSinking[0];
		this.suspR = 0.9F * this.suspR + 0.1F * this.FM.Gears.gWheelSinking[1];
		if (this.suspL > 0.035F) this.suspL = 0.035F;
		if (this.suspR > 0.035F) this.suspR = 0.035F;
		if (this.suspL < 0.0F) this.suspL = 0.0F;
		if (this.suspR < 0.0F) this.suspR = 0.0F;
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.xyz[1] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		float f = -793.5F;
		Aircraft.xyz[1] = this.suspL * 3.5F;
		this.hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, -this.suspL * f, 0.0F);
		this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, this.suspL * f, 0.0F);
		Aircraft.xyz[1] = this.suspR * 3.5F;
		this.hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, -this.suspR * f, 0.0F);
		this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, this.suspR * f, 0.0F);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag) {
			if (this.FM.AS.astateEngineStates[0] > 3) {
				if (World.Rnd().nextFloat() < 0.03F) this.FM.AS.hitTank(this, 0, 1);
				if (World.Rnd().nextFloat() < 0.03F) this.FM.AS.hitTank(this, 1, 1);
			}
			if (this.FM.AS.astateEngineStates[1] > 3) {
				if (World.Rnd().nextFloat() < 0.03F) this.FM.AS.hitTank(this, 2, 1);
				if (World.Rnd().nextFloat() < 0.03F) this.FM.AS.hitTank(this, 3, 1);
			}
			if (this.FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.03F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
			if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.03F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
			if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 2, 1);
			if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 1, 1);
			if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.03F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
			if (this.FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.03F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
		}
		for (int i = 1; i < 6; i++)
			if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	private void setRadist(int i) {
		if (this.FM.AS.astatePilotStates[2] > 90) return;
		this.hierMesh().chunkVisible("HMask4_D0", false);
		this.hierMesh().chunkVisible("HMask5_D0", false);
		switch (i) {
			case 0:
				this.topGunnerPosition = 0.0F;
				break;

			case 1:
				this.topGunnerPosition = 1.0F;
				break;
		}
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f < -27F) {
					f = -27F;
					flag = false;
				}
				if (f > 27F) {
					f = 27F;
					flag = false;
				}
				if (f1 < -30F) {
					f1 = -30F;
					flag = false;
				}
				if (f1 > 30F) {
					f1 = 30F;
					flag = false;
				}
				break;

			case 1:
				if (f < -135F) f = -135F;
				if (f > 135F) f = 135F;
				if (f1 < -69F) {
					f1 = -69F;
					flag = false;
				}
				if (f1 > 45F) {
					f1 = 45F;
					flag = false;
				}
				float f2;
				for (f2 = Math.abs(f); f2 > 180F; f2 -= 180F)
					;
				if (f1 < -Aircraft.floatindex(Aircraft.cvt(f2, 0.0F, 180F, 0.0F, 36F), af)) f1 = -Aircraft.floatindex(Aircraft.cvt(f2, 0.0F, 180F, 0.0F, 36F), af);
				break;

			case 2:
				if (f < -30F) {
					f = -30F;
					flag = false;
				}
				if (f > 30F) {
					f = 30F;
					flag = false;
				}
				if (f1 < -45F) {
					f1 = -45F;
					flag = false;
				}
				if (f1 > 15F) {
					f1 = 15F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 35:
			default:
				break;

			case 33:
				this.hitProp(0, j, actor);
				break;

			case 36:
				this.hitProp(1, j, actor);
				break;

			case 34:
				this.FM.AS.hitEngine(this, 0, 2);
				if (World.Rnd().nextInt(0, 99) < 66) this.FM.AS.hitEngine(this, 0, 2);
				break;

			case 37:
				this.FM.AS.hitEngine(this, 1, 2);
				if (World.Rnd().nextInt(0, 99) < 66) this.FM.AS.hitEngine(this, 1, 2);
				break;
		}
		return super.cutFM(i, j, actor);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) this.getEnergyPastArmor(World.Rnd().nextFloat(12.5F, 16F), shot);
				else if (s.endsWith("p2")) this.getEnergyPastArmor(1.01D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
				else if (s.endsWith("p3")) this.getEnergyPastArmor(World.Rnd().nextFloat(12.5F, 16F), shot);
				else if (s.endsWith("p4")) this.getEnergyPastArmor(World.Rnd().nextFloat(12.5F, 16F), shot);
			} else if (s.startsWith("xxcontrols")) {
				int i = s.charAt(10) - 48;
				if (s.endsWith("10")) i = 10;
				else if (s.endsWith("11")) i = 11;
				else if (s.endsWith("12")) i = 12;
				switch (i) {
					case 1:
						if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
							this.debuggunnery("*** Control Column: Hit, Controls Destroyed..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 2:
						this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
						break;

					case 3:
						if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setControlsDamage(shot.initiator, 2);
							if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setControlsDamage(shot.initiator, 1);
							if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 4:
						if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
							if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
							if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
						}
						break;

					case 5:
						if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
							if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
							if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 7);
						}
						break;

					case 6:
					case 7:
						if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
							this.FM.AS.setControlsDamage(shot.initiator, 0);
							this.debuggunnery("*** Aileron Controls: Disabled..");
						}
						break;

					case 8:
						if (this.getEnergyPastArmor(0.12F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
							if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
							if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
						}
						break;

					case 9:
						if (this.getEnergyPastArmor(0.12F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
							if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
							if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 7);
						}
						break;

					case 10:
					case 11:
						if (this.getEnergyPastArmor(0.002F, shot) > 0.0F) {
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.debuggunnery("*** Elevator Controls: Disabled / Strings Broken..");
						}
						break;

					case 12:
						if (this.getEnergyPastArmor(2.3F, shot) > 0.0F) {
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							this.debuggunnery("*** Rudder Controls: Disabled..");
						}
						break;
				}
			} else if (s.startsWith("xxspar")) {
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if ((s.endsWith("e1") || s.endsWith("e2")) && this.getEnergyPastArmor(32F, shot) > 0.0F) {
					this.debuggunnery("*** Engine1 Suspension Broken in Half..");
					this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
				}
				if ((s.endsWith("e3") || s.endsWith("e4")) && this.getEnergyPastArmor(32F, shot) > 0.0F) {
					this.debuggunnery("*** Engine2 Suspension Broken in Half..");
					this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
				}
			} else if (s.startsWith("xxlock")) {
				if (s.startsWith("xxlockal")) {
					if (this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
						this.debuggunnery("*** AroneL Lock Damaged..");
						this.nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
					}
				} else if (s.startsWith("xxlockar")) {
					if (this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
						this.debuggunnery("*** AroneR Lock Damaged..");
						this.nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
					}
				} else if (s.startsWith("xxlockvl")) {
					if (this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
						this.debuggunnery("*** VatorL Lock Damaged..");
						this.nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
					}
				} else if (s.startsWith("xxlockvr")) {
					if (this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
						this.debuggunnery("*** VatorR Lock Damaged..");
						this.nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
					}
				} else if (s.startsWith("xxlockr")) {
					if (this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
						this.debuggunnery("*** Rudder1 Lock Damaged..");
						this.nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
					}
				} else if (s.startsWith("xxlockl") && this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
					this.debuggunnery("*** Rudder2 Lock Damaged..");
					this.nextDMGLevels(1, 2, "Rudder2_D0", shot.initiator);
				}
			} else if (s.startsWith("xxbomb")) {
				if (World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
					this.debuggunnery("*** Bomb Payload Detonates.. CF_D" + this.chunkDamageVisible("CF"));
					this.FM.AS.hitTank(shot.initiator, 0, 100);
					this.FM.AS.hitTank(shot.initiator, 1, 100);
					this.FM.AS.hitTank(shot.initiator, 2, 100);
					this.FM.AS.hitTank(shot.initiator, 3, 100);
					this.nextDMGLevels(3, 2, "CF_D" + this.chunkDamageVisible("CF"), shot.initiator);
				}
			} else if (s.startsWith("xxeng")) {
				int j = s.charAt(5) - 49;
				if (s.endsWith("prop") && this.getEnergyPastArmor(1.2F, shot) > 0.0F) this.FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) {
							this.FM.AS.setEngineStuck(shot.initiator, j);
							this.debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, j, 2);
							this.debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Damaged..");
						}
					} else if (World.Rnd().nextFloat() < 0.04F) this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
					else {
						this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - 0.02F);
						this.debuggunnery("*** Engine" + j + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
					}
					this.getEnergyPastArmor(12F, shot);
				}
				if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 0.9878F) {
						this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
						this.debuggunnery("*** Engine" + j + " Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 48000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							this.debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
						}
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("supc")) {
					if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) this.FM.EI.engines[j].setKillCompressor(shot.initiator);
					this.getEnergyPastArmor(2.0F, shot);
				}
				if (s.endsWith("eqpt")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						if (Aircraft.Pd.y > 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
						if (Aircraft.Pd.y < 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
					}
					this.getEnergyPastArmor(2.0F, shot);
				}
				if (s.endsWith("mag1")) {
					this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
					this.debuggunnery("*** Engine" + j + " Magneto 1 Destroyed..");
				}
				if (s.endsWith("mag2")) {
					this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
					this.debuggunnery("*** Engine" + j + " Magneto 2 Destroyed..");
				}
			} else if (s.startsWith("xxoil")) {
				int k = s.charAt(5) - 49;
				if (this.getEnergyPastArmor(4.21F, shot) > 0.0F) {
					this.FM.AS.hitOil(shot.initiator, k);
					this.getEnergyPastArmor(0.42F, shot);
				}
			} else if (s.startsWith("xxtank")) {
				int l = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.6F, shot) > 0.0F) {
					if (this.FM.AS.astateTankStates[l] == 0) {
						this.FM.AS.hitTank(shot.initiator, l, 1);
						this.FM.AS.doSetTankState(shot.initiator, l, 1);
					}
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F) this.FM.AS.hitTank(shot.initiator, l, 2);
				}
			} else if (s.startsWith("xxammo")) {
				if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setJamBullets(10, 0);
			} else if (s.startsWith("xxpnm") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F) {
				this.debuggunnery("Pneumo System: Disabled..");
				this.FM.AS.setInternalDamage(shot.initiator, 1);
			}
		} else {
			if (s.startsWith("xcockpit"));
			if (s.startsWith("xcf")) {
				if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
			} else if (s.startsWith("xtail")) {
				if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
			} else if (s.startsWith("xkeel")) {
				if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
			} else if (s.startsWith("xrudder1")) {
				if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
			} else if (s.startsWith("xrudder2")) {
				if (this.chunkDamageVisible("Rudder2") < 2) this.hitChunk("Rudder2", shot);
			} else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
			else if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
			else if (s.startsWith("xvatorl")) {
				if (this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
			} else if (s.startsWith("xvatorr")) {
				if (this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
			} else if (s.startsWith("xwinglin")) {
				if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
			} else if (s.startsWith("xwingrin")) {
				if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
			} else if (s.startsWith("xwinglmid")) {
				if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
			} else if (s.startsWith("xwingrmid")) {
				if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
			} else if (s.startsWith("xwinglout")) {
				if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
			} else if (s.startsWith("xwingrout")) {
				if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
			} else if (s.startsWith("xaronel")) {
				if (this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
			} else if (s.startsWith("xaroner")) {
				if (this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
			} else if (s.startsWith("xengine1")) {
				if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
			} else if (s.startsWith("xengine2")) {
				if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
			} else if (s.startsWith("xgear")) {
				if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
					this.debuggunnery("Hydro System: Disabled..");
					this.FM.AS.setInternalDamage(shot.initiator, 0);
				}
				if (s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F) {
					this.debuggunnery("Undercarriage: Stuck..");
					this.FM.AS.setInternalDamage(shot.initiator, 3);
				}
			} else if (s.startsWith("xturret")) {
				if (s.startsWith("xturret1")) {
					this.debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
					this.FM.AS.setJamBullets(10, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 66.35F), shot);
				}
				if (s.startsWith("xturret2")) {
					this.debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
					this.FM.AS.setJamBullets(11, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 66.35F), shot);
				}
				if (s.startsWith("xturret3")) {
					this.debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
					this.FM.AS.setJamBullets(12, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 66.35F), shot);
				}
			} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
				byte byte0 = 0;
				int i1;
				if (s.endsWith("a") || s.endsWith("a2")) {
					byte0 = 1;
					i1 = s.charAt(6) - 49;
				} else if (s.endsWith("b") || s.endsWith("b2")) {
					byte0 = 2;
					i1 = s.charAt(6) - 49;
				} else i1 = s.charAt(5) - 49;
				this.hitFlesh(i1, shot, byte0);
			}
		}
	}

	public void msgExplosion(Explosion explosion) {
		this.setExplosion(explosion);
		if (explosion.chunkName == null || explosion.power <= 0.0F || !explosion.chunkName.equals("Tail1_D3") && !explosion.chunkName.equals("WingLIn_D3") && !explosion.chunkName.equals("WingRIn_D3") && !explosion.chunkName.equals("WingLMid_D3")
				&& !explosion.chunkName.equals("WingRMid_D3") && !explosion.chunkName.equals("WingLOut_D3") && !explosion.chunkName.equals("WingROut_D3") && !explosion.chunkName.equals("Engine1_D2") && !explosion.chunkName.equals("Engine2_D2"))
			super.msgExplosion(explosion);
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 1:
				this.FM.turret[0].bIsOperable = false;
				break;

			case 2:
				this.FM.turret[1].bIsOperable = false;
				break;

			case 3:
				this.FM.turret[2].bIsOperable = false;
				break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			default:
				break;

			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				break;

			case 2:
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("Pilot3_D1", true);
				this.hierMesh().chunkVisible("HMask3_D0", false);
				break;

			case 3:
				if (this.hierMesh().isChunkVisible("Pilot4_D0")) {
					this.hierMesh().chunkVisible("Pilot4_D0", false);
					this.hierMesh().chunkVisible("Pilot4_D1", true);
					this.hierMesh().chunkVisible("HMask4_D0", false);
				} else {
					this.hierMesh().chunkVisible("Pilot5_D0", false);
					this.hierMesh().chunkVisible("Pilot5_D1", true);
					this.hierMesh().chunkVisible("HMask5_D0", false);
				}
				break;

			case 4:
				if (this.hierMesh().isChunkVisible("Pilot5_D0")) {
					this.hierMesh().chunkVisible("Pilot5_D0", false);
					this.hierMesh().chunkVisible("Pilot5_D1", true);
					this.hierMesh().chunkVisible("HMask5_D0", false);
				} else {
					this.hierMesh().chunkVisible("Pilot4_D0", false);
					this.hierMesh().chunkVisible("Pilot4_D1", true);
					this.hierMesh().chunkVisible("HMask4_D0", false);
				}
				break;
		}
	}

	public void doRemoveBodyFromPlane(int i) {
		super.doRemoveBodyFromPlane(i);
		if (i >= 3) {
			this.doRemoveBodyChunkFromPlane("Pilot4");
			this.doRemoveBodyChunkFromPlane("Head4");
		}
	}

	public boolean typeBomberToggleAutomation() {
		return false;
	}

	public void typeBomberAdjDistanceReset() {
	}

	public void typeBomberAdjDistancePlus() {
	}

	public void typeBomberAdjDistanceMinus() {
	}

	public void typeBomberAdjSideslipReset() {
	}

	public void typeBomberAdjSideslipPlus() {
	}

	public void typeBomberAdjSideslipMinus() {
	}

	public void typeBomberAdjAltitudeReset() {
	}

	public void typeBomberAdjAltitudePlus() {
	}

	public void typeBomberAdjAltitudeMinus() {
	}

	public void typeBomberAdjSpeedReset() {
	}

	public void typeBomberAdjSpeedPlus() {
	}

	public void typeBomberAdjSpeedMinus() {
	}

	public void typeBomberUpdate(float f) {
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	public void update(float f) {
//        this.onAircraftLoaded();
		if (this.FM.isPlayers()) if (!Main3D.cur3D().isViewOutside()) {
			this.hierMesh().chunkVisible("Turret1B_D0", false);
			this.hierMesh().chunkVisible("Turret2B_D0", false);
		} else {
			this.hierMesh().chunkVisible("Turret1B_D0", true);
			this.hierMesh().chunkVisible("Turret2B_D0", true);
		}
		super.update(f);
		if (Time.current() > this.tme) {
			this.tme = Time.current() + World.Rnd().nextLong(1000L, 5000L);
			if (this.FM.turret.length != 0) {
				Actor actor = null;
				for (int i = 0; i < 2; i++)
					if (this.FM.turret[i].bIsOperable) actor = this.FM.turret[i].target;

				for (int l = 0; l < 2; l++)
					this.FM.turret[l].target = actor;

				if (actor != null && Actor.isValid(actor)) {
					this.pos.getAbs(Aircraft.tmpLoc2);
					actor.pos.getAbs(Aircraft.tmpLoc3);
					Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
					if (Aircraft.tmpLoc3.getPoint().z > 0.0D) this.setRadist(1);
					else this.setRadist(0);
				}
			}
		}
		if (this.FM.AS.astatePilotStates[2] < 90) {
			if (this.topGunnerPosition > 0.5F) {
				this.curTopGunnerPosition += 0.1F * f;
				if (this.curTopGunnerPosition > 1.0F) this.curTopGunnerPosition = 1.0F;
			} else {
				this.curTopGunnerPosition -= 0.1F * f;
				if (this.curTopGunnerPosition < 0.0F) this.curTopGunnerPosition = 0.0F;
			}
			if (this.curTopGunnerPosition > 0.1F);
			if (this.curTopGunnerPosition < 0.9F);
			this.FM.turret[0].bIsOperable = true;
			this.FM.turret[1].bIsOperable = true;
			this.FM.turret[2].bIsOperable = true;
		}
		this.hierMesh().chunkVisible("Turret3B_D0", this.curTopGunnerPosition <= 0.01F);
		this.hierMesh().chunkVisible("Gun_D0", this.curTopGunnerPosition > 0.01F);
		this.resetYPRmodifier();
		Aircraft.xyz[0] = Aircraft.cvt(this.curTopGunnerPosition, 0.0F, 0.15F, 0.43105F, 0.0F);
		Aircraft.xyz[1] = Aircraft.cvt(this.curTopGunnerPosition, 0.0F, 0.15F, 0.396F, 0.0F);
		Aircraft.xyz[2] = Aircraft.cvt(this.curTopGunnerPosition, 0.0F, 0.15F, -0.7906F, 0.0F);
		this.hierMesh().chunkSetLocate("Gun_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, Aircraft.cvt(this.curTopGunnerPosition, 0.4F, 0.5F, -32F, 0.0F), 0.0F);
		if (this.FM.AS.astatePilotStates[2] < 90) {
			this.hierMesh().chunkVisible("Pilot4_D0", this.curTopGunnerPosition <= 0.75F);
			this.resetYPRmodifier();
			Aircraft.xyz[1] = Aircraft.cvt(this.curTopGunnerPosition, 0.5F, 0.75F, 0.0F, -0.45715F);
			Aircraft.xyz[2] = Aircraft.cvt(this.curTopGunnerPosition, 0.5F, 0.75F, 0.0F, 0.239F);
			Aircraft.ypr[2] = Aircraft.cvt(this.curTopGunnerPosition, 0.5F, 0.75F, 0.0F, -40F);
			this.hierMesh().chunkSetLocate("Pilot4_D0", Aircraft.xyz, Aircraft.ypr);
		}
		if (this.FM.AS.astatePilotStates[2] < 90) {
			this.hierMesh().chunkVisible("Pilot5_D0", this.curTopGunnerPosition > 0.75F);
			this.resetYPRmodifier();
			Aircraft.xyz[1] = Aircraft.cvt(this.curTopGunnerPosition, 0.75F, 1.0F, 0.0443F, 0.0F);
			Aircraft.xyz[2] = Aircraft.cvt(this.curTopGunnerPosition, 0.75F, 1.0F, -0.1485F, 0.0F);
			Aircraft.ypr[2] = Aircraft.cvt(this.curTopGunnerPosition, 0.75F, 1.0F, -45F, 0.0F);
			this.hierMesh().chunkSetLocate("Pilot5_D0", Aircraft.xyz, Aircraft.ypr);
		}
		float f1 = 25F * this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps[0] - f1) > 0.01F) {
			this.flapps[0] = f1;
			for (int j = 1; j <= 10; j++) {
				String s = "Radiator1_" + j + "_D0";
				if (j <= 5) this.hierMesh().chunkSetAngles(s, 0.0F, -f1, 0.0F);
				else this.hierMesh().chunkSetAngles(s, 0.0F, f1, 0.0F);
			}

		}
		f1 = 25F * this.FM.EI.engines[1].getControlRadiator();
		if (Math.abs(this.flapps[1] - f1) > 0.01F) {
			this.flapps[1] = f1;
			for (int k = 1; k <= 10; k++) {
				String s1 = "Radiator2_" + k + "_D0";
				if (k > 5) this.hierMesh().chunkSetAngles(s1, 0.0F, -f1, 0.0F);
				else this.hierMesh().chunkSetAngles(s1, 0.0F, f1, 0.0F);
			}

		}
		if (this.FM.AS.astateBailoutStep > 1) this.hierMesh().chunkVisible("Turret3B_D0", this.hierMesh().isChunkVisible("Blister2_D0"));
	}

	float                      suspR;
	float                      suspL;
	private long               tme;
	private float              topGunnerPosition;
	private float              curTopGunnerPosition;
	private static final float gear3Scale[] = { 0.0F, 0.84F, 1.4F, 1.46F, 1.0F };
	private static final float gear7Scale[] = { 0.0F, 0.22F, 0.46F, 0.65F, 1.0F };
	private float              flapps[]     = { 0.0F, 0.0F };

	static {
		Class class1 = KI_48.class;
		Property.set(class1, "originCountry", PaintScheme.countryJapan);
	}
}
