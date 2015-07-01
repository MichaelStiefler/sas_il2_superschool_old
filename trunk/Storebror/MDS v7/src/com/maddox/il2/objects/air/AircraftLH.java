/*Here because of obfuscation reasons*/
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;

public abstract class AircraftLH extends Aircraft
{
	public static int		hudLogCompassId		= HUD.makeIdLog();
	public static boolean	printCompassHeading	= false;
	public boolean			bWantBeaconKeys		= false;

	public void beaconPlus()
	{
		if (bWantBeaconKeys && (Main.cur().mission.getBeacons(getArmy()) == null || Main.cur().mission.getBeacons(getArmy()).size() != 0))
			FM.AS.beaconPlus();
	}

	public void beaconMinus()
	{
		if (bWantBeaconKeys && (Main.cur().mission.getBeacons(getArmy()) == null || Main.cur().mission.getBeacons(getArmy()).size() != 0))
			FM.AS.beaconMinus();
	}

	public void beaconSet(int i)
	{
		if (bWantBeaconKeys && (Main.cur().mission.getBeacons(getArmy()) == null || Main.cur().mission.getBeacons(getArmy()).size() != 0))
			FM.AS.setBeacon(i);
	}

	public void auxPlus(int i)
	{
		switch (i)
		{
			case 1:
				headingBug = headingBug + 1.0F;
				if (headingBug >= 360.0F)
					headingBug = 0.0F;
				if (printCompassHeading && World.cur().diffCur.RealisticNavigationInstruments && bWantBeaconKeys)
					HUD.log(hudLogCompassId, "CompassHeading", new Object[] { "" + (int) headingBug });
		}
	}

	public void auxMinus(int i)
	{
		switch (i)
		{
			case 1:
				headingBug = headingBug - 1.0F;
				if (headingBug < 0.0F)
					headingBug = 359.0F;
				if (printCompassHeading && World.cur().diffCur.RealisticNavigationInstruments && bWantBeaconKeys)
					HUD.log(hudLogCompassId, "CompassHeading", new Object[] { "" + (int) headingBug });
		}
	}

	public void auxPressed(int i)
	{
		if (i == 1)
			FM.CT.dropExternalStores(true);
	}

	protected void hitFlesh(int i, Shot shot, int i_0_)
	{
		int i_1_ = 0;
		int i_2_ = (int) (shot.power * 0.0035F * World.Rnd().nextFloat(0.5F, 1.5F));
		switch (i_0_)
		{
			case 0:
				if (!(World.Rnd().nextFloat() < 0.05F))
				{
					if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
						HUD.logCenter("H E A D S H O T");
					i_2_ *= 30;
					break;
				}
				return;
			case 1:
				if (World.Rnd().nextFloat() < 0.08F)
				{
					i_2_ *= 2;
					i_1_ = World.Rnd().nextInt(1, 15) * 8000;
				}
				else
				{
					boolean bool = World.Rnd().nextInt(0, 100 - i_2_) <= 20;
					if (bool)
						i_1_ = i_2_ / World.Rnd().nextInt(1, 10);
				}
			break;
			case 2:
				if (World.Rnd().nextFloat() < 0.015F)
				{
					i_1_ = World.Rnd().nextInt(1, 15) * 1000;
				}
				else
				{
					boolean bool = World.Rnd().nextInt(0, 100 - i_2_) <= 10;
					if (bool)
						i_1_ = i_2_ / World.Rnd().nextInt(1, 15);
				}
				i_2_ /= 1.5F;
			break;
		}
		debuggunnery("*** Pilot " + i + " hit for " + i_2_ + "% (" + (int) shot.power + " J)");
		FM.AS.hitPilot(shot.initiator, i, i_2_);
		if (World.cur().diffCur.RealisticPilotVulnerability)
		{
			if (i_1_ > 0)
				FM.AS.setBleedingPilot(shot.initiator, i, i_1_);
			if (i == 0 && i_0_ > 0)
				FM.AS.woundedPilot(shot.initiator, i_0_, i_2_);
		}
		if (FM.AS.astatePilotStates[i] > 95 && i_0_ == 0)
			debuggunnery("*** Headshot!.");
	}
}
