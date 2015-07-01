/*4.10.1 UP class*/
package com.maddox.il2.objects.ships;

import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.game.ZutiTypeRRR;
import com.maddox.il2.objects.vehicles.radios.TypeHasBeacon;
import com.maddox.il2.objects.vehicles.radios.TypeHasHayRake;
import com.maddox.rts.SectFile;

public abstract class Ship
{
	static Class	class$com$maddox$il2$objects$ships$Ship$Apanui;
	static Class	class$com$maddox$il2$objects$ships$Ship$Awanui;
	static Class	class$com$maddox$il2$objects$ships$Ship$Bismarck;
	static Class	class$com$maddox$il2$objects$ships$Ship$FortGeorge;
	static Class	class$com$maddox$il2$objects$ships$Ship$germantrawlerA;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSColossusCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$HokokuMaru;
	static Class	class$com$maddox$il2$objects$ships$Ship$Holmlea;
	static Class	class$com$maddox$il2$objects$ships$Ship$Janssens;
	static Class	class$com$maddox$il2$objects$ships$Ship$KMHansa;
	static Class	class$com$maddox$il2$objects$ships$Ship$LSHuddell;
	static Class	class$com$maddox$il2$objects$ships$Ship$LSSimonBenson;
	static Class	class$com$maddox$il2$objects$ships$Ship$MalaysianQueen;
	static Class	class$com$maddox$il2$objects$ships$Ship$Mapito;
	static Class	class$com$maddox$il2$objects$ships$Ship$Mararoa;
	static Class	class$com$maddox$il2$objects$ships$Ship$MSAtenfels;
	static Class	class$com$maddox$il2$objects$ships$Ship$RedCanyon;
	static Class	class$com$maddox$il2$objects$ships$Ship$SeawayPrincess;
	static Class	class$com$maddox$il2$objects$ships$Ship$SetsuMaru;
	static Class	class$com$maddox$il2$objects$ships$Ship$Speedwell;
	static Class	class$com$maddox$il2$objects$ships$Ship$Sumatra;
	static Class	class$com$maddox$il2$objects$ships$Ship$TakatsuMaru;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSBesugo_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSNautilus_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSPermit_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSSpot_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICRO500_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICswordfish_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICU124_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICU564_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICwinter_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$WhiteRiver;
	static Class	class$com$maddox$il2$objects$ships$Ship$Zephyr;
	static Class	class$com$maddox$il2$objects$ships$Ship$IIbfinland;
	static Class	class$com$maddox$il2$objects$ships$Ship$IIbfinlandP;
	static Class	class$com$maddox$il2$objects$ships$Ship$IICU3;
	static Class	class$com$maddox$il2$objects$ships$Ship$IICU3P;
	static Class	class$com$maddox$il2$objects$ships$Ship$IICU12;
	static Class	class$com$maddox$il2$objects$ships$Ship$IICU12P;
	static Class	class$com$maddox$il2$objects$ships$Ship$IICU57;
	static Class	class$com$maddox$il2$objects$ships$Ship$IICU57P;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSBesugo_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSNautilus_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSPermit_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSSpot_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICRO500_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICswordfish_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICU124_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICU564_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$VIICwinter_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNRyujoCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSValleyForgeCV45;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSBoxerCV21;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSHoustonCA30;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSChesterCA27;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSPortlandCA33;
	static Class	class$com$maddox$il2$objects$ships$Ship$GrafZeppelin;
	static Class	class$com$maddox$il2$objects$ships$Ship$PeterStrasser;
	static Class	class$com$maddox$il2$objects$ships$Ship$Aquila;
	static Class	class$com$maddox$il2$objects$ships$Ship$LCM_1;
	static Class	class$com$maddox$il2$objects$ships$Ship$LCM_2;
	static Class	class$com$maddox$il2$objects$ships$Ship$LCM_3;
	static Class	class$com$maddox$il2$objects$ships$Ship$LCVP_1;
	static Class	class$com$maddox$il2$objects$ships$Ship$LCVP_2;
	static Class	class$com$maddox$il2$objects$ships$Ship$LST_1;
	static Class	class$com$maddox$il2$objects$ships$Ship$LST_3;
	static Class	class$com$maddox$il2$objects$ships$Ship$LSD_1;
	static Class	class$com$maddox$il2$objects$ships$Ship$Hospital_2;
	static Class	class$com$maddox$il2$objects$ships$Ship$Icebreaker_1;
	static Class	class$com$maddox$il2$objects$ships$Ship$Sub_Coldwar;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSCossack;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSJupiter;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSJavelin;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMASNapier;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSKipling;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSMatabele;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSNubian;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSTartar;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSWarspite;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSFiji;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSBunkerHillCV17_1944;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSFranklinCV13_1943;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSFranklinCV13_1944;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSGenericCV9ClassMS21;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSHancockCV19_1944;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSHornetCV12_1944;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSIntrepidCV11_1944;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSLexingtonCV16;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSTiconderogaCV14_1944;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSWaspCV18_1944;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSYorktownCV10_1944;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSPrincetonCVL23;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSBelleauWoodCVL24;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSSanJacintoCVL30;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSMcKean;
	static Class	class$com$maddox$il2$objects$ships$Ship$Littorio;
	static Class	class$com$maddox$il2$objects$ships$Ship$Soldati;
	static Class	class$com$maddox$il2$objects$ships$Ship$Italia0;
	static Class	class$com$maddox$il2$objects$ships$Ship$Italia1;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNHiryuCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNKagaCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNSoryuCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNCVLGeneric;
	static Class	class$com$maddox$il2$objects$ships$Ship$TroopTrans0;
	static Class	class$com$maddox$il2$objects$ships$Ship$TroopTrans1;
	static Class	class$com$maddox$il2$objects$ships$Ship$TroopTrans2;
	static Class	class$com$maddox$il2$objects$ships$Ship$TroopTrans3;
	static Class	class$com$maddox$il2$objects$ships$Ship$HospitalShip;
	static Class	class$com$maddox$il2$objects$ships$Ship$Transport1;
	static Class	class$com$maddox$il2$objects$ships$Ship$Transport2;
	static Class	class$com$maddox$il2$objects$ships$Ship$Transport3;
	static Class	class$com$maddox$il2$objects$ships$Ship$Transport4;
	static Class	class$com$maddox$il2$objects$ships$Ship$Transport5;
	static Class	class$com$maddox$il2$objects$ships$Ship$Transport6;
	static Class	class$com$maddox$il2$objects$ships$Ship$Transport7;
	static Class	class$com$maddox$il2$objects$ships$Ship$TransDmg;
	static Class	class$com$maddox$il2$objects$ships$Ship$TransWreck;
	static Class	class$com$maddox$il2$objects$ships$Ship$Tanker0;
	static Class	class$com$maddox$il2$objects$ships$Ship$Tanker1;
	static Class	class$com$maddox$il2$objects$ships$Ship$Tanker2;
	static Class	class$com$maddox$il2$objects$ships$Ship$TankerDmg;
	static Class	class$com$maddox$il2$objects$ships$Ship$MFPT;
	static Class	class$com$maddox$il2$objects$ships$Ship$MFPIT;
	static Class	class$com$maddox$il2$objects$ships$Ship$MAS501JP;
	static Class	class$com$maddox$il2$objects$ships$Ship$MAS501UNE;
	static Class	class$com$maddox$il2$objects$ships$Ship$MAS501UNP;
	static Class	class$com$maddox$il2$objects$ships$Ship$MAS501RN;
	static Class	class$com$maddox$il2$objects$ships$Ship$DLCWreck;
	static Class	class$com$maddox$il2$objects$ships$Ship$LCVPWreck;
	static Class	class$com$maddox$il2$objects$ships$Ship$SmallWreck;
	static Class	class$com$maddox$il2$objects$ships$Ship$Fisherman;
	static Class	class$com$maddox$il2$objects$ships$Ship$Fisherman1;
	static Class	class$com$maddox$il2$objects$ships$Ship$Fisherman2;
	static Class	class$com$maddox$il2$objects$ships$Ship$Barge0;
	static Class	class$com$maddox$il2$objects$ships$Ship$Barge1;
	static Class	class$com$maddox$il2$objects$ships$Ship$Boat;
	static Class	class$com$maddox$il2$objects$ships$Ship$Boat1;
	static Class	class$com$maddox$il2$objects$ships$Ship$Destroyer0;
	static Class	class$com$maddox$il2$objects$ships$Ship$Destroyer1;
	static Class	class$com$maddox$il2$objects$ships$Ship$Destroyer2;
	static Class	class$com$maddox$il2$objects$ships$Ship$DestroyerRN;
	static Class	class$com$maddox$il2$objects$ships$Ship$DestroyerKM;
	static Class	class$com$maddox$il2$objects$ships$Ship$DestroyerDmg;
	static Class	class$com$maddox$il2$objects$ships$Ship$DestroyerWreck;
	static Class	class$com$maddox$il2$objects$ships$Ship$Carrier1;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSFormidableCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSIndomitableCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$G5;
	static Class	class$com$maddox$il2$objects$ships$Ship$MO4;
	static Class	class$com$maddox$il2$objects$ships$Ship$BBK_1942;
	static Class	class$com$maddox$il2$objects$ships$Ship$BBK1124_1943;
	static Class	class$com$maddox$il2$objects$ships$Ship$Destroyer_USSR_Type7;
	static Class	class$com$maddox$il2$objects$ships$Ship$Destroyer_USSR_Type7_44;
	static Class	class$com$maddox$il2$objects$ships$Ship$Tral;
	static Class	class$com$maddox$il2$objects$ships$Ship$Shuka;
	static Class	class$com$maddox$il2$objects$ships$Ship$ShukaP;
	static Class	class$com$maddox$il2$objects$ships$Ship$Aurora;
	static Class	class$com$maddox$il2$objects$ships$Ship$Marat;
	static Class	class$com$maddox$il2$objects$ships$Ship$Kirov;
	static Class	class$com$maddox$il2$objects$ships$Ship$Tashkent;
	static Class	class$com$maddox$il2$objects$ships$Ship$Tramp;
	static Class	class$com$maddox$il2$objects$ships$Ship$Tanker;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSLexingtonCV2;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSSaratogaCV3;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSCVGeneric;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSBBGeneric;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSIndianapolisCA35;
	static Class	class$com$maddox$il2$objects$ships$Ship$LVT_2WAT;
	static Class	class$com$maddox$il2$objects$ships$Ship$DUKW_WAT;
	static Class	class$com$maddox$il2$objects$ships$Ship$LCVP;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSGreenlingSS213_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSGreenlingSS213_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSGatoSS212_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSGatoSS212_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSWardDD139;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSDentDD116;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSFletcherDD445;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSOBannonDD450;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSKiddDD661;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSCasablancaCVE55;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSKitkunBayCVE71;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSShamrockBayCVE84;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSEssexCV9;
	static Class	class$com$maddox$il2$objects$ships$Ship$USSIntrepidCV11;
	static Class	class$com$maddox$il2$objects$ships$Ship$PilotWater_US;
	static Class	class$com$maddox$il2$objects$ships$Ship$PilotBoatWater_US;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSIllustriousCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSPoWBB;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSKingGeorgeVBB;
	static Class	class$com$maddox$il2$objects$ships$Ship$HMSDukeOfYorkBB;
	static Class	class$com$maddox$il2$objects$ships$Ship$S80;
	static Class	class$com$maddox$il2$objects$ships$Ship$MFP;
	static Class	class$com$maddox$il2$objects$ships$Ship$MFP2;
	static Class	class$com$maddox$il2$objects$ships$Ship$MAS501;
	static Class	class$com$maddox$il2$objects$ships$Ship$Murgesku;
	static Class	class$com$maddox$il2$objects$ships$Ship$MBoat;
	static Class	class$com$maddox$il2$objects$ships$Ship$Submarine;
	static Class	class$com$maddox$il2$objects$ships$Ship$SubmarineP;
	static Class	class$com$maddox$il2$objects$ships$Ship$Niobe;
	static Class	class$com$maddox$il2$objects$ships$Ship$NiobeWithBeacon;
	static Class	class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_Srf;
	static Class	class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_SrfWithBeacon;
	static Class	class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_Sub;
	static Class	class$com$maddox$il2$objects$ships$Ship$Illmarinen;
	static Class	class$com$maddox$il2$objects$ships$Ship$Vainamoinen;
	static Class	class$com$maddox$il2$objects$ships$Ship$Tirpitz;
	static Class	class$com$maddox$il2$objects$ships$Ship$PAM;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNAkagiCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNShokakuCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNZuikakuCV;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNCVGeneric;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNBBGeneric;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNAkizukiDD42;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD41;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNArashiDD41;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNKageroDD41;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNNowakiDD41;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD41;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD43;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD43WithBeacon;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNNowakiDD43;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD43;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD45;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD45;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNFishJunk;
	static Class	class$com$maddox$il2$objects$ships$Ship$IJNFishJunkA;
	static Class	class$com$maddox$il2$objects$ships$Ship$DaihatsuLC;
	static Class	class$com$maddox$il2$objects$ships$Ship$PilotWater_JA;
	static Class	class$com$maddox$il2$objects$ships$Ship$RwyTranspWide;
	static Class	class$com$maddox$il2$objects$ships$Ship$RwyTranspSqr;
	static Class	class$com$maddox$il2$objects$ships$Ship$RwyCon;
	static Class	class$com$maddox$il2$objects$ships$Ship$RwySteel;
	static Class	class$com$maddox$il2$objects$ships$Ship$RwySteelLow;
	static Class	class$com$maddox$il2$objects$ships$Ship$RwyTransp;

	//TODO: Arranged by |ZUTI|: carriers first
	//--------------------------------------------------------------------------------
	public static class HMSIllustriousCV extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public HMSIllustriousCV()
		{
			
		}

		public HMSIllustriousCV(String string, int i, SectFile sectfile, String string_204_, SectFile sectfile_205_, String string_206_)
		{
			super(string, i, sectfile, string_204_, sectfile_205_, string_206_);
		}

