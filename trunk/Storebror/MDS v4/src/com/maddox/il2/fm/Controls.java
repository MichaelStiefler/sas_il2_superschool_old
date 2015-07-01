/*4.10.1 class made compatible with clean UP. Contains BombBayDoor code. + TAK brake controls*/
package com.maddox.il2.fm;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.game.ZutiSupportMethods_Multicrew;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.SU_26M2;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.il2.objects.weapons.RocketGun4andHalfInch;
import com.maddox.rts.Time;

public class Controls
{
	public float				Sensitivity;
	public float				PowerControl;
	public boolean				afterburnerControl;
	public float				GearControl;
	public float				wingControl;
	public float				cockpitDoorControl;
	public float				arrestorControl;
	public float				FlapsControl;
	public float				AileronControl;
	public float				ElevatorControl;
	public float				RudderControl;
	public float				BrakeControl;
	private float				StepControl;
	private boolean				bStepControlAuto;
	private float				MixControl;
	private int					MagnetoControl;
	private int					CompressorControl;
	public float				BayDoorControl;
	public float				AirBrakeControl;
	private float				trimAileronControl;
	private float				trimElevatorControl;
	private float				trimRudderControl;
	public float				trimAileron;
	public float				trimElevator;
	public float				trimRudder;
	private float				RadiatorControl;
	private boolean				bRadiatorControlAuto;
	public boolean				StabilizerControl;
	public boolean[]			WeaponControl;
	public boolean[]			saveWeaponControl;
	public boolean				bHasGearControl;
	public boolean				bHasWingControl;
	public boolean				bHasCockpitDoorControl;
	public boolean				bHasArrestorControl;
	public boolean				bHasFlapsControl;
	public boolean				bHasFlapsControlRed;
	public boolean				bHasAileronControl;
	public boolean				bHasElevatorControl;
	public boolean				bHasRudderControl;
	public boolean				bHasBrakeControl;
	public boolean				bHasAirBrakeControl;
	public boolean				bHasLockGearControl;
	public boolean				bHasAileronTrim;
	public boolean				bHasRudderTrim;
	public boolean				bHasElevatorTrim;
	public BulletEmitter[][]	Weapons;
	public byte					CTL;
	public byte					WCT;
	public int					TWCT;
	private float				Power;
	private float				Gear;
	private float				wing;
	private float				cockpitDoor;
	private float				arrestor;
	private float				Flaps;
	private float				Ailerons;
	private float				Elevators;
	private float				Rudder;
	private float				Brake;
	private float				Step;
	private float				radiator;
	private float				airBrake;
	private float				Ev;
	private int					tick;
	public float				AilThr;
	public float				AilThr3;
	public float				RudThr;
	public float				RudThr2;
	public float				ElevThr;
	public float				ElevThr2;
	public float				dvGear;
	public float				dvWing;
	public float				dvCockpitDoor;
	public float				dvAirbrake;
	private FlightModelMain		FM;
	private static float		tmpF;
	private static Vector3d		tmpV3d				= new Vector3d();
	public boolean				bHasBayDoors		= false;
	private float				fSaveCockpitDoor;
	private float				fSaveCockpitDoorControl;
	private float				fSaveSideDoor;
	private float				fSaveSideDoorControl;
	public boolean				bMoveSideDoor		= false;
	// private int COCKPIT_DOOR = 1;
	private int					SIDE_DOOR			= 2;

	public int					electricPropUp;
	public int					electricPropDn;
	public boolean				bUseElectricProp;
	public float				PowerControlArr[];
	public float				StepControlArr[];

	// TODO: Modified by |ZUTI|: changed from private to public
	// --------------------------------------------------------
	public int					LEFT				= 1;
	public int					RIGHT				= 2;
	public int					iToggleRocketSide	= LEFT;
	public int					iToggleBombSide		= LEFT;
	public long					lWeaponTime			= System.currentTimeMillis();
	public boolean				bIsMustang			= false;

	// --------------------------------------------------------

