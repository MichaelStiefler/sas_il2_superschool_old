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

//*************************************************************************
// Definitions
//*************************************************************************
#define _IMM_ // Define _IMM_ in order to avoid forward declaration of ImmDisableIME() in Imm.h

//*************************************************************************
// Includes
//*************************************************************************
#include "_dinput.h"
//#include <windows.h>
#include <tchar.h>
#include <vector>
#include <set>
#include <algorithm>
#include <filesystem>
#include <fstream>
#include <psapi.h>
#include "jni.h"
#include "trace.h"
#include "SFS_Tools.h"
#include "globals.h"
#include "classes_runtime.h"
#include "../global/version.h"
#include "../detours/include/detours.h"
#pragma comment(lib, "../detours/lib.X86/detours.lib")
#pragma comment(lib, "psapi.lib")

// Suppress compiler warnings about unsafe functions
#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

//************************************
// Method:    DirectInputCreateA
// FullName:  DirectInputCreateA
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: HINSTANCE hinst
// Parameter: DWORD dwVersion
// Parameter: LPDIRECTINPUTA * ppDI
// Parameter: LPUNKNOWN punkOuter
//************************************
DINPUT_API HRESULT __stdcall DirectInputCreateA(HINSTANCE hinst, DWORD dwVersion, LPDIRECTINPUTA *ppDI, LPUNKNOWN punkOuter)
{
	// forward any DirectInputCreateA function call to windows native dinput.dll
	if (hDInput == NULL) {
		LPCTSTR lpEnvPath = _wgetenv(L"SystemRoot");
		TCHAR szDirectInputPath[MAX_PATH];
		_stprintf(szDirectInputPath, L"%s\\system32\\dinput.dll", lpEnvPath);
		hDInput = LoadLibraryW(szDirectInputPath);
	}
	if (hDInput == NULL) {
		return E_NOINTERFACE;
	}

	if (SysDirectInputCreateA == NULL) {
		SysDirectInputCreateA = (DIRECTINPUTCREATEA)GetProcAddress(hDInput, "DirectInputCreateA");
	}

	return SysDirectInputCreateA(hinst, dwVersion, ppDI, punkOuter);
}

//************************************
// Method:    DirectInputCreateEx
// FullName:  DirectInputCreateEx
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: HINSTANCE hinst
// Parameter: DWORD dwVersion
// Parameter: REFIID refIID
// Parameter: LPVOID * ppDI
// Parameter: LPUNKNOWN punkOuter
//************************************
DINPUT_API HRESULT __stdcall DirectInputCreateEx(HINSTANCE hinst, DWORD dwVersion, REFIID refIID, LPVOID *ppDI, LPUNKNOWN punkOuter)
{
	// forward any DirectInputCreateEx function call to windows native dinput.dll
	if (hDInput == NULL) {
		LPCTSTR lpEnvPath = _wgetenv(L"SystemRoot");
		TCHAR szDirectInputPath[MAX_PATH];
		_stprintf(szDirectInputPath, L"%s\\system32\\dinput.dll", lpEnvPath);
		hDInput = LoadLibraryW(szDirectInputPath);
	}
	if (hDInput == NULL) {
		return E_NOINTERFACE;
	}

	if (SysDirectInputCreateEx == NULL) {
		SysDirectInputCreateEx = (DIRECTINPUTCREATEEX)GetProcAddress(hDInput, "DirectInputCreateEx");
	}

	return SysDirectInputCreateEx(hinst, dwVersion, refIID, ppDI, punkOuter);
}

//************************************
// Method:    DirectInputCreateW
// FullName:  DirectInputCreateW
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: HINSTANCE hinst
// Parameter: DWORD dwVersion
// Parameter: LPDIRECTINPUTW * ppDI
// Parameter: LPUNKNOWN punkOuter
//************************************
DINPUT_API HRESULT __stdcall DirectInputCreateW(HINSTANCE hinst, DWORD dwVersion, LPDIRECTINPUTW *ppDI, LPUNKNOWN punkOuter)
{
	// forward any DirectInputCreateW function call to windows native dinput.dll
	if (hDInput == NULL) {
		LPCTSTR lpEnvPath = _wgetenv(L"SystemRoot");
		TCHAR szDirectInputPath[MAX_PATH];
		_stprintf(szDirectInputPath, L"%s\\system32\\dinput.dll", lpEnvPath);
		hDInput = LoadLibraryW(szDirectInputPath);
	}
	if (hDInput == NULL) {
		return E_NOINTERFACE;
	}

	if (SysDirectInputCreateW == NULL) {
		SysDirectInputCreateW = (DIRECTINPUTCREATEW)GetProcAddress(hDInput, "DirectInputCreateW");
	}

	return SysDirectInputCreateW(hinst, dwVersion, ppDI, punkOuter);
}

//************************************
// Method:    ImmDisableIME
// FullName:  ImmDisableIME
// Access:    public
// Returns:   DINPUT_API BOOL __stdcall
// Qualifier:
// Parameter: __in DWORD idThread
//************************************
DINPUT_API BOOL __stdcall ImmDisableIME(__in  DWORD idThread)
{
	// forward any ImmDisableIME function call to windows native imm32.dll
	if (hImm32 == NULL) {
		LPCTSTR lpEnvPath = _wgetenv(L"SystemRoot");
		TCHAR szImm32Path[MAX_PATH];
		_stprintf(szImm32Path, L"%s\\system32\\imm32.dll", lpEnvPath);
		hImm32 = LoadLibraryW(szImm32Path);
	}
	if (hImm32 == NULL) {
		return E_NOINTERFACE;
	}

	if (SysImmDisableIme == NULL) {
		SysImmDisableIme = (IMMDISABLEIME)GetProcAddress(hImm32, "ImmDisableIME");
	}

	return SysImmDisableIme(idThread);
}

