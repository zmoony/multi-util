@echo off
title ���̼��
color 0A

:check
tasklist | find /i "v2rayN.exe" > nul
if %errorlevel% equ 0 (
    msg * "���棺��⵽ v2rayN.exe �������У���رպ��ٽ���Զ�̲�����"
    exit
)

echo δ��⵽ v2rayN.exe�����Խ���Զ�̲���...
timeout /t 3 > nul
exit