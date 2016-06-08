package com.maddox.il2.game;

import java.util.HashMap;
import java.util.Map;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.NetAircraft.AircraftNet;
import com.maddox.rts.NetEnv;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class ZutiAircraftCrewManagement {
    private static Map AIRCRAFT_CREW = new HashMap();

    /**
     * Clears AIRCRAFT_CREW map.
     */
    public static void resetMainMap() {
        AIRCRAFT_CREW.clear();
    }

    /**
     * Method returns aircraft crew object for given aircraft. If such object does not
     * exist, new one is created.
     * 
     * @param acName
     * @return
     */
    public static ZutiAircraftCrew getAircraftCrew(String acName) {
        ZutiAircraftCrew result = null;
        if (AIRCRAFT_CREW != null) {
            result = (ZutiAircraftCrew) AIRCRAFT_CREW.get(acName);
        }

        if (result == null) {
            // crew for given AC does not yet exist, create it
            result = new ZutiAircraftCrew(acName);
            AIRCRAFT_CREW.put(acName, result);
        }

        return result;
    }

    /**
     * This method removes aircraft and it's crew information from AIRCRAFT_CREW map.
     * 
     * @param acName
     */
    public static void removeAircraft(String acName) {
//      System.out.println("----------------------ZutiAircraftCrewManagement - Removing AC: " + acName);

        // TODO: Storebror: Track Users hitting refly where this shouldn't be possible
        NetUser netuser = null;
        NetAircraft netaircraft = null;
        if (NetEnv.isServer()) {
            netaircraft = (NetAircraft) Actor.getByName(acName);
            do {
                if (netaircraft == null)
                    break;
                netuser = ((AircraftNet) (netaircraft.net)).netUser;
                if (netuser == null)
                    break;
                System.out.println("----------------------ZutiAircraftCrewManagement - Removing AC: " + acName);
                System.out.println("netaircraft = " + netaircraft.name());
                System.out.println("user = " + netuser.uniqueName());
                boolean isAircraftValid = Actor.isValid(netaircraft);
                System.out.println("Is valid = " + isAircraftValid);
                if (!isAircraftValid)
                    break;
                boolean isAircraftAlive = netaircraft.isAlive();
                System.out.println("Is alive = " + isAircraftAlive);
                if (!isAircraftAlive)
                    break;
                System.out.println("Takeoff_N_Landing = " + World.cur().diffCur.Takeoff_N_Landing);
                if (!World.cur().diffCur.Takeoff_N_Landing)
                    break;
                boolean isPilotDead = netaircraft.FM.AS.isPilotDead(0);
                System.out.println("Is Pilot dead = " + isPilotDead);
                byte astateBailoutStep = netaircraft.FM.AS.astateBailoutStep;
                System.out.println("astateBailoutStep = " + astateBailoutStep);
                boolean isPilotParatrooper = netaircraft.FM.AS.isPilotParatrooper(0);
                System.out.println("Is Pilot Paratrooper = " + isPilotParatrooper);
                boolean isAircraftOnGround = netaircraft.FM.Gears.onGround();
                System.out.println("Is on ground = " + isAircraftOnGround);
                boolean isAircraftStandingStill = netaircraft.FM.getSpeedKMH() < 10F;
                System.out.println("Speed (kmh)= " + netaircraft.FM.getSpeedKMH());
                boolean isAircraftOnChocks = netaircraft.FM.brakeShoe;
                System.out.println("Chocks engaged = " + isAircraftOnChocks);
                boolean isAircraftGearDamaged = netaircraft.FM.Gears.isAnyDamaged();
                System.out.println("Gear damaged = " + isAircraftGearDamaged);
                boolean bDamagedGround = Reflection.getBoolean(netaircraft.FM, "bDamagedGround");
                System.out.println("bDamagedGround = " + bDamagedGround);
                boolean isReflyOverride = NetAircraft.ZUTI_REFLY_OWERRIDE;
                System.out.println("Refly override = " + isReflyOverride);
                boolean isLandedOnWater = false;
                Point3d pos = netaircraft.pos.getAbsPoint();
                if (pos != null && netaircraft.FM.getSpeedKMH() < 100F && netaircraft.FM.getAltitude() < 50F && World.land().isWater(pos.x, pos.y))
                    isLandedOnWater = true;
                System.out.println("Landed on water = " + isLandedOnWater);
                
                if (isReflyOverride)
                    break;
                if (isAircraftOnGround && isAircraftStandingStill)
                    break;
                if (isPilotDead)
                    break;
                if (isPilotParatrooper)
                    break;
                if (astateBailoutStep > 10)
                    break;
                if (isAircraftOnChocks)
                    break;
                if (isAircraftGearDamaged && isAircraftOnGround)
                    break;
                if (isLandedOnWater)
                    break;
                if (bDamagedGround)
                    break;
                System.out.println("This could become a suspicious refly attempt, current time is " + Time.current());
                netuser.setLastSuspiciousPreRefly(Time.current());
                
                System.out.println("*********************************************************************************");
                System.out.println("*                 additional Aircraft/Player State Information                  *");
                System.out.println("*********************************************************************************");
                
                System.out.println("isCapableOfACM = " + netaircraft.FM.isCapableOfACM());
                System.out.println("isCapableOfBMP = " + netaircraft.FM.isCapableOfBMP());
                System.out.println("isCapableOfTaxiing = " + netaircraft.FM.isCapableOfTaxiing());
                System.out.println("isReadyToDie = " + netaircraft.FM.isReadyToDie());
                System.out.println("isReadyToReturn = " + netaircraft.FM.isReadyToReturn());
                System.out.println("isTakenMortalDamage = " + netaircraft.FM.isTakenMortalDamage());
                System.out.println("isStationedOnGround = " + netaircraft.FM.isStationedOnGround());
                System.out.println("isCrashedOnGround = " + netaircraft.FM.isCrashedOnGround());
                System.out.println("isNearAirdrome = " + netaircraft.FM.isNearAirdrome());
                System.out.println("isCrossCountry = " + netaircraft.FM.isCrossCountry());
                System.out.println("isWasAirborne = " + netaircraft.FM.isWasAirborne());
                System.out.println("isSentWingNote = " + netaircraft.FM.isSentWingNote());
                System.out.println("isSentBuryNote = " + netaircraft.FM.isSentBuryNote());
                System.out.println("isSentControlsOutNote = " + netaircraft.FM.isSentControlsOutNote());
                System.out.println("bDamaged = " + Reflection.getBoolean(netaircraft.FM, "bDamaged"));
                System.out.println("flags0 = " + Reflection.getLong(netaircraft.FM, "flags0"));
                
                dumpArray(netaircraft.FM.AS.astateBleedingNext, "astateBleedingNext contents:");
                dumpArray(netaircraft.FM.AS.astateBleedingTimes, "astateBleedingTimes contents:");
                dumpArray(netaircraft.FM.AS.astateEngineStates, "astateEngineStates contents:");
                dumpArray(netaircraft.FM.AS.astateOilStates, "astateOilStates contents:");
                dumpArray(netaircraft.FM.AS.astatePilotStates, "astatePilotStates contents:");
                dumpArray(netaircraft.FM.AS.astateSootStates, "astateSootStates contents:");
                dumpArray(netaircraft.FM.AS.astateTankStates, "astateTankStates contents:");
                
                System.out.println("astateCockpitState = " + netaircraft.FM.AS.astateCockpitState);
                System.out.println("astatePlayerIndex = " + netaircraft.FM.AS.astatePlayerIndex);
                System.out.println("bIsAboutToBailout = " + netaircraft.FM.AS.bIsAboutToBailout);
                System.out.println("bIsEnableToBailout = " + netaircraft.FM.AS.bIsEnableToBailout);
                System.out.println("bShowSmokesOn = " + netaircraft.FM.AS.bShowSmokesOn);
                System.out.println("bNavLightsOn = " + netaircraft.FM.AS.bNavLightsOn);
                System.out.println("bLandingLightOn = " + netaircraft.FM.AS.bLandingLightOn);
                System.out.println("bWingTipLExists = " + netaircraft.FM.AS.bWingTipLExists);
                System.out.println("bWingTipRExists = " + netaircraft.FM.AS.bWingTipRExists);
                System.out.println("bIsAboveCriticalSpeed = " + Reflection.getBoolean(netaircraft.FM, "bIsAboveCriticalSpeed"));
                System.out.println("bIsAboveCondensateAlt = " + Reflection.getBoolean(netaircraft.FM, "bIsAboveCondensateAlt"));
                System.out.println("bIsOnInadequateAOA = " + Reflection.getBoolean(netaircraft.FM, "bIsOnInadequateAOA"));
                System.out.println("legsWounded = " + Reflection.getBoolean(netaircraft.FM, "legsWounded"));
                System.out.println("armsWounded = " + Reflection.getBoolean(netaircraft.FM, "armsWounded"));
            } while (false);
        }
        // ---

        if (AIRCRAFT_CREW != null && AIRCRAFT_CREW.containsKey(acName)) {
            AIRCRAFT_CREW.remove(acName);
            if (netaircraft != null && netaircraft.isNetPlayer()) // Patch Pack 107, reduce logging, don't show removed AI planes
                System.out.println("ZutiAircraftCrewManagement - removeAircraft: Aircraft removed >" + acName);
        }
    }
    
    // TODO: Storebror: Track Users hitting refly where this shouldn't be possible
    private static final long REFLY_TRACKING_TIME   = 300000L; // 5 Minutes
    
    public static void dumpArray(long[] theArray, String theMessage) {
        System.out.println(theMessage);
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<theArray.length; i++) {
            if (i>0) sb.append(", ");
            sb.append(theArray[i]);
        }
        System.out.println(sb.toString());
    }

    public static void dumpArray(byte[] theArray, String theMessage) {
        System.out.println(theMessage);
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<theArray.length; i++) {
            if (i>0) sb.append(", ");
            sb.append(theArray[i]);
        }
        System.out.println(sb.toString());
    }

    public static boolean isSuspiciousRefly(byte byte0, NetUser netuser) {
        if (!NetEnv.isServer()) return false;
        if (byte0 != EventLog.REFLY) return false;
        if (netuser.getLastSuspiciousPreRefly() == 0L) return false;
        if (Time.current() < netuser.getLastSuspiciousPreRefly()) return false;
        if (Time.current() - netuser.getLastSuspiciousPreRefly() > REFLY_TRACKING_TIME) return false;
        if (RTSConf.cur.console.getEnv().existAtom("reflykick", true)) {
            if (RTSConf.cur.console.getEnv().atom("reflykick").toString().trim().equalsIgnoreCase("on")) {
                
                System.out.println(" ****************************************************************************************************************************************** ");
                System.out.println("********************************************************************************************************************************************");
                System.out.println("**                                                                                                                                        **");
                System.out.println("**                                                       !!! REFLY KICK !!!                                                               **");
                System.out.println("**                                                                                                                                        **");
                System.out.println("********************************************************************************************************************************************");
                System.out.println(" ****************************************************************************************************************************************** ");
                System.out.println();
                System.out.println("Time: " + Time.current());
                System.out.println("ZutiAircraftCrewManagement isSuspiciousRefly(" + byte0 + ", " + netuser.uniqueName() + ") = true and reflykick is on, player will be kicked!");
                return true;
            }
        }
        System.out.println("************************************");
        System.out.println("*     --- SUSPICIOUS REFLY ---     *");
        System.out.println("************************************");
        System.out.println();
        System.out.println("ZutiAircraftCrewManagement isSuspiciousRefly(" + byte0 + ", " + netuser.uniqueName() + ") = true but reflykick isn't on, player will stay!");
        return false;
    }
    // ---

}