:: 设置IP地址
:: 关闭终端回显
@echo off

netsh interface ip set address name="本地连接" source=static addr=172.17.204.252 mask=255.255.255.0 gateway=172.17.204.1

pause
 
