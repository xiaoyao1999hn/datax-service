package org.datax.console.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.enums.DbTypeEnum;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库操作类
 */
@Slf4j
public final class DbUtils {


    private DbUtils() {
        throw new IllegalStateException("no instance");
    }


    public static Connection getConnection(DbTypeEnum dbType, String host, int port, String database, String username, String password) {
        Objects.requireNonNull(dbType, "dbType");
        return getConnection(dbType.genJdbcUrl(host, port, database), username, password);
    }


    public static Connection getConnection(String jdbcUrl, String username, String password) {
        Validate.isTrue(StringUtils.isNotBlank(jdbcUrl), "jdbc url require not null");
        DbTypeEnum dbTypeEnum = DbTypeEnum.fromTypeName(parseDriverTypeFromJdbcUrl(jdbcUrl));
        try {
            Class.forName(dbTypeEnum.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new GlobalegrowExpcetion("not found jdbc driver class", e);
        }
        try {
            DriverManager.setLoginTimeout(30);
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new GlobalegrowExpcetion("failed to get connection", e);
        }
    }


    public static List<Column> getAllColumns(Connection connection, String tableName) {
        return getColumns(connection, tableName, null);
    }



    public static List<Column> getColumns(Connection connection, String tableName, List<String> columns) {
        Validate.isTrue(null != connection, "connection require not null");
        Validate.isTrue(StringUtils.isNotBlank(tableName), "tableName require not null");
        final Set<String> columnNames = new HashSet<>();
        if (columns == null || columns.size() <= 0) {
            columnNames.add("*");
        } else {
            columnNames.addAll(columns);
        }
        final List<Column> columnList = new ArrayList<>();
        String joinedColumnNames = columnNames.stream().filter(Objects::nonNull).map(String::trim).collect(Collectors.joining(","));
        final String sql = String.format("select %s from %s limit 0", joinedColumnNames, tableName);

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();

            String columnName;
            String columnLabel;
            JDBCType jdbcType;
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                columnName = metaData.getColumnName(i + 1);
                columnLabel = metaData.getColumnLabel(i + 1);
                jdbcType = JDBCType.valueOf(metaData.getColumnType(i + 1));
                //for hive
                if (columnName.startsWith(tableName + ".")) {
                    columnName = columnName.replaceFirst(tableName + ".", "");
                }
                columnList.add(new Column(columnName, columnLabel, jdbcType));
            }
        } catch (SQLException e) {
            throw GlobalegrowExpcetion.unchecked(e);
        } finally {
            closeDbResources(rs, statement, connection);
        }
        return columnList;
    }


    public static List<String> getAllTables(Connection connection) {
        Validate.isTrue(null != connection, "connection require not null");
        final String sql = "show tables";

        final List<String> tables = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw GlobalegrowExpcetion.unchecked(e);
        } finally {
            closeDbResources(rs, statement, connection);
        }
        return tables;
    }


    public static void closeDbResources(Statement statement, Connection connection) {
        closeDbResources(null, statement, connection);
    }



    public static void closeDbResources(ResultSet rs, Statement statement, Connection connection) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignored) {
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }


    public static String parseDriverTypeFromJdbcUrl(String jdbcUrl) {
        Validate.isTrue(StringUtils.isNotBlank(jdbcUrl), "jdbc url require not null");
        String[] split = jdbcUrl.trim().split(":");
        if (split.length >= 2) {
            return split[1];
        }
        throw new GlobalegrowExpcetion("invalid jdbc url: " + jdbcUrl);
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Column {
        private String columnName;
        private String columnLabel;
        private JDBCType jdbcType;
    }

}
