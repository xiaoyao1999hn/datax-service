#!/bin/bash

JAR_PATH=datax-service-0.0.1-SNAPSHOT.jar

base=`dirname $0`
pidfile=$base/running.pid

if [ -f $pidfile ] ; then
    echo "found $pidfile , Please run stop.sh first ,then startup.sh"
    exit 1
fi

JAVA_OPT="${JAVA_OPT} -server -Xms1g -Xmx1g -Xmn512m"
JAVA_OPT="${JAVA_OPT} -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:G1ReservePercent=25 -XX:InitiatingHeapOccupancyPercent=30 -XX:SoftRefLRUPolicyMSPerMB=0"
JAVA_OPT="${JAVA_OPT} -verbose:gc -Xloggc:/usr/local/services/datax/service/gclogs/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintAdaptiveSizePolicy"
JAVA_OPT="${JAVA_OPT} -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=100m"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"
JAVA_OPT="${JAVA_OPT} -XX:+AlwaysPreTouch"
JAVA_OPT="${JAVA_OPT} -XX:MaxDirectMemorySize=4g"
JAVA_OPT="${JAVA_OPT} -XX:-UseLargePages -XX:-UseBiasedLocking"
# JAVA_OPT="${JAVA_OPT} -Dcom.sun.management.jmxremote.port=21011 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=10.60.34.11"


echo "start $JAR_PATH ..."
nohup java -jar $JAVA_OPT   $JAR_PATH 1>>$JAR_PATH.log 2>&1 &
v_pid=$!
echo $v_pid > $pidfile
echo "Ok! log: $JAR_PATH.log, pid: $v_pid"
