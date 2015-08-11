/*4.10.1 class*/
package com.maddox.il2.ai;

import com.maddox.rts.IniFile;
import com.maddox.rts.SectFile;
import com.maddox.util.HashMapExt;
import com.maddox.util.NumberTokenizer;
import java.io.File;
import java.util.ArrayList;

public class UserCfg
{
	public static final java.lang.String defName;
	public static final java.lang.String defCallsign;
	public static final java.lang.String defSurname;
	public static final java.lang.String nameHotKeyEnvs[] = {"pilot", "gunner", "aircraftView", "SnapView", "PanView", "orders", "misc", "$$$misc", "timeCompression", "move"};
	public java.lang.String sId;
	private int krypto[];
	public java.lang.String name;
	public java.lang.String callsign;
	public java.lang.String surname;
	public int singleDifficulty;
	public int netDifficulty;
	public java.lang.String placeBirth;
	public int yearBirth;
	public java.lang.String netRegiment;
	public java.lang.String netAirName;
	public java.lang.String netPilot;
	public int netSquadron;
	public int netTacticalNumber;
	public boolean netNumberOn;
	public float coverMashineGun;
	public float coverCannon;
	public float coverRocket;
	public float rocketDelay;
	public float bombDelay;
	public float fuel;
	
	//TODO: Added by |ZUTI|
	//------------------------------------
	public boolean bZutiMultiCrew;
	public boolean bZutiMultiCrewAnytime;
	//------------------------------------
	
	private com.maddox.util.HashMapExt skinMap;
	private com.maddox.util.HashMapExt weaponMap;
	private com.maddox.util.HashMapExt noseartMap;
	
	public java.lang.String iniFileName()
	{
		return "users/" + sId + "/settings.ini";
	}
	
	public java.lang.String getSkin(java.lang.String s)
	{
		return (java.lang.String)skinMap.get(s);
	}
	
	public java.lang.String getWeapon(java.lang.String s)
	{
		return (java.lang.String)weaponMap.get(s);
	}
	
	public java.lang.String getNoseart(java.lang.String s)
	{
		return (java.lang.String)noseartMap.get(s);
	}
	
	public void setSkin(java.lang.String s, java.lang.String s1)
	{
		skinMap.put(s, s1);
	}
	
	public void setWeapon(java.lang.String s, java.lang.String s1)
	{
		weaponMap.put(s, s1);
	}
	
	public void setNoseart(java.lang.String s, java.lang.String s1)
	{
		noseartMap.put(s, s1);
	}
	
	private void loadMap(com.maddox.rts.IniFile inifile, com.maddox.util.HashMapExt hashmapext, java.lang.String s)
	{
		hashmapext.clear();
		java.lang.String as[] = inifile.getVariables(s);
		if (as == null || as.length == 0)
			return;
		for (int i = 0; i < as.length; i++)
		{
			java.lang.String s1 = inifile.get(s, as[i], (java.lang.String)null);
			if (s1 != null)
				hashmapext.put(as[i], s1);
		}
		
	}
	
	private void saveMap(com.maddox.rts.IniFile inifile, com.maddox.util.HashMapExt hashmapext, java.lang.String s)
	{
		inifile.deleteSubject(s);
		if (hashmapext.size() == 0)
			return;
		for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
			if (entry.getValue() != null)
				inifile.set(s, (java.lang.String)entry.getKey(), (java.lang.String)entry.getValue());
		
	}
	
