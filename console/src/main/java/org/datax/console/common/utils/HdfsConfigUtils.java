package org.datax.console.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.enums.ValueTypes;
import org.datax.console.plugin.DataXColumn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public final class HdfsConfigUtils {

    public static final String HDFS_NAME_SERVICES_NAME_KEY = "dfs.nameservices";
    public static final String DEFAULT_FS = "fs.defaultFS";
    public static final String LOCATION_KEY = "LOCATION";
    public static final Function<String, String> HDFS_NAME_SERVICES_LIST_KEY_FUN = nameServices -> String.format("dfs.ha.namenodes.%s", nameServices);
    public static final BiFunction<String, String, String> HDFS_NAMENODE_RPC_ADDRESS_KEY_FUN =
            (nameServices, nameNodeName) -> String.format("dfs.namenode.rpc-address.%s.%s", nameServices, nameNodeName);
    public static final Function<String, String> HDFS_FAILOVER_PROVIDER_KEY_FUN = nameServices -> String.format("dfs.client.failover.proxy.provider.%s", nameServices);

    private HdfsConfigUtils() {
        throw new IllegalStateException("no instance");
    }


    /**
     * get config hadoop for hive
     * <p>
     * "dfs.nameservices": "haCluster",
     * "dfs.ha.namenodes.haCluster": "nn1,nn2",
     * "dfs.namenode.rpc-address.haCluster.nn1": "10.60.49.23:8020",
     * "dfs.namenode.rpc-address.haCluster.nn2": "10.60.49.31:8020",
     * "dfs.client.failover.proxy.provider.haCluster": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"
     *
     * @param connection hive jdbc connection
     * @return hadoop config
     */
    public static Map<String, String> getHiveHadoopConfig(Connection connection, boolean closeConnection) {
        final Map<String, String> hadoopConfig = new HashMap<>();
        Objects.requireNonNull(connection, "connection");
        final String sql = "set ";
        Statement statement = null;
        ResultSet rs = null;
        try {
            String excuteSql=sql+HDFS_NAME_SERVICES_NAME_KEY;
            statement = connection.createStatement();
            String nameServices=queryHive(statement,excuteSql);

            if (StringUtils.isBlank(nameServices)) {
                log.error("hadoop config {} not exist, just skip", HDFS_NAME_SERVICES_NAME_KEY);
                return hadoopConfig;
            }
            //dfs.nameservices = haCluster
            hadoopConfig.put(HDFS_NAME_SERVICES_NAME_KEY, nameServices);

            //dfs.ha.namenodes.haCluster = nn1,nn2
            final String nameNodesKey = HDFS_NAME_SERVICES_LIST_KEY_FUN.apply(nameServices);
            excuteSql=sql+nameNodesKey;
            final String nameNodesValue=queryHive(statement,excuteSql);
            checkHadoopConfig(nameNodesKey, nameNodesValue, StringUtils::isNotBlank);
            hadoopConfig.put(nameNodesKey, nameNodesValue);

            //dfs.namenode.rpc-address.haCluster.nn1 = 10.60.49.23:8020
            //dfs.namenode.rpc-address.haCluster.nn2 = 10.60.49.31:8020
            String[] nameNodes = nameNodesValue.split(",");
            String rpcAddressKey;
            String rpcAddressValue;
            for (String nameNode : nameNodes) {
                rpcAddressKey = HDFS_NAMENODE_RPC_ADDRESS_KEY_FUN.apply(nameServices, nameNode);
                excuteSql=sql+rpcAddressKey;
                rpcAddressValue=queryHive(statement,excuteSql);
                checkHadoopConfig(rpcAddressKey, rpcAddressValue, StringUtils::isNotBlank);
                hadoopConfig.put(rpcAddressKey, rpcAddressValue);
            }

            //dfs.client.failover.proxy.provider.haCluster = org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider
            final String proxyProviderKey = HDFS_FAILOVER_PROVIDER_KEY_FUN.apply(nameServices);
            excuteSql=sql+proxyProviderKey;
            final String proxyProviderValue=queryHive(statement,excuteSql);
            checkHadoopConfig(proxyProviderKey, proxyProviderValue, StringUtils::isNotBlank);
            hadoopConfig.put(proxyProviderKey, proxyProviderValue);

            excuteSql=sql+DEFAULT_FS;
            final String defaultFs=queryHive(statement,excuteSql);
            hadoopConfig.put(DEFAULT_FS, defaultFs);
        } catch (SQLException e) {
            log.error("获取hiveConfig配置异常：{}",e);
            throw GlobalegrowExpcetion.unchecked(e);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (closeConnection) {
                DbUtils.closeDbResources(rs, statement, connection);
            } else {
                DbUtils.closeDbResources(rs, statement, null);
            }
        }
        return hadoopConfig;
    }

    /**
     * 获取分区表的路径
     * @param connection
     * @param tableName
     * @param metaList
     * @param closeConnection
     * @return
     */
    public static String getHdfsFilePath(Connection connection, String tableName, List<DataXColumn> metaList, boolean closeConnection){
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();

            //=============================待优化代码=================================
            //自动运算路径 desc formatted tableName partition(date='',site='');
            String colValue ="";
            StringBuffer tempSql =new StringBuffer();
            tempSql.append("desc formatted ").append(tableName);
            //如果是分区表则,按分区字段来
            if(metaList!=null&&metaList.size()>0){
                tempSql.append(" partition(");
                for (DataXColumn metaVO:metaList){
                    colValue =metaVO.getValue();
                    if(colValue.contains("#{")&&colValue.contains("}")){
                        colValue=DataXUtils.analysisParam(colValue,ValueTypes.getNameType(metaVO.getType()));
                    }
                    tempSql.append(metaVO.getName()).append("=");
                    if (ValueTypes.STRING.getType().equals(metaVO.getType()) || ValueTypes.DATE.getType().equals(metaVO.getType())) {
                        tempSql.append("'").append(colValue).append("'").append(",");
                    } else {
                        tempSql.append(colValue).append(",");
                    }
                }
                tempSql=tempSql.deleteCharAt(tempSql.lastIndexOf(","));
                tempSql.append(")");
            }
            rs = statement.executeQuery(tempSql.toString());
            while (rs.next()) {
                String key=rs.getString(1).trim();
                //这里要去掉：号不然会影响结果
                key=key.replace(":","");
                System.out.println(rs.getString(1).toUpperCase()+"\t"+rs.getString(2)+"\t"+rs.getString(3));
                if(LOCATION_KEY.equals(key.toUpperCase())){
                    return rs.getString(2);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            if (closeConnection) {
                DbUtils.closeDbResources(rs, statement, connection);
            } else {
                DbUtils.closeDbResources(rs, statement, null);
            }
        }
        return null;
    }

    private static String queryHive( Statement statement,String excuteSql) throws SQLException {
        ResultSet rs = statement.executeQuery(excuteSql);
        if (rs.next()) {
            String conf = rs.getString(1);
            log.info("成功获取配置conf: {}",conf);
            String[] kv = conf.split("=");
            if (kv.length == 2) {
                return  kv[1].trim();
            }
        }
        return null;
    }


    private static void checkHadoopConfig(String key, String value, Predicate<String> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        if (!predicate.test(value)) {
            throw new RuntimeException(String.format("Invalid hive hadoop config %s = %s", key, value));
        }
    }
}