//************************************
// Method:    DllRegisterServer
// FullName:  DllRegisterServer
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: void
//************************************
DINPUT_API HRESULT __stdcall DllRegisterServer(void)
{
	// forward any DllRegisterServer function call to windows native dinput.dll
	if (hDInput == NULL) {
		LPCTSTR lpEnvPath = _wgetenv(L"SystemRoot");
		TCHAR szDirectInputPath[MAX_PATH];
		_stprintf(szDirectInputPath, L"%s\\system32\\dinput.dll", lpEnvPath);
		hDInput = LoadLibraryW(szDirectInputPath);
	}
	if (hDInput == NULL) {
		return E_NOINTERFACE;
	}

	if (SysDllRegisterServer == NULL) {
		SysDllRegisterServer = (DLLREGISTERSERVER)GetProcAddress(hDInput, "DllRegisterServer");
	}

	return SysDllRegisterServer();
}

//************************************
// Method:    DllUnregisterServer
// FullName:  DllUnregisterServer
// Access:    public
// Returns:   DINPUT_API HRESULT __stdcall
// Qualifier:
// Parameter: void
//************************************
DINPUT_API HRESULT __stdcall DllUnregisterServer(void)
{
	// forward any DllUnregisterServer function call to windows native dinput.dll
	if (hDInput == NULL) {
		LPCTSTR lpEnvPath = _wgetenv(L"SystemRoot");
		TCHAR szDirectInputPath[MAX_PATH];
		_stprintf(szDirectInputPath, L"%s\\system32\\dinput.dll", lpEnvPath);
		hDInput = LoadLibraryW(szDirectInputPath);
	}
	if (hDInput == NULL) {
		return E_NOINTERFACE;
	}

	if (SysDllUnregisterServer == NULL) {
		SysDllUnregisterServer = (DLLUNREGISTERSERVER)GetProcAddress(hDInput, "DllUnregisterServer");
	}

	return SysDllUnregisterServer();
}

//************************************
// Method:    SAS_CreateJavaVM
// FullName:  SAS_CreateJavaVM
// Access:    public
// Returns:   jint JNICALL
// Qualifier:
// Parameter: JavaVM * * p_vm
// Parameter: void * * p_env
// Parameter: void * vm_args
//************************************
jint JNICALL SAS_CreateJavaVM(JavaVM **p_vm, void **p_env, void *vm_args)
{
	// This function is being called when the JMP command has been successfully
	// injected into JVM.dll and an application calls JNI_CreateJavaVM().
	TRACE("Hooked \"SAS_CreateJavaVM\" function activated\r\n");
	if (g_hIL2GE == NULL && !IsServerExe()) {
		if (g_bIsOpenGL) {
			TCHAR szIL2GEFile[MAX_PATH];
			TRACE("OpenGL Mode detected, checking IL-2 Graphics Extender Availability...\r\n");
			LPCTSTR il2gepaths[] = IL2GE_PATHS;
			for (int i = 0; i < IL2GE_PATHS_NUM; i++) {
				ZeroMemory(szIL2GEFile, sizeof(szIL2GEFile));
				_tcscpy(szIL2GEFile, szCurDir);
				_tcscat(szIL2GEFile, il2gepaths[i]);
				TRACE(L"Trying %s\r\n", szIL2GEFile);
				if (FileExists(szIL2GEFile)) {
					TRACE(L"il2ge.dll found, loading library.\r\n", szIL2GEFile);
					g_hIL2GE = LoadLibrary(szIL2GEFile);
					if (g_hIL2GE != NULL) {
						TRACE("IL-2 Graphics Extender loaded successfully, trying to call Init() method...\r\n");
						Il2geInit = (IL2GE_Init)GetProcAddress(g_hIL2GE, "Init");
						if (Il2geInit == NULL) {
							TRACE("No Init() method available in IL-2 Graphics Extender.\r\n");
							break;
						}
						TRACE("Init() method found, calling it now.\r\n");
						Il2geInit();
						break;
					}
				}
			}
			if (g_hIL2GE == NULL) {
				TRACE("IL-2 Graphics Extender not available.\r\n");
				g_hIL2GE = (HMODULE)INVALID_HANDLE_VALUE;
			}
		}
		else {
			TRACE("DirectX Mode detected, skipping IL-2 Graphics Extender routines.\r\n");
		}
	}
	TRACE("Injecting JVM Parameters\r\n");

	// get access to JVM arguments being passed to the JNI_CreateJavaVM() function.
	JavaVMInitArgs *vmargs = (JavaVMInitArgs *)vm_args;
	// create a new set of  JVM arguments which holds our modified JVM parameters
	JavaVMInitArgs newArgs;
	newArgs.ignoreUnrecognized = JNI_FALSE; // never ignore unknown JVM options and parameters
	newArgs.version = vmargs->version;
	// allocate memory for our new JVM parameters
	JavaVMOption* newOptions = (JavaVMOption*)malloc((JvmOptions.size()) * sizeof(JavaVMOption));
	ZeroMemory(newOptions, sizeof(newOptions));

	// fill new JVM arguments array.
	try {
		int iCurOption = 0;

		for (const std::string &option : JvmOptions) {
			newOptions[iCurOption++].optionString = (char*)option.c_str();
		}

		newArgs.options = newOptions;
		newArgs.nOptions = iCurOption;
	}
	catch (...) {
		TRACE("Error in SAS_CreateJavaVM while generating new JavaVMInitArgs!\r\n");
		return JNI_ERR;
	}

	if (JniCreateJavaVM == NULL) {
		TRACE("Error in SAS_CreateJavaVM: Couldn't find original JNI_CreateJavaVM entry point!\r\n");
		return JNI_ERR;
	}

	// call back into original function JNI_CreateJavaVM() (with restored header)
	jint jRet = JniCreateJavaVM(p_vm, p_env, &newArgs);

	if (jRet == JNI_OK) {
		TRACE("Java Virtual Machine Initialization with additional parameters successful!\r\n");
		if (IsServerExe()) {
			printf("#\r\n");
			printf("# Java VM successfully initialized with addon parameters!\r\n");
			printf("# JVM is up and running, starting IL-2 Server now...\r\n");
			printf("#\r\n");
			printf("#########################################################\r\n");
		}
	}
	else {
		TRACE("Error in SAS_CreateJavaVM: JNI_CreateJavaVM return code = %d\r\n", jRet);
	}
	return jRet;
}

