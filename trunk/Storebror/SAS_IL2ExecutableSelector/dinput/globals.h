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

#include "StdAfx.h"
#include <set>
#include <string>
#include <thread>
#include <queue>
#include <condition_variable>
#include <mutex>
#include "jni.h"
#include "dinput.h"

// Suppress compiler warnings about unsafe functions
#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

#define RunWorker(ThreadStartRoutine) 	CloseHandle(CreateThread(NULL,0,(LPTHREAD_START_ROUTINE)ThreadStartRoutine,NULL,0,NULL))

//*************************************************************************
// Type definitions
//*************************************************************************
typedef HRESULT(WINAPI * DIRECTINPUTCREATEA)(HINSTANCE, DWORD, LPDIRECTINPUTA *, LPUNKNOWN);
typedef HRESULT(WINAPI * DIRECTINPUTCREATEW)(HINSTANCE, DWORD, LPDIRECTINPUTW *, LPUNKNOWN);
typedef HRESULT(WINAPI * DIRECTINPUTCREATEEX)(HINSTANCE, DWORD, REFIID, LPVOID *, LPUNKNOWN);
typedef HRESULT(WINAPI * DLLREGISTERSERVER)();
typedef HRESULT(WINAPI * DLLUNREGISTERSERVER)();
typedef BOOL(WINAPI * IMMDISABLEIME)(__in  DWORD);
typedef jint(CALLBACK * ORI_CreateJavaVM)(JavaVM **p_vm, void **p_env, void *vm_args);
typedef jclass(CALLBACK * ORI_DefineClass)(JNIEnv *env, const char *name, jobject loader, const jbyte *buf, jsize bufLen, jobject jo);
typedef void(WINAPI * IL2GE_Init)();
typedef int(__cdecl * ORI_SFS_mount)(const char *sfs, int i, char c);

struct ClassListItem {
	BOOL isEncrypted;
	std::string className;
	ClassListItem() {
	}
	ClassListItem(BOOL IsEncrypted, LPSTR FilePath) {
		isEncrypted = IsEncrypted;
		className = FilePath;
	}
	ClassListItem(LPSTR FilePath) {
		isEncrypted = FALSE;
		className = FilePath;
	}
	bool operator==(const ClassListItem& other)
	{
		return !className.compare(other.className);
	}
};

struct classlistitem_compare {
	bool operator() (const ClassListItem* lhs, const ClassListItem* rhs) const {
		return lhs->className.compare(rhs->className) < 0;
	}
};

struct jvm_options_compare {
	bool operator() (const std::string& lhs, const std::string& rhs) const {
		return stricmp(lhs.c_str(), rhs.c_str()) < 0;
	}
};

//*************************************************************************
// Globals
//*************************************************************************
extern BOOL bDisableMutex;
extern bool classesRuntimeWriterThreadDone;
extern bool classesRuntimeWriterThreadNotified;
extern BOOL g_bDumpClassFiles;
extern BOOL g_bDumpFileAccess;
extern BOOL g_bDumpOtherFiles;
extern BOOL g_bDumpSFSAccess;
extern BOOL g_bInstantDump;
extern BOOL g_bIsOpenGL;
extern BOOL g_bProcessAttached;
extern BOOL isMain;
extern BYTE g_oldMem[9];
extern CHAR cBuffer[MAX_PATH];
extern CHAR cBuf[MAX_PATH];
extern CHAR cClassesRuntimeLogFileName[MAX_PATH];
extern char cIniFile[MAX_PATH];
extern CHAR g_cDumpSeparator;
extern const TCHAR *	c_szMsgWndClass;
extern DIRECTINPUTCREATEA		SysDirectInputCreateA;
extern DIRECTINPUTCREATEEX		SysDirectInputCreateEx;
extern DIRECTINPUTCREATEW		SysDirectInputCreateW;
extern DLLREGISTERSERVER		SysDllRegisterServer;
extern DLLUNREGISTERSERVER		SysDllUnregisterServer;
extern FILE *classesRuntimeLog;
extern HANDLE g_hJobObject;
extern HMODULE g_hIL2GE;
extern HMODULE hDInput;
extern HMODULE hImm32;
extern HMODULE hJvm;
extern IL2GE_Init				Il2geInit;
extern IMMDISABLEIME			SysImmDisableIme;
extern int g_iDebugMode;
extern int g_iSplashScreenMode;
extern int g_isServer;
extern ORI_CreateJavaVM		JniCreateJavaVM;
extern ORI_DefineClass		    JniDefineClass;
extern ORI_SFS_mount           Il2fbSFS_mount;
extern std::condition_variable cond_var;
extern std::mutex m;
extern std::queue<std::string> pendingClassesRuntimeOutput;
extern std::set<ClassListItem*, classlistitem_compare> ClassesList;
extern std::set<std::string, jvm_options_compare> JvmOptions;
extern std::thread ClassesRuntimeWriterThread;
extern TCHAR ClassesRuntimeFileName[MAX_PATH];
extern TCHAR ClassesSortedFileName[MAX_PATH];
extern TCHAR ClassesSummaryFileName[MAX_PATH];
extern TCHAR LogFileName[MAX_PATH];
extern TCHAR szBuffer[MAX_PATH];
extern TCHAR szClassDumpFile[MAX_PATH];
extern TCHAR szConfIniFile[MAX_PATH];
extern TCHAR szCurDir[MAX_PATH];
extern CHAR cCurDir[MAX_PATH];
extern TCHAR szDirectInputPath[MAX_PATH];
extern TCHAR szImm32Path[MAX_PATH];
extern TCHAR szIniFile[MAX_PATH];
extern TCHAR szJvmPath[MAX_PATH];
