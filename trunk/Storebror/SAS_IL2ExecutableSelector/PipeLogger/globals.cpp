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

#include "globals.h"

// Suppress compiler warnings about unsafe functions
#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

//*************************************************************************
// Globals
//*************************************************************************
TCHAR g_szLogFileName[MAX_PATH];
TCHAR g_szBuf[1024];
CHAR g_cBuf[1024];
CHAR g_cPipeBuf[PIPE_BUF_SIZE];
std::atomic<int> g_aiLogWriters = 0;
HANDLE g_hTerminatePipeLogger;
DWORD g_dwCurInstance = 0;