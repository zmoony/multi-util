cd /yuez/project/jenkinsjar

#!/bin/bash
project=psmp_BigData_highLevel-1.0.1.jar #这里需要替换成你jar包的名字
log=/yuez/project/jenkinsjar/psmp_BigData_highLevel.log
pid=`ps -ef | grep $project | grep -v grep | awk '{print $2}'`  #杀掉原有项目进程
if [ -n "$pid" ]
then
   kill -9 $pid
   echo "杀死存在进程"
fi
BUILD_ID=dontKillMe 
echo "执行"
nohup java -jar $project  > $log 2>&1 &
echo "$project Start successful"   #启动进程
echo "启动成功！"