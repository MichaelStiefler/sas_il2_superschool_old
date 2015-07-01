//*****************************************************************
// IL-2 Class Hasher
// Copyright (C) 2012 SAS~Storebror
//
// This file is part of the IL-2 Class Hasher.
//
// IL-2 Class Hasher is free software.
// It is distributed under the DWTFYWTWIPL license:
//
// DO WHAT THE FUCK YOU WANT TO WITH IT PUBLIC LICENSE
// Version 1, March 2012
//
// Copyright (C) 2012 SAS~Storebror <Storebror@gmx.de>
//
// Everyone is permitted to copy and distribute verbatim or modified
// copies of this license document, and changing it is allowed as long
// as the name is changed.
//
// DO WHAT THE FUCK YOU WANT TO WITH IT PUBLIC LICENSE
// TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
//
// 0. You just DO WHAT THE FUCK YOU WANT TO WITH IT.
//
//*****************************************************************

//*************************************************************************
// Includes
//*************************************************************************
#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include "ClassHasher.h"
#include <tchar.h>

#include <windows.h>
#include <string>
#include <vector>
#include <stack>
#include <iostream>
#include <conio.h>

typedef std::vector<std::string> STRVECTOR;

TCHAR				g_szClassPath[MAX_PATH];
TCHAR				g_szModPath[MAX_PATH];
STRVECTOR			g_vFiles;
DWORD				g_dwFilesDone = 0;
DWORD				g_dwFilesTotal = 0;
DWORD				g_dwNumThreadsAvail = 0;
HANDLE				g_hThreadAvailEvent = NULL;
HANDLE				g_hClassHasherExitEvent = NULL;
CRITICAL_SECTION 	g_hMyCriticalSection;

