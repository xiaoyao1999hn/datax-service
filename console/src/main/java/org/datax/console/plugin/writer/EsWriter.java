package org.datax.console.plugin.writer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.globalegrow.bigdata.bean.DataXColumn;
import com.globalegrow.bigdata.domain.ds.config.OdsEsDsConfigDO;
import com.globalegrow.bigdata.exception.GlobalegrowExpcetion;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.DataXPlugin;
import com.globalegrow.bigdata.vo.ds.OdsDsVO;
import com.globalegrow.bigdata.vo.push.config.OdsPushTaskVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/9/8 15:57
 **/
@Getter
@Setter
public class EsWriter extends DataXPlugin {

    private String endpoint;
    private String accessId;
    private String accessKey;
    private Integer batchSize;
    private Integer timeout;
    private String index;
    private String type;
    private List<DataXColumn> columns;
    private Boolean cleanUp;

    public EsWriter(OdsPushTaskVO pushConfig) {
        super(pushConfig);
    }

    @Override
    public void initParams(OdsPushTaskVO pushConfig) {
        JSONObject config = JSONObject.parseObject(pushConfig.getConfigContent());
        JSONArray array=config.getJSONArray(Key.COLUMNS);
        if(array!=null){
            setColumns(array.toJavaList(DataXColumn.class));
        }
        OdsDsVO dsVO = pushConfig.getDsInfo().get(DataXPlugin.Key.WRITER_DS_NAME);
        dsVO.build();
        OdsEsDsConfigDO esConfig = (OdsEsDsConfigDO) dsVO.getConfig();
        setName(Key.NAME);
        BeanUtils.copyProperties(esConfig, this);
        setEndpoint("http://" + esConfig.getHost() + ":" + esConfig.getPort());
        String target = pushConfig.getToTable();
        setIndex(target.split("\\|")[0]);
        setType(target.split("\\|")[1]);
    }

    @Override
    protected void checkedConf(OdsPushTaskVO pushConfig) throws GlobalegrowExpcetion {

    }

    public static class Key {
        public final static String NAME = "elasticsearchwriter";
        public final static String COLUMNS = "columns";
    }
}