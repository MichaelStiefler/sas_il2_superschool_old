package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class MIG_21PF extends MIG_21
implements TypeGuidedMissileCarrier,TypeCountermeasure,
TypeThreatDetector {

	private GuidedMissileUtils guidedMissileUtils = null;

	public MIG_21PF()
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
	}

	private void doDealRadarLockThreat() {    // Must be filled with life for A/Cs capable of dealing with radar lock Threats
	}

	private void doDealMissileLaunchThreat() {// Must be filled with life for A/Cs capable of dealing with missile launch Threats
	}// </editor-fold>

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.CT.bHasDragChuteControl = true;
		bHasDeployedDragChute = false;
		this.guidedMissileUtils.onAircraftLoaded();
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
	}

	protected void moveFlap(float f)
	{
		//TODO: This code enables the aft movement of the Fowler Flaps
		super.moveFlap(f);
		resetYPRmodifier();
		Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.3F);
		hierMesh().chunkSetLocate("Flap01a_D0", Aircraft.xyz, Aircraft.ypr);
		hierMesh().chunkSetLocate("Flap02a_D0", Aircraft.xyz, Aircraft.ypr);
	}

	public boolean typeFighterAceMakerToggleAutomation()
	{
		k14Mode++;
		if(k14Mode > 1)
			k14Mode = 0;
		if (k14Mode == 0) {
		      if (FM.actor == World.getPlayerAircraft()) {
		        HUD.log(AircraftHotKeys.hudLogWeaponId, "PKI Sight: On");
		      }
		    } else if (k14Mode == 1) {
		      if (FM.actor == World.getPlayerAircraft()) {
		        HUD.log(AircraftHotKeys.hudLogWeaponId, "PKI Sight: Off");
		      }
		    }
		return true;
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
			chute.pos.setRel(new Point3d(-5.0D, 0.0D, -0.6D), new Orient(0.0F, 90F, 0.0F));
			bHasDeployedDragChute = true;
		}
		else if (bHasDeployedDragChute  && FM.CT.bHasDragChuteControl){
			if(((FM.CT.DragChuteControl == 1.0F && (FM.getSpeedKMH() > 600F))|| FM.CT.DragChuteControl < 1.0F))
			{
				if(chute != null)
				{
					chute.tangleChute(this);
					chute.pos.setRel(new Point3d(-10D, 0.0D, 0.4D), new Orient(0.0F, 60F, 0.0F));
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
		typeFighterAceMakerRangeFinder();
		this.guidedMissileUtils.update();
	}

	private void setAfterburner(){
		//////TODO: !!!! IMPORTANT NOTE!!!! These parameters define afterburner function at altitude and are different for each version of MIG-21!
		/*
		if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6){
			FM.producedAF.x += 26500D;
			////Celing limitation parameters
			if(super.FM.getAltitude() >= 1000F && super.FM.getAltitude() < 9000F)
				FM.producedAF.x += -0.5D*super.FM.getAltitude();
			if(super.FM.getAltitude() >= 9000F && super.FM.getAltitude() < 10000F)
				FM.producedAF.x += ((6.6D*super.FM.getAltitude())-63900D);
			if(super.FM.getAltitude() >= 10000F && super.FM.getAltitude() < 12000F)
				FM.producedAF.x += ((-1.25D*super.FM.getAltitude())+14600D);
			if(super.FM.getAltitude() >= 12000F && super.FM.getAltitude() < 14000F)
				FM.producedAF.x += ((-2.5D*super.FM.getAltitude())+29650D);
			if(super.FM.getAltitude() >= 14000F && super.FM.getAltitude() < 14500F)
				FM.producedAF.x += ((-5D*super.FM.getAltitude())+64650D);
			if(super.FM.getAltitude() >= 14500F && super.FM.getAltitude() < 15000F)
				FM.producedAF.x += ((-7.4D*super.FM.getAltitude())+99450D);
			if(super.FM.getAltitude() >= 15000F && super.FM.getAltitude() < 16000F)
				FM.producedAF.x += ((-4.6D*super.FM.getAltitude())+57450D);
			if(super.FM.getAltitude() >= 16000F && super.FM.getAltitude() < 17000F)
				FM.producedAF.x += ((-5D*super.FM.getAltitude())+61850D);
			if(super.FM.getAltitude() >= 17000F && super.FM.getAltitude() < 18000F)
				FM.producedAF.x += ((-9D*super.FM.getAltitude())+131850D);
			if(super.FM.getAltitude() >= 18000F && super.FM.getAltitude() < 18500F)
				FM.producedAF.x += ((-5D*super.FM.getAltitude())+61850D);
			if(super.FM.getAltitude() >= 18500F)
				FM.producedAF.x += ((-8D*super.FM.getAltitude())+117350D);     
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
		hierMesh().chunkSetAngles("Blister1_D0", 0.0F, -50F * f, 0.0F);
		if(Config.isUSE_RENDER())
		{
			if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	private static Actor hunted = null;
	private boolean bHasDeployedDragChute;
	private Chute chute;
	private long removeChuteTimer;

	static 
	{
		Class class1 = com.maddox.il2.objects.air.MIG_21PF.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "MiG21");
		Property.set(class1, "meshName", "3DO/Plane/MiG-21PF/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1944.9F);
		Property.set(class1, "yearExpired", 1948.3F);
		Property.set(class1, "FlightModel", "FlightModels/MiG-21PF.fmd:MIG21");
		Property.set(class1, "cockpitClass", new Class[] {
				com.maddox.il2.objects.air.CockpitMIG_21PF.class
		});
		Property.set(class1, "LOSElevation", 0.965F);
		Aircraft.weaponTriggersRegister(class1, new int[] {
				0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 
				2, 2, 2, 2, 2, 9, 9, 3, 3, 9,
				9, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2
		});
		Aircraft.weaponHooksRegister(class1, new String[] {
				"_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", 
				"_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev06", "_ExternalDev07", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev08",
				"_ExternalDev09", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13",
				"_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23",
				"_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33",
				"_ExternalRock34", "_ExternalRock35", "_ExternalRock36"
		});
		Aircraft.weaponsRegister(class1, "default", new String[] { 
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "rocketK13", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "rocketK5M", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5M 1","RocketGunNull 1","RocketGunK5M 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "rocketK5Mf", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5Mf 1","RocketGunNull 1","RocketGunK5Mf 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "rocketR55", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55 1","RocketGunNull 1","RocketGunR55 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "rocketR55f", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55f 1","RocketGunNull 1","RocketGunR55f 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "2xFAB100", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null,
				null, null, null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "2xFAB250", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null,
				null, null, null, null, null, null, null, "BombGunFAB250m46 1", "BombGunFAB250m46 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "2xZB360", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null,
				null, null, null, null, null, null, null, "BombGunZB360 1", "BombGunZB360 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "2xUBI16", new String[] { 
				"MGunNull 1", null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, "PylonUB16 1",
				"PylonUB16 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1"
		});
		Aircraft.weaponsRegister(class1, "rocketS24", new String[] { 
				"MGunNull 1", null, null, null, null, "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunS24 1", "RocketGunNull 1", "RocketGunS24 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "droptank", new String[] { 
				null, null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "dt_rocketK13", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
				"RocketGunNull 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "dt_rocketK5M", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5M 1","RocketGunNull 1","RocketGunK5M 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "dt_rocketK5Mf", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1","MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunK5Mf 1","RocketGunNull 1","RocketGunK5Mf 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null 
		});
		Aircraft.weaponsRegister(class1, "dt_rocketR55", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1","MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, "RocketGunR55 1","RocketGunNull 1","RocketGunR55 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "dt_rocketR55f", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, "RocketGunR55f 1","RocketGunNull 1","RocketGunR55f 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "dt_2xFAB100", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "dt_2xFAB250", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunFAB250m46 1", "BombGunFAB250m46 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "dt_2xZB360", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null,
				null, null, null, null, null, null, null, "BombGunZB360 1", "BombGunZB360 1", null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null
		});
		Aircraft.weaponsRegister(class1, "dt_2xUBI16", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, null, null, null, null, null, null, null, null, "PylonUB16 1",
				"PylonUB16 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1",
				"RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1"
		});
		Aircraft.weaponsRegister(class1, "dt_rocketS24", new String[] { 
				"MGunNull 1", null, null, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", null, null, null, 
				null, "RocketGunS24 1", "RocketGunNull 1", "RocketGunS24 1", "RocketGunNull 1", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null 
		});
		Aircraft.weaponsRegister(class1, "3xdroptank", new String[] { 
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
				null, null, null
		});
	}
}