int FPaTable[] = { 0, 595103070, 1190206140, 1703516130, 232928632, 781891622, 1259548612, 1751884442, 951904174, 465857264, 2119040274, 1563783244, 895406806, 371613576, 1940899946, 1356285236, 1903808348, 1375814146, 931714528, 351295678, 2090596900, 1608742778, 980082840, 420638150, 1237657842, 1790813612, 255078990, 743227152, 1143414154, 1734316244, 47580982, 565086824, 1660133048, 1099633638, 604144644, 125438298, 1863429056, 1281959582, 702591356, 177749026, 1515157782, 2033710152, 482239402, 1070001908, 1470975086, 1960165680, 291264210, 841276300, 327832036, 821222586, 1434143576, 1979958790, 510157980, 1024518594, 1486454304, 2078405502, 725005898, 139344660, 1841802486, 1321148840, 651461426, 95161964, 1613076878, 1130173648, 1721741358, 1172782448, 544123538, 51783628, 1803388246, 1208289288, 764190698, 250876596, 1579374464, 2103171806, 416435516, 1001046114, 1405182712, 1891233702, 355498052, 910751002, 400981874, 882831916, 1360487886, 1919936656, 436488714, 964478804, 1559580854, 2140003816, 794466524, 203560322, 1772847712, 1255345982, 582528420, 29368570, 1682552600, 1194408518, 74198678, 655664072, 1117598762, 1642445172, 160308206, 720803504, 1333723474, 1812433932, 1020315960, 531121254, 2049037188, 1499029210, 825424960, 306868510, 2009327356, 1421568930, 1964368330, 1450011796, 870644598, 278689320, 2029507762, 1536121324, 1040633358, 494814032, 1302922852, 1859226426, 190323928, 673223046, 1078670108, 1664335426, 112863648, 633513214, 1850207490, 1295999068, 683079614, 198081248, 1671813242, 1088247076, 626311878, 103567256, 1459292844, 1971554290, 269094928, 863149390, 1528381396, 2019668618, 501753192, 1049667638, 524165726, 1011265280, 1506752738, 2058859964, 316413734, 832871032, 1412238746, 2002092228, 662881776, 83511470, 1634983756, 1108038162, 710996104, 152600022, 1821502004, 1340696426, 213152698, 801963748, 1246062854, 1765663832, 22431426, 573492124, 1202150526, 1692389664, 872977428, 393222474, 1928957608, 1367409654, 971678060, 445787186, 2132523984, 1550005902, 2110635238, 1588933048, 993830490, 407120644, 1882163614, 1398211776, 920556322, 363208316, 1165056840, 1711920662, 58737140, 553176234, 1217621552, 1810621294, 241333388, 756742610, 148397356, 731959410, 1311328144, 1834076878, 87713876, 641918218, 1137406696, 1622409142, 811907714, 320616412, 1989517374, 1441607008, 1032228858, 519963300, 2071434566, 1477384216, 2040631920, 1524178734, 1062242508, 472384914, 1950590728, 1463495254, 850574772, 298463466, 1092449758, 1650849920, 132935522, 613737020, 1291796646, 1871171064, 168712730, 695654212, 1781253012, 1230196426, 752539944, 262296694, 1741289196, 1152482226, 557378640, 37773582, 1385636922, 1911531876, 342244998, 924759000, 1601507650, 2081266716, 428084222, 989628064, 458362056, 942309782, 1570969204, 2128321322, 380647856, 902345966, 1346446092, 1933160018, 602860390, 9856568, 1696592346, 1181187204, 772595230, 225727296, 1761461410, 1267026428 };
int FPbTable[] = { 0, 338958382, 1012672626, 677916764, 1821133002, 2025345252, 1355833528, 1155823766, 1294764474, 1494782356, 1903206856, 1699002854, 564183408, 898947422, 503113986, 164163884, 442045300, 242027354, 637876998, 842081064, 1993694142, 1658930064, 1250522060, 1589472226, 1467325134, 1128366816, 1797894844, 2132650642, 1006227972, 802015786, 128318070, 328327768, 545668806, 884090600, 484054708, 149819034, 1275753996, 1480486434, 1884708478, 1684162128, 1839904636, 2040442706, 1375117070, 1170376480, 19333046, 353560472, 1031460804, 693030890, 987704754, 787166620, 109249984, 313990638, 1448306040, 1114078550, 1779387658, 2117817636, 2012455944, 1674034214, 1269795962, 1604031572, 461368514, 256636140, 656655536, 857201822, 1429759394, 1091337612, 1768181200, 2102416894, 968109416, 763376966, 99091738, 299638068, 404024344, 203486262, 608748650, 813489220, 1956160722, 1621933308, 1220840608, 1559270542, 1331787478, 1532325624, 1933401764, 1728661130, 602750492, 936977970, 531699310, 193269312, 38666092, 377087810, 1041356574, 707120944, 1858189222, 2062921608, 1386061780, 1185515514, 1975409508, 1636451146, 1239577366, 1574333240, 422712238, 218499968, 627981276, 827991026, 949146334, 749128432, 80673452, 284877442, 1411291668, 1076527674, 1749201510, 2088151624, 1877428240, 2077446206, 1404788834, 1200584780, 57344218, 392108276, 1060579496, 721629318, 583778730, 922737028, 513272280, 178516470, 1313311072, 1517523278, 1914413330, 1714403644, 1044697962, 712035140, 35191576, 372040502, 1388878752, 1190953870, 1855239122, 2057350140, 1936218832, 1734099710, 1328837282, 1526753932, 535040538, 198183476, 599276136, 931930694, 605929502, 808048688, 406972524, 209055810, 1217497300, 1554354426, 1959633062, 1626978440, 1764837796, 2097500554, 1433231830, 1096382968, 96272750, 294197568, 971057436, 768946482, 516091308, 183956866, 580830686, 917167600, 1917756774, 1719319880, 1309838612, 1512478010, 1408132118, 1205500984, 1873955940, 2072400970, 1063398620, 727069938, 54396078, 386538624, 77332184, 279963382, 952620714, 754175620, 1746384402, 2082713148, 1414241888, 1082099278, 1236760418, 1568894796, 1978359568, 1642022718, 624639912, 823076742, 426186714, 223547380, 1803335368, 2135469798, 1461755578, 1125418644, 133234178, 331671084, 1001182832, 798543454, 642793330, 845424476, 436999936, 238554926, 1255962552, 1592291222, 1988124618, 1655982052, 1898292668, 1695661458, 1299811790, 1498256864, 497675638, 161346904, 569754884, 901897514, 1007234054, 675099688, 5571700, 341908570, 1350919372, 1152482530, 1826180286, 2028819600, 1274710030, 1607372832, 2007408764, 1670559826, 662094020, 860018922, 455796918, 253685912, 114688436, 316807578, 982133190, 784216552, 1784301950, 2121158992, 1443258636, 1110604066, 1369676666, 1167557460, 1845474056, 2043390758, 1026544560, 689687454, 24378306, 357032940, 479138496, 146475758, 550714034, 887562908, 1879267850, 1681343012, 1281323640, 1483434582 };
int ktable[] = { 42, 37, -49, 48, 35, 120, -115, 94, 70, -15, 26, -68, 101, -119, -105, -30, 13, -30, 53, 120, 46, -102, -72, 38, 75, 19, 47, -60, 104, 107, -94, -102, 56, -68, -25, -82, 27, -60, 106, -16, 126, 77, -3, 18, 93, 53, 112, 76, 53, 94, -46, -42, 22, 38, 95, -120, 115, -81, -56, 106, 80, -41, 69, 52, 113, 121, -49, 92, 82, 1, 66, 2, 55, -120, -43, -32, 20, -16, 88, -66, 124, -101, -6, 36, 95, -29, 119, 122, 58, 106, -32, -104, 25, 18, 109, -58, 73, -59, 40, -14, 106, -67, -91, -84, 15, 52, 50, 78, 44, 76, -65, 16, 68, 39, 29, -118, 103, 95, -112, -44, 2, -42, 7, 54, 33, -82, -118, 104, 98, -13, -98, -72, 65, -117, 19, -26, 36, 2, -124, 4, 7, 122, 9, 90, 111, 17, -85, -64, 76, 105, 38, -98, 41, -32, -79, 124, 10, -104, 60, 34, 90, 79, 121, 22, 121, 55, -12, 72, 28, -66, 99, -86, 63, -58, -18, -12, 87, -83, 76, 110, 116, -43, -63, 48, 17, 92, 86, -46, 50, 36, -37, -116, 19, -118, 81, -28, 48, -14, -36, -70, 85, 123, 75, 88, 118, 3, -58, 6, 30, 104, 100, -100, 61, 16, -23, -62, 88, -103, 126, 32, 123, -31, -13, 126, 43, 54, -74, 74, 8, 78, 59, 20, 109, -57, -84, -10, 78, -65, 33, -88, 38, -44, -125, 50, 5, -84, 14, 108, 96, 37, -103, -114, 67, 93, 20, -48, 102, -97, -80, 46, 69, -25, 61, 112, 32, 110, -86, -110, 3, 22, 39, -52, 107, 125, -123, 86, 72, 5, 8, 8, 45, -116, -97, -22, 14, -12, 18, -76, 94, 35, 87, -128, 125, 91, -38, -34, 24, -46, 77, 60, 59, -86, -64, 98, 83, -63, 98, -8, 112, -71, -17, -90, 21, 48, 120, 68, 54, 72, -11, 26, 23, -26, 127, 114, 52, -98, -14, 44, 81, 23, 101, -50, 114, 111, -24, -112, 26, 4, 74, 10, 57, 124, -57, 84, 92, -11, 80, -74, 127, -115, -35, -24, 47, 90, -104, -36, 12, 34, 21, -126, 105, -85, -126, 96, 74, -45, 15, 62, 34, -72, -83, -92, 1, -64, 32, -6, 100, 73, -73, 24, 71, 49, 58, 70, 4, 108, 46, -106, 39, 20, -93, -56, 66, -99, 52, 42, 97, -27, -71, 116, 9, -114, 27, -18, 42, -10, -106, -80, 79, 127, 1, 82, 108, 7, -116, 12, 60, -48, -55, 56, 31, -88, 68, 102, 122, 33, -45, -124, 89, 89, 94, -38, 49, 50, -4, 64, 18, 74, 113, 30, 119, -61, -26, -4, 84, -69, 107, -94, 117, 21, -31, -54, 86, 109, 108, -108, 51, -28, -5, 118, 16, -100, 118, 40, 120, -9, -44, -78, 91, -113, 89, -20, 62, 6, -50, 14, 29, 126, 67, 80, 77, -87, 6, 100, 110, -47, -117, 58, 11, 88, 28, -40, 40, 32, -111, -122, 64, 75, 51, 28, 99, 51, -66, 66, 6, -70, 41, -96, 37, -62, -92, -2, 110, 71, -19, 2, 77, 63, 96, 92, 40, -74, -9, -66, 11, -50, 122, -32, 99, -91, -40, 122, 64, -35, 85, 36, 37, 84, -62, -58, 6, 44, 79, -104, 86, -5, 10, -84, 117, -125, -121, -14, 16, 10, 16, 16, 51, 114, -99, 78, 91, 25, 63, -44, 120, 97, -78, -118, 29, -24, 37, 104, 62, -112, -88, 54, 31, 62, 34, 94, 60, 70, -81, 0, 89, -49, 56, -30, 122, -73, -75, -68, 18, -36, 23, 38, 49, -92, -102, 120, 84, 45, 13, -102, 119, 85, -128, -60, 39, -126, -59, -16, 4, -6, 72, -82, 97, 115, -33, 76, 66, 11, 82, 18, 42, 96, -16, -120, 9, 24, 125, -42, 108, -111, -22, 52, 79, -23, 103, 106, 12, -76, 115, -70, 47, -52, -2, -28, 74, 69, 105, 6, 105, 61, -28, 88, 1, 86, 70, -62, 34, 46, -53, -100, 71, -89, 92, 126, 100, -33, -47, 32, 52, 8, -108, 20, 23, 112, 25, 74, 114, -7, -114, -88, 81, -127, 3, -10, 57, -22, -95, 108, 26, -110, 44, 50, 127, 27, -69, -48, 92, 99, 54, -114, 125, -51, -68, -26, 94, -75, 49, -72, 59, 60, -90, 90, 24, 68, 43, 4, 112, 47, -119, -98, 83, 87, 4, -64, 54, -34, -109, 34, 21, -90, 30, 124, 69, 113, 91, 72, 102, 9, -42, 22, 3, -128, 65, -12, 32, -8, -52, -86, 72, -109, 110, 48, 107, -21, -29, 110, 14, 98, 116, -116, 45, 26, -7, -46, 8, -40, 93, 44, 43, -96, -48, 114, 78, 41, 71, -112, 109, 81, -54, -50, 5, 58, 104, 84, 38, 66, -27, 10, 67, -53, 114, -24, 96, -77, -1, -74, 48, 100, -70, -126, 19, 28, 55, -36, 118, -107, -96, 62, 85, -19, 45, 96, 61, -122, -113, -6, 30, -2, 2, -92, 123, 119, -107, 70, 88, 15, 24, 24, 121, -95, -110, 112, 90, -39, 31, 46, 63, 80, -120, -52, 28, 40, 5, -110, 116, 67, -89, 8, 87, 59, 42, 86, 50, -78, -67, -76, 17, -54, 48, -22, 65, 29, 117, -34, 98, 101, -8, -128, 7, -20, 111, 98, 36, -108, -30, 60, 76, -1, 64, -90, 111, -121, -51, -8, 10, 14, 90, 26, 41, 118, -41, 68, 106, 43, -61, -108, 73, 83, 78, -54, 44, -38, -39, 40, 15, -94, 84, 118, 103, -55, -10, -20, 68, -79, 123, -78, 33, 56, -20, 80, 2, 64, 97, 14, 82, -105, 36, 58, 113, -17, -87, 100, 20, 102, 62, -122, 55, 30, -77, -40, 95, 117, 17, 66, 124, 13, -100, 28, 25, -124, 11, -2, 58, -4, -122, -96, 27, 82, 12, -56, 56, 42, -127, -106, 93, -93, 22, 116, 126, -37, -101, 42, 22, -80, 57, -80, 53, -56, -76, -18, 80, 65, 35, 12, 115, 57, -82, 82, 35, -18, -21, 102, 0, -106, 102, 56, 101, 31, -15, -38, 70, 103, 124, -124, 46, 12, -34, 30, 13, 116, 83, 64, 104, -3, -60, -94, 75, -123, 73, -4 };

