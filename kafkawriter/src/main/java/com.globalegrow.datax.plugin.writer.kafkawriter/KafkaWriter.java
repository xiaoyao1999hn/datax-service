package com.globalegrow.datax.plugin.writer.kafkawriter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.common.plugin.RecordReceiver;
import com.alibaba.datax.common.spi.Writer;
import com.alibaba.datax.common.util.Configuration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * kafka writer
 * @author ChengJie
 * @desciption
 * @date 2019/4/24 17:49
 **/
public class KafkaWriter  extends Writer {

    private final static String WRITE_COLUMNS = "columns";

    private final static int BATCH_SIZE=1024;

    public static class Job extends Writer.Job {

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

        Producer<String, String> producer;

        private Configuration conf = null;

        private List<KafkaColumn> columnList=null;

        private String topic="";

        private int count=0;

        @Override
        public void preCheck() {
            super.preCheck();
        }

        @Override
        public void prepare() {
        }

        @Override
        public void init() {
            log.info("初始化kafka");
            this.conf = super.getPluginJobConf();
            Properties properties = new Properties();
            properties.put("bootstrap.servers", conf.getString("bootstrapServers"));
            properties.put("groupId", conf.getString("groupId"));
            properties.put("acks", conf.getString("acks"));
            properties.put("retries", conf.getInt("retries"));
            properties.put("batch.size", conf.getInt("batchSize"));
            properties.put("linger.ms", conf.getInt("lingerMs"));
            properties.put("buffer.memory", conf.getLong("bufferMemory"));
            properties.put("key.serializer", conf.getString("keySerializer"));
            properties.put("value.serializer", conf.getString("valueSerializer"));
            topic=conf.getString("topic");
            producer = new KafkaProducer<String, String>(properties);

            //初始化列名
            columnList=JSONArray.parseArray(this.conf.getString(WRITE_COLUMNS),KafkaColumn.class);
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
                total += doBatchInsert(writerBuffer);
                writerBuffer.clear();
            }

            String msg = String.format("task end, write size :%d , msg count：%d", total,count);
            getTaskPluginCollector().collectMessage("writesize", String.valueOf(total));
            log.info(msg);
        }

        private long doBatchInsert(final List<Record> writerBuffer){
            int index=0;
            try {
                List<Map> dataList=new ArrayList();
                log.info("本次批量处理数据数：{}",writerBuffer.size());
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
                        producer.send(new ProducerRecord<String,String>(topic,JSON.toJSONString(dataList)));
                        dataList.clear();
                        count++;
                    }
                }

                if(!dataList.isEmpty()){
                    producer.send(new ProducerRecord<String,String>(topic,JSON.toJSONString(dataList)));
                    count++;
                }

            } catch (Exception e) {
                log.error(e.getMessage());
                throw DataXException.asDataXException(KafkaWriterErrorCode.EXECUTE_ERROR, e);
            }
            return index;
        }

        @Override
        public void destroy() {
            if(producer!=null){
                producer.close();
            }
        }
    }

}