char* getClassName(byte *theClassBytes) {
	byte* classBytes = theClassBytes;
	classBytes += 8;
	unsigned short cpcnt = *(reinterpret_cast<unsigned short *>(classBytes));
	cpcnt = _byteswap_ushort(cpcnt);
	classBytes += 2;
	int classNameIndex = -1;
	char* retVal = NULL;

	// 1st loop, just skip across the constant pool, we need to get to the index for "this" class
	for (int i = 0; i < cpcnt - 1; i++) {
		byte tag = (byte)*classBytes;
		classBytes++;
		switch (tag) {
		case 1:
		{
			unsigned short wslen = *(reinterpret_cast<unsigned short *>(classBytes));
			wslen = _byteswap_ushort(wslen);
			classBytes += 2 + wslen;
			break;
		}
		case 5:
		case 6:
			classBytes += 8;
			i++;
			break;
		case 7:
		case 8:
		case 16:
			classBytes += 2;
			break;
		default:
			classBytes += 4;
			break;
		}
	}
	classBytes += 2;
	unsigned short cnidx = *(reinterpret_cast<unsigned short *>(classBytes));
	cnidx = _byteswap_ushort(cnidx);

	classBytes = theClassBytes + 10;

	// 2nd loop, try to get index of classname constant (needs "this" class index) and ClassName string constant (the latter won't work if they are in reversed order)
	for (int i = 0; i < cpcnt - 1; i++) {
		byte tag = (byte)*classBytes;
		classBytes++;
		switch (tag) {
		case 7:
		{
			unsigned short cidx = *(reinterpret_cast<unsigned short *>(classBytes));
			cidx = _byteswap_ushort(cidx);
			classBytes += 2;
			if (i == cnidx - 1) classNameIndex = cidx;
			break;
		}
		case 1:
		{
			unsigned short slen = *(reinterpret_cast<unsigned short *>(classBytes));
			slen = _byteswap_ushort(slen);
			classBytes += 2;
			if (i == classNameIndex - 1) {
				retVal = (char*)calloc(slen + 1, 1);
				memcpy(retVal, classBytes, slen);
			}
			classBytes += slen;
			break;
		}
		case 5:
		case 6:
			classBytes += 8;
			i++;
			break;
		case 8:
		case 16:
			classBytes += 2;
			break;
		default:
			classBytes += 4;
			break;
		}
	}

	// 3rd loop, finally get the ClassName even if it came after it's index.
	if (retVal == NULL) {
		classBytes = theClassBytes + 10;
		for (int i = 0; i < cpcnt - 1; i++) {
			byte tag = (byte)*classBytes;
			classBytes++;
			switch (tag) {
			case 1:
			{
				unsigned short slen = *(reinterpret_cast<unsigned short *>(classBytes));
				slen = _byteswap_ushort(slen);
				classBytes += 2;
				if (i == classNameIndex - 1) {
					retVal = (char*)calloc(slen + 1, 1);
					memcpy(retVal, classBytes, slen);
				}
				classBytes += slen;
				break;
			}
			case 5:
			case 6:
				classBytes += 8;
				i++;
				break;
			case 7:
			case 8:
			case 16:
				classBytes += 2;
				break;
			default:
				classBytes += 4;
				break;
			}
		}
	}

	return retVal;
}