BOOL CheckCommandLineArguments();
BOOL GetClassFiles();
BOOL RemoveOldFiles();
BOOL StartHashThreads();
UINT HashThread( LPVOID pParam );
unsigned __int64 LongFN(unsigned __int64 paramLong, LPCTSTR paramString);
unsigned __int32 IntFN(unsigned __int32 paramInt, LPCTSTR paramString);
BOOL GetJavaClassName(LPTSTR lpClassName, LPCTSTR lpClassFilePath);
UINT HashThreadStarter( LPVOID pParam );

struct JavaConstant {
	BYTE tag;
	BYTE* bytes;
	UINT32 indexA;
	UINT32 indexB;
	JavaConstant() {
		tag = 0;
		bytes = NULL;
		indexA = 0;
		indexB = 0;
	}
};

BOOL ctrl_handler(DWORD event) {
	switch (event) {
	case CTRL_C_EVENT:
	case CTRL_CLOSE_EVENT:
	case CTRL_BREAK_EVENT:
	case CTRL_LOGOFF_EVENT:
	case CTRL_SHUTDOWN_EVENT:
		if (g_hClassHasherExitEvent != NULL)
			SetEvent(g_hClassHasherExitEvent);
		return TRUE;
	}
	return FALSE;
}

int _tmain(int argc, _TCHAR* argv[]) {
	SetConsoleTitle("IL-2 Class Hasher");
	if (!CheckCommandLineArguments()) {
		printf("Command line arguments error, exiting.\r\n");
		return -1;
	}
	if (!GetClassFiles()) {
		printf("Couldn't find classfiles for hashing, exiting.\r\n");
		return -2;
	}
	g_dwFilesTotal = g_vFiles.size();

	g_hThreadAvailEvent = CreateEvent(NULL, FALSE, FALSE,
			CLASS_HASHER_EVENT_THREAD_AVAIL);

	g_hClassHasherExitEvent = CreateEvent(NULL, FALSE, FALSE,
			CLASS_HASHER_EXIT);

	printf("Removing old files from mod folder...\r\n");
	if (!RemoveOldFiles()) {
		printf("Couldn't remove old mod files, exiting.\r\n");
		return -3;
	}

	SetConsoleCtrlHandler((PHANDLER_ROUTINE) ctrl_handler, TRUE);

	if (!InitializeCriticalSectionAndSpinCount(&g_hMyCriticalSection, 4000)) {
		printf(
				"Couldn't initialize interthread mutual exclusion, exiting.\r\n");
		return -4;
	}

	printf("Hashing %lu files, each file a dot, max. %d dots per line:\r\n",
			g_dwFilesTotal, CLASS_HASHER_DOTS_PER_LINE);
	if (!StartHashThreads()) {
		printf("Couldn't start hash threads, exiting.\r\n");
		DeleteCriticalSection(&g_hMyCriticalSection);
		return -5;
	}
	DWORD dwWaitForThreads = WaitForSingleObject(g_hClassHasherExitEvent,
			CLASS_HASHER_MAX_WAIT);

	if (dwWaitForThreads == WAIT_TIMEOUT) {
		printf("****************************************");
		printf("*                                      *");
		printf("*          Class Hasher Error          *");
		printf("* Hashing didn't finish within %dms *", CLASS_HASHER_MAX_WAIT);
		printf("*   Please check your output folder    *");
		printf("*                                      *");
		printf("****************************************");
	}

	printf("\r\nDone hashing %lu files.\r\n", g_dwFilesDone);
	Sleep(100);

	DeleteCriticalSection(&g_hMyCriticalSection);
	CloseHandle(g_hClassHasherExitEvent);
	CloseHandle(g_hThreadAvailEvent);
	g_vFiles.clear();
	return 0;
}

