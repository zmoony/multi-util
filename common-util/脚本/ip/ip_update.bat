:: 设置IP地址
:: 关闭终端回显
@echo off
::江宁分局
::netsh interface ip set address name="本地连接" source=static addr=50.35.2.247 mask=255.255.255.0 gateway=50.35.2.254
netsh interface ip set address name="本地连接" source=static addr=172.17.204.252 mask=255.255.255.0 gateway=172.17.204.1
::netsh interface ip set address name="本地连接" source=static addr=192.168.1.10 mask=255.255.255.0 gateway=192.168.1.1
::溧水
::netsh interface ip set address name="本地连接" source=static addr=172.18.43.49 mask=255.255.255.0 gateway=172.18.43.1
::设置DNS
::netsh interface ip set dns name="本地连接" source=static addr=192.168.1.1 validate=no
pause
 
