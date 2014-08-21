package com.maddox.il2.game;

import java.util.Map;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Front;
import com.maddox.il2.builder.ZutiSupportMethods_Builder;
import com.maddox.il2.builder.Zuti_WResourcesManagement.RRRItem;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.objects.buildings.House;

public class ZutiSupportMethods_ResourcesManagement
{
	private static BornPlace LAST_RESOURCES_BORN_PLACE = null;
	
	/**
	 * Reset class variables
	 */
	public static void resetClassVariables()
	{
		LAST_RESOURCES_BORN_PLACE = null;
	}
	
	/**
	 * Method searches for born place near given coordinates and checks if it has
	 * enough fuel to serve to the player. Its fuel supply counter is appropriately 
	 * adjusted.
	 * @param requestedFuel
	 * @param x
	 * @param y
	 * @return
	 */
	public static long getAvailableFuelAtLocation(float requestedFuel, double x, double y)
	{
		System.out.println("Requesting fuel: " + (long)requestedFuel);
		if( !Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases )
		{
			System.out.println("Returning fuel: " + (long)requestedFuel);
			return (long)requestedFuel;
		}
		
		BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
		int armyForLocation = Front.army(x, y);
		
		if( bp != null && bp.zutiEnableResourcesManagement)
		{
			if( requestedFuel <= bp.zutiFuelSupply )
			{
				bp.zutiFuelSupply -= requestedFuel;
			}
			else
			{
				requestedFuel = bp.zutiFuelSupply;
				bp.zutiFuelSupply = 0;
			}
			
			System.out.println("  Fuel was deducted from HB at x=" + (int)bp.place.x + ", y=" + (int)bp.place.y + ". New fuel amount: " + (long)bp.zutiFuelSupply);
		}
		else if( armyForLocation == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
		{			
			//Manage side resources
			if( requestedFuel <= Mission.MDS_VARIABLES().zutiFuelSupply_Red )
			{
				Mission.MDS_VARIABLES().zutiFuelSupply_Red -= requestedFuel;
			}
			else
			{
				requestedFuel = (int)Mission.MDS_VARIABLES().zutiFuelSupply_Red;
				Mission.MDS_VARIABLES().zutiFuelSupply_Red = 0;
			}
			
			System.out.println("  Fuel was deducted from RED side. New fuel amount: " + Mission.MDS_VARIABLES().zutiFuelSupply_Red);
		}
		else if( armyForLocation == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
		{
			//Manage side resources
			if( requestedFuel <= Mission.MDS_VARIABLES().zutiFuelSupply_Blue )
			{
				Mission.MDS_VARIABLES().zutiFuelSupply_Blue -= requestedFuel;
			}
			else
			{
				requestedFuel = (int)Mission.MDS_VARIABLES().zutiFuelSupply_Blue;
				Mission.MDS_VARIABLES().zutiFuelSupply_Blue = 0;
			}
			
			System.out.println("  Fuel was deducted from BLUE side. New fuel amount: " + Mission.MDS_VARIABLES().zutiFuelSupply_Blue);
		}
		
		System.out.println("Returning fuel: " + (long)requestedFuel);
		return (long)requestedFuel;
	}
	
	/**
	 * Method searches for born place near given coordinates and checks if it has
	 * enough bullets to serve to the player. Its bullets supply counter is appropriately 
	 * adjusted.
	 * @param requestedBullets
	 * @param x
	 * @param y
	 * @return
	 */
	public static long getAvailableBulletsAtLocation(long requestedBullets, double x, double y)
	{
		System.out.println("Requesting bullets: " + requestedBullets);
		if( !Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases )
		{
			System.out.println("Returning bullets: " + requestedBullets);
			return requestedBullets;
		}
				
		BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
		int armyForLocation = Front.army(x, y);
				
		if( bp != null && bp.zutiEnableResourcesManagement)
		{
			if( requestedBullets <= bp.zutiBulletsSupply )
			{
				bp.zutiBulletsSupply -= requestedBullets;
			}
			else
			{
				requestedBullets = bp.zutiBulletsSupply;
				bp.zutiBulletsSupply = 0;
			}

			System.out.println("  Bullets were deducted from HB at x=" + (int)bp.place.x + ", y=" + (int)bp.place.y + ". New bullets amount: " + bp.zutiBulletsSupply);
		}
		else if( armyForLocation == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
		{
			//Manage side resources
			if( requestedBullets <= Mission.MDS_VARIABLES().zutiBulletsSupply_Red )
			{
				Mission.MDS_VARIABLES().zutiBulletsSupply_Red -= requestedBullets;
			}
			else
			{
				requestedBullets = (int)Mission.MDS_VARIABLES().zutiBulletsSupply_Red;
				Mission.MDS_VARIABLES().zutiBulletsSupply_Red = 0;
			}
			
			System.out.println("  Bullets were deducted from RED side. New bullets amount: " + Mission.MDS_VARIABLES().zutiBulletsSupply_Red);
		}
		else if( armyForLocation == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
		{
			//Manage side resources
			if( requestedBullets <= Mission.MDS_VARIABLES().zutiBulletsSupply_Blue )
			{
				Mission.MDS_VARIABLES().zutiBulletsSupply_Blue -= requestedBullets;
			}
			else
			{
				requestedBullets = (int)Mission.MDS_VARIABLES().zutiBulletsSupply_Blue;
				Mission.MDS_VARIABLES().zutiBulletsSupply_Blue = 0;
			}
			System.out.println("  Bullets were deducted from BLUE side. New bullets amount: " + Mission.MDS_VARIABLES().zutiBulletsSupply_Blue);
		}
		
		System.out.println("Returning bullets: " + requestedBullets);
		return requestedBullets;
	}
	
	/**
	 * Method searches for born place near given coordinates and checks if it has
	 * enough bullets to serve to the player. Its bullets supply counter is appropriately 
	 * adjusted.
	 * @param requestedBullets
	 * @param x
	 * @param y
	 * @return
	 */
	public static int[] getAvailableBombsAtLocation(int[] requestedBombs, double x, double y)
	{
		System.out.println("Requesting bombs by weight: ");
		System.out.println("   	 250kg: " + requestedBombs[0]);
		System.out.println("   	 500kg: " + requestedBombs[1]);
		System.out.println("   	1000kg: " + requestedBombs[2]);
		System.out.println("   	2000kg: " + requestedBombs[3]);
		System.out.println("   	5000kg: " + requestedBombs[4]);
		System.out.println("   	  more: " + requestedBombs[5]);
		
		if( !Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases )
		{
			System.out.println("Returning same amount of bombs as were requested!");
			return requestedBombs;
		}
		
		BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
		int armyForLocation = Front.army(x, y);
		
		if( bp != null && bp.zutiEnableResourcesManagement )
		{
			if( requestedBombs[0] <= bp.zutiBombsSupply[0] )
			{
				bp.zutiBombsSupply[0] -= requestedBombs[0];
			}
			else
			{
				requestedBombs[0] =  bp.zutiBombsSupply[0];
				bp.zutiBombsSupply[0] = 0;
			}
			
			if( requestedBombs[1] <= bp.zutiBombsSupply[1] )
			{
				bp.zutiBombsSupply[1] -= requestedBombs[1];
			}
			else
			{
				requestedBombs[1] =  bp.zutiBombsSupply[1];
				bp.zutiBombsSupply[1] = 0;
			}
			
			if( requestedBombs[2] <= bp.zutiBombsSupply[2] )
			{
				bp.zutiBombsSupply[2] -= requestedBombs[2];
			}
			else
			{
				requestedBombs[2] =  bp.zutiBombsSupply[2];
				bp.zutiBombsSupply[2] = 0;
			}
			
			if( requestedBombs[3] <= bp.zutiBombsSupply[3] )
			{
				bp.zutiBombsSupply[3] -= requestedBombs[3];
			}
			else
			{
				requestedBombs[3] =  bp.zutiBombsSupply[3];
				bp.zutiBombsSupply[3] = 0;
			}
			
			if( requestedBombs[4] <= bp.zutiBombsSupply[4] )
			{
				bp.zutiBombsSupply[4] -= requestedBombs[4];
			}
			else
			{
				requestedBombs[4] =  bp.zutiBombsSupply[4];
				bp.zutiBombsSupply[4] = 0;
			}
			
			if( requestedBombs[5] <= bp.zutiBombsSupply[5] )
			{
				 bp.zutiBombsSupply[5] -= requestedBombs[5];
			}
			else
			{
				requestedBombs[5] =  bp.zutiBombsSupply[5];
				bp.zutiBombsSupply[5] = 0;
			}
			
			System.out.println("  Bombs were deducted from HB at x=" + (int)bp.place.x + ", y=" + (int)bp.place.y + ".");
			System.out.println("  New bombs by weight for HB: ");
			System.out.println("   	 250kg: " + bp.zutiBombsSupply[0]);
			System.out.println("   	 500kg: " + bp.zutiBombsSupply[1]);
			System.out.println("   	1000kg: " + bp.zutiBombsSupply[2]);
			System.out.println("   	2000kg: " + bp.zutiBombsSupply[3]);
			System.out.println("   	5000kg: " + bp.zutiBombsSupply[4]);
			System.out.println("   	  more: " + bp.zutiBombsSupply[5]);
			
		}
		else if( armyForLocation == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
		{			
			//Manage side resources
			if( requestedBombs[0] <= Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] -= requestedBombs[0];
			}
			else
			{
				requestedBombs[0] =   Mission.MDS_VARIABLES().zutiBombsSupply_Red[0];
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] = 0;
			}
			
			if( requestedBombs[1] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] -= requestedBombs[1];
			}
			else
			{
				requestedBombs[1] =   Mission.MDS_VARIABLES().zutiBombsSupply_Red[1];
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] = 0;
			}
			
			if( requestedBombs[2] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] -= requestedBombs[2];
			}
			else
			{
				requestedBombs[2] =   Mission.MDS_VARIABLES().zutiBombsSupply_Red[2];
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] = 0;
			}
			
			if( requestedBombs[3] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] -= requestedBombs[3];
			}
			else
			{
				requestedBombs[3] =   Mission.MDS_VARIABLES().zutiBombsSupply_Red[3];
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] = 0;
			}
			
			if( requestedBombs[4] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] -= requestedBombs[4];
			}
			else
			{
				requestedBombs[4] =   Mission.MDS_VARIABLES().zutiBombsSupply_Red[4];
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] = 0;
			}
			
			if( requestedBombs[5] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] -= requestedBombs[5];
			}
			else
			{
				requestedBombs[5] =   Mission.MDS_VARIABLES().zutiBombsSupply_Red[5];
				Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] = 0;
			}
			
			System.out.println("  Bombs were deducted from RED side.");
			System.out.println("  New bombs by weight: ");
			System.out.println("   	 250kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[0]);
			System.out.println("   	 500kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[1]);
			System.out.println("   	1000kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[2]);
			System.out.println("   	2000kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[3]);
			System.out.println("   	5000kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[4]);
			System.out.println("   	  more: " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[5]);
		}
		else if( armyForLocation == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
		{
			//Manage side resources
			if( requestedBombs[0] <= Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] -= requestedBombs[0];
			}
			else
			{
				requestedBombs[0] =   Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0];
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] = 0;
			}
			
			if( requestedBombs[1] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] -= requestedBombs[1];
			}
			else
			{
				requestedBombs[1] =   Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1];
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] = 0;
			}
			
			if( requestedBombs[2] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] )
			{
				  Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] -= requestedBombs[2];
			}
			else
			{
				requestedBombs[2] =   Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2];
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] = 0;
			}
			
			if( requestedBombs[3] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] -= requestedBombs[3];
			}
			else
			{
				requestedBombs[3] =   Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3];
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] = 0;
			}
			
			if( requestedBombs[4] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] )
			{
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] -= requestedBombs[4];
			}
			else
			{
				requestedBombs[4] =   Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4];
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] = 0;
			}
			
			if( requestedBombs[5] <=  Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] )
			{
				  Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] -= requestedBombs[5];
			}
			else
			{
				requestedBombs[5] =   Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5];
				Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] = 0;
			}
			
			System.out.println("  Bombs were deducted from BLUE side.");
			System.out.println("  New bombs by weight: ");
			System.out.println("   	 250kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0]);
			System.out.println("   	 500kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1]);
			System.out.println("   	1000kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2]);
			System.out.println("   	2000kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3]);
			System.out.println("   	5000kg: " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4]);
			System.out.println("   	  more: " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5]);
		}
		
		System.out.println("Returning bombs by weight: ");
		System.out.println("   	 250kg: " + requestedBombs[0]);
		System.out.println("   	 500kg: " + requestedBombs[1]);
		System.out.println("   	1000kg: " + requestedBombs[2]);
		System.out.println("   	2000kg: " + requestedBombs[3]);
		System.out.println("   	5000kg: " + requestedBombs[4]);
		System.out.println("   	  more: " + requestedBombs[5]);
		
		return requestedBombs;
	}
	
	/**
	 * Method searches for born place near given coordinates and checks if it has
	 * enough rockets to serve to the player. Its rockets supply counter is appropriately 
	 * adjusted.
	 * @param requestedRockets
	 * @param x
	 * @param y
	 * @return
	 */
	public static long getAvailableRocketsAtLocation(long requestedRockets, double x, double y)
	{
		System.out.println("Requesting rockets: " + requestedRockets);
		if( !Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases )
		{
			System.out.println("Returning rockets: " + requestedRockets);
			return requestedRockets;
		}
		
		BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
		int armyForLocation = Front.army(x, y);
		
		if( bp != null && bp.zutiEnableResourcesManagement )
		{
			if( requestedRockets <= bp.zutiRocketsSupply )
			{
				bp.zutiRocketsSupply -= requestedRockets;
			}
			else
			{
				requestedRockets = bp.zutiRocketsSupply;
				bp.zutiRocketsSupply = 0;
			}
			
			System.out.println("  Rockets were deducted from HB at x=" + (int)bp.place.x + ", y=" + (int)bp.place.y + ". New rockets amount: " + bp.zutiRocketsSupply);
		}
		else if( armyForLocation == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
		{
			//Manage side resources
			if( requestedRockets <= Mission.MDS_VARIABLES().zutiRocketsSupply_Red )
			{
				Mission.MDS_VARIABLES().zutiRocketsSupply_Red -= requestedRockets;
			}
			else
			{
				requestedRockets = (int)Mission.MDS_VARIABLES().zutiRocketsSupply_Red;
				Mission.MDS_VARIABLES().zutiRocketsSupply_Red = 0;
			}
			System.out.println("  Rockets were deducted from RED side. New rockets amount: " + Mission.MDS_VARIABLES().zutiRocketsSupply_Red);
		}
		else if( armyForLocation == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
		{
			//Manage side resources
			if( requestedRockets <= Mission.MDS_VARIABLES().zutiRocketsSupply_Blue )
			{
				Mission.MDS_VARIABLES().zutiRocketsSupply_Blue -= requestedRockets;
			}
			else
			{
				requestedRockets = (int)Mission.MDS_VARIABLES().zutiRocketsSupply_Blue;
				Mission.MDS_VARIABLES().zutiRocketsSupply_Blue = 0;
			}
			System.out.println("  Rockets were deducted from RED side. New rockets amount: " + Mission.MDS_VARIABLES().zutiRocketsSupply_Blue);
		}
		
		System.out.println("Returning rockets: " + requestedRockets);
		return requestedRockets;
	}

	/**
	 * Method analyzes home base resources and calculates number or cargo "bombs" that can be generated
	 * from them based on player side and side/home base resources.
	 * @param requestedCargo
	 * @param x
	 * @param y
	 * @return
	 */
	public static long getAvailableCargoAtLocation(long requestedCargo, double x, double y)
	{
		System.out.println("Requesting cargo: " + requestedCargo);
		if( !Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases )
		{
			System.out.println("Returning cargo: " + requestedCargo);
			return requestedCargo;
		}
		
		BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
		int armyForLocation = Front.army(x, y);
		
		int availableCargo = 0;
		
		for( int i=0; i<requestedCargo; i++ )
		{
			if( bp != null && bp.zutiEnableResourcesManagement )
			{
				if( checkHomeBaseForCargoResources(bp) )
				{
						availableCargo++;
				}
			}
			else if( armyForLocation == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
			{
				if( checkRedSideForCargoResources() )
					availableCargo++;
			}
			else if( armyForLocation == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
			{
				if( checkBlueSideForCargoResources() )
					availableCargo++;	
			}
		}
		
		if( bp != null && bp.zutiEnableResourcesManagement )
		{
			System.out.println("Cargo resources were taken from HB:");
			printOutResourcesForHomeBase(bp);
		}
		else if( Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide || Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
		{
			System.out.println("Cargo resources were taken from side:");
			printOutResourcesForSide(armyForLocation);
		}
		
		System.out.println("Returning cargo: " + availableCargo);
		return availableCargo;
	}
	
	/**
	 * Method checks cargo resources for specified home base
	 * @param bp
	 * @return
	 */
	private static boolean checkHomeBaseForCargoResources(BornPlace bp)
	{
		long bullets = 0;
		long rockets = 0;
		int[] bombs = new int[]{0,0,0,0,0,0};
		long fuel = 0;
		long engines = 0;
		long repairKits = 0;
		
		//Manage side resources - BLUE
		String cargoName = ZutiSupportMethods_Builder.BOMB_CARGO_NAME;
		switch( bp.army )
		{
			case 1:
				cargoName += "_Red";
				break;
			case 2:
				cargoName += "_Blue";
				break;
		}
		
		RRRItem cargoItem = (RRRItem)bp.objectsMap.get(cargoName);
		
		if( cargoItem == null )
			return false;
		
		bullets = cargoItem.bullets;
		rockets = cargoItem.rockets;
		bombs[0] = (int)cargoItem.bomb250;
		bombs[1] = (int)cargoItem.bomb500;
		bombs[2] = (int)cargoItem.bomb1000;
		bombs[3] = (int)cargoItem.bomb2000;
		bombs[4] = (int)cargoItem.bomb5000;
		bombs[5] = (int)cargoItem.bomb9999;
		fuel = cargoItem.fuel;
		engines = cargoItem.engines;
		repairKits = cargoItem.repairKits;
				
		if( bullets > bp.zutiBulletsSupply )
		{
			return false;
		}
		
		if( rockets > bp.zutiRocketsSupply )
		{
			return false;
		}
		
		if( bombs[0] > bp.zutiBombsSupply[0] )
		{
			return false;
		}
		
		if( bombs[1] > bp.zutiBombsSupply[1] )
		{
			return false;
		}
		
		if( bombs[2] > bp.zutiBombsSupply[2] )
		{
			return false;
		}
		
		if( bombs[3] > bp.zutiBombsSupply[3] )
		{
			return false;
		}
		
		if( bombs[4] > bp.zutiBombsSupply[4] )
		{
			return false;
		}
		
		if( bombs[5] > bp.zutiBombsSupply[5] )
		{
			return false;
		}
		
		if( fuel > bp.zutiFuelSupply )
		{
			return false;
		}
		
		if( engines > bp.zutiEnginesSupply )
		{
			return false;
		}
		
		if( repairKits > bp.zutiRepairKitsSupply )
		{
			return false;
		}

		bp.zutiBulletsSupply -= bullets;
		bp.zutiRocketsSupply -= rockets;
		bp.zutiBombsSupply[0] -= bombs[0];
		bp.zutiBombsSupply[1] -= bombs[1];
		bp.zutiBombsSupply[2] -= bombs[2];
		bp.zutiBombsSupply[3] -= bombs[3];
		bp.zutiBombsSupply[4] -= bombs[4];
		bp.zutiBombsSupply[5] -= bombs[5];
		bp.zutiFuelSupply -= fuel;
		bp.zutiEnginesSupply -= engines;
		bp.zutiRepairKitsSupply -= repairKits;
		
		return true;
	}	
	private static boolean checkRedSideForCargoResources()
	{
		long bullets = 0;
		long rockets = 0;
		int[] bombs = new int[]{0,0,0,0,0,0};
		long fuel = 0;
		long engines = 0;
		long repairKits = 0;
		
		//Manage side resources - BLUE
		String cargoName = ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Red";
		RRRItem cargoItem = (RRRItem)Mission.MDS_VARIABLES().objectsMap_Red.get(cargoName);
		
		if( cargoItem == null )
			return false;
		
		bullets = cargoItem.bullets;
		rockets = cargoItem.rockets;
		bombs[0] = (int)cargoItem.bomb250;
		bombs[1] = (int)cargoItem.bomb500;
		bombs[2] = (int)cargoItem.bomb1000;
		bombs[3] = (int)cargoItem.bomb2000;
		bombs[4] = (int)cargoItem.bomb5000;
		bombs[5] = (int)cargoItem.bomb9999;
		fuel = cargoItem.fuel;
		engines = cargoItem.engines;
		repairKits = cargoItem.repairKits;
				
		if( bullets > Mission.MDS_VARIABLES().zutiBulletsSupply_Red )
		{
			return false;
		}
		
		if( rockets > Mission.MDS_VARIABLES().zutiRocketsSupply_Red )
		{
			return false;
		}
		
		if( bombs[0] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] )
		{
			return false;
		}
		
		if( bombs[1] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] )
		{
			return false;
		}
		
		if( bombs[2] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] )
		{
			return false;
		}
		
		if( bombs[3] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] )
		{
			return false;
		}
		
		if( bombs[4] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] )
		{
			return false;
		}
		
		if( bombs[5] > Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] )
		{
			return false;
		}
		
		if( fuel > Mission.MDS_VARIABLES().zutiFuelSupply_Red )
		{
			return false;
		}
		
		if( engines > Mission.MDS_VARIABLES().zutiEnginesSupply_Red )
		{
			return false;
		}
		
		if( repairKits > Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red )
		{
			return false;
		}

		Mission.MDS_VARIABLES().zutiBulletsSupply_Red -= bullets;
		Mission.MDS_VARIABLES().zutiRocketsSupply_Red -= rockets;
		Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] -= bombs[0];
		Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] -= bombs[1];
		Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] -= bombs[2];
		Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] -= bombs[3];
		Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] -= bombs[4];
		Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] -= bombs[5];
		Mission.MDS_VARIABLES().zutiFuelSupply_Red -= fuel;
		Mission.MDS_VARIABLES().zutiEnginesSupply_Red -= engines;
		Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red -= repairKits;
		
		return true;
	}
	private static boolean checkBlueSideForCargoResources()
	{
		long bullets = 0;
		long rockets = 0;
		int[] bombs = new int[]{0,0,0,0,0,0};
		long fuel = 0;
		long engines = 0;
		long repairKits = 0;
		
		//Manage side resources - BLUE
		String cargoName = ZutiSupportMethods_Builder.BOMB_CARGO_NAME + "_Blue";
		RRRItem cargoItem = (RRRItem)Mission.MDS_VARIABLES().objectsMap_Blue.get(cargoName);
		
		if( cargoItem == null )
			return false;
		
		bullets = cargoItem.bullets;
		rockets = cargoItem.rockets;
		bombs[0] = (int)cargoItem.bomb250;
		bombs[1] = (int)cargoItem.bomb500;
		bombs[2] = (int)cargoItem.bomb1000;
		bombs[3] = (int)cargoItem.bomb2000;
		bombs[4] = (int)cargoItem.bomb5000;
		bombs[5] = (int)cargoItem.bomb9999;
		fuel = cargoItem.fuel;
		engines = cargoItem.engines;
		repairKits = cargoItem.repairKits;
				
		if( bullets > Mission.MDS_VARIABLES().zutiBulletsSupply_Blue )
		{
			return false;
		}
		
		if( rockets > Mission.MDS_VARIABLES().zutiRocketsSupply_Blue )
		{
			return false;
		}
		
		if( bombs[0] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] )
		{
			return false;
		}
		
		if( bombs[1] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] )
		{
			return false;
		}
		
		if( bombs[2] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] )
		{
			return false;
		}
		
		if( bombs[3] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] )
		{
			return false;
		}
		
		if( bombs[4] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] )
		{
			return false;
		}
		
		if( bombs[5] > Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] )
		{
			return false;
		}
		
		if( fuel > Mission.MDS_VARIABLES().zutiFuelSupply_Blue )
		{
			return false;
		}
		
		if( engines > Mission.MDS_VARIABLES().zutiEnginesSupply_Blue )
		{
			return false;
		}
		
		if( repairKits > Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue )
		{
			return false;
		}

		Mission.MDS_VARIABLES().zutiBulletsSupply_Blue -= bullets;
		Mission.MDS_VARIABLES().zutiRocketsSupply_Blue -= rockets;
		Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] -= bombs[0];
		Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] -= bombs[1];
		Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] -= bombs[2];
		Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] -= bombs[3];
		Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] -= bombs[4];
		Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] -= bombs[5];
		Mission.MDS_VARIABLES().zutiFuelSupply_Blue -= fuel;
		Mission.MDS_VARIABLES().zutiEnginesSupply_Blue -= engines;
		Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue -= repairKits;
		
		return true;
	}
	
	/**
	 * Method searches for born place near given coordinates and checks if it has
	 * enough engines to serve to the player. Its rockets supply counter is appropriately 
	 * adjusted.
	 * @param requestedEngines
	 * @param x
	 * @param y
	 * @return
	 */
	public static long getAvailableEnginesAtLocation(long requestedEngines, double x, double y)
	{
		System.out.println("Requesting engines: " + requestedEngines);
		if( !Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases )
		{
			System.out.println("Returning engines: " + requestedEngines);
			return requestedEngines;
		}
				
		BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
		int armyForLocation = Front.army(x, y);
		
		if( bp != null && bp.zutiEnableResourcesManagement )
		{
			if( requestedEngines <= bp.zutiEnginesSupply )
			{
				bp.zutiEnginesSupply -= requestedEngines;
			}
			else
			{
				requestedEngines = bp.zutiEnginesSupply;
				bp.zutiEnginesSupply = 0;
			}
			
			System.out.println("  Engines were deducted from HB at x=" + (int)bp.place.x + ", y=" + (int)bp.place.y + ". New engines amount: " + bp.zutiEnginesSupply);
		}
		else if( armyForLocation == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
		{			
			//Manage side resources
			if( requestedEngines <= Mission.MDS_VARIABLES().zutiEnginesSupply_Red )
			{
				Mission.MDS_VARIABLES().zutiEnginesSupply_Red -= requestedEngines;
			}
			else
			{
				requestedEngines = Mission.MDS_VARIABLES().zutiEnginesSupply_Red;
				Mission.MDS_VARIABLES().zutiEnginesSupply_Red = 0;
			}
			System.out.println("  Engines were deducted from RED side. New engines amount: " + Mission.MDS_VARIABLES().zutiEnginesSupply_Red);
		}
		else if( armyForLocation == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
		{
			//Manage side resources
			if( requestedEngines <= Mission.MDS_VARIABLES().zutiEnginesSupply_Blue )
			{
				Mission.MDS_VARIABLES().zutiEnginesSupply_Blue -= requestedEngines;
			}
			else
			{
				requestedEngines = Mission.MDS_VARIABLES().zutiEnginesSupply_Blue;
				Mission.MDS_VARIABLES().zutiEnginesSupply_Blue = 0;
			}
			System.out.println("  Engines were deducted from BLUE side. New engines amount: " + Mission.MDS_VARIABLES().zutiEnginesSupply_Blue);
		}
		
		System.out.println("Returning engines: " + requestedEngines);
		return requestedEngines;
	}

	/**
	 * Method searches for born place near given coordinates and checks if it has
	 * enough repair kits to serve to the player. Its repair kits supply counter is appropriately 
	 * adjusted.
	 * @param requestedEngines
	 * @param x
	 * @param y
	 * @return
	 */
	public static long getAvailableRepairKitsAtLocation(long requestedKits, double x, double y)
	{
		System.out.println("Requesting repair kits: " + requestedKits);
		if( !Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases )
		{
			System.out.println("Returning repair kits: " + requestedKits);
			return requestedKits;
		}
		
		BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
		int armyForLocation = Front.army(x, y);
		
		if( bp != null && bp.zutiEnableResourcesManagement )
		{
			//Born place has resource management... nice.
			if( requestedKits <= bp.zutiRepairKitsSupply )
			{
				bp.zutiRepairKitsSupply -= requestedKits;
			}
			else
			{
				requestedKits = (int)bp.zutiRepairKitsSupply;
				bp.zutiRepairKitsSupply = 0;
			}
			System.out.println("  Repair kits were deducted from HB at x=" + (int)bp.place.x + ", y=" + (int)bp.place.y + ". New repair kits amount: " + bp.zutiRepairKitsSupply);
		}
		else if( armyForLocation == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
		{			
			//Manage side resources
			if( requestedKits <= Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red )
			{
				Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red -= requestedKits;
			}
			else
			{
				requestedKits = (int)Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red;
				Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red = 0;
			}
			System.out.println("  Repair kits were deducted from RED side. New repair kits amount: " + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red);
		}
		else if( armyForLocation == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
		{
			//Manage side resources
			if( requestedKits <= Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue )
			{
				Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue -= requestedKits;
			}
			else
			{
				requestedKits = (int)Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue;
				Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue = 0;
			}
			System.out.println("  Repair kits were deducted from BLUE side. New repair kits amount: " + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue);
		}
		
		System.out.println("Returning repair kits: " + requestedKits);
		return requestedKits;
	}

	/**
	 * Call this method when a object is destroyed.
	 * @param objectName
	 */
	public static void reduceResources(House house, boolean printResults)
	{
		String objectName = house.name();
		//System.out.println("Checking object: " + objectName);
		if( ZutiSupportMethods.isAmmoBoxObject(objectName) || ZutiSupportMethods.isFuelTankObject(objectName) || ZutiSupportMethods.isWorkshopObject(objectName) )
		{
			objectName = objectName.substring(objectName.indexOf("$")+1, objectName.length());
			RRRItem rrrItem = null;
			Point3d housePosition = house.pos.getAbsPoint();
			boolean updateSideResources = true;
			
			//Check if destroyed object was part of any home base. If so, reduce resources at that home base.
			//----------------------------------------------------------------
			if( LAST_RESOURCES_BORN_PLACE != null )
			{
				boolean isOnBornPlace = ZutiSupportMethods_Net.isOnBornPlace(housePosition.x, housePosition.y, LAST_RESOURCES_BORN_PLACE);
				if( isOnBornPlace && LAST_RESOURCES_BORN_PLACE.zutiEnableResourcesManagement )
				{
					//OK, this home base has resources management... update resources for it.
					reduceHomeBaseResources(LAST_RESOURCES_BORN_PLACE, objectName, printResults);
					
					updateSideResources = false;
				}
				else
				{
					//It is not on current known home base, try searching for new one
					BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(housePosition.x, housePosition.y);
					if( bp != null && bp.zutiEnableResourcesManagement )
					{
						//Yes, house was part of born place and it has resources management, update it...
						reduceHomeBaseResources(bp, objectName, printResults);
						//Set it as new last known home base
						LAST_RESOURCES_BORN_PLACE = bp;
						
						updateSideResources = false;
					}
				}
			}
			else
			{
				//No known home base, search for possible one.
				//It is not on current known home base, try searching for new one
				BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(housePosition.x, housePosition.y);
				if( bp != null && bp.zutiEnableResourcesManagement )
				{
					//Yes, house was part of born place and it has resources management, update it...
					reduceHomeBaseResources(bp, objectName, printResults);
					//Set it as new last known home base
					LAST_RESOURCES_BORN_PLACE = bp;
					
					updateSideResources = false;
				}
			}
			//----------------------------------------------------------------
			int army = Front.army(housePosition.x, housePosition.y);
			if( updateSideResources && army == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
			{
				rrrItem = (RRRItem)Mission.MDS_VARIABLES().objectsMap_Red.get(objectName);
				if( rrrItem != null )
				{
					//Ok, objects were loaded into has, now it's time that we update counters...
					Mission.MDS_VARIABLES().zutiBulletsSupply_Red -= rrrItem.bullets;
					Mission.MDS_VARIABLES().zutiRocketsSupply_Red -= rrrItem.rockets;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] -= rrrItem.bomb250;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] -= rrrItem.bomb500;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] -= rrrItem.bomb1000;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] -= rrrItem.bomb2000;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] -= rrrItem.bomb5000;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] -= rrrItem.bomb9999;
					Mission.MDS_VARIABLES().zutiFuelSupply_Red -= rrrItem.fuel;
					Mission.MDS_VARIABLES().zutiEnginesSupply_Red -= rrrItem.engines;
					Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red -= rrrItem.repairKits;
				}
			}
			else if( updateSideResources && army == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
			{
				rrrItem = (RRRItem)Mission.MDS_VARIABLES().objectsMap_Blue.get(objectName);
				if( rrrItem != null )
				{
					//Ok, objects were loaded into has, now it's time that we update counters...
					Mission.MDS_VARIABLES().zutiBulletsSupply_Blue -= rrrItem.bullets;
					Mission.MDS_VARIABLES().zutiRocketsSupply_Blue -= rrrItem.rockets;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] -= rrrItem.bomb250;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] -= rrrItem.bomb500;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] -= rrrItem.bomb1000;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] -= rrrItem.bomb2000;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] -= rrrItem.bomb5000;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] -= rrrItem.bomb9999;
					Mission.MDS_VARIABLES().zutiFuelSupply_Blue -= rrrItem.fuel;
					Mission.MDS_VARIABLES().zutiEnginesSupply_Blue -= rrrItem.engines;
					Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue -= rrrItem.repairKits;
				}
			}
				
			if( printResults )
				printOutResourcesForSide(army);
		}
	}
	
	/**
	 * Call this method when a moving object reaches it's final destination.
	 * @param chiefName
	 * @param chiefPosition
	 * @param printResults
	 */
	public static void addResourcesFromMovingRRRObjects(String chiefName, Point3d chiefPosition, int chiefArmy, float survivability, boolean printResults)
	{
		//System.out.println("Checking moving object: " + chiefName + ", survivability: " + survivability);
		if( ZutiSupportMethods.isMovingRRRObject(chiefName, null) )
		{
			RRRItem rrrItem = null;
			boolean updateSideResources = true;
			
			//Check if destroyed object was part of any home base. If so, reduce resources at that home base.
			//----------------------------------------------------------------
			if( LAST_RESOURCES_BORN_PLACE != null )
			{
				boolean isOnBornPlace = ZutiSupportMethods_Net.isOnBornPlace(chiefPosition.x, chiefPosition.y, LAST_RESOURCES_BORN_PLACE);
				if( isOnBornPlace && LAST_RESOURCES_BORN_PLACE.zutiEnableResourcesManagement )
				{
					//TODO: OK, this home base has resources management... update resources for it.
					addResourcesToHomeBase(LAST_RESOURCES_BORN_PLACE, chiefArmy, chiefName, survivability, printResults);
					
					updateSideResources = false;
				}
				else
				{
					//It is not on current known home base, try searching for new one
					BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(chiefPosition.x, chiefPosition.y);
					if( bp != null && bp.zutiEnableResourcesManagement )
					{
						//Yes, house was part of born place and it has resources management, update it...
						addResourcesToHomeBase(bp, chiefArmy, chiefName, survivability, printResults);
						//Set it as new last known home base
						LAST_RESOURCES_BORN_PLACE = bp;
						
						updateSideResources = false;
					}
				}
			}
			else
			{
				//No known home base, search for possible one.
				//It is not on current known home base, try searching for new one
				BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(chiefPosition.x, chiefPosition.y);
				if( bp != null && bp.zutiEnableResourcesManagement )
				{
					//Yes, house was part of born place and it has resources management, update it...
					addResourcesToHomeBase(bp, chiefArmy, chiefName, survivability, printResults);
					//Set it as new last known home base
					LAST_RESOURCES_BORN_PLACE = bp;
					
					updateSideResources = false;
				}
			}
			//----------------------------------------------------------------
			if( updateSideResources && chiefArmy == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide && Mission.MDS_VARIABLES().objectsMap_Red != null )
			{
				rrrItem = (RRRItem)Mission.MDS_VARIABLES().objectsMap_Red.get(chiefName);
				if( rrrItem != null )
				{
					//Ok, objects were loaded into has, now it's time that we update counters...
					Mission.MDS_VARIABLES().zutiBulletsSupply_Red += rrrItem.bullets*survivability;
					Mission.MDS_VARIABLES().zutiRocketsSupply_Red += rrrItem.rockets*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[0] += rrrItem.bomb250*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[1] += rrrItem.bomb500*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[2] += rrrItem.bomb1000*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[3] += rrrItem.bomb2000*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[4] += rrrItem.bomb5000*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Red[5] += rrrItem.bomb9999*survivability;
					Mission.MDS_VARIABLES().zutiFuelSupply_Red += rrrItem.fuel*survivability;
					Mission.MDS_VARIABLES().zutiEnginesSupply_Red += rrrItem.engines*survivability;
					Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red += rrrItem.repairKits*survivability;
				}
			}
			else if( updateSideResources && chiefArmy == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide && Mission.MDS_VARIABLES().objectsMap_Blue != null )
			{
				rrrItem = (RRRItem)Mission.MDS_VARIABLES().objectsMap_Blue.get(chiefName);
				if( rrrItem != null )
				{
					//Ok, objects were loaded into has, now it's time that we update counters...
					Mission.MDS_VARIABLES().zutiBulletsSupply_Blue += rrrItem.bullets*survivability;
					Mission.MDS_VARIABLES().zutiRocketsSupply_Blue += rrrItem.rockets*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0] += rrrItem.bomb250*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1] += rrrItem.bomb500*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2] += rrrItem.bomb1000*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3] += rrrItem.bomb2000*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4] += rrrItem.bomb5000*survivability;
					Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5] += rrrItem.bomb9999*survivability;
					Mission.MDS_VARIABLES().zutiFuelSupply_Blue += rrrItem.fuel*survivability;
					Mission.MDS_VARIABLES().zutiEnginesSupply_Blue += rrrItem.engines*survivability;
					Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue += rrrItem.repairKits*survivability;
				}
			}
			
			if( printResults )
				printOutResourcesForSide(chiefArmy);
		}
	}
	
	/**
	 * Reduce resources for given home base.
	 * @param bp
	 * @param objectName
	 * @param printResults
	 */
	private static void reduceHomeBaseResources(BornPlace bp, String objectName, boolean printResults)
	{
		if( bp == null || bp.objectsMap == null || objectName == null )
		{
			//System.out.println("BP = NULL!");
			return;
		}
		
		RRRItem rrrItem = (RRRItem)bp.objectsMap.get(objectName);
		if( rrrItem != null )
		{
			//Ok, objects were loaded into has, now it's time that we update counters...
			bp.zutiBulletsSupply -= rrrItem.bullets;
			bp.zutiRocketsSupply -= rrrItem.rockets;
			bp.zutiBombsSupply[0] -= rrrItem.bomb250;
			bp.zutiBombsSupply[1] -= rrrItem.bomb500;
			bp.zutiBombsSupply[2] -= rrrItem.bomb1000;
			bp.zutiBombsSupply[3] -= rrrItem.bomb2000;
			bp.zutiBombsSupply[4] -= rrrItem.bomb5000;
			bp.zutiBombsSupply[5] -= rrrItem.bomb9999;
			bp.zutiFuelSupply -= rrrItem.fuel;
			bp.zutiEnginesSupply -= rrrItem.engines;
			bp.zutiRepairKitsSupply -= rrrItem.repairKits;
			
			if( printResults )
			{
				printOutResourcesForHomeBase(bp);
			}
		}
	}
	
	/**
	 * Add resources for given home base from moving object.
	 * @param bp
	 * @param objectName
	 * @param printResults
	 */
	private static void addResourcesToHomeBase(BornPlace bp, int objectArmy, String objectName, float survivability, boolean printResults)
	{
		if( bp == null || objectName == null )
		{
			//System.out.println("BP = NULL!");
			return;
		}
		
		Map objectsMap = null;
		switch( objectArmy )
		{
			case 1:
				objectsMap = Mission.MDS_VARIABLES().objectsMap_Red;
				break;
			case 2:
				objectsMap = Mission.MDS_VARIABLES().objectsMap_Blue;
				break;
		}
		
		if( objectsMap == null )
			return;
		
		RRRItem rrrItem = (RRRItem)objectsMap.get(objectName);
		if( rrrItem != null )
		{
			//Ok, objects were loaded into has, now it's time that we update counters...
			//System.out.println("Current BP Bullets: " + bp.zutiBulletsSupply + ", convoy bullets: " + rrrItem.bullets + ", convoy survivabilits: " + survivability);
			
			bp.zutiBulletsSupply += rrrItem.bullets*survivability;
			bp.zutiRocketsSupply += rrrItem.rockets*survivability;
			bp.zutiBombsSupply[0] += rrrItem.bomb250*survivability;
			bp.zutiBombsSupply[1] += rrrItem.bomb500*survivability;
			bp.zutiBombsSupply[2] += rrrItem.bomb1000*survivability;
			bp.zutiBombsSupply[3] += rrrItem.bomb2000*survivability;
			bp.zutiBombsSupply[4] += rrrItem.bomb5000*survivability;
			bp.zutiBombsSupply[5] += rrrItem.bomb9999*survivability;
			bp.zutiFuelSupply += rrrItem.fuel*survivability;
			bp.zutiEnginesSupply += rrrItem.engines*survivability;
			bp.zutiRepairKitsSupply += rrrItem.repairKits*survivability;
			
			if( printResults )
			{
				printOutResourcesForHomeBase(bp);
			}
		}
	}
	
	/**
	 * Get string array holding information about resources at specified location.
	 * @param x
	 * @param y
	 * @return
	 */
	public static String[] getResourcesInformationForLocation(double x, double y)
	{
		String[] result = new String[14];
		if( !Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide && !Mission.MDS_VARIABLES().enabledResourcesManagement_HomeBases )
		{
			result[0] = "No resources information available for specified location!";
			return result;
		}
		
		BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(x, y);
		int armyForLocation = Front.army(x, y);
		
		if( bp != null && bp.zutiEnableResourcesManagement )
		{
			result[0]  = "==========RECEIVED RESOURCES INFORMATION==========";
			result[1]  = "Resources status - Home Base(" + (int)bp.place.x + ", " + (int)bp.place.y + ")";
			result[2]  = "  Bullets       supply=" + bp.zutiBulletsSupply;
			result[3]  = "  Rockets       supply=" + bp.zutiRocketsSupply;
			result[4]  = "  Bombs{250kg}  supply=" + bp.zutiBombsSupply[0];
			result[5]  = "  Bombs{500kg}  supply=" + bp.zutiBombsSupply[1];
			result[6]  = "  Bombs{1000kg} supply=" + bp.zutiBombsSupply[2];
			result[7]  = "  Bombs{2000kg} supply=" + bp.zutiBombsSupply[3];
			result[8]  = "  Bombs{5000kg} supply=" + bp.zutiBombsSupply[4];
			result[9]  = "  Bombs{9999kg} supply=" + bp.zutiBombsSupply[5];
			result[10]  = "  Fuel          supply=" + bp.zutiFuelSupply;
			result[11] = "  Engines       supply=" + bp.zutiEnginesSupply;
			result[12] = "  Repair kits   supply=" + bp.zutiRepairKitsSupply;
			result[13]  = "==================================================";
		}
		else if( armyForLocation == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
		{
			result[0]  = "==========RECEIVED RESOURCES INFORMATION==========";
			result[1]  = "Resources - Red side";
			result[2]  = "  Bullets  = " + Mission.MDS_VARIABLES().zutiBulletsSupply_Red;
			result[3]  = "  Rockets  = " + Mission.MDS_VARIABLES().zutiRocketsSupply_Red;
			result[4]  = "  Bomb250  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[0];
			result[5]  = "  Bomb500  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[1];
			result[6]  = "  Bomb1000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[2];
			result[7]  = "  Bomb2000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[3];
			result[8]  = "  Bomb5000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[4];
			result[9]  = "  Bomb9999 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[5];
			result[10]  = "  Fuel     = " + Mission.MDS_VARIABLES().zutiFuelSupply_Red;
			result[11] = "  Engines  = " + Mission.MDS_VARIABLES().zutiEnginesSupply_Red;
			result[12] = "  Repairs  = " + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red;
			result[13]  = "==================================================";
		}
		else if( armyForLocation == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
		{
			result[0]  = "==========RECEIVED RESOURCES INFORMATION==========";
			result[1]  = "Resources - Blue side";
			result[2]  = "  Bullets  = " + Mission.MDS_VARIABLES().zutiBulletsSupply_Blue;
			result[3]  = "  Rockets  = " + Mission.MDS_VARIABLES().zutiRocketsSupply_Blue;
			result[4]  = "  Bomb250  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0];
			result[5]  = "  Bomb500  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1];
			result[6]  = "  Bomb1000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2];
			result[7]  = "  Bomb2000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3];
			result[8]  = "  Bomb5000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4];
			result[9]  = "  Bomb9999 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5];
			result[10]  = "  Fuel     = " + Mission.MDS_VARIABLES().zutiFuelSupply_Blue;
			result[11] = "  Engines  = " + Mission.MDS_VARIABLES().zutiEnginesSupply_Blue;
			result[12] = "  Repairs  = " + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue;
			result[13]  = "==================================================";
		}
		else
			result[0] = "No resources information available for specified location!";
		
		return result;
	}
	
	/**
	 * Prints to console resources status for specified home base!
	 * @param bp
	 */
	public static void printOutResourcesForHomeBase(BornPlace bp)
	{
		if( bp != null && bp.zutiEnableResourcesManagement )
		{
			System.out.println("Resources status - Home Base(" + (int)bp.place.x + ", " + (int)bp.place.y + ")");
			System.out.println("  Bullets       supply=" + bp.zutiBulletsSupply);
			System.out.println("  Rockets       supply=" + bp.zutiRocketsSupply);
			System.out.println("  Bombs{250kg}  supply=" + bp.zutiBombsSupply[0]);
			System.out.println("  Bombs{500kg}  supply=" + bp.zutiBombsSupply[1]);
			System.out.println("  Bombs{1000kg} supply=" + bp.zutiBombsSupply[2]);
			System.out.println("  Bombs{2000kg} supply=" + bp.zutiBombsSupply[3]);
			System.out.println("  Bombs{5000kg} supply=" + bp.zutiBombsSupply[4]);
			System.out.println("  Bombs{9999kg} supply=" + bp.zutiBombsSupply[5]);
			System.out.println("  Fuel          supply=" + bp.zutiFuelSupply);
			System.out.println("  Engines       supply=" + bp.zutiEnginesSupply);
			System.out.println("  Repair kits   supply=" + bp.zutiRepairKitsSupply);
			System.out.println("======================================================");
		}
	}
	
	/**
	 * Prints to console resources status for specified side!
	 * @param bp
	 */
	public static void printOutResourcesForSide(int army)
	{
		if( army == 1 && Mission.MDS_VARIABLES().enabledResourcesManagement_RedSide )
		{
			System.out.println("Resources - Red side");
			System.out.println("  Bullets  = " + Mission.MDS_VARIABLES().zutiBulletsSupply_Red);
			System.out.println("  Rockets  = " + Mission.MDS_VARIABLES().zutiRocketsSupply_Red);
			System.out.println("  Bomb250  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[0]);
			System.out.println("  Bomb500  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[1]);
			System.out.println("  Bomb1000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[2]);
			System.out.println("  Bomb2000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[3]);
			System.out.println("  Bomb5000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[4]);
			System.out.println("  Bomb9999 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Red[5]);
			System.out.println("  Fuel     = " + Mission.MDS_VARIABLES().zutiFuelSupply_Red);
			System.out.println("  Engines  = " + Mission.MDS_VARIABLES().zutiEnginesSupply_Red);
			System.out.println("  Repairs  = " + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Red);
			System.out.println("================================");
		}
		else if( army == 2 && Mission.MDS_VARIABLES().enabledResourcesManagement_BlueSide )
		{
			System.out.println("Resources - Blue side");
			System.out.println("  Bullets  = " + Mission.MDS_VARIABLES().zutiBulletsSupply_Blue);
			System.out.println("  Rockets  = " + Mission.MDS_VARIABLES().zutiRocketsSupply_Blue);
			System.out.println("  Bomb250  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[0]);
			System.out.println("  Bomb500  = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[1]);
			System.out.println("  Bomb1000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[2]);
			System.out.println("  Bomb2000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[3]);
			System.out.println("  Bomb5000 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[4]);
			System.out.println("  Bomb9999 = " + Mission.MDS_VARIABLES().zutiBombsSupply_Blue[5]);
			System.out.println("  Fuel     = " + Mission.MDS_VARIABLES().zutiFuelSupply_Blue);
			System.out.println("  Engines  = " + Mission.MDS_VARIABLES().zutiEnginesSupply_Blue);
			System.out.println("  Repairs  = " + Mission.MDS_VARIABLES().zutiRepairKitsSupply_Blue);
			System.out.println("================================");
		}
	}
}