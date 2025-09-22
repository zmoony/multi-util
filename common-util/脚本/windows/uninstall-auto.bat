@echo off
set BASEDIR=%CD%
cd ../

for   %%s in ( *.exe) do (
  set  exefilename=%%~ns

) 
set jarfile=%exefilename%.jar
set xmlfile=%exefilename%.xml
set exefile=%exefilename%.exe

if exist  %xmlfile% (
    if exist  %jarfile% (
      echo ....install......
      %exefile% uninstall
      %exefile% status
    ) else (
        echo %jarfile% no exist !
        echo install failed !
    )

) else ( 
echo %xmlfile% no exist !
echo install failed !
)

:end
pause