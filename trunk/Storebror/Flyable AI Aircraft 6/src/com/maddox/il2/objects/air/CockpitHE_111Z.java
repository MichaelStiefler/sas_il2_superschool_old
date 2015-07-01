// This file is part of the SAS IL-2 Sturmovik 1946 4.13
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
// Last Edited on: 2014/10/28

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitHE_111Z extends CockpitPilot {
	private class Variables {

		float altimeter;
		float throttlel;
		float throttler;
		float azimuth;
		float waypointAzimuth;
		float blindDotX;
		float blindDotY;
		float vspeed;

		private Variables() {
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			setNew.altimeter = fm.getAltitude();
			setNew.throttlel = (10F * setOld.throttlel + fm.EI.engines[0].getControlThrottle()) / 11F;
			setNew.throttler = (10F * setOld.throttler + fm.EI.engines[1].getControlThrottle()) / 11F;
			setNew.azimuth = fm.Or.getYaw();
			if (setOld.azimuth > 270F && setNew.azimuth < 90F)
				setOld.azimuth -= 360F;
			if (setOld.azimuth < 90F && setNew.azimuth > 270F)
				setOld.azimuth += 360F;
			setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
			setNew.vspeed = (499F * setOld.vspeed + fm.getVertSpeed()) / 500F;
			if (fm.isTick(12, 0)) {
				Airport airport = Airport.nearest(fm.Loc, -1, 1);
				if (airport != null && airport.nearestRunway(fm.Loc, tmpLoc)) {
					tmpLoc.transformInv(fm.Loc, tmpP);
					tmpV.x = tmpP.x;
					tmpV.y = tmpP.y;
					tmpV.z = tmpP.z;
					tmpV.normalize();
					tmpV.z -= 0.034899000078439713D;
					if (tmpV.x > 0.95999997854232788D) {
						if (tmpV.y > 0.0D)
							setNew.blindDotX = 1.889F * (float) Math.sqrt(tmpV.y);
						else
							setNew.blindDotX = -1.889F * (float) Math.sqrt(-tmpV.y);
						if (tmpV.z > 0.0D)
							setNew.blindDotY = 1.889F * (float) Math.sqrt(tmpV.z);
						else
							setNew.blindDotY = -1.889F * (float) Math.sqrt(-tmpV.z);
					} else {
						setNew.blindDotX = 0.0F;
						setNew.blindDotY = 0.0F;
					}
				}
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected float waypointAzimuth() {
		WayPoint waypoint = fm.AP.way.curr();
		if (waypoint == null) {
			return 0.0F;
		} else {
			waypoint.getP(tmpP);
			tmpV.sub(tmpP, fm.Loc);
			return (float) (57.295779513082323D * Math.atan2(tmpV.y, tmpV.x));
		}
	}

	public CockpitHE_111Z() {
		super("3DO/Cockpit/He-111H-6/CockpitHE111Z.him", "he111");
		setOld = new Variables();
		setNew = new Variables();
		tmpLoc = new Loc();
		tmpP = new Point3d();
		tmpV = new Vector3d();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light1 = new LightPointActor(new LightPoint(), loc.getPoint());
		light1.light.setColor(218F, 143F, 128F);
		light1.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK1", light1);
		cockpitNightMats = (new String[] { "clocks1", "clocks2", "clocks2DMG", "clocks3", "clocks3DMG", "clocks4", "clocks5", "clocks6" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		if (fm.isTick(44, 0)) {
			if ((fm.AS.astateCockpitState & 8) == 0) {
				mesh.chunkVisible("Z_GearLRed1", fm.CT.getGear() == 0.0F || fm.Gears.isAnyDamaged());
				mesh.chunkVisible("Z_GearRRed1", fm.CT.getGear() == 0.0F || fm.Gears.isAnyDamaged());
				mesh.chunkVisible("Z_GearLGreen1", fm.CT.getGear() == 1.0F && fm.Gears.lgear);
				mesh.chunkVisible("Z_GearRGreen1", fm.CT.getGear() == 1.0F && fm.Gears.rgear);
			} else {
				mesh.chunkVisible("Z_GearLRed1", false);
				mesh.chunkVisible("Z_GearRRed1", false);
				mesh.chunkVisible("Z_GearLGreen1", false);
				mesh.chunkVisible("Z_GearRGreen1", false);
			}
			if ((fm.AS.astateCockpitState & 0x40) == 0) {
				mesh.chunkVisible("zFuelWarning1", fm.M.fuel < 600F);
				mesh.chunkVisible("zFuelWarning2", fm.M.fuel < 600F);
			}
		}
		mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl));
		mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl));
		if (fm.CT.getRudder() > 0.0F) {
			mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -10F * fm.CT.getRudder());
			mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -45F * fm.CT.getRudder());
		} else {
			mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -45F * fm.CT.getRudder());
			mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -10F * fm.CT.getRudder());
		}
		mesh.chunkSetAngles("zTurretA", 0.0F, fm.turret[0].tu[0], 0.0F);
		mesh.chunkSetAngles("zTurretB", 0.0F, fm.turret[0].tu[1], 0.0F);
		mesh.chunkSetAngles("zOilFlap1", 0.0F, 0.0F, -50F * fm.EI.engines[0].getControlRadiator());
		mesh.chunkSetAngles("zOilFlap2", 0.0F, 0.0F, -50F * fm.EI.engines[1].getControlRadiator());
		mesh.chunkSetAngles("zMix1", 0.0F, 0.0F, -30F * fm.EI.engines[0].getControlMix());
		mesh.chunkSetAngles("zMix2", 0.0F, 0.0F, -30F * fm.EI.engines[1].getControlMix());
		mesh.chunkSetAngles("zFlaps1", 0.0F, 0.0F, -45F * fm.CT.FlapsControl);
		if (fm.EI.engines[0].getControlProp() >= 0.0F)
			mesh.chunkSetAngles("zPitch1", 0.0F, 0.0F, -65F * fm.EI.engines[0].getControlProp());
		if (fm.EI.engines[1].getControlProp() >= 0.0F)
			mesh.chunkSetAngles("zPitch2", 0.0F, 0.0F, -65F * fm.EI.engines[1].getControlProp());
		mesh.chunkSetAngles("zThrottle1", 0.0F, 0.0F, -33.6F * interp(setNew.throttlel, setOld.throttlel, f));
		mesh.chunkSetAngles("zThrottle2", 0.0F, 0.0F, -33.6F * interp(setNew.throttler, setOld.throttler, f));
		mesh.chunkSetAngles("zCompressor1", 0.0F, 0.0F, -25F * (float) fm.EI.engines[0].getControlCompressor());
		mesh.chunkSetAngles("zCompressor2", 0.0F, 0.0F, -25F * (float) fm.EI.engines[1].getControlCompressor());
		mesh.chunkSetAngles("zHour", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zMinute", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zSecond", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zAH1", 0.0F, 0.0F, fm.Or.getKren());
		mesh.chunkSetAngles("zAH2", 0.0F, 0.0F, cvt(fm.Or.getTangage(), -30F, 30F, -6.5F, 6.5F));
		float f1;
		if (aircraft().isFMTrackMirror()) {
			f1 = aircraft().fmTrack().getCockpitAzimuthSpeed();
		} else {
			f1 = cvt((setNew.azimuth - setOld.azimuth) / Time.tickLenFs(), -3F, 3F, 30F, -30F);
			if (aircraft().fmTrack() != null)
				aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
		}
		mesh.chunkSetAngles("zTurnBank", f1, 0.0F, 0.0F);
		float f2 = getBall(4.5D);
		mesh.chunkSetAngles("zBall", cvt(f2, -4F, 4F, -8F, 8F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zBall2", cvt(f2, -4.5F, 4.5F, -9F, 9F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zVSI", cvt(setNew.vspeed, -15F, 15F, -160F, 160F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zSpeed", floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("zAlt1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zAlt2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zRepeater", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zCompass", -interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zNavP", waypointAzimuth() - setNew.azimuth, 0.0F, 0.0F);
		mesh.chunkSetAngles("zBlip1", 0.0F, 3F * setNew.blindDotY, 3F * setNew.blindDotX);
		if (Math.abs(fm.Or.getKren()) < 20F && Math.abs(fm.Or.getTangage()) < 20F)
			mesh.chunkSetAngles("zMagnetic", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zRPM1", floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("zRPM2", floatindex(cvt(fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("zBoost1", cvt(fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zBoost2", cvt(fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOilTemp1", cvt(fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 130F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOilTemp2", cvt(fm.EI.engines[1].tOilOut, 0.0F, 100F, 0.0F, 130F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zCoolant1", cvt(fm.EI.engines[0].tWaterOut, 0.0F, 100F, 0.0F, 126F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zCoolant2", cvt(fm.EI.engines[1].tWaterOut, 0.0F, 100F, 0.0F, 126F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP1-1", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP1-2", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP1-3", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP1-4", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP2-1", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP2-2", cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP2-3", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP2-4", cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zFuel1", cvt(fm.M.fuel / 0.72F, 0.0F, 2000F, 0.0F, 140F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zFuel2", cvt(fm.M.fuel / 0.72F, 0.0F, 2000F, 0.0F, 140F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zExtT", cvt(Atmosphere.temperature((float) fm.Loc.z), 223.09F, 323.09F, -145F, 145F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zPatin", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
		float f3 = (float) Math.toDegrees(fm.EI.engines[0].getPropPhi() - fm.EI.engines[0].getPropPhiMin());
		f3 = (float) (int) (-f3 / 0.2F) * 0.2F;
		mesh.chunkSetAngles("zProp1-1", f3 * 60F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zProp1-2", f3 * 5F, 0.0F, 0.0F);
		f3 = (float) Math.toDegrees(fm.EI.engines[1].getPropPhi() - fm.EI.engines[0].getPropPhiMin());
		f3 = (float) (int) (-f3 / 0.2F) * 0.2F;
		mesh.chunkSetAngles("zProp2-1", f3 * 60F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zProp2-2", f3 * 5F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zFlapsIL", 145F * fm.CT.getFlap(), 0.0F, 0.0F);
		mesh.chunkSetAngles("zFlapsIR", 145F * fm.CT.getFlap(), 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("ZHolesL_D1", true);
			mesh.chunkVisible("PanelL_D1", true);
			mesh.chunkVisible("PanelL_D0", false);
			mesh.chunkVisible("zVSI", false);
			mesh.chunkVisible("zBlip1", false);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("ZHolesL_D2", true);
			mesh.chunkVisible("PanelFloat_D1", true);
			mesh.chunkVisible("PanelFloat_D0", false);
			mesh.chunkVisible("zProp1-1", false);
			mesh.chunkVisible("zProp1-2", false);
			mesh.chunkVisible("zProp2-1", false);
			mesh.chunkVisible("zProp2-2", false);
			mesh.chunkVisible("zFlapsIL", false);
			mesh.chunkVisible("zFlapsIR", false);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("ZHolesR_D1", true);
			mesh.chunkVisible("PanelR_D1", true);
			mesh.chunkVisible("PanelR_D0", false);
			mesh.chunkVisible("zRPM1", false);
			mesh.chunkVisible("zBoost2", false);
			mesh.chunkVisible("zOilTemp2", false);
			mesh.chunkVisible("zCoolant1", false);
			mesh.chunkVisible("zOFP1-1", false);
			mesh.chunkVisible("zOFP1-2", false);
			mesh.chunkVisible("zFlapsIR", false);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("ZHolesR_D2", true);
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("ZHolesF_D1", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("PanelT_D1", true);
			mesh.chunkVisible("PanelT_D0", false);
			mesh.chunkVisible("zFuel2", false);
			mesh.chunkVisible("zOFP1-3", false);
			mesh.chunkVisible("zOFP1-4", false);
		}
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("zOil_D1", true);
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl) {
			light1.light.setEmit(0.0032F, 7.2F);
			setNightMats(true);
		} else {
			light1.light.setEmit(0.0F, 0.0F);
			setNightMats(false);
		}
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	private LightPointActor light1;
	private Loc tmpLoc;
	private Point3d tmpP;
	private Vector3d tmpV;
	private float pictAiler;
	private float pictElev;
	private static final float speedometerScale[] = { 0.0F, 0.1F, 19F, 37.25F, 63.5F, 91.5F, 112F, 135.5F, 159.5F, 186.5F, 213F, 238F, 264F, 289F, 314.5F, 339.5F, 359.5F, 360F, 360F, 360F };
	private static final float rpmScale[] = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };

}