BOOL CheckCommandLineArguments() {
	if (__argc != 3) {
		printf(
				"Calling convention:\r\nClassHasher <Class Files Base Path> <Hashed Files Target Path>\r\n");
		printf("Press any key to continue\r\n");
		getch();
		return FALSE;
	}
	_tcscpy(g_szClassPath, __targv[1]);
	_tcscpy(g_szModPath, __targv[2]);
	return TRUE;
}

BOOL RemoveOldFiles() {
	HANDLE hFind = INVALID_HANDLE_VALUE;
	WIN32_FIND_DATAA ffd;
	std::string spec;
	std::string path = g_szModPath;
	std::string delfile;

	spec = path + "\\*.*";
	CreateDirectory(g_szModPath, NULL);

	hFind = FindFirstFileA(spec.c_str(), &ffd);
	if (hFind == INVALID_HANDLE_VALUE) {
		return FALSE;
	}

	do {
		if (strcmp(ffd.cFileName, ".") != 0
				&& strcmp(ffd.cFileName, "..") != 0) {
			if (!(ffd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)) {
				if (_tcslen(ffd.cFileName) == 16) {
					if (!(ffd.dwFileAttributes & FILE_ATTRIBUTE_READONLY)) {
						BOOL bIsHex = TRUE;
						for (unsigned int iPos = 0;
								iPos < _tcslen(ffd.cFileName); iPos++) {
							if (!isxdigit(ffd.cFileName[iPos])) {
								bIsHex = FALSE;
								break;
							}
						}
						if (bIsHex) {
							delfile = path + "\\" + ffd.cFileName;
							DeleteFile(delfile.c_str());
						}
					}
				}
			}
		}
	} while (FindNextFile(hFind, &ffd) != 0);

	if (GetLastError() != ERROR_NO_MORE_FILES) {
		FindClose(hFind);
		return FALSE;
	}

	FindClose(hFind);
	hFind = INVALID_HANDLE_VALUE;

	return TRUE;
}

