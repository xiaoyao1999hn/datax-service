package com.globalegrow.datax.plugin.reader.hivereader;

import com.alibaba.datax.common.plugin.RecordSender;
import com.alibaba.datax.common.spi.Reader;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.rdbms.reader.CommonRdbmsReader;
import com.alibaba.datax.plugin.rdbms.reader.Constant;
import com.alibaba.datax.plugin.rdbms.util.DataBaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Hive jdbc reader插件，这里需要先判断hive表是否已经落成，如果没有落成直接报错不往下执行
 * 防止hive跑mr任务，占用资源
 * @author ChengJie
 * @desciption
 * @date 2019/7/2 11:05
 **/
public class HiveReader extends Reader {


    private static final DataBaseType DATABASE_TYPE = DataBaseType.HIVE;

    public static class Job extends Reader.Job {
        private static final Logger LOG = LoggerFactory
                .getLogger(Job.class);

        private Configuration originalConfig = null;
        private CommonRdbmsReader.Job commonRdbmsReaderJob;

        @Override
        public void init() {
            this.originalConfig = super.getPluginJobConf();
            this.originalConfig.set(Constant.FETCH_SIZE, Integer.MIN_VALUE);
            this.commonRdbmsReaderJob = new CommonRdbmsReader.Job(DATABASE_TYPE);
            this.commonRdbmsReaderJob.init(this.originalConfig);
        }

        @Override
        public void preCheck(){
            init();
            this.commonRdbmsReaderJob.preCheck(this.originalConfig,DATABASE_TYPE);
        }

        @Override
        public List<Configuration> split(int adviceNumber) {
            return this.commonRdbmsReaderJob.split(this.originalConfig, adviceNumber);
        }

        @Override
        public void post() {
            this.commonRdbmsReaderJob.post(this.originalConfig);
        }

        @Override
        public void destroy() {
            this.commonRdbmsReaderJob.destroy(this.originalConfig);
        }

    }

    public static class Task extends Reader.Task {

        private Configuration readerSliceConfig;
        private CommonRdbmsReader.Task commonRdbmsReaderTask;

        @Override
        public void init() {
            this.readerSliceConfig = super.getPluginJobConf();
            this.commonRdbmsReaderTask = new CommonRdbmsReader.Task(DATABASE_TYPE,super.getTaskGroupId(), super.getTaskId());
            this.commonRdbmsReaderTask.init(this.readerSliceConfig);

        }

        @Override
        public void startRead(RecordSender recordSender) {
//            int fetchSize = this.readerSliceConfig.getInt(Constant.FETCH_SIZE);

            int fetchSize=Integer.MAX_VALUE;

            this.commonRdbmsReaderTask.startRead(this.readerSliceConfig, recordSender,
                    super.getTaskPluginCollector(), fetchSize);
        }

        @Override
        public void post() {
            this.commonRdbmsReaderTask.post(this.readerSliceConfig);
        }

        @Override
        public void destroy() {
            this.commonRdbmsReaderTask.destroy(this.readerSliceConfig);
        }

    }
}
