:: ����IP��ַ
:: �ر��ն˻���
@echo off
::�����־�
::netsh interface ip set address name="��������" source=static addr=50.35.2.247 mask=255.255.255.0 gateway=50.35.2.254
netsh interface ip set address name="��������" source=static addr=172.17.204.252 mask=255.255.255.0 gateway=172.17.204.1
::netsh interface ip set address name="��������" source=static addr=192.168.1.10 mask=255.255.255.0 gateway=192.168.1.1
::��ˮ
::netsh interface ip set address name="��������" source=static addr=172.18.43.49 mask=255.255.255.0 gateway=172.18.43.1
::����DNS
::netsh interface ip set dns name="��������" source=static addr=192.168.1.1 validate=no
pause
 
