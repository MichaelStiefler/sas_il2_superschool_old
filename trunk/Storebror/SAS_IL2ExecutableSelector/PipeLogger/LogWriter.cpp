//*****************************************************************
// PipeLogger.exe - Enhanced Logging Tool for IL-2 1946
// Copyright (C) 2021 SAS~Storebror
//
// This file is part of PipeLogger.exe.
//
// PipeLogger.exe is free software.
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
// Includes
//*************************************************************************
#include "stdafx.h"
#include "LogWriter.h"
#include "trace.h"
#include "globals.h"

FILE* OpenLogFile(LPCTSTR logFileName) {
	FILE *retVal = NULL;
	while (true) {
		retVal = _tfsopen(logFileName, L"ab", _SH_DENYWR);

		if (retVal != NULL) {
			break;
		}

		Sleep(rand() % 100);
	}
	return retVal;
}

void CloseLogFile(FILE *logFile) {
	fflush(logFile);
	fclose(logFile);
}

FILE* FlushLogFile(FILE *logFile, LPCTSTR logFileName) {
	CloseLogFile(logFile);
	return OpenLogFile(logFileName);
}

BOOL flushQueue(FILE *logFile, TQueueConcurrent<std::string> *logQueue) {
	BOOL retVal = FALSE;
	try {
		std::optional<std::string> queueElement = logQueue->pop_front();
		while (queueElement.has_value()) {
			fprintf(logFile, queueElement->c_str());
			retVal = TRUE;
			queueElement = logQueue->pop_front();
		}
	}
	catch (...) {
		TRACE("Exception occured in flushQueue()\r\n");
	}
	return retVal;
}

DWORD WINAPI LogWriterThread(LPVOID lpvParam)
{
	LogWriterParams* pLogWriterParams =(LogWriterParams*)lpvParam;
	HANDLE hNewData;
	HANDLE hEndLogWriter;
	HANDLE hTerminatePipeLogger;
	BOOL bDuplicateHandles = DuplicateHandle(GetCurrentProcess(), pLogWriterParams->hNewData, GetCurrentProcess(), &hNewData, 0, FALSE, DUPLICATE_SAME_ACCESS)
							&& DuplicateHandle(GetCurrentProcess(), pLogWriterParams->hEndLogWriter, GetCurrentProcess(), &hEndLogWriter, 0, FALSE, DUPLICATE_SAME_ACCESS)
							&& DuplicateHandle(GetCurrentProcess(), g_hTerminatePipeLogger, GetCurrentProcess(), &hTerminatePipeLogger, 0, FALSE, DUPLICATE_SAME_ACCESS);
	if (!bDuplicateHandles) {
		TRACE("Log Writer No.%d could not duplicate required Wait Handles!\r\n", pLogWriterParams->dwIndex);
		return -1;
	}
	HANDLE waitHandles[] = { hNewData, hEndLogWriter, hTerminatePipeLogger };

	TCHAR* pchLogFile = (TCHAR*)calloc(_tcslen(pLogWriterParams->pchLogFile) + 1, sizeof(TCHAR));
	_tcscpy(pchLogFile, pLogWriterParams->pchLogFile);

	DWORD dwIndex = pLogWriterParams->dwIndex, dwFlushTimeout = pLogWriterParams->dwFlushTimeout;
	TQueueConcurrent<std::string> *logQueue = pLogWriterParams->logQueue;

	g_aiLogWriters++;
	TRACE("Log Writer No.%d running\r\n", dwIndex);
	DWORD lastFlush = GetTickCount();
	BOOL bInstantFlush = FALSE;
	if (dwFlushTimeout == 0) {
		dwFlushTimeout = INFINITE;
		bInstantFlush = TRUE;
	}
	FILE *logFile = OpenLogFile(pchLogFile);
	BOOL bFlushPending = FALSE;
	BOOL bContinue = TRUE;
	try {
		while (bContinue)
		{
			switch (WaitForMultipleObjects(3, waitHandles, FALSE, dwFlushTimeout)) {
			case WAIT_OBJECT_0:
				bFlushPending = flushQueue(logFile, logQueue);
				if (bInstantFlush) {
					if (bFlushPending) {
						logFile = FlushLogFile(logFile, pchLogFile);
						bFlushPending = FALSE;
					}
				}
				else if (dwFlushTimeout != INFINITE && GetTickCount() - lastFlush > dwFlushTimeout) {
					logFile = FlushLogFile(logFile, pchLogFile);
					lastFlush = GetTickCount();
					bFlushPending = FALSE;
				}
				break;
			case WAIT_OBJECT_0 + 1:
			case WAIT_OBJECT_0 + 2:
				flushQueue(logFile, logQueue);
				bContinue = FALSE;
				break;
			case WAIT_TIMEOUT:
				logFile = FlushLogFile(logFile, pchLogFile);
				lastFlush = GetTickCount();
				bFlushPending = FALSE;
				break;
			default:
				break;
			}
		}
	}
	catch (...) { }
	CloseLogFile(logFile);
	g_aiLogWriters--;
	free(pchLogFile);
	CloseHandle(hNewData);
	CloseHandle(hEndLogWriter);
	CloseHandle(hTerminatePipeLogger);
	TRACE("Log Writer No.%d terminated\r\n", dwIndex);
	return 0;
}

