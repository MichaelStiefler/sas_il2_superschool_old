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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitCantZ506B extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
				setNew.prop1 = 0.9F * setOld.prop1 + 0.1F * fm.EI.engines[0].getControlProp();
				setNew.man1 = 0.92F * setOld.man1 + 0.08F * fm.EI.engines[0].getManifoldPressure();
				setNew.throttle2 = 0.9F * setOld.throttle2 + 0.1F * fm.EI.engines[1].getControlThrottle();
				setNew.prop2 = 0.9F * setOld.prop2 + 0.1F * fm.EI.engines[1].getControlProp();
				setNew.man2 = 0.92F * setOld.man2 + 0.08F * fm.EI.engines[1].getManifoldPressure();
				setNew.throttle3 = 0.9F * setOld.throttle3 + 0.1F * fm.EI.engines[2].getControlThrottle();
				setNew.prop3 = 0.9F * setOld.prop3 + 0.1F * fm.EI.engines[2].getControlProp();
				setNew.man3 = 0.92F * setOld.man3 + 0.08F * fm.EI.engines[2].getManifoldPressure();
				setNew.altimeter = fm.getAltitude();
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
				setNew.vspeed = (100F * setOld.vspeed + fm.getVertSpeed()) / 101F;
				float f = waypointAzimuth();
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), (f - setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
				setNew.waypointDirection.setDeg(setOld.waypointDirection.getDeg(1.0F), f);
				setNew.inert = 0.999F * setOld.inert + 0.001F * (fm.EI.engines[0].getStage() != 6 ? 0.0F : 0.867F);
				if (useRealisticNavigationInstruments())
					setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
				else
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), (f - setOld.azimuth.getDeg(1.0F)) + 90F);
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float throttle1;
		float throttle2;
		float throttle3;
		float prop1;
		float prop2;
		float prop3;
		float man1;
		float man2;
		float man3;
		float altimeter;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork waypointDirection;
		float vspeed;
		float inert;
		float beaconDirection;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
			waypointDirection = new AnglesFork();
		}

	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			((CantZ506B) fm.actor).bPitUnfocused = false;
			aircraft().hierMesh().chunkVisible("Interior_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		if (isFocused()) {
			((CantZ506B) fm.actor).bPitUnfocused = true;
			aircraft().hierMesh().chunkVisible("Interior_D0", true);
			super.doFocusLeave();
		}
	}

	protected float waypointAzimuth() {
		WayPoint waypoint = fm.AP.way.curr();
		if (waypoint == null)
			return 0.0F;
		waypoint.getP(tmpP);
		tmpV.sub(tmpP, fm.Loc);
		float f;
		for (f = (float) (57.295779513082323D * Math.atan2(-tmpV.y, tmpV.x)); f <= -180F; f += 180F)
			;
		for (; f > 180F; f -= 180F)
			;
		return f;
	}

	public CockpitCantZ506B() {
		super("3DO/Cockpit/Cant/hier.him", "he111");
		bNeedSetUp = true;
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		cockpitNightMats = (new String[] { "GP1", "GP2", "GP_II_DM", "GP_III_DM", "GP3", "GP_IV_DM", "GP_IV", "GP4", "GP5", "GP6", "GP7", "GP8", "GP9", "compass", "instr", "Ita_Needles", "gauges5", "throttle", "Eqpt_II", "Trans_II", "Trans_VI_Pilot",
				"Trans_VII_Pilot" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);
		limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.08F, 0.12F, -0.1F, 0.04F, -0.03F });
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		resetYPRmodifier();
		mesh.chunkSetAngles("Z_Throtle1", -45F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Throtle2", -45F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Throtle3", -45F * interp(setNew.throttle3, setOld.throttle3, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Prop1", -45F * interp(setNew.prop1, setOld.prop1, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Prop2", -45F * interp(setNew.prop2, setOld.prop2, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Prop3", -45F * interp(setNew.prop3, setOld.prop3, f), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = -0.095F * fm.CT.getRudder();
		mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[1] = -Cockpit.xyz[1];
		mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Columnbase", -8F * (pictElev = 0.65F * pictElev + 0.35F * fm.CT.ElevatorControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Column", -45F * (pictAiler = 0.65F * pictAiler + 0.35F * fm.CT.AileronControl), 0.0F, 0.0F);
		resetYPRmodifier();
		mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter3", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter4", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Speedometer1", -floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Speedometer2", -floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.Or.getTangage(), -45F, 45F, 0.018F, -0.018F);
		mesh.chunkSetLocate("Z_TurnBank1", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_TurnBank1Q", -fm.Or.getKren(), 0.0F, 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("Z_TurnBank2", cvt(w.z, -0.23562F, 0.23562F, -27F, 27F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_TurnBank3", cvt(getBall(7D), -7F, 7F, 10F, -10F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Climb1", cvt(setNew.vspeed, -30F, 30F, 180F, -180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass2", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM1", cvt(fm.EI.engines[0].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM2", cvt(fm.EI.engines[1].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM3", cvt(fm.EI.engines[2].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F, 0.0F);
		float f1 = 0.0F;
		if (fm.M.fuel > 1.0F)
			f1 = cvt(fm.EI.engines[0].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
		mesh.chunkSetAngles("Z_FuelPres1", cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
		f1 = 0.0F;
		if (fm.M.fuel > 1.0F)
			f1 = cvt(fm.EI.engines[1].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
		mesh.chunkSetAngles("Z_FuelPres2", cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Pres1", -cvt(setNew.man1, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Pres2", -cvt(setNew.man2, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Pres3", -cvt(setNew.man3, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oil1", cvt(fm.EI.engines[0].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oil2", cvt(fm.EI.engines[1].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_FlapPos", cvt(fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass4", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		mesh.chunkVisible("XRGearUp", fm.CT.getGear() < 0.01F || !fm.Gears.rgear);
		mesh.chunkVisible("XLGearUp", fm.CT.getGear() < 0.01F || !fm.Gears.lgear);
		mesh.chunkVisible("XRGearDn", fm.CT.getGear() > 0.99F && fm.Gears.rgear);
		mesh.chunkVisible("XLGearDn", fm.CT.getGear() > 0.99F && fm.Gears.lgear);
		mesh.chunkSetAngles("Zfuel", 0.0F, cvt(fm.M.fuel, 0.0F, 3117F, 0.0F, 245F), 0.0F);
		if (useRealisticNavigationInstruments()) {
			float f2 = -cvt(setNew.beaconDirection, -45F, 45F, -45F, 45F);
			mesh.chunkSetAngles("Zcourse", 0.0F, f2, 0.0F);
		} else {
			float f3 = -cvt(setNew.waypointAzimuth.getDeg(f * 0.1F), -45F, 45F, -45F, 45F);
			mesh.chunkSetAngles("ZCourse", 0.0F, f3, 0.0F);
		}
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	private boolean bNeedSetUp;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private Point3d tmpP;
	private Vector3d tmpV;
	private static final float speedometerScale[] = { 0.0F, 0.0F, 10.5F, 42.5F, 85F, 125F, 165.5F, 181F, 198F, 214.5F, 231F, 249F, 266.5F, 287.5F, 308F, 326.5F, 346F };

	static {
		Property.set(CLASS.THIS(), "normZNs", new float[] { 1.0F, 1.0F, 0.86F, 0.8F });
	}

}
