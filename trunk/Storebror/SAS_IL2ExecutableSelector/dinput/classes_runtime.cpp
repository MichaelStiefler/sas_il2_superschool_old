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

#include "stdafx.h"
#include "classes_runtime.h"
#include "SFS_Tools.h"
#include "globals.h"
#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <share.h>
#include <tchar.h>
#include <vector>
#include <set>
#include <algorithm>
#include <string>
#include <thread>
#include <queue>
#include <condition_variable>
#include <mutex>

#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

void ClassesRuntimeWriter()
{
	std::unique_lock<std::mutex> lock(m);
	while (!classesRuntimeWriterThreadDone) {
		while (!classesRuntimeWriterThreadNotified) {  // loop to avoid spurious wakeups
			cond_var.wait(lock);
		}
		if (classesRuntimeLog == NULL) {
			while (true) {
				classesRuntimeLog = _fsopen(cClassesRuntimeLogFileName, "ab", _SH_DENYWR);

				if (classesRuntimeLog != NULL) {
					break;
				}

				Sleep(rand() % 100);
			}
		}
		while (!pendingClassesRuntimeOutput.empty()) {
			fprintf(classesRuntimeLog, pendingClassesRuntimeOutput.front().c_str());
			//std::cout << "consuming " << pendingClassesRuntimeOutput.front() << '\n';
			pendingClassesRuntimeOutput.pop();
		}
		fflush(classesRuntimeLog);
		fclose(classesRuntimeLog);
		classesRuntimeLog = NULL;
		classesRuntimeWriterThreadNotified = false;
	}
}

void WaitForClassesRuntimeWriter()
{
	classesRuntimeWriterThreadDone = true;
	classesRuntimeWriterThreadNotified = true;
	cond_var.notify_one();
	if (ClassesRuntimeWriterThread.joinable()) ClassesRuntimeWriterThread.join();
}

UINT StartRuntimeWriterThread(LPVOID pParam)
{
	ClassesRuntimeWriterThread = std::thread(ClassesRuntimeWriter);
	return 0;
}


void ResetFile(LPCTSTR lpFileName) {
	FILE * file;

	while (true) {
		file = _tfsopen(lpFileName, L"wb", _SH_DENYWR);

		if (file != NULL) {
			break;
		}

		Sleep(rand() % 100);
	}

	if (_tcscmp(lpFileName, ClassesRuntimeFileName) == 0) {
		ZeroMemory(cClassesRuntimeLogFileName, sizeof(cClassesRuntimeLogFileName));
		wcstombs(cClassesRuntimeLogFileName, ClassesRuntimeFileName, _tcslen(ClassesRuntimeFileName));
		ClassesRuntimeFileName[_tcslen(ClassesRuntimeFileName)] = '\0';
		fprintf(file, "TIMESTAMP%cHASHCODE%cSFS%cPACKAGE%cCLASS\r\n", g_cDumpSeparator, g_cDumpSeparator, g_cDumpSeparator, g_cDumpSeparator);
		if (!g_bInstantDump) {
			RunWorker(StartRuntimeWriterThread);
		}
	}

	fflush(file);
	fclose(file);
}

void InitClassesList() {
	ClassesList.clear();
}

std::string packageNameFromFullQualifiedClassName(LPCSTR lpClassName) {
	return std::string(lpClassName).substr(0, std::string(lpClassName).find_last_of('.'));
}

std::string classNameFromFullQualifiedClassName(LPCSTR lpClassName) {
	return std::string(lpClassName).substr(std::string(lpClassName).find_last_of('.') + 1);
}

__int64 hashFromFullQualifiedClassName(LPCSTR lpClassName) {
	std::string hashString = "sdw" + std::string(lpClassName) + "cwc2w9e";
	hashString = "cod/" + std::to_string(IntFN(0, hashString.c_str()));
	return LongFN(0, hashString.c_str());
}

