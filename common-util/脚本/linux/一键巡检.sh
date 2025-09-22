nux 一键巡检脚本
# 作者: yuez
# 生成时间: $(date)

LOG_FILE="/opt/巡检报告_$(date +%F_%T).log"
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# 初始化日志文件
echo "系统巡检报告" > $LOG_FILE
echo "生成时间: $(date)" >> $LOG_FILE
echo "主机名: $(hostname)" >> $LOG_FILE

# 输出函数
log() {
    local level=$1
    local message=$2
    
    # 输出到控制台
    case $level in
        "INFO") echo -e "${GREEN}[INFO]${NC} $message" ;;
        "WARN") echo -e "${YELLOW}[WARN]${NC} $message" ;;
        "ERROR") echo -e "${RED}[ERROR]${NC} $message" ;;
        *) echo -e "$message" ;;
    esac
    
    # 输出到日志文件
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] [$level] $message" >> $LOG_FILE
}

# 错误处理函数
handle_error() {
    log "ERROR" "执行命令失败: $1"
    log "ERROR" "错误详情: $2"
    log "ERROR" "----------------------------------------"
}

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        log "WARN" "命令 '$1' 不存在，相关检查将跳过"
        return 1
    fi
    return 0
}

log "INFO" ""
log "INFO" "======================[1] 系统基本信息========================"
log "INFO" "主机名: $(hostname)"
log "INFO" "IP地址: $(hostname -I | awk '{print $1}')"

if check_command "hostnamectl"; then
    log "INFO" "操作系统: $(hostnamectl | grep "Operating System" | cut -d: -f2 | sed 's/^ *//')"
else
    log "INFO" "操作系统: $(cat /etc/os-release | grep PRETTY_NAME | cut -d= -f2 | tr -d '\"')"
fi

log "INFO" "内核版本: $(uname -r)"
log "INFO" "启动时间: $(uptime -s)"
log "INFO" "运行时长: $(uptime -p)"
log "INFO" "系统负载: $(uptime | awk -F'load average:' '{print $2}' | sed 's/^ *//')"
log "INFO" ""

log "INFO" "======================[2] CPU 信息==========================:"
if check_command "lscpu"; then
    log "INFO" "CPU 型号: $(lscpu | grep 'Model name' | awk -F: '{print $2}' | sed 's/^ *//')"
else
    log "INFO" "CPU 型号: $(cat /proc/cpuinfo | grep 'model name' | head -1 | awk -F': ' '{print $2}')"
fi

log "INFO" "逻辑CPU核数: $(grep -c "processor" /proc/cpuinfo)"
log "INFO" "物理CPU核数: $(grep "physical id" /proc/cpuinfo | sort -u | wc -l)"

if check_command "top"; then
    log "INFO" "CPU 使用率: $(top -bn1 | grep '%Cpu' | awk '{print $2}')%"
else
    log "INFO" "CPU 使用率: 无法获取 (缺少top命令)"
fi
log "INFO" ""

log "INFO" "======================[3] 内存使用情况=========================="
if check_command "free"; then
    free -h >> $LOG_FILE
    total_mem=$(free -m | awk 'NR==2 {print $2}')
    used_mem=$(free -m | awk 'NR==2 {print $3}')
    free_mem=$(free -m | awk 'NR==2 {print $4}')
    cache_mem=$(free -m | awk 'NR==2 {print $6}')
    available_mem=$(free -m | awk 'NR==2 {print $7}')
    mem_percent=$(echo "scale=2; ($used_mem - $cache_mem) / $total_mem * 100" | bc)
    
    log "INFO" "总共内存: ${total_mem}MB"
    log "INFO" "使用内存: ${used_mem}MB (包含缓存: ${cache_mem}MB)"
    log "INFO" "可用内存: ${available_mem}MB"
    log "INFO" "内存使用占比: ${mem_percent}%"
    
    if (( $(echo "$mem_percent > 80" | bc -l) )); then
        log "WARN" "内存使用超过80%，可能存在内存压力"
    fi
else
    log "ERROR" "无法获取内存信息 (缺少free命令)"
fi
log "INFO" ""

log "INFO" "======================[4] 磁盘使用情况=========================="
if check_command "df"; then
    df -hT >> $LOG_FILE
    
    # 检查是否有分区使用率超过80%
    high_usage=$(df -h | awk '$5 >= "80%" {print $1 " (" $5 ")"}')
    if [ -n "$high_usage" ]; then
        log "WARN" "以下分区使用率超过80%:"
        echo "$high_usage" | while read line; do
            log "WARN" "$line"
        done
    fi
else
    log "ERROR" "无法获取磁盘信息 (缺少df命令)"
fi
log "INFO" ""

log "INFO" "======================[5] 网络配置和连接=========================="
log "INFO" "IP 地址: $(hostname -I | tr -d '\n')"

if check_command "ip"; then
    log "INFO" "默认网关: $(ip route | grep default | awk '{print $3}')"
else
    log "INFO" "默认网关: 无法获取 (缺少ip命令)"
fi

log "INFO" "网络接口状态:"
if check_command "ifconfig"; then
    ifconfig >> $LOG_FILE
else
    log "INFO" "网络接口状态: (使用ip代替ifconfig)"
    ip addr show >> $LOG_FILE
