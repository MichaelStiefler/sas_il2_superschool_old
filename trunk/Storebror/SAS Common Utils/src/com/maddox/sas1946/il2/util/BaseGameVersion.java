package com.maddox.sas1946.il2.util;

import java.util.StringTokenizer;

import com.maddox.il2.engine.Config;

/**
 * "BaseGameVersion" Class of the "SAS Common Utils"
 * <p>
 * This Class is used to get the stock game version of the instance of IL-2 which is currently running.
 * This information can be used to conditionally call methods or access fields depending on the base game version, therefore creating mods which can run on multiple different base game versions.
 * This Class is intended to be used with other classes of this package, e.g. the "Reflection" class e.g. in order to get access to fields which are not part of the base game version a mod is currently compiled against.
 * <p>
 * 
 * @version 1.0.6
 * @since 1.0.2
 * @author SAS~Storebror
 */
public class BaseGameVersion {

	/**
	 * Override default Constructor to avoid instanciation
	 * @throws Exception
     * @since 1.0.4
	 */
	private BaseGameVersion() throws Exception{
        throw new Exception("Class com.maddox.sas1946.il2.util.BaseGameVersion cannot be instanciated!");
	}
	
	// *****************************************************************************************************************************************************************************************************
	// Public interface section.
	// Methods and Arguments are supposed to be final here.
	// Take care of encapsulation and don't modify methods or arguments declared on this interface
	// to ensure future backward compatibility
	// If you need a new method with the same name but different parameters or return types,
	// simply overload the given methods in this interface.
	// *****************************************************************************************************************************************************************************************************

	/**
	 * Checks whether the running game is IL-2 4.09m
	 * 
	 * @return true if the running game is IL-2 4.09m
     * @since 1.0.2
	 */
	public static final boolean is409() {
		return (ConfigVersionToFloat() > 4.0899F && ConfigVersionToFloat() < 4.0999F);
	}

	/**
	 * Checks whether the running game is IL-2 4.10m or IL-2 4.10.1m
	 * 
	 * @return true if the running game is IL-2 4.10m or IL-2 4.10.1m
     * @since 1.0.2
	 */
	public static final boolean is410() {
		return (ConfigVersionToFloat() > 4.0999F && ConfigVersionToFloat() < 4.1099F);
	}

	/**
	 * Checks whether the running game is IL-2 4.10m (and NOT IL-2 4.10.1m)
	 * 
	 * @return true if the running game is IL-2 4.10m (and NOT IL-2 4.10.1m)
     * @since 1.0.2
	 */
	public static final boolean is4100() {
		return (ConfigVersionToFloat() > 4.0999F && ConfigVersionToFloat() < 4.1009F);
	}

	/**
	 * Checks whether the running game is IL-2 4.10.1m
	 * 
	 * @return true if the running game is IL-2 4.10.1m
     * @since 1.0.2
	 */
	public static final boolean is4101() {
		return (ConfigVersionToFloat() > 4.1009F && ConfigVersionToFloat() < 4.1019F);
	}

	/**
	 * Checks whether the running game is IL-2 4.11m or IL-2 4.11.1m
	 * 
	 * @return true if the running game is IL-2 4.11m or IL-2 4.11.1m
     * @since 1.0.2
	 */
	public static final boolean is411() {
		return (ConfigVersionToFloat() > 4.1099F && ConfigVersionToFloat() < 4.1199F);
	}

	/**
	 * Checks whether the running game is IL-2 4.11m (and NOT IL-2 4.11.1m)
	 * 
	 * @return true if the running game is IL-2 4.11m (and NOT IL-2 4.11.1m)
     * @since 1.0.2
	 */
	public static final boolean is4110() {
		return (ConfigVersionToFloat() > 4.1099F && ConfigVersionToFloat() < 4.1109F);
	}