	public Controls(FlightModelMain flightmodelmain)
	{
		Sensitivity = 1.0F;
		PowerControl = 0.0F;
		GearControl = 0.0F;
		wingControl = 0.0F;
		cockpitDoorControl = 0.0F;
		arrestorControl = 0.5F;
		FlapsControl = 0.0F;
		AileronControl = 0.0F;
		ElevatorControl = 0.0F;
		RudderControl = 0.0F;
		BrakeControl = 0.0F;
		StepControl = 1.0F;
		bStepControlAuto = true;
		MixControl = 1.0F;
		MagnetoControl = 3;
		CompressorControl = 0;
		BayDoorControl = 0.0F;
		AirBrakeControl = 0.0F;
		trimAileronControl = 0.0F;
		trimElevatorControl = 0.0F;
		trimRudderControl = 0.0F;
		trimAileron = 0.0F;
		trimElevator = 0.0F;
		trimRudder = 0.0F;
		RadiatorControl = 0.0F;
		bRadiatorControlAuto = true;
		StabilizerControl = false;
		WeaponControl = new boolean[21];
		saveWeaponControl = new boolean[4];
		bHasGearControl = true;
		bHasWingControl = false;
		bHasCockpitDoorControl = false;
		bHasArrestorControl = false;
		bHasFlapsControl = true;
		bHasFlapsControlRed = false;
		bHasAileronControl = true;
		bHasElevatorControl = true;
		bHasRudderControl = true;
		bHasBrakeControl = true;
		bHasAirBrakeControl = true;
		bHasLockGearControl = true;
		bHasAileronTrim = true;
		bHasRudderTrim = true;
		bHasElevatorTrim = true;
		Weapons = new BulletEmitter[21][];
		Step = 1.0F;
		AilThr = 100.0F;
		AilThr3 = 1000000.0F;
		RudThr = 100.0F;
		RudThr2 = 10000.0F;
		ElevThr = 112.0F;
		ElevThr2 = 12544.0F;
		dvGear = 0.2F;
		dvWing = 0.1F;
		dvCockpitDoor = 0.1F;
		dvAirbrake = 0.5F;
		electricPropDn = 0;
		PowerControlArr = new float[6];
		StepControlArr = new float[6];
		FM = flightmodelmain;
		for (int i = 0; i < 6; i++)
			PowerControlArr[i] = 0.0F;
		for (int j = 0; j < 6; j++)
			StepControlArr[j] = 1.0F;
	}

	public void set(Controls controls_0_)
	{
		PowerControl = controls_0_.PowerControl;
		GearControl = controls_0_.GearControl;
		arrestorControl = controls_0_.arrestorControl;
		FlapsControl = controls_0_.FlapsControl;
		AileronControl = controls_0_.AileronControl;
		ElevatorControl = controls_0_.ElevatorControl;
		RudderControl = controls_0_.RudderControl;
		BrakeControl = controls_0_.BrakeControl;
		BayDoorControl = controls_0_.BayDoorControl;
		AirBrakeControl = controls_0_.AirBrakeControl;
		dvGear = controls_0_.dvGear;
		dvWing = controls_0_.dvWing;
		dvCockpitDoor = controls_0_.dvCockpitDoor;
		dvAirbrake = controls_0_.dvAirbrake;
	}

	public void CalcTresholds()
	{
		AilThr3 = AilThr * AilThr * AilThr;
		RudThr2 = RudThr * RudThr;
		ElevThr2 = ElevThr * ElevThr;
	}

	public void setLanded()
	{
		if (bHasGearControl)
			GearControl = Gear = 1.0F;
		else
			Gear = 1.0F;
		FlapsControl = Flaps = 0.0F;
		StepControl = 1.0F;
		bStepControlAuto = true;
		bRadiatorControlAuto = true;
		BayDoorControl = 0.0F;
		AirBrakeControl = 0.0F;
	}

	public void setFixedGear(boolean bool)
	{
		if (bool)
		{
			Gear = 1.0F;
			GearControl = 0.0F;
		}
	}

	public void setGearAirborne()
	{
		if (bHasGearControl)
			GearControl = Gear = 0.0F;
	}

	public void setGear(float f)
	{
		Gear = f;
	}

	public void setGearBraking()
	{
		Brake = 1.0F;
	}

	public void forceFlaps(float f)
	{
		Flaps = f;
	}

	public void forceGear(float f)
	{
		if (bHasGearControl)
			Gear = GearControl = f;
		else
			setFixedGear(true);
	}

	public void forceWing(float f)
	{
		wing = f;
		FM.doRequestFMSFX(1, (int) (100.0F * f));
		((Aircraft) FM.actor).moveWingFold(f);
	}

	public void forceArrestor(float f)
	{
		arrestor = f;
		((Aircraft) FM.actor).moveArrestorHook(f);
	}