BOOL GetClassFiles() {
	HANDLE hFind = INVALID_HANDLE_VALUE;
	WIN32_FIND_DATAA ffd;
	std::string spec;
	std::string path = g_szClassPath;
	std::stack<std::string> directories;

	directories.push(path);
	g_vFiles.clear();

	while (!directories.empty()) {
		path = directories.top();
		spec = path + "\\*.*";
		directories.pop();

		hFind = FindFirstFileA(spec.c_str(), &ffd);
		if (hFind == INVALID_HANDLE_VALUE) {
			return FALSE;
		}

		do {
			if (strcmp(ffd.cFileName, ".") != 0
					&& strcmp(ffd.cFileName, "..") != 0) {
				if (ffd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) {
					directories.push(path + "\\" + ffd.cFileName);
				} else {
					if (strcmp(ffd.cFileName + _tcslen(ffd.cFileName) - 6,
							".class") == 0)
						g_vFiles.push_back(path + "\\" + ffd.cFileName);
				}
			}
		} while (FindNextFile(hFind, &ffd) != 0);

		if (GetLastError() != ERROR_NO_MORE_FILES) {
			FindClose(hFind);
			return FALSE;
		}

		FindClose(hFind);
		hFind = INVALID_HANDLE_VALUE;
	}

	return TRUE;
}