void AddClassesList(BOOL isEncrypted, LPSTR lpClassName) {
	ClassListItem *cli = new ClassListItem(isEncrypted, lpClassName);
	if (ClassesList.insert(cli).second != true) return;
	ZeroMemory(cBuf, sizeof(cBuf));
	sprintf(cBuf, "%016I64X%c%s%c%s%c%s",
		hashFromFullQualifiedClassName(lpClassName),
		g_cDumpSeparator,
		isEncrypted ? "SFS" : "MOD",
		g_cDumpSeparator,
		packageNameFromFullQualifiedClassName(lpClassName).c_str(),
		g_cDumpSeparator,
		classNameFromFullQualifiedClassName(lpClassName).c_str());
	if (g_bInstantDump)
		LogClassAccessAtRuntime(cBuf);
	else
		LogClassAccessAtRuntimeAsync(cBuf);
}

void PrintClassesList() {
	int lenClassesSortedFileName = _tcslen(ClassesSortedFileName);
	CHAR* cClassesSortedFileName = (CHAR*)malloc((lenClassesSortedFileName + 1) * sizeof(TCHAR));
	ZeroMemory(cClassesSortedFileName, sizeof(lenClassesSortedFileName));
	wcstombs(cClassesSortedFileName, ClassesSortedFileName, lenClassesSortedFileName);
	cClassesSortedFileName[lenClassesSortedFileName] = '\0';
	int lenClassesSummaryFileName = _tcslen(ClassesSummaryFileName);
	CHAR* cClassesSummaryFileName = (CHAR*)malloc((lenClassesSummaryFileName + 1) * sizeof(TCHAR));
	ZeroMemory(cClassesSummaryFileName, sizeof(cClassesSummaryFileName));
	wcstombs(cClassesSummaryFileName, ClassesSummaryFileName, lenClassesSummaryFileName);
	cClassesSummaryFileName[lenClassesSummaryFileName] = '\0';

	FILE *classesSummaryFile;
	while (true) {
		classesSummaryFile = _fsopen(cClassesSummaryFileName, "wb", _SH_DENYWR);

		if (classesSummaryFile != NULL) {
			break;
		}

		Sleep(rand() % 100);
	}

	SYSTEMTIME stSystemTime;
	GetLocalTime(&stSystemTime);

	int numClasses = 0;
	int numSubClasses = 0;
	int modClasses = 0;
	int sfsClasses = 0;

	for (ClassListItem *cli : ClassesList) {
		if (cli->isEncrypted) sfsClasses++; else modClasses++;
		if (cli->className.find('$') == std::string::npos) numClasses++; else numSubClasses++;
	}


	fprintf(classesSummaryFile, " ************************************\r\n");
	fprintf(classesSummaryFile, "*                                    *\r\n");
	fprintf(classesSummaryFile, "*       List of loaded Classes       *\r\n");
	fprintf(classesSummaryFile, "*                                    *\r\n");
	fprintf(classesSummaryFile, "*      %*d Classes in total       *\r\n", 6, ClassesList.size());
	fprintf(classesSummaryFile, "*      %*d Classes                *\r\n", 6, numClasses);
	fprintf(classesSummaryFile, "*      %*d Inner Classes          *\r\n", 6, numSubClasses);
	fprintf(classesSummaryFile, "*      %*d Stock SFS Classes      *\r\n", 6, sfsClasses);
	fprintf(classesSummaryFile, "*      %*d Modded Classes         *\r\n", 6, modClasses);
	fprintf(classesSummaryFile, "*                                    *\r\n");
	fprintf(classesSummaryFile, "* Generated: %04d-%02d-%02d %02d:%02d:%02d:%03d *\r\n",
		stSystemTime.wYear,
		stSystemTime.wMonth,
		stSystemTime.wDay,
		stSystemTime.wHour,
		stSystemTime.wMinute,
		stSystemTime.wSecond,
		stSystemTime.wMilliseconds);
	fprintf(classesSummaryFile, "*                                    *\r\n");
	fprintf(classesSummaryFile, " ************************************\r\n");
	fprintf(classesSummaryFile, "\r\n");
	fflush(classesSummaryFile);
	fclose(classesSummaryFile);
	free(cClassesSummaryFileName);

	FILE *classesSortedFile;
	while (true) {
		classesSortedFile = _fsopen(cClassesSortedFileName, "wb", _SH_DENYWR);

		if (classesSortedFile != NULL) {
			break;
		}

		Sleep(rand() % 100);
	}

	fprintf(classesSortedFile, "SFS%cHASHCODE%cPACKAGE%cCLASS\r\n", g_cDumpSeparator, g_cDumpSeparator, g_cDumpSeparator);

	for (ClassListItem *cli : ClassesList) {
		fprintf(classesSortedFile, "%s%c%016I64X%c%s%c%s\r\n",
			cli->isEncrypted ? "SFS" : "MOD",
			g_cDumpSeparator,
			hashFromFullQualifiedClassName(cli->className.c_str()),
			g_cDumpSeparator,
			packageNameFromFullQualifiedClassName(cli->className.c_str()).c_str(),
			g_cDumpSeparator,
			classNameFromFullQualifiedClassName(cli->className.c_str()).c_str());
	}

	fflush(classesSortedFile);
	fclose(classesSortedFile);
	free(cClassesSortedFileName);
}

