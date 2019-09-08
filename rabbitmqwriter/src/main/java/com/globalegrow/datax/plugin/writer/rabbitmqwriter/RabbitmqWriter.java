package com.globalegrow.datax.plugin.writer.rabbitmqwriter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.common.plugin.RecordReceiver;
import com.alibaba.datax.common.spi.Writer;
import com.alibaba.datax.common.util.Configuration;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/4/27 16:18
 **/
public class RabbitmqWriter extends Writer {

    private final static String WRITE_COLUMNS = "columns";

    private final static int BATCH_SIZE = 1000;

    private final static int SEND_SIZE = 16;

    public static class Job extends Writer.Job {
        private static final Logger log = LoggerFactory.getLogger(Job.class);

        private Configuration conf = null;

        @Override
        public void preCheck() {
            super.preCheck();
        }

        @Override
        public List<Configuration> split(int mandatoryNumber) {
            List<Configuration> configurations = new ArrayList<Configuration>(mandatoryNumber);
            for (int i = 0; i < mandatoryNumber; i++) {
                configurations.add(conf);
            }
            return configurations;
        }

        @Override
        public void init() {
            this.conf = super.getPluginJobConf();
        }

        @Override
        public void destroy() {

        }
    }

    public static class Task extends Writer.Task {

        private static final Logger log = LoggerFactory.getLogger(Task.class);

        private Configuration conf = null;

        private List<RabbitmqColumn> columnList = null;

        private Connection conn = null;

        private Channel channel = null;

        private int count=0;

        @Override
        public void preCheck() {
            super.preCheck();
        }

        @Override
        public void prepare() {
//            this.conf = super.getPluginJobConf();
//            this.conn = RabbitmqUtil.getInstance().getConn(conf);
//            if (this.conn != null) {
//                this.channel = RabbitmqUtil.getInstance().getChannel(conn, conf);
//            }
//            check(this.conn,this.channel);
        }

        @Override
        public void init() {
            this.conf = super.getPluginJobConf();
            if (this.conn == null) {
                this.conn = RabbitmqUtil.getInstance().getConn(conf);
            }
            if (this.channel == null) {
                this.channel = RabbitmqUtil.getInstance().getChannel(conn, conf);
            }
            columnList=JSONArray.parseArray(this.conf.getString(WRITE_COLUMNS),RabbitmqColumn.class);

            log.info("配置：{}  列信息：",JSONObject.toJSONString(conf),this.conf.getString(WRITE_COLUMNS));
            check(this.conn,this.channel);
        }

        @Override
        public void post() {
            super.post();
        }

        @Override
        public void startWrite(RecordReceiver recordReceiver) {
            List<Record> writerBuffer = new ArrayList<>(BATCH_SIZE);
            Record record;
            long total = 0;
            while ((record = recordReceiver.getFromReader()) != null) {
                writerBuffer.add(record);
                if (writerBuffer.size() >= BATCH_SIZE) {
                    total += doBatchInsert(writerBuffer);
                    writerBuffer.clear();
                }
            }
            if (!writerBuffer.isEmpty()) {
                log.info("本次需要处理的数据大小：{}",writerBuffer.size());
                total += doBatchInsert(writerBuffer);
                writerBuffer.clear();
            }
            String msg = String.format("task end, write size :%d ，msg count：%d", total,count);
            getTaskPluginCollector().collectMessage("writesize", String.valueOf(total));
            log.info(msg);
        }

        private long doBatchInsert(final List<Record> writerBuffer) {
            int index = 0;
            try {
                List<Map> dataList=new ArrayList();
                log.info("本次批量处理数据数：{},当前第",writerBuffer.size());
                for(Record record:writerBuffer){
                    Map data = new HashMap(16);
                    int length = record.getColumnNumber();
                    for (int i = 0; i < length; i++) {
                        Column column=record.getColumn(i);
                        data.put(columnList.get(i).getName(), column.getRawData());
                    }
                    dataList.add(data);
                    index++;

                    if(index%10==0){
                        sendMsg(dataList);
                    }
                }

                if(!dataList.isEmpty()){
                    sendMsg(dataList);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                throw DataXException.asDataXException(RabbitmqWriterErrorCode.EXECUTE_ERROR, e);
            }
            return index;
        }

        private void sendMsg(List<Map> dataList) throws IOException {
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).
                    contentEncoding("UTF-8").build();
            String msg=JSONObject.toJSONString(dataList);
            channel.basicPublish(conf.getString("exchangeName"), conf.getString("routeKey"), properties, msg.getBytes());
            dataList.clear();
            count++;
        }

        @Override
        public void destroy() {
            RabbitmqUtil.getInstance().closeConn(conn, channel);
        }

        private void check(Connection connection,Channel channel) {
            if (connection == null) {
                throw DataXException.asDataXException(RabbitmqWriterErrorCode.CONNECT_MQ_FAIL, "获取Connection失败！");
            }
            if (channel == null) {
                throw DataXException.asDataXException(RabbitmqWriterErrorCode.CONNECT_MQ_FAIL, "获取channel失败！");
            }
        }
    }

}