	/**
	 * Checks whether the running game is IL-2 4.11.1m
	 * 
	 * @return true if the running game is IL-2 4.11.1m
     * @since 1.0.2
	 */
	public static final boolean is4111() {
		return (ConfigVersionToFloat() > 4.1109F && ConfigVersionToFloat() < 4.1119F);
	}

	/**
	 * Checks whether the running game is IL-2 4.12m or IL-2 4.12.1m or IL-2 4.12.2m
	 * 
	 * @return true if the running game is IL-2 4.12m or IL-2 4.12.1m or IL-2 4.12.2m
     * @since 1.0.2
	 */
	public static final boolean is412() {
		return (ConfigVersionToFloat() > 4.1199F && ConfigVersionToFloat() < 4.1299F);
	}

	/**
	 * Checks whether the running game is IL-2 4.12m (and neither IL-2 4.12.1m nor IL-2 4.12.2m)
	 * 
	 * @return true if the running game is IL-2 4.12m (and neither IL-2 4.12.1m nor IL-2 4.12.2m)
     * @since 1.0.2
	 */
	public static final boolean is4120() {
		return (ConfigVersionToFloat() > 4.1199F && ConfigVersionToFloat() < 4.1209F);
	}

	/**
	 * Checks whether the running game is IL-2 4.12.1m
	 * 
	 * @return true if the running game is IL-2 4.12.1m
     * @since 1.0.2
	 */
	public static final boolean is4121() {
		return (ConfigVersionToFloat() > 4.1209F && ConfigVersionToFloat() < 4.1219F);
	}

	/**
	 * Checks whether the running game is IL-2 4.12.2m
	 * 
	 * @return true if the running game is IL-2 4.12.2m
     * @since 1.0.2
	 */
	public static final boolean is4122() {
		return (ConfigVersionToFloat() > 4.1219F && ConfigVersionToFloat() < 4.1229F);
	}

	/**
	 * Checks whether the running game is IL-2 4.13m or IL-2 4.13.xm
	 * 
	 * @return true if the running game is IL-2 4.13m or IL-2 4.13.xm
     * @since 1.0.2
	 */
	public static final boolean is413() {
		return (ConfigVersionToFloat() > 4.1299F && ConfigVersionToFloat() < 4.1399F);
	}

	/**
	 * Checks whether the running game is IL-2 4.14m or IL-2 4.14.xm
	 * 
	 * @return true if the running game is IL-2 4.14m or IL-2 4.14.xm
     * @since 1.0.2
	 */
	public static final boolean is414() {
		return (ConfigVersionToFloat() > 4.1399F && ConfigVersionToFloat() < 4.1499F);
	}

	/**
	 * Checks whether the running game is IL-2 4.15m or IL-2 4.15.xm
	 * 
	 * @return true if the running game is IL-2 4.15m or IL-2 4.15.xm
     * @since 1.0.2
	 */
	public static final boolean is415() {
		return (ConfigVersionToFloat() > 4.1499F && ConfigVersionToFloat() < 4.1599F);
	}

	/**
	 * Checks whether the running game is IL-2 4.16m or IL-2 4.16.xm
	 * 
	 * @return true if the running game is IL-2 4.16m or IL-2 4.16.xm
     * @since 1.0.2
	 */
	public static final boolean is416() {
		return (ConfigVersionToFloat() > 4.1599F && ConfigVersionToFloat() < 4.1699F);
	}

	/**
	 * Checks whether the running game is IL-2 4.17m or IL-2 4.17.xm
	 * 
	 * @return true if the running game is IL-2 4.17m or IL-2 4.17.xm
     * @since 1.0.2
	 */
	public static final boolean is417() {
		return (ConfigVersionToFloat() > 4.1699F && ConfigVersionToFloat() < 4.1799F);
	}

	/**
	 * Checks whether the running game is IL-2 4.18m or IL-2 4.18.xm
	 * 
	 * @return true if the running game is IL-2 4.18m or IL-2 4.18.xm
     * @since 1.0.2
	 */
	public static final boolean is418() {
		return (ConfigVersionToFloat() > 4.1799F && ConfigVersionToFloat() < 4.1899F);
	}

