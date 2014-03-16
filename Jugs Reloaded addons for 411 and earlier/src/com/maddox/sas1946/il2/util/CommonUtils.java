package com.maddox.sas1946.il2.util;

import java.util.StringTokenizer;

import com.maddox.il2.engine.Config;

public class CommonUtils {
	private static float fConfigVersion = -99.999F;
	private static float ConfigVersionToFloat() throws NumberFormatException {
		if (fConfigVersion > 0.0F) return fConfigVersion;
		String theVersion = "4.00m";
		try {
			theVersion = (String)Config.class.getDeclaredField("VERSION").get(null);
		} catch (Exception e) {
			System.out.println("Couldn't resolve game version due to following error:");
			e.printStackTrace();
			return fConfigVersion;
		}
		StringTokenizer versionParts = new StringTokenizer(theVersion.trim(), "\\.");
		if (versionParts.countTokens() < 1) return 0.0F;
		if (versionParts.countTokens() < 2) return Float.parseFloat(versionParts.nextToken());
		String versionToParse = versionParts.nextToken() + ".";
	    while (versionParts.hasMoreTokens())
	    	versionToParse += versionParts.nextToken();
    	if (versionToParse.endsWith("m")) {
    		versionToParse = versionToParse.substring(0, versionToParse.length()-1);
    	}
    	versionToParse += "F";
		fConfigVersion = Float.parseFloat(versionToParse);
    	System.out.println("SAS Common Utils Game Version = " + fConfigVersion);
		return fConfigVersion;
	}
	public static boolean is409() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.0899F && ConfigVersionToFloat() < 4.0999F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is410() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.0999F && ConfigVersionToFloat() < 4.1099F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4100() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.0999F && ConfigVersionToFloat() < 4.1009F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4101() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1009F && ConfigVersionToFloat() < 4.1019F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is411() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1099F && ConfigVersionToFloat() < 4.1199F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4110() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1099F && ConfigVersionToFloat() < 4.1109F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4111() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1109F && ConfigVersionToFloat() < 4.1119F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is412() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1199F && ConfigVersionToFloat() < 4.1299F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4120() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1199F && ConfigVersionToFloat() < 4.1209F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4121() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1209F && ConfigVersionToFloat() < 4.1219F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4122() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1219F && ConfigVersionToFloat() < 4.1229F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is413() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1299F && ConfigVersionToFloat() < 4.1399F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is414() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1399F && ConfigVersionToFloat() < 4.1499F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is415() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1499F && ConfigVersionToFloat() < 4.1599F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is416() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1599F && ConfigVersionToFloat() < 4.1699F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is417() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1699F && ConfigVersionToFloat() < 4.1799F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is418() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1799F && ConfigVersionToFloat() < 4.1899F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is419() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1899F && ConfigVersionToFloat() < 4.1999F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is420() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1999F && ConfigVersionToFloat() < 4.2099F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}


	public static boolean is409orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.0899F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is410orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.0999F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4100orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.0999F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4101orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1009F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is411orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1099F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4110orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1099F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4111orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1109F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is412orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1199F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4120orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1199F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4121orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1209F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is4122orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1219F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is413orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1299F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is414orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1399F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is415orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1499F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is416orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1599F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is417orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1699F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is418orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1799F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is419orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1899F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}
	public static boolean is420orLater() {
		boolean retVal = false;
		try {
			if (ConfigVersionToFloat() > 4.1999F) retVal = true;			
		} catch (Exception e) {}
		return retVal;
	}

	public static float floatindex(float f, float af[])
    {
        int i = (int)f;
        if(i >= af.length - 1)
            return af[af.length - 1];
        if(i < 0)
            return af[0];
        if(i == 0)
        {
            if(f > 0.0F)
                return af[0] + f * (af[1] - af[0]);
            else
                return af[0];
        } else
        {
            return af[i] + (f % (float)i) * (af[i + 1] - af[i]);
        }
    }

}
