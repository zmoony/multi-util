:: 动态脚本
:: 关闭终端回显
@echo off
netsh interface ip set address name="本地连接" source=dhcp

netsh interface ip set dns name="本地连接" source=dhcp

pause
 
