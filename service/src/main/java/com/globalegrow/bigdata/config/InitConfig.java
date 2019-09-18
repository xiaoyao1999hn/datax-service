package com.globalegrow.bigdata.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.globalegrow.bigdata.common.utils.DerbyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/17 14:55
 **/
@Configuration
@Slf4j
public class InitConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl("jdbc:derby:derbydb/datax_metas_db;create=true");
        datasource.setUsername("datax_metas");
        datasource.setPassword("datax_metas");
        datasource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        return datasource;
    }

    @Bean
    JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource());
        return manager;
    }

    @PostConstruct
    public void initDerby(){
        DataSource dataSource = dataSource();
        if(!DerbyUtils.INSTANCE.checkTableExist(dataSource)){
            try {
                execute(dataSource.getConnection());
                } catch (Exception e) {
                log.error("初始化derby失败: "+e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取SQL文件
     *
     * @return sqls
     * @throws Exception Exception
     */
    private List<String> loadSql() throws Exception {
        List<String> sqlList = new ArrayList<String>();
        InputStream sqlFileIn = null;
        try {
            sqlFileIn = new ClassPathResource("derbydb/datax_metas.sql").getInputStream();
            StringBuffer sqlSb = new StringBuffer();
            byte[] buff = new byte[1024];
            int byteRead = 0;
            while ((byteRead = sqlFileIn.read(buff)) != -1) {
                sqlSb.append(new String(buff, 0, byteRead, "UTF-8"));
            }

            String[] sqlArr = sqlSb.toString().split(";");
            for (int i = 0; i < sqlArr.length; i++) {
                String sql = sqlArr[i].replaceAll("--.*", "").trim();
                if (StringUtils.isNotEmpty(sql)) {
                    sqlList.add(sql);
                }
            }
            return sqlList;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (sqlFileIn != null) {
                sqlFileIn.close();
            }
        }
    }

    /**
     * 执行SQL语句
     *
     * @param conn    connect
     * @throws Exception Exception
     */
    private void execute(Connection conn) throws Exception {
        Statement stmt = null;
        try {
            List<String> sqlList = loadSql();
            stmt = conn.createStatement();
            for (String sql : sqlList) {
                try {
                    stmt.execute(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
