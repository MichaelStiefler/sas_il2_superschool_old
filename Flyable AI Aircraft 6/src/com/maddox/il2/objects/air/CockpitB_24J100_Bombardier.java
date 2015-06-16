package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class CockpitB_24J100_Bombardier extends CockpitBombardier {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			setNew.altimeter = fm.getAltitude();
			if (fm.CT.bombReleaseMode == 0) {
				if (salvoLever < 1.0F)
					salvoLever += 0.05F;
			} else if (salvoLever > 0.0F)
				salvoLever -= 0.05F;
			float f = ((B_24J100)aircraft()).fSightCurForwardAngle;
			float f1 = ((B_24J100)aircraft()).fSightHeadTurn * 30F;
			float f2 = aircraft().FM.Or.getTangage();
			float f3 = aircraft().FM.Or.getKren() + (float)Math.sin(Math.toRadians(f)) * f1 * 0.01F
					+ (float)Math.sin(Math.toRadians(f1)) * f2;
			if (f2 > 20F)
				f2 = 20F;
			if (f2 < -20F)
				f2 = -20F;
			if (f3 > 20F)
				f3 = 20F;
			if (f3 < -20F)
				f3 = -20F;
			float f4 = f - f2;
			mesh.chunkSetAngles("RollGyro", 0.0F, -f3, 0.0F);
			mesh.chunkSetAngles("BlackBox", -f1, 0.0F, f4);
			if (bEntered) {
				HookPilot hookpilot = HookPilot.cur();
				hookpilot.setInstantOrient(aAim - f1, tAim + f, 0.0F);
			}
			float f5 = waypointAzimuth();
			setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
			if (useRealisticNavigationInstruments()) {
				setNew.waypointAzimuth.setDeg(f5 - 90F);
				setOld.waypointAzimuth.setDeg(f5 - 90F);
				setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus()
						- setOld.azimuth.getDeg(1.0F));
			} else {
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f5);
				setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f5 - setOld.azimuth.getDeg(0.1F)
						- 90F);
			}
			switch (iWiper) {
			default:
				break;

			case 0: // '\0'
				if (Mission.curCloudsType() > 4 && fm.getSpeedKMH() < 220F
						&& fm.getAltitude() < Mission.curCloudsHeight() + 300F)
					iWiper = 1;
				break;

			case 1: // '\001'
				setNew.wiper = setOld.wiper - 0.05F;
				if (setNew.wiper < -1.03F)
					iWiper++;
				if (wiState >= 2)
					break;
				if (wiState == 0) {
					if (fxw == null) {
						fxw = aircraft().newSound("aircraft.wiper", false);
						if (fxw != null) {
							fxw.setParent(aircraft().getRootFX());
							fxw.setPosition(sfxPos);
						}
					}
					if (fxw != null)
						fxw.play(wiStart);
				}
				if (fxw != null) {
					fxw.play(wiRun);
					wiState = 2;
				}
				break;

			case 2: // '\002'
				setNew.wiper = setOld.wiper + 0.05F;
				if (setNew.wiper > 1.03F)
					iWiper++;
				if (wiState > 1)
					wiState = 1;
				break;

			case 3: // '\003'
				setNew.wiper = setOld.wiper - 0.05F;
				if (setNew.wiper >= 0.02F)
					break;
				if (fm.getSpeedKMH() > 250F || fm.getAltitude() > Mission.curCloudsHeight() + 400F)
					iWiper++;
				else
					iWiper = 1;
				break;

			case 4: // '\004'
				setNew.wiper = setOld.wiper;
				iWiper = 0;
				wiState = 0;
				if (fxw != null)
					fxw.cancel();
				break;
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork radioCompassAzimuth;
		float altimeter;
		float wiper;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
			radioCompassAzimuth = new AnglesFork();
		}

	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.cur();
			hookpilot.doAim(false);
			Point3d point3d = new Point3d();
			point3d.set(0.15D, 0.0D, -0.1D);
			hookpilot.setTubeSight(point3d);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		if (!isFocused()) {
			return;
		} else {
			leave();
			super.doFocusLeave();
			return;
		}
	}

	private void prepareToEnter() {
		HookPilot hookpilot = HookPilot.cur();
		if (hookpilot.isPadlock())
			hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleAimOrient(0.0F, -33F, 0.0F);
		enteringAim = true;
	}

	private void enter() {
		saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov " + saveBSFov);
		Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
		HookPilot hookpilot = HookPilot.cur();
		hookpilot.setInstantOrient(aAim, tAim, 0.0F);
		hookpilot.setSimpleUse(true);
		hookpilot.setStabilizedBSUse(true);
		doSetSimpleUse(true);
		HotKeyEnv.enable("PanView", false);
		HotKeyEnv.enable("SnapView", false);
		bEntered = true;
	}

	private void leave() {
		if (enteringAim) {
			HookPilot hookpilot = HookPilot.cur();
			hookpilot.setInstantOrient(0.0F, -33F, 0.0F);
			hookpilot.doAim(false);
			hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			return;
		}
		if (!bEntered) {
			return;
		} else {
			saveBSFov = Main3D.FOVX;
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
			CmdEnv.top().exec("fov " + saveFov);
			HookPilot hookpilot1 = HookPilot.cur();
			hookpilot1.setInstantOrient(0.0F, -33F, 0.0F);
			hookpilot1.doAim(false);
			hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			hookpilot1.setSimpleUse(false);
			hookpilot1.setStabilizedBSUse(false);
			doSetSimpleUse(false);
			boolean flag = HotKeyEnv.isEnabled("aircraftView");
			HotKeyEnv.enable("PanView", flag);
			HotKeyEnv.enable("SnapView", flag);
			bEntered = false;
			return;
		}
	}

	public void destroy() {
		super.destroy();
		leave();
	}

	public void doToggleAim(boolean flag) {
		if (!isFocused())
			return;
		if (isToggleAim() == flag)
			return;
		if (flag)
			prepareToEnter();
		else
			leave();
	}

	public CockpitB_24J100_Bombardier() {
		super("3DO/Cockpit/B-24D-Bombardier/hier.him", "bf109");
		fxw = null;
		wiStart = new Sample("wip_002_s.wav", 256, 65535);
		wiRun = new Sample("wip_002.wav", 256, 65535);
		wiState = 0;
		iWiper = 0;
		enteringAim = false;
		patron = 1.0F;
		patronMat = null;
		salvoLever = 0.0F;
		saveBSFov = 23.913F;
		bEntered = false;
		setOld = new Variables();
		setNew = new Variables();
		try {
			Loc loc = new Loc();
			HookNamed hooknamed1 = new HookNamed(mesh, "CAMERAAIM");
			hooknamed1.computePos(this, new Loc(), loc);
			aAim = loc.getOrient().getAzimut();
			tAim = loc.getOrient().getTangage();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		cockpitNightMats = (new String[] { "APanelSides", "Needles2", "oxygen2", "RadioComp", "textrbm9", "texture25",
				"norden_crosshair" });
		setNightMats(false);
		HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
		Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
		light1 = new LightPointActor(new LightPoint(), loc1.getPoint());
		light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
		light1.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOKB1", light1);
		hooknamed = new HookNamed(mesh, "LAMPHOOK2");
		loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
		light2 = new LightPointActor(new LightPoint(), loc1.getPoint());
		light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
		light2.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOKB2", light2);
		interpPut(new Interpolater(), null, Time.current(), null);
		printCompassHeading = true;
		for (int i = 0; i < internalBombs.length; i++) {
			String s = "_BombSpawn";
			if (i < 10)
				s = s + "0" + (i + 1);
			else
				s = s + (i + 1);
			internalBombs[i] = getBomb(s);
		}

		int j = -1;
		j = mesh.materialFind("50CalRound");
		if (j != -1) {
			patronMat = mesh.material(j);
			patronMat.setLayer(0);
		}
	}

	private BulletEmitter getBomb(String s) {
		BulletEmitter bulletemitter = aircraft().getBulletEmitterByHookName(s);
		return bulletemitter;
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl) {
			setNightMats(true);
			light1.light.setEmit(0.4F, 1.0F);
			light2.light.setEmit(0.4F, 2.0F);
		} else {
			setNightMats(false);
			light1.light.setEmit(0.0F, 0.0F);
			light2.light.setEmit(0.0F, 0.0F);
		}
	}

	public void reflectWorldToInstruments(float f) {
		float f1 = ((B_24J100)aircraft()).fSightCurForwardAngle;
		float f2 = ((B_24J100)aircraft()).fSightCurAltitude;
		float f3 = ((B_24J100)aircraft()).fSightCurSpeed;
		boolean flag = ((B_24J100)aircraft()).bSightClutch;
		float f4 = ((B_24J100)aircraft()).fSightCurSideslip;
		float f5 = ((B_24J100)aircraft()).fSightHeadTurn * 30F;
		float f6 = ((B_24J100)aircraft()).getBombSightPDI();
		mesh.chunkSetAngles("zFootBall", f5, 0.0F, 0.0F);
		mesh.chunkSetAngles("zNPDI", f6 * 10F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zBsClutch", f5, 0.0F, 0.0F);
		mesh.chunkSetAngles("zBSArm01", f5, 0.0F, 0.0F);
		mesh.chunkSetAngles("zBSArm02", f6 * 10F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zClutchLever", flag ? 0.0F : 30F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zNAutoClutch", f6 * -10F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zSightAngle", 0.0F, 0.0F, -f1);
		mesh.chunkSetAngles("zMirDrClutch", 0.0F, 0.0F, 5F * f1);
		mesh.chunkSetAngles("zBSpeed", 0.0F, 0.0F, -cvt(f3 * f2, 100000F, 2.25E+007F, 0.0F, 360F));
		mesh.chunkSetAngles("zNTurnDrift", f4, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_IntToggle", 0.0F, 0.0F, aircraft().FM.CT.bombReleaseMode != 5 ? -60F : 0.0F);
		mesh.chunkVisible("intLightOn", aircraft().FM.CT.bombReleaseMode == 5);
		mesh.chunkSetAngles("Z_IntBInterval", 0.0F, 0.0F, -5.64F * (float)aircraft().FM.CT.bombTrainAmount);
		mesh.chunkSetAngles("Z_GroundSpeed", 0.0F, 0.0F, -184F + (float)aircraft().FM.CT.bombTrainDelay * 18.4F);
		mesh.chunkSetAngles("Z_KnobGrndSpd", 0.0F, 0.0F, (float)aircraft().FM.CT.bombTrainDelay * -32F);
		mesh.chunkSetAngles("zWiperBottom", cvt(interp(setNew.wiper, setOld.wiper, f), -1F, 1.0F, -61F, 61F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Turret7A_Axis", 0.0F, 35F, 0.0F);
		mesh.chunkSetAngles("Turret7B_D0", 0.0F, -40F, 0.0F);
		mesh.chunkSetAngles("Turret8A_Axis", 0.0F, -40F, 0.0F);
		mesh.chunkSetAngles("Turret8B_D0", 0.0F, -40F, 0.0F);
		for (int i = 0; i < internalBombs.length; i++)
			mesh.chunkVisible("rack" + (i + 1) + "On", internalBombs[i].haveBullets());

		float f7 = cvt(aircraft().getBayDoor(), 0.0F, 0.5F, 0.0F, 1.0F);
		float f8 = cvt(aircraft().getBayDoor(), 0.5F, 1.0F, 0.0F, 1.0F);
		mesh.chunkSetAngles("zDOpenClose", 0.0F, 0.0F, f7 * -50F);
		mesh.chunkSetAngles("zDOpenCloseRod", 0.0F, 0.0F, f7 * -51.5F);
		mesh.chunkSetAngles("zReleaseLvSlot", 0.0F, 0.0F, f7 * -17.5F);
		resetYPRmodifier();
		xyz[0] = 0.018F * f7;
		mesh.chunkSetLocate("zHydraulicRod", xyz, ypr);
		mesh.chunkSetAngles("zArmingSafe", 0.0F, 0.0F, f8 * 60F);
		mesh.chunkSetAngles("zArmingSafeRod", 0.0F, 0.0F, f8 * 58.5F);
		mesh.chunkSetAngles("zArmingPL", 0.0F, 0.0F, f8 * 28F);
		resetYPRmodifier();
		xyz[0] = 0.0035F * f8;
		mesh.chunkSetLocate("zArmingPLRod", xyz, ypr);
		mesh.chunkSetAngles("zHSalvoLock", 0.0F, 0.0F, salvoLever * 30F);
		mesh.chunkSetAngles("zHSalvoLockRod", 0.0F, 0.0F, salvoLever * 29.9F);
		mesh.chunkSetAngles("zHSalvoLkPL", 0.0F, 0.0F, salvoLever * 20.5F);
		resetYPRmodifier();
		xyz[0] = 0.0075F * salvoLever;
		mesh.chunkSetLocate("zHSalvoLkPLRod", xyz, ypr);
		mesh.chunkVisible("lDoorOpenOn", f7 > 0.01F);
		mesh.chunkVisible("bombReleaseOn", fm.CT.saveWeaponControl[3]);
		mesh.chunkSetAngles("zAlt2", 0.0F, 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F));
		mesh.chunkSetAngles("zAlt1", 0.0F, 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F));
		mesh.chunkSetAngles("zCompass1", 0.0F, 0.0F, -setNew.azimuth.getDeg(f) - 90F);
		mesh.chunkSetAngles("zCompass2", 0.0F, 0.0F, -setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F);
		mesh.chunkSetAngles("zCompass3", -setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
		mesh.chunkSetAngles(
				"zSpeed1",
				0.0F,
				0.0F,
				-floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F),
						speedometerScale));
		if (enteringAim) {
			HookPilot hookpilot = HookPilot.cur();
			if (hookpilot.isAimReached()) {
				enteringAim = false;
				enter();
			} else if (!hookpilot.isAim())
				enteringAim = false;
		}
		if (bEntered) {
			mesh.chunkVisible("BlackBox", true);
			mesh.chunkVisible("zReticle", true);
		} else {
			mesh.chunkVisible("BlackBox", false);
			mesh.chunkVisible("zReticle", false);
		}
		if (fm.CT.WeaponControl[0] && patronMat != null) {
			patron += 0.07F * f;
			patronMat.setLayer(0);
			patronMat.set((byte)11, patron);
		}
		mesh.chunkSetAngles("zO2CylPress", 0.0F, 0.0F, -130F);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("zHolesG_D1", true);
	}

	private SoundFX fxw;
	private Sample wiStart;
	private Sample wiRun;
	private int wiState;
	private int iWiper;
	private LightPointActor light1;
	private LightPointActor light2;
	private boolean enteringAim;
	private float patron;
	private Mat patronMat;
	private float salvoLever;
	private BulletEmitter internalBombs[] = { null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null };
	private static final float speedometerScale[] = { 0.0F, 2.5F, 54F, 104F, 154.5F, 205.5F, 224F, 242F, 259.5F, 277.5F,
			296.25F, 314F, 334F, 344.5F };
	private float saveFov;
	private float saveBSFov;
	private float aAim;
	private float tAim;
	private boolean bEntered;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;

	static {
		Property.set(CockpitB_24J100_Bombardier.class, "astatePilotIndx", 2);
		Property.set(CockpitB_24J100_Bombardier.class, "aiTuretNum", -2);
		Property.set(CockpitB_24J100_Bombardier.class, "weaponControlNum", 3);
		Property.set(CockpitB_24J100_Bombardier.class, "normZN", 1.6F);
	}

}