//************************************
// Method:    SAS_DefineClass
// FullName:  SAS_DefineClass
// Access:    public
// Returns:   jclass JNICALL
// Qualifier:
// Parameter: JNIEnv * env
// Parameter: const char * name
// Parameter: jobject loader
// Parameter: const jbyte * buf
// Parameter: jsize bufLen
// Parameter: jobject pd
//************************************
jclass JNICALL SAS_DefineClass(JNIEnv *env, const char *name, jobject loader, const jbyte *buf, jsize bufLen, jobject jo)
{
	BOOL isEncrypted = TRUE;
	BOOL needsFreeClassName = FALSE;
	BOOL needsJniRelease = TRUE;
	DWORD* classHeader = (DWORD*)buf;
	if (*classHeader == 0xBEBAFECA) isEncrypted = FALSE;
	jclass theNewClass = JniDefineClass(env, name, loader, buf, bufLen, jo);
	char *className;
	jstring jname;
	jobject   classObj;
	jclass    theNewClassClass;
	if (isMain) {
		className = "com.maddox.il2.game.Main";
		isMain = FALSE;
		needsJniRelease = FALSE;
	} else {
		try {
			jmethodID mid_getClass = NULL;
			if (isEncrypted)
				mid_getClass = env->GetMethodID(theNewClass, "getClass", "()Ljava/lang/Class;");
			if (mid_getClass != NULL) {
				classObj = env->CallObjectMethod(theNewClass, mid_getClass);
				theNewClassClass = env->GetObjectClass(classObj);
				jmethodID mid_getName = env->GetMethodID(theNewClassClass, "getName", "()Ljava/lang/String;");
				jname = (jstring)env->CallObjectMethod(theNewClass, mid_getName);
				className = (char*)env->GetStringUTFChars(jname, JNI_FALSE);
			}
			else {
				className = getClassName((byte*)buf);
				char* current_pos = strchr(className, '/');
				for (char* p = current_pos; (current_pos = strchr(className, '/')) != NULL; *current_pos = '.');
				needsJniRelease = FALSE;
				needsFreeClassName = TRUE;
			}
		}
		catch (...) {
			TRACE("############################## ERROR RESOLVING CLASS NAME ##############################");
			return theNewClass;
		}
	}
	if (g_bDumpFileAccess) {
		AddClassesList(isEncrypted, (char*)className);
	}

	if (g_bDumpClassFiles) {
		std::string classDumpPath(cCurDir);
		std::string classNameToPath(className);
		std::replace(classNameToPath.begin(), classNameToPath.end(), '.', '\\');
		if (isEncrypted)
			classDumpPath = classDumpPath.append("classdump\\SFS\\").append(classNameToPath).append(".class");
		else
			classDumpPath = classDumpPath.append("classdump\\MOD\\").append(classNameToPath).append(".class");
		std::filesystem::path p(classDumpPath);
		if (!std::filesystem::exists(p.parent_path()))
			std::filesystem::create_directories(p.parent_path());
		std::ofstream(p, std::ios::binary).write((char*)buf, bufLen);
	}
	if (needsFreeClassName) free(className);
	else if (needsJniRelease) env->ReleaseStringUTFChars(jname, className);
	return theNewClass;
}

int __cdecl SAS_SFS_mount(const char *sfs, int i, char c) {
	int retVal=Il2fbSFS_mount(sfs, i, c);
	TRACE("SFS_mount(%s, %d, %d) = %d\r\n", sfs, i, c, retVal);
	return retVal;
}

