package mainController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.ini4j.Ini;
import org.ini4j.Wini;
import org.json.JSONException;
import org.json.JSONObject;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import com.neovisionaries.i18n.CountryCode;

class PilotGeoIPController {
    private static boolean      useNewGeoIP = false;
    private static final String GEOIP_INI   = "geoip.ini";
    private static final String WHITELIST_INI   = "whitelist.ini";

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
        if (useNewGeoIP) {
            try {
                JSONObject json = readJsonIpInfo(ipAddress);
                return getGeoIPInfoNew(infoName, json);
            } catch (IOException ioe) {
                System.out.println("IO Exception occured in PilotGeoIPController:");
                ioe.printStackTrace();
                return "unknown";
            }
        }

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
            JSONObject json = readJsonIpInfo(ipAddress);
            retVal = CountryCode.getByCode(json.getString("country")).getName();
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController:");
            ex.printStackTrace();
        }
        return retVal;
    }


    public static String getGeoIPInfoNew(String infoName, JSONObject json) {
        String retVal = "N/A";
        try {
            if (infoName.equalsIgnoreCase("country")) {
                retVal = json.getString("country");
            } else if (infoName.equalsIgnoreCase("city")) {
                retVal = json.getString("city");
            } else if (infoName.equalsIgnoreCase("countryCode")) {
                retVal = json.getString("countryCode");
            } else if (infoName.equalsIgnoreCase("continent")) {
                retVal = json.getString("continent");
            } else if (infoName.equalsIgnoreCase("region")) {
                retVal = json.getString("region");
            } else if (infoName.equalsIgnoreCase("org")) {
                retVal = json.getString("org");
            } else if (infoName.equalsIgnoreCase("ipType")) {
                retVal = json.getString("ipType");
            } else if (infoName.equalsIgnoreCase("businessName")) {
                retVal = json.getString("businessName");
            } else if (infoName.equalsIgnoreCase("status")) {
                retVal = json.getString("status");
            } else if (infoName.equalsIgnoreCase("message")) {
                retVal = json.getString("message");
            } else {
                MainController.writeDebugLogFile(1, "PilotGeoIPController.getGeoIPInfo: Invalid Info (" + infoName + ")");
            }
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController:");
            ex.printStackTrace();
        }
        return retVal;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonIpInfo(String ipAddress) throws IOException, JSONException {
        try (InputStream is = new URL("http://extreme-ip-lookup.com/json/" + ipAddress).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController.readJsonIpInfo:");
            ex.printStackTrace();
            return null;
        }
    }

    public static JSONObject readJsonFromAbuseIpDb(String ip) {
        String abuseIpDbApiKey = abuseIpDbApiKey();
        if (abuseIpDbApiKey == null) return null;
        if (abuseIpDbApiKey.length() < 40) return null;
        HttpURLConnection con = null;
        try {
            URL abuseIpDbUrl = new URL("https://api.abuseipdb.com/api/v2/check?ipAddress="+ip+"&maxAgeInDays=" + abuseMaxAge());
            con = (HttpURLConnection) abuseIpDbUrl.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Key", abuseIpDbApiKey);
            con.setRequestProperty("Accept", "application/json");
            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);
            con.setDoInput(true);
           
            if (con.getResponseCode() != 200) return null;
            
            InputStream response = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response));

            String line = "";
            StringBuffer message = new StringBuffer();
            while ((line = reader.readLine()) != null)
            {
                message.append(line);
            }
            JSONObject json = new JSONObject(message.toString()).getJSONObject("data");
            return json;
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController.readJsonFromAbuseIpDb:");
            ex.printStackTrace();
            return null;
        } finally {
            if (con != null) con.disconnect();
        }
    }
    
    public static int abuseProbability(JSONObject json) {
        if (json == null) return 0;
        try {
            int retVal = json.getInt("abuseConfidenceScore");
            if (retVal < 0) retVal = 0;
            if (retVal > 100) retVal = 100;
            return retVal;
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController.abuseProbability:");
            ex.printStackTrace();
            return 0;
        }
    }

    public static JSONObject readJsonFromIpHub(String ip) {
        String ipHubApiKey = ipHubApiKey();
        if (ipHubApiKey == null) return null;
        if (ipHubApiKey.length() < 26) return null;
        HttpURLConnection con = null;
        try {
            URL abuseIpDbUrl = new URL("http://v2.api.iphub.info/ip/"+ip);
            con = (HttpURLConnection) abuseIpDbUrl.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Key", ipHubApiKey);
            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);
            con.setDoInput(true);
           
            if (con.getResponseCode() != 200) return null;
            
            InputStream response = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response));

            String line = "";
            StringBuffer message = new StringBuffer();
            while ((line = reader.readLine()) != null)
            {
                message.append(line);
            }
            JSONObject json = new JSONObject(message.toString());
            return json;
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController.readJsonFromAbuseIpDb:");
            ex.printStackTrace();
            return null;
        } finally {
            if (con != null) con.disconnect();
        }
    }
    
    public static boolean blockedByIpHub(JSONObject json) {
        if (json == null) return false;
        try {
             return ((json.getInt("block") & ipHubBlockBits()) != 0);
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController.abuseProbability:");
            ex.printStackTrace();
            return false;
        }
    }

    public static JSONObject readJsonFromIpQualityScore(String ip) {
        String ipQualityScoreApiKey = ipQualityScoreApiKey();
        if (ipQualityScoreApiKey == null) return null;
        if (ipQualityScoreApiKey.length() < 16) return null;
        try (InputStream is = new URL("https://ipqualityscore.com/api/json/ip/" + ipQualityScoreApiKey + "/" + ip + "?strictness=1&allow_public_access_points=true").openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController.readJsonFromIpQualityScore:");
            ex.printStackTrace();
            return null;
        }    
    }
    
    public static int ipQualityScoreFraudScore(JSONObject json) {
        if (json == null) return 0;
        try {
             return json.getInt("fraud_score");
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController.abuseProbability:");
            ex.printStackTrace();
            return 0;
        }
    }

    public static JSONObject readJsonFromIpIntel(String ip) {
        String getIpIntelContact = getIpIntelContact();
        if (getIpIntelContact == null) return null;
        if (getIpIntelContact.length() < 5) return null;
        try (InputStream is = new URL("http://check.getipintel.net/check.php?ip=" + ip + "&contact=" + getIpIntelContact + "&format=json").openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController.readJsonFromIpIntel:");
            ex.printStackTrace();
            return null;
        }    
    }
    
    public static int ipIntelResult(JSONObject json) {
        if (json == null) return 0;
        try {
            double d = json.getDouble("result");
            int retVal = (int)(d*100.0);
            if (retVal < 0) retVal = 0;
            if (retVal > 100) retVal = 100;
            return retVal;
        } catch (Exception ex) {
            System.out.println("Exception occured in PilotGeoIPController.abuseProbability:");
            ex.printStackTrace();
            return 0;
        }
    }
    
    private static String abuseIpDbApiKey() {
        try (FileReader iniFile = new FileReader(new File(GEOIP_INI))) {
            return new Wini(iniFile).get("geoip", "abuseIpDbApiKey");
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.abuseIpDbApiKey:");
            ioe.printStackTrace();
        }
        return null;
    }

    private static int abuseMaxAge() {
        try (FileReader iniFile = new FileReader(new File(GEOIP_INI))) {
            return new Wini(iniFile).get("geoip", "abuseMaxAge", int.class);
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.abuseMaxAge:");
            ioe.printStackTrace();
        }
        return 30;
    }

    public static int trustLevelLimit() {
        try (FileReader iniFile = new FileReader(new File(GEOIP_INI))) {
            return new Wini(iniFile).get("geoip", "trustLevelLimit", int.class);
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.trustLevelLimit:");
            ioe.printStackTrace();
        }
        return 50;
    }

    private static String ipHubApiKey() {
        try (FileReader iniFile = new FileReader(new File(GEOIP_INI))) {
            return new Wini(iniFile).get("geoip", "ipHubApiKey");
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.ipHubApiKey:");
            ioe.printStackTrace();
        }
        return null;
    }

    private static int ipHubBlockBits() {
        try (FileReader iniFile = new FileReader(new File(GEOIP_INI))) {
            return new Wini(iniFile).get("geoip", "ipHubBlockBits", int.class);
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.ipHubBlockBits:");
            ioe.printStackTrace();
        }
        return 1;
    }

    private static String ipQualityScoreApiKey() {
        try (FileReader iniFile = new FileReader(new File(GEOIP_INI))) {
            return new Wini(iniFile).get("geoip", "ipQualityScoreApiKey");
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.ipQualityScoreApiKey:");
            ioe.printStackTrace();
        }
        return null;
    }

    private static String getIpIntelContact() {
        try (FileReader iniFile = new FileReader(new File(GEOIP_INI))) {
            return new Wini(iniFile).get("geoip", "getIpIntelContact");
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.getIpIntelContact:");
            ioe.printStackTrace();
        }
        return null;
    }
    
    private static String createRegexFromGlob(String glob) {
        StringBuilder out = new StringBuilder("^");
        for(int i = 0; i < glob.length(); ++i) {
            final char c = glob.charAt(i);
            switch(c) {
                case '*': out.append(".*"); break;
                case '?': out.append('.'); break;
                case '.': out.append("\\."); break;
                case '\\': out.append("\\\\"); break;
                default: out.append(c);
            }
        }
        out.append('$');
        return out.toString();
    }
    
    public static boolean isWhitelisted(String s) {
        try (BufferedReader whitelistReader = new BufferedReader(new FileReader(new File(WHITELIST_INI)))) {
            String line = whitelistReader.readLine();
            while (line != null) {
                if (s.matches(createRegexFromGlob(line))) return true;
                line = whitelistReader.readLine();
            }
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.getIpIntelContact:");
            ioe.printStackTrace();
        }
        return false;
    }

    static {
        File geoIpIniFile = new File(GEOIP_INI);
        try {
            Ini ini = new Ini(new FileReader(geoIpIniFile));
            Ini.Section section = ini.get("geoip");
            useNewGeoIP = section.get("useNewGeoIP").equalsIgnoreCase("true") ? true : false;
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController:");
            ioe.printStackTrace();
        }
    }
    
    public static int trustLevel(String ipAddress) {
        JSONObject json = readJsonFromAbuseIpDb(ipAddress);
        int abuseProbability = abuseProbability(json);
        
        json = readJsonFromIpHub(ipAddress);
        boolean blockedByIpHub = blockedByIpHub(json);
        
        json = readJsonFromIpQualityScore(ipAddress);
        int ipQualityScoreFraudScore = ipQualityScoreFraudScore(json);
        
        json = readJsonFromIpIntel(ipAddress);
        int ipIntelResult = ipIntelResult(json);
        
        int fraudLevel = (abuseProbability + (blockedByIpHub?100:0) + ipQualityScoreFraudScore + ipIntelResult) / 4;
        if (fraudLevel < 0) fraudLevel = 0;
        if (fraudLevel > 100) fraudLevel = 100;
        return 100-fraudLevel;
    }


	public static void main(String[] args) {
//		String ip = "46.59.244.8";
//		String ip = "84.189.74.137";
//		String ip = "71.38.220.137";
        String ip = "178.155.5.154";
        try {
            JSONObject json = readJsonIpInfo(ip);
            System.out.println("Status: " + getGeoIPInfoNew("status", json));
            if (getGeoIPInfoNew("status", json).equalsIgnoreCase("success")) {
                System.out.println("Continent: " + getGeoIPInfoNew("continent", json));
        		System.out.println("Country: " + getGeoIPInfoNew("country", json));
                System.out.println("Country Code: " + getGeoIPInfoNew("countryCode", json));
                System.out.println("Region: " + getGeoIPInfoNew("region", json));
        		System.out.println("City: " + getGeoIPInfoNew("city", json));
                System.out.println("ISP: " + getGeoIPInfoNew("org", json));
                System.out.println("Type: " + getGeoIPInfoNew("ipType", json));
                if (!getGeoIPInfoNew("ipType", json).equalsIgnoreCase("Residential")) {
                    System.out.println("Business: " + getGeoIPInfoNew("businessName", json));
                }
            } else {
                System.out.println("Error Message: " + getGeoIPInfoNew("message", json));
            }
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController:");
            ioe.printStackTrace();
        }
        
        JSONObject json2 = PilotGeoIPController.readJsonFromAbuseIpDb(ip);
        System.out.println("AbuseProbability: " + PilotGeoIPController.abuseProbability(json2));
        
        JSONObject json3 = PilotGeoIPController.readJsonFromIpHub(ip);
        System.out.println("Blocked by IpHub: " + blockedByIpHub(json3));
		
        JSONObject json4 = PilotGeoIPController.readJsonFromIpQualityScore(ip);
        System.out.println("IpQualityScore Fraud Score: " + ipQualityScoreFraudScore(json4));
        
        JSONObject json5 = PilotGeoIPController.readJsonFromIpIntel(ip);
        System.out.println("IpIntel Result: " + ipIntelResult(json5));
        
        
        System.out.println("TrustLevel: " + trustLevel(ip));
        
        System.out.println("Is whitelisted: " + isWhitelisted(ip));
	}
}
