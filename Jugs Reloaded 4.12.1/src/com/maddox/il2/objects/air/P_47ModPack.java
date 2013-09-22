package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.il2.objects.weapons.WeaponHelper;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class P_47ModPack extends P_47 {

	public P_47ModPack() {
		bFlaps = false;
		bFlapsEnd = false;
	}

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
				|| (this.thisWeaponsName.equalsIgnoreCase("1x10002x500")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x5006x45")) || (this.thisWeaponsName.indexOf("w_") != -1));
		if (hierMesh().chunkFindCheck("Rack_D0") >= 0)
			hierMesh().chunkVisible("Rack_D0", bCenterRackVisible);
		if (hierMesh().chunkFindCheck("RackL_D0") >= 0)
			hierMesh().chunkVisible("RackL_D0", bWingRacksVisible);
		if (hierMesh().chunkFindCheck("RackR_D0") >= 0)
			hierMesh().chunkVisible("RackR_D0", bWingRacksVisible);
		this.rocketsLeft = this.getNumRocketsAvail();
		this.bombsLeft = this.getNumBombsAvail();
		this.releaseMode = 0;
		this.lastRocketBurst = 0;
		this.inRocketBurstMode = false;
		this.inBombBurstMode = false;
		this.inReleaseModeToggle = false;
		this.inPairwiseMode = false;
		this.bExtTank = checkFuelTanks();

		this.hasStockOrdnance = false;
		int stockOrdnanceAvailable = Property.intValue(this.getClass(), "StockOrdnanceAvailable", 0);
		if (stockOrdnanceAvailable != 0)
			this.hasStockOrdnance = (this.thisWeaponsName.indexOf("*") == -1);

		if (this.hasStockOrdnance || !(World.cur().diffCur.Limited_Ammo))
			this.removeBombGunNull();
	}

	private int getNumRocketsAvail() {
		int retVal = 0;
		try {
			for (int i = 0; i < this.FM.CT.Weapons.length; i++)
				if (this.FM.CT.Weapons[i] != null)
					for (int j = 0; j < this.FM.CT.Weapons[i].length; j++)
						if (this.FM.CT.Weapons[i][j] != null)
							if ((this.FM.CT.Weapons[i][j] instanceof RocketGun) || (this.FM.CT.Weapons[i][j] instanceof RocketBombGun))
								retVal += this.FM.CT.Weapons[i][j].countBullets();
		} catch (Exception exception) {
			printDebugMessage("Exception in getNumRocketsAvail(): " + exception.getMessage());
		}
		return retVal;
	}

	private int getNumBombsAvail() {
		int retVal = 0;
		try {
			for (int i = 0; i < this.FM.CT.Weapons.length; i++)
				if (this.FM.CT.Weapons[i] != null)
					for (int j = 0; j < this.FM.CT.Weapons[i].length; j++)
						if (this.FM.CT.Weapons[i][j] != null)
							if (this.FM.CT.Weapons[i][j] instanceof BombGun)
								retVal += this.FM.CT.Weapons[i][j].countBullets();
		} catch (Exception exception) {
			printDebugMessage("Exception in getNumBombsAvail(): " + exception.getMessage());
		}
		return retVal;
	}

	private void removeBombGunNull() {
		for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
			if (this.FM.CT.Weapons[i] != null) {
				ArrayList saveWeapons = new ArrayList();
				for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
					if (!(this.FM.CT.Weapons[i][j] instanceof BombGunNull)) {
						saveWeapons.add(this.FM.CT.Weapons[i][j]);
					}
					if (!(World.cur().diffCur.Limited_Ammo)) {
						if (isBombRocketGun(this.FM.CT.Weapons[i][j])) {
							setShotStep(this.FM.CT.Weapons[i][j], 1);
						}
					}
				}
				this.FM.CT.Weapons[i] = new BulletEmitter[saveWeapons.size()];
				for (int j = 0; j < saveWeapons.size(); j++) {
					this.FM.CT.Weapons[i][j] = (BulletEmitter) saveWeapons.get(j);
				}
			}
		}
	}

	private boolean checkFuelTanks() {
		Object aobj[] = pos.getBaseAttached();
		if (aobj == null)
			return false;
		for (int i = 0; i < aobj.length; i++)
			if (aobj[i] instanceof FuelTank)
				return true;
		return false;
	}

	private void setShotStep(BulletEmitter theBulletEmitter, int theShotStep) {
		if (theBulletEmitter instanceof BombGun)
			WeaponHelper.setBombGunShotStep((BombGun) theBulletEmitter, 1);
		else if (theBulletEmitter instanceof RocketGun)
			WeaponHelper.setRocketGunShotStep((RocketGun) theBulletEmitter, 1);
		else if (theBulletEmitter instanceof RocketBombGun)
			WeaponHelper.setRocketBombGunShotStep((RocketBombGun) theBulletEmitter, 1);
	}

	private boolean isBombRocketGun(BulletEmitter theBulletEmitter) {
		return (theBulletEmitter instanceof BombGun) || (theBulletEmitter instanceof RocketGun) || (theBulletEmitter instanceof RocketBombGun);
	}

	private void toggleReleaseMode() {
		if (this.inReleaseModeToggle)
			return;
		this.inReleaseModeToggle = true;
		this.releaseMode++;
		this.releaseMode %= 3;
		if (this == World.getPlayerAircraft()) {
			switch (this.releaseMode) {
			case 0:
				HUD.log("Single Weapon Release");
				break;
			case 1:
				HUD.log("Pairwise Weapon Release");
				break;
			case 2:
				HUD.log("Salvo Weapon Release");
				break;
			}
		}
	}

	public void update(float f) {
		do {

			if (!(World.cur().diffCur.Limited_Ammo) || this.hasStockOrdnance)
				break;

			if (this.FM.CT.WeaponControl[1] && !this.FM.CT.saveWeaponControl[0]) // Toggle Release Mode when Trigger 2 only has been pressed.
				this.toggleReleaseMode();
			else
				this.inReleaseModeToggle = false;
			
			if (this.FM.CT.saveWeaponControl[2]) { // Trigger 3 (Rockets) pressed
				if (this.rocketsLeft > 0)
					this.rocketsLeft--;
				switch (this.releaseMode) {
				case 1:
					if (this.inPairwiseMode)
						break;
					this.inPairwiseMode = true;
					this.FM.CT.WeaponControl[2] = true;
					break;
				case 2:
					if (this.rocketsLeft > 0) {
						this.inRocketBurstMode = true;
						this.lastRocketBurst = Time.current();
					}
				}
			} 
			if (this.FM.CT.saveWeaponControl[3]) { // Trigger 4 (Bombs) pressed
				if (this.bombsLeft > 0)
					this.bombsLeft--;
				switch (this.releaseMode) {
				case 1:
					if (this.inPairwiseMode)
						break;
					this.inPairwiseMode = true;
					this.FM.CT.WeaponControl[3] = true;
					break;
				case 2:
					if (this.bombsLeft > 0) {
						this.inBombBurstMode = true;
						this.lastBombBurst = Time.current();
					}
				}
			}
			if (!this.FM.CT.saveWeaponControl[2] && !this.FM.CT.saveWeaponControl[3])
				this.inPairwiseMode = false;

			if (this.rocketsLeft < 1) {
				this.inRocketBurstMode = false;
				this.rocketsLeft = 0;
			}

			if (this.bombsLeft < 1) {
				this.inBombBurstMode = false;
				this.bombsLeft = 0;
			}

			if (this.inRocketBurstMode) {
				if (Time.current() - this.lastRocketBurst > rocketBurstGap) {
					this.FM.CT.WeaponControl[2] = true;
					this.lastRocketBurst = Time.current();
				}
			}
			
			if (this.inBombBurstMode) {
				if (Time.current() - this.lastBombBurst > bombBurstGap) {
					this.FM.CT.WeaponControl[3] = true;
					this.lastBombBurst = Time.current();
				}
			}
		} while (false);

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
		super.destroy();
		if (soundWheels != null)
			soundWheels.cancel();
		if (soundGearDn != null)
			soundGearDn.cancel();
		if (soundGearUp != null)
			soundGearUp.cancel();
	}

	public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		Aircraft.xyz[2] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
		hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	protected void moveFan(float f) {
		if (!Config.isUSE_RENDER())
			return;
		float f1 = f;
		int i = FM.EI.engines[0].getStage();
		if (i >= 1 && i <= 3)
			f1 = 0.0F;
		if (i == 4)
			f1 = -150F;
		if (i == 5)
			f1 = -400F;
		super.moveFan(f1);
		hierMesh().chunkSetAngles(Aircraft.Props[0][0], 0.0F, -propPos[0] + 45F, 0.0F);
		// float f2 = FM.CT.getRudder();
		// hierMesh().chunkSetAngles("Head1_D0", 0.0F, Aircraft.cvt(-f2, -1F, 1.0F, -30F, 30F), 0.0F);
	}

	protected void moveAirBrake(float f) {
		float f1 = 30F * f;
		hierMesh().chunkSetAngles("Brake1_D0", 0.0F, 0.0F, f1);
		hierMesh().chunkSetAngles("Brake2_D0", 0.0F, 0.0F, f1);
	}

	private int rocketsLeft = 0;
	private int bombsLeft = 0;
	private int releaseMode = 0;
	private long lastRocketBurst = 0;
	private long lastBombBurst = 0;
	private boolean inRocketBurstMode = false;
	private boolean inBombBurstMode = false;
	private boolean inPairwiseMode = false;
	private boolean inReleaseModeToggle = false;
	private boolean hasStockOrdnance = false;

	public boolean bExtTank = false;
	public SoundFX soundWheels;
	public SoundFX soundGearDn;
	public SoundFX soundGearUp;
	public boolean bFlaps;
	public boolean bFlapsEnd;

	private static boolean _DEBUG = false;
	private static final long rocketBurstGap = 250;
	private static final long bombBurstGap = 250;

	protected static void printDebugMessage(String theMessage) {
		if (_DEBUG)
			System.out.println(theMessage);
	}
}
