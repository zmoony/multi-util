:: ����IP��ַ
:: �ر��ն˻���
@echo off

netsh interface ip set address name="��������" source=static addr=172.17.204.252 mask=255.255.255.0 gateway=172.17.204.1

pause
 
