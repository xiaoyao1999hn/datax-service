package org.datax.console.common.utils.ds;

import lombok.extern.slf4j.Slf4j;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.common.utils.DbUtils;
import org.datax.console.ds.entity.config.RdbmsDsConfig;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.base.enums.DataSourceType;
import org.datax.console.plugin.DataXColumn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 11:41
 **/
@Slf4j
public class RdbmsDsUtil implements DsUtil{


    public static final String MYSQL_PREFIX = "jdbc:mysql://";

    public static final String MYSQL_SUFFIX =
            "?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&serverTimezone=GMT%2B8";

    public static final String MYSQL_CLASS_NAME = "com.mysql.jdbc.Driver";

    public static final String ORACLE_PREFIX = "jdbc:oracle:thin:@";

    public static final String ORACLE_SUFFIX = ":";

    public static final String ORACLE_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";

    public static final String PRESTO_PREFIX = "jdbc:presto://";

    public static final String PRESTO_CLASS_NAME = "com.facebook.presto.jdbc.PrestoDriver";

    public static final String ES_PREFIX="http://";

    public static final String HIVE_PREFIX = "jdbc:hive2://";

    public static final String HIVE_SUFFIX = "/default";

    public static final String HIVE_CLASS_NAME = "org.apache.hive.jdbc.HiveDriver";

    /**
     * 查询MYSQL全部表名语句
     */
    public static final String MYSQL_ALL_TABLENAME = "select table_name from information_schema.TABLES where TABLE_SCHEMA=";
    /**
     * 查询oracle全部表名语句
     */
    public static final String ORACLE_ALL_TABLENAME = "select table_name from user_tables ";

    /**
     * 查询语句排序
     */
    public static final String SQL_ORDER = " ORDER BY table_name ASC ";
    
    /**
     * 检测数据库是否可以连通
     *
     * @param dsVO 数据库连接对象
     * @return
     */
    @Override
    public  void testConnect(DataXDsVO dsVO) {
        RdbmsDsConfig dsConfigDO=dsVO.getConfig();
        Connection con = null;
        Statement stmt = null;
        try {
            // 加载数据库引擎，返回给定字符串名的类
            Class.forName(getDriverString(dsVO.getType()));
            // 设置连接超时时间,防止假死
            DriverManager.setLoginTimeout(3);
            // 连接数据库对象
            con = DriverManager.getConnection(getConnectUrl(dsVO.getType(), dsConfigDO.getHost(),
                    dsConfigDO.getPort(), dsConfigDO.getInstanceName()), dsConfigDO.getUserName(),
                    dsConfigDO.getPassword());
            // 创建SQL命令对象
            stmt = con.createStatement();
            // 执行查询,返回SQL语句查询结果集
            stmt.executeQuery(dsConfigDO.getValidateQuery());
        } catch (ClassNotFoundException e) {
            throw new GlobalegrowExpcetion("加载数据库引擎失败,请检查驱动配置是否正确");
        } catch (Exception e) {
            throw new GlobalegrowExpcetion("未知异常: " + e.getMessage());
        } finally {
            DbUtils.closeDbResources(stmt, con);
        }
    }

    /**
     * 拼接数据库连接字符串
     *
     * @param dsType
     * @param host
     * @param port
     * @param instanceName
     * @return
     */
    public static String getConnectUrl(Integer dsType, String host, String port, String instanceName) {
        StringBuffer url = new StringBuffer();
        if (DataSourceType.MYSQL.getType().equals(dsType)) {
            url.append(MYSQL_PREFIX).append(host).append(":").append(port).append("/")
                    .append(instanceName).append(MYSQL_SUFFIX);
        } else if (DataSourceType.ORACLE.getType().equals(dsType)) {
            url.append(ORACLE_PREFIX).append(host).append(":").append(port).append(":")
                    .append(instanceName);
        } else if (DataSourceType.PRESTO.getType().equals(dsType)) {
            url.append(PRESTO_PREFIX).append(host).append(":").append(port).append("/")
                    .append(instanceName).append(";SSL = 1;");
        } else if (DataSourceType.ES.getType().equals(dsType)) {
            url.append(ES_PREFIX).append(host).append(":").append(port);
        } else if (DataSourceType.HIVE.getType().equals(dsType)) {
            //这里需要带上队列名，不然会报错
            url.append(HIVE_PREFIX).append(host).append(":").append(port).append("/")
                    .append(instanceName).append("?mapreduce.job.queuename=root.bigdata_bi_offline");
        }
        return url.toString();
    }

    /**
     * 拼接数据库驱动字符串(目前只支持mysql和oracle，后续可以考虑接入db2，sqlserver等其他数据库)
     *
     * @param dsType
     * @return
     */
    public static String getDriverString(Integer dsType) {
        if (DataSourceType.MYSQL.getType().equals(dsType)) {
            return MYSQL_CLASS_NAME;
        } else if (DataSourceType.ORACLE.getType().equals(dsType)) {
            return ORACLE_CLASS_NAME;
        } else if (DataSourceType.PRESTO.getType().equals(dsType)) {
            return PRESTO_CLASS_NAME;
        } else if (DataSourceType.HIVE.getType().equals(dsType)) {
            return HIVE_CLASS_NAME;
        } else {
            return null;
        }
    }

    @Override
    public  List<String> getTables(DataXDsVO dsVO) {

        RdbmsDsConfig dsConfigDO=dsVO.getConfig();

        Connection con = null;
        Statement stmt = null;
        try {
            // 加载数据库引擎，返回给定字符串名的类
            Class.forName(getDriverString(dsVO.getType()));
            // 连接数据库对象
            con = DriverManager.getConnection(getConnectUrl(dsVO.getType(), dsConfigDO.getHost(),
                    dsConfigDO.getPort(), dsConfigDO.getInstanceName()), dsConfigDO.getUserName(),
                    dsConfigDO.getPassword());
            // 创建SQL命令对象
            stmt = con.createStatement();
            String sql;
            if (DataSourceType.MYSQL.getType().equals(dsVO.getType())) {
                sql = MYSQL_ALL_TABLENAME + " \'" + dsConfigDO.getInstanceName() + "\'"
                        + SQL_ORDER;
            } else if (DataSourceType.ORACLE.getType().equals(dsVO.getType())) {
                sql = ORACLE_ALL_TABLENAME + SQL_ORDER;
            } else if (DataSourceType.HIVE.getType().equals(dsVO.getType())) {
                //hive
                return DbUtils.getAllTables(con);
            } else {
                return null;
            }
            ResultSet rs = stmt.executeQuery(sql);

            List<String> listTables = new ArrayList<>();
            while (rs.next()) {
                listTables.add(rs.getString("table_name"));
            }
            return listTables;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            DbUtils.closeDbResources(stmt, con);
        }
    }

    @Override
    public List<DataXColumn> getColumns(DataXDsVO ds, String... tableName) {
        return null;
    }

    private static class   RdbmsDsUtilHandler{
        private   static final RdbmsDsUtil rdbmsDsUtil =new RdbmsDsUtil();
    }

    public static RdbmsDsUtil getInstance(){
        return RdbmsDsUtilHandler.rdbmsDsUtil;
    }

}