BOOL StartHashThreads() {
	SYSTEM_INFO sysinfo;
	GetSystemInfo(&sysinfo);
	g_dwNumThreadsAvail = sysinfo.dwNumberOfProcessors * 2;
	if (g_dwNumThreadsAvail < 2)
		g_dwNumThreadsAvail = 2;
	if (g_dwNumThreadsAvail > 256)
		g_dwNumThreadsAvail = 256;
	HANDLE threadStarterHandle = CreateThread(NULL, 0,
			(LPTHREAD_START_ROUTINE) HashThreadStarter, NULL, CREATE_SUSPENDED,
			NULL);
	SetThreadPriority(threadStarterHandle, THREAD_PRIORITY_BELOW_NORMAL);
	ResumeThread(threadStarterHandle);
	CloseHandle(threadStarterHandle);
	return TRUE;
}

UINT HashThreadStarter(LPVOID pParam) {
	DWORD dwThreadsRemain = 1;
	for (STRVECTOR::iterator it = g_vFiles.begin(); it != g_vFiles.end();
			it++) {
		dwThreadsRemain = InterlockedDecrement(
				(volatile LONG*) &g_dwNumThreadsAvail);
		HANDLE threadHandle = CreateThread(NULL, 0,
				(LPTHREAD_START_ROUTINE) HashThread, (LPVOID) it->c_str(),
				CREATE_SUSPENDED, NULL);
		SetThreadPriority(threadHandle, THREAD_PRIORITY_BELOW_NORMAL);
		ResumeThread(threadHandle);
		CloseHandle(threadHandle);
		while (dwThreadsRemain == 0) {
			DWORD dwWaitForThread = WaitForSingleObject(g_hThreadAvailEvent,
					CLASS_HASHER_THREAD_MAX_WAIT);
			if (dwWaitForThread == WAIT_OBJECT_0)
				ResetEvent(g_hThreadAvailEvent);
			InterlockedIncrement((volatile LONG*) &g_dwNumThreadsAvail);
			dwThreadsRemain = InterlockedDecrement(
					(volatile LONG*) &g_dwNumThreadsAvail);
			if ((dwWaitForThread == WAIT_TIMEOUT) && (dwThreadsRemain == 0)) {
				printf("*************************************");
				printf("*                                   *");
				printf("*         Class Hasher Error        *");
				printf("* No Thread available within %dms *",
						CLASS_HASHER_THREAD_MAX_WAIT);
				printf("*  Please check your output folder  *");
				printf("*                                   *");
				printf("*************************************");
				SetEvent(g_hClassHasherExitEvent);
			}
		}
	}
	return 0;
}

