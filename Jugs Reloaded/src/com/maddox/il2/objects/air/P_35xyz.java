package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class P_35xyz extends RE_2000xyz {
	
	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.rocketsLeft = this.getNumRocketsAvail();
		this.bombsLeft = this.getNumBombsAvail();
		this.releaseMode = 0;
		this.lastRocketBurst = 0;
		this.inRocketBurstMode = false;
		this.inBombBurstMode = false;
		this.inReleaseModeToggle = false;
		this.inPairwiseMode = false;
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
					if (rocketsLeft > 0) {
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
		if ((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode()) {
			float f2 = FM.EI.engines[0].getRPM();
			if (f2 < 1000F && f2 > 100F)
				((RealFlightModel)FM).producedShakeLevel = (1000F - f2) / 8000F;
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

	private static boolean _DEBUG = false;
	private static final long rocketBurstGap = 250;
	private static final long bombBurstGap = 250;

	protected static void printDebugMessage(String theMessage) {
		if (_DEBUG)
			System.out.println(theMessage);
	}

	static {
		Property.set(P_35xyz.class, "originCountry", PaintScheme.countryUSA);
	}
}
