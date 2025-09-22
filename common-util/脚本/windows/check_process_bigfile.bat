@echo off
title 进程检查
color 0A

:check
tasklist | find /i "v2rayN.exe" > nul
if %errorlevel% equ 0 (
    msg * "警告：检测到 v2rayN.exe 正在运行！请关闭后再进行远程操作。"
    exit
)

echo 未检测到 v2rayN.exe，可以进行远程操作...
timeout /t 3 > nul
exit