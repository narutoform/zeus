#! /bin/shell

#======================================================================
# 快速安装JDK
# CentOS7 中已成功验证
# 使用yum方式安装
#
# author: sy
# date: 2019-8-29
#======================================================================


hasJdk(){
    RESULT=$(pgrep java)
    if [[ ! $RESULT ]]
    then
        return 0;
    fi
    return 1;
}

hasJdk
if [ $? != 1 ]
then
    echo "Not Found jdk"
    echo "Installing jdk..."
    yum install -y java-1.8.0-openjdk
    hasJdk
    if [ $? != 1 ]
    then
      echo "Install jdk Fail"
    fi
fi

java -version
echo ""



