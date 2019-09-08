package com.globalegrow.bigdata.common.utils;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/25 11:44
 **/
public enum  DerbyUtils {

    INSTANCE;

    public boolean checkTableExist(DataSource dataSource){
        Connection connection=null;
        Statement statement=null;
        ResultSet rs=null;
        try {
            connection=dataSource.getConnection();
            statement=connection.createStatement();
            rs=statement.executeQuery("select count(1) from datax_report");
            return rs.next();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        return false;
    }

}
