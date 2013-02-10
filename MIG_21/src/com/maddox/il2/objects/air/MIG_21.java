package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombSPRD99;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

import java.io.IOException;

public class MIG_21 extends Scheme1
implements TypeSupersonic, TypeFastJet, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit
{
	private float oldthrl;
	private float curthrl;
	private float engineSurgeDamage;
	private float SonicBoom = 0.0F;
	private Eff3DActor shockwave;
	private boolean isSonic;
	private boolean overrideBailout;
	private boolean ejectComplete;
	private boolean pylonOccupied;
	private Bomb booster[];
	protected boolean bHasBoosters;
	protected long boosterFireOutTime;
	private SoundFX fxSirena = newSound("aircraft.Sirena2", false);
	private Sample smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
	private boolean sirenaSoundPlaying = false;
	private boolean bRadarWarning;
	protected boolean bHasSK1Seat = true;
	private static Actor hunted = null;
	private Eff3DActor gearLightsEffects[] = { null, null, null};
	private LightPointActor gearLightsLights[] = { null, null, null};
	private boolean bGearLightsOn = false;
	private boolean bGearLightState = false;

	public MIG_21()
	{
		k14Mode = 0;
		k14WingspanType = 0;
		k14Distance = 200F;
		overrideBailout = false;
		ejectComplete = false;
		oldthrl = -1F;
		curthrl = -1F;
		pylonOccupied = false;
		booster = new Bomb[2];
		bHasBoosters = true;
		boosterFireOutTime = -1L;
		smplSirena.setInfinite(true);
	}

	/**
	 * G-Force Resistance, Tolerance and Recovery parmeters. See
	 * TypeGSuit.GFactors Private fields implementation for further details.
	 */
	private static final float NEG_G_TOLERANCE_FACTOR = 1.0F;
	private static final float NEG_G_TIME_FACTOR = 1.0F;
	private static final float NEG_G_RECOVERY_FACTOR = 1.0F;
	private static final float POS_G_TOLERANCE_FACTOR = 1.8F;
	private static final float POS_G_TIME_FACTOR = 1.5F;
	private static final float POS_G_RECOVERY_FACTOR = 1.0F;

	public void getGFactors(GFactors theGFactors) {
		theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR,
				NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR,
				POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
	}

	public void rareAction(float f, boolean flag)
	{
		super.rareAction(f, flag);
		if ((FM.Gears.nearGround() || FM.Gears.onGround())
				&& FM.CT.getCockpitDoor() == 1.0F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0",
					this.hierMesh().isChunkVisible("Pilot1_D0"));
		}
	}

	/* TODO: Not needed?
	private void speedcontrol(float f)
	{
		if(((FlightModelMain) (super.FM)).AP.way.isLanding() && (double)super.FM.getSpeed() > (double)((FlightModelMain) (super.FM)).VmaxFLAPS * 1.2D)
			((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.3F;
		else
			if(((FlightModelMain) (super.FM)).AP.way.isLanding() && (double)super.FM.getSpeed() < (double)((FlightModelMain) (super.FM)).VmaxFLAPS)
				((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
	}
*/
	public boolean typeFighterAceMakerToggleAutomation()
	{
		k14Mode++;
		if(k14Mode > 2)
			k14Mode = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
		return true;
	}

	public void typeFighterAceMakerAdjDistanceReset()
	{
	}

	public void typeFighterAceMakerAdjDistancePlus()
	{
		k14Distance += 10F;
		if(k14Distance > 800F)
			k14Distance = 800F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
	}

	public void typeFighterAceMakerAdjDistanceMinus()
	{
		k14Distance -= 10F;
		if(k14Distance < 200F)
			k14Distance = 200F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
	}

	public void typeFighterAceMakerAdjSideslipReset()
	{
	}

	public void typeFighterAceMakerAdjSideslipPlus()
	{
		k14WingspanType--;
		if(k14WingspanType < 0)
			k14WingspanType = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
	}

	public void typeFighterAceMakerAdjSideslipMinus()
	{
		k14WingspanType++;
		if(k14WingspanType > 9)
			k14WingspanType = 9;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
	}

	public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
	throws IOException
	{
		netmsgguaranted.writeByte(k14Mode);
		netmsgguaranted.writeByte(k14WingspanType);
		netmsgguaranted.writeFloat(k14Distance);
	}

	public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
	throws IOException
	{
		k14Mode = netmsginput.readByte();
		k14WingspanType = netmsginput.readByte();
		k14Distance = netmsginput.readFloat();
	}

	//Radar-ranging gunsite code
	public void typeFighterAceMakerRangeFinder() {
		if (k14Mode == 0) {
			return;
		}
		hunted = Main3D.cur3D().getViewPadlockEnemy();
		if (hunted == null) {
			hunted = War.GetNearestEnemyAircraft(FM.actor, 2000F, 9);
		}
		if (hunted != null) {
			k14Distance = (float) FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
			if (k14Distance > 1700F) {
				k14Distance = 1700F;
			} else if (k14Distance < 200F) {
				k14Distance = 200F;
			}
		}
	 //if(hunted instanceof TypeThreatDetector){
		 //HUD.log("TARGET PAINTED");
		 //((TypeThreatDetector) hunted).setCommonThreatActive();
	 //}
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
		}
	}

	public static void moveGear(HierMesh hiermesh, float f)
    {
       hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -125F), 0.0F);
       hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F));
       hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 90F));
       hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -90F));
       hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -90F));
       hiermesh.chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, 115F), 0.0F);
       hiermesh.chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, 115F), 0.0F);
       hiermesh.chunkSetAngles("GearL23_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.2F, 0.0F, -55F));
       hiermesh.chunkSetAngles("GearR23_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.2F, 0.0F, -55F));
       hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, 45.0F), 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -40.3F));
       hiermesh.chunkSetAngles("GearR3_D0", Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, 47.0F), 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -42.3F));
       hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, 145F));
       hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, -145F));
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.12F, 0.3F, 0.0F, 0.31F);
        hierMesh().chunkSetLocate("GearL21_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.12F, 0.3F, 0.0F, 0.31F);
        hierMesh().chunkSetLocate("GearR21_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveWheelSink()
    {	
        float f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.0F, 0.4F, 0.0F, 0.3F);
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 50.0F * f, 0.0F);
        resetYPRmodifier();
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.6F, 0.0F, 0.59F);
        xyz[1] = f;
        hierMesh().chunkSetLocate("GearL212_D0", xyz, ypr);
        resetYPRmodifier();
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.6F, 0.0F, 0.59F);
        xyz[1] = f;
        hierMesh().chunkSetLocate("GearR212_D0", xyz, ypr);
    }

	protected void moveRudder(float f)
	{
		hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
	}

	protected void moveElevator(float f)
	{
		if(f > 0F){
		hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -16.5F * ((FlightModelMain) (super.FM)).CT.getElevator(), 0.0F);
		hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -16.5F * ((FlightModelMain) (super.FM)).CT.getElevator(), 0.0F);
		}
		else if (f < 0F){
			hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -7F * ((FlightModelMain) (super.FM)).CT.getElevator(), 0.0F);
			hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -7F * ((FlightModelMain) (super.FM)).CT.getElevator(), 0.0F);
		}
	}

	protected void moveAileron(float f)
	{
		hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 20F * f, 0.0F);
		hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
	}

	protected void moveFlap(float f)
	{
		float f1 = -44.5F * f;
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	protected void moveAirBrake(float f)
	{
		hierMesh().chunkSetAngles("AirbrakeL", -35F * f, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("AirbrakeR", 35F * f, 0.0F, 0.0F);
		if(!pylonOccupied){
			hierMesh().chunkSetAngles("AirbrakeRear", 0.0F, 40F * f, 0.0F);
			hierMesh().chunkSetAngles("AirbrakeTelescope", 0.0F, -40F * f, 0.0F);
		}
	}

	protected void moveFan(float f)
	{
	}

	public void moveSteering(float f)
	{

		if(FM.CT.getGear() < 0.99)
			hierMesh().chunkSetAngles("GearC_D0", 0.0F, 0.0F, 0.0F*f);
		else
				hierMesh().chunkSetAngles("GearC_D0", 0.0F, 0.0F, 30F * f);
	} 
	
	public void moveShockCone(){
		float m = calculateMach();
		Aircraft.xyz[0] = Aircraft.cvt(m/1.9F, 0.0F, 1.0F, 0.0F, 1.2F);
		hierMesh().chunkSetLocate("Cone", Aircraft.xyz, Aircraft.ypr);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d)
	{
		if(s.startsWith("xx"))
		{
			if(s.startsWith("xxarmor"))
			{
				debuggunnery("Armor: Hit..");
				if(s.endsWith("p1"))
				{
					getEnergyPastArmor(13.350000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
					if(shot.power <= 0.0F)
						doRicochetBack(shot);
				} else
					if(s.endsWith("p2"))
						getEnergyPastArmor(8.77F, shot);
					else
						if(s.endsWith("g1"))
						{
							getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
							((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
							if(shot.power <= 0.0F)
								doRicochetBack(shot);
						}
				return;
			}
			if(s.startsWith("xxcontrols"))
			{
				debuggunnery("Controls: Hit..");
				int i = s.charAt(10) - 48;
				switch(i)
				{
				default:
					break;

				case 1: // '\001'
				case 2: // '\002'
				if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F)
				{
					debuggunnery("Controls: Ailerones Controls: Out..");
					((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
				}
				break;

				case 3: // '\003'
				case 4: // '\004'
					if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
					{
						debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
						((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
					}
					if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
					{
						debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
						((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
					}
					break;
				}
				return;
			}
			if(s.startsWith("xxeng1"))
			{
				debuggunnery("Engine Module: Hit..");
				if(s.endsWith("bloc"))
					getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
				if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 20F)
				{
					((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
					debuggunnery("Engine Module: Engine Cams Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Left..");
					if(World.Rnd().nextFloat() < shot.power / 24000F)
					{
						((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
						debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
					}
					if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
					{
						((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
						debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
					}
				}
				if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
				{
					((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
					debuggunnery("Engine Module: Hit - Engine Fires..");
				}
				if(s.endsWith("exht"));
				return;
			}
			if(s.startsWith("xxmgun0"))
			{
				Aircraft.debugprintln(this, "Armament: Gunpod Disabled..");
				((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
				((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
				return;
			}
			if(s.startsWith("xxtank"))
			{
				int k = s.charAt(6) - 49;
				if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
				{
					if(((FlightModelMain) (super.FM)).AS.astateTankStates[k] == 0)
					{
						debuggunnery("Fuel Tank (" + k + "): Pierced..");
						((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, k, 1);
						((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, k, 1);
					}
					if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
					{
						((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, k, 2);
						debuggunnery("Fuel Tank (" + k + "): Hit..");
					}
				}
				return;
			}
			if(s.startsWith("xxspar"))
			{
				debuggunnery("Spar Construction: Hit..");
				if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
				{
					debuggunnery("Spar Construction: WingLMid Spars Damaged..");
					nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
				{
					debuggunnery("Spar Construction: WingRMid Spars Damaged..");
					nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
				{
					debuggunnery("Spar Construction: WingLOut Spars Damaged..");
					nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
				{
					debuggunnery("Spar Construction: WingROut Spars Damaged..");
					nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				return;
			}
			if(s.startsWith("xxhyd"))
			{
				((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
				return;
			}
			if(s.startsWith("xxpnm"))
			{
				((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
				return;
			} else
			{
				return;
			}
		}
		if(s.startsWith("xcockpit"))
		{
			((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
			getEnergyPastArmor(0.05F, shot);
		}
		if(s.startsWith("xcf"))
			hitChunk("CF", shot);
		else
			if(s.startsWith("xnose"))
				hitChunk("Nose", shot);
			else
				if(s.startsWith("xtail"))
				{
					if(chunkDamageVisible("Tail1") < 3)
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
								if(s.startsWith("xvator"))
								{
									if(s.startsWith("xvatorl"))
										hitChunk("VatorL", shot);
									if(s.startsWith("xvatorr"))
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
									} else
										if(s.startsWith("xarone"))
										{
											if(s.startsWith("xaronel"))
												hitChunk("AroneL", shot);
											if(s.startsWith("xaroner"))
												hitChunk("AroneR", shot);
										} else
											if(s.startsWith("xflap"))
											{
												if(s.startsWith("xflap1"))
													hitChunk("Flap01", shot);
												if(s.startsWith("xflap2"))
													hitChunk("Flap02", shot);
												if(World.Rnd().nextFloat() < 0.4F)
													FM.CT.bHasFlapsControlRed = true;
											} else
											if(s.startsWith("xgear"))
											{
												if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
												{
													debuggunnery("Hydro System: Disabled..");
													((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
												}
												if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
												{
													debuggunnery("Undercarriage: Stuck..");
													((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
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

	protected boolean cutFM(int i, int j, Actor actor)
	{
		switch(i)
		{
		case 19: // '\023'
			((FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(actor);
			// fall through

		case 33: // '!'
		case 34: // '"'
		case 35: // '#'
		case 36: // '$'
		case 37: // '%'
		case 38: // '&'
			doCutBoosters();
			((FlightModelMain) (super.FM)).AS.setGliderBoostOff();
			bHasBoosters = false;
			// fall through
		}
		return super.cutFM(i, j, actor);
	}

	public void destroy()
	{
		doCutBoosters();
		super.destroy();
	}

	public void doFireBoosters()
	{
		Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 15F);
		Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 6F);
		Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 6F);
		Eff3DActor.New(this, findHook("_Booster1"), null, 0.25F, "3DO/Effects/Aircraft/TurboHWK109D.eff", 6F);
		Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 15F);
		Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 6F);
		Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 6F);
		Eff3DActor.New(this, findHook("_Booster2"), null, 0.25F, "3DO/Effects/Aircraft/TurboHWK109D.eff", 6F);
	}

	public void doCutBoosters()
	{
		for(int i = 0; i < 2; i++)
			if(booster[i] != null)
			{
				booster[i].start();
				booster[i] = null;
			}

	}

	public float getAirPressure(float theAltitude) {
		float fBase = 1F - ( L * theAltitude / T0);
		float fExponent = (G_CONST * M) / (R * L);
		return p0 * (float)Math.pow(fBase, fExponent);
	}

	public float getAirPressureFactor(float theAltitude) {
		return getAirPressure(theAltitude) / p0;
	}

	public float getAirDensity(float theAltitude) {
		return (getAirPressure(theAltitude) * M) / (R * (T0 - (L * theAltitude)));
	}

	public float getAirDensityFactor(float theAltitude) {
		return getAirDensity(theAltitude) / Rho0;
	}

	public float getMachForAlt(float theAltValue) {
		theAltValue /= 1000F; // get altitude in km
		int i=0;
		for (i=0; i<fMachAltX.length; i++) {
			if (fMachAltX[i] > theAltValue) break;
		}
		if (i == 0) return fMachAltY[0];
		float baseMach = fMachAltY[i-1];
		float spanMach = fMachAltY[i] - baseMach;
		float baseAlt = fMachAltX[i-1];
		float spanAlt = fMachAltX[i] - baseAlt;
		float spanMult = (theAltValue - baseAlt) / spanAlt;
		return baseMach + (spanMach * spanMult);
	}

	public float getMpsFromKmh(float kilometersPerHour) {
		return kilometersPerHour/3.6F;
	}

	public float calculateMach(){
		return (FM.getSpeedKMH()/getMachForAlt(FM.getAltitude()));
	}

	public void soundbarier() {

		float f = getMachForAlt(FM.getAltitude()) - FM.getSpeedKMH();
		if (f < 0.5F)
			f = 0.5F;
		float f_0_ = FM.getSpeedKMH() - getMachForAlt(FM.getAltitude());
		if (f_0_ < 0.5F)
			f_0_ = 0.5F;
		if (calculateMach() <= 1.0){
			FM.VmaxAllowed = FM.getSpeedKMH() + f;
			SonicBoom = 0.0F;
			isSonic = false;
		}
		if (calculateMach() >= 1.0){    			
			FM.VmaxAllowed = FM.getSpeedKMH() + f_0_;
			isSonic = true; 
		}
		if (FM.VmaxAllowed > 1500.0F)
			FM.VmaxAllowed = 1500.0F;

		if (isSonic && SonicBoom < 1){
			super.playSound("aircraft.SonicBoom", true);
			super.playSound("aircraft.SonicBoomInternal", true);
			if(FM.actor == World.getPlayerAircraft())
				HUD.log("Mach 1 Exceeded!");
			if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude())){
				shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
			}
			SonicBoom = 1;
		}
		if (calculateMach() > 1.01 || calculateMach() < 1.0)
			Eff3DActor.finish(shockwave);
	}

	public void engineSurge(float f){
		if(((FlightModelMain) (super.FM)).AS.isMaster())

			if(curthrl == -1F)
			{
				curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
			} else
			{
				curthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
				if (curthrl < 1.05F) 
				{
					if ((curthrl - oldthrl) / f > 20.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.40F)
					{
						if(FM.actor == World.getPlayerAircraft())
							HUD.log("Compressor Stall!");
						super.playSound("weapon.MGunMk108s", true);
						engineSurgeDamage += 1.0000000000000001E-002D * (double)(((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
						((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
						if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode())
						{
							FM.AS.hitEngine(this, 0, 100);
						}
						if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode())
						{
							FM.EI.engines[0].setEngineDies(this);
						}
					}
					if ((curthrl - oldthrl) / f < -20.0F && (curthrl - oldthrl) / f > -100.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6) 
					{
						super.playSound("weapon.MGunMk108s", true);
						engineSurgeDamage += 1.0000000000000001E-003D * (double)(((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
						((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
						if (World.Rnd().nextFloat() < 0.40F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode())
						{
							if(FM.actor == World.getPlayerAircraft())
								HUD.log("Engine Flameout!");
							FM.EI.engines[0].setEngineStops(this);
						}
						else
						{
							if(FM.actor == World.getPlayerAircraft())
								HUD.log("Compressor Stall!");
						}
					}
				}
				oldthrl = curthrl;
			}
	}

	private boolean sirenaWarning()
	{
		Point3d point3d = new Point3d();
		pos.getAbs(point3d);
		Vector3d vector3d = new Vector3d();
		Aircraft aircraft = World.getPlayerAircraft();
		double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x;
		double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y;
		double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().z;
		int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
		if(i < 0)
			i = 360 + i;
		int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
		if(j < 0)
			j = 360 + j;
		Actor actor = War.getNearestEnemy(this, 4000F);
		if((actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && ((actor instanceof TypeFighterAceMaker) && (actor instanceof TypeSupersonic || actor instanceof TypeFastJet))&& actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
		{
			pos.getAbs(point3d);
			double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
			double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
			double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
			new String();
			new String();
			double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
			new String();
			double d7 = d3 - d;
			double d8 = d4 - d1;
			float f = 57.32484F * (float)Math.atan2(d8, -d7);
			int i1 = (int)(Math.floor((int)f) - 90D);
			if(i1 < 0)
				i1 = 360 + i1;
			int j1 = i1 - i;
			double d9 = d - d3;
			double d10 = d1 - d4;
			double d11 = Math.sqrt(d6 * d6);
			int k1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
			float f1 = 57.32484F * (float)Math.atan2(k1, d11);
			int l1 = (int)(Math.floor((int)f1) - 90D);
			if(l1 < 0)
				l1 = 360 + l1;
			int i2 = l1 - j;
			int j2 = (int)(Math.ceil(((double)k1 * 3.2808399000000001D) / 100D) * 100D);
			if(j2 >= 5280)
			{
				j2 = (int)Math.floor(j2 / 5280);
			}
			bRadarWarning = (double)k1 <= 3000D && (double)k1 >= 50D && i2 >= 195 && i2 <= 345 && Math.sqrt(j1 * j1) >= 120D;
			playSirenaWarning(bRadarWarning);
		}
		else
		{
			bRadarWarning = false;
			playSirenaWarning(bRadarWarning);
		}
		return true;
	}

	public void playSirenaWarning(boolean isThreatened)
	{
		if (isThreatened && !sirenaSoundPlaying) {
			fxSirena.play(smplSirena);
			sirenaSoundPlaying = true;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
		}
		else if(!isThreatened && sirenaSoundPlaying){
			fxSirena.cancel();
			sirenaSoundPlaying = false;
		}
	}

	public void onAircraftLoaded(){
		super.onAircraftLoaded();
	    FM.AS.wantBeaconsNet(true);
		if((getBulletEmitterByHookName("_ExternalDev01") instanceof com.maddox.il2.objects.weapons.PylonGP9) || (getBulletEmitterByHookName("_ExternalDev02") instanceof com.maddox.il2.objects.weapons.MiG21Pylon)){
			pylonOccupied = true;
			FM.Sq.dragAirbrakeCx = FM.Sq.dragAirbrakeCx/2;
		}    		
		if(super.thisWeaponsName.endsWith("RATO"))
		{
			for(int i = 0; i < 2; i++)
				try
			{
					booster[i] = new BombSPRD99();
					((Actor) (booster[i])).pos.setBase(this, findHook("_BoosterH" + (i + 1)), false);
					((Actor) (booster[i])).pos.resetAsBase();
					booster[i].drawing(true);
			}
			catch(Exception exception)
			{
				debugprintln("Structure corrupt - can't hang SPRD-99..");
			}

		}
	}
	
    private float flapsMovement(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = (float)Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if(f6 < f1)
        {
            f6 += f4 * f;
            if(f6 > f1)
                f6 = f1;
        } else
        if(f6 > f1)
        {
            f6 -= f4 * f;
            if(f6 < f1)
                f6 = f1;
        }
        return f6;
    }
    
	public void update(float f)
	{
		//Controls flap behaviour
		if(super.FM.getSpeedKMH() > 700F && ((FlightModelMain) (super.FM)).CT.bHasFlapsControl)
		{
			((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
			((FlightModelMain) (super.FM)).CT.bHasFlapsControl = false;
		} else
		{
			((FlightModelMain) (super.FM)).CT.bHasFlapsControl = true;
		}
		if(FM.CT.getFlap() < FM.CT.FlapsControl)
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, cvt(FM.getSpeedKMH(), 0F, 700F, 0.5F, 0.08F)));
		else
			FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, cvt(FM.getSpeedKMH(), 0F, 700F, 0.5F, 0.7F)));
		
		//Controls exhaust flap movement
		for(int i = 1; i < 19; i++)
			hierMesh().chunkSetAngles("EngineExhaustFlap" + i, 0.0F, -38F * ((FlightModelMain) (super.FM)).CT.getPowerControl(), 0.0F);
		//Controls position of exhaust flame
		resetYPRmodifier();
		float f1 = ((FlightModelMain) (super.FM)).CT.getPowerControl() * 1.5F;
			Aircraft.xyz[0] = Aircraft.cvt(f1, 0.0F, 1.5F, 0.0F, 1.5F);
				hierMesh().chunkSetLocate("EffectBox", Aircraft.xyz, Aircraft.ypr);
		//Controls shock cone movement
		resetYPRmodifier();
		Aircraft.xyz[0] = Aircraft.cvt(calculateMach()/1.9F, 0.0F, 1F, 0.0F, 0.15F);
			hierMesh().chunkSetLocate("Cone", Aircraft.xyz, Aircraft.ypr);
		//Speed-induced gear retraction limits
			float f2 = super.FM.getSpeedKMH() - 700F;
			if(f2 < 0.0F)
				f2 = 0.0F;
			((FlightModelMain) (super.FM)).CT.dvGear = 0.2F - f2 / 700F;
			if(((FlightModelMain) (super.FM)).CT.dvGear < 0.0F)
				((FlightModelMain) (super.FM)).CT.dvGear = 0.0F;
		//RWR code
		try{
			sirenaWarning();
		}
		catch(NullPointerException exception){}
		engineSurge(f);
		typeFighterAceMakerRangeFinder();
		soundbarier(); //checks conditions for breaking sound barrier, also implements effects on flight controls
		//TODO: Not needed? speedcontrol(f);
		if (FM.AS.isMaster() && Config.isUSE_RENDER()) {
			if (FM.EI.engines[0].getThrustOutput() > 0.5F && FM.EI.engines[0].getStage() == 6) {
				if (FM.EI.engines[0].getThrustOutput() > 0.50F) {
					if (FM.EI.engines[0].getThrustOutput() > 1.001F) {
						FM.AS.setSootState(this, 0, 5);
					} else {
						FM.AS.setSootState(this, 0, 3);
					}
				}
			} else {
				FM.AS.setSootState(this, 0, 0);
			}
		}
		if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
		{
			if(((FlightModelMain) (super.FM)).AP.way.isLanding() && ((FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() > 40F){
				((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
				if(FM.CT.bHasDragChuteControl)
				((FlightModelMain) (super.FM)).CT.DragChuteControl = 1.0F;	
			}
			if(((FlightModelMain) (super.FM)).AP.way.isLanding() && ((FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() < 40F){
				((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
				if(super.FM.getSpeed() < 20F)
					((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;	
			}
		}
		if ((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete
				&& FM.getSpeedKMH() > 15.0F) {
			overrideBailout = true;
			FM.AS.bIsAboutToBailout = false;
			bailout();
		}
		if((super.FM instanceof Pilot) && bHasBoosters && super.thisWeaponsName.endsWith("RATO"))
		{
			if(super.FM.getAltitude() > 300F && boosterFireOutTime == -1L && ((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z != 0.0D && World.Rnd().nextFloat() < 0.05F)
			{
				doCutBoosters();
				((FlightModelMain) (super.FM)).AS.setGliderBoostOff();
				bHasBoosters = false;
			}
			if(bHasBoosters && boosterFireOutTime == -1L && ((FlightModelMain) (super.FM)).Gears.onGround() && ((FlightModelMain) (super.FM)).EI.getPowerOutput() > 0.8F && super.FM.getSpeedKMH() > 20F)
			{
				boosterFireOutTime = Time.current() + 6000L;
				doFireBoosters();
				((FlightModelMain) (super.FM)).AS.setGliderBoostOn();
			}
			if(bHasBoosters && boosterFireOutTime > 0L)
			{
				if(Time.current() < boosterFireOutTime)
					FM.producedAF.x += 20000D;
				if(Time.current() > boosterFireOutTime + 20000L)
				{
					doCutBoosters();
					((FlightModelMain) (super.FM)).AS.setGliderBoostOff();
					bHasBoosters = false;
				}
			}
		}
		if(FM.AS.bNavLightsOn && !(FM.CT.getGear() < 1.0F))
			bGearLightsOn = true;
		else
			bGearLightsOn = false;
		if(bGearLightState != bGearLightsOn)
			doSetGearLightsState(bGearLightsOn);
		super.update(f);
	}

	private void doSetGearLightsState(boolean flag){
		//Gear Lights
		for(int i = 0; i < gearLightsEffects.length; i++){
			if(gearLightsEffects[i] != null){
				Eff3DActor.finish(gearLightsEffects[i]);
				gearLightsLights[i].light.setEmit(0.0F, 0.0F);
			}
			gearLightsEffects[i] = null;
		}
		
		if(flag){
			gearLightsEffects[0] = Eff3DActor.New(this, findHook("_GearLightC"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
			gearLightsEffects[1] = Eff3DActor.New(this, findHook("_GearLightL"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
			gearLightsEffects[2] = Eff3DActor.New(this, findHook("_GearLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
			
			for(int j = 0; j < gearLightsEffects.length; j++)
			{
			Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			Loc loc1 = new Loc();
			loc1.set(1.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			if(j == 0)
			findHook("_GearLightC").computePos(this, loc, loc1);
			if(j == 1)
			findHook("_GearLightL").computePos(this, loc, loc1);
			if(j == 2)
			findHook("_GearLightR").computePos(this, loc, loc1);
			Point3d point3d = loc1.getPoint();
			gearLightsLights[j] = new LightPointActor(new LightPoint(), point3d);
			gearLightsLights[j].light.setColor(0.5F, 0.6F, 0.7F);
			gearLightsLights[j].light.setEmit(1.0F, 8F);
			draw.lightMap().put("_GearLightC", gearLightsLights[0]);
			draw.lightMap().put("_GearLightL", gearLightsLights[1]);
			draw.lightMap().put("_GearLightR", gearLightsLights[2]);
			}
			bGearLightState = true;
		}
		else
			bGearLightState = flag;
	}
	public void doSetSootState(int i, int j)
	{
		for(int k = 0; k < 2; k++)
		{
			if(((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] != null)
				Eff3DActor.finish(((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k]);
			((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] = null;
		}

		switch(j)
		{
		case 1: // '\001'
		case 2: // '\002'
		case 3: // '\003'
			((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("HolyGrail02"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
			break;

		case 5: // '\005'
			((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("HolyGrail02"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
			((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("HolyGrail01"), null, 2.5F, "3DO/Effects/Aircraft/afterburner.eff", -1F);
			// fall through

		case 4: // '\004'
		default:
			return;
		}
	}

	public void doEjectCatapult() {
		new MsgAction(false, this)  {

			public void doAction(Object paramObject) {
				Aircraft localAircraft = (Aircraft) paramObject;
				if (Actor.isValid(localAircraft)) {
					Loc localLoc1 = new Loc();
					Loc localLoc2 = new Loc();
					Vector3d localVector3d = new Vector3d(0.0, 0.0, 20.0);
					HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat01");
					localAircraft.pos.getAbs(localLoc2);
					localHookNamed.computePos(localAircraft, localLoc2,
							localLoc1);
					localLoc1.transform(localVector3d);
					localVector3d.x += localAircraft.FM.Vwld.x;
					localVector3d.y += localAircraft.FM.Vwld.y;
					localVector3d.z += localAircraft.FM.Vwld.z;
					new EjectionSeat(9, localLoc1, localVector3d,
							localAircraft);
				}
			}
		};
		this.hierMesh().chunkVisible("Seat_D0", false);
		FM.setTakenMortalDamage(true, null);
		FM.CT.WeaponControl[0] = false;
		FM.CT.WeaponControl[1] = false;
		FM.CT.bHasAileronControl = false;
		FM.CT.bHasRudderControl = false;
		FM.CT.bHasElevatorControl = false;
	}

	private void bailout() {
		if (overrideBailout) {
			if (FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2) {
				if (FM.CT.cockpitDoorControl > 0.5F
						&& FM.CT.getCockpitDoor() > 0.5F) {
					FM.AS.astateBailoutStep = (byte) 11;
					doRemoveBlisters();
				} else {
					FM.AS.astateBailoutStep = (byte) 2;
				}
			} else if (FM.AS.astateBailoutStep >= 2
					&& FM.AS.astateBailoutStep <= 3) {
				switch (FM.AS.astateBailoutStep) {
				case 2:
					if (FM.CT.cockpitDoorControl < 0.5F) {
						doRemoveBlister1();
					}
					break;
				case 3:
					doRemoveBlisters();
					break;
				}
				if (FM.AS.isMaster()) {
					FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
				}
				AircraftState tmp178_177 = FM.AS;
				tmp178_177.astateBailoutStep = (byte) (tmp178_177.astateBailoutStep + 1);
				if (FM.AS.astateBailoutStep == 4) {
					FM.AS.astateBailoutStep = (byte) 11;
				}
			} else if (FM.AS.astateBailoutStep >= 11
					&& FM.AS.astateBailoutStep <= 19) {
				int i = FM.AS.astateBailoutStep;
				if (FM.AS.isMaster()) {
					FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
				}
				AircraftState tmp383_382 = FM.AS;
				tmp383_382.astateBailoutStep = (byte) (tmp383_382.astateBailoutStep + 1);
				if (i == 11) {
					FM.setTakenMortalDamage(true, null);
					if (FM instanceof Maneuver
							&& ((Maneuver) FM).get_maneuver() != 44) {
						World.cur();
						if (FM.AS.actor != World.getPlayerAircraft()) {
							((Maneuver) FM).set_maneuver(44);
						}
					}
				}
				if (FM.AS.astatePilotStates[i - 11] < 99) {
					this.doRemoveBodyFromPlane(i - 10);
					if (i == 11) {
						doEjectCatapult();
						FM.setTakenMortalDamage(true, null);
						FM.CT.WeaponControl[0] = false;
						FM.CT.WeaponControl[1] = false;
						FM.AS.astateBailoutStep = (byte) -1;
						overrideBailout = false;
						FM.AS.bIsAboutToBailout = true;
						ejectComplete = true;
					}
				}
			}
		}
	}

	private final void doRemoveBlister1() {
		if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1
				&& FM.AS.getPilotHealth(0) > 0.0F) {
			this.hierMesh().hideSubTrees("Blister1_D0");
			if(!bHasSK1Seat){
			Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
			localWreckage.collide(false);
			Vector3d localVector3d = new Vector3d();
			localVector3d.set(FM.Vwld);
			localWreckage.setSpeed(localVector3d);
			}
		}
	}

	private final void doRemoveBlisters() {
		for (int i = 2; i < 10; i++) {
			if (this.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1
					&& FM.AS.getPilotHealth(i - 1) > 0.0F) {
				this.hierMesh().hideSubTrees("Blister" + i + "_D0");
				if(!bHasSK1Seat){
				Wreckage localWreckage = new Wreckage(this,
						this.hierMesh().chunkFind("Blister" + i + "_D0"));
				localWreckage.collide(false);
				Vector3d localVector3d = new Vector3d();
				localVector3d.set(FM.Vwld);
				localWreckage.setSpeed(localVector3d);
				}
			}
		}
	}

	public int k14Mode;
	public int k14WingspanType;
	public float k14Distance;

	static 
	{
		Class class1 = com.maddox.il2.objects.air.MIG_21.class;
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
	}
}