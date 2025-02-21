#!/usr/bin/env bash

#常见命令

#文件
ls -l  # 列出详细信息，包括权限、所有者、大小和修改时间
ls -a  # 显示所有文件，包括隐藏文件（以 . 开头的文件）
cat /etc/passwd  #使用 cat /etc/passwd 显示系统用户账户信息
cat /etc/hosts   #使用 cat /etc/hosts 显示主机名与 IP 地址映射列表
cat /etc/resolv.conf  #使用 cat /etc/resolv.conf 显示 DNS 服务器列表
cat /etc/sysconfig/network-scripts/ifcfg-eth0  #使用 cat /etc/sysconfig/network-scripts/ifcfg-eth0 显示网卡配置信息
cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep GATEWAY  #使用 cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep GATEWAY 显示网卡的网关信息
cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep IPADDR   #使用 cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep IPADDR 显示网卡的 IP 地址
cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep NETMASK  #使用 cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep NETMASK 显示网卡的子网掩码
cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep HWADDR #使用 cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep HWADDR 显示网卡的物理地址
cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep ONBOOT  #使用 cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep ONBOOT 显示网卡的开机自启动状态
cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep DEVICE   #使用 cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep DEVICE 显示网卡的设备名称
cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep BOOTPROTO  #使用 cat /etc/sysconfig/network-scripts/ifcfg-eth0 | grep BOOTPROTO 显示网卡的 IP 配置方式

cd /root #进入根目录
cd .. #返回上一级目录
cd . #返回当前目录
cd - #返回上一次目录
cd ~ #返回当前用户的主目录
cd /home/user #进入指定目录
cd /home/user/Desktop #进入指定目录
cd /home/user/Desktop/ #进入指定目录
cd /home/user/Desktop/../ #返回上一级目录
cd /home/user/Desktop/../../ #返回上两级目录
cd /home/user/Desktop/../../../ #返回上三级目录
cd /home/user/Desktop/../../../../ #返回上四级目录

pwd #显示当前目录

mkdir /home/user/Desktop/test #创建目录
mkdir -p /home/user/Desktop/test/test1/test2 #创建多级目录

touch /home/user/Desktop/test/test.txt #创建文件

cp /home/user/Desktop/test /home/user/Desktop/test1 #复制目录
cp -r /home/user/Desktop/test /home/user/Desktop/test1 #递归复制目录

mv /home/user/Desktop/old /home/user/Desktop/new #移动目录

