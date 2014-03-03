package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.IniFile;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class P_47ModPack extends P_47 {

	public P_47ModPack() {
		bFlaps = false;
		bFlapsEnd = false;
		pTailSway = new Point3f(4.0F, 0F, 0F);
//		try {
//			this.aircraftLHclass = Class.forName("com.maddox.il2.objects.air.AircraftLH");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
//	private Class aircraftLHclass;

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (hierMesh().chunkFindCheck("ETank_D0") >= 0)
			hierMesh().chunkVisible("ETank_D0", false);
		boolean bCenterRackVisible = false;
		boolean bWingRacksVisible = false;
		bCenterRackVisible = ((this.thisWeaponsName.equalsIgnoreCase("tank")) || (this.thisWeaponsName.equalsIgnoreCase("tank2x500")) || (this.thisWeaponsName.equalsIgnoreCase("tank6x45")) || (this.thisWeaponsName.equalsIgnoreCase("tank2x5006x45"))
				|| (this.thisWeaponsName.equalsIgnoreCase("1x1000")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x500")) || (this.thisWeaponsName.equalsIgnoreCase("1x10006x45")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x5006x45")) || (this.thisWeaponsName
				.indexOf("c_") != -1));
		bWingRacksVisible = ((this.thisWeaponsName.equalsIgnoreCase("tank2x500")) || (this.thisWeaponsName.equalsIgnoreCase("tank2x5006x45")) || (this.thisWeaponsName.equalsIgnoreCase("2x500")) || (this.thisWeaponsName.equalsIgnoreCase("2x5006x45"))
				|| (this.thisWeaponsName.equalsIgnoreCase("1x10002x500")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x5006x45")) || (this.thisWeaponsName.equalsIgnoreCase("2x1000")) || (this.thisWeaponsName.indexOf("w_") != -1));
		if (hierMesh().chunkFindCheck("Rack_D0") >= 0)
			hierMesh().chunkVisible("Rack_D0", bCenterRackVisible);
		if (hierMesh().chunkFindCheck("RackL_D0") >= 0)
			hierMesh().chunkVisible("RackL_D0", bWingRacksVisible);
		if (hierMesh().chunkFindCheck("RackR_D0") >= 0)
			hierMesh().chunkVisible("RackR_D0", bWingRacksVisible);
		// Some stock FMs don't have compressor control for the P&W engines, but our Jugs should have them, so we forcibly enable it here!
//		try {
//			Class engineClass = this.FM.EI.engines[0].getClass();
//			Field bHasCompressorControlField = engineClass.getDeclaredField("bHasCompressorControl");
//			bHasCompressorControlField.setAccessible(true);
//			bHasCompressorControlField.setBoolean(this.FM.EI.engines[0], true);
//		} catch (Exception e) {
//			System.out.println("Exception in setting bHasCompressorControl=true:");
//			e.printStackTrace();
//		}
        if (!Mission.isNet()) {
        	this.FM.CT.bHasCockpitDoorControl = true;
        	this.FM.CT.dvCockpitDoor = 1F;
        }
		
	}

	public void update(float f) {
		super.update(f);

		if ((FM instanceof RealFlightModel) && ((RealFlightModel) FM).isRealMode()) {
			float f2 = FM.EI.engines[0].getRPM();
			if (f2 < 1000F && f2 > 100F)
				((RealFlightModel) FM).producedShakeLevel = (1000F - f2) / 8000F;
		}
	}

	public void sfxFlaps(boolean flag) {
		if (flag) {
			bFlaps = true;
			bFlapsEnd = false;
			printDebugMessage("*** Flaps sound starts");
		}
		if (bFlaps && !flag) {
			bFlaps = false;
			bFlapsEnd = true;
			printDebugMessage("*** Flaps sound ends");
		}
	}

	public void sfxGear(boolean flag) {
		if (soundGearUp != null)
			soundGearUp.setPlay(flag);
	}

	public void sfxWheels() {
		if (FM.getSpeedKMH() > 0.0F) {
			if (soundWheels != null)
				soundWheels.setPlay(true);
			printDebugMessage("*** Wheels sound used");
		}
	}

	public void destroy() {
		if (isDestroyed())
			return;
//    	System.out.println("P_47ModPack destroy 01");
		super.destroy();
//    	System.out.println("P_47ModPack destroy 02");
		if (soundWheels != null)
			soundWheels.cancel();
		if (soundGearDn != null)
			soundGearDn.cancel();
		if (soundGearUp != null)
			soundGearUp.cancel();
	}

	public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		if (this instanceof P_47B1 || this instanceof P_47B15 || this instanceof P_47BDT || this instanceof P_47C5 || this instanceof P_47D10 || this instanceof P_47D22) {
			Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.85F);
			Aircraft.xyz[2] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.025F);
		} else if (this instanceof P_47D27 || this instanceof P_47D) {
			Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
			Aircraft.xyz[2] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.025F);
		} else {
			Aircraft.xyz[2] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
		}
		hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	protected void moveFan(float f) {
		if (!Config.isUSE_RENDER()) return;
		int i = FM.EI.engines[0].getStage();
		if (i > 0 && i < 6) f = 0.005F * (float)i;
		super.moveFan(f);
		hierMesh().chunkSetAngles(Aircraft.Props[0][0], 0.0F, -propPos[0] + 45F, 0.0F);
		// float f2 = FM.CT.getRudder();
		// hierMesh().chunkSetAngles("Head1_D0", 0.0F, Aircraft.cvt(-f2, -1F, 1.0F, -30F, 30F), 0.0F);
	}

	protected void moveAirBrake(float f) {
		float f1 = 30F * f;
		hierMesh().chunkSetAngles("Brake1_D0", 0.0F, 0.0F, f1);
		hierMesh().chunkSetAngles("Brake2_D0", 0.0F, 0.0F, f1);
	}
	
    void bubbleTopTailSway() {
    	float fGunFactor = this.FM.CT.WeaponControl[0] || this.FM.CT.WeaponControl[1]?2F:1F;
    	Vector3f theTailSway = new Vector3f(0F, (float)Math.sin((float)Time.current() / 1000F * Math.PI * fGunFactor), 0F);
    	float fSwayFactor = this.FM.getSpeed() * this.FM.getSpeed() * fGunFactor * fGunFactor / 15F;
    	theTailSway.scale(fSwayFactor);
    	Vector3f theTailSwayMomentum = new Vector3f();
    	theTailSwayMomentum.cross(pTailSway, theTailSway);
    	this.FM.producedAM.z += theTailSwayMomentum.z;
    }
    
    
    private Point3f pTailSway;
	public boolean bExtTank = false;
	public SoundFX soundWheels;
	public SoundFX soundGearDn;
	public SoundFX soundGearUp;
	public boolean bFlaps;
	public boolean bFlapsEnd;
	private static int flightModelsConfIniValue = -1;

	private static boolean _DEBUG = false;

	protected static void printDebugMessage(String theMessage) {
		if (_DEBUG)
			System.out.println(theMessage);
	}
	
	static boolean useStockFlightModels() {
		if (flightModelsConfIniValue == -1) {
	        IniFile inifile = new IniFile("conf.ini", 0);
	        flightModelsConfIniValue = inifile.get("Mods", "P47PackNewFM", 0);
		}
		return flightModelsConfIniValue == 0;
	}
}
