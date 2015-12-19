#! /bin/bash

#################################################
#               蜂朵网java应用发布脚本
#       (maven对java项目打包,重新启动tomcat服务器）
#
#    V1.0      Writen by: zxc       Date:2015-05-28
##################################################

#path=`pwd`
path=/root/workspace/com.fengduo.bee/
echo "Update fengduo project by svn"
svn up --username zxc --password '6379zxc!@' --non-interactive $path
#手动清内存
echo 0 >  /proc/sys/vm/drop_caches
echo 3 >  /proc/sys/vm/drop_caches
echo 0 >  /proc/sys/vm/drop_caches
echo 3 >  /proc/sys/vm/drop_caches

echo 'cd '$path',and mevan install package,bulid fengduo bee project war'
echo 'cp $JAVA_HOME/jre/lib/ext/sunjce_provider.jar to src/main/webapp/WEB-INF/lib'
cp $JAVA_HOME/jre/lib/ext/sunjce_provider.jar $path/src/main/webapp/WEB-INF/lib/

echo "Update fengduo project by svn"
cd $path
which mvn
mvn clean && mvn install package -Dmaven.test.skip=true

echo "shutdown tomcat app"
ps aux|grep tomcat |grep -v grep|grep -v solr|grep -v my-tomcat|grep tomcat|awk '{print $2}'|xargs kill -9
sleep 30
rm -rf /data/run/tomcat/webapps/bee*
rm -rf /data/run/tomcat/webapps/ROOT

echo "copy bee.war to /tomcat/webapps/"
cp $path/bee-web/target/ROOT.war /data/run/tomcat/webapps/ROOT.war

echo "Update static style files"
svn up --username zxc --password '6379zxc!@' --non-interactive /data/static/style/

echo "now start tomcat app"
JAVA_DEBUG_OPT=" -server -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=n "
str=`file $JAVA_HOME/bin/java | perl -pe 's/\n//g' | grep 64-bit`
if [ -n "$str" ]; then
    memTotal=`cat /proc/meminfo |grep MemTotal|awk '{printf "%d", $2/1024 }'`
    if [ $memTotal -gt 2500 ]; then
        JAVA_MEM_OPTS=" -server -Xms512m -Xmx3g -Xmn128m -XX:PermSize=96m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
    else
        JAVA_MEM_OPTS=" -server -Xmx1g -Xms3g -Xmn256m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
    fi
else
    JAVA_MEM_OPTS=" -server -Xms1024m -Xmx3g -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi
JAVA_OPTS="$JAVA_OPTS $JAVA_MEM_OPTS "
JAVA_OPTS_EXT=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dcomset.reoced.urltime.flag=true "
JAVA_OPTS="$JAVA_OPTS $JAVA_OPTS_EXT"
export JAVA_OPTS=$JAVA_OPTS

echo "now run startup.sh, run catalina.sh"
/data/run/tomcat/bin/startup.sh

echo "waitting 20 seconds!"
sleep 20
echo "tomcat having startup,waitting 10 seconds!"
sleep 10
echo "connection to databases,waitting 10 seconds!"
sleep 10
echo "something initing,Please Waitting 10 seconds!"
sleep 10

echo "now repalce style files"
chown -R nobody.nobody  /data/static/style/

find /data/static/style/ -name "*.js"|xargs perl -pi -e 's|/static/js/|/js/|g'
find /data/static/style/ -name "*.js"|xargs perl -pi -e 's|/static/img/|/img/|g'
find /data/static/style/ -name "*.css"|xargs perl -pi -e 's|/static/img/|/img/|g'

find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/images/|/images/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/js/|/js/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/css/|/css/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/img/|/img/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/styles/|/styles/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/simditor/|/simditor/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/jquery-validation/|/jquery-validation/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/jquery/|/jquery/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/fengduo/|/fengduo/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/layer/|/layer/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/bootstrap/|/bootstrap/|g'
find /data/run/tomcat/webapps/ -name "*.vm"|xargs perl -pi -e 's|/static/v1/|/v1/|g'

echo "Please Waitting 10 seconds!"
sleep 10
tail -10 /home/admin/logs/all.log
echo "Please Waitting 10 seconds!"
sleep 10
tail -10 /home/admin/logs/all.log
echo "Please Waitting 10 seconds!"
sleep 10
tail -10 /home/admin/logs/all.log

exit 0