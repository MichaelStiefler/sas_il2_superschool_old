#include "stdafx.h"
//#include <winternl.h>
//#include <limits.h>
#include <assert.h>
#include <tchar.h>
//#include <WinIoCtl.h>

#define _CRT_SECURE_NO_WARNINGS
#pragma warning( disable : 4996 )

#pragma comment(lib, "lib/ntdll.lib")

#define METHOD_BUFFERED                 0
#define FILE_ANY_ACCESS                 0
#define CTL_CODE( DeviceType, Function, Method, Access ) (                 \
    ((DeviceType) << 16) | ((Access) << 14) | ((Function) << 2) | (Method) \
)
#define USHRT_MAX     0xffff        // maximum unsigned short value
typedef __success(return >= 0) LONG NTSTATUS;

enum OBJECT_INFORMATION_CLASS { ObjectNameInformation = 1 };
enum FILE_INFORMATION_CLASS { FileNameInformation = 9 };
struct FILE_NAME_INFORMATION { ULONG FileNameLength; WCHAR FileName[1]; };
typedef struct _IO_STATUS_BLOCK { PVOID Dummy; ULONG_PTR Information; } IO_STATUS_BLOCK, * PIO_STATUS_BLOCK;
struct UNICODE_STRING { USHORT Length; USHORT MaximumLength; PWSTR Buffer; };
struct MOUNTMGR_TARGET_NAME { USHORT DeviceNameLength; WCHAR DeviceName[1]; };
struct MOUNTMGR_VOLUME_PATHS { ULONG MultiSzLength; WCHAR MultiSz[1]; };

extern "C" NTSYSAPI NTSTATUS NTAPI NtQueryObject(IN HANDLE Handle OPTIONAL,
	IN OBJECT_INFORMATION_CLASS ObjectInformationClass,
	OUT PVOID ObjectInformation OPTIONAL, IN ULONG ObjectInformationLength,
	OUT PULONG ReturnLength OPTIONAL);
extern "C" NTSYSAPI NTSTATUS NTAPI NtQueryInformationFile(IN HANDLE FileHandle,
	OUT PIO_STATUS_BLOCK IoStatusBlock, OUT PVOID FileInformation,
	IN ULONG Length, IN FILE_INFORMATION_CLASS FileInformationClass);

#define MOUNTMGRCONTROLTYPE ((ULONG) 'm')
#define IOCTL_MOUNTMGR_QUERY_DOS_VOLUME_PATH \
    CTL_CODE(MOUNTMGRCONTROLTYPE, 12, METHOD_BUFFERED, FILE_ANY_ACCESS)

union ANY_BUFFER {
	MOUNTMGR_TARGET_NAME TargetName;
	MOUNTMGR_VOLUME_PATHS TargetPaths;
	FILE_NAME_INFORMATION NameInfo;
	UNICODE_STRING UnicodeString;
	WCHAR Buffer[USHRT_MAX];
};

LPWSTR GetXpFilePath(HANDLE hFile)
{
	static ANY_BUFFER nameFull, nameRel, nameMnt;
	ULONG returnedLength; IO_STATUS_BLOCK iosb; NTSTATUS status;
	status = NtQueryObject(hFile, ObjectNameInformation,
		nameFull.Buffer, sizeof(nameFull.Buffer), &returnedLength);
	assert(status == 0);
	status = NtQueryInformationFile(hFile, &iosb, nameRel.Buffer,
		sizeof(nameRel.Buffer), FileNameInformation);
	assert(status == 0);
	//I'm not sure how this works with network paths...
	assert(nameFull.UnicodeString.Length >= nameRel.NameInfo.FileNameLength);
	nameMnt.TargetName.DeviceNameLength = (USHORT)(
		nameFull.UnicodeString.Length - nameRel.NameInfo.FileNameLength);
	wcsncpy(nameMnt.TargetName.DeviceName, nameFull.UnicodeString.Buffer,
		nameMnt.TargetName.DeviceNameLength / sizeof(WCHAR));
	HANDLE hMountPointMgr = CreateFile(_T("\\\\.\\MountPointManager"),
		0, FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE,
		NULL, OPEN_EXISTING, 0, NULL);
	__try
	{
		DWORD bytesReturned;
		BOOL success = DeviceIoControl(hMountPointMgr,
			IOCTL_MOUNTMGR_QUERY_DOS_VOLUME_PATH, &nameMnt,
			sizeof(nameMnt), &nameMnt, sizeof(nameMnt),
			&bytesReturned, NULL);
		assert(success && nameMnt.TargetPaths.MultiSzLength > 0);
		wcsncat(nameMnt.TargetPaths.MultiSz, nameRel.NameInfo.FileName,
			nameRel.NameInfo.FileNameLength / sizeof(WCHAR));
		return nameMnt.TargetPaths.MultiSz;
	}
	__finally { CloseHandle(hMountPointMgr); }
}
