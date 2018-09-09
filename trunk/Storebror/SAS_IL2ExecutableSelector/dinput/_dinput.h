//*****************************************************************
// DINPUT.dll - JVM Parameter parser and il2fb.exe modifier
// Copyright (C) 2013 SAS~Storebror
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

#ifdef DINPUT_EXPORTS
#define DINPUT_API extern "C" __declspec(dllexport)
#else
#define DINPUT_API __declspec(dllimport)
#endif

//*************************************************************************
// Includes
//*************************************************************************
#include "jni.h"
#include "dinput.h"

//*************************************************************************
// Definitions
//*************************************************************************
#define PROC_FULL_RIGHTS DELETE|READ_CONTROL|SYNCHRONIZE|WRITE_DAC|WRITE_OWNER|PROCESS_TERMINATE|PROCESS_CREATE_THREAD|PROCESS_SET_SESSIONID|PROCESS_VM_OPERATION|PROCESS_VM_READ|PROCESS_VM_WRITE|PROCESS_DUP_HANDLE|PROCESS_CREATE_PROCESS|PROCESS_SET_QUOTA|PROCESS_SET_INFORMATION|PROCESS_QUERY_INFORMATION|PROCESS_SUSPEND_RESUME
#define CLIENT_INI			L"il2fb.ini"
#define SERVER_INI			L"il2server.ini"
#define CLIENT_CONF_INI		L"conf.ini"
#define LOGFILE_NAME		L"initlog.lst"
#define IL2GE_PATHS			{L"il2ge.dll", L"il2ge\\lib\\il2ge.dll", L"bin\\selector\\basefiles\\il2ge.dll"}
#define IL2GE_PATHS_NUM		3
#define	XSS_DIVIDER			128
#define PERM_DIVIDER		8

#define MEM_STRATEGY_BALANCED		0
#define MEM_STRATEGY_CONSERVATIVE	1
#define MEM_STRATEGY_HEAPONLY		2

#define SELECTOR_INFO_FILE_VERSION		1
#define SELECTOR_INFO_PRODUCT_VERSION	2
#define SELECTOR_INFO_FILEVERSION		3
#define SELECTOR_INFO_PRODUCTVERSION	4
#define SELECTOR_INFO_SPECIAL_BUILD		5
#define SELECTOR_INFO_COPYRIGHT			6

//*************************************************************************
// Structure definitions
//*************************************************************************
struct MyJvmOptionItem {
    LPCSTR lpJvmOptionItem;
    LPCSTR lpJvmOptionItemLowercase;
};

//*************************************************************************
// Function Prototypes
//*************************************************************************
void AdjustJvmParams();
void ReadSelectorSettings();
void GetParams();
BOOL IsServerExe();
BOOL StartWatchdog();
BOOL FileExists(LPCTSTR szPath);
void ReadConfSettings();
void _tcstrim(LPTSTR str);

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
JNIEXPORT jstring JNICALL Java_com_maddox_sas1946_il2_util_BaseGameVersion_getSelectorInfo(JNIEnv *, jobject, jint);
typedef void(WINAPI * IL2GE_Init)();