	/**
	 * Checks whether the running game is IL-2 4.19m or IL-2 4.19.xm
	 * 
	 * @return true if the running game is IL-2 4.19m or IL-2 4.19.xm
     * @since 1.0.2
	 */
	public static final boolean is419() {
		return (ConfigVersionToFloat() > 4.1899F && ConfigVersionToFloat() < 4.1999F);
	}

	/**
	 * Checks whether the running game is IL-2 4.20m or IL-2 4.20.xm
	 * 
	 * @return true if the running game is IL-2 4.20m or IL-2 4.20.xm
     * @since 1.0.2
	 */
	public static final boolean is420() {
		return (ConfigVersionToFloat() > 4.1999F && ConfigVersionToFloat() < 4.2099F);
	}

	/**
	 * Checks whether the running game is IL-2 4.09m or later
	 * 
	 * @return true if the running game is IL-2 4.09m or later
     * @since 1.0.2
	 */
	public static final boolean is409orLater() {
		return (ConfigVersionToFloat() > 4.0899F);
	}

	/**
	 * Checks whether the running game is IL-2 4.10m or later
	 * 
	 * @return true if the running game is IL-2 4.10m or later
     * @since 1.0.2
	 */
	public static final boolean is410orLater() {
		return (ConfigVersionToFloat() > 4.0999F);
	}

	/**
	 * Checks whether the running game is IL-2 4.10m or later
	 * 
	 * @return true if the running game is IL-2 4.10m or later
     * @since 1.0.2
	 */
	public static final boolean is4100orLater() {
		return (ConfigVersionToFloat() > 4.0999F);
	}

	/**
	 * Checks whether the running game is IL-2 4.10.1m or later
	 * 
	 * @return true if the running game is IL-2 4.10.1m or later
     * @since 1.0.2
	 */
	public static final boolean is4101orLater() {
		return (ConfigVersionToFloat() > 4.1009F);
	}

	/**
	 * Checks whether the running game is IL-2 4.11m or later
	 * 
	 * @return true if the running game is IL-2 4.11m or later
     * @since 1.0.2
	 */
	public static final boolean is411orLater() {
		return (ConfigVersionToFloat() > 4.1099F);
	}

	/**
	 * Checks whether the running game is IL-2 4.11m or later
	 * 
	 * @return true if the running game is IL-2 4.11m or later
     * @since 1.0.2
	 */
	public static final boolean is4110orLater() {
		return (ConfigVersionToFloat() > 4.1099F);
	}

	/**
	 * Checks whether the running game is IL-2 4.11.1m or later
	 * 
	 * @return true if the running game is IL-2 4.11.1m or later
     * @since 1.0.2
	 */
	public static final boolean is4111orLater() {
		return (ConfigVersionToFloat() > 4.1109F);
	}

	/**
	 * Checks whether the running game is IL-2 4.12m or later
	 * 
	 * @return true if the running game is IL-2 4.12m or later
     * @since 1.0.2
	 */
	public static final boolean is412orLater() {
		return (ConfigVersionToFloat() > 4.1199F);
	}

	/**
	 * Checks whether the running game is IL-2 4.12m or later
	 * 
	 * @return true if the running game is IL-2 4.12m or later
     * @since 1.0.2
	 */
	public static final boolean is4120orLater() {
		return (ConfigVersionToFloat() > 4.1199F);
	}

	/**
	 * Checks whether the running game is IL-2 4.12.1m or later
	 * 
	 * @return true if the running game is IL-2 4.12.1m or later
     * @since 1.0.2
	 */
	public static final boolean is4121orLater() {
		return (ConfigVersionToFloat() > 4.1209F);
	}