	public void loadConf()
	{
		com.maddox.rts.IniFile inifile = new IniFile(iniFileName(), 0);
		for (int i = 0; i < nameHotKeyEnvs.length; i++)
		{
			com.maddox.rts.HotKeyEnv.setCurrentEnv(nameHotKeyEnvs[i]);
			com.maddox.rts.HotKeyEnv.currentEnv().all().clear();
			com.maddox.rts.HotKeyEnv.fromIni(nameHotKeyEnvs[i], inifile, "HotKey " + nameHotKeyEnvs[i]);
		}
		
		singleDifficulty = inifile.get("difficulty", "single", 0);
		netDifficulty = inifile.get("difficulty", "net", 0);
		netRegiment = inifile.get("net", "regiment", (java.lang.String)null);
		netAirName = inifile.get("net", "airclass", (java.lang.String)null);
		netPilot = inifile.get("net", "pilot", (java.lang.String)null);
		netSquadron = inifile.get("net", "squadron", 0, 0, 3);
		netTacticalNumber = inifile.get("net", "tacticalnumber", 1, 1, 99);
		netNumberOn = inifile.get("net", "numberOn", 1, 0, 1) == 1;
		
		//TODO: Added by |ZUTI|
		//------------------------------------
		bZutiMultiCrew = inifile.get("net", "zutiMultiCrew", 0, 0, 1) == 1;
		bZutiMultiCrewAnytime = inifile.get("net", "zutiMultiCrewAnytime", 0, 0, 1) == 1;
		//------------------------------------
		
		coverMashineGun = inifile.get("cover", "mashinegun", 500F, 100F, 1000F);
		coverCannon = inifile.get("cover", "cannon", 500F, 100F, 1000F);
		coverRocket = inifile.get("cover", "rocket", 500F, 100F, 1000F);
		rocketDelay = inifile.get("cover", "rocketdelay", 10F, 1.0F, 60F);
		bombDelay = inifile.get("cover", "bombdelay", 0.0F, 0.0F, 10F);
		fuel = inifile.get("cover", "fuel", 100F, 0.0F, 100F);
		loadMap(inifile, skinMap, "skin");
		loadMap(inifile, weaponMap, "weapon");
		loadMap(inifile, noseartMap, "noseart");
		placeBirth = inifile.get("dgen", "placeBirth", (java.lang.String)null);
		yearBirth = inifile.get("dgen", "yearBirth", 1910, 1850, 2050);
		redirectKeysPause();
		if (com.maddox.il2.engine.Config.cur.newCloudsRender)
		{
			singleDifficulty |= 0x1000000;
			netDifficulty |= 0x1000000;
		}
		else
		{
			singleDifficulty &= 0xfeffffff;
			netDifficulty &= 0xfeffffff;
		}
	}
	
	public void saveConf()
	{
		com.maddox.rts.IniFile inifile = new IniFile(iniFileName(), 1);
		for (int i = 0; i < nameHotKeyEnvs.length; i++)
		{
			inifile.deleteSubject("HotKey " + nameHotKeyEnvs[i]);
			com.maddox.rts.HotKeyEnv.toIni(nameHotKeyEnvs[i], inifile, "HotKey " + nameHotKeyEnvs[i]);
		}
		
		inifile.deleteSubject("difficulty");
		inifile.set("difficulty", "single", singleDifficulty);
		inifile.set("difficulty", "net", netDifficulty);
		inifile.deleteSubject("net");
		if (netRegiment != null)
			inifile.set("net", "regiment", netRegiment);
		if (netAirName != null)
			inifile.set("net", "airclass", netAirName);
		if (netPilot != null)
			inifile.set("net", "pilot", netPilot);
		inifile.set("net", "squadron", netSquadron);
		inifile.set("net", "tacticalnumber", netTacticalNumber);
		inifile.set("net", "numberOn", netNumberOn ? "1" : "0");
		
		//TODO: Added by |ZUTI|
		//------------------------------------
		inifile.set("net", "zutiMultiCrew", bZutiMultiCrew ? "1" : "0");
		inifile.set("net", "zutiMultiCrewAnytime", bZutiMultiCrewAnytime ? "1" : "0");
		//------------------------------------
		
		inifile.deleteSubject("cover");
		inifile.set("cover", "mashinegun", coverMashineGun);
		inifile.set("cover", "cannon", coverCannon);
		inifile.set("cover", "rocket", coverRocket);
		inifile.set("cover", "rocketdelay", rocketDelay);
		inifile.set("cover", "bombdelay", bombDelay);
		inifile.set("cover", "fuel", fuel);
		saveMap(inifile, skinMap, "skin");
		saveMap(inifile, weaponMap, "weapon");
		saveMap(inifile, noseartMap, "noseart");
		if (placeBirth != null)
			inifile.set("dgen", "placeBirth", placeBirth);
		inifile.set("dgen", "yearBirth", yearBirth);
		inifile.saveFile();
		redirectKeysPause();
	}
	
