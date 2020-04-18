#! /bin/shell

#======================================================================
# 快速安装MySQL
# CentOS7 中已成功验证
# 使用yum+rpm方式安装
#
# author: xingcheng
# date: 2019-8-29
#======================================================================

# 下载mysql rpm
wget https://repo.mysql.com//mysql80-community-release-el7-3.noarch.rpm

# 安装rpm
rpm -Uvh mysql80-community-release-el7-3.noarch.rpm

# yum 安装mysql服务
yum install -y mysql-community-server

# 启动mysql服务
systemctl start mysqld.service

# 查看mysql服务状态
systemctl status mysqld.service

# 查看安装的mysql密码
grep 'temporary password' /var/log/mysqld.log

TEMP_PWD=$(grep 'temporary password' /var/log/mysqld.log)
PWD=${TEMP_PWD##* }
echo "${PWD}"

# 登录
mysql -uroot -p${PWD}

# 进入到mysql命令行时，修改密码
# 修改密码
# ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';

# 使用新密码登录
# exit
# mysql -uroot -proot

# 导入zeus数据库脚本
# use mysql;
# source /root/mysql_zeus.sql;
