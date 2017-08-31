//*************************************************************************
// Includes
//*************************************************************************
#include "stdafx.h"
#include <windows.h>

//*************************************************************************
// Suppress new style warning messages
//*************************************************************************
#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

#define lengthof(a) (sizeof a / sizeof a[0])

//************************************
// Method:    _tmain
// FullName:  _tmain
// Access:    public 
// Returns:   int
// Qualifier:
// Parameter: int argc
// Parameter: _TCHAR * argv[]
//************************************
int _tmain(int argc, _TCHAR* argv[])
{
	STARTUPINFO si;
	PROCESS_INFORMATION pi;
	ZeroMemory( &si, sizeof(si) );
	si.cb = sizeof(si);
	ZeroMemory( &pi, sizeof(pi) );
	TCHAR szCmdLine[0x8000];
	memset(szCmdLine, 0, sizeof(szCmdLine));
	_tcscpy(szCmdLine, L"il2server.exe ");
	for (int i=1; i<lengthof(argv); i++) {
		_tcscat(szCmdLine, L" ");
		_tcscat(szCmdLine, argv[i]);
	}
	if (CreateProcess(NULL, szCmdLine, NULL,
		NULL, FALSE, 0, NULL, NULL, &si, &pi))
	{
		CloseHandle(pi.hThread);
		WaitForSingleObject(pi.hProcess, INFINITE);
		CloseHandle(pi.hProcess);
	} else {
		_tprintf(L"\r\n");
		_tprintf(L"IL-2 Server launch failed!\r\n");
		_tprintf(L"\r\n");
	}
	return 0;
}

