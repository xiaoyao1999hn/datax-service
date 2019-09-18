package org.datax.console.plugin.reader;

import com.globalegrow.bigdata.domain.ds.config.OdsRdbmsDsConfigDO;
import com.globalegrow.bigdata.exception.GlobalegrowExpcetion;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.DataXPlugin;
import com.globalegrow.bigdata.vo.ds.OdsDsVO;
import com.globalegrow.bigdata.vo.push.config.ConnConfigVO;
import com.globalegrow.bigdata.vo.push.config.OdsPushTaskVO;
import lombok.Getter;
import lombok.Setter;

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

    public MysqlReader(OdsPushTaskVO pushConfig) {
        super(pushConfig);
    }

    @Override
    protected void initParams(OdsPushTaskVO pushConfig) {
        OdsDsVO odsDsVO=pushConfig.getDsInfo().get(DataXPlugin.Key.READER_DS_NAME);
        odsDsVO.build();

        OdsRdbmsDsConfigDO dsConfigDO= (OdsRdbmsDsConfigDO) odsDsVO.getConfig();

        setName(Key.NAME);
        setUsername(dsConfigDO.getUserName());
        setPassword(dsConfigDO.getPassword());
        setWhere(pushConfig.getRemark());
//        setColumns(pushConfig.get);
    }

    @Override
    protected void checkedConf(OdsPushTaskVO pushConfig) throws GlobalegrowExpcetion {

    }

    public static class Key {
        public final static String NAME = "mysqlreader";
        public final static String COLUMNS = "columns";
    }
}
