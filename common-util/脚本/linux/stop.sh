#!/bin/bash

# 定义服务相关变量
SERVICE_NAME="myJavaProject"
PID_FILE="/var/run/myJavaProject.pid"

# 停止服务
stop_service() {
    echo "Stopping $SERVICE_NAME..."
    if [ -f $PID_FILE ]; then
        PID=$(cat $PID_FILE)
        kill -SIGTERM $PID
        rm -f $PID_FILE
        echo "$SERVICE_NAME stopped."
    else
        echo "$Java_PROJECT is not running."
    fi
}

stop_service

exit 0