	public void setPowerControl(float f)
	{
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.1F)
			f = 1.1F;
		PowerControl = f;
		for (int i = 0; i < 6; i++)
			if (i < FM.EI.getNum() && FM.EI.bCurControl[i])
				PowerControlArr[i] = f;

	}

	public void setPowerControl(float f, int i)
	{
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.1F)
			f = 1.1F;
		PowerControlArr[i] = f;
		if (i == 0)
			PowerControl = f;
	}

	public float getPowerControl()
	{
		return PowerControl;
	}

	public void setStepControl(float f)
	{
		if (!bUseElectricProp)
		{
			if (f > 1.0F)

				f = 1.0F;
			if (f < 0.0F)
				f = 0.0F;
			StepControl = f;
			for (int i = 0; i < 6; i++)
				if (i < FM.EI.getNum() && FM.EI.bCurControl[i])
					StepControlArr[i] = f;

			com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogPowerId, "PropPitch", new java.lang.Object[] { new Integer(java.lang.Math.round(getStepControl() * 100F)) });
		}
	}

	public void setStepControl(float f, int i)
	{
		if (!bUseElectricProp)
		{
			if (f > 1.0F)
				f = 1.0F;
			if (f < 0.0F)
				f = 0.0F;
			StepControlArr[i] = f;
			if (!getStepControlAuto(i))
				com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogPowerId, "PropPitch", new java.lang.Object[] { new Integer(java.lang.Math.round(getStepControl(i) * 100F)) });
		}
	}

	public boolean getStepControlAuto(int i)
	{
		if (i < FM.EI.getNum())
			return !FM.EI.engines[i].isHasControlProp() || FM.EI.engines[i].getControlPropAuto();
		else
			return true;
	}

	public float getStepControl(int i)
	{
		return StepControlArr[i];
	}

	public void setElectricPropUp(boolean flag)
	{
		if (bUseElectricProp)
			if (flag)
				electricPropUp = 1;
			else
				electricPropUp = 0;
	}

	public void setElectricPropDn(boolean flag)
	{
		if (bUseElectricProp)
			if (flag)
				electricPropDn = 1;
			else
				electricPropDn = 0;
	}

	public void setStepControlAuto(boolean bool)
	{
		bStepControlAuto = bool;
	}

	public float getStepControl()
	{
		return StepControl;
	}

	public boolean getStepControlAuto()
	{
		return bStepControlAuto;
	}

	public void setRadiatorControl(float f)
	{
		if (f > 1.0F)
			f = 1.0F;
		if (f < 0.0F)
			f = 0.0F;
		RadiatorControl = f;
	}

	public void setRadiatorControlAuto(boolean bool, EnginesInterface enginesinterface)
	{
		bRadiatorControlAuto = bool;
		if (enginesinterface.getFirstSelected() != null)
			radiator = enginesinterface.getFirstSelected().getControlRadiator();
	}

	public float getRadiatorControl()
	{
		return RadiatorControl;
	}

	public boolean getRadiatorControlAuto()
	{
		return bRadiatorControlAuto;
	}

	public float getRadiator()
	{
		return radiator;
	}

	public void setMixControl(float f)
	{
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.2F)
			f = 1.2F;
		MixControl = f;
	}

	public float getMixControl()
	{
		return MixControl;
	}

	public void setAfterburnerControl(boolean bool)
	{
		afterburnerControl = bool;
	}

	public boolean getAfterburnerControl()
	{
		return afterburnerControl;
	}

	public void setMagnetoControl(int i)
	{
		if (i < 0)
			i = 0;
		if (i > 3)
			i = 3;
		MagnetoControl = i;
	}

	public int getMagnetoControl()
	{
		return MagnetoControl;
	}

	public void setCompressorControl(int i)
	{
		if (i < 0)
			i = 0;
		if (i > FM.EI.engines[0].compressorMaxStep)
			i = FM.EI.engines[0].compressorMaxStep;
		CompressorControl = i;
	}

	public int getCompressorControl()
	{
		return CompressorControl;
	}

	public void setTrimAileronControl(float f)
	{
		trimAileronControl = f;
	}

	public float getTrimAileronControl()
	{
		return trimAileronControl;
	}

	public void setTrimElevatorControl(float f)
	{
		trimElevatorControl = f;
	}

	public float getTrimElevatorControl()
	{
		return trimElevatorControl;
	}

	public void setTrimRudderControl(float f)
	{
		trimRudderControl = f;
	}

	public float getTrimRudderControl()
	{
		return trimRudderControl;
	}

	public void interpolate(Controls controls_1_, float f)
	{
		PowerControl = FMMath.interpolate(PowerControl, controls_1_.PowerControl, f);
		FlapsControl = FMMath.interpolate(FlapsControl, controls_1_.FlapsControl, f);
		AileronControl = FMMath.interpolate(AileronControl, controls_1_.AileronControl, f);
		ElevatorControl = FMMath.interpolate(ElevatorControl, controls_1_.ElevatorControl, f);
		RudderControl = FMMath.interpolate(RudderControl, controls_1_.RudderControl, f);
		BrakeControl = FMMath.interpolate(BrakeControl, controls_1_.BrakeControl, f);

		// TAK++
		BrakeRightControl = FMMath.interpolate(BrakeRightControl, controls_1_.BrakeRightControl, f);
		BrakeLeftControl = FMMath.interpolate(BrakeLeftControl, controls_1_.BrakeLeftControl, f);
		// TAK--
	}

	public float getGear()
	{
		return Gear;
	}

	public float getWing()
	{
		return wing;
	}

	public float getCockpitDoor()
	{
		return cockpitDoor;
	}

	public void forceCockpitDoor(float f)
	{
		cockpitDoor = f;
	}

	public float getArrestor()
	{
		return arrestor;
	}

	public float getFlap()
	{
		return Flaps;
	}

	public float getAileron()
	{
		return Ailerons;
	}

	public float getElevator()
	{
		return Elevators;
	}

	public float getRudder()
	{
		return Rudder;
	}

	public float getBrake()
	{
		return Brake;
	}

	public float getPower()
	{
		return Power;
	}

	public float getStep()
	{
		return Step;
	}

	public float getBayDoor()
	{
		return BayDoorControl;
	}

	public float getAirBrake()
	{
		return airBrake;
	}

	private float filter(float f, float f_2_, float f_3_, float f_4_, float f_5_)
	{
		float f_6_ = (float) Math.exp((double) (-f / f_4_));
		float f_7_ = f_2_ + (f_3_ - f_2_) * f_6_;
		if (f_7_ < f_2_)
		{
			f_7_ += f_5_ * f;
			if (f_7_ > f_2_)
				f_7_ = f_2_;
		}
		else if (f_7_ > f_2_)
		{
			f_7_ -= f_5_ * f;
			if (f_7_ < f_2_)
				f_7_ = f_2_;
		}
		return f_7_;
	}

	private float clamp01(float f)
	{
		if (f < 0.0F)
			f = 0.0F;
		else if (f > 1.0F)
			f = 1.0F;
		return f;
	}

	private float clamp0115(float f)
	{
		if (f < 0.0F)
			f = 0.0F;
		else if (f > 1.1F)
			f = 1.1F;
		return f;
	}

	private float clamp11(float f)
	{
		if (f < -1.0F)
			f = -1.0F;
		else if (f > 1.0F)
			f = 1.0F;
		return f;
	}

	private float clampA(float f, float f_8_)
	{
		if (f < -f_8_)
			f = -f_8_;
		else if (f > f_8_)
			f = f_8_;
		return f;
	}

	public void setActiveDoor(int i)
	{
		if (i != SIDE_DOOR && bMoveSideDoor)
		{
			fSaveSideDoor = cockpitDoor;
			fSaveSideDoorControl = cockpitDoorControl;
			cockpitDoor = fSaveCockpitDoor;
			cockpitDoorControl = fSaveCockpitDoorControl;
			bMoveSideDoor = false;
		}
		else if (i == SIDE_DOOR && !bMoveSideDoor)
		{
			fSaveCockpitDoor = cockpitDoor;
			fSaveCockpitDoorControl = cockpitDoorControl;
			cockpitDoor = fSaveSideDoor;
			cockpitDoorControl = fSaveSideDoorControl;
			bMoveSideDoor = true;
		}
	}

	public void update(float f, float f_9_, EnginesInterface enginesinterface, boolean bool)
	{
		update(f, f_9_, enginesinterface, bool, false);
	}

	public void update(float f, float f_10_, EnginesInterface enginesinterface, boolean bool, boolean bool_11_)
	{
		float f_12_ = 1.0F;
		float f_13_ = 1.0F;
		float f_14_ = 1.0F;
		float f_15_ = f_10_ * f_10_;
		if (f_10_ > AilThr)
			f_12_ = Math.max(0.2F, AilThr3 / (f_15_ * f_10_));
		if (f_15_ > RudThr2)
			f_13_ = Math.max(0.2F, RudThr2 / f_15_);
		if (f_15_ > ElevThr2)
			f_14_ = Math.max(0.4F, ElevThr2 / f_15_);
		f_12_ *= Sensitivity;
		f_13_ *= Sensitivity;
		f_14_ *= Sensitivity;
		if (Elevators >= 0.0F && !(FM.actor instanceof SU_26M2))
			f_14_ = f_13_;
		if (!bool_11_)
		{
			if (FM instanceof com.maddox.il2.fm.RealFlightModel)
			{
				float f6 = 0.0F;
				for (int j1 = 0; j1 < enginesinterface.getNum(); j1++)
				{
					PowerControlArr[j1] = clamp0115(PowerControlArr[j1]);
					enginesinterface.engines[j1].setControlThrottle(PowerControlArr[j1]);
					if (PowerControlArr[j1] > f6)
						f6 = PowerControlArr[j1];
				}

				if (bool)
				{
					Power = f6;
				}
				else
				{
					Power = filter(f, f6, Power, 5F, 0.01F * f);
					enginesinterface.setThrottle(Power);
				}
			}
			else
			{
				PowerControl = clamp0115(PowerControl);
				if (bool)
					Power = PowerControl;

				else
					Power = filter(f, PowerControl, Power, 5F, 0.01F * f);
				enginesinterface.setThrottle(Power);

			}
		}
		if (!bool_11_)
			enginesinterface.setAfterburnerControl(afterburnerControl);
		if (!bool_11_)
		{
			StepControl = clamp01(StepControl);
			if (bUseElectricProp && (FM instanceof com.maddox.il2.fm.RealFlightModel))
			{
				enginesinterface.setPropAuto(bStepControlAuto);
				int i = electricPropUp - electricPropDn;
				if (i < 0)
					com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogPowerId, "elPropDn");
				else if (i > 0)
					com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogPowerId, "elPropUp");
				enginesinterface.setPropDelta(i);
			}
			if (bStepControlAuto && enginesinterface.getFirstSelected() != null)
			{
				if (enginesinterface.isSelectionAllowsAutoProp())
					enginesinterface.setPropAuto(true);
				else
				{
					enginesinterface.setPropAuto(false);
					bStepControlAuto = false;
				}
			}
			else if (FM instanceof com.maddox.il2.fm.RealFlightModel)
			{
				if (!bUseElectricProp)
				{
					for (int j = 0; j < enginesinterface.getNum(); j++)
					{
						StepControlArr[j] = clamp01(StepControlArr[j]);
						enginesinterface.engines[j].setControlPropAuto(false);
						enginesinterface.engines[j].setControlProp(StepControlArr[j]);
					}

				}
			}
			else
			{
				Step = filter(f, StepControl, Step, 0.2F, 0.02F);
				enginesinterface.setPropAuto(false);
				enginesinterface.setProp(Step);
			}
		}
		RadiatorControl = clamp01(RadiatorControl);
		radiator = filter(f, RadiatorControl, radiator, 999.9F, 0.2F);
		if (bRadiatorControlAuto && enginesinterface.getFirstSelected() != null)
		{
			if (enginesinterface.isSelectionAllowsAutoRadiator())
				enginesinterface.updateRadiator(f);
			else
			{
				enginesinterface.setRadiator(radiator);
				bRadiatorControlAuto = false;
			}
		}
		else
			enginesinterface.setRadiator(radiator);
		if (!bool_11_)
			enginesinterface.setMagnetos(MagnetoControl);
		if (!bool_11_ && bool)
			enginesinterface.setCompressorStep(CompressorControl);
		if (!bool_11_)
			enginesinterface.setMix(MixControl);
		if (bHasGearControl || bool_11_)
		{
			GearControl = clamp01(GearControl);
			Gear = filter(f, GearControl, Gear, 999.9F, dvGear);
		}
		if (bHasAirBrakeControl || bool_11_)
			airBrake = filter(f, AirBrakeControl, airBrake, 999.9F, dvAirbrake);
		if (bHasWingControl)
		{
			wing = filter(f, wingControl, wing, 999.9F, dvWing);
			if (wing > 0.01F && wing < 0.99F)
				FM.doRequestFMSFX(1, (int) (100.0F * wing));
		}
		if (bHasCockpitDoorControl)
			cockpitDoor = filter(f, cockpitDoorControl, cockpitDoor, 999.9F, dvCockpitDoor);
		if ((bHasArrestorControl || bool_11_) && (arrestorControl == 0.0F || arrestorControl == 1.0F))
			arrestor = filter(f, arrestorControl, arrestor, 999.9F, 0.2F);
		if (bHasFlapsControl || bool_11_)
		{
			FlapsControl = clamp01(FlapsControl);
			if (Flaps > FlapsControl)
				Flaps = filter(f, FlapsControl, Flaps, 999.0F, Aircraft.cvt(FM.getSpeedKMH(), 150.0F, 280.0F, 0.15F, 0.25F));
			else
				Flaps = filter(f, FlapsControl, Flaps, 999.0F, Aircraft.cvt(FM.getSpeedKMH(), 150.0F, 280.0F, 0.15F, 0.02F));
		}
		if (StabilizerControl)
		{
			AileronControl = -0.2F * FM.Or.getKren() - 2.0F * (float) FM.getW().x;
			tmpV3d.set(FM.Vwld);
			tmpV3d.normalize();
			float f_16_ = (float) (-500.0 * (tmpV3d.z - 0.0010));
			if (f_16_ > 0.8F)
				f_16_ = 0.8F;
			if (f_16_ < -0.8F)
				f_16_ = -0.8F;
			ElevatorControl = (f_16_ - 0.2F * FM.Or.getTangage() - 0.05F * FM.AOA + 25.0F * (float) FM.getW().y);
			RudderControl = -0.2F * FM.AOS + 20.0F * (float) FM.getW().z;
		}
		if (bHasAileronControl || bool_11_)
		{
			trimAileron = filter(f, trimAileronControl, trimAileron, 999.9F, 0.25F);
			AileronControl = clamp11(AileronControl);
			tmpF = clampA(AileronControl, f_12_);
			Ailerons = filter(f, ((1.0F + (trimAileron * tmpF <= 0.0F ? 1.0F : -1.0F) * Math.abs(trimAileron)) * tmpF) + trimAileron, Ailerons, 0.2F * (1.0F + 0.3F * Math.abs(AileronControl)), 0.025F);
		}
		if (bHasElevatorControl || bool_11_)
		{
			trimElevator = filter(f, trimElevatorControl, trimElevator, 999.9F, 0.25F);
			ElevatorControl = clamp11(ElevatorControl);
			tmpF = clampA(ElevatorControl, f_14_);
			Ev = filter(f, ((1.0F + ((trimElevator * tmpF <= 0.0F ? 1.0F : -1.0F) * Math.abs(trimElevator))) * tmpF + trimElevator), Ev, 0.3F * (1.0F + 0.3F * Math.abs(ElevatorControl)), 0.022F);
			if (FM.actor instanceof SU_26M2)
				Elevators = clamp11(Ev);
			else
				Elevators = clamp11(Ev - 0.25F * (1.0F - f_14_));
		}
		if (bHasRudderControl || bool_11_)
		{
			trimRudder = filter(f, trimRudderControl, trimRudder, 999.9F, 0.25F);
			RudderControl = clamp11(RudderControl);
			tmpF = clampA(RudderControl, f_13_);
			Rudder = filter(f, (1.0F + ((trimRudder * tmpF <= 0.0F ? 1.0F : -1.0F) * Math.abs(trimRudder))) * tmpF + trimRudder, Rudder, 0.35F * (1.0F + 0.3F * Math.abs(RudderControl)), 0.025F);
		}
		BrakeControl = clamp01(BrakeControl);
		if (bHasBrakeControl || bool_11_)
		{
			if (BrakeControl > Brake)
				Brake = Brake + 0.3F * f;
			else
				Brake = BrakeControl;
		}
		else
			Brake = 0.0F;
		// TAK++
		if (bHasBrakeControl || bool_11_)
		{
			if (BrakeRightControl > BrakeRight)
				BrakeRight = BrakeRight + 0.3F * f;
			else
				BrakeRight = BrakeRightControl;
		}
		else
		{
			BrakeRight = 0.0F;
		}
		if (bHasBrakeControl || bool_11_)
		{
			if (BrakeLeftControl > BrakeLeft)
				BrakeLeft = BrakeLeft + 0.3F * f;
			else
				BrakeLeft = BrakeLeftControl;
		}
		else
		{
			BrakeLeft = 0.0F;
		}
		// TAK--
		if (tick != Time.tickCounter())
		{
			tick = Time.tickCounter();
			CTL = (byte) ((GearControl <= 0.5F ? 0 : 1) | (FlapsControl <= 0.2F ? 0 : 2) | (BrakeControl <= 0.2F ? 0 : 4) | (RadiatorControl <= 0.5F ? 0 : 8) | (BayDoorControl <= 0.5F ? 0 : 16) | (AirBrakeControl <= 0.5F ? 0
					: 32));
			WCT &= 0xfc;
			TWCT &= 0xfc;
			int i = 0;
			for (int i_17_ = 1; i < WeaponControl.length && i_17_ < 256; i_17_ <<= 1)
			{
				if (WeaponControl[i])
				{
					WCT |= i_17_;
					TWCT |= i_17_;
				}
				i++;
			}
			for (int i_18_ = 0; i_18_ < 4; i_18_++)
				saveWeaponControl[i_18_] = WeaponControl[i_18_];
			for (int i_19_ = 0; i_19_ < Weapons.length; i_19_++)
			{
				if (Weapons[i_19_] != null)
				{
					switch (i_19_)
					{
						case 2:
						case 3:
						{
							int i_20_ = WeaponControl[i_19_] ? 1 : 0;
							if (i_20_ != 0)
							{
								// TODO: Added by |ZUTI|
								// ----------------------------------------------------------------
								Aircraft ac = (Aircraft) FM.actor;
								if (i_19_ == 3 && ac instanceof TypeBomber && zutiBombsightAutomationStatus && ZutiSupportMethods_Multicrew.mustSyncACOperation((Aircraft) FM.actor))
								{
									// Can we drop bombs at all? If not, send data to the one that can!
									// This check is put here also because us gunsight automation.
									ZutiSupportMethods_NetSend.bombardierReleasedOrdinance_ToServer(ac.name(), true, FM.CT.bHasBayDoors);
									return;
									// System.out.println("  Speed=" + ((FM.getSpeed() + 50F) * 0.5F) + ", f=" + f + ", f10=" + f_10_ + ", bool=" + bool + ", bool11=" + bool_11_);
								}
								// ----------------------------------------------------------------

								try
								{
									//TODO: Disabled for 410 compatibility
									/*
									 * if ((Aircraft)FM.actor instanceof
									 * P_51Mustang)
									 * bIsMustang = true;
									 */
								}
								catch (Throwable throwable)
								{
									/* empty */
								}
								if (!bIsMustang || (System.currentTimeMillis() > (lWeaponTime + 250L / (long) Time.speed())))
								{
									int i_21_ = -1;
									for (int i_22_ = 0; i_22_ < Weapons[i_19_].length; i_22_ += 2)
									{
										if (!(Weapons[i_19_][i_22_] instanceof FuelTankGun) && Weapons[i_19_][i_22_].haveBullets())
										{
											if (bHasBayDoors && Weapons[i_19_][i_22_].getHookName().startsWith("_BombSpawn"))
											{
												if (BayDoorControl == 1.0F)
												{
													Weapons[i_19_][i_22_].shots(i_20_);
													// TODO:Added by |ZUTI|
													// System.out.println("INTERNAL: i19=" + i_19_ + ", i22=" + i_22_ + ", i20=" + i_20_);
													ZutiSupportMethods_FM.executeOnbombDropped(zutiOwnerAircraftName, i_19_, i_22_, i_20_);
												}
											}
											else
											{
												if (!bIsMustang || (Weapons[i_19_][i_22_] instanceof RocketGun4andHalfInch)
														|| ((Weapons[i_19_][i_22_] instanceof RocketGun) && (iToggleRocketSide == LEFT))
														|| ((Weapons[i_19_][i_22_] instanceof BombGun) && (iToggleBombSide == LEFT)))
												{
													Weapons[i_19_][i_22_].shots(i_20_);
													// TODO:Added by |ZUTI|
													// System.out.println("EXTERNAL: i19=" + i_19_ + ", i22=" + i_22_ + ", i20=" + i_20_);
													ZutiSupportMethods_FM.executeOnbombDropped(zutiOwnerAircraftName, i_19_, i_22_, i_20_);
												}
												if (Weapons[i_19_][i_22_].getHookName().startsWith("_BombSpawn"))
													BayDoorControl = 1.0F;
											}
											if (((Weapons[i_19_][i_22_] instanceof BombGun) && !((BombGun) Weapons[i_19_][i_22_]).isCassette())
													|| ((Weapons[i_19_][i_22_] instanceof RocketGun) && !((RocketGun) Weapons[i_19_][i_22_]).isCassette()))
											{
												i_21_ = i_22_;
												lWeaponTime = System.currentTimeMillis();
												break;
											}
										}
									}
									for (int i_23_ = 1; i_23_ < Weapons[i_19_].length; i_23_ += 2)
									{
										if (!(Weapons[i_19_][i_23_] instanceof FuelTankGun) && Weapons[i_19_][i_23_].haveBullets())
										{
											if (bHasBayDoors && Weapons[i_19_][i_23_].getHookName().startsWith("_BombSpawn"))
											{
												if (BayDoorControl == 1.0F)
												{
													Weapons[i_19_][i_23_].shots(i_20_);
													// TODO:Added by |ZUTI|
													ZutiSupportMethods_FM.executeOnbombDropped(zutiOwnerAircraftName, i_19_, i_23_, i_20_);
												}
											}
											else if (!bIsMustang || (Weapons[i_19_][i_23_] instanceof RocketGun4andHalfInch)
													|| ((Weapons[i_19_][i_23_] instanceof RocketGun) && (iToggleRocketSide == RIGHT))
													|| ((Weapons[i_19_][i_23_] instanceof BombGun) && (iToggleBombSide == RIGHT)))
											{
												Weapons[i_19_][i_23_].shots(i_20_);
												// TODO:Added by |ZUTI|
												ZutiSupportMethods_FM.executeOnbombDropped(zutiOwnerAircraftName, i_19_, i_23_, i_20_);
											}
											if (((Weapons[i_19_][i_23_] instanceof BombGun) && !((BombGun) Weapons[i_19_][i_23_]).isCassette())
													|| ((Weapons[i_19_][i_23_] instanceof RocketGun) && !((RocketGun) Weapons[i_19_][i_23_]).isCassette()))
											{
												i_21_ = i_23_;
												lWeaponTime = System.currentTimeMillis();
												break;
											}
										}
									}
									if (i_21_ != -1)
									{
										if (Weapons[i_19_][i_21_] instanceof BombGun)
										{
											if (iToggleBombSide == LEFT)
												iToggleBombSide = RIGHT;
											else
												iToggleBombSide = LEFT;
										}
										else if (!(Weapons[i_19_][i_21_] instanceof RocketGun4andHalfInch))
										{
											if (iToggleRocketSide == LEFT)
												iToggleRocketSide = RIGHT;
											else
												iToggleRocketSide = LEFT;
										}
									}
									if (!bIsMustang)
										WeaponControl[i_19_] = false;
								}
							}
							break;
						}
						default:
							boolean flag2 = false;
							for (int i2 = 0; i2 < Weapons[i_19_].length; i2++)
							{
								Weapons[i_19_][i2].shots(WeaponControl[i_19_] ? -1 : 0);
								flag2 = flag2 || Weapons[i_19_][i2].haveBullets();
							}

							if (WeaponControl[i_19_] && !flag2 && FM.isPlayers())
								com.maddox.il2.objects.effects.ForceFeedback.fxTriggerShake(i_19_, false);
						break;
					}
				}
			}
		}
	}

	public boolean dropExternalStores(boolean flag)
	{
		boolean flag1 = ((com.maddox.il2.objects.air.Aircraft) FM.actor).dropExternalStores(flag);
		if (flag1)
		{
			FM.AS.externalStoresDropped = true;
			((com.maddox.il2.objects.air.Aircraft) FM.actor).replicateDropExternalStores();
		}
		return flag1;
	}

	public float getWeaponMass()
	{
		int i = Weapons.length;
		float f = 0.0F;
		for (int i_25_ = 0; i_25_ < i; i_25_++)
		{
			if (Weapons[i_25_] != null)
			{
				int i_26_ = Weapons[i_25_].length;
				for (int i_27_ = 0; i_27_ < i_26_; i_27_++)
				{
					BulletEmitter bulletemitter = Weapons[i_25_][i_27_];
					if (bulletemitter != null && !(bulletemitter instanceof FuelTankGun))
					{
						int i_28_ = bulletemitter.countBullets();
						if (i_28_ < 0)
						{
							i_28_ = 1;
							if (bulletemitter instanceof BombGun && ((BombGun) bulletemitter).isCassette())
								i_28_ = 10;
						}
						f += bulletemitter.bulletMassa() * (float) i_28_;
					}
				}
			}
		}
		return f;
	}

	public int getWeaponCount(int i)
	{
		if (i >= Weapons.length || Weapons[i] == null)
			return 0;
		int i_29_ = Weapons[i].length;
		int i_31_;
		int i_30_ = i_31_ = 0;
		for (/**/; i_31_ < i_29_; i_31_++)
		{
			BulletEmitter bulletemitter = Weapons[i][i_31_];
			if (bulletemitter != null && !(bulletemitter instanceof FuelTankGun))
				i_30_ += bulletemitter.countBullets();
		}
		return i_30_;
	}

	public boolean dropFuelTanks()
	{
		boolean bool = false;
		for (int i = 0; i < Weapons.length; i++)
		{
			if (Weapons[i] != null)
			{
				for (int i_32_ = 0; i_32_ < Weapons[i].length; i_32_++)
				{
					if (Weapons[i][i_32_] instanceof FuelTankGun && Weapons[i][i_32_].haveBullets())
					{
						Weapons[i][i_32_].shots(1);
						bool = true;
					}
				}
			}
		}
		if (bool)
		{
			((Aircraft) FM.actor).replicateDropFuelTanks();
			FM.M.onFuelTanksChanged();
		}
		return bool;
	}

	public FuelTank[] getFuelTanks()
	{
		int i = 0;
		for (int i_33_ = 0; i_33_ < Weapons.length; i_33_++)
		{
			if (Weapons[i_33_] != null)
			{
				for (int i_34_ = 0; i_34_ < Weapons[i_33_].length; i_34_++)
				{
					if (Weapons[i_33_][i_34_] instanceof FuelTankGun)
						i++;
				}
			}
		}
		FuelTank[] fueltanks = new FuelTank[i];
		int i_35_;
		for (int i_36_ = i_35_ = 0; i_36_ < Weapons.length; i_36_++)
		{
			if (Weapons[i_36_] != null)
			{
				for (int i_37_ = 0; i_37_ < Weapons[i_36_].length; i_37_++)
				{
					if (Weapons[i_36_][i_37_] instanceof FuelTankGun)
						fueltanks[i_35_++] = ((FuelTankGun) Weapons[i_36_][i_37_]).getFuelTank();
				}
			}
		}
		return fueltanks;
	}

	public void resetControl(int i)
	{
		switch (i)
		{
			case 0:
				AileronControl = 0.0F;
				Ailerons = 0.0F;
				trimAileron = 0.0F;
			break;
			case 1:
				ElevatorControl = 0.0F;
				Elevators = 0.0F;
				trimElevator = 0.0F;
			break;
			case 2:
				RudderControl = 0.0F;
				Rudder = 0.0F;
				trimRudder = 0.0F;
			break;
		}
	}

	// TODO: |ZUTI| methods and variables
	// ------------------------------------------------------------
	public String	zutiOwnerAircraftName			= null;

	private boolean	zutiBombsightAutomationStatus	= false;

	/**
	 * Sets bombsight automation status to on or off.
	 * 
	 * @return
	 */
	public void zutiSetBombsightAutomationEngaged(boolean value)
	{
		zutiBombsightAutomationStatus = value;
	}

	// TAK Methods
	// ------------------------------------------------------------
	public float	BrakeRightControl;
	public float	BrakeLeftControl;
	private float	BrakeRight;
	private float	BrakeLeft;

	public float getBrakeRight()
	{
		return BrakeRight;
	}

	public float getBrakeLeft()
	{
		return BrakeLeft;
	}
	// ------------------------------------------------------------
}