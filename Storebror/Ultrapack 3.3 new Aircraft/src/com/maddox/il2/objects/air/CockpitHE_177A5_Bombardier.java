package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_177A5_Bombardier extends CockpitPilot {
	private class Variables {

		float elevTrim;
		float rudderTrim;
		float ailTrim;
		float cons;

		private Variables() {
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			float f = ((HE_177A5) aircraft()).fSightCurForwardAngle;
			float f1 = ((HE_177A5) aircraft()).fSightCurSideslip;
			CockpitHE_177A5_Bombardier.calibrAngle = 360F - fm.Or.getPitch();
			mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, calibrAngle + f);
			if (bEntered) {
				if (!hasToKG)
					HookPilot.current.setInstantOrient(aAim + 10F * f1, tAim + calibrAngle + f, 0.0F);
			}
			mesh.chunkSetAngles("Z_turret1A", 0.0F, aircraft().FM.turret[0].tu[0], 0.0F);
			mesh.chunkSetAngles("Z_turret1B", 0.0F, aircraft().FM.turret[0].tu[1], 0.0F);
			setNew.elevTrim = 0.85F * setOld.elevTrim + fm.CT.getTrimElevatorControl() * 0.15F;
			setNew.rudderTrim = 0.85F * setOld.rudderTrim + fm.CT.getTrimRudderControl() * 0.15F;
			setNew.ailTrim = 0.85F * setOld.ailTrim + fm.CT.getTrimAileronControl() * 0.15F;
			float f2 = prevFuel - fm.M.fuel;
			prevFuel = fm.M.fuel;
			f2 /= 0.72F;
			f2 /= Time.tickLenFs();
			f2 *= 3600F;
			setNew.cons = 0.91F * setOld.cons + 0.09F * f2;
			return true;
		}

		Interpolater() {
		}
	}

	protected boolean doFocusEnter() {
		if (!super.doFocusEnter())
			return false;
//		if (CommonUtils.is411orLater()) {
			if (doToKGCheck) {
				doToKGCheck = false;
				if ((aircraft() instanceof HE_177A5) && ((HE_177A5) aircraft()).hasToKG) {
					hasToKG = true;
					showToKG();
				}
			}
//		}
		((HE_177A5) fm.actor).bPitUnfocused = false;
		HookPilot hookpilot = HookPilot.current;
		hookpilot.doAim(false);
		aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
		aircraft().hierMesh().chunkVisible("Head1_D0", false);
		aircraft().hierMesh().chunkVisible("Hmask1_D0", false);
		aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
		aircraft().hierMesh().chunkVisible("Hmask2_D0", false);
		aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
		aircraft().hierMesh().chunkVisible("Hmask3_D0", false);
		aircraft().hierMesh().chunkVisible("Pilot4_D0", false);
		aircraft().hierMesh().chunkVisible("Hmask4_D0", false);
		aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
		aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
		aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
		aircraft().hierMesh().chunkVisible("Pilot4_D1", false);
		aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
		aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
		mesh.chunkVisible("k2-Box", false);
		mesh.chunkVisible("k2-Cable", false);
		mesh.chunkVisible("k2-cushion", false);
		mesh.chunkVisible("k2-FuG203", false);
		mesh.chunkVisible("k2-gunsight", false);
		return true;
	}

	protected void doFocusLeave() {
		if (isFocused()) {
			((HE_177A5) fm.actor).bPitUnfocused = false;
			if (!fm.AS.isPilotParatrooper(0)) {
				aircraft().hierMesh().chunkVisible("Pilot1_D0", !fm.AS.isPilotDead(0));
				aircraft().hierMesh().chunkVisible("Head1_D0", !fm.AS.isPilotDead(0));
				aircraft().hierMesh().chunkVisible("Pilot1_D1", fm.AS.isPilotDead(0));
			}
			if (!fm.AS.isPilotParatrooper(1)) {
				aircraft().hierMesh().chunkVisible("Pilot2_D0", !fm.AS.isPilotDead(1));
				aircraft().hierMesh().chunkVisible("Pilot2_D1", fm.AS.isPilotDead(1));
			}
			if (!fm.AS.isPilotParatrooper(2)) {
				aircraft().hierMesh().chunkVisible("Pilot3_D0", !fm.AS.isPilotDead(2));
				aircraft().hierMesh().chunkVisible("Pilot3_D1", fm.AS.isPilotDead(2));
			}
			if (!fm.AS.isPilotParatrooper(3)) {
				aircraft().hierMesh().chunkVisible("Pilot4_D0", !fm.AS.isPilotDead(3));
				aircraft().hierMesh().chunkVisible("Pilot4_D1", fm.AS.isPilotDead(3));
			}
			aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
			aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
			aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
			mesh.chunkVisible("k2-Box", true);
			mesh.chunkVisible("k2-Cable", true);
			mesh.chunkVisible("k2-cushion", true);
			mesh.chunkVisible("k2-FuG203", true);
			mesh.chunkVisible("k2-gunsight", true);
			leave();
			super.doFocusLeave();
		}
	}

	private void enter() {
		saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov 23.913");
		Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock())
			hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleUse(true);
		hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
		HotKeyEnv.enable("PanView", false);
		HotKeyEnv.enable("SnapView", false);
		bEntered = true;
	}

	private void leave() {
		if (!bEntered)
			return;
		Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
		CmdEnv.top().exec("fov " + saveFov);
		HookPilot hookpilot1 = HookPilot.current;
		hookpilot1.doAim(false);
		hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
		hookpilot1.setSimpleUse(false);
		boolean flag = HotKeyEnv.isEnabled("aircraftView");
		HotKeyEnv.enable("PanView", flag);
		HotKeyEnv.enable("SnapView", flag);
		bEntered = false;
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
			enter();
		else
			leave();
	}

	public CockpitHE_177A5_Bombardier() {
		super("3DO/Cockpit/He-177A-5/Bombardier.him", "he111");
		doToKGCheck = true;
		hasToKG = false;
		bEntered = false;
		pictAiler = 0.0F;
		pictElev = 0.0F;
		setOld = new Variables();
		setNew = new Variables();
		prevFuel = 0.0F;
		try {
			Loc loc = new Loc();
			HookNamed hooknamed = new HookNamed(mesh, "CAMERAAIM");
			hooknamed.computePos(this, pos.getAbs(), loc);
			aAim = loc.getOrient().getAzimut();
			tAim = loc.getOrient().getTangage();
			HookNamed hooknamed1 = new HookNamed(mesh, "LAMPHOOK1");
			Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			hooknamed1.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
			light1 = new LightPointActor(new LightPoint(), loc.getPoint());
			light1.light.setColor(218F, 143F, 128F);
			light1.light.setEmit(0.0F, 0.0F);
			pos.base().draw.lightMap().put("LAMPHOOK1", light1);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		cockpitNightMats = (new String[] { "Peil1", "Peil2", "Instrument1", "Instrument2", "Instrument4", "Instrument5", "Instrument6", "Instrument7", "Instrument8", "Instrument9", "Needles" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
	}

	private void showToKG() {
		System.out.println("CockpitHE_177_Bombardier showToKG()");
		mesh.chunkVisible("Lofte", false);
		mesh.chunkVisible("TorpedoBoxNew", true);
		mesh.chunkVisible("speedDial", true);
		mesh.chunkVisible("angleDial", true);
		mesh.chunkVisible("Ship", true);
		mesh.chunkVisible("Z_Angle", true);
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	public void reflectWorldToInstruments(float f) {
		boolean bShowBombsight = bEntered;
		if (bShowBombsight) {
			mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((HE_177A5) aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
			boolean flag = ((HE_177A5) aircraft()).fSightCurReadyness > 0.93F;
			mesh.chunkVisible("BlackBox", true);
			mesh.chunkVisible("zReticle", flag);
			mesh.chunkVisible("zAngleMark", flag);
		} else {
			mesh.chunkVisible("BlackBox", false);
			mesh.chunkVisible("zReticle", false);
			mesh.chunkVisible("zAngleMark", false);
			mesh.chunkSetAngles("ZWheel", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 40F, 0.0F);
			mesh.chunkSetAngles("ZColumn", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F, 0.0F);
			mesh.chunkSetAngles("PedalR", 0.0F, -fm.CT.getRudder() * 10F, 0.0F);
			mesh.chunkSetAngles("PedalL", 0.0F, fm.CT.getRudder() * 10F, 0.0F);
			resetYPRmodifier();
			Cockpit.xyz[1] = cvt(fm.CT.getRudder(), -1F, 1.0F, 0.08F, -0.08F);
			mesh.chunkSetLocate("PedalRbar", Cockpit.xyz, Cockpit.ypr);
			Cockpit.xyz[1] = cvt(fm.CT.getRudder(), -1F, 1.0F, -0.08F, 0.08F);
			mesh.chunkSetLocate("PedalLbar", Cockpit.xyz, Cockpit.ypr);
			mesh.chunkSetAngles("z_ElevTrim", 0.0F, -cvt(interp(setNew.elevTrim, setOld.elevTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
			mesh.chunkSetAngles("z_RudderTrim", 0.0F, cvt(interp(setNew.rudderTrim, setOld.rudderTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
			mesh.chunkSetAngles("z_AileronTrim", 0.0F, cvt(interp(setNew.ailTrim, setOld.ailTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
			float f1 = setNew.cons;
			float f2 = fm.EI.engines[0].getRPM();
			float f3 = fm.EI.engines[1].getRPM();
			float f4 = (f1 * f2) / (f2 + f3);
			float f5 = (f1 * f3) / (f2 + f3);
			mesh.chunkSetAngles("z_FuelCons1", 0.0F, cvt(f4, 0.0F, 500F, 0.0F, 300F), 0.0F);
			mesh.chunkSetAngles("z_FuelCons2", 0.0F, cvt(f5, 0.0F, 500F, 0.0F, 300F), 0.0F);
			float f6 = fm.M.fuel / 0.72F;
			mesh.chunkSetAngles("z_Fuel3", 0.0F, cvt(f6, 0.0F, 1100F, 0.0F, 69F), 0.0F);
			mesh.chunkSetAngles("z_Fuel1", 0.0F, cvt(f6, 1100F, 2670F, 0.0F, 84F), 0.0F);
			mesh.chunkSetAngles("z_Fuel2", 0.0F, cvt(f6, 1100F, 2670F, 0.0F, 84F), 0.0F);
			mesh.chunkSetAngles("z_OilPres1", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 15F, 0.0F, -135F), 0.0F);
			mesh.chunkSetAngles("z_OilPres2", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness(), 0.0F, 15F, 0.0F, -135F), 0.0F);
			mesh.chunkSetAngles("z_FuelPres1", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F);
			mesh.chunkSetAngles("z_FuelPres2", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F);
			mesh.chunkSetAngles("Z_TempCylL", 0.0F, cvt(fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
			mesh.chunkSetAngles("Z_TempCylR", 0.0F, cvt(fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
			mesh.chunkSetAngles("z_OAT", 0.0F, floatindex(cvt(Atmosphere.temperature((float) fm.Loc.z) - 273.15F, -60F, 40F, 0.0F, 10F), OATscale), 0.0F);
			mesh.chunkSetAngles("Z_Turret1A", 0.0F, -fm.turret[0].tu[0], 0.0F);
			mesh.chunkSetAngles("Z_Turret1B", 0.0F, fm.turret[0].tu[1], 0.0F);
			mesh.chunkSetAngles("Z_Turret5A", 0.0F, -fm.turret[4].tu[0], 0.0F);
			mesh.chunkSetAngles("Z_Turret5B", 0.0F, fm.turret[4].tu[1], 0.0F);
			mesh.chunkSetAngles("z_Velx", 0.0F, floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 50F, 800F, 0.0F, 15F), speedometerScale), 0.0F);
		}
//		if (CommonUtils.is411orLater()) {
			if (hasToKG) {
				mesh.chunkSetAngles("Ship", 0.0F, -((HE_177A5) aircraft()).fAOB, 0.0F);
				mesh.chunkSetAngles("speedDial", 0.0F, 6.4F * ((HE_177A5) aircraft()).fShipSpeed, 0.0F);
				mesh.chunkSetAngles("Z_Angle", 0.0F, ((HE_177A5) aircraft()).FM.AS.getGyroAngle(), 0.0F);
			}
//		}
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("XGlassHoles1", true);
			mesh.chunkVisible("XGlassHoles3", true);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("XGlassHoles7", true);
			mesh.chunkVisible("XGlassHoles4", true);
			mesh.chunkVisible("XGlassHoles2", true);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("XGlassHoles5", true);
			mesh.chunkVisible("XGlassHoles3", true);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("XGlassHoles1", true);
		mesh.chunkVisible("XGlassHoles6", true);
		mesh.chunkVisible("XGlassHoles4", true);
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("XGlassHoles6", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("XGlassHoles7", true);
			mesh.chunkVisible("XGlassHoles2", true);
		}
		if ((fm.AS.astateCockpitState & 0x80) != 0) {
			mesh.chunkVisible("XGlassHoles5", true);
			mesh.chunkVisible("XGlassHoles2", true);
		}
	}

	public float[] getBombSightFovs() {
		return defaultBSightFoVs;
	}

	private static final float defaultBSightFoVs[] = { 25F, 30F };

	private static final float angleScale[] = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
	private static final float speedometerScale[] = { 0.0F, 17F, 41F, 66F, 93F, 119F, 139F, 164F, 188F, 214F, 239F, 266F, 292F, 317F, 341F, 372F };
	private float saveFov;
	private float aAim;
	private float tAim;
	private boolean bEntered;
	private boolean doToKGCheck;
	private boolean hasToKG;
	private float pictAiler;
	private float pictElev;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	private LightPointActor light1;
	private float prevFuel;
	private static float calibrAngle = 3F;
	private static final float OATscale[] = { 0.0F, 7F, 17F, 27F, 37F, 47F, 56F, 65F, 72F, 80F, 85F };

	static {
		Class class1 = CockpitHE_177A5_Bombardier.class;
		Property.set(class1, "astatePilotIndx", 0);
	}

}
