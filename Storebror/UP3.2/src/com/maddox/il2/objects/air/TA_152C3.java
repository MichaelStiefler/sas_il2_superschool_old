package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class TA_152C3 extends FW_190 implements TypeX4Carrier, TypeStormovik {

	public TA_152C3() {
		this.bToFire = false;
		this.tX4Prev = 0L;
		this.kangle = 0.0F;
		this.deltaAzimuth = 0.0F;
		this.deltaTangage = 0.0F;
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -77F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -77F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -102F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -102F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 20F * f, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
		float f1 = Math.max(-f * 1500F, -94F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		if (this.FM.CT.getGear() < 0.98F) {
			return;
		} else {
			this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
			return;
		}
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.44F, 0.0F, 0.44F);
		this.hierMesh().chunkSetLocate("GearL2a_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.44F, 0.0F, 0.44F);
		this.hierMesh().chunkSetLocate("GearR2a_D0", Aircraft.xyz, Aircraft.ypr);
	}

	protected void moveFlap(float f) {
		float f1 = -50F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -f1, 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode() || !flag || !(this.FM instanceof Pilot)) { return; }
		Pilot pilot = (Pilot) this.FM;
		if (pilot.get_maneuver() == 63 && pilot.target != null) {
			Point3d point3d = new Point3d(pilot.target.Loc);
			point3d.sub(this.FM.Loc);
			this.FM.Or.transformInv(point3d);
			if ((point3d.x > 4000D && point3d.x < 5500D || point3d.x > 100D && point3d.x < 5000D && World.Rnd().nextFloat() < 0.33F) && Time.current() > this.tX4Prev + 10000L) {
				this.bToFire = true;
				this.tX4Prev = Time.current();
			}
		}
	}

	public void update(float f) {
		for (int i = 1; i < 15; i++) {
			this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -10F * this.kangle, 0.0F);
		}

		this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		super.update(f);
	}

	public void typeX4CAdjSidePlus() {
		this.deltaAzimuth = 1.0F;
	}

	public void typeX4CAdjSideMinus() {
		this.deltaAzimuth = -1F;
	}

	public void typeX4CAdjAttitudePlus() {
		this.deltaTangage = 1.0F;
	}

	public void typeX4CAdjAttitudeMinus() {
		this.deltaTangage = -1F;
	}

	public void typeX4CResetControls() {
		this.deltaAzimuth = this.deltaTangage = 0.0F;
	}

	public float typeX4CgetdeltaAzimuth() {
		return this.deltaAzimuth;
	}

	public float typeX4CgetdeltaTangage() {
		return this.deltaTangage;
	}

	public boolean bToFire;
	private long   tX4Prev;
	private float  kangle;
	private float  deltaAzimuth;
	private float  deltaTangage;

	static {
		Class class1 = TA_152C3.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ta.152");
		Property.set(class1, "meshName", "3DO/Plane/Ta-152C3/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1944.6F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Ta-152C3.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitTA_152C3.class });
		Property.set(class1, "LOSElevation", 0.755F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1, 1, 1, 1, 9, 9, 2, 2, 2, 2, 3, 3, 9, 9, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalBomb02",
				"_ExternalBomb03", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb04" });
	}
}
