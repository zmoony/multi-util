#!/bin/bash

# 定义服务名称、jar 文件路径、日志文件路径及PID文件路径
SERVICE_NAME="myJavaProject"
JAR_FILE="/path/to/myJavaProject.jar"
LOG_FILE="/var/log/myJavaProject.log"
PID_FILE="/var/run/myJavaProject.pid"

# 定义函数，用于启动服务
start_service() {
    echo "Starting $SERVICE_NAME..."
    nohup java -jar $JAR_FILE > $LOG_FILE 2>&1 &
    PID=$!
    echo $PID > $PID_FILE
    echo "$SERVICE_NAME started with PID $PID."
}

# 定义函数，用于停止服务
stop_service() {
    echo "Stopping $SERVICE_NAME..."
    if [ -f $PID_FILE ]; then
        PID=$(cat $PID_FILE)
        kill -SIGTERM $PID
        rm -f $PID_FILE
        echo "$SERVICE_NAME stopped."
    else
        echo "$SERVICE_NAME is not running."
    fi
}

# 定义函数，用于重启服务
restart_service() {
    stop_service
    sleep 5 # 延迟一段时间等待服务完全停止（可调整）
    start_service
}

# 主要逻辑，根据传入的参数执行相应操作
case "$1" in
    start)
        start_service
        ;;
    stop)
        stop_service
        ;;
    restart)
        restart_service
        ;;
    *)
        echo "Usage: $0 {start|stop|restart}"
        exit 1
esac

exit 0
