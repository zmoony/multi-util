title graalvm package
set PATH=D:/install/graalvm/graalvm-ee-java17-22.3.1/bin;%path%
set INCLUDE=C:/Program Files (x86)/Windows Kits/10/Include/10.0.22000.0/shared;C:/Program Files (x86)/Windows Kits/10/Include/10.0.22000.0/um;C:/Program Files (x86)/Windows Kits/10/Include/10.0.22000.0/ucrt;C:/Program Files/Microsoft Visual Studio/2022/Community/VC/Tools/MSVC/14.16.27023/include
set LIB=C:/Program Files (x86)/Windows Kits/10/Lib/10.0.22000.0/ucrt/x64;C:/Program Files (x86)/Windows Kits/10/Lib/10.0.22000.0/um/x64;C:/Program Files/Microsoft Visual Studio/2022/Community/VC/Tools/MSVC/14.16.27023/lib/x64
set JAVA_HOME=D:/install/graalvm/graalvm-ee-java17-22.3.1
set CLASSPATH=.;%JAVA_HOME%/lib/dt.jar;%JAVA_HOME%/lib/tools.jar
cd ..
mvn -Pnative clean compile native:compile-no-fork
::mvn -Pnative clean compile  native:compile-no-fork
::mvn -Pnative clean package

