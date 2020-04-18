#! /bin/shell

#======================================================================
# 先下载按照脚本，多次执行该脚本，会覆盖以下下载的文件
# 快速安装jdk/git/maven/redis/mysql
#
# author: xingcheng
# date: 2019-8-29
#======================================================================

# 下载脚本
wget -O install-all.sh https://raw.githubusercontent.com/geekidea/zeus/master/docs/bin/install/install-all.sh
wget -O install-jdk.sh  https://raw.githubusercontent.com/geekidea/zeus/master/docs/bin/install/install-jdk.sh
wget -O install-git.sh  https://raw.githubusercontent.com/geekidea/zeus/master/docs/bin/install/install-git.sh
wget -O install-maven.sh  https://raw.githubusercontent.com/geekidea/zeus/master/docs/bin/install/install-maven.sh
wget -O install-redis.sh  https://raw.githubusercontent.com/geekidea/zeus/master/docs/bin/install/install-redis.sh
#wget -O install-mysql.sh  https://raw.githubusercontent.com/geekidea/zeus/master/docs/bin/install/install-mysql.sh

# 执行安装所有
sh install-all.sh