		public int getDeckTypeId()
		{
			return 5;
		}
	}
	public static class HMSColossusCV extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 5;
		}
		
		public HMSColossusCV()
		{
			
		}

		public HMSColossusCV(String string, int i, SectFile sectfile, String string_681_, SectFile sectfile_682_, String string_683_)
		{
			super(string, i, sectfile, string_681_, sectfile_682_, string_683_);
		}

		public int getSpawnPoints()
		{
			return 5;
		}
	}
	
	public static class IJNAkagiCV extends BigshipGeneric implements TgtShip, TypeHasBeacon, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 6;
		}
		
		public IJNAkagiCV()
		{
			
		}

		public IJNAkagiCV(String string, int i, SectFile sectfile, String string_267_, SectFile sectfile_268_, String string_269_)
		{
			super(string, i, sectfile, string_267_, sectfile_268_, string_269_);
		}

		public int getSpawnPoints()
		{
			return 8;
		}
	}

	public static class IJNShokakuCV extends BigshipGeneric implements TgtShip, TypeHasBeacon, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 7;
		}
		
		public int getSpawnPoints()
		{
			return 6;
		}
		
		public IJNShokakuCV()
		{
			
		}

		public IJNShokakuCV(String string, int i, SectFile sectfile, String string_270_, SectFile sectfile_271_, String string_272_)
		{
			super(string, i, sectfile, string_270_, sectfile_271_, string_272_);
		}
	}

	public static class IJNZuikakuCV extends BigshipGeneric implements TgtShip, TypeHasBeacon, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 7;
		}
		
		public int getSpawnPoints()
		{
			return 6;
		}
		
		public IJNZuikakuCV()
		{
			
		}

		public IJNZuikakuCV(String string, int i, SectFile sectfile, String string_273_, SectFile sectfile_274_, String string_275_)
		{
			super(string, i, sectfile, string_273_, sectfile_274_, string_275_);
		}
	}

	public static class IJNCVGeneric extends BigshipGeneric implements TgtShip, TypeHasBeacon, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 7;
		}
		
		public int getSpawnPoints()
		{
			return 6;
		}
		
		public IJNCVGeneric()
		{
			
		}

		public IJNCVGeneric(String string, int i, SectFile sectfile, String string_276_, SectFile sectfile_277_, String string_278_)
		{
			super(string, i, sectfile, string_276_, sectfile_277_, string_278_);
		}
	}

	public static class USSTiconderogaCV14_1944 extends USSEssexCV9 implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSTiconderogaCV14_1944()
		{
			
		}

		public USSTiconderogaCV14_1944(String string, int i, SectFile sectfile, String string_534_, SectFile sectfile_535_, String string_536_)
		{
			super(string, i, sectfile, string_534_, sectfile_535_, string_536_);
		}
	}

	public static class USSBunkerHillCV17_1944 extends USSEssexCV9 implements TgtShip
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSBunkerHillCV17_1944()
		{
			
		}

		public USSBunkerHillCV17_1944(String string, int i, SectFile sectfile, String string_510_, SectFile sectfile_511_, String string_512_)
		{
			super(string, i, sectfile, string_510_, sectfile_511_, string_512_);
		}
	}

	public static class USSFranklinCV13_1943 extends USSEssexCV9 implements TgtShip
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSFranklinCV13_1943()
		{
			
		}

		public USSFranklinCV13_1943(String string, int i, SectFile sectfile, String string_513_, SectFile sectfile_514_, String string_515_)
		{
			super(string, i, sectfile, string_513_, sectfile_514_, string_515_);
		}
	}

	public static class USSFranklinCV13_1944 extends USSEssexCV9 implements TgtShip
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSFranklinCV13_1944()
		{
			
		}

		public USSFranklinCV13_1944(String string, int i, SectFile sectfile, String string_516_, SectFile sectfile_517_, String string_518_)
		{
			super(string, i, sectfile, string_516_, sectfile_517_, string_518_);
		}
	}

	public static class USSGenericCV9ClassMS21 extends USSEssexCV9 implements TgtShip
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSGenericCV9ClassMS21()
		{
			
		}

		public USSGenericCV9ClassMS21(String string, int i, SectFile sectfile, String string_519_, SectFile sectfile_520_, String string_521_)
		{
			super(string, i, sectfile, string_519_, sectfile_520_, string_521_);
		}
	}

	public static class USSHancockCV19_1944 extends USSEssexCV9 implements TgtShip
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSHancockCV19_1944()
		{
			
		}

		public USSHancockCV19_1944(String string, int i, SectFile sectfile, String string_522_, SectFile sectfile_523_, String string_524_)
		{
			super(string, i, sectfile, string_522_, sectfile_523_, string_524_);
		}
	}

	public static class USSHornetCV12_1944 extends USSEssexCV9 implements TgtShip
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSHornetCV12_1944()
		{
			
		}

		public USSHornetCV12_1944(String string, int i, SectFile sectfile, String string_525_, SectFile sectfile_526_, String string_527_)
		{
			super(string, i, sectfile, string_525_, sectfile_526_, string_527_);
		}
	}

	public static class USSIntrepidCV11_1944 extends USSEssexCV9 implements TgtShip
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSIntrepidCV11_1944()
		{
			
		}

		public USSIntrepidCV11_1944(String string, int i, SectFile sectfile, String string_528_, SectFile sectfile_529_, String string_530_)
		{
			super(string, i, sectfile, string_528_, sectfile_529_, string_530_);
		}
	}

	public static class USSLexingtonCV16 extends USSEssexCV9 implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSLexingtonCV16()
		{
			
		}

		public USSLexingtonCV16(String string, int i, SectFile sectfile, String string_531_, SectFile sectfile_532_, String string_533_)
		{
			super(string, i, sectfile, string_531_, sectfile_532_, string_533_);
		}
	}
	public static class USSWaspCV18_1944 extends USSEssexCV9 implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSWaspCV18_1944()
		{
			
		}

		public USSWaspCV18_1944(String string, int i, SectFile sectfile, String string_537_, SectFile sectfile_538_, String string_539_)
		{
			super(string, i, sectfile, string_537_, sectfile_538_, string_539_);
		}
	}
	public static class USSYorktownCV10_1944 extends USSEssexCV9 implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSYorktownCV10_1944()
		{
			
		}

		public USSYorktownCV10_1944(String string, int i, SectFile sectfile, String string_540_, SectFile sectfile_541_, String string_542_)
		{
			super(string, i, sectfile, string_540_, sectfile_541_, string_542_);
		}
	}
	public static class USSCasablancaCVE55 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 3;
		}
		
		public int getSpawnPoints()
		{
			return 3;
		}
		
		public USSCasablancaCVE55()
		{
			
		}

		public USSCasablancaCVE55(String string, int i, SectFile sectfile, String string_183_, SectFile sectfile_184_, String string_185_)
		{
			super(string, i, sectfile, string_183_, sectfile_184_, string_185_);
		}
	}
	public static class USSKitkunBayCVE71 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 3;
		}
		
		public int getSpawnPoints()
		{
			return 3;
		}
		
		public USSKitkunBayCVE71()
		{
			
		}

		public USSKitkunBayCVE71(String string, int i, SectFile sectfile, String string_186_, SectFile sectfile_187_, String string_188_)
		{
			super(string, i, sectfile, string_186_, sectfile_187_, string_188_);
		}
	}
	public static class USSShamrockBayCVE84 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 3;
		}
		
		public int getSpawnPoints()
		{
			return 3;
		}
		
		public USSShamrockBayCVE84()
		{
			
		}

		public USSShamrockBayCVE84(String string, int i, SectFile sectfile, String string_189_, SectFile sectfile_190_, String string_191_)
		{
			super(string, i, sectfile, string_189_, sectfile_190_, string_191_);
		}
	}
	public static class USSEssexCV9 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSEssexCV9()
		{
			
		}

		public USSEssexCV9(String string, int i, SectFile sectfile, String string_192_, SectFile sectfile_193_, String string_194_)
		{
			super(string, i, sectfile, string_192_, sectfile_193_, string_194_);
		}
	}
	public static class USSIntrepidCV11 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSIntrepidCV11()
		{
			
		}

		public USSIntrepidCV11(String string, int i, SectFile sectfile, String string_195_, SectFile sectfile_196_, String string_197_)
		{
			super(string, i, sectfile, string_195_, sectfile_196_, string_197_);
		}
	}

	public static class USSBelleauWoodCVL24 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 4;
		}
		
		public int getSpawnPoints()
		{
			return 8;
		}
		
		public USSBelleauWoodCVL24()
		{
			
		}

		public USSBelleauWoodCVL24(String string, int i, SectFile sectfile, String string_477_, SectFile sectfile_478_, String string_479_)
		{
			super(string, i, sectfile, string_477_, sectfile_478_, string_479_);
		}
	}

	public static class USSSanJacintoCVL30 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 4;
		}
		
		public USSSanJacintoCVL30()
		{
			
		}

		public USSSanJacintoCVL30(String string, int i, SectFile sectfile, String string_474_, SectFile sectfile_475_, String string_476_)
		{
			super(string, i, sectfile, string_474_, sectfile_475_, string_476_);
		}

		public int getSpawnPoints()
		{
			return 8;
		}
	}
		
	public static class USSPrincetonCVL23 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 4;
		}
		
		public int getSpawnPoints()
		{
			return 8;
		}
		
		public USSPrincetonCVL23()
		{
			
		}

		public USSPrincetonCVL23(String string, int i, SectFile sectfile, String string_480_, SectFile sectfile_481_, String string_482_)
		{
			super(string, i, sectfile, string_480_, sectfile_481_, string_482_);
		}
	}

	public static class IJNHiryuCV extends BigshipGeneric implements TgtShip, TypeHasBeacon, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 7;
		}
		
		public IJNHiryuCV()
		{
			
		}

		public IJNHiryuCV(String string, int i, SectFile sectfile, String string_483_, SectFile sectfile_484_, String string_485_)
		{
			super(string, i, sectfile, string_483_, sectfile_484_, string_485_);
		}

		public int getSpawnPoints()
		{
			return 6;
		}
	}

	public static class IJNKagaCV extends BigshipGeneric implements TgtShip, TypeHasBeacon, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 7;
		}
		
		public int getSpawnPoints()
		{
			return 6;
		}
		
		public IJNKagaCV()
		{
			
		}

		public IJNKagaCV(String string, int i, SectFile sectfile, String string_486_, SectFile sectfile_487_, String string_488_)
		{
			super(string, i, sectfile, string_486_, sectfile_487_, string_488_);
		}
	}

	public static class IJNSoryuCV extends BigshipGeneric implements TgtShip, TypeHasBeacon, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 7;
		}
		
		public int getSpawnPoints()
		{
			return 6;
		}
		
		public IJNSoryuCV()
		{
			
		}

		public IJNSoryuCV(String string, int i, SectFile sectfile, String string_489_, SectFile sectfile_490_, String string_491_)
		{
			super(string, i, sectfile, string_489_, sectfile_490_, string_491_);
		}
	}

	public static class IJNCVLGeneric extends BigshipGeneric implements TgtShip, TypeHasBeacon, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 7;
		}
		
		public int getSpawnPoints()
		{
			return 6;
		}
		
		public IJNCVLGeneric()
		{
			
		}

		public IJNCVLGeneric(String string, int i, SectFile sectfile, String string_492_, SectFile sectfile_493_, String string_494_)
		{
			super(string, i, sectfile, string_492_, sectfile_493_, string_494_);
		}
	}

	public static class HMSFormidableCV extends HMSIllustriousCV implements TgtShip, ZutiTypeAircraftCarrier
	{		
		public int getDeckTypeId()
		{
			return 5;
		}
		
		public HMSFormidableCV()
		{
			
		}

		public HMSFormidableCV(String string, int i, SectFile sectfile, String string_495_, SectFile sectfile_496_, String string_497_)
		{
			super(string, i, sectfile, string_495_, sectfile_496_, string_497_);
		}

		public int getSpawnPoints()
		{
			return 5;
		}
	}

	public static class HMSIndomitableCV extends HMSIllustriousCV implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 5;
		}
		
		public HMSIndomitableCV()
		{
			
		}
		public int getSpawnPoints()
		{
			return 5;
		}
		public HMSIndomitableCV(String string, int i, SectFile sectfile, String string_498_, SectFile sectfile_499_, String string_500_)
		{
			super(string, i, sectfile, string_498_, sectfile_499_, string_500_);
		}
	}
	
	public static class USSLexingtonCV2 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 1;
		}
		
		public USSLexingtonCV2()
		{
			
		}

		public USSLexingtonCV2(String string, int i, SectFile sectfile, String string_132_, SectFile sectfile_133_, String string_134_)
		{
			super(string, i, sectfile, string_132_, sectfile_133_, string_134_);
		}

		public int getSpawnPoints()
		{
			return 6;
		}
	}
	public static class USSSaratogaCV3 extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 1;
		}
		
		public USSSaratogaCV3()
		{
			
		}

		public USSSaratogaCV3(String string, int i, SectFile sectfile, String string_135_, SectFile sectfile_136_, String string_137_)
		{
			super(string, i, sectfile, string_135_, sectfile_136_, string_137_);
		}
		
		public int getSpawnPoints()
		{
			return 6;
		}
	}

	public static class USSCVGeneric extends BigshipGeneric implements TgtShip, TypeHasHayRake, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 1;
		}
		
		public USSCVGeneric()
		{
			
		}

		public USSCVGeneric(String string, int i, SectFile sectfile, String string_138_, SectFile sectfile_139_, String string_140_)
		{
			super(string, i, sectfile, string_138_, sectfile_139_, string_140_);
		}
		
		public int getSpawnPoints()
		{
			return 6;
		}
	}
	
	public static class Aquila extends BigshipGeneric implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 5;
		}
		
		public Aquila()
		{
			
		}

		public Aquila(String string, int i, SectFile sectfile, String string_18_, SectFile sectfile_19_, String string_20_)
		{
			super(string, i, sectfile, string_18_, sectfile_19_, string_20_);
		}

		public int getSpawnPoints()
		{
			return 5;
		}
	}	
	public static class PeterStrasser extends BigshipGeneric implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 5;
		}
		
		public PeterStrasser()
		{
			
		}

		public PeterStrasser(String string, int i, SectFile sectfile, String string_21_, SectFile sectfile_22_, String string_23_)
		{
			super(string, i, sectfile, string_21_, sectfile_22_, string_23_);
		}

		public int getSpawnPoints()
		{
			return 5;
		}
	}
	public static class GrafZeppelin extends BigshipGeneric implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 5;
		}
		
		public GrafZeppelin()
		{
			
		}

		public GrafZeppelin(String string, int i, SectFile sectfile, String string_24_, SectFile sectfile_25_, String string_26_)
		{
			super(string, i, sectfile, string_24_, sectfile_25_, string_26_);
		}

		public int getSpawnPoints()
		{
			return 5;
		}
	}

	public static class Carrier1 extends BigshipGeneric implements TgtShip, ZutiTypeAircraftCarrier
	{
		//Carrier1 = Ameer
		public int getDeckTypeId()
		{
			return 3;
		}
		
		public int getSpawnPoints()
		{
			return 3;
		}
		
		public Carrier1()
		{
			
		}

		public Carrier1(String string, int i, SectFile sectfile, String string_399_, SectFile sectfile_400_, String string_401_)
		{
			super(string, i, sectfile, string_399_, sectfile_400_, string_401_);
		}
	}
		
	public static class IJNRyujoCV extends BigshipGeneric implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 7;
		}
		
		public int getSpawnPoints()
		{
			return 6;
		}
		
		public IJNRyujoCV()
		{
			
		}

		public IJNRyujoCV(String string, int i, SectFile sectfile, String string_0_, SectFile sectfile_1_, String string_2_)
		{
			super(string, i, sectfile, string_0_, sectfile_1_, string_2_);
		}
	}

	public static class USSValleyForgeCV45 extends BigshipGeneric implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSValleyForgeCV45()
		{
			
		}

		public USSValleyForgeCV45(String string, int i, SectFile sectfile, String string_3_, SectFile sectfile_4_, String string_5_)
		{
			super(string, i, sectfile, string_3_, sectfile_4_, string_5_);
		}
	}

	public static class USSBoxerCV21 extends BigshipGeneric implements TgtShip, ZutiTypeAircraftCarrier
	{
		public int getDeckTypeId()
		{
			return 2;
		}
		
		public int getSpawnPoints()
		{
			return 5;
		}
		
		public USSBoxerCV21()
		{
			
		}

		public USSBoxerCV21(String string, int i, SectFile sectfile, String string_6_, SectFile sectfile_7_, String string_8_)
		{
			super(string, i, sectfile, string_6_, sectfile_7_, string_8_);
		}
	}
	//--------------------------------------------------------------------------------

	public static class USSHoustonCA30 extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public USSHoustonCA30()
		{
			
		}

		public USSHoustonCA30(String string, int i, SectFile sectfile, String string_9_, SectFile sectfile_10_, String string_11_)
		{
			super(string, i, sectfile, string_9_, sectfile_10_, string_11_);
		}
	}

	public static class USSChesterCA27 extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public USSChesterCA27()
		{
			
		}

		public USSChesterCA27(String string, int i, SectFile sectfile, String string_12_, SectFile sectfile_13_, String string_14_)
		{
			super(string, i, sectfile, string_12_, sectfile_13_, string_14_);
		}
	}

	public static class USSPortlandCA33 extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public USSPortlandCA33()
		{
			
		}

		public USSPortlandCA33(String string, int i, SectFile sectfile, String string_15_, SectFile sectfile_16_, String string_17_)
		{
			super(string, i, sectfile, string_15_, sectfile_16_, string_17_);
		}
	}

	public static class LCM_1 extends ShipGeneric implements TgtShip
	{
		public LCM_1()
		{
			
		}

		public LCM_1(String string, int i, SectFile sectfile, String string_27_, SectFile sectfile_28_, String string_29_)
		{
			super(string, i, sectfile, string_27_, sectfile_28_, string_29_);
		}
	}

	public static class LCM_2 extends ShipGeneric implements TgtShip
	{
		public LCM_2()
		{
			
		}

		public LCM_2(String string, int i, SectFile sectfile, String string_30_, SectFile sectfile_31_, String string_32_)
		{
			super(string, i, sectfile, string_30_, sectfile_31_, string_32_);
		}
	}

	public static class LCM_3 extends ShipGeneric implements TgtShip
	{
		public LCM_3()
		{
			
		}

		public LCM_3(String string, int i, SectFile sectfile, String string_33_, SectFile sectfile_34_, String string_35_)
		{
			super(string, i, sectfile, string_33_, sectfile_34_, string_35_);
		}
	}

	public static class LCVP_1 extends ShipGeneric implements TgtShip
	{
		public LCVP_1()
		{
			
		}

		public LCVP_1(String string, int i, SectFile sectfile, String string_36_, SectFile sectfile_37_, String string_38_)
		{
			super(string, i, sectfile, string_36_, sectfile_37_, string_38_);
		}
	}

	public static class LCVP_2 extends ShipGeneric implements TgtShip
	{
		public LCVP_2()
		{
			
		}

		public LCVP_2(String string, int i, SectFile sectfile, String string_39_, SectFile sectfile_40_, String string_41_)
		{
			super(string, i, sectfile, string_39_, sectfile_40_, string_41_);
		}
	}

	public static class LST_1 extends BigshipGeneric implements TgtShip
	{
		public LST_1()
		{
			
		}

		public LST_1(String string, int i, SectFile sectfile, String string_42_, SectFile sectfile_43_, String string_44_)
		{
			super(string, i, sectfile, string_42_, sectfile_43_, string_44_);
		}
	}

	public static class LST_3 extends BigshipGeneric implements TgtShip
	{
		public LST_3()
		{
			
		}

		public LST_3(String string, int i, SectFile sectfile, String string_45_, SectFile sectfile_46_, String string_47_)
		{
			super(string, i, sectfile, string_45_, sectfile_46_, string_47_);
		}
	}

	public static class LSD_1 extends BigshipGeneric implements TgtShip
	{
		public LSD_1()
		{
			
		}

		public LSD_1(String string, int i, SectFile sectfile, String string_48_, SectFile sectfile_49_, String string_50_)
		{
			super(string, i, sectfile, string_48_, sectfile_49_, string_50_);
		}
	}

	public static class Hospital_2 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Hospital_2()
		{
			
		}

		public Hospital_2(String string, int i, SectFile sectfile, String string_51_, SectFile sectfile_52_, String string_53_)
		{
			super(string, i, sectfile, string_51_, sectfile_52_, string_53_);
		}
	}

	public static class Icebreaker_1 extends BigshipGeneric implements TgtShip
	{
		public Icebreaker_1()
		{
			
		}

		public Icebreaker_1(String string, int i, SectFile sectfile, String string_54_, SectFile sectfile_55_, String string_56_)
		{
			super(string, i, sectfile, string_54_, sectfile_55_, string_56_);
		}
	}

	public static class Sub_Coldwar extends BigshipGeneric implements TgtShip
	{
		public Sub_Coldwar()
		{
			
		}

		public Sub_Coldwar(String string, int i, SectFile sectfile, String string_57_, SectFile sectfile_58_, String string_59_)
		{
			super(string, i, sectfile, string_57_, sectfile_58_, string_59_);
		}
	}

	public static class HMSCossack extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public HMSCossack()
		{
			
		}

		public HMSCossack(String string, int i, SectFile sectfile, String string_60_, SectFile sectfile_61_, String string_62_)
		{
			super(string, i, sectfile, string_60_, sectfile_61_, string_62_);
		}
	}

	public static class HMSJupiter extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public HMSJupiter()
		{
			
		}

		public HMSJupiter(String string, int i, SectFile sectfile, String string_63_, SectFile sectfile_64_, String string_65_)
		{
			super(string, i, sectfile, string_63_, sectfile_64_, string_65_);
		}
	}

	public static class HMSJavelin extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public HMSJavelin()
		{
			
		}

		public HMSJavelin(String string, int i, SectFile sectfile, String string_66_, SectFile sectfile_67_, String string_68_)
		{
			super(string, i, sectfile, string_66_, sectfile_67_, string_68_);
		}
	}

	public static class HMASNapier extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public HMASNapier()
		{
			
		}

		public HMASNapier(String string, int i, SectFile sectfile, String string_69_, SectFile sectfile_70_, String string_71_)
		{
			super(string, i, sectfile, string_69_, sectfile_70_, string_71_);
		}
	}

	public static class HMSKipling extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public HMSKipling()
		{
			
		}

		public HMSKipling(String string, int i, SectFile sectfile, String string_72_, SectFile sectfile_73_, String string_74_)
		{
			super(string, i, sectfile, string_72_, sectfile_73_, string_74_);
		}
	}

	public static class HMSMatabele extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public HMSMatabele()
		{
			
		}

		public HMSMatabele(String string, int i, SectFile sectfile, String string_75_, SectFile sectfile_76_, String string_77_)
		{
			super(string, i, sectfile, string_75_, sectfile_76_, string_77_);
		}
	}

	public static class HMSNubian extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public HMSNubian()
		{
			
		}

		public HMSNubian(String string, int i, SectFile sectfile, String string_78_, SectFile sectfile_79_, String string_80_)
		{
			super(string, i, sectfile, string_78_, sectfile_79_, string_80_);
		}
	}

	public static class HMSTartar extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public HMSTartar()
		{
			
		}

		public HMSTartar(String string, int i, SectFile sectfile, String string_81_, SectFile sectfile_82_, String string_83_)
		{
			super(string, i, sectfile, string_81_, sectfile_82_, string_83_);
		}
	}

	public static class HMSWarspite extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public HMSWarspite()
		{
			
		}

		public HMSWarspite(String string, int i, SectFile sectfile, String string_84_, SectFile sectfile_85_, String string_86_)
		{
			super(string, i, sectfile, string_84_, sectfile_85_, string_86_);
		}
	}

	public static class G5 extends BigshipGeneric implements TgtShip
	{
		public G5()
		{
			
		}

		public G5(String string, int i, SectFile sectfile, String string_87_, SectFile sectfile_88_, String string_89_)
		{
			super(string, i, sectfile, string_87_, sectfile_88_, string_89_);
		}
	}

	public static class MO4 extends BigshipGeneric implements TgtShip
	{
		public MO4()
		{
			
		}

		public MO4(String string, int i, SectFile sectfile, String string_90_, SectFile sectfile_91_, String string_92_)
		{
			super(string, i, sectfile, string_90_, sectfile_91_, string_92_);
		}
	}

	public static class BBK_1942 extends BigshipGeneric implements TgtShip
	{
		public BBK_1942()
		{
			
		}

		public BBK_1942(String string, int i, SectFile sectfile, String string_93_, SectFile sectfile_94_, String string_95_)
		{
			super(string, i, sectfile, string_93_, sectfile_94_, string_95_);
		}
	}

	public static class BBK1124_1943 extends BigshipGeneric implements TgtShip
	{
		public BBK1124_1943()
		{
			
		}

		public BBK1124_1943(String string, int i, SectFile sectfile, String string_96_, SectFile sectfile_97_, String string_98_)
		{
			super(string, i, sectfile, string_96_, sectfile_97_, string_98_);
		}
	}

	public static class Destroyer_USSR_Type7 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Destroyer_USSR_Type7()
		{
			
		}

		public Destroyer_USSR_Type7(String string, int i, SectFile sectfile, String string_99_, SectFile sectfile_100_, String string_101_)
		{
			super(string, i, sectfile, string_99_, sectfile_100_, string_101_);
		}
	}

	public static class Destroyer_USSR_Type7_44 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Destroyer_USSR_Type7_44()
		{
			
		}

		public Destroyer_USSR_Type7_44(String string, int i, SectFile sectfile, String string_102_, SectFile sectfile_103_, String string_104_)
		{
			super(string, i, sectfile, string_102_, sectfile_103_, string_104_);
		}
	}

	public static class Tral extends BigshipGeneric implements TgtShip
	{
		public Tral()
		{
			
		}

		public Tral(String string, int i, SectFile sectfile, String string_105_, SectFile sectfile_106_, String string_107_)
		{
			super(string, i, sectfile, string_105_, sectfile_106_, string_107_);
		}
	}

	public static class Shuka extends ShipGeneric implements TgtShip
	{
		public Shuka()
		{
			
		}

		public Shuka(String string, int i, SectFile sectfile, String string_108_, SectFile sectfile_109_, String string_110_)
		{
			super(string, i, sectfile, string_108_, sectfile_109_, string_110_);
		}
	}

	public static class ShukaP extends BigshipGeneric implements TgtShip
	{
		public ShukaP()
		{
			
		}

		public ShukaP(String string, int i, SectFile sectfile, String string_111_, SectFile sectfile_112_, String string_113_)
		{
			super(string, i, sectfile, string_111_, sectfile_112_, string_113_);
		}
	}

	public static class Aurora extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public Aurora()
		{
			
		}

		public Aurora(String string, int i, SectFile sectfile, String string_114_, SectFile sectfile_115_, String string_116_)
		{
			super(string, i, sectfile, string_114_, sectfile_115_, string_116_);
		}
	}

	public static class Marat extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public Marat()
		{
			
		}

		public Marat(String string, int i, SectFile sectfile, String string_117_, SectFile sectfile_118_, String string_119_)
		{
			super(string, i, sectfile, string_117_, sectfile_118_, string_119_);
		}
	}

	public static class Kirov extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public Kirov()
		{
			
		}

		public Kirov(String string, int i, SectFile sectfile, String string_120_, SectFile sectfile_121_, String string_122_)
		{
			super(string, i, sectfile, string_120_, sectfile_121_, string_122_);
		}
	}

	public static class Tashkent extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Tashkent()
		{
			
		}

		public Tashkent(String string, int i, SectFile sectfile, String string_123_, SectFile sectfile_124_, String string_125_)
		{
			super(string, i, sectfile, string_123_, sectfile_124_, string_125_);
		}
	}

	public static class Tramp extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Tramp()
		{
			
		}

		public Tramp(String string, int i, SectFile sectfile, String string_126_, SectFile sectfile_127_, String string_128_)
		{
			super(string, i, sectfile, string_126_, sectfile_127_, string_128_);
		}
	}

	public static class Tanker extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Tanker()
		{
			
		}

		public Tanker(String string, int i, SectFile sectfile, String string_129_, SectFile sectfile_130_, String string_131_)
		{
			super(string, i, sectfile, string_129_, sectfile_130_, string_131_);
		}
	}

	public static class USSBBGeneric extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public USSBBGeneric()
		{
			
		}

		public USSBBGeneric(String string, int i, SectFile sectfile, String string_141_, SectFile sectfile_142_, String string_143_)
		{
			super(string, i, sectfile, string_141_, sectfile_142_, string_143_);
		}
	}

	public static class USSIndianapolisCA35 extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public USSIndianapolisCA35()
		{
			
		}

		public USSIndianapolisCA35(String string, int i, SectFile sectfile, String string_144_, SectFile sectfile_145_, String string_146_)
		{
			super(string, i, sectfile, string_144_, sectfile_145_, string_146_);
		}
	}

	public static class LVT_2WAT extends ShipGeneric implements TgtShip
	{
		public LVT_2WAT()
		{
			
		}

		public LVT_2WAT(String string, int i, SectFile sectfile, String string_147_, SectFile sectfile_148_, String string_149_)
		{
			super(string, i, sectfile, string_147_, sectfile_148_, string_149_);
		}
	}

	public static class DUKW_WAT extends ShipGeneric implements TgtShip
	{
		public DUKW_WAT()
		{
			
		}

		public DUKW_WAT(String string, int i, SectFile sectfile, String string_150_, SectFile sectfile_151_, String string_152_)
		{
			super(string, i, sectfile, string_150_, sectfile_151_, string_152_);
		}
	}

	public static class LCVP extends ShipGeneric implements TgtShip
	{
		public LCVP()
		{
			
		}

		public LCVP(String string, int i, SectFile sectfile, String string_153_, SectFile sectfile_154_, String string_155_)
		{
			super(string, i, sectfile, string_153_, sectfile_154_, string_155_);
		}
	}

	public static class USSGreenlingSS213_Srf extends BigshipGeneric implements TgtShip
	{
		public USSGreenlingSS213_Srf()
		{
			
		}

		public USSGreenlingSS213_Srf(String string, int i, SectFile sectfile, String string_156_, SectFile sectfile_157_, String string_158_)
		{
			super(string, i, sectfile, string_156_, sectfile_157_, string_158_);
		}
	}

	public static class USSGreenlingSS213_Sub extends BigshipGeneric implements TgtShip
	{
		public USSGreenlingSS213_Sub()
		{
			
		}

		public USSGreenlingSS213_Sub(String string, int i, SectFile sectfile, String string_159_, SectFile sectfile_160_, String string_161_)
		{
			super(string, i, sectfile, string_159_, sectfile_160_, string_161_);
		}
	}

	public static class USSGatoSS212_Srf extends BigshipGeneric implements TgtShip
	{
		public USSGatoSS212_Srf()
		{
			
		}

		public USSGatoSS212_Srf(String string, int i, SectFile sectfile, String string_162_, SectFile sectfile_163_, String string_164_)
		{
			super(string, i, sectfile, string_162_, sectfile_163_, string_164_);
		}
	}

	public static class USSGatoSS212_Sub extends BigshipGeneric implements TgtShip
	{
		public USSGatoSS212_Sub()
		{
			
		}

		public USSGatoSS212_Sub(String string, int i, SectFile sectfile, String string_165_, SectFile sectfile_166_, String string_167_)
		{
			super(string, i, sectfile, string_165_, sectfile_166_, string_167_);
		}
	}

	public static class USSWardDD139 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public USSWardDD139()
		{
			
		}

		public USSWardDD139(String string, int i, SectFile sectfile, String string_168_, SectFile sectfile_169_, String string_170_)
		{
			super(string, i, sectfile, string_168_, sectfile_169_, string_170_);
		}
	}

	public static class USSDentDD116 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public USSDentDD116()
		{
			
		}

		public USSDentDD116(String string, int i, SectFile sectfile, String string_171_, SectFile sectfile_172_, String string_173_)
		{
			super(string, i, sectfile, string_171_, sectfile_172_, string_173_);
		}
	}

	public static class USSFletcherDD445 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public USSFletcherDD445()
		{
			
		}

		public USSFletcherDD445(String string, int i, SectFile sectfile, String string_174_, SectFile sectfile_175_, String string_176_)
		{
			super(string, i, sectfile, string_174_, sectfile_175_, string_176_);
		}
	}

	public static class USSOBannonDD450 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public USSOBannonDD450()
		{
			
		}

		public USSOBannonDD450(String string, int i, SectFile sectfile, String string_177_, SectFile sectfile_178_, String string_179_)
		{
			super(string, i, sectfile, string_177_, sectfile_178_, string_179_);
		}
	}

	public static class USSKiddDD661 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public USSKiddDD661()
		{
			
		}

		public USSKiddDD661(String string, int i, SectFile sectfile, String string_180_, SectFile sectfile_181_, String string_182_)
		{
			super(string, i, sectfile, string_180_, sectfile_181_, string_182_);
		}
	}

	public static class PilotWater_US extends ShipGeneric implements TgtShip
	{
		public PilotWater_US()
		{
			
		}

		public PilotWater_US(String string, int i, SectFile sectfile, String string_198_, SectFile sectfile_199_, String string_200_)
		{
			super(string, i, sectfile, string_198_, sectfile_199_, string_200_);
		}
	}

	public static class PilotBoatWater_US extends ShipGeneric implements TgtShip, WeakBody
	{
		public PilotBoatWater_US()
		{
			
		}

		public PilotBoatWater_US(String string, int i, SectFile sectfile, String string_201_, SectFile sectfile_202_, String string_203_)
		{
			super(string, i, sectfile, string_201_, sectfile_202_, string_203_);
		}
	}

	public static class HMSPoWBB extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public HMSPoWBB()
		{
			
		}

		public HMSPoWBB(String string, int i, SectFile sectfile, String string_207_, SectFile sectfile_208_, String string_209_)
		{
			super(string, i, sectfile, string_207_, sectfile_208_, string_209_);
		}
	}

	public static class HMSKingGeorgeVBB extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public HMSKingGeorgeVBB()
		{
			
		}

		public HMSKingGeorgeVBB(String string, int i, SectFile sectfile, String string_210_, SectFile sectfile_211_, String string_212_)
		{
			super(string, i, sectfile, string_210_, sectfile_211_, string_212_);
		}
	}

	public static class HMSDukeOfYorkBB extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public HMSDukeOfYorkBB()
		{
			
		}

		public HMSDukeOfYorkBB(String string, int i, SectFile sectfile, String string_213_, SectFile sectfile_214_, String string_215_)
		{
			super(string, i, sectfile, string_213_, sectfile_214_, string_215_);
		}
	}

	public static class S80 extends BigshipGeneric implements TgtShip
	{
		public S80()
		{
			
		}

		public S80(String string, int i, SectFile sectfile, String string_216_, SectFile sectfile_217_, String string_218_)
		{
			super(string, i, sectfile, string_216_, sectfile_217_, string_218_);
		}
	}

	public static class MFP extends BigshipGeneric implements TgtShip
	{
		public MFP()
		{
			
		}

		public MFP(String string, int i, SectFile sectfile, String string_219_, SectFile sectfile_220_, String string_221_)
		{
			super(string, i, sectfile, string_219_, sectfile_220_, string_221_);
		}
	}

	public static class MFP2 extends BigshipGeneric implements TgtShip
	{
		public MFP2()
		{
			
		}

		public MFP2(String string, int i, SectFile sectfile, String string_222_, SectFile sectfile_223_, String string_224_)
		{
			super(string, i, sectfile, string_222_, sectfile_223_, string_224_);
		}
	}

	public static class MAS501 extends BigshipGeneric implements TgtShip
	{
		public MAS501()
		{
			
		}

		public MAS501(String string, int i, SectFile sectfile, String string_225_, SectFile sectfile_226_, String string_227_)
		{
			super(string, i, sectfile, string_225_, sectfile_226_, string_227_);
		}
	}

	public static class Murgesku extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Murgesku()
		{
			
		}

		public Murgesku(String string, int i, SectFile sectfile, String string_228_, SectFile sectfile_229_, String string_230_)
		{
			super(string, i, sectfile, string_228_, sectfile_229_, string_230_);
		}
	}

	public static class MBoat extends BigshipGeneric implements TgtShip
	{
		public MBoat()
		{
			
		}

		public MBoat(String string, int i, SectFile sectfile, String string_231_, SectFile sectfile_232_, String string_233_)
		{
			super(string, i, sectfile, string_231_, sectfile_232_, string_233_);
		}
	}

	public static class Submarine extends ShipGeneric implements TgtShip
	{
		public Submarine()
		{
			
		}

		public Submarine(String string, int i, SectFile sectfile, String string_234_, SectFile sectfile_235_, String string_236_)
		{
			super(string, i, sectfile, string_234_, sectfile_235_, string_236_);
		}
	}

	public static class SubmarineP extends BigshipGeneric implements TgtShip
	{
		public SubmarineP()
		{
			
		}

		public SubmarineP(String string, int i, SectFile sectfile, String string_237_, SectFile sectfile_238_, String string_239_)
		{
			super(string, i, sectfile, string_237_, sectfile_238_, string_239_);
		}
	}

	public static class Niobe extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public Niobe()
		{
			
		}

		public Niobe(String string, int i, SectFile sectfile, String string_240_, SectFile sectfile_241_, String string_242_)
		{
			super(string, i, sectfile, string_240_, sectfile_241_, string_242_);
		}
	}

	public static class NiobeWithBeacon extends BigshipGeneric implements ZutiTypeWarShip, TgtShip, TypeHasBeacon
	{
		public NiobeWithBeacon()
		{
			
		}

		public NiobeWithBeacon(String string, int i, SectFile sectfile, String string_243_, SectFile sectfile_244_, String string_245_)
		{
			super(string, i, sectfile, string_243_, sectfile_244_, string_245_);
		}
	}

	public static class SubTypeVIIC_Srf extends BigshipGeneric implements TgtShip
	{
		public SubTypeVIIC_Srf()
		{
			
		}

		public SubTypeVIIC_Srf(String string, int i, SectFile sectfile, String string_246_, SectFile sectfile_247_, String string_248_)
		{
			super(string, i, sectfile, string_246_, sectfile_247_, string_248_);
		}
	}

	public static class SubTypeVIIC_SrfWithBeacon extends BigshipGeneric implements TgtShip, TypeHasBeacon
	{
		public SubTypeVIIC_SrfWithBeacon()
		{
			
		}

		public SubTypeVIIC_SrfWithBeacon(String string, int i, SectFile sectfile, String string_249_, SectFile sectfile_250_, String string_251_)
		{
			super(string, i, sectfile, string_249_, sectfile_250_, string_251_);
		}
	}

	public static class SubTypeVIIC_Sub extends BigshipGeneric implements TgtShip
	{
		public SubTypeVIIC_Sub()
		{
			
		}

		public SubTypeVIIC_Sub(String string, int i, SectFile sectfile, String string_252_, SectFile sectfile_253_, String string_254_)
		{
			super(string, i, sectfile, string_252_, sectfile_253_, string_254_);
		}
	}

	public static class Illmarinen extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public Illmarinen()
		{
			
		}

		public Illmarinen(String string, int i, SectFile sectfile, String string_255_, SectFile sectfile_256_, String string_257_)
		{
			super(string, i, sectfile, string_255_, sectfile_256_, string_257_);
		}
	}

	public static class Vainamoinen extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public Vainamoinen()
		{
			
		}

		public Vainamoinen(String string, int i, SectFile sectfile, String string_258_, SectFile sectfile_259_, String string_260_)
		{
			super(string, i, sectfile, string_258_, sectfile_259_, string_260_);
		}
	}

	public static class Tirpitz extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public Tirpitz()
		{
			
		}

		public Tirpitz(String string, int i, SectFile sectfile, String string_261_, SectFile sectfile_262_, String string_263_)
		{
			super(string, i, sectfile, string_261_, sectfile_262_, string_263_);
		}
	}

	public static class PAM extends BigshipGeneric implements TgtShip
	{
		public PAM()
		{
			
		}

		public PAM(String string, int i, SectFile sectfile, String string_264_, SectFile sectfile_265_, String string_266_)
		{
			super(string, i, sectfile, string_264_, sectfile_265_, string_266_);
		}
	}

	public static class IJNBBGeneric extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public IJNBBGeneric()
		{
			
		}

		public IJNBBGeneric(String string, int i, SectFile sectfile, String string_279_, SectFile sectfile_280_, String string_281_)
		{
			super(string, i, sectfile, string_279_, sectfile_280_, string_281_);
		}
	}

	public static class IJNAkizukiDD42 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNAkizukiDD42()
		{
			
		}

		public IJNAkizukiDD42(String string, int i, SectFile sectfile, String string_282_, SectFile sectfile_283_, String string_284_)
		{
			super(string, i, sectfile, string_282_, sectfile_283_, string_284_);
		}
	}

	public static class IJNAmatsukazeDD41 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNAmatsukazeDD41()
		{
			
		}

		public IJNAmatsukazeDD41(String string, int i, SectFile sectfile, String string_285_, SectFile sectfile_286_, String string_287_)
		{
			super(string, i, sectfile, string_285_, sectfile_286_, string_287_);
		}
	}

	public static class IJNArashiDD41 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNArashiDD41()
		{
			
		}

		public IJNArashiDD41(String string, int i, SectFile sectfile, String string_288_, SectFile sectfile_289_, String string_290_)
		{
			super(string, i, sectfile, string_288_, sectfile_289_, string_290_);
		}
	}

	public static class IJNKageroDD41 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNKageroDD41()
		{
			
		}

		public IJNKageroDD41(String string, int i, SectFile sectfile, String string_291_, SectFile sectfile_292_, String string_293_)
		{
			super(string, i, sectfile, string_291_, sectfile_292_, string_293_);
		}
	}

	public static class IJNNowakiDD41 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNNowakiDD41()
		{
			
		}

		public IJNNowakiDD41(String string, int i, SectFile sectfile, String string_294_, SectFile sectfile_295_, String string_296_)
		{
			super(string, i, sectfile, string_294_, sectfile_295_, string_296_);
		}
	}

	public static class IJNYukikazeDD41 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNYukikazeDD41()
		{
			
		}

		public IJNYukikazeDD41(String string, int i, SectFile sectfile, String string_297_, SectFile sectfile_298_, String string_299_)
		{
			super(string, i, sectfile, string_297_, sectfile_298_, string_299_);
		}
	}

	public static class IJNAmatsukazeDD43 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNAmatsukazeDD43()
		{
			
		}

		public IJNAmatsukazeDD43(String string, int i, SectFile sectfile, String string_300_, SectFile sectfile_301_, String string_302_)
		{
			super(string, i, sectfile, string_300_, sectfile_301_, string_302_);
		}
	}

	public static class IJNAmatsukazeDD43WithBeacon extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip, TypeHasBeacon
	{
		public IJNAmatsukazeDD43WithBeacon()
		{
			
		}

		public IJNAmatsukazeDD43WithBeacon(String string, int i, SectFile sectfile, String string_303_, SectFile sectfile_304_, String string_305_)
		{
			super(string, i, sectfile, string_303_, sectfile_304_, string_305_);
		}
	}

	public static class IJNNowakiDD43 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNNowakiDD43()
		{
			
		}

		public IJNNowakiDD43(String string, int i, SectFile sectfile, String string_306_, SectFile sectfile_307_, String string_308_)
		{
			super(string, i, sectfile, string_306_, sectfile_307_, string_308_);
		}
	}

	public static class IJNYukikazeDD43 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNYukikazeDD43()
		{
			
		}

		public IJNYukikazeDD43(String string, int i, SectFile sectfile, String string_309_, SectFile sectfile_310_, String string_311_)
		{
			super(string, i, sectfile, string_309_, sectfile_310_, string_311_);
		}
	}

	public static class IJNAmatsukazeDD45 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNAmatsukazeDD45()
		{
			
		}

		public IJNAmatsukazeDD45(String string, int i, SectFile sectfile, String string_312_, SectFile sectfile_313_, String string_314_)
		{
			super(string, i, sectfile, string_312_, sectfile_313_, string_314_);
		}
	}

	public static class IJNYukikazeDD45 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public IJNYukikazeDD45()
		{
			
		}

		public IJNYukikazeDD45(String string, int i, SectFile sectfile, String string_315_, SectFile sectfile_316_, String string_317_)
		{
			super(string, i, sectfile, string_315_, sectfile_316_, string_317_);
		}
	}

	public static class IJNFishJunk extends BigshipGeneric implements TgtShip
	{
		public IJNFishJunk()
		{
			
		}

		public IJNFishJunk(String string, int i, SectFile sectfile, String string_318_, SectFile sectfile_319_, String string_320_)
		{
			super(string, i, sectfile, string_318_, sectfile_319_, string_320_);
		}
	}

	public static class IJNFishJunkA extends BigshipGeneric implements TgtShip
	{
		public IJNFishJunkA()
		{
			
		}

		public IJNFishJunkA(String string, int i, SectFile sectfile, String string_321_, SectFile sectfile_322_, String string_323_)
		{
			super(string, i, sectfile, string_321_, sectfile_322_, string_323_);
		}
	}

	public static class DaihatsuLC extends ShipGeneric implements TgtShip
	{
		public DaihatsuLC()
		{
			
		}

		public DaihatsuLC(String string, int i, SectFile sectfile, String string_324_, SectFile sectfile_325_, String string_326_)
		{
			super(string, i, sectfile, string_324_, sectfile_325_, string_326_);
		}
	}

	public static class PilotWater_JA extends ShipGeneric implements TgtShip, WeakBody
	{
		public PilotWater_JA()
		{
			
		}

		public PilotWater_JA(String string, int i, SectFile sectfile, String string_327_, SectFile sectfile_328_, String string_329_)
		{
			super(string, i, sectfile, string_327_, sectfile_328_, string_329_);
		}
	}

	public static class RwyCon extends BigshipGeneric implements TgtShip
	{
		public RwyCon()
		{
			
		}

		public RwyCon(String string, int i, SectFile sectfile, String string_330_, SectFile sectfile_331_, String string_332_)
		{
			super(string, i, sectfile, string_330_, sectfile_331_, string_332_);
		}
	}

	public static class RwySteel extends BigshipGeneric implements TgtShip
	{
		public RwySteel()
		{
			
		}

		public RwySteel(String string, int i, SectFile sectfile, String string_333_, SectFile sectfile_334_, String string_335_)
		{
			super(string, i, sectfile, string_333_, sectfile_334_, string_335_);
		}
	}

	public static class RwySteelLow extends BigshipGeneric implements TgtShip
	{
		public RwySteelLow()
		{
			
		}

		public RwySteelLow(String string, int i, SectFile sectfile, String string_336_, SectFile sectfile_337_, String string_338_)
		{
			super(string, i, sectfile, string_336_, sectfile_337_, string_338_);
		}
	}

	public static class DestroyerKM extends ShipGeneric implements TgtShip
	{
		public DestroyerKM()
		{
			
		}

		public DestroyerKM(String string, int i, SectFile sectfile, String string_339_, SectFile sectfile_340_, String string_341_)
		{
			super(string, i, sectfile, string_339_, sectfile_340_, string_341_);
		}
	}

	public static class DestroyerRN extends ShipGeneric implements TgtShip
	{
		public DestroyerRN()
		{
			
		}

		public DestroyerRN(String string, int i, SectFile sectfile, String string_342_, SectFile sectfile_343_, String string_344_)
		{
			super(string, i, sectfile, string_342_, sectfile_343_, string_344_);
		}
	}

	public static class MAS501RN extends ShipGeneric implements TgtShip
	{
		public MAS501RN()
		{
			
		}

		public MAS501RN(String string, int i, SectFile sectfile, String string_345_, SectFile sectfile_346_, String string_347_)
		{
			super(string, i, sectfile, string_345_, sectfile_346_, string_347_);
		}
	}

	public static class MAS501UNP extends ShipGeneric implements TgtShip
	{
		public MAS501UNP()
		{
			
		}

		public MAS501UNP(String string, int i, SectFile sectfile, String string_348_, SectFile sectfile_349_, String string_350_)
		{
			super(string, i, sectfile, string_348_, sectfile_349_, string_350_);
		}
	}

	public static class MAS501UNE extends ShipGeneric implements TgtShip
	{
		public MAS501UNE()
		{
			
		}

		public MAS501UNE(String string, int i, SectFile sectfile, String string_351_, SectFile sectfile_352_, String string_353_)
		{
			super(string, i, sectfile, string_351_, sectfile_352_, string_353_);
		}
	}

	public static class MAS501JP extends ShipGeneric implements TgtShip
	{
		public MAS501JP()
		{
			
		}

		public MAS501JP(String string, int i, SectFile sectfile, String string_354_, SectFile sectfile_355_, String string_356_)
		{
			super(string, i, sectfile, string_354_, sectfile_355_, string_356_);
		}
	}

	public static class DLCWreck extends ShipGeneric implements TgtShip
	{
		public DLCWreck()
		{
			
		}

		public DLCWreck(String string, int i, SectFile sectfile, String string_357_, SectFile sectfile_358_, String string_359_)
		{
			super(string, i, sectfile, string_357_, sectfile_358_, string_359_);
		}
	}

	public static class LCVPWreck extends ShipGeneric implements TgtShip
	{
		public LCVPWreck()
		{
			
		}

		public LCVPWreck(String string, int i, SectFile sectfile, String string_360_, SectFile sectfile_361_, String string_362_)
		{
			super(string, i, sectfile, string_360_, sectfile_361_, string_362_);
		}
	}

	public static class SmallWreck extends ShipGeneric implements TgtShip
	{
		public SmallWreck()
		{
			
		}

		public SmallWreck(String string, int i, SectFile sectfile, String string_363_, SectFile sectfile_364_, String string_365_)
		{
			super(string, i, sectfile, string_363_, sectfile_364_, string_365_);
		}
	}

	public static class Fisherman2 extends ShipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Fisherman2()
		{
			
		}

		public Fisherman2(String string, int i, SectFile sectfile, String string_366_, SectFile sectfile_367_, String string_368_)
		{
			super(string, i, sectfile, string_366_, sectfile_367_, string_368_);
		}
	}

	public static class Fisherman1 extends ShipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Fisherman1()
		{
			
		}

		public Fisherman1(String string, int i, SectFile sectfile, String string_369_, SectFile sectfile_370_, String string_371_)
		{
			super(string, i, sectfile, string_369_, sectfile_370_, string_371_);
		}
	}

	public static class Fisherman extends ShipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Fisherman()
		{
			
		}

		public Fisherman(String string, int i, SectFile sectfile, String string_372_, SectFile sectfile_373_, String string_374_)
		{
			super(string, i, sectfile, string_372_, sectfile_373_, string_374_);
		}
	}

	public static class TransDmg extends BigshipGeneric implements TgtShip
	{
		public TransDmg()
		{
			
		}

		public TransDmg(String string, int i, SectFile sectfile, String string_375_, SectFile sectfile_376_, String string_377_)
		{
			super(string, i, sectfile, string_375_, sectfile_376_, string_377_);
		}
	}

	public static class TransWreck extends BigshipGeneric implements TgtShip
	{
		public TransWreck()
		{
			
		}

		public TransWreck(String string, int i, SectFile sectfile, String string_378_, SectFile sectfile_379_, String string_380_)
		{
			super(string, i, sectfile, string_378_, sectfile_379_, string_380_);
		}
	}

	public static class Transport3 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Transport3()
		{
			
		}

		public Transport3(String string, int i, SectFile sectfile, String string_381_, SectFile sectfile_382_, String string_383_)
		{
			super(string, i, sectfile, string_381_, sectfile_382_, string_383_);
		}
	}

	public static class Transport2 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Transport2()
		{
			
		}

		public Transport2(String string, int i, SectFile sectfile, String string_384_, SectFile sectfile_385_, String string_386_)
		{
			super(string, i, sectfile, string_384_, sectfile_385_, string_386_);
		}
	}

	public static class Transport1 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Transport1()
		{
			
		}

		public Transport1(String string, int i, SectFile sectfile, String string_387_, SectFile sectfile_388_, String string_389_)
		{
			super(string, i, sectfile, string_387_, sectfile_388_, string_389_);
		}
	}

	public static class HospitalShip extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public HospitalShip()
		{
			
		}

		public HospitalShip(String string, int i, SectFile sectfile, String string_390_, SectFile sectfile_391_, String string_392_)
		{
			super(string, i, sectfile, string_390_, sectfile_391_, string_392_);
		}
	}

	public static class Barge0 extends ShipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Barge0()
		{
			
		}

		public Barge0(String string, int i, SectFile sectfile, String string_393_, SectFile sectfile_394_, String string_395_)
		{
			super(string, i, sectfile, string_393_, sectfile_394_, string_395_);
		}
	}

	public static class Barge1 extends ShipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Barge1()
		{
			
		}

		public Barge1(String string, int i, SectFile sectfile, String string_396_, SectFile sectfile_397_, String string_398_)
		{
			super(string, i, sectfile, string_396_, sectfile_397_, string_398_);
		}
	}

	public static class DestroyerDmg extends BigshipGeneric implements TgtShip
	{
		public DestroyerDmg()
		{
			
		}

		public DestroyerDmg(String string, int i, SectFile sectfile, String string_402_, SectFile sectfile_403_, String string_404_)
		{
			super(string, i, sectfile, string_402_, sectfile_403_, string_404_);
		}
	}

	public static class DestroyerWreck extends BigshipGeneric implements TgtShip
	{
		public DestroyerWreck()
		{
			
		}

		public DestroyerWreck(String string, int i, SectFile sectfile, String string_405_, SectFile sectfile_406_, String string_407_)
		{
			super(string, i, sectfile, string_405_, sectfile_406_, string_407_);
		}
	}

	public static class Destroyer2 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Destroyer2()
		{
			
		}

		public Destroyer2(String string, int i, SectFile sectfile, String string_408_, SectFile sectfile_409_, String string_410_)
		{
			super(string, i, sectfile, string_408_, sectfile_409_, string_410_);
		}
	}

	public static class Destroyer1 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Destroyer1()
		{
			
		}

		public Destroyer1(String string, int i, SectFile sectfile, String string_411_, SectFile sectfile_412_, String string_413_)
		{
			super(string, i, sectfile, string_411_, sectfile_412_, string_413_);
		}
	}

	public static class Destroyer0 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Destroyer0()
		{
			
		}

		public Destroyer0(String string, int i, SectFile sectfile, String string_414_, SectFile sectfile_415_, String string_416_)
		{
			super(string, i, sectfile, string_414_, sectfile_415_, string_416_);
		}
	}

	public static class TankerDmg extends BigshipGeneric implements TgtShip
	{
		public TankerDmg()
		{
			
		}

		public TankerDmg(String string, int i, SectFile sectfile, String string_417_, SectFile sectfile_418_, String string_419_)
		{
			super(string, i, sectfile, string_417_, sectfile_418_, string_419_);
		}
	}

	public static class Tanker2 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Tanker2()
		{
			
		}

		public Tanker2(String string, int i, SectFile sectfile, String string_420_, SectFile sectfile_421_, String string_422_)
		{
			super(string, i, sectfile, string_420_, sectfile_421_, string_422_);
		}
	}

	public static class Tanker1 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Tanker1()
		{
			
		}

		public Tanker1(String string, int i, SectFile sectfile, String string_423_, SectFile sectfile_424_, String string_425_)
		{
			super(string, i, sectfile, string_423_, sectfile_424_, string_425_);
		}
	}

	public static class Tanker0 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Tanker0()
		{
			
		}

		public Tanker0(String string, int i, SectFile sectfile, String string_426_, SectFile sectfile_427_, String string_428_)
		{
			super(string, i, sectfile, string_426_, sectfile_427_, string_428_);
		}
	}

	public static class Boat1 extends ShipGeneric implements TgtShip
	{
		public Boat1()
		{
			
		}

		public Boat1(String string, int i, SectFile sectfile, String string_429_, SectFile sectfile_430_, String string_431_)
		{
			super(string, i, sectfile, string_429_, sectfile_430_, string_431_);
		}
	}

	public static class Boat extends ShipGeneric implements TgtShip
	{
		public Boat()
		{
			
		}

		public Boat(String string, int i, SectFile sectfile, String string_432_, SectFile sectfile_433_, String string_434_)
		{
			super(string, i, sectfile, string_432_, sectfile_433_, string_434_);
		}
	}

	public static class MFPIT extends ShipGeneric implements TgtShip
	{
		public MFPIT()
		{
			
		}

		public MFPIT(String string, int i, SectFile sectfile, String string_435_, SectFile sectfile_436_, String string_437_)
		{
			super(string, i, sectfile, string_435_, sectfile_436_, string_437_);
		}
	}

	public static class MFPT extends ShipGeneric implements TgtShip
	{
		public MFPT()
		{
			
		}

		public MFPT(String string, int i, SectFile sectfile, String string_438_, SectFile sectfile_439_, String string_440_)
		{
			super(string, i, sectfile, string_438_, sectfile_439_, string_440_);
		}
	}

	public static class USSMcKean extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public USSMcKean()
		{
			
		}

		public USSMcKean(String string, int i, SectFile sectfile, String string_441_, SectFile sectfile_442_, String string_443_)
		{
			super(string, i, sectfile, string_441_, sectfile_442_, string_443_);
		}
	}

	public static class Transport4 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Transport4()
		{
			
		}

		public Transport4(String string, int i, SectFile sectfile, String string_444_, SectFile sectfile_445_, String string_446_)
		{
			super(string, i, sectfile, string_444_, sectfile_445_, string_446_);
		}
	}

	public static class Transport5 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Transport5()
		{
			
		}

		public Transport5(String string, int i, SectFile sectfile, String string_447_, SectFile sectfile_448_, String string_449_)
		{
			super(string, i, sectfile, string_447_, sectfile_448_, string_449_);
		}
	}

	public static class Transport6 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Transport6()
		{
			
		}

		public Transport6(String string, int i, SectFile sectfile, String string_450_, SectFile sectfile_451_, String string_452_)
		{
			super(string, i, sectfile, string_450_, sectfile_451_, string_452_);
		}
	}

	public static class Transport7 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Transport7()
		{
			
		}

		public Transport7(String string, int i, SectFile sectfile, String string_453_, SectFile sectfile_454_, String string_455_)
		{
			super(string, i, sectfile, string_453_, sectfile_454_, string_455_);
		}
	}

	public static class TroopTrans3 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public TroopTrans3()
		{
			
		}

		public TroopTrans3(String string, int i, SectFile sectfile, String string_456_, SectFile sectfile_457_, String string_458_)
		{
			super(string, i, sectfile, string_456_, sectfile_457_, string_458_);
		}
	}

	public static class TroopTrans2 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public TroopTrans2()
		{
			
		}

		public TroopTrans2(String string, int i, SectFile sectfile, String string_459_, SectFile sectfile_460_, String string_461_)
		{
			super(string, i, sectfile, string_459_, sectfile_460_, string_461_);
		}
	}

	public static class TroopTrans1 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public TroopTrans1()
		{
			
		}

		public TroopTrans1(String string, int i, SectFile sectfile, String string_462_, SectFile sectfile_463_, String string_464_)
		{
			super(string, i, sectfile, string_462_, sectfile_463_, string_464_);
		}
	}

	public static class TroopTrans0 extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public TroopTrans0()
		{
			
		}

		public TroopTrans0(String string, int i, SectFile sectfile, String string_465_, SectFile sectfile_466_, String string_467_)
		{
			super(string, i, sectfile, string_465_, sectfile_466_, string_467_);
		}
	}

	public static class Italia1 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Italia1()
		{
			
		}

		public Italia1(String string, int i, SectFile sectfile, String string_468_, SectFile sectfile_469_, String string_470_)
		{
			super(string, i, sectfile, string_468_, sectfile_469_, string_470_);
		}
	}

	public static class Italia0 extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Italia0()
		{
			
		}

		public Italia0(String string, int i, SectFile sectfile, String string_471_, SectFile sectfile_472_, String string_473_)
		{
			super(string, i, sectfile, string_471_, sectfile_472_, string_473_);
		}
	}

	public static class HMSFiji extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public HMSFiji()
		{
			
		}

		public HMSFiji(String string, int i, SectFile sectfile, String string_501_, SectFile sectfile_502_, String string_503_)
		{
			super(string, i, sectfile, string_501_, sectfile_502_, string_503_);
		}
	}

	public static class Littorio extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Littorio()
		{
			
		}

		public Littorio(String string, int i, SectFile sectfile, String string_504_, SectFile sectfile_505_, String string_506_)
		{
			super(string, i, sectfile, string_504_, sectfile_505_, string_506_);
		}
	}

	public static class Soldati extends BigshipGeneric implements ZutiTypeDestroyer, TgtShip
	{
		public Soldati()
		{
			
		}

		public Soldati(String string, int i, SectFile sectfile, String string_507_, SectFile sectfile_508_, String string_509_)
		{
			super(string, i, sectfile, string_507_, sectfile_508_, string_509_);
		}
	}

	public static class RwyTranspWide extends BigshipGeneric implements TgtShip
	{
		public RwyTranspWide()
		{
			
		}

		public RwyTranspWide(String string, int i, SectFile sectfile, String string_543_, SectFile sectfile_544_, String string_545_)
		{
			super(string, i, sectfile, string_543_, sectfile_544_, string_545_);
		}
	}

	public static class RwyTranspSqr extends BigshipGeneric implements TgtShip
	{
		public RwyTranspSqr()
		{
			
		}

		public RwyTranspSqr(String string, int i, SectFile sectfile, String string_546_, SectFile sectfile_547_, String string_548_)
		{
			super(string, i, sectfile, string_546_, sectfile_547_, string_548_);
		}
	}

	public static class Zephyr extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Zephyr()
		{
			
		}

		public Zephyr(String string, int i, SectFile sectfile, String string_549_, SectFile sectfile_550_, String string_551_)
		{
			super(string, i, sectfile, string_549_, sectfile_550_, string_551_);
		}
	}

	public static class WhiteRiver extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public WhiteRiver()
		{
			
		}

		public WhiteRiver(String string, int i, SectFile sectfile, String string_552_, SectFile sectfile_553_, String string_554_)
		{
			super(string, i, sectfile, string_552_, sectfile_553_, string_554_);
		}
	}

	public static class VIICwinter_Sub extends ShipGeneric implements TgtShip
	{
		public VIICwinter_Sub()
		{
			
		}

		public VIICwinter_Sub(String string, int i, SectFile sectfile, String string_555_, SectFile sectfile_556_, String string_557_)
		{
			super(string, i, sectfile, string_555_, sectfile_556_, string_557_);
		}
	}

	public static class VIICwinter_Srf extends BigshipGeneric implements TgtShip
	{
		public VIICwinter_Srf()
		{
			
		}

		public VIICwinter_Srf(String string, int i, SectFile sectfile, String string_558_, SectFile sectfile_559_, String string_560_)
		{
			super(string, i, sectfile, string_558_, sectfile_559_, string_560_);
		}
	}

	public static class VIICU564_Sub extends ShipGeneric implements TgtShip
	{
		public VIICU564_Sub()
		{
			
		}

		public VIICU564_Sub(String string, int i, SectFile sectfile, String string_561_, SectFile sectfile_562_, String string_563_)
		{
			super(string, i, sectfile, string_561_, sectfile_562_, string_563_);
		}
	}

	public static class VIICU564_Srf extends BigshipGeneric implements TgtShip
	{
		public VIICU564_Srf()
		{
			
		}

		public VIICU564_Srf(String string, int i, SectFile sectfile, String string_564_, SectFile sectfile_565_, String string_566_)
		{
			super(string, i, sectfile, string_564_, sectfile_565_, string_566_);
		}
	}

	public static class VIICU124_Sub extends ShipGeneric implements TgtShip
	{
		public VIICU124_Sub()
		{
			
		}

		public VIICU124_Sub(String string, int i, SectFile sectfile, String string_567_, SectFile sectfile_568_, String string_569_)
		{
			super(string, i, sectfile, string_567_, sectfile_568_, string_569_);
		}
	}

	public static class VIICU124_Srf extends BigshipGeneric implements TgtShip
	{
		public VIICU124_Srf()
		{
			
		}

		public VIICU124_Srf(String string, int i, SectFile sectfile, String string_570_, SectFile sectfile_571_, String string_572_)
		{
			super(string, i, sectfile, string_570_, sectfile_571_, string_572_);
		}
	}

	public static class VIICswordfish_Sub extends ShipGeneric implements TgtShip
	{
		public VIICswordfish_Sub()
		{
			
		}

		public VIICswordfish_Sub(String string, int i, SectFile sectfile, String string_573_, SectFile sectfile_574_, String string_575_)
		{
			super(string, i, sectfile, string_573_, sectfile_574_, string_575_);
		}
	}

	public static class VIICswordfish_Srf extends BigshipGeneric implements TgtShip
	{
		public VIICswordfish_Srf()
		{
			
		}

		public VIICswordfish_Srf(String string, int i, SectFile sectfile, String string_576_, SectFile sectfile_577_, String string_578_)
		{
			super(string, i, sectfile, string_576_, sectfile_577_, string_578_);
		}
	}

	public static class VIICRO500_Sub extends ShipGeneric implements TgtShip
	{
		public VIICRO500_Sub()
		{
			
		}

		public VIICRO500_Sub(String string, int i, SectFile sectfile, String string_579_, SectFile sectfile_580_, String string_581_)
		{
			super(string, i, sectfile, string_579_, sectfile_580_, string_581_);
		}
	}

	public static class VIICRO500_Srf extends BigshipGeneric implements TgtShip
	{
		public VIICRO500_Srf()
		{
			
		}

		public VIICRO500_Srf(String string, int i, SectFile sectfile, String string_582_, SectFile sectfile_583_, String string_584_)
		{
			super(string, i, sectfile, string_582_, sectfile_583_, string_584_);
		}
	}

	public static class USSSpot_Sub extends ShipGeneric implements TgtShip
	{
		public USSSpot_Sub()
		{
			
		}

		public USSSpot_Sub(String string, int i, SectFile sectfile, String string_585_, SectFile sectfile_586_, String string_587_)
		{
			super(string, i, sectfile, string_585_, sectfile_586_, string_587_);
		}
	}

	public static class USSSpot_Srf extends BigshipGeneric implements TgtShip
	{
		public USSSpot_Srf()
		{
			
		}

		public USSSpot_Srf(String string, int i, SectFile sectfile, String string_588_, SectFile sectfile_589_, String string_590_)
		{
			super(string, i, sectfile, string_588_, sectfile_589_, string_590_);
		}
	}

	public static class USSPermit_Sub extends ShipGeneric implements TgtShip
	{
		public USSPermit_Sub()
		{
			
		}

		public USSPermit_Sub(String string, int i, SectFile sectfile, String string_591_, SectFile sectfile_592_, String string_593_)
		{
			super(string, i, sectfile, string_591_, sectfile_592_, string_593_);
		}
	}

	public static class USSPermit_Srf extends BigshipGeneric implements TgtShip
	{
		public USSPermit_Srf()
		{
			
		}

		public USSPermit_Srf(String string, int i, SectFile sectfile, String string_594_, SectFile sectfile_595_, String string_596_)
		{
			super(string, i, sectfile, string_594_, sectfile_595_, string_596_);
		}
	}

	public static class USSNautilus_Sub extends ShipGeneric implements TgtShip
	{
		public USSNautilus_Sub()
		{
			
		}

		public USSNautilus_Sub(String string, int i, SectFile sectfile, String string_597_, SectFile sectfile_598_, String string_599_)
		{
			super(string, i, sectfile, string_597_, sectfile_598_, string_599_);
		}
	}

	public static class USSNautilus_Srf extends BigshipGeneric implements TgtShip
	{
		public USSNautilus_Srf()
		{
			
		}

		public USSNautilus_Srf(String string, int i, SectFile sectfile, String string_600_, SectFile sectfile_601_, String string_602_)
		{
			super(string, i, sectfile, string_600_, sectfile_601_, string_602_);
		}
	}

	public static class USSBesugo_Sub extends ShipGeneric implements TgtShip
	{
		public USSBesugo_Sub()
		{
			
		}

		public USSBesugo_Sub(String string, int i, SectFile sectfile, String string_603_, SectFile sectfile_604_, String string_605_)
		{
			super(string, i, sectfile, string_603_, sectfile_604_, string_605_);
		}
	}

	public static class USSBesugo_Srf extends BigshipGeneric implements TgtShip
	{
		public USSBesugo_Srf()
		{
			
		}

		public USSBesugo_Srf(String string, int i, SectFile sectfile, String string_606_, SectFile sectfile_607_, String string_608_)
		{
			super(string, i, sectfile, string_606_, sectfile_607_, string_608_);
		}
	}

	public static class TakatsuMaru extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public TakatsuMaru()
		{
			
		}

		public TakatsuMaru(String string, int i, SectFile sectfile, String string_609_, SectFile sectfile_610_, String string_611_)
		{
			super(string, i, sectfile, string_609_, sectfile_610_, string_611_);
		}
	}

	public static class Sumatra extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Sumatra()
		{
			
		}

		public Sumatra(String string, int i, SectFile sectfile, String string_612_, SectFile sectfile_613_, String string_614_)
		{
			super(string, i, sectfile, string_612_, sectfile_613_, string_614_);
		}
	}

	public static class Speedwell extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Speedwell()
		{
			
		}

		public Speedwell(String string, int i, SectFile sectfile, String string_615_, SectFile sectfile_616_, String string_617_)
		{
			super(string, i, sectfile, string_615_, sectfile_616_, string_617_);
		}
	}

	public static class SetsuMaru extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public SetsuMaru()
		{
			
		}

		public SetsuMaru(String string, int i, SectFile sectfile, String string_618_, SectFile sectfile_619_, String string_620_)
		{
			super(string, i, sectfile, string_618_, sectfile_619_, string_620_);
		}
	}

	public static class SeawayPrincess extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public SeawayPrincess()
		{
			
		}

		public SeawayPrincess(String string, int i, SectFile sectfile, String string_621_, SectFile sectfile_622_, String string_623_)
		{
			super(string, i, sectfile, string_621_, sectfile_622_, string_623_);
		}
	}

	public static class RedCanyon extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public RedCanyon()
		{
			
		}

		public RedCanyon(String string, int i, SectFile sectfile, String string_624_, SectFile sectfile_625_, String string_626_)
		{
			super(string, i, sectfile, string_624_, sectfile_625_, string_626_);
		}
	}

	public static class MSAtenfels extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public MSAtenfels()
		{
			
		}

		public MSAtenfels(String string, int i, SectFile sectfile, String string_627_, SectFile sectfile_628_, String string_629_)
		{
			super(string, i, sectfile, string_627_, sectfile_628_, string_629_);
		}
	}

	public static class Mararoa extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Mararoa()
		{
			
		}

		public Mararoa(String string, int i, SectFile sectfile, String string_630_, SectFile sectfile_631_, String string_632_)
		{
			super(string, i, sectfile, string_630_, sectfile_631_, string_632_);
		}
	}

	public static class Mapito extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Mapito()
		{
			
		}

		public Mapito(String string, int i, SectFile sectfile, String string_633_, SectFile sectfile_634_, String string_635_)
		{
			super(string, i, sectfile, string_633_, sectfile_634_, string_635_);
		}
	}

	public static class MalaysianQueen extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public MalaysianQueen()
		{
			
		}

		public MalaysianQueen(String string, int i, SectFile sectfile, String string_636_, SectFile sectfile_637_, String string_638_)
		{
			super(string, i, sectfile, string_636_, sectfile_637_, string_638_);
		}
	}

	public static class LSSimonBenson extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public LSSimonBenson()
		{
			
		}

		public LSSimonBenson(String string, int i, SectFile sectfile, String string_639_, SectFile sectfile_640_, String string_641_)
		{
			super(string, i, sectfile, string_639_, sectfile_640_, string_641_);
		}
	}

	public static class LSHuddell extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public LSHuddell()
		{
			
		}

		public LSHuddell(String string, int i, SectFile sectfile, String string_642_, SectFile sectfile_643_, String string_644_)
		{
			super(string, i, sectfile, string_642_, sectfile_643_, string_644_);
		}
	}

	public static class KMHansa extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public KMHansa()
		{
			
		}

		public KMHansa(String string, int i, SectFile sectfile, String string_645_, SectFile sectfile_646_, String string_647_)
		{
			super(string, i, sectfile, string_645_, sectfile_646_, string_647_);
		}
	}

	public static class Janssens extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Janssens()
		{
			
		}

		public Janssens(String string, int i, SectFile sectfile, String string_648_, SectFile sectfile_649_, String string_650_)
		{
			super(string, i, sectfile, string_648_, sectfile_649_, string_650_);
		}
	}

	public static class IICU57P extends ShipGeneric implements TgtShip
	{
		public IICU57P()
		{
			
		}

		public IICU57P(String string, int i, SectFile sectfile, String string_651_, SectFile sectfile_652_, String string_653_)
		{
			super(string, i, sectfile, string_651_, sectfile_652_, string_653_);
		}
	}

	public static class IICU57 extends ShipGeneric implements TgtShip
	{
		public IICU57()
		{
			
		}

		public IICU57(String string, int i, SectFile sectfile, String string_654_, SectFile sectfile_655_, String string_656_)
		{
			super(string, i, sectfile, string_654_, sectfile_655_, string_656_);
		}
	}

	public static class IICU12P extends ShipGeneric implements TgtShip
	{
		public IICU12P()
		{
			
		}

		public IICU12P(String string, int i, SectFile sectfile, String string_657_, SectFile sectfile_658_, String string_659_)
		{
			super(string, i, sectfile, string_657_, sectfile_658_, string_659_);
		}
	}

	public static class IICU12 extends ShipGeneric implements TgtShip
	{
		public IICU12()
		{
			
		}

		public IICU12(String string, int i, SectFile sectfile, String string_660_, SectFile sectfile_661_, String string_662_)
		{
			super(string, i, sectfile, string_660_, sectfile_661_, string_662_);
		}
	}

	public static class IICU3P extends ShipGeneric implements TgtShip
	{
		public IICU3P()
		{
			
		}

		public IICU3P(String string, int i, SectFile sectfile, String string_663_, SectFile sectfile_664_, String string_665_)
		{
			super(string, i, sectfile, string_663_, sectfile_664_, string_665_);
		}
	}

	public static class IICU3 extends ShipGeneric implements TgtShip
	{
		public IICU3()
		{
			
		}

		public IICU3(String string, int i, SectFile sectfile, String string_666_, SectFile sectfile_667_, String string_668_)
		{
			super(string, i, sectfile, string_666_, sectfile_667_, string_668_);
		}
	}

	public static class IIbfinlandP extends ShipGeneric implements TgtShip
	{
		public IIbfinlandP()
		{
			
		}

		public IIbfinlandP(String string, int i, SectFile sectfile, String string_669_, SectFile sectfile_670_, String string_671_)
		{
			super(string, i, sectfile, string_669_, sectfile_670_, string_671_);
		}
	}

	public static class IIbfinland extends ShipGeneric implements TgtShip
	{
		public IIbfinland()
		{
			
		}

		public IIbfinland(String string, int i, SectFile sectfile, String string_672_, SectFile sectfile_673_, String string_674_)
		{
			super(string, i, sectfile, string_672_, sectfile_673_, string_674_);
		}
	}

	public static class Holmlea extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Holmlea()
		{
			
		}

		public Holmlea(String string, int i, SectFile sectfile, String string_675_, SectFile sectfile_676_, String string_677_)
		{
			super(string, i, sectfile, string_675_, sectfile_676_, string_677_);
		}
	}

	public static class HokokuMaru extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public HokokuMaru()
		{
			
		}

		public HokokuMaru(String string, int i, SectFile sectfile, String string_678_, SectFile sectfile_679_, String string_680_)
		{
			super(string, i, sectfile, string_678_, sectfile_679_, string_680_);
		}
	}

	public static class germantrawlerA extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public germantrawlerA()
		{
			
		}

		public germantrawlerA(String string, int i, SectFile sectfile, String string_684_, SectFile sectfile_685_, String string_686_)
		{
			super(string, i, sectfile, string_684_, sectfile_685_, string_686_);
		}
	}

	public static class FortGeorge extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public FortGeorge()
		{
			
		}

		public FortGeorge(String string, int i, SectFile sectfile, String string_687_, SectFile sectfile_688_, String string_689_)
		{
			super(string, i, sectfile, string_687_, sectfile_688_, string_689_);
		}
	}

	public static class Bismarck extends BigshipGeneric implements ZutiTypeWarShip, TgtShip
	{
		public Bismarck()
		{
			
		}

		public Bismarck(String string, int i, SectFile sectfile, String string_690_, SectFile sectfile_691_, String string_692_)
		{
			super(string, i, sectfile, string_690_, sectfile_691_, string_692_);
		}
	}

	public static class Awanui extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Awanui()
		{
			
		}

		public Awanui(String string, int i, SectFile sectfile, String string_693_, SectFile sectfile_694_, String string_695_)
		{
			super(string, i, sectfile, string_693_, sectfile_694_, string_695_);
		}
	}

	public static class Apanui extends BigshipGeneric implements ZutiTypeRRR, TgtShip
	{
		public Apanui()
		{
			
		}

		public Apanui(String string, int i, SectFile sectfile, String string_696_, SectFile sectfile_697_, String string_698_)
		{
			super(string, i, sectfile, string_696_, sectfile_697_, string_698_);
		}
	}

	public static class RwyTransp extends BigshipGeneric implements TgtShip
	{
		public RwyTransp()
		{
			
		}

		public RwyTransp(String string, int i, SectFile sectfile, String string_699_, SectFile sectfile_700_, String string_701_)
		{
			super(string, i, sectfile, string_699_, sectfile_700_, string_701_);
		}
	}

	static Class class$ZutiShip(String string)
	{
		try
		{
			return Class.forName(string);
		}
		catch (ClassNotFoundException classnotfoundexception)
		{
			throw new NoClassDefFoundError(classnotfoundexception.getMessage());
		}
	}

	static
	{
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Apanui == null ? (class$com$maddox$il2$objects$ships$Ship$Apanui = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Apanui"))
				: class$com$maddox$il2$objects$ships$Ship$Apanui);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Awanui == null ? (class$com$maddox$il2$objects$ships$Ship$Awanui = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Awanui"))
				: class$com$maddox$il2$objects$ships$Ship$Awanui);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Bismarck == null ? (class$com$maddox$il2$objects$ships$Ship$Bismarck = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Bismarck"))
				: class$com$maddox$il2$objects$ships$Ship$Bismarck);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$FortGeorge == null ? (class$com$maddox$il2$objects$ships$Ship$FortGeorge = class$ZutiShip("com.maddox.il2.objects.ships.Ship$FortGeorge"))
						: class$com$maddox$il2$objects$ships$Ship$FortGeorge);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$germantrawlerA == null ? (class$com$maddox$il2$objects$ships$Ship$germantrawlerA = class$ZutiShip("com.maddox.il2.objects.ships.Ship$germantrawlerA"))
						: class$com$maddox$il2$objects$ships$Ship$germantrawlerA);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSColossusCV == null ? (class$com$maddox$il2$objects$ships$Ship$HMSColossusCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSColossusCV"))
						: class$com$maddox$il2$objects$ships$Ship$HMSColossusCV);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HokokuMaru == null ? (class$com$maddox$il2$objects$ships$Ship$HokokuMaru = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HokokuMaru"))
						: class$com$maddox$il2$objects$ships$Ship$HokokuMaru);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Holmlea == null ? (class$com$maddox$il2$objects$ships$Ship$Holmlea = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Holmlea"))
				: class$com$maddox$il2$objects$ships$Ship$Holmlea);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Janssens == null ? (class$com$maddox$il2$objects$ships$Ship$Janssens = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Janssens"))
				: class$com$maddox$il2$objects$ships$Ship$Janssens);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$KMHansa == null ? (class$com$maddox$il2$objects$ships$Ship$KMHansa = class$ZutiShip("com.maddox.il2.objects.ships.Ship$KMHansa"))
				: class$com$maddox$il2$objects$ships$Ship$KMHansa);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$LSHuddell == null ? (class$com$maddox$il2$objects$ships$Ship$LSHuddell = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LSHuddell"))
						: class$com$maddox$il2$objects$ships$Ship$LSHuddell);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$LSSimonBenson == null ? (class$com$maddox$il2$objects$ships$Ship$LSSimonBenson = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LSSimonBenson"))
						: class$com$maddox$il2$objects$ships$Ship$LSSimonBenson);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$MalaysianQueen == null ? (class$com$maddox$il2$objects$ships$Ship$MalaysianQueen = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MalaysianQueen"))
						: class$com$maddox$il2$objects$ships$Ship$MalaysianQueen);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Mapito == null ? (class$com$maddox$il2$objects$ships$Ship$Mapito = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Mapito"))
				: class$com$maddox$il2$objects$ships$Ship$Mapito);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Mararoa == null ? (class$com$maddox$il2$objects$ships$Ship$Mararoa = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Mararoa"))
				: class$com$maddox$il2$objects$ships$Ship$Mararoa);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$MSAtenfels == null ? (class$com$maddox$il2$objects$ships$Ship$MSAtenfels = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MSAtenfels"))
						: class$com$maddox$il2$objects$ships$Ship$MSAtenfels);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$RedCanyon == null ? (class$com$maddox$il2$objects$ships$Ship$RedCanyon = class$ZutiShip("com.maddox.il2.objects.ships.Ship$RedCanyon"))
						: class$com$maddox$il2$objects$ships$Ship$RedCanyon);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$SeawayPrincess == null ? (class$com$maddox$il2$objects$ships$Ship$SeawayPrincess = class$ZutiShip("com.maddox.il2.objects.ships.Ship$SeawayPrincess"))
						: class$com$maddox$il2$objects$ships$Ship$SeawayPrincess);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$SetsuMaru == null ? (class$com$maddox$il2$objects$ships$Ship$SetsuMaru = class$ZutiShip("com.maddox.il2.objects.ships.Ship$SetsuMaru"))
						: class$com$maddox$il2$objects$ships$Ship$SetsuMaru);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Speedwell == null ? (class$com$maddox$il2$objects$ships$Ship$Speedwell = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Speedwell"))
						: class$com$maddox$il2$objects$ships$Ship$Speedwell);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Sumatra == null ? (class$com$maddox$il2$objects$ships$Ship$Sumatra = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Sumatra"))
				: class$com$maddox$il2$objects$ships$Ship$Sumatra);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$TakatsuMaru == null ? (class$com$maddox$il2$objects$ships$Ship$TakatsuMaru = class$ZutiShip("com.maddox.il2.objects.ships.Ship$TakatsuMaru"))
						: class$com$maddox$il2$objects$ships$Ship$TakatsuMaru);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSBesugo_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$USSBesugo_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSBesugo_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$USSBesugo_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSNautilus_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$USSNautilus_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSNautilus_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$USSNautilus_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSPermit_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$USSPermit_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSPermit_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$USSPermit_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSSpot_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$USSSpot_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSSpot_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$USSSpot_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICRO500_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$VIICRO500_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICRO500_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$VIICRO500_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICswordfish_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$VIICswordfish_Srf = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICswordfish_Srf")))
						: class$com$maddox$il2$objects$ships$Ship$VIICswordfish_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICU124_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$VIICU124_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICU124_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$VIICU124_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICU564_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$VIICU564_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICU564_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$VIICU564_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICwinter_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$VIICwinter_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICwinter_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$VIICwinter_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$WhiteRiver == null ? (class$com$maddox$il2$objects$ships$Ship$WhiteRiver = class$ZutiShip("com.maddox.il2.objects.ships.Ship$WhiteRiver"))
						: class$com$maddox$il2$objects$ships$Ship$WhiteRiver);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Zephyr == null ? (class$com$maddox$il2$objects$ships$Ship$Zephyr = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Zephyr"))
				: class$com$maddox$il2$objects$ships$Ship$Zephyr);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IIbfinland == null ? (class$com$maddox$il2$objects$ships$Ship$IIbfinland = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IIbfinland"))
						: class$com$maddox$il2$objects$ships$Ship$IIbfinland);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IIbfinlandP == null ? (class$com$maddox$il2$objects$ships$Ship$IIbfinlandP = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IIbfinlandP"))
						: class$com$maddox$il2$objects$ships$Ship$IIbfinlandP);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$IICU3 == null ? (class$com$maddox$il2$objects$ships$Ship$IICU3 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IICU3"))
				: class$com$maddox$il2$objects$ships$Ship$IICU3);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$IICU3P == null ? (class$com$maddox$il2$objects$ships$Ship$IICU3P = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IICU3P"))
				: class$com$maddox$il2$objects$ships$Ship$IICU3P);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$IICU12 == null ? (class$com$maddox$il2$objects$ships$Ship$IICU12 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IICU12"))
				: class$com$maddox$il2$objects$ships$Ship$IICU12);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$IICU12P == null ? (class$com$maddox$il2$objects$ships$Ship$IICU12P = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IICU12P"))
				: class$com$maddox$il2$objects$ships$Ship$IICU12P);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$IICU57 == null ? (class$com$maddox$il2$objects$ships$Ship$IICU57 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IICU57"))
				: class$com$maddox$il2$objects$ships$Ship$IICU57);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$IICU57P == null ? (class$com$maddox$il2$objects$ships$Ship$IICU57P = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IICU57P"))
				: class$com$maddox$il2$objects$ships$Ship$IICU57P);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSBesugo_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$USSBesugo_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSBesugo_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$USSBesugo_Sub);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSNautilus_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$USSNautilus_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSNautilus_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$USSNautilus_Sub);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSPermit_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$USSPermit_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSPermit_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$USSPermit_Sub);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSSpot_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$USSSpot_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSSpot_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$USSSpot_Sub);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICRO500_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$VIICRO500_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICRO500_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$VIICRO500_Sub);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICswordfish_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$VIICswordfish_Sub = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICswordfish_Sub")))
						: class$com$maddox$il2$objects$ships$Ship$VIICswordfish_Sub);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICU124_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$VIICU124_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICU124_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$VIICU124_Sub);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICU564_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$VIICU564_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICU564_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$VIICU564_Sub);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$VIICwinter_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$VIICwinter_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$VIICwinter_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$VIICwinter_Sub);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNRyujoCV == null ? (class$com$maddox$il2$objects$ships$Ship$IJNRyujoCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNRyujoCV"))
						: class$com$maddox$il2$objects$ships$Ship$IJNRyujoCV);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSValleyForgeCV45 == null ? (class$com$maddox$il2$objects$ships$Ship$USSValleyForgeCV45 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSValleyForgeCV45")))
						: class$com$maddox$il2$objects$ships$Ship$USSValleyForgeCV45);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSBoxerCV21 == null ? (class$com$maddox$il2$objects$ships$Ship$USSBoxerCV21 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSBoxerCV21"))
						: class$com$maddox$il2$objects$ships$Ship$USSBoxerCV21);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSHoustonCA30 == null ? (class$com$maddox$il2$objects$ships$Ship$USSHoustonCA30 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSHoustonCA30"))
						: class$com$maddox$il2$objects$ships$Ship$USSHoustonCA30);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSChesterCA27 == null ? (class$com$maddox$il2$objects$ships$Ship$USSChesterCA27 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSChesterCA27"))
						: class$com$maddox$il2$objects$ships$Ship$USSChesterCA27);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSPortlandCA33 == null ? (class$com$maddox$il2$objects$ships$Ship$USSPortlandCA33 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSPortlandCA33"))
						: class$com$maddox$il2$objects$ships$Ship$USSPortlandCA33);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$GrafZeppelin == null ? (class$com$maddox$il2$objects$ships$Ship$GrafZeppelin = class$ZutiShip("com.maddox.il2.objects.ships.Ship$GrafZeppelin"))
						: class$com$maddox$il2$objects$ships$Ship$GrafZeppelin);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$PeterStrasser == null ? (class$com$maddox$il2$objects$ships$Ship$PeterStrasser = class$ZutiShip("com.maddox.il2.objects.ships.Ship$PeterStrasser"))
						: class$com$maddox$il2$objects$ships$Ship$PeterStrasser);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Aquila == null ? (class$com$maddox$il2$objects$ships$Ship$Aquila = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Aquila"))
				: class$com$maddox$il2$objects$ships$Ship$Aquila);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LCM_1 == null ? (class$com$maddox$il2$objects$ships$Ship$LCM_1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LCM_1"))
				: class$com$maddox$il2$objects$ships$Ship$LCM_1);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LCM_2 == null ? (class$com$maddox$il2$objects$ships$Ship$LCM_2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LCM_2"))
				: class$com$maddox$il2$objects$ships$Ship$LCM_2);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LCM_3 == null ? (class$com$maddox$il2$objects$ships$Ship$LCM_3 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LCM_3"))
				: class$com$maddox$il2$objects$ships$Ship$LCM_3);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LCVP_1 == null ? (class$com$maddox$il2$objects$ships$Ship$LCVP_1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LCVP_1"))
				: class$com$maddox$il2$objects$ships$Ship$LCVP_1);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LCVP_2 == null ? (class$com$maddox$il2$objects$ships$Ship$LCVP_2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LCVP_2"))
				: class$com$maddox$il2$objects$ships$Ship$LCVP_2);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LST_1 == null ? (class$com$maddox$il2$objects$ships$Ship$LST_1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LST_1"))
				: class$com$maddox$il2$objects$ships$Ship$LST_1);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LST_3 == null ? (class$com$maddox$il2$objects$ships$Ship$LST_3 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LST_3"))
				: class$com$maddox$il2$objects$ships$Ship$LST_3);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LSD_1 == null ? (class$com$maddox$il2$objects$ships$Ship$LSD_1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LSD_1"))
				: class$com$maddox$il2$objects$ships$Ship$LSD_1);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Hospital_2 == null ? (class$com$maddox$il2$objects$ships$Ship$Hospital_2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Hospital_2"))
						: class$com$maddox$il2$objects$ships$Ship$Hospital_2);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Icebreaker_1 == null ? (class$com$maddox$il2$objects$ships$Ship$Icebreaker_1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Icebreaker_1"))
						: class$com$maddox$il2$objects$ships$Ship$Icebreaker_1);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Sub_Coldwar == null ? (class$com$maddox$il2$objects$ships$Ship$Sub_Coldwar = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Sub_Coldwar"))
						: class$com$maddox$il2$objects$ships$Ship$Sub_Coldwar);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSCossack == null ? (class$com$maddox$il2$objects$ships$Ship$HMSCossack = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSCossack"))
						: class$com$maddox$il2$objects$ships$Ship$HMSCossack);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSJupiter == null ? (class$com$maddox$il2$objects$ships$Ship$HMSJupiter = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSJupiter"))
						: class$com$maddox$il2$objects$ships$Ship$HMSJupiter);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSJavelin == null ? (class$com$maddox$il2$objects$ships$Ship$HMSJavelin = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSJavelin"))
						: class$com$maddox$il2$objects$ships$Ship$HMSJavelin);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMASNapier == null ? (class$com$maddox$il2$objects$ships$Ship$HMASNapier = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMASNapier"))
						: class$com$maddox$il2$objects$ships$Ship$HMASNapier);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSKipling == null ? (class$com$maddox$il2$objects$ships$Ship$HMSKipling = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSKipling"))
						: class$com$maddox$il2$objects$ships$Ship$HMSKipling);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSMatabele == null ? (class$com$maddox$il2$objects$ships$Ship$HMSMatabele = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSMatabele"))
						: class$com$maddox$il2$objects$ships$Ship$HMSMatabele);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSNubian == null ? (class$com$maddox$il2$objects$ships$Ship$HMSNubian = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSNubian"))
						: class$com$maddox$il2$objects$ships$Ship$HMSNubian);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSTartar == null ? (class$com$maddox$il2$objects$ships$Ship$HMSTartar = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSTartar"))
						: class$com$maddox$il2$objects$ships$Ship$HMSTartar);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSWarspite == null ? (class$com$maddox$il2$objects$ships$Ship$HMSWarspite = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSWarspite"))
						: class$com$maddox$il2$objects$ships$Ship$HMSWarspite);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$HMSFiji == null ? (class$com$maddox$il2$objects$ships$Ship$HMSFiji = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSFiji"))
				: class$com$maddox$il2$objects$ships$Ship$HMSFiji);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSBunkerHillCV17_1944 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSBunkerHillCV17_1944 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSBunkerHillCV17_1944")))
						: class$com$maddox$il2$objects$ships$Ship$USSBunkerHillCV17_1944);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSFranklinCV13_1943 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSFranklinCV13_1943 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSFranklinCV13_1943")))
						: class$com$maddox$il2$objects$ships$Ship$USSFranklinCV13_1943);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSFranklinCV13_1944 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSFranklinCV13_1944 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSFranklinCV13_1944")))
						: class$com$maddox$il2$objects$ships$Ship$USSFranklinCV13_1944);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSGenericCV9ClassMS21 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSGenericCV9ClassMS21 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSGenericCV9ClassMS21")))
						: class$com$maddox$il2$objects$ships$Ship$USSGenericCV9ClassMS21);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSHancockCV19_1944 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSHancockCV19_1944 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSHancockCV19_1944")))
						: class$com$maddox$il2$objects$ships$Ship$USSHancockCV19_1944);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSHornetCV12_1944 == null ? (class$com$maddox$il2$objects$ships$Ship$USSHornetCV12_1944 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSHornetCV12_1944")))
						: class$com$maddox$il2$objects$ships$Ship$USSHornetCV12_1944);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSIntrepidCV11_1944 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSIntrepidCV11_1944 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSIntrepidCV11_1944")))
						: class$com$maddox$il2$objects$ships$Ship$USSIntrepidCV11_1944);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSLexingtonCV16 == null ? (class$com$maddox$il2$objects$ships$Ship$USSLexingtonCV16 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSLexingtonCV16"))
						: class$com$maddox$il2$objects$ships$Ship$USSLexingtonCV16);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSTiconderogaCV14_1944 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSTiconderogaCV14_1944 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSTiconderogaCV14_1944")))
						: class$com$maddox$il2$objects$ships$Ship$USSTiconderogaCV14_1944);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSWaspCV18_1944 == null ? (class$com$maddox$il2$objects$ships$Ship$USSWaspCV18_1944 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSWaspCV18_1944"))
						: class$com$maddox$il2$objects$ships$Ship$USSWaspCV18_1944);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSYorktownCV10_1944 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSYorktownCV10_1944 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSYorktownCV10_1944")))
						: class$com$maddox$il2$objects$ships$Ship$USSYorktownCV10_1944);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSPrincetonCVL23 == null ? (class$com$maddox$il2$objects$ships$Ship$USSPrincetonCVL23 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSPrincetonCVL23")))
						: class$com$maddox$il2$objects$ships$Ship$USSPrincetonCVL23);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSBelleauWoodCVL24 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSBelleauWoodCVL24 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSBelleauWoodCVL24")))
						: class$com$maddox$il2$objects$ships$Ship$USSBelleauWoodCVL24);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSSanJacintoCVL30 == null ? (class$com$maddox$il2$objects$ships$Ship$USSSanJacintoCVL30 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSSanJacintoCVL30")))
						: class$com$maddox$il2$objects$ships$Ship$USSSanJacintoCVL30);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSMcKean == null ? (class$com$maddox$il2$objects$ships$Ship$USSMcKean = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSMcKean"))
						: class$com$maddox$il2$objects$ships$Ship$USSMcKean);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Littorio == null ? (class$com$maddox$il2$objects$ships$Ship$Littorio = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Littorio"))
				: class$com$maddox$il2$objects$ships$Ship$Littorio);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Soldati == null ? (class$com$maddox$il2$objects$ships$Ship$Soldati = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Soldati"))
				: class$com$maddox$il2$objects$ships$Ship$Soldati);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Italia0 == null ? (class$com$maddox$il2$objects$ships$Ship$Italia0 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Italia0"))
				: class$com$maddox$il2$objects$ships$Ship$Italia0);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Italia1 == null ? (class$com$maddox$il2$objects$ships$Ship$Italia1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Italia1"))
				: class$com$maddox$il2$objects$ships$Ship$Italia1);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNHiryuCV == null ? (class$com$maddox$il2$objects$ships$Ship$IJNHiryuCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNHiryuCV"))
						: class$com$maddox$il2$objects$ships$Ship$IJNHiryuCV);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNKagaCV == null ? (class$com$maddox$il2$objects$ships$Ship$IJNKagaCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNKagaCV"))
						: class$com$maddox$il2$objects$ships$Ship$IJNKagaCV);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNSoryuCV == null ? (class$com$maddox$il2$objects$ships$Ship$IJNSoryuCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNSoryuCV"))
						: class$com$maddox$il2$objects$ships$Ship$IJNSoryuCV);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNCVLGeneric == null ? (class$com$maddox$il2$objects$ships$Ship$IJNCVLGeneric = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNCVLGeneric"))
						: class$com$maddox$il2$objects$ships$Ship$IJNCVLGeneric);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$TroopTrans0 == null ? (class$com$maddox$il2$objects$ships$Ship$TroopTrans0 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$TroopTrans0"))
						: class$com$maddox$il2$objects$ships$Ship$TroopTrans0);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$TroopTrans1 == null ? (class$com$maddox$il2$objects$ships$Ship$TroopTrans1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$TroopTrans1"))
						: class$com$maddox$il2$objects$ships$Ship$TroopTrans1);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$TroopTrans2 == null ? (class$com$maddox$il2$objects$ships$Ship$TroopTrans2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$TroopTrans2"))
						: class$com$maddox$il2$objects$ships$Ship$TroopTrans2);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$TroopTrans3 == null ? (class$com$maddox$il2$objects$ships$Ship$TroopTrans3 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$TroopTrans3"))
						: class$com$maddox$il2$objects$ships$Ship$TroopTrans3);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HospitalShip == null ? (class$com$maddox$il2$objects$ships$Ship$HospitalShip = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HospitalShip"))
						: class$com$maddox$il2$objects$ships$Ship$HospitalShip);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Transport1 == null ? (class$com$maddox$il2$objects$ships$Ship$Transport1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Transport1"))
						: class$com$maddox$il2$objects$ships$Ship$Transport1);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Transport2 == null ? (class$com$maddox$il2$objects$ships$Ship$Transport2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Transport2"))
						: class$com$maddox$il2$objects$ships$Ship$Transport2);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Transport3 == null ? (class$com$maddox$il2$objects$ships$Ship$Transport3 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Transport3"))
						: class$com$maddox$il2$objects$ships$Ship$Transport3);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Transport4 == null ? (class$com$maddox$il2$objects$ships$Ship$Transport4 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Transport4"))
						: class$com$maddox$il2$objects$ships$Ship$Transport4);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Transport5 == null ? (class$com$maddox$il2$objects$ships$Ship$Transport5 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Transport5"))
						: class$com$maddox$il2$objects$ships$Ship$Transport5);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Transport6 == null ? (class$com$maddox$il2$objects$ships$Ship$Transport6 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Transport6"))
						: class$com$maddox$il2$objects$ships$Ship$Transport6);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Transport7 == null ? (class$com$maddox$il2$objects$ships$Ship$Transport7 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Transport7"))
						: class$com$maddox$il2$objects$ships$Ship$Transport7);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$TransDmg == null ? (class$com$maddox$il2$objects$ships$Ship$TransDmg = class$ZutiShip("com.maddox.il2.objects.ships.Ship$TransDmg"))
				: class$com$maddox$il2$objects$ships$Ship$TransDmg);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$TransWreck == null ? (class$com$maddox$il2$objects$ships$Ship$TransWreck = class$ZutiShip("com.maddox.il2.objects.ships.Ship$TransWreck"))
						: class$com$maddox$il2$objects$ships$Ship$TransWreck);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Tanker0 == null ? (class$com$maddox$il2$objects$ships$Ship$Tanker0 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Tanker0"))
				: class$com$maddox$il2$objects$ships$Ship$Tanker0);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Tanker1 == null ? (class$com$maddox$il2$objects$ships$Ship$Tanker1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Tanker1"))
				: class$com$maddox$il2$objects$ships$Ship$Tanker1);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Tanker2 == null ? (class$com$maddox$il2$objects$ships$Ship$Tanker2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Tanker2"))
				: class$com$maddox$il2$objects$ships$Ship$Tanker2);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$TankerDmg == null ? (class$com$maddox$il2$objects$ships$Ship$TankerDmg = class$ZutiShip("com.maddox.il2.objects.ships.Ship$TankerDmg"))
						: class$com$maddox$il2$objects$ships$Ship$TankerDmg);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MFPT == null ? (class$com$maddox$il2$objects$ships$Ship$MFPT = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MFPT"))
				: class$com$maddox$il2$objects$ships$Ship$MFPT);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MFPIT == null ? (class$com$maddox$il2$objects$ships$Ship$MFPIT = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MFPIT"))
				: class$com$maddox$il2$objects$ships$Ship$MFPIT);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MAS501JP == null ? (class$com$maddox$il2$objects$ships$Ship$MAS501JP = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MAS501JP"))
				: class$com$maddox$il2$objects$ships$Ship$MAS501JP);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MAS501UNE == null ? (class$com$maddox$il2$objects$ships$Ship$MAS501UNE = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MAS501UNE"))
				: class$com$maddox$il2$objects$ships$Ship$MAS501UNE);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MAS501UNP == null ? (class$com$maddox$il2$objects$ships$Ship$MAS501UNP = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MAS501UNP"))
				: class$com$maddox$il2$objects$ships$Ship$MAS501UNP);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MAS501RN == null ? (class$com$maddox$il2$objects$ships$Ship$MAS501RN = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MAS501RN"))
				: class$com$maddox$il2$objects$ships$Ship$MAS501RN);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$DLCWreck == null ? (class$com$maddox$il2$objects$ships$Ship$DLCWreck = class$ZutiShip("com.maddox.il2.objects.ships.Ship$DLCWreck"))
				: class$com$maddox$il2$objects$ships$Ship$DLCWreck);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LCVPWreck == null ? (class$com$maddox$il2$objects$ships$Ship$LCVPWreck = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LCVPWreck"))
				: class$com$maddox$il2$objects$ships$Ship$LCVPWreck);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$SmallWreck == null ? (class$com$maddox$il2$objects$ships$Ship$SmallWreck = class$ZutiShip("com.maddox.il2.objects.ships.Ship$SmallWreck"))
						: class$com$maddox$il2$objects$ships$Ship$SmallWreck);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Fisherman == null ? (class$com$maddox$il2$objects$ships$Ship$Fisherman = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Fisherman"))
				: class$com$maddox$il2$objects$ships$Ship$Fisherman);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Fisherman1 == null ? (class$com$maddox$il2$objects$ships$Ship$Fisherman1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Fisherman1"))
						: class$com$maddox$il2$objects$ships$Ship$Fisherman1);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Fisherman2 == null ? (class$com$maddox$il2$objects$ships$Ship$Fisherman2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Fisherman2"))
						: class$com$maddox$il2$objects$ships$Ship$Fisherman2);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Barge0 == null ? (class$com$maddox$il2$objects$ships$Ship$Barge0 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Barge0"))
				: class$com$maddox$il2$objects$ships$Ship$Barge0);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Barge1 == null ? (class$com$maddox$il2$objects$ships$Ship$Barge1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Barge1"))
				: class$com$maddox$il2$objects$ships$Ship$Barge1);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Boat == null ? (class$com$maddox$il2$objects$ships$Ship$Boat = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Boat"))
				: class$com$maddox$il2$objects$ships$Ship$Boat);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Boat1 == null ? (class$com$maddox$il2$objects$ships$Ship$Boat1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Boat1"))
				: class$com$maddox$il2$objects$ships$Ship$Boat1);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Destroyer0 == null ? (class$com$maddox$il2$objects$ships$Ship$Destroyer0 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Destroyer0"))
						: class$com$maddox$il2$objects$ships$Ship$Destroyer0);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Destroyer1 == null ? (class$com$maddox$il2$objects$ships$Ship$Destroyer1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Destroyer1"))
						: class$com$maddox$il2$objects$ships$Ship$Destroyer1);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Destroyer2 == null ? (class$com$maddox$il2$objects$ships$Ship$Destroyer2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Destroyer2"))
						: class$com$maddox$il2$objects$ships$Ship$Destroyer2);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$DestroyerRN == null ? (class$com$maddox$il2$objects$ships$Ship$DestroyerRN = class$ZutiShip("com.maddox.il2.objects.ships.Ship$DestroyerRN"))
						: class$com$maddox$il2$objects$ships$Ship$DestroyerRN);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$DestroyerKM == null ? (class$com$maddox$il2$objects$ships$Ship$DestroyerKM = class$ZutiShip("com.maddox.il2.objects.ships.Ship$DestroyerKM"))
						: class$com$maddox$il2$objects$ships$Ship$DestroyerKM);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$DestroyerDmg == null ? (class$com$maddox$il2$objects$ships$Ship$DestroyerDmg = class$ZutiShip("com.maddox.il2.objects.ships.Ship$DestroyerDmg"))
						: class$com$maddox$il2$objects$ships$Ship$DestroyerDmg);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$DestroyerWreck == null ? (class$com$maddox$il2$objects$ships$Ship$DestroyerWreck = class$ZutiShip("com.maddox.il2.objects.ships.Ship$DestroyerWreck"))
						: class$com$maddox$il2$objects$ships$Ship$DestroyerWreck);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Carrier1 == null ? (class$com$maddox$il2$objects$ships$Ship$Carrier1 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Carrier1"))
				: class$com$maddox$il2$objects$ships$Ship$Carrier1);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSFormidableCV == null ? (class$com$maddox$il2$objects$ships$Ship$HMSFormidableCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSFormidableCV"))
						: class$com$maddox$il2$objects$ships$Ship$HMSFormidableCV);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSIndomitableCV == null ? (class$com$maddox$il2$objects$ships$Ship$HMSIndomitableCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSIndomitableCV"))
						: class$com$maddox$il2$objects$ships$Ship$HMSIndomitableCV);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$G5 == null ? (class$com$maddox$il2$objects$ships$Ship$G5 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$G5"))
				: class$com$maddox$il2$objects$ships$Ship$G5);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MO4 == null ? (class$com$maddox$il2$objects$ships$Ship$MO4 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MO4"))
				: class$com$maddox$il2$objects$ships$Ship$MO4);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$BBK_1942 == null ? (class$com$maddox$il2$objects$ships$Ship$BBK_1942 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$BBK_1942"))
				: class$com$maddox$il2$objects$ships$Ship$BBK_1942);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$BBK1124_1943 == null ? (class$com$maddox$il2$objects$ships$Ship$BBK1124_1943 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$BBK1124_1943"))
						: class$com$maddox$il2$objects$ships$Ship$BBK1124_1943);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$Destroyer_USSR_Type7 == null) ? (class$com$maddox$il2$objects$ships$Ship$Destroyer_USSR_Type7 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$Destroyer_USSR_Type7")))
						: class$com$maddox$il2$objects$ships$Ship$Destroyer_USSR_Type7);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$Destroyer_USSR_Type7_44 == null) ? (class$com$maddox$il2$objects$ships$Ship$Destroyer_USSR_Type7_44 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$Destroyer_USSR_Type7_44")))
						: class$com$maddox$il2$objects$ships$Ship$Destroyer_USSR_Type7_44);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Tral == null ? (class$com$maddox$il2$objects$ships$Ship$Tral = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Tral"))
				: class$com$maddox$il2$objects$ships$Ship$Tral);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Shuka == null ? (class$com$maddox$il2$objects$ships$Ship$Shuka = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Shuka"))
				: class$com$maddox$il2$objects$ships$Ship$Shuka);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$ShukaP == null ? (class$com$maddox$il2$objects$ships$Ship$ShukaP = class$ZutiShip("com.maddox.il2.objects.ships.Ship$ShukaP"))
				: class$com$maddox$il2$objects$ships$Ship$ShukaP);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Aurora == null ? (class$com$maddox$il2$objects$ships$Ship$Aurora = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Aurora"))
				: class$com$maddox$il2$objects$ships$Ship$Aurora);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Marat == null ? (class$com$maddox$il2$objects$ships$Ship$Marat = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Marat"))
				: class$com$maddox$il2$objects$ships$Ship$Marat);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Kirov == null ? (class$com$maddox$il2$objects$ships$Ship$Kirov = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Kirov"))
				: class$com$maddox$il2$objects$ships$Ship$Kirov);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Tashkent == null ? (class$com$maddox$il2$objects$ships$Ship$Tashkent = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Tashkent"))
				: class$com$maddox$il2$objects$ships$Ship$Tashkent);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Tramp == null ? (class$com$maddox$il2$objects$ships$Ship$Tramp = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Tramp"))
				: class$com$maddox$il2$objects$ships$Ship$Tramp);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Tanker == null ? (class$com$maddox$il2$objects$ships$Ship$Tanker = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Tanker"))
				: class$com$maddox$il2$objects$ships$Ship$Tanker);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSLexingtonCV2 == null ? (class$com$maddox$il2$objects$ships$Ship$USSLexingtonCV2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSLexingtonCV2"))
						: class$com$maddox$il2$objects$ships$Ship$USSLexingtonCV2);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSSaratogaCV3 == null ? (class$com$maddox$il2$objects$ships$Ship$USSSaratogaCV3 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSSaratogaCV3"))
						: class$com$maddox$il2$objects$ships$Ship$USSSaratogaCV3);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSCVGeneric == null ? (class$com$maddox$il2$objects$ships$Ship$USSCVGeneric = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSCVGeneric"))
						: class$com$maddox$il2$objects$ships$Ship$USSCVGeneric);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSBBGeneric == null ? (class$com$maddox$il2$objects$ships$Ship$USSBBGeneric = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSBBGeneric"))
						: class$com$maddox$il2$objects$ships$Ship$USSBBGeneric);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSIndianapolisCA35 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSIndianapolisCA35 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSIndianapolisCA35")))
						: class$com$maddox$il2$objects$ships$Ship$USSIndianapolisCA35);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LVT_2WAT == null ? (class$com$maddox$il2$objects$ships$Ship$LVT_2WAT = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LVT_2WAT"))
				: class$com$maddox$il2$objects$ships$Ship$LVT_2WAT);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$DUKW_WAT == null ? (class$com$maddox$il2$objects$ships$Ship$DUKW_WAT = class$ZutiShip("com.maddox.il2.objects.ships.Ship$DUKW_WAT"))
				: class$com$maddox$il2$objects$ships$Ship$DUKW_WAT);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$LCVP == null ? (class$com$maddox$il2$objects$ships$Ship$LCVP = class$ZutiShip("com.maddox.il2.objects.ships.Ship$LCVP"))
				: class$com$maddox$il2$objects$ships$Ship$LCVP);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSGreenlingSS213_Srf == null) ? (class$com$maddox$il2$objects$ships$Ship$USSGreenlingSS213_Srf = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSGreenlingSS213_Srf")))
						: class$com$maddox$il2$objects$ships$Ship$USSGreenlingSS213_Srf);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSGreenlingSS213_Sub == null) ? (class$com$maddox$il2$objects$ships$Ship$USSGreenlingSS213_Sub = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSGreenlingSS213_Sub")))
						: class$com$maddox$il2$objects$ships$Ship$USSGreenlingSS213_Sub);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSGatoSS212_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$USSGatoSS212_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSGatoSS212_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$USSGatoSS212_Srf);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSGatoSS212_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$USSGatoSS212_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSGatoSS212_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$USSGatoSS212_Sub);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSWardDD139 == null ? (class$com$maddox$il2$objects$ships$Ship$USSWardDD139 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSWardDD139"))
						: class$com$maddox$il2$objects$ships$Ship$USSWardDD139);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSDentDD116 == null ? (class$com$maddox$il2$objects$ships$Ship$USSDentDD116 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSDentDD116"))
						: class$com$maddox$il2$objects$ships$Ship$USSDentDD116);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSFletcherDD445 == null ? (class$com$maddox$il2$objects$ships$Ship$USSFletcherDD445 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSFletcherDD445"))
						: class$com$maddox$il2$objects$ships$Ship$USSFletcherDD445);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSOBannonDD450 == null ? (class$com$maddox$il2$objects$ships$Ship$USSOBannonDD450 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSOBannonDD450"))
						: class$com$maddox$il2$objects$ships$Ship$USSOBannonDD450);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSKiddDD661 == null ? (class$com$maddox$il2$objects$ships$Ship$USSKiddDD661 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSKiddDD661"))
						: class$com$maddox$il2$objects$ships$Ship$USSKiddDD661);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSCasablancaCVE55 == null ? (class$com$maddox$il2$objects$ships$Ship$USSCasablancaCVE55 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSCasablancaCVE55")))
						: class$com$maddox$il2$objects$ships$Ship$USSCasablancaCVE55);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSKitkunBayCVE71 == null ? (class$com$maddox$il2$objects$ships$Ship$USSKitkunBayCVE71 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSKitkunBayCVE71")))
						: class$com$maddox$il2$objects$ships$Ship$USSKitkunBayCVE71);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$USSShamrockBayCVE84 == null) ? (class$com$maddox$il2$objects$ships$Ship$USSShamrockBayCVE84 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSShamrockBayCVE84")))
						: class$com$maddox$il2$objects$ships$Ship$USSShamrockBayCVE84);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSEssexCV9 == null ? (class$com$maddox$il2$objects$ships$Ship$USSEssexCV9 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSEssexCV9"))
						: class$com$maddox$il2$objects$ships$Ship$USSEssexCV9);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$USSIntrepidCV11 == null ? (class$com$maddox$il2$objects$ships$Ship$USSIntrepidCV11 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$USSIntrepidCV11"))
						: class$com$maddox$il2$objects$ships$Ship$USSIntrepidCV11);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$PilotWater_US == null ? (class$com$maddox$il2$objects$ships$Ship$PilotWater_US = class$ZutiShip("com.maddox.il2.objects.ships.Ship$PilotWater_US"))
						: class$com$maddox$il2$objects$ships$Ship$PilotWater_US);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$PilotBoatWater_US == null ? (class$com$maddox$il2$objects$ships$Ship$PilotBoatWater_US = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$PilotBoatWater_US")))
						: class$com$maddox$il2$objects$ships$Ship$PilotBoatWater_US);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSIllustriousCV == null ? (class$com$maddox$il2$objects$ships$Ship$HMSIllustriousCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSIllustriousCV"))
						: class$com$maddox$il2$objects$ships$Ship$HMSIllustriousCV);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$HMSPoWBB == null ? (class$com$maddox$il2$objects$ships$Ship$HMSPoWBB = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSPoWBB"))
				: class$com$maddox$il2$objects$ships$Ship$HMSPoWBB);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSKingGeorgeVBB == null ? (class$com$maddox$il2$objects$ships$Ship$HMSKingGeorgeVBB = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSKingGeorgeVBB"))
						: class$com$maddox$il2$objects$ships$Ship$HMSKingGeorgeVBB);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$HMSDukeOfYorkBB == null ? (class$com$maddox$il2$objects$ships$Ship$HMSDukeOfYorkBB = class$ZutiShip("com.maddox.il2.objects.ships.Ship$HMSDukeOfYorkBB"))
						: class$com$maddox$il2$objects$ships$Ship$HMSDukeOfYorkBB);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$S80 == null ? (class$com$maddox$il2$objects$ships$Ship$S80 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$S80"))
				: class$com$maddox$il2$objects$ships$Ship$S80);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MFP == null ? (class$com$maddox$il2$objects$ships$Ship$MFP = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MFP"))
				: class$com$maddox$il2$objects$ships$Ship$MFP);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MFP2 == null ? (class$com$maddox$il2$objects$ships$Ship$MFP2 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MFP2"))
				: class$com$maddox$il2$objects$ships$Ship$MFP2);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MAS501 == null ? (class$com$maddox$il2$objects$ships$Ship$MAS501 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MAS501"))
				: class$com$maddox$il2$objects$ships$Ship$MAS501);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Murgesku == null ? (class$com$maddox$il2$objects$ships$Ship$Murgesku = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Murgesku"))
				: class$com$maddox$il2$objects$ships$Ship$Murgesku);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$MBoat == null ? (class$com$maddox$il2$objects$ships$Ship$MBoat = class$ZutiShip("com.maddox.il2.objects.ships.Ship$MBoat"))
				: class$com$maddox$il2$objects$ships$Ship$MBoat);
		new ShipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Submarine == null ? (class$com$maddox$il2$objects$ships$Ship$Submarine = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Submarine"))
				: class$com$maddox$il2$objects$ships$Ship$Submarine);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$SubmarineP == null ? (class$com$maddox$il2$objects$ships$Ship$SubmarineP = class$ZutiShip("com.maddox.il2.objects.ships.Ship$SubmarineP"))
						: class$com$maddox$il2$objects$ships$Ship$SubmarineP);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Niobe == null ? (class$com$maddox$il2$objects$ships$Ship$Niobe = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Niobe"))
				: class$com$maddox$il2$objects$ships$Ship$Niobe);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$NiobeWithBeacon == null ? (class$com$maddox$il2$objects$ships$Ship$NiobeWithBeacon = class$ZutiShip("com.maddox.il2.objects.ships.Ship$NiobeWithBeacon"))
						: class$com$maddox$il2$objects$ships$Ship$NiobeWithBeacon);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_Srf == null ? (class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_Srf = class$ZutiShip("com.maddox.il2.objects.ships.Ship$SubTypeVIIC_Srf"))
						: class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_Srf);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_SrfWithBeacon == null) ? (class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_SrfWithBeacon = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$SubTypeVIIC_SrfWithBeacon")))
						: class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_SrfWithBeacon);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_Sub == null ? (class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_Sub = class$ZutiShip("com.maddox.il2.objects.ships.Ship$SubTypeVIIC_Sub"))
						: class$com$maddox$il2$objects$ships$Ship$SubTypeVIIC_Sub);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Illmarinen == null ? (class$com$maddox$il2$objects$ships$Ship$Illmarinen = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Illmarinen"))
						: class$com$maddox$il2$objects$ships$Ship$Illmarinen);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$Vainamoinen == null ? (class$com$maddox$il2$objects$ships$Ship$Vainamoinen = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Vainamoinen"))
						: class$com$maddox$il2$objects$ships$Ship$Vainamoinen);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$Tirpitz == null ? (class$com$maddox$il2$objects$ships$Ship$Tirpitz = class$ZutiShip("com.maddox.il2.objects.ships.Ship$Tirpitz"))
				: class$com$maddox$il2$objects$ships$Ship$Tirpitz);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$PAM == null ? (class$com$maddox$il2$objects$ships$Ship$PAM = class$ZutiShip("com.maddox.il2.objects.ships.Ship$PAM"))
				: class$com$maddox$il2$objects$ships$Ship$PAM);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNAkagiCV == null ? (class$com$maddox$il2$objects$ships$Ship$IJNAkagiCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNAkagiCV"))
						: class$com$maddox$il2$objects$ships$Ship$IJNAkagiCV);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNShokakuCV == null ? (class$com$maddox$il2$objects$ships$Ship$IJNShokakuCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNShokakuCV"))
						: class$com$maddox$il2$objects$ships$Ship$IJNShokakuCV);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNZuikakuCV == null ? (class$com$maddox$il2$objects$ships$Ship$IJNZuikakuCV = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNZuikakuCV"))
						: class$com$maddox$il2$objects$ships$Ship$IJNZuikakuCV);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNCVGeneric == null ? (class$com$maddox$il2$objects$ships$Ship$IJNCVGeneric = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNCVGeneric"))
						: class$com$maddox$il2$objects$ships$Ship$IJNCVGeneric);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNBBGeneric == null ? (class$com$maddox$il2$objects$ships$Ship$IJNBBGeneric = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNBBGeneric"))
						: class$com$maddox$il2$objects$ships$Ship$IJNBBGeneric);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNAkizukiDD42 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNAkizukiDD42 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNAkizukiDD42"))
						: class$com$maddox$il2$objects$ships$Ship$IJNAkizukiDD42);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD41 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD41 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNAmatsukazeDD41")))
						: class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD41);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNArashiDD41 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNArashiDD41 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNArashiDD41"))
						: class$com$maddox$il2$objects$ships$Ship$IJNArashiDD41);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNKageroDD41 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNKageroDD41 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNKageroDD41"))
						: class$com$maddox$il2$objects$ships$Ship$IJNKageroDD41);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNNowakiDD41 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNNowakiDD41 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNNowakiDD41"))
						: class$com$maddox$il2$objects$ships$Ship$IJNNowakiDD41);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD41 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD41 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNYukikazeDD41"))
						: class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD41);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD43 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD43 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNAmatsukazeDD43")))
						: class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD43);
		new BigshipGeneric.SPAWN(
				(class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD43WithBeacon == null) ? (class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD43WithBeacon = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNAmatsukazeDD43WithBeacon")))
						: class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD43WithBeacon);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNNowakiDD43 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNNowakiDD43 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNNowakiDD43"))
						: class$com$maddox$il2$objects$ships$Ship$IJNNowakiDD43);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD43 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD43 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNYukikazeDD43"))
						: class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD43);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD45 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD45 = (class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNAmatsukazeDD45")))
						: class$com$maddox$il2$objects$ships$Ship$IJNAmatsukazeDD45);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD45 == null ? (class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD45 = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNYukikazeDD45"))
						: class$com$maddox$il2$objects$ships$Ship$IJNYukikazeDD45);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNFishJunk == null ? (class$com$maddox$il2$objects$ships$Ship$IJNFishJunk = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNFishJunk"))
						: class$com$maddox$il2$objects$ships$Ship$IJNFishJunk);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$IJNFishJunkA == null ? (class$com$maddox$il2$objects$ships$Ship$IJNFishJunkA = class$ZutiShip("com.maddox.il2.objects.ships.Ship$IJNFishJunkA"))
						: class$com$maddox$il2$objects$ships$Ship$IJNFishJunkA);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$DaihatsuLC == null ? (class$com$maddox$il2$objects$ships$Ship$DaihatsuLC = class$ZutiShip("com.maddox.il2.objects.ships.Ship$DaihatsuLC"))
						: class$com$maddox$il2$objects$ships$Ship$DaihatsuLC);
		new ShipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$PilotWater_JA == null ? (class$com$maddox$il2$objects$ships$Ship$PilotWater_JA = class$ZutiShip("com.maddox.il2.objects.ships.Ship$PilotWater_JA"))
						: class$com$maddox$il2$objects$ships$Ship$PilotWater_JA);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$RwyTranspWide == null ? (class$com$maddox$il2$objects$ships$Ship$RwyTranspWide = class$ZutiShip("com.maddox.il2.objects.ships.Ship$RwyTranspWide"))
						: class$com$maddox$il2$objects$ships$Ship$RwyTranspWide);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$RwyTranspSqr == null ? (class$com$maddox$il2$objects$ships$Ship$RwyTranspSqr = class$ZutiShip("com.maddox.il2.objects.ships.Ship$RwyTranspSqr"))
						: class$com$maddox$il2$objects$ships$Ship$RwyTranspSqr);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$RwyCon == null ? (class$com$maddox$il2$objects$ships$Ship$RwyCon = class$ZutiShip("com.maddox.il2.objects.ships.Ship$RwyCon"))
				: class$com$maddox$il2$objects$ships$Ship$RwyCon);
		new BigshipGeneric.SPAWN(class$com$maddox$il2$objects$ships$Ship$RwySteel == null ? (class$com$maddox$il2$objects$ships$Ship$RwySteel = class$ZutiShip("com.maddox.il2.objects.ships.Ship$RwySteel"))
				: class$com$maddox$il2$objects$ships$Ship$RwySteel);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$RwySteelLow == null ? (class$com$maddox$il2$objects$ships$Ship$RwySteelLow = class$ZutiShip("com.maddox.il2.objects.ships.Ship$RwySteelLow"))
						: class$com$maddox$il2$objects$ships$Ship$RwySteelLow);
		new BigshipGeneric.SPAWN(
				class$com$maddox$il2$objects$ships$Ship$RwyTransp == null ? (class$com$maddox$il2$objects$ships$Ship$RwyTransp = class$ZutiShip("com.maddox.il2.objects.ships.Ship$RwyTransp"))
						: class$com$maddox$il2$objects$ships$Ship$RwyTransp);
	}
}
