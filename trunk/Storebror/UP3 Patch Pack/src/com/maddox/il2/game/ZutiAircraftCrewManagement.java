package com.maddox.il2.game;

import java.util.HashMap;
import java.util.Map;

public class ZutiAircraftCrewManagement
{
	private static Map AIRCRAFT_CREW = new HashMap();
	
	/**
	 * Clears AIRCRAFT_CREW map.
	 */
	public static void resetMainMap()
	{
		AIRCRAFT_CREW.clear();
	}
	
	/**
	 * Method returns aircraft crew object for given aircraft. If such object does not
	 * exist, new one is created.
	 * @param acName
	 * @return
	 */
	public static ZutiAircraftCrew getAircraftCrew(String acName)
	{
		ZutiAircraftCrew result = null;
		if( AIRCRAFT_CREW != null )
		{
			result = (ZutiAircraftCrew)AIRCRAFT_CREW.get(acName);
		}
		
		if( result == null )
		{
			//crew for given AC does not yet exist, create it
			result = new ZutiAircraftCrew(acName);
			AIRCRAFT_CREW.put(acName, result);
		}
		
		return result;
	}
	
	/**
	 * This method removes aircraft and it's crew information from AIRCRAFT_CREW map.
	 * @param acName
	 */
	public static void removeAircraft(String acName)
	{
		//System.out.println("----------------------ZutiAircraftCrewManagement - Removing AC: " + acName);
		if( AIRCRAFT_CREW != null && AIRCRAFT_CREW.containsKey(acName) )
		{
			AIRCRAFT_CREW.remove(acName);
//			System.out.println("ZutiAircraftCrewManagement - removeAircraft: Aircraft removed >" + acName);
		}
	}
}