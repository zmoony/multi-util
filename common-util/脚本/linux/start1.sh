#!/bin/bash

# 打印
function echo_info() {
    echo -e "Hello, World!"
}
#变量应用，注意使用中文的感叹号，因为！在bash里一般是查看历史的命令
name="zhangsan"
echo "name is $name ！"

#位置参数  ./args.sh one "two three" four
echo "第一个参数是：$1"
echo "第二个参数是：$2"
echo "第三个参数是：$3"
echo "所有参数是：$*"
echo "所有参数是：$@"
echo "所有参数是：$*"
echo "参数个数是：$#"
echo "脚本名是：$0"
echo "脚本路径是：$PWD"

#条件语句 注意[] 和 [[]] []前后的空格不能少 否则会报错
if [ $1 = "one" ]; then
    echo "第一个参数是one"
elif [ $1 = "two" ]; then
    echo "第一个参数是two"
else
    echo "第一个参数不是one也不是two"
fi

# shellcheck disable=SC1073
if [ "$name" == "zhangsan" ]; then
   echo "name is zhangsan"
elif [ "$name" == "lisi" ]; then
   echo "name is lisi"
else
   echo "name is not zhangsan or lisi"
fi

# 循环语句 注意条件结束添加分号 否则会报错
for i in 1 2 3 4 5; do
    echo "i is $i"
done

for i in {1..5};do
    echo "i is $i"
done

#=前后没有空格 否则会报错
count=1
while [ $count -le 5 ]; do
    echo "count is $count"
    count=$((count+1))
done

#函数
function echo_info() {
    echo "Hello, World!"
}
echo_info

#文件操作
#读取文件
#IFS=: 设置内部字段分隔符为空，确保读取整行内容。
#read -r line: 从标准输入读取一行，并存储到变量 line 中，-r 选项防止反斜杠转义。
while IFS= read -r line; do
    echo "$line"
done < inputfile.txt

#创建文件
touch outputfile.txt

#删除文件
rm outputfile.txt

#重定向
echo "Hello, World!" > outputfile.txt
#将 inputfile.txt 文件的内容追加到 outputfile.txt 文件末尾。如果 outputfile.txt 不存在，则创建该文件。
cat inputfile.txt >> outputfile.txt
#使用 grep 命令筛选包含 "Hello" 的行
cat inputfile.txt | grep "Hello" > outputfile.txt

#写入文件
#cat << EOF表示开始读取直到遇到EOF标记的内容
cat << EOF > outputfile.txt
Hello, World!
This is a sample text.
EOF

echo "Hello" > outputfile.txt

#错误处理 /dev/null 在 Linux 和 Unix 类似系统中是一个特殊的文件，被称为“黑洞”。任何写入到 /dev/null 的数据都会被丢弃，不会被存储或保留
echo "Hello" > /dev/null 2>&1
#运行command。
 #若command执行失败（返回非零状态码），则：
 #输出字符串 "Command failed"。
 #退出脚本，返回状态码1。
command || { echo "Command failed";exit 1;}

#调试脚本
set -x
#这意味着脚本执行的每一条命令都会在执行前被打印到标准错误输出上，同时还会打印出变量的当前值。
# 这对于调试脚本非常有用，因为它允许你看到脚本执行的每一个步骤以及变量的变化情况。
bash -x script.sh

#定时任务 cron
#编辑 crontab 文件
crontab -e
#添加定时任务
0 12 * * * /path/to/script.sh

