UINT HashThread(LPVOID pParam) {
	Sleep(0);
	TCHAR szHashedFileName[MAX_PATH];
	TCHAR szClassName[MAX_PATH];
	TCHAR szClassNameToHash[MAX_PATH];
	memset(szHashedFileName, 0, sizeof(szHashedFileName));
	memset(szClassName, 0, sizeof(szClassName));
	memset(szClassNameToHash, 0, sizeof(szClassNameToHash));
	LPCTSTR lpClassFilePath = (LPCTSTR) pParam;
	if (!GetJavaClassName(szClassName, lpClassFilePath))
		return -1;
	strcpy(szClassNameToHash, "sdw");
	strcat(szClassNameToHash, szClassName);
	strcat(szClassNameToHash, "cwc2w9e");
	UINT32 HashInt = IntFN(0, szClassNameToHash);
	sprintf(szClassName, "cod/%d", HashInt);
	UINT64 HashLong = LongFN(0, szClassName);
	unsigned __int32 a = (unsigned) (HashLong & 0xFFFFFFFF);
	unsigned __int32 b = (unsigned) (HashLong >> 32 & 0xFFFFFFFF);
	sprintf(szHashedFileName, "%s\\%08lX%08lX", g_szModPath, b, a);
	CopyFile(lpClassFilePath, szHashedFileName, FALSE);
	EnterCriticalSection(&g_hMyCriticalSection);
	DWORD dwFilesDone = InterlockedIncrement((volatile LONG*) &g_dwFilesDone);
	printf(".");
	if (dwFilesDone % CLASS_HASHER_DOTS_PER_LINE == 0)
		printf("\r\n");
	InterlockedIncrement((volatile LONG*) &g_dwNumThreadsAvail);
	SetEvent(g_hThreadAvailEvent);
	if (dwFilesDone >= g_dwFilesTotal)
		SetEvent(g_hClassHasherExitEvent);
	LeaveCriticalSection(&g_hMyCriticalSection);
	return 0;
}

unsigned __int64 LongFN(unsigned __int64 paramLong, LPCTSTR paramString) {
	int paramLen = _tcslen(paramString);
	unsigned __int32 c;
	unsigned __int32 a = (unsigned) (paramLong & 0xFFFFFFFF);
	unsigned __int32 b = (unsigned) (paramLong >> 32 & 0xFFFFFFFF);
	for (int i = 0; i < paramLen; i++) {
		c = (unsigned __int32) paramString[i];
		if ((c > 96) && (c < 123))
			c &= 223;
		else if (c == 47)
			c = 92;
		a = (a << 8 | c) ^ FPaTable[a >> 24];
		b = (b << 8 | c) ^ FPbTable[b >> 24];
	}
	return ((unsigned __int64) a & 0xFFFFFFFF) | ((unsigned __int64) b << 32);
}

