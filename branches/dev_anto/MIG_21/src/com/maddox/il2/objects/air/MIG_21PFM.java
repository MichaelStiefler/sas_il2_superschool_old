package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class MIG_21PFM extends MIG_21
implements TypeGuidedMissileCarrier,TypeCountermeasure,
TypeThreatDetector {

	private GuidedMissileUtils guidedMissileUtils = null;

	public MIG_21PFM()
	{
		guidedMissileUtils = new GuidedMissileUtils(this);
	}

	// <editor-fold defaultstate="collapsed" desc="TypeGuidedMissileCarrier Implementation">

	public GuidedMissileUtils getGuidedMissileUtils() {
		return this.guidedMissileUtils;
	}

	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Countermeasures">
	private boolean hasChaff = false;     // Aircraft is equipped with Chaffs yes/no
	private boolean hasFlare = false;     // Aircraft is equipped with Flares yes/no
	private long lastChaffDeployed = 0L;  // Last Time when Chaffs have been deployed
	private long lastFlareDeployed = 0L;  // Last Time when Flares have been deployed

	public long getChaffDeployed() {
		if (this.hasChaff) {
			return this.lastChaffDeployed;
		}
		return 0L;
	}

	public long getFlareDeployed() {
		if (this.hasFlare) {
			return this.lastFlareDeployed;
		}
		return 0L;
	}// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Threat Detection">
	private long lastCommonThreatActive = 0L;         // Last Time when a common threat was reported
	private long intervalCommonThreat = 1000L;        // Interval (milliseconds) at which common threats should be dealt with (i.e. duration of warning sound / light)
	private long lastRadarLockThreatActive = 0L;      // Last Time when a radar lock threat was reported
	private long intervalRadarLockThreat = 1000L;     // Interval (milliseconds) at which radar lock threats should be dealt with (i.e. duration of warning sound / light)
	private long lastMissileLaunchThreatActive = 0L;  // Last Time when a missile launch threat was reported
	private long intervalMissileLaunchThreat = 1000L; // Interval (milliseconds) at which missile launch threats should be dealt with (i.e. duration of warning sound / light)

	public void setCommonThreatActive() {
		long curTime = Time.current();
		if ((curTime - this.lastCommonThreatActive) > this.intervalCommonThreat) {
			this.lastCommonThreatActive = curTime;
			this.doDealCommonThreat();
		}
	}

	public void setRadarLockThreatActive() {
		long curTime = Time.current();
		if ((curTime - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
			this.lastRadarLockThreatActive = curTime;
			this.doDealRadarLockThreat();
		}
	}

	public void setMissileLaunchThreatActive() {
		long curTime = Time.current();
		if ((curTime - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
			this.lastMissileLaunchThreatActive = curTime;
			this.doDealMissileLaunchThreat();
		}
	}

	private void doDealCommonThreat() {       // Must be filled with life for A/Cs capable of dealing with common Threats
		//HUD.log("THREATENED!!");
	}

	private void doDealRadarLockThreat() {    // Must be filled with life for A/Cs capable of dealing with radar lock Threats
	}

	private void doDealMissileLaunchThreat() {// Must be filled with life for A/Cs capable of dealing with missile launch Threats
	}// </editor-fold>

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		super.bHasSK1Seat = false;
		FM.CT.bHasDragChuteControl = true;
		bHasDeployedDragChute = false;
		this.guidedMissileUtils.onAircraftLoaded();
		FM.CT.bHasRefuelControl = true;
	}

	public void update(float f)
	{
		super.update(f);
		//TODO: Controls afterburner thrust and changes with altitude
		setAfterburner();
		if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
		{
			chute = new Chute(this);
			chute.setMesh("3do/plane/ChuteMiG21/mono.sim");
			chute.collide(true);
			chute.mesh().setScale(1F);
			chute.pos.setRel(new Point3d(-5.0D, 0.0D, 0.6D), new Orient(0.0F, 90F, 0.0F));
			bHasDeployedDragChute = true;
		}
		else if (bHasDeployedDragChute  && FM.CT.bHasDragChuteControl){
			if(((FM.CT.DragChuteControl == 1.0F && (FM.getSpeedKMH() > 600F))|| FM.CT.DragChuteControl < 1.0F))
			{
				if(chute != null)
				{
					chute.tangleChute(this);
					chute.pos.setRel(new Point3d(-10D, 0.0D, 1D), new Orient(0.0F, 80F, 0.0F));
				}
				FM.CT.DragChuteControl = 0.0F;
				FM.CT.bHasDragChuteControl = false;
				FM.Sq.dragChuteCx = 0F;
				removeChuteTimer = Time.current() + 250L;
			}
			else if((FM.CT.DragChuteControl == 1.0F && (FM.getSpeedKMH() < 20F)))
			{
				if(chute != null)
					chute.tangleChute(this);
				chute.pos.setRel(new Orient(0.0F, 100F, 0.0F));
				FM.CT.DragChuteControl = 0.0F;
				FM.CT.bHasDragChuteControl = false;
				FM.Sq.dragChuteCx = 0F;
				removeChuteTimer = Time.current() + 10000L;
			}
		}
		if(removeChuteTimer > 0L && !FM.CT.bHasDragChuteControl)
		{
			if(Time.current() > (removeChuteTimer))
			{
				chute.destroy();
			}
		}
		this.guidedMissileUtils.update();
	}

	protected void moveVarWing(float f)
	{
		hierMesh().chunkSetAngles("AirbrakeL", -45F * f, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("AirbrakeR", 45F * f, 0.0F, 0.0F);
	}

	private void setAfterburner(){
		//////TODO: !!!! IMPORTANT NOTE!!!! These parameters define afterburner function at altitude and are different for each version of MIG-21!
		/*
		if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6){
			FM.producedAF.x += 19500D;
		double slope = 0;
		double intercept = 0;
		int i;
		for(i = 0; i < AfbAlt.length; i++)
		{
			slope = (AfbAlt[i]-AfbAlt[i+1])/(AfbThr[i]-AfbThr[i+1]);
			intercept = AfbAlt[i] - slope*AfbThr[i];
		}
		HUD.log("Equation: y = "+slope+"x + "+intercept);
			////Celing limitation parameters
			if(super.FM.getAltitude() >= 1000F && super.FM.getAltitude() < 9000F)
				FM.producedAF.x += -0.4D*super.FM.getAltitude();
			if(super.FM.getAltitude() >= 9000F && super.FM.getAltitude() < 10000F)
				FM.producedAF.x += ((5.75D*super.FM.getAltitude())-55350D);
			if(super.FM.getAltitude() >= 10000F && super.FM.getAltitude() < 12000F)
				FM.producedAF.x += ((-1.25D*super.FM.getAltitude())+14650D);
			if(super.FM.getAltitude() >= 12000F && super.FM.getAltitude() < 14000F)
				FM.producedAF.x += ((-2.5D*super.FM.getAltitude())+29650D);
			if(super.FM.getAltitude() >= 14000F && super.FM.getAltitude() < 14500F)
				FM.producedAF.x += ((-5D*super.FM.getAltitude())+64650D);
			if(super.FM.getAltitude() >= 14500F && super.FM.getAltitude() < 15000F)
				FM.producedAF.x += ((-7.4D*super.FM.getAltitude())+99450D);
			if(super.FM.getAltitude() >= 15000F && super.FM.getAltitude() < 16000F)
				FM.producedAF.x += ((-3*super.FM.getAltitude())+33400D);
			if(super.FM.getAltitude() >= 16000F && super.FM.getAltitude() < 17000F)
				FM.producedAF.x += ((-3D*super.FM.getAltitude())+33400D);
			if(super.FM.getAltitude() >= 17000F && super.FM.getAltitude() < 18000F)
				FM.producedAF.x += ((-9D*super.FM.getAltitude())+135400D);
			if(super.FM.getAltitude() >= 18000F && super.FM.getAltitude() < 18500F)
				FM.producedAF.x += ((-5D*super.FM.getAltitude())+65400D);
			if(super.FM.getAltitude() >= 18500F)
				FM.producedAF.x += ((-8D*super.FM.getAltitude())+120900D); 
		 */
		if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 18500D;
		if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 1.03D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
		if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.96999999999999997D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 6000D;
		if(super.FM.getAltitude() > 1000F && (double)calculateMach() >= 1.03D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 4000D;
		if(super.FM.getAltitude() > 1500F && (double)calculateMach() >= 1.03D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 2000D;
		if(super.FM.getAltitude() > 2000F && (double)calculateMach() >= 1.03D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 2000D;
		if(super.FM.getAltitude() > 10500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x += 2500D;
		if(super.FM.getAltitude() > 11000F && (double)calculateMach() >= 2.0099999999999998D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
		if(super.FM.getAltitude() > 12000F && (double)calculateMach() >= 0.94999999999999996D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 6000D;
		if(super.FM.getAltitude() > 12700F && (double)calculateMach() >= 1.99D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
		if(super.FM.getAltitude() > 13000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
		if(super.FM.getAltitude() > 14000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
		if(super.FM.getAltitude() > 14500F && (double)calculateMach() >= 1.9299999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
		if(super.FM.getAltitude() > 14500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
		if(super.FM.getAltitude() > 15000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
		if(super.FM.getAltitude() > 15500F && (double)calculateMach() >= 1.8200000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
		if(super.FM.getAltitude() > 16000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
		if(super.FM.getAltitude() > 16500F && (double)calculateMach() >= 1.75D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
		if(super.FM.getAltitude() > 17000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
		if(super.FM.getAltitude() > 17000F && (double)calculateMach() >= 1.7D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
		if(super.FM.getAltitude() > 17500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
		if(super.FM.getAltitude() > 17500F && (double)calculateMach() >= 1.6499999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
		if(super.FM.getAltitude() > 18000F && (double)calculateMach() >= 1.5900000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
		if(super.FM.getAltitude() > 18500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
		if(super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
			((FlightModelMain) (super.FM)).producedAF.x -= 12000D;
	}

	public void moveCockpitDoor(float f)
	{
		resetYPRmodifier();
		hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
		if(Config.isUSE_RENDER())
		{
			if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	public void doEjectCatapult() {
		new MsgAction(false, this)  {

			public void doAction(Object paramObject) {
				Aircraft localAircraft = (Aircraft) paramObject;
				if (Actor.isValid(localAircraft)) {
					Loc localLoc1 = new Loc();
					Loc localLoc2 = new Loc();
					Vector3d localVector3d = new Vector3d(0.0, 0.0, 40.0);
					HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat01");
					localAircraft.pos.getAbs(localLoc2);
					localHookNamed.computePos(localAircraft, localLoc2,
							localLoc1);
					localLoc1.transform(localVector3d);
					localVector3d.x += localAircraft.FM.Vwld.x;
					localVector3d.y += localAircraft.FM.Vwld.y;
					localVector3d.z += localAircraft.FM.Vwld.z;
					new EjectionSeat(10, localLoc1, localVector3d,
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

	private boolean bHasDeployedDragChute;
	private Chute chute;
	private long removeChuteTimer;
	/*
	public double[][] 	AfbAlt = {{0D, 1000D, 1500D, 2000D, 3000D, 4000D, 5000D, 6000D, 7000D, 8000D, 9000D, 10000D, 12000D, 13000D, 14000D, 14000D, 15000D, 16000D, 17000D, 17500D, 18500D, 20000D},
	{0D, -400D, -400D, -400D, -400D, -400D, -400D, -400D, -400D, -400D, -400D, 6700D, -2500D, -2500D, -2500D, -2500D, -3700D, -3000D, -3000D, -4500D, -5000D, -12000D},
	{1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 2D, 2D, 2D, 2D, 2D, 2D, 2D, 2D, 2D},
	{-10000D, -2500D, 5000D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D}};
	//Row one is altitude, row two is thrus, row three is mach, row four thrust as function of mach
	public double[]	AfbThr = {0F, -400D, -400D, -400D, -400D, -400D, -400D, -400D, -400D, -400D, -400D, 6700D, -2500D, -2500D, -2500D, -2500D, -3700D, -3000D, -3000D, -4500D, -5000D, -12000D};
	public double[] AfbMach = {1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 1.03D, 2D, 2D, 2D, 2D, 2D, 2D, 2D, 2D, 2D};
	public double[]  AfbMachThr = {-10000D, -2500D, 5000D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, 2500D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D, -8000D};
	 */

	static 
	{
		Class class1 = com.maddox.il2.objects.air.MIG_21PFM.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "MiG21");
		Property.set(class1, "meshName", "3DO/Plane/MiG-21PFM/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1944.9F);
		Property.set(class1, "yearExpired", 1948.3F);
		Property.set(class1, "FlightModel", "FlightModels/MiG-21PFM.fmd:MIG21");
		Property.set(class1, "cockpitClass", new Class[] {
				com.maddox.il2.objects.air.CockpitMIG_21PFM.class
		});
		Property.set(class1, "LOSElevation", 0.965F);
		Aircraft.weaponTriggersRegister(class1, new int[] {
				0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 
				2, 2, 2, 2, 2, 9, 9, 3, 3, 9,
				9, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 4
		});
		Aircraft.weaponHooksRegister(class1, new String[] {
				"_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", 
				"_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev06", "_ExternalDev07", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev08",
				"_ExternalDev09", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13",
				"_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23",
				"_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33",
				"_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37"
		});
		Aircraft.weaponsRegister(class1, "default", new String[] { 
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketK13", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketK5M", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5M 1","RocketGunNull 1","RocketGunK5M 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketK5Mf", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5Mf 1","RocketGunNull 1","RocketGunK5Mf 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketR55", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55 1","RocketGunNull 1","RocketGunR55 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketR55f", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55f 1","RocketGunNull 1","RocketGunR55f 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketKh66", new String[] { 
				"MGunNull 1", null, null, null, "MiG21Pylon 1", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, "RocketGunKh66 1"
		});
		Aircraft.weaponsRegister(class1, "rocketKh66+K13", new String[] { 
				"MGunNull 1", null, null, null, "MiG21Pylon 1", "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, "RocketGunKh66 1"
		});
		Aircraft.weaponsRegister(class1, "2xFAB100", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null,
				null, null, null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "2xFAB250", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null,
				null, null, null, null, null, null, null, "BombGunFAB250m46 1", "BombGunFAB250m46 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "2xZB360", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null,
				null, null, null, null, null, null, null, "BombGunZB360 1", "BombGunZB360 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "2xUBI16", new String[] { 
				"MGunNull 1", null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, "PylonUB16 1",
				"PylonUB16 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", null
		});
		Aircraft.weaponsRegister(class1, "rocketS24", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunS24 1", "RocketGunNull 1", "RocketGunS24 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "droptank", new String[] { 
				null, null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketK13", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketK5M", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5M 1","RocketGunNull 1","RocketGunK5M 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketK5Mf", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1","MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5Mf 1","RocketGunNull 1","RocketGunK5Mf 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketR55", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1","MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, "RocketGunR55 1","RocketGunNull 1","RocketGunR55 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketR55f", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55f 1","RocketGunNull 1","RocketGunR55f 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_2xFAB100", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_2xFAB250", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB250m46 1", "BombGunFAB250m46 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_2xZB360", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunZB360 1", "BombGunZB360 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_2xUBI16", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, null, null, null, null, null, null, null, null, "PylonUB16 1",
				"PylonUB16 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", null
		});
		Aircraft.weaponsRegister(class1, "dt_rocketS24", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, "RocketGunS24 1", "RocketGunNull 1", "RocketGunS24 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gunpod", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketK13", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketK5M", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5M 1","RocketGunNull 1","RocketGunK5M 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketK5Mf", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5Mf 1","RocketGunNull 1","RocketGunK5Mf 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketR55", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55 1","RocketGunNull 1","RocketGunR55 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketR55f", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, "RocketGunR55f 1","RocketGunNull 1","RocketGunR55f 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_2xFAB100", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_2xFAB250", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB250m46 1", "BombGunFAB250m46 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_2xZB360", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunZB360 1", "BombGunZB360 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_2xdroptank", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, "FuelTankGun_PT490 1", "FuelTankGun_PT490 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_2xUBI16", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, null, null, null, null, null, null, null, null, "PylonUB16 1",
				"PylonUB16 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", null
		});
		Aircraft.weaponsRegister(class1, "gp_rocketS24", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunS24 1","RocketGunNull 1","RocketGunS24 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "3xdroptank", new String[] { 
				null, null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, "FuelTankGun_PT490 1", "FuelTankGun_PT490 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "default_RATO", new String[] { 
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketK13_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketK5M_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5M 1","RocketGunNull 1","RocketGunK5M 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketK5Mf_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5Mf 1","RocketGunNull 1","RocketGunK5Mf 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketR55_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55 1","RocketGunNull 1","RocketGunR55 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketR55f_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55f 1","RocketGunNull 1","RocketGunR55f 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "rocketKh66_RATO", new String[] { 
				"MGunNull 1", null, null, null, "MiG21Pylon 1", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, "RocketGunKh66 1"
		});
		Aircraft.weaponsRegister(class1, "rocketKh66+K13_RATO", new String[] { 
				"MGunNull 1", null, null, null, "MiG21Pylon 1", "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, "RocketGunKh66 1"
		});
		Aircraft.weaponsRegister(class1, "2xFAB100_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null,
				null, null, null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "2xFAB250_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null,
				null, null, null, null, null, null, null, "BombGunFAB250m46 1", "BombGunFAB250m46 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "2xZB360_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null,
				null, null, null, null, null, null, null, "BombGunZB360 1", "BombGunZB360 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "2xUBI16_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, "PylonUB16 1",
				"PylonUB16 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", null
		});
		Aircraft.weaponsRegister(class1, "rocketS24_RATO", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunS24 1", "RocketGunNull 1", "RocketGunS24 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "droptank_RATO", new String[] { 
				null, null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketK13_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketK5M_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5M 1","RocketGunNull 1","RocketGunK5M 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketK5Mf_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1","MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5Mf 1","RocketGunNull 1","RocketGunK5Mf 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketR55_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1","MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, "RocketGunR55 1","RocketGunNull 1","RocketGunR55 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketR55f_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55f 1","RocketGunNull 1","RocketGunR55f 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_2xFAB100_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_2xFAB250_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB250m46 1", "BombGunFAB250m46 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_2xZB360_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunZB360 1", "BombGunZB360 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_2xUBI16_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, null, null, null, null, null, null, null, null, "PylonUB16 1",
				"PylonUB16 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", null
		});
		Aircraft.weaponsRegister(class1, "dt_rocketS24_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, "RocketGunS24 1", "RocketGunNull 1", "RocketGunS24 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gunpod_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketK13_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketK5M_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5M 1","RocketGunNull 1","RocketGunK5M 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketK5Mf_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5Mf 1","RocketGunNull 1","RocketGunK5Mf 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketR55_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55 1","RocketGunNull 1","RocketGunR55 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_rocketR55f_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, "RocketGunR55f 1","RocketGunNull 1","RocketGunR55f 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_2xFAB100_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_2xFAB250_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB250m46 1", "BombGunFAB250m46 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_2xZB360_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunZB360 1", "BombGunZB360 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "gp_2xdroptank_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, "FuelTankGun_PT490 1", "FuelTankGun_PT490 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_2xUBI16_RATO", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, null, null, null, null, null, null, null, null, "PylonUB16 1",
				"PylonUB16 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", null
		});
		Aircraft.weaponsRegister(class1, "gp_rocketS24_RATO", new String[] { 
				"MGunGSh23Ls 100", "MGunGSh23Ls 100", "PylonGP9 1", null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunS24 1","RocketGunNull 1","RocketGunS24 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});
		Aircraft.weaponsRegister(class1, "3xdroptank_RATO", new String[] { 
				null, null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, "FuelTankGun_PT490 1", "FuelTankGun_PT490 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null 
		});

		Aircraft.weaponsRegister(class1, "none", new java.lang.String[]{
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null
		});
	}
}