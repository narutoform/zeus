#! /bin/shell

#======================================================================
# 1. 下载或更新zeus版本库
# 2. maven打包
# 3. 停服
# 4. 备份
# 5. 运行zeus
# author: xingcheng
# date: 2020-3-25
#======================================================================

NOW=$(date --date='0 days ago' "+%Y-%m-%d-%H-%M-%S")
echo "${NOW}"
PULL_RESULT=""
IS_UPDATE=0
# 项目名称
PROJECT_NAME="zeus"
# 版本库路径
REPO_URL="https://github.com/narutoform/zeus.git"
# 打包文件名称
ASSEMBLY_NAME="zeus-server-2.0.tar.gz"
# 服务目录名称
SERVER_DIR="zeus-server-2.0"
# 发布的GIT分支名称
DEPLOY_BRANCH=dev
# 发布的Maven Profile
ACTIVE_PROFILE=test

# 1. 下载或更新zeus版本库
# 先判断当前目录下是否有zeus目录
# 如果有，则执行git pull
# 如果没有，则clone
if [ ! -d "${PROJECT_NAME}" ]; then
  git clone ${REPO_URL}
  cd ${PROJECT_NAME}
else
  cd ${PROJECT_NAME}
  # 拉取代码，并获取结果判断，是否有新的代码更新，如果有，则备份之前的server，否则替换
  PULL_RESULT=$(git pull)
  echo "${PULL_RESULT}"

  if [[ ! $PULL_RESULT == *up-to-date* ]]
  then
    echo "update code..."
    IS_UPDATE=1
  fi
fi

pwd

git checkout ${DEPLOY_BRANCH}
git branch

# 2. maven打包
mvn clean install -P release,${ACTIVE_PROFILE}
mvn clean package -P release,${ACTIVE_PROFILE}

pwd
# 判断是否生成成功
if [ ! -f "distribution/target/${ASSEMBLY_NAME}" ]; then
  echo "maven build fail"
  exit
fi

# 3. 停服
cd ..
pwd

if [ -d "${SERVER_DIR}" ]; then
  sh ${SERVER_DIR}/bin/shutdown.sh
fi

# 4. 复制zeus-server-assembly.tar.gz到项目同级目录下
# 备份之前的server
if [ ! -d "${SERVER_DIR}-back" ]; then
  mkdir ${SERVER_DIR}-back
fi

if [[ -d "${SERVER_DIR}" ]]
then
	echo "back ${SERVER_DIR}..."
  mv ${SERVER_DIR} ${SERVER_DIR}-back/${SERVER_DIR}-back-"${NOW}"
  echo "back success"
fi

echo "copy ${ASSEMBLY_NAME}..."
# 复制到项目同级目录，如果有，则覆盖
cp -r -f ${PROJECT_NAME}/distribution/target/${ASSEMBLY_NAME} ${ASSEMBLY_NAME}
echo "copy success"

pwd
# 5. 运行zeus
tar -zxvf ${ASSEMBLY_NAME}
echo "tar.gz decompression success"

pwd
sh ${SERVER_DIR}/bin/shutdown.sh
sh ${SERVER_DIR}/bin/startup.sh