	private void redirectKeysPause()
	{
		java.util.ArrayList arraylist = new ArrayList();
		com.maddox.rts.HotKeyEnv hotkeyenv = com.maddox.rts.HotKeyEnv.env("hotkeys");
		com.maddox.util.HashMapInt hashmapint = hotkeyenv.all();
		for (com.maddox.util.HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null); hashmapintentry != null; hashmapintentry = hashmapint.nextEntry(hashmapintentry))
		{
			int i = hashmapintentry.getKey();
			java.lang.String s = (java.lang.String)hashmapintentry.getValue();
			if ("pause".equals(s))
				arraylist.add(new Integer(i));
		}
		
		for (int j = 0; j < arraylist.size(); j++)
		{
			java.lang.Integer integer = (java.lang.Integer)arraylist.get(j);
			hashmapint.remove(integer.intValue());
		}
		
		arraylist.clear();
		hotkeyenv = com.maddox.rts.HotKeyEnv.env("timeCompression");
		hashmapint = hotkeyenv.all();
		for (com.maddox.util.HashMapIntEntry hashmapintentry1 = hashmapint.nextEntry(null); hashmapintentry1 != null; hashmapintentry1 = hashmapint.nextEntry(hashmapintentry1))
		{
			int k = hashmapintentry1.getKey();
			java.lang.String s1 = (java.lang.String)hashmapintentry1.getValue();
			if ("timeSpeedPause".equals(s1))
				arraylist.add(new Integer(k));
		}
		
