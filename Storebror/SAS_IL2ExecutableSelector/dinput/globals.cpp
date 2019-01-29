//*****************************************************************
// DINPUT.dll - JVM Parameter parser and il2fb.exe modifier
// Copyright (C) 2019 SAS~Storebror
//
// This file is part of DINPUT.dll.
//
// DINPUT.dll is free software.
// It is distributed under the DWTFYWTWIPL license:
//
// DO WHAT THE FUCK YOU WANT TO WITH IT PUBLIC LICENSE
// Version 1, March 2012
//
// Copyright (C) 2013 SAS~Storebror <mike@sas1946.com>
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

#include "globals.h"

// Suppress compiler warnings about unsafe functions
#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

//*************************************************************************
// Globals
//*************************************************************************
BOOL bDisableMutex = FALSE;
bool classesRuntimeWriterThreadDone = false;
bool classesRuntimeWriterThreadNotified = false;
BOOL g_bDumpClassFiles = FALSE;
BOOL g_bDumpFileAccess = FALSE;
BOOL g_bDumpOtherFiles = FALSE;
BOOL g_bDumpSFSAccess = FALSE;
BOOL g_bInitialized = FALSE;
BOOL g_bInstantDump = FALSE;
BOOL g_bIsOpenGL = FALSE;
BOOL g_bProcessAttached = FALSE;
BOOL isMain = TRUE;
BYTE g_oldMem[9];
CHAR cBuffer[MAX_PATH];
CHAR cBuf[MAX_PATH];
CHAR cClassesRuntimeLogFileName[MAX_PATH];
char cIniFile[MAX_PATH];
CHAR g_cDumpSeparator = ' ';
const TCHAR *	c_szMsgWndClass = _T("Il2SASWatchdogMsgWnd");
DIRECTINPUTCREATEA		SysDirectInputCreateA = NULL;
DIRECTINPUTCREATEEX		SysDirectInputCreateEx = NULL;
DIRECTINPUTCREATEW		SysDirectInputCreateW = NULL;
DLLREGISTERSERVER		SysDllRegisterServer = NULL;
DLLUNREGISTERSERVER		SysDllUnregisterServer = NULL;
FILE *classesRuntimeLog = NULL;
HANDLE g_hJobObject = NULL;
HMODULE g_hIL2GE = NULL;
HMODULE hDInput = NULL;
HMODULE hImm32 = NULL;
HMODULE hJvm = NULL;
IL2GE_Init				Il2geInit = NULL;
IMMDISABLEIME			SysImmDisableIme = NULL;
int g_iDebugMode = 0;
int g_iSplashScreenMode = 0;
int g_isServer = -1;
ORI_CreateJavaVM		JniCreateJavaVM = NULL;
ORI_DefineClass		    JniDefineClass = NULL;
ORI_SFS_mount           Il2fbSFS_mount = NULL;
std::condition_variable cond_var;
std::mutex m;
std::queue<std::string> pendingClassesRuntimeOutput;
std::set<ClassListItem*, classlistitem_compare> ClassesList;
std::set<std::string, jvm_options_compare> JvmOptions;
std::thread ClassesRuntimeWriterThread;
TCHAR ClassesRuntimeFileName[MAX_PATH];
TCHAR ClassesSortedFileName[MAX_PATH];
TCHAR ClassesSummaryFileName[MAX_PATH];
TCHAR LogFileName[MAX_PATH];
TCHAR szBuffer[MAX_PATH];
TCHAR szClassDumpFile[MAX_PATH];
TCHAR szConfIniFile[MAX_PATH];
TCHAR szCurDir[MAX_PATH];
CHAR cCurDir[MAX_PATH];
TCHAR szDirectInputPath[MAX_PATH];
TCHAR szImm32Path[MAX_PATH];
TCHAR szIniFile[MAX_PATH];
TCHAR szJvmPath[MAX_PATH];