unsigned __int32 IntFN(unsigned __int32 paramInt, LPCTSTR paramString) {
	int paramLen = _tcslen(paramString);
	unsigned __int32 c;
	unsigned __int32 b;
	unsigned __int32 a = paramInt;
	for (int i = 0; i < paramLen; i++) {
		c = (unsigned __int32) paramString[i];
		b = c & 0xFF;
		a = (a << 8 | b) ^ FPaTable[a >> 24];
		b = c >> 8 & 0xFF;
		a = (a << 8 | b) ^ FPaTable[a >> 24];
	}
	return (unsigned __int32) a;
}

LPSTR strreplace(LPSTR theString, char theCharToReplace, char replaceWith) {
	while (*theString != '\0') {
		if (*theString == theCharToReplace)
			*theString = replaceWith;
		theString++;
	}
	return theString;
}

BOOL GetJavaClassName(LPTSTR lpClassName, LPCTSTR lpClassFilePath) {
	UINT32 ui32Buf;
	UINT16 ui16Buf;
	UINT8 ui8Buf;
	UINT16 uiNumConstPool;
	UINT16 uiClassNameIndex;
	UINT16 ui16Swap;
	LPSTR lpClassNameAscii;
	FILE* fClass = fopen(lpClassFilePath, "rb");
	fseek(fClass, 0, SEEK_SET);
	fread(&ui32Buf, 4, 1, fClass); // Magic Bytes
	if (ui32Buf != 0xBEBAFECA)
		return FALSE;
	fseek(fClass, 4, SEEK_CUR);
	fread(&ui16Buf, 2, 1, fClass); // Major Version
	uiNumConstPool = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes

	JavaConstant* Constants = new JavaConstant[uiNumConstPool];

	for (int i = 1; i < uiNumConstPool; i++) {
		fread(&ui8Buf, 1, 1, fClass); // Constant type
		Constants[i].tag = ui8Buf;
		switch (ui8Buf) {
		case 7: // Class
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexA = ui16Swap;
			Constants[i].indexB = 0;
			break;
		case 8: // String
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexA = ui16Swap;
			Constants[i].indexB = 0;
			break;
		case 9: // Fieldref
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexA = ui16Swap;
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexB = ui16Swap;
			break;
		case 10: // Methodref
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexA = ui16Swap;
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexB = ui16Swap;
			break;
		case 11: // InterfaceMethodref
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexA = ui16Swap;
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexB = ui16Swap;
			break;
		case 3: // Integer
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexA = ui16Swap;
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexB = ui16Swap;
			break;
		case 4: // Float
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexA = ui16Swap;
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexB = ui16Swap;
			break;
		case 12: // Name and Type
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexA = ui16Swap;
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexB = ui16Swap;
			break;
		case 1: // UTF8
			fread(&ui16Buf, 2, 1, fClass);
			ui16Swap = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
			Constants[i].indexA = ui16Swap;
			Constants[i].indexB = 0;
			Constants[i].bytes = new BYTE[ui16Swap + 1];
			memset(Constants[i].bytes, 0, ui16Swap + 1);
			fread(Constants[i].bytes, ui16Swap, 1, fClass); // read ASCII string
			break;
		case 5: // Long
			Constants[i].indexA = 0;
			Constants[i].indexB = 0;
			fseek(fClass, 8, SEEK_CUR);
			i++;
			break;
		case 6: // Double
			Constants[i].indexA = 0;
			Constants[i].indexB = 0;
			fseek(fClass, 8, SEEK_CUR);
			i++;
			break;
		case 2:
		default:
			break;
		}
	}
	fseek(fClass, 2, SEEK_CUR); // Access Flags
	fread(&ui16Buf, 2, 1, fClass); // Class Name Index
	uiClassNameIndex = (ui16Buf >> 8) | (ui16Buf << 8); // swap bytes
	JavaConstant theClassConstantIndex = Constants[uiClassNameIndex];
	JavaConstant theClassConstant;
	switch (theClassConstantIndex.tag) {
	case 7:
	case 8:
		theClassConstant = Constants[theClassConstantIndex.indexA];
		if (theClassConstant.tag == 1) { // ASCII string
			lpClassNameAscii = (LPSTR) theClassConstant.bytes;
			strreplace(lpClassNameAscii, '/', '.');
			strcpy(lpClassName, lpClassNameAscii);
		}
		break;
	default:
		break;
	}
	fclose(fClass);
	return TRUE;
}
