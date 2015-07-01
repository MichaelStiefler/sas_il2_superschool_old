//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRearm_Rockets
{
	private long startTime = 0;
	private float rocketsRearmTime = 0;
	private Aircraft playerAC = null;
	
	private long rockets;
	private BornPlace bornPlace = null;
	
	public ZutiRearm_Rockets(float rearmPenalty, long rockets)
	{
		this.rockets = rockets;
		this.playerAC = World.getPlayerAircraft();
		this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(playerAC.FM);
		
		if( playerAC.FM.CT.wingControl > 0 )
		{
			com.maddox.il2.game.HUD.log("mds.unfoldWings");
			return;
		}
	
		calculateRearmingTimeConsumption(rearmPenalty);
		
		HUD.log( "mds.rearmingRocketsTime", new java.lang.Object[]{new Integer(Math.round(rocketsRearmTime))} );
		
		rocketsRearmTime *= 1000;
		
		startTime = Time.current();
	}

	/**
	 * if -1 is returned abort execution
	 * @return
	 */
	public int updateTimer() 
	{
		if( !ZutiSupportMethods.allowRRR(playerAC) )
		{
			this.cancelTimer();
			return -1;
		}
		
		try
		{		
			if( rocketsRearmTime > -1 && Time.current()-startTime > rocketsRearmTime )
			{
				ZutiWeaponsManagement.rearmRockets(playerAC, this.rockets);
				
				if( playerAC instanceof NetAircraft )
					ZutiSupportMethods_Air.sendNetAircraftRearmOrdinance(playerAC, 1, this.rockets, null);
				
				String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
				String userLocation = ZutiSupportMethods.getPlayerLocation();
				ZutiSupportMethods_NetSend.logMessage((NetUser)NetEnv.host(), userData + " rearmed rockets at " + userLocation);
				
				rocketsRearmTime = -1;
				
				//Collect earned points
				ZutiSupportMethods_AI.collectPoints();
				//Reset processing of cargo drops since player rearmed the plane and had to land first (and survive :D)
				ZutiWeaponsManagement.ZUTI_PROCESS_CARGO_DROPS = true;
				
				stopTimer();
				
				return -1;
			}
		}
		catch(Exception ex){ex.printStackTrace();}

		return 0;
	}
	
	private void calculateRearmingTimeConsumption(float rearmPenalty)
	{
		/*
		com.maddox.il2.ai.BulletEmitter[][] weapons = World.getPlayerAircraft().FM.CT.Weapons;
		
		for( int i=0; i<weapons.length; i++ )
		{
			try
			{
				for( int j=0; j<weapons[i].length; j++ )
				{
					//Covers rockets
					if( weapons[i][j] instanceof RocketGun )
					{
						if( bornPlace == null )
							rocketsRearmTime += Mission.MDS_VARIABLES().zutiReload_OneRocketRearmSeconds;
						else
							rocketsRearmTime += bornPlace.zutiOneRocketRearmSeconds;
					}
				}
			}
			catch(Exception ex){}
		}
		*/
		
		if( bornPlace == null )
			rocketsRearmTime = (Mission.MDS_VARIABLES().zutiReload_OneRocketRearmSeconds * this.rockets);
		else
			rocketsRearmTime = (bornPlace.zutiOneRocketRearmSeconds * this.rockets);
		
		System.out.println("Rockets Rearm time: " + rocketsRearmTime);
		System.out.println("  Rearming Penalty: " + rearmPenalty);
		
		rocketsRearmTime = rocketsRearmTime * rearmPenalty;
		
		System.out.println("--------------------------------------");
		System.out.println("Calculated Rearm Time: " + rocketsRearmTime);
	}
	
	private int countLoadedRockets()
	{
		int counter = 0;
		com.maddox.il2.ai.BulletEmitter[][] weapons = World.getPlayerAircraft().FM.CT.Weapons;
		
		for( int i=0; i<weapons.length; i++ )
		{
			try
			{
				for( int j=0; j<weapons[i].length; j++ )
				{
					//Covers bombs, fuel tanks, torpedoes
					if( weapons[i][j] instanceof RocketGun )
					{
						counter += ((RocketGun)weapons[i][j]).countBullets();
					}
				}
			}
			catch(Exception ex){}
		}
		
		return counter;
	}
	
	private void stopTimer()
	{
		int loadedRockets = countLoadedRockets();
		HUD.log("mds.rearmingRocketsDone", new java.lang.Object[]{new Integer(loadedRockets)} );
		System.out.println("Rearming done! Loaded >" + loadedRockets +"< rockets.");
	}
	
	public void cancelTimer()
	{
		//Return unused fuel
		ZutiSupportMethods_NetSend.returnRRRResources_Rockets(this.rockets, playerAC.pos.getAbsPoint());
		
		com.maddox.il2.game.HUD.log("mds.rearmingRocketsAborted");
		System.out.println("Rearming aborted!!!");
	}
}