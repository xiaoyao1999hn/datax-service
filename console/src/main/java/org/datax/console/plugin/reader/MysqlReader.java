package org.datax.console.plugin.reader;

import lombok.Getter;
import lombok.Setter;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.ds.entity.config.RdbmsDsConfig;
import org.datax.console.ds.vo.ConnConfigVO;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXPlugin;
import org.datax.console.push.vo.DataXPushTaskVO;

import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/20 16:49
 **/
@Getter
@Setter
public class MysqlReader extends DataXPlugin {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 查询条件
     */
    private String where;

    /**
     * 连接信息
     */
    private List<ConnConfigVO> connection;

    /**
     * 列名
     */
    private List<String> columns;

    public MysqlReader(DataXPushTaskVO pushConfig) {
        super(pushConfig);
    }

    @Override
    protected void initParams(DataXPushTaskVO pushConfig) {
        DataXDsVO odsDsVO=pushConfig.getDsInfo().get(DataXPlugin.Key.READER_DS_NAME);
        RdbmsDsConfig dsConfigDO= odsDsVO.getConfig();
        setName(Key.NAME);
        setUsername(dsConfigDO.getUserName());
        setPassword(dsConfigDO.getPassword());
        setWhere(pushConfig.getRemark());
    }

    @Override
    protected void checkedConf(DataXPushTaskVO pushConfig) throws GlobalegrowExpcetion {

    }

    public static class Key {
        public final static String NAME = "mysqlreader";
        public final static String COLUMNS = "columns";
    }
}
