package mainController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.ini4j.Ini;
import org.json.JSONException;
import org.json.JSONObject;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import com.neovisionaries.i18n.CountryCode;

class PilotGeoIPController {
    private static boolean      useNewGeoIP = false;
    private static String       apiKey      = null;
    private static final String GEOIP_INI   = "geoip.ini";

    public static LookupService initialize(String geoDBFile) {
        LookupService newLookupService = null;

        try {
            newLookupService = new LookupService(geoDBFile, LookupService.GEOIP_MEMORY_CACHE);
            MainController.writeDebugLogFile(2, "PilotGeoIPController.initialize(): LookupService initialized.");
        } catch (IOException e) {
            MainController.writeDebugLogFile(1, "PilotGeoIPController.initialize(): IOException!\n" + e.toString());
        }

        return newLookupService;
    }

    public static String getCountry(String ipAddress) {
        if (useNewGeoIP)
            return getCountryNew(ipAddress);

        String country = null;

        if (MainController.IPLOOKUPSERVICE != null) {
            country = MainController.IPLOOKUPSERVICE.getCountry(ipAddress).getName();
        } else {
            MainController.writeDebugLogFile(1, "PilotGeoIP.getCountry: LookupService is null!");
        }

        return country;
    }

    public static String getGeoIPInfo(String ipAddress, String infoName) {
        if (useNewGeoIP)
            return getGeoIPInfoNew(ipAddress, infoName);

        String info = "N/A";

        try {
            if (MainController.IPLOOKUPSERVICE != null) {
                Location locationInfo = MainController.IPLOOKUPSERVICE.getLocation(ipAddress);
                if (infoName.equalsIgnoreCase("country")) {
                    info = locationInfo.countryName;
                } else if (infoName.equalsIgnoreCase("city")) {
                    info = locationInfo.city;
                } else if (infoName.equalsIgnoreCase("countrycode")) {
                    info = locationInfo.countryCode;
                } else if (infoName.equalsIgnoreCase("regionName")) {
                    info = regionName.regionNameByCode(locationInfo.countryCode, locationInfo.region);
                } else if (infoName.equalsIgnoreCase("region")) {
                    info = locationInfo.region;
                } else {
                    MainController.writeDebugLogFile(1, "PilotGeoIPController.getGeoIPInfo: Invalid Info (" + infoName + ")");
                }

            } else {
                MainController.writeDebugLogFile(1, "PilotGeoIP.getGeoIPInfo: LookupService is null!");
            }

            return info;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotGeoIPController.getGeoIPInfo - No IP Data for IP(" + ipAddress + ") info (" + infoName + ")");
            return "N/A";
        }
    }

    public static String getCountryNew(String ipAddress) {
        String retVal = null;
        try {
            JSONObject json = readJsonFromUrl(urlFromIp(ipAddress));
            retVal = CountryCode.getByCode(json.getString("country")).getName();
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController:");
            ex.printStackTrace();
        }
        return retVal;
    }

    public static String getGeoIPInfoNew(String ipAddress, String infoName) {
        String retVal = "N/A";
        try {
            JSONObject json = readJsonFromUrl(urlFromIp(ipAddress));
            if (infoName.equalsIgnoreCase("country")) {
//				retVal = json.getString("country");
                retVal = CountryCode.getByCode(json.getString("country")).getName();
            } else if (infoName.equalsIgnoreCase("city")) {
                retVal = json.getString("city");
            } else if (infoName.equalsIgnoreCase("countrycode")) {
                retVal = json.getString("country");
            } else if (infoName.equalsIgnoreCase("regionName")) {
                retVal = json.getString("stateprov");
            } else if (infoName.equalsIgnoreCase("region")) {
                retVal = json.getString("stateprov");
            } else {
                MainController.writeDebugLogFile(1, "PilotGeoIPController.getGeoIPInfo: Invalid Info (" + infoName + ")");
            }
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController:");
            ex.printStackTrace();
        }
        return retVal;
    }

    private static String urlFromIp(String ipAddress) {
        return "http://api.db-ip.com/addrinfo?addr=" + ipAddress + "&api_key=" + apiKey;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    static {
        File geoIpIniFile = new File(GEOIP_INI);
        try {
            Ini ini = new Ini(new FileReader(geoIpIniFile));
            Ini.Section section = ini.get("geoip");
            useNewGeoIP = section.get("useNewGeoIP").equalsIgnoreCase("true") ? true : false;
            apiKey = section.get("apiKey");
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController:");
            ioe.printStackTrace();
        }
    }

//	public static void main(String[] args) {
////		String ip = "46.59.244.8";
//		String ip = "84.189.74.137";
//		System.out.println("Country: " + getCountry(ip));
//		System.out.println("Country Info: " + getGeoIPInfoNew(ip, "country"));
//		System.out.println("City: " + getGeoIPInfoNew(ip, "city"));
//		System.out.println("Country Code: " + getGeoIPInfoNew(ip, "countrycode"));
//		System.out.println("Region Name: " + getGeoIPInfoNew(ip, "regionName"));
//		System.out.println("Region: " + getGeoIPInfoNew(ip, "region"));
//		
//	}

}