	/**
	 * Checks whether the running game is IL-2 4.12.2m or later
	 * 
	 * @return true if the running game is IL-2 4.12.2m or later
     * @since 1.0.2
	 */
	public static final boolean is4122orLater() {
		return (ConfigVersionToFloat() > 4.1219F);
	}

	/**
	 * Checks whether the running game is IL-2 4.13m or later
	 * 
	 * @return true if the running game is IL-2 4.13m or later
     * @since 1.0.2
	 */
	public static final boolean is413orLater() {
		return (ConfigVersionToFloat() > 4.1299F);
	}

	/**
	 * Checks whether the running game is IL-2 4.14m or later
	 * 
	 * @return true if the running game is IL-2 4.14m or later
     * @since 1.0.2
	 */
	public static final boolean is414orLater() {
		return (ConfigVersionToFloat() > 4.1399F);
	}

	/**
	 * Checks whether the running game is IL-2 4.15m or later
	 * 
	 * @return true if the running game is IL-2 4.15m or later
     * @since 1.0.2
	 */
	public static final boolean is415orLater() {
		return (ConfigVersionToFloat() > 4.1499F);
	}

	/**
	 * Checks whether the running game is IL-2 4.16m or later
	 * 
	 * @return true if the running game is IL-2 4.16m or later
     * @since 1.0.2
	 */
	public static final boolean is416orLater() {
		return (ConfigVersionToFloat() > 4.1599F);
	}

	/**
	 * Checks whether the running game is IL-2 4.17m or later
	 * 
	 * @return true if the running game is IL-2 4.17m or later
     * @since 1.0.2
	 */
	public static final boolean is417orLater() {
		return (ConfigVersionToFloat() > 4.1699F);
	}

	/**
	 * Checks whether the running game is IL-2 4.18m or later
	 * 
	 * @return true if the running game is IL-2 4.18m or later
     * @since 1.0.2
	 */
	public static final boolean is418orLater() {
		return (ConfigVersionToFloat() > 4.1799F);
	}

	/**
	 * Checks whether the running game is IL-2 4.19m or later
	 * 
	 * @return true if the running game is IL-2 4.19m or later
     * @since 1.0.2
	 */
	public static final boolean is419orLater() {
		return (ConfigVersionToFloat() > 4.1899F);
	}

	/**
	 * Checks whether the running game is IL-2 4.20m or later
	 * 
	 * @return true if the running game is IL-2 4.20m or later
     * @since 1.0.2
	 */
	public static final boolean is420orLater() {
		return (ConfigVersionToFloat() > 4.1999F);
	}

	// *****************************************************************************************************************************************************************************************************
	// Private implementation section.
	// Do whatever you like here but keep it private to this class.
	// *****************************************************************************************************************************************************************************************************

	private static float fConfigVersion = -99.999F;

	private static float ConfigVersionToFloat() {
		if (fConfigVersion > 0.0F) return fConfigVersion;
		String theVersion = "4.00m";
		try {
			theVersion = (String) Config.class.getDeclaredField("VERSION").get(null);
		} catch (Exception e) {
			System.out.println("Couldn't resolve game version due to following error:");
			e.printStackTrace();
			return fConfigVersion;
		}
		StringTokenizer versionParts = new StringTokenizer(theVersion.trim(), "\\.");
		if (versionParts.countTokens() < 1) return 0.0F;
		if (versionParts.countTokens() < 2) return Float.parseFloat(versionParts.nextToken());
		String versionToParse = versionParts.nextToken() + ".";
		while (versionParts.hasMoreTokens()) {
			versionToParse += versionParts.nextToken();
		}
		if (versionToParse.endsWith("m")) {
			versionToParse = versionToParse.substring(0, versionToParse.length() - 1);
		}
		versionToParse += "F";
		fConfigVersion = Float.parseFloat(versionToParse);
		System.out.println("SAS Common Utils Game Version = " + fConfigVersion);
		return fConfigVersion;
	}

}
