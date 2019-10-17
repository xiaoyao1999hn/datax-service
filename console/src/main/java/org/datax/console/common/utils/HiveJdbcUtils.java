package org.datax.console.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.base.enums.HdfsFileCompressTypeEnum;
import org.datax.console.base.enums.HdfsFileTypeEnum;
import org.datax.console.plugin.DataXColumn;
import org.javatuples.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: jiangsongsong
 * Date: 2019/3/2
 * Time: 10:16
 */
@Slf4j
public final class HiveJdbcUtils {

    private static final String SINGLE_QUOTE = "'";
    private final static Pattern FIELD_DELIMITER_PATTERN = Pattern.compile("FIELDS TERMINATED BY '(.+)'");
    private final static String INPUT_FORMAT_KEY = "STORED AS INPUTFORMAT";
    private final static String OUTPUT_FORMAT_KEY = "OUTPUTFORMAT";
    private final static String LOCATION_KEY = "LOCATION";
    private final static Pattern FILE_COMPRESS_PATTERN = Pattern.compile("'(.+)\\.compress'='(.+)',");

    private HiveJdbcUtils() {
        throw new IllegalStateException("no instance");
    }


    public static HiveTableMeta parseHiveTableMeta(Connection connection, String tableName, String partitionPath, List<DataXColumn> partitionsColList, boolean closeConnection) {
        Objects.requireNonNull(connection, "connection");
        Validate.isTrue(StringUtils.isNotBlank(tableName), "tableName require not null");

        final HiveTableMeta tableMeta = new HiveTableMeta();

        final String sql = String.format("show create table %s", tableName);

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            //=============================待优化代码=================================
            if(partitionsColList!=null&&partitionsColList.size()>0){
                //自动运算路径 desc formatted tableName partition(date='',site='');
                tableMeta.setFileLocation(HdfsConfigUtils.getHdfsFilePath(connection,tableName,partitionsColList,false));
            }

            //=====================================================================
            rs = statement.executeQuery(sql);
            String line;
            while (rs.next()) {
                line = rs.getString(1);
                if (StringUtils.isNotBlank(line)) {
                    line = line.trim();

                    /*
                     * field delimiter
                     *
                     * FIELDS TERMINATED BY '\u0001'
                     */
                    Matcher fieldDelimiterMatcher = FIELD_DELIMITER_PATTERN.matcher(line);
                    if (fieldDelimiterMatcher.find()) {
                        tableMeta.setFieldDelimiter(StringEscapeUtils.unescapeJava(fieldDelimiterMatcher.group(1)).charAt(0));
                        continue;
                    }

                    /*
                     * file compress
                     *
                     * 'orc.compress'='SNAPPY'
                     */
                    Matcher fileCompressMatcher = FILE_COMPRESS_PATTERN.matcher(line);
                    if (fileCompressMatcher.find()) {
                        tableMeta.setHdfsFileCompressTypeEnum(HdfsFileCompressTypeEnum.fromType(fileCompressMatcher.group(2)));
                        continue;
                    }

                    /*
                     * file location
                     * LOCATION
                     *      'hdfs://dwtestNameService/bigdata/dm/dss/dm_dss_prf_order_stat_dtl_f3'
                     */
                    if (LOCATION_KEY.equals(line)) {
                        rs.next();
                        line = rs.getString(1).trim();
                        Pair<String, String> pair = parseHiveLocation(removeSingleQuote(line));
                        tableMeta.setDefaultFs(pair.getValue0());
                        if(StringUtils.isEmpty(tableMeta.getFileLocation())){
                            //文件原路径
                            tableMeta.setFileLocation(pair.getValue1());
                            if(StringUtils.isNotEmpty(partitionPath)){
                                tableMeta.setFileLocation(pair.getValue1()+partitionPath);
                            }
                        }else{
                            //如果是分区表，前面获取到的就是完整的路径了，这里要先去掉头部
                            tableMeta.setFileLocation(tableMeta.getFileLocation().replace(pair.getValue0(),""));
                        }
                        continue;
                    }

                    /*
                     * STORED AS INPUTFORMAT
                     *      org.apache.hadoop.hive.ql.io.orc.OrcInputFormat
                     */
                    if (INPUT_FORMAT_KEY.equals(line)) {
                        //move forward to next line
                        rs.next();
                        line = rs.getString(1).trim();
                        tableMeta.setHdfsFileTypeEnum(parseFileType(removeSingleQuote(line)));
                    }
                }
            }
        } catch (SQLException e) {
            throw GlobalegrowExpcetion.unchecked(e);
        } finally {
            if (closeConnection) {
                DbUtils.closeDbResources(rs, statement, connection);
            } else {
                DbUtils.closeDbResources(rs, statement, null);
            }
        }
        return tableMeta;
    }

    private static HdfsFileTypeEnum parseFileType(String inputFormat) {
        Objects.requireNonNull(inputFormat, "inputFormat");

        switch (inputFormat) {
            case "org.apache.hadoop.mapred.TextInputFormat":
                return HdfsFileTypeEnum.TEXT;
            case "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat":
                return HdfsFileTypeEnum.ORC;
            case "org.apache.hadoop.hive.ql.io.RCFileInputFormat":
                return HdfsFileTypeEnum.RC;
            case "org.apache.hadoop.mapred.SequenceFileInputFormat":
                return HdfsFileTypeEnum.SEQ;
            default:
                throw new IllegalArgumentException("not supported hive file input format: " + inputFormat);
        }
    }


    /**
     * hdfs://dwtestNameService/apps/hive/warehouse/test01
     *
     * @param fileLocation hdfs://dwtestNameService/apps/hive/warehouse/test01
     * @return defaultFs -> absolute hive file path eg: hdfs://dwtestNameService -> /apps/hive/warehouse/test01
     */
    private static Pair<String, String> parseHiveLocation(String fileLocation) {
        Validate.isTrue(StringUtils.isNotBlank(fileLocation), "fileLocation require not null");
        final String hdfsPrefix = "hdfs://";
        if (fileLocation.startsWith(hdfsPrefix)) {
            int separatorIndex = fileLocation.indexOf("/", hdfsPrefix.length());
            String defaultFs = fileLocation.substring(0, separatorIndex);
            String filePath = fileLocation.substring(separatorIndex);
            return Pair.with(defaultFs, filePath);
        }
        throw new IllegalArgumentException("invalid file location: " + fileLocation);
    }


    private static String removeSingleQuote(String src) {
        if (StringUtils.isNotBlank(src)) {
            src = src.trim();
            if (src.startsWith(SINGLE_QUOTE) && src.endsWith(SINGLE_QUOTE)) {
                src = src.substring(1, src.length() - 1);
            }
        }
        return src;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HiveTableMeta {
        private String defaultFs;
        private String fileLocation;
        private HdfsFileTypeEnum hdfsFileTypeEnum;
        private HdfsFileCompressTypeEnum hdfsFileCompressTypeEnum;
        private Character fieldDelimiter;
        private String hdfsUserName;
    }
}
