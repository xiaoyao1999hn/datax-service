package org.datax.console.plugin.reader;

import com.alibaba.fastjson.JSONObject;
import com.globalegrow.bigdata.bean.DataXColumn;
import com.globalegrow.bigdata.domain.ds.config.OdsHiveDsConfigDO;
import com.globalegrow.bigdata.enums.DbTypeEnum;
import com.globalegrow.bigdata.exception.GlobalegrowExpcetion;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.DataXPlugin;
import com.globalegrow.bigdata.ods.admin.push.utils.HiveJdbcUtils;
import com.globalegrow.bigdata.utils.DbUtils;
import com.globalegrow.bigdata.utils.hdfs.HdfsConfigUtils;
import com.globalegrow.bigdata.vo.ds.OdsDsVO;
import com.globalegrow.bigdata.vo.push.config.OdsPushTaskVO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/20 16:45
 **/
@Getter
@Setter
@Slf4j
public class HdfsReader extends DataXPlugin {

    /**
     * hdfs文件路径
     */
    String path;

    /**
     * 默认hdfs域名
     */
    String defaultFS;

    /**
     * 获取的列信息
     */
    List<DataXColumn> column;

    /**
     * 文件类型
     */
    String fileType;

    /**
     * 编码格式
     */
    String encoding;

    /**
     * 文件分隔符
     */
    Character fieldDelimiter;

    /**
     * hadoop配置
     */
    Map<String, String> hadoopConfig;

    /**
     * 分区信息
     */
    List<DataXColumn> partitionParam;

    /**
     * 默认附加列值
     */
    List<DataXColumn> defaultValue;

    /**
     * hdfs用户名，方便程序执行时用相对应的用户去执行程序
     */
    String hdfsUserName;

    /**
     * 压缩方式
     * {@link com.globalegrow.bigdata.ods.admin.push.datax.conf.enums.HdfsFileCompressTypeEnum}
     */
    String compress;

    /**
     * 空字符格式（一般默认是\\N，为了防止其他奇葩的格式，这里我们需要传到hdfsreader插件指定对应的配置）
     */
    String nullFormat;

    public HdfsReader(OdsPushTaskVO pushConfig) {
        super(pushConfig);
    }

    @Override
    protected void initParams(OdsPushTaskVO pushConfig) {
        //初始化jdbc连接，通过hive jdbc set指令获取hadoop的配置信息
        OdsDsVO dsVO =pushConfig.getDsInfo().get(Key.HDFS_DS);
        dsVO.build();
        final OdsHiveDsConfigDO configDO = (OdsHiveDsConfigDO)dsVO.getConfig();
        Connection connection = DbUtils.getConnection(DbTypeEnum.HIVE2, configDO.getHost(), Integer.parseInt(configDO.getPort()), configDO.getInstanceName(), configDO.getUserName(), configDO.getPassword());

        //初始化hadoop配置
        initHadoopConf(connection, false);

        JSONObject config=JSONObject.parseObject(pushConfig.getConfigContent());
        //初始化hdfs配置
        HiveJdbcUtils.HiveTableMeta tableMeta = HiveJdbcUtils.parseHiveTableMeta(connection,
                pushConfig.getFromTable(),
                config.getString("patitionPath"),
                config.getJSONArray("partitionColList").toJavaList(DataXColumn.class),
                false);

        //将共同属性复制过来
        BeanUtils.copyProperties(tableMeta, this);

        //设置hdfs文件路径
        path = tableMeta.getFileLocation();

        //设置datax执行程序使用的用户信息
        hdfsUserName = configDO.getHdfsUserName();

        //设置文件压缩类型
        if (tableMeta.getHdfsFileCompressTypeEnum() != null) {
            compress = tableMeta.getHdfsFileCompressTypeEnum().getType();
        }

        //设置文件类型
        if (tableMeta.getHdfsFileTypeEnum() != null) {
            fileType = tableMeta.getHdfsFileTypeEnum().toString();
        }

        //设置文件分隔符
        fieldDelimiter = fieldDelimiter == null ? Key.DEFAULT_FIELD_TERMINATOR : fieldDelimiter;
//        nullFormat=tableMeta.get;

        initColumn(pushConfig,connection,true);


    }

    @Override
    protected void checkedConf(OdsPushTaskVO pushConfig) throws GlobalegrowExpcetion {

    }

    /**
     * 通过hive jdbc的方式去获取hadoop集群的元信息以及获取分区表路径
     *
     * @param connection
     * @param closeConnection
     */
    private void initHadoopConf(Connection connection, boolean closeConnection) {
        hadoopConfig = HdfsConfigUtils.getHiveHadoopConfig(connection, closeConnection);
    }

    private void initColumn(OdsPushTaskVO config, Connection connection, boolean closeConnection) {

//        if (columnList == null || columnList.size() <= 0) {
//            return Collections.emptyList();
//        }
//
//        final List<HdfsReaderConf.ColumnEntry> data = new ArrayList<>(columnList.size());
//        HdfsReaderConf.ColumnEntry ce;
//        for (ColumnMeta column : columnList) {
//            ce = new HdfsReaderConf.ColumnEntry();
//            ce.setIndex(column.getOrdinalPosition() - 1);
//            ce.setType(HdfsFieldTypeEnum.convertJDBCTypeToHdfsFieldType(JDBCType.valueOf(column.getDataType())).name());
//            ce.setName(column.getColumnName());
//            data.add(ce);
//        }
//
//        column.addAll(defaultValue);
    }


    public static class Key {

        final static String HDFS_DS="hdfsDs";

        /**
         * [/user/hive/warehouse/test_table]
         */
        final static String PATH = "path";
        /**
         * hdfs://ip:host  or hdfs://haCluster
         */
        final static String DEFAULT_FS = "defaultFS";
        /**
         * "dfs.nameservices": "haCluster",
         * "dfs.ha.namenodes.haCluster": "nn1,nn2",
         * "dfs.namenode.rpc-address.haCluster.nn1": "10.60.49.23:8020",
         * "dfs.namenode.rpc-address.haCluster.nn2": "10.60.49.31:8020",
         * "dfs.client.failover.proxy.provider.haCluster": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"
         */
        final static String HADOOP_CONFIG = "hadoopConfig";
        /**
         * {@link DataXColumn}
         */
        final static String COLUMN = "column";
        /**
         * {@link com.globalegrow.bigdata.enums.hdfs.HdfsFileTypeEnum}
         */
        final static String FILE_TYPE = "fileType";
        /**
         * UTF-8
         */
        final static String ENCODING = "encoding";
        /**
         * ,
         */
        final static String FIELD_DELIMITER = "fieldDelimiter";
        /**
         * {@link com.globalegrow.bigdata.enums.hdfs.HdfsFileCompressTypeEnum}
         */
        final static String COMPRESS = "compress";

        final static String NULL_FORMAT = "nullFormat";

        final static String HDFS_USER_NAME = "hdfsUserName";

        final static String PARTITION_PARAM = "partitionParam";

        final static Character DEFAULT_FIELD_TERMINATOR = '\u0001';

    }
}