bool LogClassAccessAtRuntime(char *format, ...)
{
	va_list args;
	va_start(args, format);
	bool retVal = LogClassAccessAtRuntime(format, args);
	va_end(args);
	return retVal;
}

bool LogClassAccessAtRuntimeAsync(char *format, ...)
{
	va_list args;
	va_start(args, format);
	bool retVal = LogClassAccessAtRuntimeAsync(format, args);
	va_end(args);
	return retVal;
}

bool LogClassAccessAtRuntime(char *format, va_list args)
{
    int len = _vscprintf(format, args)   // _vscprintf doesn't count
              + 1; // terminating '\0'
    CHAR* buffer = (CHAR*)calloc(len, sizeof(CHAR));
    _vsnprintf(buffer, len - 1, format, args);
    buffer[len - 1] = '\0';

    if(strlen(cClassesRuntimeLogFileName) != 0) {
        SYSTEMTIME stSystemTime;
        /////////////////////////////////////////////////////////////////////////
        // Generate Time Stamp
        /////////////////////////////////////////////////////////////////////////
        GetLocalTime(&stSystemTime);

		if (classesRuntimeLog == NULL) {
			while (true) {
				classesRuntimeLog = _fsopen(cClassesRuntimeLogFileName, "ab", _SH_DENYWR);

				if (classesRuntimeLog != NULL) {
					break;
				}

				Sleep(rand() % 100);
			}
		}

        fprintf(classesRuntimeLog, "%02d:%02d:%02d:%03d%c%s\r\n",
                stSystemTime.wHour,
                stSystemTime.wMinute,
                stSystemTime.wSecond,
                stSystemTime.wMilliseconds,
				g_cDumpSeparator,
			    buffer);
		fflush(classesRuntimeLog);
		fclose(classesRuntimeLog);
		classesRuntimeLog = NULL;
    }

    free(buffer);
    return true;
}

bool LogClassAccessAtRuntimeAsync(char *format, va_list args)
{
	int len = _vscprintf(format, args)   // _vscprintf doesn't count
		+ 1; // terminating '\0'
	CHAR* buffer = (CHAR*)calloc(len, sizeof(CHAR));
	_vsnprintf(buffer, len - 1, format, args);
	buffer[len - 1] = '\0';

		SYSTEMTIME stSystemTime;
		/////////////////////////////////////////////////////////////////////////
		// Generate Time Stamp
		/////////////////////////////////////////////////////////////////////////
		GetLocalTime(&stSystemTime);

		sprintf(cBuf, "%02d:%02d:%02d:%03d%c%s\r\n",
			stSystemTime.wHour,
			stSystemTime.wMinute,
			stSystemTime.wSecond,
			stSystemTime.wMilliseconds,
			g_cDumpSeparator,
			buffer);

		pendingClassesRuntimeOutput.push(std::string(cBuf));
		classesRuntimeWriterThreadNotified = true;
		cond_var.notify_one();

	free(buffer);
	return true;
}