rm -rf /home/user/Desktop/test #删除目录
rm -rf /home/user/Desktop/test/* #删除目录下的所有文件
rm -rf /home/user/Desktop/test/test.txt #删除文件
rm -rf /home/user/Desktop/test/test.txt /home/user/Desktop/test/test1.txt /home/user/Desktop/test/test2.txt #删除多个文件

 rmdir /home/user/Desktop/test #只能删除空目录

 #文件内容查看
 cat filename
 cat file1 file2 > combined_file #将多个文件内容合并到 combined_file 中

 less filename #查看文件内容，支持分页查看
 less -S filename #查看文件内容，支持分页查看，且不换行显示
 less -N filename #查看文件内容，支持分页查看，且显示行号
 less -r filename #查看文件内容，支持分页查看，且显示原始内容
 less -S -N -r filename #查看文件内容，支持分页查看，且显示行号和原始内容
 less -S -N -r -X filename #查看文件内容，支持分页查看，且显示行号和原始内容，且不显示顶部和底部信息

 more filename #类似于 less，但功能较少

 head filename #查看文件开头的 10 行内容
 head -n 20 filename #查看文件开头的 20 行内容
 head -c 100 filename #查看文件开头的 100 个字节的内容
 head -100 filename #查看文件开头的 100 行内容
 head -n 100 filename #查看文件开头的 100 行内容
 head -100 filename | less #查看文件开头的 100 行内容，支持分页查看
 head -100 filename | less -S #查看文件开头的 100 行内容，支持分页查看，且不换行显示
head -100 filename | less -N #查看文件开头的 100 行

tail filename #查看文件结尾的 10 行内容
tail -n 20 filename #查看文件结尾的 20 行内容
tail -c 100 filename #查看文件结尾的 100 个字节的内容
tail -100 filename #查看文件结尾的 100 行内容
tail -n 100 filename #查看文件结尾的 100 行内容
tail -100 filename | less #查看文件结尾的 100 行内容，支持分页查看
tail -100 filename | less -S #查看文件结尾的 100 行内容，支持分页查看，且不换行显示
tail -100 filename | less -N #查看文件结尾的 100 行
tail -f filename ## 实时查看文件内容更新（例如日志文件）

grep pattern filename #查找文件中包含 pattern 的行
grep 'search_term' file_name
grep -r 'search_term' . #递归查找当前目录及子目录下所有包含 search_term 的文件
grep -r 'search_term' /path/to/directory  # 递归搜索目录中的文件

find /path/to/search -name 'file_name' #在指定目录中查找指定文件
find /path/to/search -type f -name '*.txt'  #在指定目录中查找所有以 .txt 结尾的文件
find /path/to/search -type d -name 'directory_name' #在指定目录中查找指定目录
find /path/to/search -type f -name '*.txt' -exec sed -i 's/old/new/g' {} \; #在指定目录中查找所有以 .txt 结尾的文件，并将文件中的所有 old 替换为 new

wc filename #统计文件内容行数、单词数、字符数
wc -l filename #统计文件内容的行数
wc -w filename #统计文件内容的单词数
wc -c filename #统计文件内容的字符数

 #文件内容修改
 #sed ，如查找、替换、删除、插入等  -i: 直接修改文件内容  -e: 执行指定的编辑命令  -f: 从文件中读取编辑命令  sed [options] 'command' [file]
 ## 查找替换
sed -i 's/old/new/g' filename #将文件中的所有 old 替换为 new
sed 's/old/new/g' filename > new_filename #将文件中的所有 old 替换为 new，并将结果保存到 new_filename 中
sed 's/old/new/g' filename >> new_filename #将文件中的所有 old 替换为 new，并将结果追加到 new_filename 中
sed 's/old/new/g' filename | tee new_filename #将文件中的所有 old 替换为 new，并将结果保存到new_filename 中，并显示结果
## 删除行
sed '/pattern/d' filename #删除文件中包含 pattern 的行
sed '/pattern/d' filename > new_filename #删除文件中包含 pattern 的行，并将结果保存到 new_filename 中
sed '/pattern/d' filename >> new_filename #删除文件中包含 pattern 的行，并将结果追加到 new_filename 中
sed '/pattern/d' filename | tee new_filename #删除文件中包含 pattern 的行，并将结果保存到 new_filename 中，并显示结果
## 插入行
sed '1i line' filename #在文件中的第一行插入一行 line

# 权限和所有权 r w x(4,2,1)
chmod 777 filename #修改文件权限为 777
chmod +x file_name       # 添加执行权限
chown user:group filename #修改文件所有者为 user，组为 group
chgrp group filename #修改文件组为 group

# 系统管理
ps -ef #查看当前运行的进程  显示进程树
ps -aux #查看当前运行的进程，包括用户、PID、CPU、内存等  显示所有进程
ps -ef | grep process_name #查看指定进程的详细信息
ps -ef | grep process_name | awk '{print $2}' #查看指定进程的 PID
ps -ef | grep process_name | awk '{print $2}' | xargs kill -9 #杀死指定进程
ps -ef | grep process_name | awk '{print $2}' | xargs kill -9 2>/dev/null #杀死指定进程，并忽略错误

top #查看当前运行的进程，包括 CPU、内存等
top -b -n 1 > top.txt #将 top 命令的结果保存到 top.txt 中

kill -9 pid #杀死指定进程
killall process_name #杀死指定进程

df -h #查看磁盘使用情况  df 用于查看文件系统的总体磁盘空间使用情况。 du 用于查看单个文件或目录的磁盘使用情况
df -h | awk '$NF=="/"{print $5}' #查看根目录使用率
df -h | awk '$NF=="/"{print $5}' | sed 's/%//g' | awk '{if($1>80) print $1}' #查看根目录使用率，超过 80% 的使用率

du -sh /path/to/directory #查看指定目录的大小

free -h #查看内存使用情况
free -h | awk '/^Mem:/{print $3"/"$2}' #查看内存使用率

cat /proc/cpuinfo #查看 CPU 信息
cat /proc/cpuinfo | grep "model name" | uniq #查看 CPU 型号

 uname -a #查看系统信息

 # 网络管理
 ping hostname #测试网络连接
 ping -c 4 hostname #测试网络连接，并指定次数
 ifconfig #查看网络接口信息
 ip addr show #查看网络接口信息
 ifconfig eth0 | grep "inet addr" | awk '{print $2}' | cut -d ':' -f2 #查看 IP 地址
 netstat #查看网络状态
 netstat -tuln #查看监听的 TCP 端口
 netstat -antp | grep "ESTABLISHED" #查看已连接的 TCP 连接
 ssh username@hostname #登录远程服务器


# 用户管理
useradd username #创建用户
userdel username #删除用户
usermod -aG groupname username #将用户添加到组中
usermod -d /home/username username #修改用户目录
usermod -m -d /home/username username #修改用户目录，并保留原目录下的文件
usermod -s /bin/bash username #修改用户shell
usermod -l new_username username #修改用户名
usermod -c "New Name" username #修改用户描述
usermod -g groupname username #修改用户组

groupadd groupname #创建组
groupdel groupname #删除组
groupmod -n new_groupname groupname #修改组名
groupmod -g group_id groupname #修改组ID

id username #查看用户信息
id -u username #查看用户ID
id -g username #查看用户组ID
id -G username #查看用户所属组

# 其他命令
## awk 它可以用来处理结构化的文本数据，例如 CSV 或 TSV 文件，并执行诸如筛选、排序、计算统计信息等操作。
## awk [options] 'pattern {action}' [file]
## pattern: 匹配文本中的模式。{action}: 执行的动作。
## $0: 整行内容 $1: 第一列 $2: 第二列 ... NR: 当前行号 NF: 当前行的字段数
## 条件判断 if, else if, else: 条件判断
## 循环结构 for, while, do-while: 循环结构。
## 函数 print, printf: 输出 sqrt, sin, cos: 数学函数。length, substr, index: 字符串操作。
awk '{print $1}' filename #打印文件中的第一列
 awk '{sum += $1} END {print sum}' file #计算文件中所有数字的和
awk '{print $1, $2, $3}' filename #打印文件中前三列的内容
awk '{print $1, $2, $3}' filename | sort -n #按第一列内容排序
awk '{print $1, $2, $3}' filename | sort -n -r #按第一列内容逆序排序
awk '{print $1, $2, $3}' filename | sort -n -k2 #按第二列内容排序
 awk '$1 > 10' file #打印文件中第一列大于10的行
  awk -F: '{print $1}' file #指定分隔符为:，打印第一列的内容
  awk 'BEGIN {FS=","} NR==1 {print; next} $2 > 100 {print $1, $2}' file #打印第一行，并跳过第一行，打印第二列大于100的行 FS: 字段分隔符  NR: 当前行号  next: 跳过剩余动作
