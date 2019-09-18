#!/bin/bash

JAR_PATH=datax-service-0.0.1-SNAPSHOT.jar

base=`dirname $0`
pidfile=$base/running.pid
if [ ! -f "$pidfile" ];then
	echo "$JAR_PATH is not running."
	exit
fi

pid=`cat $pidfile`
if [ "$pid" != "" ] ; then
    echo -e "`hostname`: stopping $JAR_PATH $pid ... "
    kill $pid
fi

LOOPS=0
while (true); 
do 
	gpid=`ps -ef | grep "$pid" | grep "java -jar $JAR_PATH"`
    if [ "$gpid" == "" ] ; then
    	echo "Ok! cost:$LOOPS"
    	`rm $pidfile`
    	break;
    fi
    let LOOPS=LOOPS+1
    sleep 1
done