//************************************
// Method:    ApplyMutexSetting
// FullName:  ApplyMutexSetting
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ApplyMutexSetting()
{
	// This function is called by modified entry point header from Stock il2fb.exe
	// This is necessary since stock il2fb.exe is UPX-compressed, so we have to wait
	// until the executable has been decompressed in memory and step in before it's
	// being executed then.
	DWORD dwOldProtect = 0;
	DWORD dwNumRead = 0;
	DWORD dwProcRights = PROC_FULL_RIGHTS;
	DWORD dwCurProc = GetCurrentProcessId();
	HANDLE hCurProc = OpenProcess(dwProcRights, FALSE, dwCurProc);

	if (hCurProc != NULL) {
		int iIl2fbMutex = 0x00422068;

		try {
			if (NULL != VirtualProtectEx(hCurProc, (LPVOID)iIl2fbMutex, 15, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
				ZeroMemory((LPVOID)iIl2fbMutex, 15); // disable Mutex
				VirtualProtectEx(hCurProc, (LPVOID)iIl2fbMutex, 9, dwOldProtect, NULL);
			}
			else {
				TRACE("Error in ApplyMutexSetting: Couldn't get access to mutex string memory area!\r\n");
			}
		}
		catch (...) {
			TRACE("Error in ApplyMutexSetting while overwriting mutex string!\r\n");
		}

		CloseHandle(hCurProc);
	}
	else {
		TRACE("Error in ApplyMutexSetting: Couldn't aquire current process handle!\r\n");
	}

	__asm { // jump back to original entry point at process address 0x4128E9
		mov eax, 0x004128E9
		jmp eax
	}
	return;
}

//************************************
// Method:    IsServerExe
// FullName:  IsServerExe
// Access:    public
// Returns:   BOOL
// Qualifier:
//************************************
BOOL IsServerExe()
{
	if (g_isServer == 0)
		return FALSE;
	else if (g_isServer == 1)
		return TRUE;
	// Check if the process executable is running IL-2 Server or Client
	DWORD dwOldProtect = 0;
	DWORD dwNumRead = 0;
	DWORD dwProcRights = DELETE | READ_CONTROL | SYNCHRONIZE | WRITE_DAC | WRITE_OWNER
		| PROCESS_TERMINATE | PROCESS_CREATE_THREAD | PROCESS_SET_SESSIONID | PROCESS_VM_OPERATION | PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_DUP_HANDLE | PROCESS_CREATE_PROCESS | PROCESS_SET_QUOTA | PROCESS_SET_INFORMATION | PROCESS_QUERY_INFORMATION | PROCESS_SUSPEND_RESUME;
	DWORD dwCurProc = GetCurrentProcessId();
	HANDLE hCurProc = OpenProcess(dwProcRights, FALSE, dwCurProc);
	BOOL bIsServer = FALSE;
	g_isServer = 0;

	try {
		int iIl2ServerID = 0x0041EBA8;
		char* cFilesServer = "filesserver.sfs"; // this file reference is present in IL-2 Server executable only.

		if (NULL != VirtualProtectEx(hCurProc, (LPVOID)iIl2ServerID, 15, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
			if (memcmp((LPVOID)iIl2ServerID, cFilesServer, 15) == 0) {
				bIsServer = TRUE;
				g_isServer = 1;
			}

			VirtualProtectEx(hCurProc, (LPVOID)iIl2ServerID, 9, dwOldProtect, NULL);
		}
		else {
			TRACE("Error in AdjustJvmParams: Couldn't get access to modded il2fb.exe server ID memory area!\r\n");
		}
	}
	catch (...) {
		TRACE("Error in AdjustJvmParams: Couldn't check modded il2fb.exe server ID!\r\n");
	}

	CloseHandle(hCurProc);
	return bIsServer;
}

//************************************
// Method:    AdjustJvmParams
// FullName:  AdjustJvmParams
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void AdjustJvmParams()
{
	// This function is called when our DINPUT.dll is getting linked.
	// The process of IL-2 exists at this time so we can hijack a set of memory
	// areas and inject code, overwrite memory areas or shift pointers around
	GetParams();

	Sleep(0);
	if (!IsServerExe()) {
		if (!StartWatchdog()) {
			TRACE("Watchdog Start failed.");
		}
	}
	StartPipeLogger();

	// Get original function pointer of Java VM's JNI_CreateJavaVM() function.
	if (hJvm == NULL) {
		TCHAR szJvmPath[MAX_PATH];
		_stprintf(szJvmPath, L"%sbin\\hotspot\\jvm.dll", szCurDir);
		hJvm = LoadLibraryW(szJvmPath);
	}
	JniCreateJavaVM = (ORI_CreateJavaVM)GetProcAddress(hJvm, "JNI_CreateJavaVM");
	if (g_bDumpFileAccess || g_bDumpClassFiles || g_bDumpOtherFiles) {
		JniDefineClass = (ORI_DefineClass)GetProcAddress(hJvm, "_JVM_DefineClass@24");
	}

	if (JniCreateJavaVM == NULL) {
		TRACE("Error in AdjustJvmParams: Couldn't find original JNI_CreateJavaVM entry point!\r\n");
		return;
	}

	Il2fbSFS_mount = (ORI_SFS_mount)GetProcAddress(GetModuleHandle(NULL), "SFS_mount");

	// When running an IL-2 server, show some output string on command line
	if (IsServerExe()) {
		printf("#########################################################\r\n");
		printf("#\r\n");
		printf("# IL-2 Server Executable %s %s\r\n", G_PRODUCT_VERSION, G_COPYRIGHT);
		printf("#\r\n");
	}

	DetourTransactionBegin();
	DetourUpdateThread(GetCurrentThread());
	DetourAttach(&(PVOID&)JniCreateJavaVM, SAS_CreateJavaVM);
	if (g_bDumpSFSAccess) DetourAttach(&(PVOID&)Il2fbSFS_mount, SAS_SFS_mount);
	if (g_bDumpFileAccess || g_bDumpClassFiles || g_bDumpOtherFiles) {
		DetourAttach(&(PVOID&)JniDefineClass, SAS_DefineClass);
		SYSTEMTIME stSystemTime;
		/////////////////////////////////////////////////////////////////////////
		// Generate Time Stamp
		/////////////////////////////////////////////////////////////////////////
		GetLocalTime(&stSystemTime);
		_stprintf(ClassesRuntimeFileName, CLASSES_RUNTIME_FILE_NAME,
			szCurDir,
			stSystemTime.wYear,
			stSystemTime.wMonth,
			stSystemTime.wDay);
		_stprintf(ClassesSortedFileName, CLASSES_SORTED_FILE_NAME,
			szCurDir,
			stSystemTime.wYear,
			stSystemTime.wMonth,
			stSystemTime.wDay);
		_stprintf(ClassesSummaryFileName, CLASSES_SUMMARY_FILE_NAME,
			szCurDir,
			stSystemTime.wYear,
			stSystemTime.wMonth,
			stSystemTime.wDay);
	}
	DetourTransactionCommit();

	if (g_bDumpFileAccess) {
		InitClassesList();
		ResetFile(ClassesRuntimeFileName);
	}

	if (bDisableMutex) {  // strip mutex string from process
		DWORD dwOldProtect = 0;
		DWORD dwProcRights = DELETE | READ_CONTROL | SYNCHRONIZE | WRITE_DAC | WRITE_OWNER
			| PROCESS_TERMINATE | PROCESS_CREATE_THREAD | PROCESS_SET_SESSIONID | PROCESS_VM_OPERATION | PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_DUP_HANDLE | PROCESS_CREATE_PROCESS | PROCESS_SET_QUOTA | PROCESS_SET_INFORMATION | PROCESS_QUERY_INFORMATION | PROCESS_SUSPEND_RESUME;
		DWORD dwCurProc = GetCurrentProcessId();
		HANDLE hCurProc = OpenProcess(dwProcRights, FALSE, dwCurProc);

		BOOL bIsStockExe = FALSE;
		BYTE checkStock[] = { 0x32, 0x2E, 0x30, 0x30, 0x00 }; // UPX 2.00 identification string in Stock il2fb.exe
		int iIl2fbUpxCheck = 0x004003DB; // Address of UPX 2.00 ID string prior to decompression

		try {
			if (NULL != VirtualProtectEx(hCurProc, (LPVOID)iIl2fbUpxCheck, 5, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
				if (memcmp((LPVOID)iIl2fbUpxCheck, checkStock, 5) == 0) {
					bIsStockExe = TRUE;
				}

				VirtualProtectEx(hCurProc, (LPVOID)iIl2fbUpxCheck, 5, dwOldProtect, NULL);
			}
			else {
				TRACE("Error in AdjustJvmParams: Couldn't get access to il2fb.exe Version Check memory area!\r\n");
			}

			if (bIsStockExe) {  // disable Mutex for Stock il2fb.exe
				try {
					int iIl2fbUpxDecompressEnd = 0x01EA16EB;

					if (NULL != VirtualProtectEx(hCurProc, (LPVOID)iIl2fbUpxDecompressEnd, 9, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
						*(DWORD *)iIl2fbUpxDecompressEnd = 0x90909090; // fill with NOPs
						*(DWORD *)(iIl2fbUpxDecompressEnd + 4) = 0x90909090; // fill with NOPs
						*(BYTE *)(iIl2fbUpxDecompressEnd + 8) = 0x90; // fill with NOPs
						*(BYTE *)iIl2fbUpxDecompressEnd = 0xE9; // near JMP
						*(DWORD *)(iIl2fbUpxDecompressEnd + 1) = (DWORD)ApplyMutexSetting - 0x01EA16EB - 5; // run ApplyMutexSetting() after decompressing
						VirtualProtectEx(hCurProc, (LPVOID)iIl2fbUpxDecompressEnd, 9, dwOldProtect, NULL);
					}
					else {
						TRACE("Error in AdjustJvmParams: Couldn't get access to Stock il2fb.exe entry point memory area!\r\n");
					}
				}
				catch (...) {
					TRACE("Error in AdjustJvmParams: Couldn't inject code into Stock il2fb.exe entry point memory area!\r\n");
				}
			}
			else { // disable Mutex for mod enabled il2fb.exe
				if (!IsServerExe()) {
					try {
						int iIl2fbMutex = 0x0042206C;

						if (NULL != VirtualProtectEx(hCurProc, (LPVOID)iIl2fbMutex, 15, PAGE_EXECUTE_READWRITE, &dwOldProtect)) {
							ZeroMemory((LPVOID)iIl2fbMutex, 15); // disable Mutex
							VirtualProtectEx(hCurProc, (LPVOID)iIl2fbMutex, 9, dwOldProtect, NULL);
						}
						else {
							TRACE("Error in AdjustJvmParams: Couldn't get access to modded il2fb.exe mutex string memory area!\r\n");
						}
					}
					catch (...) {
						TRACE("Error in AdjustJvmParams: Couldn't erase modded il2fb.exe mutex string!\r\n");
					}
				}
			}
		}
		catch (...) {
			TRACE("Error in AdjustJvmParams: Couldn't check il2fb.exe Version!\r\n");
		}
		CloseHandle(hCurProc);

	}
}

//************************************
// Method:    AddJvmOption
// FullName:  AddJvmOption
// Access:    public
// Returns:   void
// Qualifier:
// Parameter: LPCSTR lpOptionString
//************************************
void AddJvmOption(LPCSTR lpOptionString)
{
	std::string newOption(lpOptionString);
	std::pair optionInsert = JvmOptions.emplace(newOption);
	if (!optionInsert.second) {
		JvmOptions.erase(optionInsert.first);
		JvmOptions.emplace(newOption);
	}

	TRACE("Added JVM Option: %s\r\n", newOption.c_str());
}

//************************************
// Method:    ReadSelectorSettings
// FullName:  ReadSelectorSettings
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ReadSelectorSettings()
{
	bDisableMutex = (GetPrivateProfileInt(L"Settings", L"MultipleInstances", 0, szIniFile) != 0);
	int iSelectorRamSize = GetPrivateProfileInt(L"Settings", L"RamSize", 0, szIniFile);
	int iSelectorMemStrategy = GetPrivateProfileInt(L"Settings", L"MemoryStrategy", 0, szIniFile);
	g_iSplashScreenMode = GetPrivateProfileInt(L"Settings", L"SplashScreenMode", 0, szIniFile);
	int iDumpMode = GetPrivateProfileInt(L"Settings", L"DumpMode", 0, szIniFile);
	if (iDumpMode & 0x01) g_bDumpFileAccess = TRUE;
	if (iDumpMode & 0x02) g_bDumpClassFiles = TRUE;
	if (iDumpMode & 0x04) g_bDumpOtherFiles = TRUE;
	if (iDumpMode & 0x08) g_bDumpSFSAccess = TRUE;
	g_bInstantDump = GetPrivateProfileInt(L"Settings", L"InstantDump", 0, szIniFile) != 0;
	ZeroMemory(szBuffer, sizeof(szBuffer));
	if (GetPrivateProfileString(L"Settings", L"DumpSeparatorChar", L" ", szBuffer, sizeof(szBuffer) / sizeof(TCHAR), szIniFile) > 0) {
		if (_tcslen(szBuffer) == 1) {
			ZeroMemory(cBuffer, sizeof(cBuffer));
			wcstombs(cBuffer, szBuffer, sizeof(cBuffer));
			g_cDumpSeparator = cBuffer[0];
		}
	}
	else {
		g_cDumpSeparator = ' ';
	}

	g_iDebugMode = GetPrivateProfileInt(L"Settings", L"DebugMode", 0, szIniFile);

	if (iSelectorRamSize != 0) {

		TRACE("Applying JVM Memory Settings from IL-2 Selector...\r\n");
		int iStackSize = (iSelectorRamSize * 1024) / XSS_DIVIDER;
		int iPermSize = iSelectorRamSize / PERM_DIVIDER;

		switch (iSelectorMemStrategy) {
		case MEM_STRATEGY_CONSERVATIVE:
			iStackSize /= 2;
			iPermSize /= 2;
			break;

		case MEM_STRATEGY_HEAPONLY:
			iStackSize = 0;
			iPermSize = 0;
			break;

		default:
			break;
		}

		int iDynamicSize = iSelectorRamSize - (iStackSize / 1024) - iPermSize;
		LPSTR lpBuf = (LPSTR)malloc(sizeof(char) * 16);
		ZeroMemory(lpBuf, sizeof(lpBuf));
		sprintf(lpBuf, "-Xms%dM", iDynamicSize);
		AddJvmOption(lpBuf);
		ZeroMemory(lpBuf, sizeof(lpBuf));
		sprintf(lpBuf, "-Xmx%dM", iDynamicSize);
		AddJvmOption(lpBuf);

		if (iSelectorMemStrategy == MEM_STRATEGY_HEAPONLY) {
			// TRACE("ReadSelectorSettings()---1\r\n");
			return;
		}

		ZeroMemory(lpBuf, sizeof(lpBuf));
		sprintf(lpBuf, "-Xss%dK", iStackSize);
		AddJvmOption(lpBuf);
		ZeroMemory(lpBuf, sizeof(lpBuf));
		sprintf(lpBuf, "-XX:PermSize=%dM", iPermSize);
		AddJvmOption(lpBuf);
		ZeroMemory(lpBuf, sizeof(lpBuf));
		sprintf(lpBuf, "-XX:MaxPermSize=%dM", iPermSize);
		AddJvmOption(lpBuf);
	}
}

//************************************
// Method:    ReadConfSettings
// FullName:  ReadConfSettings
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ReadConfSettings()
{
	if (GetPrivateProfileString(L"GLPROVIDER", L"GL", L"dx8wrap.dll", szBuffer, MAX_PATH, szConfIniFile)) {
		LPTSTR szGlProvider = szBuffer;
		_tcstrim(szGlProvider);
		if (_tcsicmp(szGlProvider, L"opengl32.dll") == 0) g_bIsOpenGL = TRUE;
	}
}

//************************************
// Method:    ReadJvmOptions
// FullName:  ReadJvmOptions
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void ReadJvmOptions()
{
	TCHAR szJvmOptionsInFile[0xFFFF];
	ZeroMemory(szJvmOptionsInFile, sizeof(szJvmOptionsInFile));
	TCHAR szJvmOptions[0xFFFF];
	ZeroMemory(szJvmOptions, sizeof(szJvmOptions));
	char cBuf[MAX_PATH];
	GetPrivateProfileSection(L"JVM", szJvmOptionsInFile, 0xFFFF, szIniFile);
	LPTSTR lpJvmOptionToken = szJvmOptionsInFile;

	while (_tcslen(lpJvmOptionToken) != 0) {
		ZeroMemory(cBuf, sizeof(cBuf));
		wcstombs(cBuf, lpJvmOptionToken, MAX_PATH);
		AddJvmOption(cBuf);
		lpJvmOptionToken += _tcslen(lpJvmOptionToken) + 1;
	}
}

//************************************
// Method:    IsInJvmOptions
// FullName:  IsInJvmOptions
// Access:    public
// Returns:   int
// Qualifier:
// Parameter: LPCSTR lpSearch
// Parameter: unsigned int iStartIndex
//************************************
bool IsInJvmOptions(LPCSTR lpSearch)
{
	std::string searchString(lpSearch);
	std::transform(searchString.begin(), searchString.end(), searchString.begin(), ::tolower);
	size_t searchStringLen = searchString.length();
	for (const std::string &jvmOption : JvmOptions) {
		std::string jvmOptionLower(jvmOption);
		std::transform(jvmOptionLower.begin(), jvmOptionLower.end(), jvmOptionLower.begin(), ::tolower);
		if (searchStringLen > jvmOption.length()) continue;
		if (jvmOptionLower._Starts_with(searchString)) {
			return true;
		}
	}
	return false;
}

//************************************
// Method:    AddMandatoryJvmOptions
// FullName:  AddMandatoryJvmOptions
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void AddMandatoryJvmOptions()
{
	TRACE("Checking mandatory JVM Options...\r\n");

	if (g_iDebugMode == 1) {
		TRACE("DebugMode=1\r\n");
		AddJvmOption("-Djava.class.path=.");
		AddJvmOption("-Xverify:none");
		AddJvmOption("-Xcomp");
	}
	else {
		if (!IsInJvmOptions("-Djava.class.path")) {
			AddJvmOption("-Djava.class.path=.");
		}

		if (!IsInJvmOptions("-Xverify")) {
			AddJvmOption("-Xverify:none");
		}

		if (!IsInJvmOptions("-Xcomp")) {
			AddJvmOption("-Xcomp");
		}
	}
}

//************************************
// Method:    GetParams
// FullName:  GetParams
// Access:    public
// Returns:   void
// Qualifier:
//************************************
void GetParams()
{
	JvmOptions.clear();
	ReadSelectorSettings();
	ReadConfSettings();
	ReadJvmOptions();
	AddMandatoryJvmOptions();
}

//************************************
// Method:    StartWatchdog
// FullName:  StartWatchdog
// Access:    public 
// Returns:   BOOL
// Qualifier:
//************************************
BOOL StartWatchdog()
{
	STARTUPINFO si;
	PROCESS_INFORMATION pi;
	TCHAR szWatchdogFile[MAX_PATH];
	TCHAR szWatchdogParams[MAX_PATH];
	ZeroMemory(&pi, sizeof(pi));
	ZeroMemory(szWatchdogFile, sizeof(szWatchdogFile));
	ZeroMemory(szWatchdogParams, sizeof(szWatchdogParams));
	ZeroMemory(&si, sizeof(si));
	si.cb = sizeof(si);

	_stprintf(szWatchdogParams, L"%d", g_iSplashScreenMode);

	_tcscpy(szWatchdogFile, szCurDir);
	_tcscat(szWatchdogFile, L"bin\\selector\\basefiles\\IL-2 Watchdog.exe");
	TRACE("IL-2 Process ID = %08X\r\n", GetCurrentProcessId());
	TRACE(L"Starting Watchdog at %s %s\r\n", szWatchdogFile, szWatchdogParams);

	if (!CreateProcess(szWatchdogFile, szWatchdogParams, NULL,
		NULL, FALSE,
		CREATE_NEW_CONSOLE | CREATE_BREAKAWAY_FROM_JOB | CREATE_SUSPENDED,
		NULL, szCurDir, &si, &pi)) {
		TRACE("Watchdog Process not created, last Error: %08X\r\n", GetLastError());
		return FALSE;
	}

	if (g_hJobObject == NULL) g_hJobObject = CreateJobObject(NULL, NULL);
	if (g_hJobObject == NULL) {
		TRACE("Job Object not created\r\n");
		TerminateProcess(pi.hProcess, -1);
		return FALSE;
	}
	JOBOBJECT_EXTENDED_LIMIT_INFORMATION jeli = { 0 };
	jeli.BasicLimitInformation.LimitFlags = JOB_OBJECT_LIMIT_KILL_ON_JOB_CLOSE;
	if (SetInformationJobObject(g_hJobObject, JobObjectExtendedLimitInformation, &jeli, sizeof(jeli)) == FALSE) {
		TRACE("Job Object extended limit information not set\r\n");
		TerminateProcess(pi.hProcess, -1);
		return FALSE;
	}

	if (AssignProcessToJobObject(g_hJobObject, pi.hProcess) == FALSE) {
		TRACE("Watchdog Process not assigned to Job Object\r\n");
		TerminateProcess(pi.hProcess, -1);
		return FALSE;
	}

	ResumeThread(pi.hThread);

	CloseHandle(pi.hThread);
	CloseHandle(pi.hProcess);
	TRACE("Watchdog process started.\r\n");
	Sleep(0);
	return TRUE;
}

//************************************
// Method:    StartPipeLogger
// FullName:  StartPipeLogger
// Access:    public 
// Returns:   BOOL
// Qualifier:
//************************************
BOOL StartPipeLogger()
{
	STARTUPINFO si;
	PROCESS_INFORMATION pi;
	ZeroMemory(&si, sizeof(si));
	si.cb = sizeof(si);
	ZeroMemory(&pi, sizeof(pi));
	TCHAR szPipeLoggerFile[MAX_PATH];
	ZeroMemory(szPipeLoggerFile, sizeof(szPipeLoggerFile));
	_tcscpy(szPipeLoggerFile, szCurDir);
	_tcscat(szPipeLoggerFile, L"bin\\selector\\basefiles\\PipeLogger.exe");
	TRACE(L"Starting Pipe Logger at %s\r\n", szPipeLoggerFile);
	if (!CreateProcess(szPipeLoggerFile, NULL, NULL,
		NULL, TRUE,
		CREATE_NEW_CONSOLE,
		NULL, szCurDir, &si, &pi)) {
		TRACE("PipeLogger Process not created, last Error: %08X\r\n", GetLastError());
		return FALSE;
	}

	CloseHandle(pi.hThread);
	CloseHandle(pi.hProcess);
	TRACE("PipeLogger process started.\r\n");
	Sleep(0);
	return TRUE;
}

void versionString(char* buffer, int numToken, ...) {
	va_list argptr;
	va_start(argptr, numToken);
	sprintf(buffer, "%d.%d.%d.%d", va_arg(argptr, int), va_arg(argptr, int), va_arg(argptr, int), va_arg(argptr, int));
	va_end(argptr);
}

BOOL FileExists(LPCTSTR szPath) {
	DWORD dwAttrib = GetFileAttributes(szPath);
	return (dwAttrib != INVALID_FILE_ATTRIBUTES &&
		!(dwAttrib & FILE_ATTRIBUTE_DIRECTORY));
}

void _tcstrim(LPTSTR str) {
	int start = 0; // number of leading spaces
	LPTSTR buffer = str;
	while (*str && *str++ == L' ') ++start;
	while (*str++); // move to end of string
	int end = str - buffer - 1;
	while (end > 0 && buffer[end - 1] == L' ') --end; // backup over trailing spaces
	buffer[end] = 0; // remove trailing spaces
	if (end <= start || start == 0) return; // exit if no leading spaces or string is now empty
	str = buffer + start;
	while ((*buffer++ = *str++));  // remove leading spaces: K&R
}

JNIEXPORT jstring JNICALL Java_com_maddox_sas1946_il2_util_BaseGameVersion_getSelectorInfo(JNIEnv *env, jobject obj, jint infoRequested) {
	char buffer[128];
	ZeroMemory(buffer, sizeof(buffer));
	switch (infoRequested) {
	case SELECTOR_INFO_FILE_VERSION:
		sprintf(buffer, G_FILE_VERSION);
		break;
	case SELECTOR_INFO_PRODUCT_VERSION:
		sprintf(buffer, G_PRODUCT_VERSION);
		break;
	case SELECTOR_INFO_FILEVERSION:
		versionString(buffer, G_FILEVERSION);
		break;
	case SELECTOR_INFO_PRODUCTVERSION:
		versionString(buffer, G_PRODUCTVERSION);
		break;
	case SELECTOR_INFO_SPECIAL_BUILD:
		sprintf(buffer, G_SPECIAL_BUILD);
		break;
	case SELECTOR_INFO_COPYRIGHT:
		sprintf(buffer, G_COPYRIGHT);
		break;
	default:
		sprintf(buffer, "unknown Selector Info requested");
		break;
	}
	return env->NewStringUTF(buffer);
}
