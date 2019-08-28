package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class G10N1 extends Scheme7 implements TypeBomber {
	
	protected void moveFlap(float f) {
		float f1 = -50F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	protected void moveBayDoor(float f) {
		this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -160F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -160F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, -160F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -160F * f, 0.0F);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 110F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -90F * f);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -90F * f);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, 90F), 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, 80F), 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, -80F), 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, -80F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, 80F), 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 0.0F, -100F * f);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, 0.0F, -100F * f);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		float f2 = Math.abs(f);
		switch (i) {
			default:
				break;

			case 0:
				if (f1 < -47F) {
					f1 = -47F;
					flag = false;
				}
				if (f1 > 47F) {
					f1 = 47F;
					flag = false;
				}
				if (f2 < 147F) {
					if (f1 < 0.5964912F * f2 - 117.6842F) {
						f1 = 0.5964912F * f2 - 117.6842F;
						flag = false;
					}
				} else if (f2 < 157F) {
					if (f1 < 0.3F * f2 - 74.1F) {
						f1 = 0.3F * f2 - 74.1F;
						flag = false;
					}
				} else if (f1 < 0.2173913F * f2 - 61.13044F) {
					f1 = 0.2173913F * f2 - 61.13044F;
					flag = false;
				}
				if (f2 >= 110F) if (f2 < 115F) {
					if (f1 < -5F && f1 > -20F) flag = false;
				} else if (f2 < 160F) {
					if (f1 < -5F) flag = false;
				} else if (f1 < 15F) flag = false;
				break;

			case 1:
				if (f1 < -47F) {
					f1 = -47F;
					flag = false;
				}
				if (f1 > 47F) {
					f1 = 47F;
					flag = false;
				}
				if (f < -38F) {
					if (f1 < -32F) {
						f1 = -32F;
						flag = false;
					}
				} else if (f < -16F) {
					if (f1 < 0.5909091F * f - 9.545455F) {
						f1 = 0.5909091F * f - 9.545455F;
						flag = false;
					}
				} else if (f < 35F) {
					if (f1 < -19F) {
						f1 = -19F;
						flag = false;
					}
				} else if (f < 44F) {
					if (f1 < -3.111111F * f + 89.88889F) {
						f1 = -3.111111F * f + 89.88889F;
						flag = false;
					}
				} else if (f < 139F) {
					if (f1 < -47F) {
						f1 = -47F;
						flag = false;
					}
				} else if (f < 150F) {
					if (f1 < 1.363636F * f - 236.5455F) {
						f1 = 1.363636F * f - 236.5455F;
						flag = false;
					}
				} else if (f1 < -32F) {
					f1 = -32F;
					flag = false;
				}
				if (f < -175.7F) {
					if (f1 < 80.8F) flag = false;
					break;
				}
				if (f < -82F) {
					if (f1 < -16F) flag = false;
					break;
				}
				if (f < 24F) {
					if (f1 < 0.0F) flag = false;
					break;
				}
				if (f < 32F) {
					if (f1 < -8.3F) flag = false;
					break;
				}
				if (f < 80F) {
					if (f1 < 0.0F) flag = false;
					break;
				}
				if (f < 174F) {
					if (f1 < 0.5F * f - 87F) flag = false;
					break;
				}
				if (f < 178.7F) {
					if (f1 < 0.0F) flag = false;
					break;
				}
				if (f1 < 80.8F) flag = false;
				break;

			case 2:
				if (f1 < -47F) {
					f1 = -47F;
					flag = false;
				}
				if (f1 > 47F) {
					f1 = 47F;
					flag = false;
				}
				if (f < -90F) flag = false;
				if (f > 90F) flag = false;
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag) {
			for (int i = 0; i < 6; i++)
				if (this.FM.AS.astateEngineStates[i] > 3 && this.FM.EI.engines[i].getReadyness() < 0.1F) this.FM.AS.repairEngine(i);

			for (int j = 0; j < 4; j++)
				if (this.FM.AS.astateTankStates[j] > 3 && this.FM.AS.astatePilotStates[4] < 50F && this.FM.AS.astatePilotStates[7] < 50F && World.Rnd().nextFloat() < 0.1F) this.FM.AS.repairTank(j);

		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xengine")) {
			int i = s.charAt(7) - 49;
			if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.hitEngine(shot.initiator, i, 1);
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int j;
			if (s.endsWith("a")) {
				byte0 = 1;
				j = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				j = s.charAt(6) - 49;
			} else j = s.charAt(5) - 49;
			this.hitFlesh(j, shot, byte0);
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 33:
				return super.cutFM(34, j, actor);

			case 36:
				return super.cutFM(37, j, actor);

			case 3:
				return false;

			case 4:
				return false;

			case 5:
				return false;

			case 6:
				return false;
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		super.update(f);
		
		// The following code serves the purpose to avoid tailstrikes on touchdown when AI is controlling the airplane.
		// Usually AI will land with flaps fully deployed, attempting to reach about 17° AoA.
		// That's too much for a plane like this, so we limit the AoA to 8° here.
		// Note that this only affects AI. A player can pull as much AoA as he likes, up and until the tailstrike happens.
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
            Maneuver maneuver = (Maneuver) this.FM;
            if ((maneuver.get_maneuver() == 25) && (maneuver.Alt < 60.0F)) { // Plane is in landing pattern and near ground
                if (maneuver.Or.getTangage() > 8F) { // Limit nose up attitude to 8 degrees on touchdown to avoid tail strike (only for AI)!
                    maneuver.Or.increment(0.0F, -(maneuver.Or.getTangage() - 8F), 0.0F); // apply AoA limit
                }
            }
        }
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 2:
				this.FM.turret[6].bIsOperable = false;
				break;

			case 3:
				this.FM.turret[4].bIsOperable = false;
				break;

			case 4:
				this.FM.turret[5].bIsOperable = false;
				break;

			case 5:
				this.FM.turret[0].bIsOperable = false;
				break;

			case 6:
				this.FM.turret[1].bIsOperable = false;
				break;

			case 7:
				this.FM.turret[2].bIsOperable = false;
				break;

			case 8:
				this.FM.turret[3].bIsOperable = false;
				break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			default:
				break;

			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("Head1_D0", false);
				if (this.hierMesh().isChunkVisible("Blister1_D0")) this.hierMesh().chunkVisible("Gore1_D0", true);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				if (this.hierMesh().isChunkVisible("Blister1_D0")) this.hierMesh().chunkVisible("Gore2_D0", true);
				break;

			case 2:
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("Pilot3_D1", true);
				break;

			case 3:
				this.hierMesh().chunkVisible("Pilot4_D0", false);
				this.hierMesh().chunkVisible("Pilot4_D1", true);
				break;

			case 4:
				this.hierMesh().chunkVisible("Pilot5_D0", false);
				this.hierMesh().chunkVisible("Pilot5_D1", true);
				break;
		}
	}

	public void msgShot(Shot shot) {
		this.setShot(shot);
		if ("CF_D3".equals(shot.chunkName)) return;
		else {
			super.msgShot(shot);
			return;
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
	
    public void moveWheelSink()
    {
    	// This is the gear's suspension code.
    	// The maximum wheelsink we accept is 0.6m (out of 0.0m ... 1.0m). Above this, the gear gets stiff.
    	// The suspension will sink max. 0.5m, which means at full gear pressure, the tire will be flattened by 10cm.
    	
        resetYPRmodifier();
        // Calculate sink value for left gear. 
        xyz[2] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.6F, 0.0F, 0.5F);
        // Apply suspension movement to part "L4". "L1" is the wheel, it's attached to "L4" (which sinks in), which in turn is attached to "L2" (which holds the clip hook, that hook must not move).
        hierMesh().chunkSetLocate("GearL4_D0", xyz, ypr);
        
        resetYPRmodifier();
        // Calculate sink value for right gear. 
        xyz[2] = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.6F, 0.0F, 0.5F);
        // Apply suspension movement to part "R4". "R1" is the wheel, it's attached to "R4" (which sinks in), which in turn is attached to "R2" (which holds the clip hook, that hook must not move).
        hierMesh().chunkSetLocate("GearR4_D0", xyz, ypr);
        
        resetYPRmodifier();
        // Calculate sink value for nose gear. 
        xyz[2] = cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.6F, 0.0F, 0.5F);
        // Apply suspension movement to part "C0". "C1" is the wheel, it's attached to "C2" (which holds the clip hook, that hook must not move).
        // Therefore we had to squeeze a new "Placeholder" mesh in, in order to get the suspension to move. That placeholder is the "C0" part.
        hierMesh().chunkSetLocate("GearC0_D0", xyz, ypr);
    
    }
    
    public void moveSteering(float f)
    {
    	// Move the nose wheel according to steering input
    	hierMesh().chunkSetAngles("GearC0b_D0", f, 0.0F, 0.0F);
    }

    
    protected void moveFan(float f) {
    	super.moveFan(f);
    	
    	// This code is used to invert the prop rotation on props 1, 3 and 5 (index 0, 2 and 4)
    	for (int propIndex = 0; propIndex < this.FM.EI.getNum(); propIndex += 2) { // select every 2nd prop, starting with index 0
    		hierMesh().chunkSetAngles(Props[propIndex][0], 0.0F, this.propPos[propIndex], 0.0F); // Invert rotation. In the base Aircraft class, the 4th paramter would be "-this.propPos[propIndex]".
    	}
    }
    
	static {
		Class class1 = G10N1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "G10N1");
		Property.set(class1, "meshName", "3Do/Plane/G10N1(multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryJapan);
		Property.set(class1, "yearService", 1945F);
		Property.set(class1, "yearExpired", 1947F);
		Property.set(class1, "cockpitClass", new Class[] { Cockpit_G10N1_P.class });
		Property.set(class1, "FlightModel", "FlightModels/G10N1.fmd:G10N1_FM");
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04",
				"_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12" });
		try {
			ArrayList arraylist = new ArrayList();
			Property.set(class1, "weaponsList", arraylist);
			HashMapInt hashmapint = new HashMapInt();
			Property.set(class1, "weaponsMap", hashmapint);
			byte byte0 = 24;
			Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
			String s = "default";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 700);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 700);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 700);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 700);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 700);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 700);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "bombs";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 700);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 700);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 700);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 700);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 700);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 700);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "none";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = null;
			a_lweaponslot[1] = null;
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
		} catch (Exception exception) {}
	}
}