		for (int l = 0; l < arraylist.size(); l++)
		{
			java.lang.Integer integer1 = (java.lang.Integer)arraylist.get(l);
			int i1 = integer1.intValue();
			com.maddox.rts.HotKeyEnv.env("hotkeys").all().put(i1, "pause");
		}
		
	}
	
	public int[] krypto()
	{
		if (krypto == null)
		{
			long l = com.maddox.rts.Finger.Long("users/" + sId);
			krypto = new int[17];
			for (int i = 0; i < 17; i++)
			{
				int j = (int)(l >> 8 * (i % 8) & 255L);
				for (int k = i / 8; k > 0; k--)
				{
					j <<= 2;
					j = (j & 3 | j) & 0xff;
				}
				
				if (j == 0)
					j = 255;
				krypto[i] = j;
			}
			
		}
		return krypto;
	}
	
	public boolean existUserDir()
	{
		return existUserDir(sId);
	}
	
	private boolean existUserDir(java.lang.String s)
	{
		java.io.File file = new File(com.maddox.rts.HomePath.toFileSystemName("users/" + s, 0));
		return file.isDirectory();
	}
	
	public boolean existUserConf()
	{
		java.io.File file = new File(com.maddox.rts.HomePath.toFileSystemName("users/" + sId + "/settings.ini", 0));
		return file.exists();
	}
	
	private void removeDGens()
	{
		java.lang.String s = "users/" + sId + "/campaigns.ini";
		com.maddox.rts.SectFile sectfile = new SectFile(s, 0, false, krypto());
		int i = sectfile.sectionIndex("list");
		if (i < 0)
			return;
		int j = sectfile.vars(i);
		for (int k = 0; k < j; k++)
			try
			{
				com.maddox.il2.game.campaign.Campaign campaign = (com.maddox.il2.game.campaign.Campaign)com.maddox.rts.ObjIO.fromString(sectfile.value(i, k));
				if (campaign.isDGen())
				{
					java.lang.String s1 = "missions/campaign/" + campaign.branch() + "/" + campaign.missionsDir();
					java.io.File file = new File(com.maddox.rts.HomePath.toFileSystemName(s1, 0));
					java.io.File afile[] = file.listFiles();
					if (afile != null)
					{
						for (int l = 0; l < afile.length; l++)
						{
							java.io.File file1 = afile[l];
							java.lang.String s2 = file1.getName();
							if (!".".equals(s2) && !"..".equals(s2))
								file1.delete();
						}
						
					}
					file.delete();
				}
				campaign.clearSavedStatics(sectfile);
			}
			catch (java.lang.Exception exception)
			{}
		
	}
	
	public void removeUserDir()
	{
		removeDGens();
		java.io.File file = new File(com.maddox.rts.HomePath.toFileSystemName("users/" + sId, 0));
		com.maddox.il2.ai.UserCfg.removeTree(file);
	}
	
	public static void removeTree(java.io.File file)
	{
		if (file.isDirectory())
		{
			java.io.File afile[] = file.listFiles();
			if (afile != null)
			{
				for (int i = 0; i < afile.length; i++)
					if (afile[i].isDirectory())
						com.maddox.il2.ai.UserCfg.removeTree(afile[i]);
					else
						afile[i].delete();
				
			}
		}
		file.delete();
	}
	
	public void createUserDir()
	{
		java.io.File file = new File(com.maddox.rts.HomePath.toFileSystemName("users/" + sId, 0));
		if (file.exists())
			com.maddox.il2.ai.UserCfg.removeTree(file);
		file.mkdirs();
	}
	
	public void createUserConf()
	{
		java.lang.String s = "users/" + sId + "/settings.ini";
		java.io.File file = new File(com.maddox.rts.HomePath.toFileSystemName(s, 0));
		if (file.exists())
			com.maddox.il2.ai.UserCfg.removeTree(file);
		java.lang.String s1 = "users/default.ini";
		com.maddox.rts.IniFile inifile = new IniFile(s1, 0);
		inifile.saveFile(s);
	}
	
	public void makeId()
	{
		sId = null;
		if (surname.length() > 0)
		{
			sId = surname.toLowerCase();
			if (!java.lang.Character.isDigit(sId.charAt(0)))
			{
				for (int i = 0; i < sId.length(); i++)
				{
					char c = sId.charAt(i);
					if (java.lang.Character.isLetterOrDigit(c))
						continue;
					sId = null;
					break;
				}
				
			}
		}
		if (sId == null)
		{
			sId = "";
		}
		else
		{
			for (int j = 0; j < sId.length(); j++)
			{
				if (sId.charAt(j) < '\200')
					continue;
				sId = "";
				break;
			}
			
		}
		int k = 0;
		do
		{
			java.lang.String s1;
			if (k == 0 && sId.length() > 0)
				s1 = sId;
			else
				s1 = sId + k;
			if (!existUserDir(s1))
			{
				sId = s1;
				return;
			}
			k++;
		}
		while (true);
	}
	
	public UserCfg()
	{
		netSquadron = 0;
		netTacticalNumber = 1;
		netNumberOn = true;
		
		//TODO: Added by |ZUTI|
		//------------------------------------
		bZutiMultiCrew = false;
		bZutiMultiCrewAnytime = false;
		//------------------------------------
		
		coverMashineGun = 500F;
		coverCannon = 500F;
		coverRocket = 500F;
		rocketDelay = 10F;
		bombDelay = 0.0F;
		fuel = 100F;
		skinMap = new HashMapExt();
		weaponMap = new HashMapExt();
		noseartMap = new HashMapExt();
	}
	
	public UserCfg(java.lang.String s, java.lang.String s1, java.lang.String s2)
	{
		netSquadron = 0;
		netTacticalNumber = 1;
		netNumberOn = true;
		
		//TODO: Added by |ZUTI|
		//------------------------------------
		bZutiMultiCrew = false;
		bZutiMultiCrewAnytime = false;
		//------------------------------------
		
		coverMashineGun = 500F;
		coverCannon = 500F;
		coverRocket = 500F;
		rocketDelay = 10F;
		bombDelay = 0.0F;
		fuel = 100F;
		skinMap = new HashMapExt();
		weaponMap = new HashMapExt();
		noseartMap = new HashMapExt();
		if (s == null || s.length() == 0)
			s = " ";
		if (s1 == null || s1.length() == 0)
			s1 = " ";
		if (s2 == null || s2.length() == 0)
			s2 = " ";
		name = s;
		callsign = s1;
		surname = s2;
		sId = null;
	}
	
	public static com.maddox.il2.ai.UserCfg loadCurrent()
	{
		java.io.File file = new File(com.maddox.rts.HomePath.toFileSystemName("users/all.ini", 0));
		if (!file.exists())
			return com.maddox.il2.ai.UserCfg.createDefault();
		com.maddox.rts.SectFile sectfile = new SectFile("users/all.ini", 0);
		int i = sectfile.sectionIndex("list");
		int j = sectfile.sectionIndex("current");
		if (i < 0 || j < 0)
			return com.maddox.il2.ai.UserCfg.createDefault();
		int k = sectfile.vars(i);
		if (k == 0)
			return com.maddox.il2.ai.UserCfg.createDefault();
		java.lang.String s = sectfile.var(j, 0);
		int l = 0;
		try
		{
			l = java.lang.Integer.parseInt(s);
		}
		catch (java.lang.Exception exception)
		{}
		if (l >= k)
			l = k - 1;
		java.lang.String s1 = sectfile.var(i, l);
		com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(i, l));
		com.maddox.il2.ai.UserCfg usercfg = new UserCfg(com.maddox.util.UnicodeTo8bit.load(numbertokenizer.next(defName)), com.maddox.util.UnicodeTo8bit.load(numbertokenizer.next(defCallsign)), com.maddox.util.UnicodeTo8bit.load(numbertokenizer
				.next(defSurname)));
		usercfg.sId = s1;
		if (!usercfg.existUserDir())
			usercfg.createUserDir();
		if (!usercfg.existUserConf())
			usercfg.createUserConf();
		usercfg.loadConf();
		return usercfg;
	}
	
	public static com.maddox.il2.ai.UserCfg createDefault()
	{
		com.maddox.rts.SectFile sectfile = new SectFile();
		sectfile.clear();
		com.maddox.il2.ai.UserCfg usercfg = new UserCfg(defName, defCallsign, defSurname);
		usercfg.makeId();
		int i = sectfile.sectionAdd("list");
		sectfile.lineAdd(i, usercfg.sId, com.maddox.util.UnicodeTo8bit.save(usercfg.name, true) + " " + com.maddox.util.UnicodeTo8bit.save(usercfg.callsign, true) + " " + com.maddox.util.UnicodeTo8bit.save(usercfg.surname, true));
		int j = sectfile.sectionAdd("current");
		sectfile.lineAdd(j, "0");
		sectfile.saveFile("users/all.ini");
		usercfg.createUserDir();
		usercfg.createUserConf();
		usercfg.loadConf();
		return usercfg;
	}
		
	static
	{
		if (com.maddox.il2.engine.Config.LOCALE.equals("RU"))
		{
			defName = "\u0418\u0432\u0430\u043D";
			defCallsign = "\u0412\u0430\u043D\u044F";
			defSurname = "\u0418\u0432\u0430\u043D\u043E\u0432";
		}
		else
		{
			defName = "John";
			defCallsign = "Mad";
			defSurname = "Doe";
		}
	}
}