fi
log "INFO" ""

log "INFO" "网络连接状态:"
if check_command "ss"; then
    ss -tunlp >> $LOG_FILE
else
    log "INFO" "网络连接状态: (使用netstat代替ss)"
    netstat -tunlp >> $LOG_FILE
fi
log "INFO" ""

log "INFO" "======================[6] 服务状态检查=========================="
log "INFO" "检查特定服务状态:"

services=("firewalld" "sshd" "nginx" "apache2" "httpd" "mysqld" "mysql" "docker" "redis")

for service in "${services[@]}"; do
    if check_command "systemctl"; then
        # 使用systemctl检查服务状态
        if systemctl is-active --quiet $service; then
            log "INFO" "$service 服务状态: 正在运行"
        elif systemctl is-enabled --quiet $service; then
            log "WARN" "$service 服务状态: 已启用但未运行"
        else
            log "INFO" "$service 服务状态: 未运行"
        fi
    elif check_command "service"; then
        # 对于不支持systemctl的系统，使用service命令
        if service $service status >/dev/null 2>&1; then
            log "INFO" "$service 服务状态: 正在运行"
        else
            log "INFO" "$service 服务状态: 未运行"
        fi
    else
        log "ERROR" "无法检查服务状态 (缺少systemctl和service命令)"
        break
    fi
done
log "INFO" ""

log "INFO" "========================[7] 安全检查============================"
log "INFO" "SSH 配置:"
if [ -f "/etc/ssh/sshd_config" ]; then
    grep -E "^#?PermitRootLogin|^#?PasswordAuthentication" /etc/ssh/sshd_config >> $LOG_FILE
    
    # 检查是否允许root登录
    root_login=$(grep "^PermitRootLogin" /etc/ssh/sshd_config | awk '{print $2}')
    if [ "$root_login" = "yes" ]; then
        log "WARN" "SSH允许root直接登录，存在安全风险"
    fi
    
    # 检查是否允许密码认证
    pass_auth=$(grep "^PasswordAuthentication" /etc/ssh/sshd_config | awk '{print $2}')
    if [ "$pass_auth" = "yes" ]; then
        log "INFO" "SSH允许密码认证，请确保使用强密码"
    fi
else
    log "ERROR" "SSH配置文件不存在"
fi
log "INFO" ""

log "INFO" "系统用户 (UID>=1000):"
if [ -f "/etc/passwd" ]; then
    awk -F: '{if ($3 >= 1000 && $1 != "nobody") print $1}' /etc/passwd >> $LOG_FILE
else
    log "ERROR" "/etc/passwd文件不存在"
fi
log "INFO" ""

log "INFO" "========================[8] 登录记录============================"
log "INFO" "当前登录用户:"
if check_command "who"; then
    who >> $LOG_FILE
else
    log "INFO" "当前登录用户: (使用w代替who)"
    w >> $LOG_FILE
fi
log "INFO" ""

log "INFO" "最近登录记录:"
if check_command "last"; then
    last -a | head -10 >> $LOG_FILE
else
    log "INFO" "最近登录记录: 无法获取 (缺少last命令)"
fi
log "INFO" ""

log "INFO" "========================[9] 系统日志检查============================"
log "INFO" "登录失败日志:"
if [ -f "/var/log/auth.log" ]; then
    grep "Failed password" /var/log/auth.log | tail -10 >> $LOG_FILE
    failed_count=$(grep "Failed password" /var/log/auth.log | wc -l)
    log "INFO" "最近登录失败次数: $failed_count"
    
    if [ $failed_count -gt 10 ]; then
        log "WARN" "登录失败次数较多，可能存在暴力破解尝试"
    fi
elif [ -f "/var/log/secure" ]; then
    grep "Failed password" /var/log/secure | tail -10 >> $LOG_FILE
    failed_count=$(grep "Failed password" /var/log/secure | wc -l)
    log "INFO" "最近登录失败次数: $failed_count"
    
    if [ $failed_count -gt 10 ]; then
        log "WARN" "登录失败次数较多，可能存在暴力破解尝试"
    fi
else
    log "INFO" "未找到认证日志文件"
fi
log "INFO" ""

log "INFO" "检查系统重启记录:"
if check_command "last"; then
    last reboot | head -5 >> $LOG_FILE
else
    log "INFO" "系统重启记录: 无法获取 (缺少last命令)"
fi
log "INFO" ""

log "INFO" "========================[10] 性能分析============================"
log "INFO" "内存占用排行前5:"
if check_command "ps"; then
    ps aux --sort=-%mem | head -6 >> $LOG_FILE
else
    log "INFO" "内存占用排行: 无法获取 (缺少ps命令)"
fi
log "INFO" ""

log "INFO" "CPU 占用排行前5:"
if check_command "ps"; then
    ps aux --sort=-%cpu | head -6 >> $LOG_FILE
else
    log "INFO" "CPU占用排行: 无法获取 (缺少ps命令)"
fi
log "INFO" ""

log "INFO" "=============================巡检完成============================"
log "INFO" "巡检报告生成完成，保存路径: $LOG_FILE"
log "INFO" "请根据巡检内容检查系统状态！"
log "INFO" ""
