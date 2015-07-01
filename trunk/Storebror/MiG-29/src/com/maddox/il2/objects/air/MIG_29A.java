package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;

public class MIG_29A extends MIG_29 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane {

	public MIG_29A() {
		guidedMissileUtils = null;
		fxSirena = newSound("aircraft.F4warning", false);
		smplSirena = new Sample("sample.F4warning.wav", 256, 65535);
		sirenaSoundPlaying = false;
		hasChaff = false;
		hasFlare = false;
		lastChaffDeployed = 0L;
		lastFlareDeployed = 0L;
		lastCommonThreatActive = 0L;
		intervalCommonThreat = 1000L;
		lastRadarLockThreatActive = 0L;
		intervalRadarLockThreat = 1000L;
		lastMissileLaunchThreatActive = 0L;
		intervalMissileLaunchThreat = 1000L;
		guidedMissileUtils = new GuidedMissileUtils(this);
		removeChuteTimer = -1L;
		smplSirena.setInfinite(true);
		dynamoOrient = 0.0F;
		g1 = null;
		engineSFX = null;
		engineSTimer = 0x98967f;
		myArmy = getArmy();
	}

	public float getFlowRate() {
		return FlowRate;
	}

	public float getFuelReserve() {
		return FuelReserve;
	}

	public void moveArrestorHook(float f) {
		hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 70F * f);
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 85F), 0.0F);
		hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -85F), 0.0F);
	}

	public void moveWingFold(float f) {
		if (f < 0.001F) {
			setGunPodsOn(true);
			hideWingWeapons(false);
		} else {
			setGunPodsOn(false);
			((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
			hideWingWeapons(false);
		}
		moveWingFold(hierMesh(), f);
	}

	public long getChaffDeployed() {
		if (hasChaff)
			return lastChaffDeployed;
		else
			return 0L;
	}

	public long getFlareDeployed() {
		if (hasFlare)
			return lastFlareDeployed;
		else
			return 0L;
	}

	public void setCommonThreatActive() {
		long l = Time.current();
		if (l - lastCommonThreatActive > intervalCommonThreat) {
			lastCommonThreatActive = l;
			doDealCommonThreat();
		}
	}

	public void setRadarLockThreatActive() {
		long l = Time.current();
		if (l - lastRadarLockThreatActive > intervalRadarLockThreat) {
			lastRadarLockThreatActive = l;
			doDealRadarLockThreat();
		}
	}

	public void setMissileLaunchThreatActive() {
		long l = Time.current();
		if (l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat) {
			lastMissileLaunchThreatActive = l;
			doDealMissileLaunchThreat();
		}
	}

	private void doDealCommonThreat() {
	}

	private void doDealRadarLockThreat() {
	}

	private void doDealMissileLaunchThreat() {
	}

	public static final double Rnd(double d, double d1) {
		return World.Rnd().nextDouble(d, d1);
	}

	public static final float Rnd(float f, float f1) {
		return World.Rnd().nextFloat(f, f1);
	}

	public void rareAction() {
		Point3d point3d = new Point3d();
		pos.getAbs(point3d);
		Vector3d vector3d = new Vector3d();
		Aircraft aircraft = World.getPlayerAircraft();
		double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x;
		double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y;
		double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().z;
		int i = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
		if (i < 0)
			i = 360 + i;
		int j = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
		if (j < 0)
			j = 360 + j;
		boolean flag1 = false;
		float f = 100F;
		do {
			List list = Engine.targets();
			int k = list.size();
			for (int l = 0; l < k; l++) {
				Actor actor = (Actor) list.get(l);
				if ((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != myArmy) {
					double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
					double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
					double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
					double d6 = d3 - d;
					double d7 = d4 - d1;
					double d8 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
					float f1 = 57.32484F * (float) Math.atan2(d7, -d6);
					int i1 = (int) (Math.floor((int) f1) - 90D);
					if (i1 < 0)
						i1 = 360 + i1;
					int j1 = i1 - i;
					double d9 = d - d3;
					double d10 = d1 - d4;
					double d11 = Math.sqrt(d8 * d8);
					int k1 = (int) (Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
					float f2 = 57.32484F * (float) Math.atan2(k1, d11);
					int l1 = (int) (Math.floor((int) f2) - 90D);
					if (l1 < 0)
						l1 = 360 + l1;
					int i2 = l1 - j;
					if ((float) k1 <= f && (double) k1 >= 120D && i2 >= 235 && i2 <= 305 && Math.sqrt(j1 * j1) <= 60D && actor.getSpeed(vector3d) > 20D) {
						String s = "level with us";
						if (d2 - d5 - 200D >= 0.0D)
							s = "below us";
						if ((d2 - d5) + 200D < 0.0D)
							s = "above us";
						int j2 = k1;
						String s1 = "m";
						if (j2 >= 1000) {
							j2 = (int) Math.floor(j2 / 1000);
							s1 = "km";
						}
						HUD.logCenter("                                          WSO: Radar Contact Bearing " + i1 + "\260" + ", Range " + j2 + s1 + ", " + s);
						flag1 = true;
					}
				}
				f += 100F;
			}

		} while (!flag1 && f <= 5000F);
	}

	private boolean sirenaWarning() {
		Point3d point3d = new Point3d();
		super.pos.getAbs(point3d);
		Vector3d vector3d = new Vector3d();
		Aircraft aircraft = World.getPlayerAircraft();
		if (World.getPlayerAircraft() == null)
			return false;
		double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
		double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
		double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
		int i = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
		if (i < 0)
			i += 360;
		int j = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
		if (j < 0)
			j += 360;
		Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
		if ((aircraft1 instanceof Aircraft) && aircraft1.getArmy() != World.getPlayerArmy() && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && aircraft1 != World.getPlayerAircraft()
				&& aircraft1.getSpeed(vector3d) > 20D) {
			super.pos.getAbs(point3d);
			double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
			double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
			double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
			new String();
			new String();
			double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
			new String();
			double d7 = d3 - d;
			double d8 = d4 - d1;
			float f = 57.32484F * (float) Math.atan2(d8, -d7);
			int k = (int) (Math.floor((int) f) - 90D);
			if (k < 0)
				k += 360;
			int l = k - i;
			double d9 = d - d3;
			double d10 = d1 - d4;
			double d11 = Math.sqrt(d6 * d6);
			int i1 = (int) (Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
			float f1 = 57.32484F * (float) Math.atan2(i1, d11);
			int j1 = (int) (Math.floor((int) f1) - 90D);
			if (j1 < 0)
				j1 += 360;
			int k1 = j1 - j;
			int l1 = (int) (Math.ceil(((double) i1 * 3.2808399000000001D) / 100D) * 100D);
			if (l1 >= 5280)
				l1 = (int) Math.floor(l1 / 5280);
			bRadarWarning = (double) i1 <= 3000D && (double) i1 >= 50D && k1 >= 195 && k1 <= 345 && Math.sqrt(l * l) >= 120D;
			playSirenaWarning(bRadarWarning);
		} else {
			bRadarWarning = false;
			playSirenaWarning(bRadarWarning);
		}
		return true;
	}

	public void playSirenaWarning(boolean flag) {
		if (flag && !sirenaSoundPlaying) {
			fxSirena.play(smplSirena);
			sirenaSoundPlaying = true;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "AN/APR-36: Enemy at Six!");
		} else if (!flag && sirenaSoundPlaying) {
			fxSirena.cancel();
			sirenaSoundPlaying = false;
		}
	}

	public void getGFactors(TypeGSuit.GFactors gfactors) {
		gfactors.setGFactors(1.0F, 1.0F, 1.0F, 1.8F, 1.5F, 1.0F);
	}

	public GuidedMissileUtils getGuidedMissileUtils() {
		return guidedMissileUtils;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		guidedMissileUtils.onAircraftLoaded();
		FM.Skill = 3;
		((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
		bHasDeployedDragChute = false;
		if (((FlightModelMain) (super.FM)).CT.Weapons[0] != null)
			g1 = ((FlightModelMain) (super.FM)).CT.Weapons[0][0];
		if (thisWeaponsName.startsWith("S1"))
			hierMesh().chunkVisible("6x20mm_C", true);
		if (thisWeaponsName.startsWith("S2")) {
			hierMesh().chunkVisible("6x20mm_L", true);
			hierMesh().chunkVisible("6x20mm_R", true);
		}
		if (thisWeaponsName.startsWith("S3")) {
			hierMesh().chunkVisible("6x20mm_C", true);
			hierMesh().chunkVisible("6x20mm_L", true);
			hierMesh().chunkVisible("6x20mm_R", true);
		}
		if (thisWeaponsName.endsWith("P1")) {
			hierMesh().chunkVisible("PylonSC", false);
			hierMesh().chunkVisible("PylonSL", false);
			hierMesh().chunkVisible("PylonSR", false);
		}
		if (thisWeaponsName.endsWith("P2")) {
			hierMesh().chunkVisible("PylonSL", false);
			hierMesh().chunkVisible("PylonSR", false);
		}
		if (thisWeaponsName.endsWith("P3"))
			hierMesh().chunkVisible("PylonSC", false);
		if (thisWeaponsName.endsWith("P4")) {
			hierMesh().chunkVisible("PylonSL", false);
			hierMesh().chunkVisible("PylonSR", false);
			hierMesh().chunkVisible("PylonSC", false);
			hierMesh().chunkVisible("PylonML", false);
			hierMesh().chunkVisible("PylonMR", false);
		}
		if (thisWeaponsName.startsWith("S1B")) {
			hierMesh().chunkVisible("6x20mm_C", true);
			hierMesh().chunkVisible("PylonTERLI", true);
			hierMesh().chunkVisible("PylonTERRI", true);
		}
		if (thisWeaponsName.startsWith("S2B")) {
			hierMesh().chunkVisible("6x20mm_L", true);
			hierMesh().chunkVisible("6x20mm_R", true);
			hierMesh().chunkVisible("PylonTERLI", true);
			hierMesh().chunkVisible("PylonTERRI", true);
		}
		if (thisWeaponsName.startsWith("S3B")) {
			hierMesh().chunkVisible("6x20mm_C", true);
			hierMesh().chunkVisible("6x20mm_L", true);
			hierMesh().chunkVisible("6x20mm_R", true);
			hierMesh().chunkVisible("PylonTERLI", true);
			hierMesh().chunkVisible("PylonTERRI", true);
		}
		if (thisWeaponsName.startsWith("B2S")) {
			hierMesh().chunkVisible("6x20mm_C", true);
			hierMesh().chunkVisible("PylonTERLI", true);
			hierMesh().chunkVisible("PylonTERRI", true);
			hierMesh().chunkVisible("PylonTERLO", true);
			hierMesh().chunkVisible("PylonTERRO", true);
		}
		if (thisWeaponsName.startsWith("B1")) {
			hierMesh().chunkVisible("PylonTERLI", true);
			hierMesh().chunkVisible("PylonTERRI", true);
		}
		if (thisWeaponsName.startsWith("B3")) {
			hierMesh().chunkVisible("PylonTERLO", true);
			hierMesh().chunkVisible("PylonTERRO", true);
		}
		if (thisWeaponsName.startsWith("M1")) {
			hierMesh().chunkVisible("PylonMERC", true);
			hierMesh().chunkVisible("PylonMERL", true);
			hierMesh().chunkVisible("PylonMERR", true);
			hierMesh().chunkVisible("PylonTERLI", true);
			hierMesh().chunkVisible("PylonTERRI", true);
		}
		if (thisWeaponsName.startsWith("M2")) {
			hierMesh().chunkVisible("PylonMERC", true);
			hierMesh().chunkVisible("PylonTERLI", true);
			hierMesh().chunkVisible("PylonTERRI", true);
		}
		if (thisWeaponsName.startsWith("B2")) {
			hierMesh().chunkVisible("PylonTERLO", true);
			hierMesh().chunkVisible("PylonTERRO", true);
			hierMesh().chunkVisible("PylonTERLI", true);
			hierMesh().chunkVisible("PylonTERRI", true);
		} else {
			return;
		}
	}

	public void update(float f) {
		guidedMissileUtils.update();
		sirenaWarning();
		if (FM.CT.getArrestor() > 0.2F)
			if (FM.Gears.arrestorVAngle != 0.0F) {
				float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
				arrestor = 0.8F * arrestor + 0.2F * f1;
				moveArrestorHook(arrestor);
			} else {
				float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
				if (f2 < 0.0F && FM.getSpeedKMH() > 60F)
					Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
				if (f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
					f2 = 0.0F;
				if (f2 > 0.2F)
					f2 = 0.2F;
				if (f2 > 0.0F)
					arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
				else
					arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
				if (arrestor < 0.0F)
					arrestor = 0.0F;
				else if (arrestor > 1.0F)
					arrestor = 1.0F;
				moveArrestorHook(arrestor);
			}
		super.update(f);
		if (((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute) {
			chute = new Chute(this);
			chute.setMesh("3do/plane/ChuteF86/mono.sim");
			chute.collide(true);
			chute.mesh().setScale(0.8F);
			((Actor) (chute)).pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
			bHasDeployedDragChute = true;
		} else if (bHasDeployedDragChute && ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
			if (((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() > 600F || ((FlightModelMain) (super.FM)).CT.DragChuteControl < 1.0F) {
				if (chute != null) {
					chute.tangleChute(this);
					((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
				}
				((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
				((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
				((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
				removeChuteTimer = Time.current() + 250L;
			} else if (((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() < 20F) {
				if (chute != null)
					chute.tangleChute(this);
				((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
				((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
				((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
				((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
				removeChuteTimer = Time.current() + 10000L;
			}
		if (removeChuteTimer > 0L && !((FlightModelMain) (super.FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
			chute.destroy();
		if (super.FM.getSpeed() > 5F) {
			hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 14.5F), 0.0F);
			hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -14.5F), 0.0F);
			hierMesh().chunkSetAngles("SlatL_Out", 0.0F, 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
			hierMesh().chunkSetAngles("SlatR_Out", 0.0F, 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
		}
		if (((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 29000D;
		if (((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 29000D;
		if (super.FM.getAltitude() > 0.0F && (double) calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 10000D;
		if (super.FM.getAltitude() > 0.0F && (double) calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 10000D;
		if (super.FM.getAltitude() > 0.0F && (double) calculateMach() >= 0.97999999999999998D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 10000D;
		if (super.FM.getAltitude() > 0.0F && (double) calculateMach() >= 0.97999999999999998D && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 10000D;
		if (super.FM.getAltitude() > 500F && (double) calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 7000D;
		if (super.FM.getAltitude() > 500F && (double) calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 7000D;
		if (super.FM.getAltitude() > 1000F && (double) calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 1500D;
		if (super.FM.getAltitude() > 1000F && (double) calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 1500D;
		if (super.FM.getAltitude() > 1500F && (double) calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 1500D;
		if (super.FM.getAltitude() > 1500F && (double) calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 1500D;
		if (super.FM.getAltitude() > 10000F && (double) calculateMach() >= 2.23D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 10000F && (double) calculateMach() >= 2.23D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 11000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 4000D;
		if (super.FM.getAltitude() > 11000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 4000D;
		if (super.FM.getAltitude() > 12500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
		if (super.FM.getAltitude() > 12500F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
		if (super.FM.getAltitude() > 14000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
		if (super.FM.getAltitude() > 14000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
		if (super.FM.getAltitude() > 15000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
		if (super.FM.getAltitude() > 15000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
		if (super.FM.getAltitude() > 15500F && (double) calculateMach() >= 2.0099999999999998D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 15500F && (double) calculateMach() >= 2.0099999999999998D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 16000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
		if (super.FM.getAltitude() > 16000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
		if (super.FM.getAltitude() > 16000F && (double) calculateMach() >= 1.9199999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 16000F && (double) calculateMach() >= 1.9199999999999999D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 17000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
		if (super.FM.getAltitude() > 17000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
		if (super.FM.getAltitude() > 17000F && (double) calculateMach() >= 1.8200000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 17000F && (double) calculateMach() >= 1.8200000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 17500F && (double) calculateMach() >= 1.73D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 17500F && (double) calculateMach() >= 1.73D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 18000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
		if (super.FM.getAltitude() > 18000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
		if (super.FM.getAltitude() > 18000F && (double) calculateMach() >= 1.5900000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 18000F && (double) calculateMach() >= 1.5900000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 18500F && (double) calculateMach() >= 1.5800000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 18500F && (double) calculateMach() >= 1.5800000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 18800F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
		if (super.FM.getAltitude() > 18800F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
		if (super.FM.getAltitude() > 19000F && (double) calculateMach() >= 1.52D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 19000F && (double) calculateMach() >= 1.52D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if (super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 7000D;
		if (super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 7000D;
		if (super.FM.getAltitude() > 21000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 17000D;
		if (super.FM.getAltitude() > 21000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 17000D;
	}

	public void moveFan(float f) {
		if (g1 != null && g1.isShots() && oldbullets != g1.countBullets()) {
			oldbullets = g1.countBullets();
			if (dynamoOrient == 360F)
				dynamoOrient = 0.0F;
			dynamoOrient = dynamoOrient + 30F;
			hierMesh().chunkSetAngles("6x20mm_C", 0.0F, dynamoOrient, 0.0F);
			hierMesh().chunkSetAngles("6x20mm_L", 0.0F, dynamoOrient, 0.0F);
			hierMesh().chunkSetAngles("6x20mm_R", 0.0F, dynamoOrient, 0.0F);
		}
		super.moveFan(f);
	}

	static Class _mthclass$(String s) {
		try {
			return Class.forName(s);
		} catch (ClassNotFoundException classnotfoundexception) {
			throw new NoClassDefFoundError(classnotfoundexception.getMessage());
		}
	}

	private GuidedMissileUtils guidedMissileUtils;
	private SoundFX fxSirena;
	private Sample smplSirena;
	private boolean sirenaSoundPlaying;
	private boolean bRadarWarning;
	private boolean hasChaff;
	private boolean hasFlare;
	private long lastChaffDeployed;
	private long lastFlareDeployed;
	private long lastCommonThreatActive;
	private long intervalCommonThreat;
	private long lastRadarLockThreatActive;
	private long intervalRadarLockThreat;
	private long lastMissileLaunchThreatActive;
	private long intervalMissileLaunchThreat;
	public static float FlowRate = 10F;
	public static float FuelReserve = 1500F;
	public boolean bToFire;
	private boolean bHasDeployedDragChute;
	private Chute chute;
	private long removeChuteTimer;
	private float arrestor;
	private BulletEmitter g1;
	private int oldbullets;
	private float dynamoOrient;
	protected SoundFX engineSFX;
	protected int engineSTimer;
	private int myArmy;

	static {
		Class class1 = MIG_29A.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "MiG_29");
		Property.set(class1, "meshName", "3DO/Plane/MIG-29A/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1965F);
		Property.set(class1, "yearExpired", 1990F);
		Property.set(class1, "FlightModel", "FlightModels/MIG29A.fmd:MIG29");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMIG29.class });
		Property.set(class1, "LOSElevation", 0.965F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_Flare01", "_Flare02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08",
				"_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalDev01" });
		String s = "undefined";
		try {
			ArrayList arraylist = new ArrayList();
			Property.set(class1, "weaponsList", arraylist);
			HashMapInt hashmapint = new HashMapInt();
			Property.set(class1, "weaponsMap", hashmapint);
			byte byte0 = 17;
			Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
			s = "Default_P4";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
			a_lweaponslot[1] = null;
			a_lweaponslot[2] = null;
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xR-27R+4xR-60M";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
			a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunR27R", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunR27R", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "RocketGunR27R", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xR-27R+4xR-73";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunR73", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunR73", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR73", 1);
			a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR73", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunR27R", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunR27R", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "RocketGunR27R", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "None";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = null;
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
		} catch (Exception exception) {
			System.out.println("MiG-29A Weapon Declaration error at " + s);
			exception.printStackTrace();
		}
	}
}
