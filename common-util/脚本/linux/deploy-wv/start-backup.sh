# 定义应用程序名称

APP_NAME_ORIGIN="psmp-10.1.jar"
APP_NAME="psmp10.jar"
JAVA_PATH="/home/psmp/jdk-21.0.6+7/bin/java"

# 查找进程ID
PID=$(pgrep -f "$APP_NAME")

if [ -n "$PID" ]; then
echo "进程 $APP_NAME (PID: $PID) 正在运行，正在终止..."
kill -9 $PID
#查看bak文件是否存在
if [ ! -d "./bak" ]; then
  mkdir ./bak
fi
DATE=$(date +%Y_%m_%d-%H%M%S)
mv $APP_NAME ./bak/$APP_NAME.$DATE.bak
echo "已备份 $APP_NAME 为 ./bak/$APP_NAME.$DATE.bak"
#将现有的jar重命名
mv $APP_NAME_ORIGIN $APP_NAME
sleep 2 # 等待进程终止
echo "进程 $APP_NAME 已终止。"
else
echo "进程 $APP_NAME 未运行。"
fi

# 启动应用程序
echo "正在启动 $APP_NAME..."
nohup $JAVA_PATH -jar $APP_NAME > nohup.out 2>&1 &
echo "$APP_NAME 已启动。"
