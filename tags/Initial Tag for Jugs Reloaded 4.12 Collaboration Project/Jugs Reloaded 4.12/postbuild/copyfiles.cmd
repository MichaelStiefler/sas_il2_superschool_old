@echo off
title WAIT !

Set "sourceDir=..\bin"
Set "destinationFolder=..\hashed"

IF NOT EXIST "%sourceDir%" (echo.Could not find %sourceDir% &GoTo:done)

:: copy files
For /F "Delims=" %%! in ('Dir "%sourceDir%\" /b /s /a-d 2^>nul') do (
rem @echo.%%! &(
@xcopy "%%!" "%destinationFolder%\" /i /y /h /f /c >nul)
rem )