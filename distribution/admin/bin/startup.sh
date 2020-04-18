#! /bin/shell

#======================================================================
# Admin项目启动shell脚本
# config目录: 配置文件目录
# logs目录: 项目运行日志目录
# logs/zeus_startup.log: 记录启动日志
# logs/back目录: 项目运行日志备份目录
# nohup后台运行
#
# author: xingcheng
# date: 2020-3-28
#======================================================================

# 项目名称
APPLICATION="${admin.artifact.name}"

# 项目启动jar包名称
APPLICATION_JAR="${APPLICATION}.jar"

echo ${APPLICATION_JAR}

# bin目录绝对路径
BIN_PATH=$(cd `dirname $0`; pwd)


# 进入bin目录
cd `dirname $0`
# 返回到上一级项目根目录路径
cd ..
# 打印项目根目录绝对路径
# `pwd` 执行系统命令并获得结果
BASE_PATH=`pwd`

# 外部配置文件绝对目录,如果是目录需要/结尾，也可以直接指定文件
# 如果指定的是目录,spring则会读取目录中的所有配置文件
CONFIG_DIR=${BASE_PATH}"/config/"

# 项目日志输出绝对路径
LOG_DIR=${BASE_PATH}"/logs"
LOG_FILE="${APPLICATION}.log"
LOG_PATH="${LOG_DIR}/${LOG_FILE}"
# 日志备份目录
LOG_BACK_DIR="${LOG_DIR}/back/"

# 项目启动日志输出绝对路径
LOG_STARTUP_PATH="${LOG_DIR}/${APPLICATION}_startup.log"

# 当前时间
NOW=$(date --date='0 days ago' "+%Y-%m-%d-%H-%M-%S")
NOW_PRETTY=$(date --date='0 days ago' "+%Y-%m-%d %H:%M:%S")

# 启动日志
STARTUP_LOG="================================================ ${NOW_PRETTY} ================================================\n"

# 如果logs文件夹不存在,则创建文件夹
if [ ! -d "${LOG_DIR}" ]; then
  mkdir "${LOG_DIR}"
fi

# 如果logs/back文件夹不存在,则创建文件夹
if [ ! -d "${LOG_BACK_DIR}" ]; then
  mkdir "${LOG_BACK_DIR}"
fi

# 如果项目运行日志存在,则重命名备份
if [ -f "${LOG_PATH}" ]; then
	mv ${LOG_PATH} "${LOG_BACK_DIR}/${APPLICATION}_back_${NOW}.log"
fi

# 创建新的项目运行日志
echo "" > ${LOG_PATH}

# 如果项目启动日志不存在,则创建,否则追加
echo ${STARTUP_LOG} >> ${LOG_STARTUP_PATH}

#=======================================================
# 将命令启动相关日志追加到日志文件
#=======================================================

# 输出项目名称
STARTUP_LOG="${STARTUP_LOG}application name: ${APPLICATION}\n"
# 输出jar包名称
STARTUP_LOG="${STARTUP_LOG}application jar name: ${APPLICATION_JAR}\n"
# 输出项目bin路径
STARTUP_LOG="${STARTUP_LOG}application bin  path: ${BIN_PATH}\n"
# 输出项目根目录
STARTUP_LOG="${STARTUP_LOG}application root path: ${BASE_PATH}\n"
# 打印日志路径
STARTUP_LOG="${STARTUP_LOG}application log  path: ${LOG_PATH}\n"

# 打印启动命令
STARTUP_LOG="${STARTUP_LOG}application background startup command: nohup java -jar ${BASE_PATH}/lib/${APPLICATION_JAR} --spring.config.location=${CONFIG_DIR} > ${LOG_PATH} 2>&1 &\n"

#======================================================================
# 执行启动命令：后台启动项目,并将日志输出到项目根目录下的logs文件夹下
#======================================================================
nohup java -jar ${BASE_PATH}/lib/${APPLICATION_JAR} --spring.config.location=${CONFIG_DIR} > ${LOG_PATH} 2>&1 &

# 进程ID
PID=$(ps -ef | grep ${APPLICATION_JAR} | grep -v grep | awk '{ print $2 }')
STARTUP_LOG="${STARTUP_LOG}application pid: ${PID}\n"

# 启动日志追加到启动日志文件中
echo -e ${STARTUP_LOG} >> ${LOG_STARTUP_PATH}
# 打印启动日志
echo -e ${STARTUP_LOG}

# 打印项目日志
tail -f ${LOG_PATH}