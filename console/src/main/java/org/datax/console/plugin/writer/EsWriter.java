package org.datax.console.plugin.writer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.ds.entity.config.EsDsConfig;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;
import org.datax.console.plugin.DataXPlugin;
import org.datax.console.push.vo.DataXPushTaskVO;
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
    private Boolean cleanup;

    public EsWriter(DataXPushTaskVO pushConfig) {
        super(pushConfig);
    }

    @Override
    public void initParams(DataXPushTaskVO pushConfig) {
        JSONObject config = JSONObject.parseObject(pushConfig.getConfigContent());
        JSONArray array=config.getJSONArray(Key.COLUMNS);
        if(array!=null){
            setColumns(array.toJavaList(DataXColumn.class));
        }
        DataXDsVO dsVO = pushConfig.getDsInfo().get(DataXPlugin.Key.WRITER_DS_NAME);
        EsDsConfig esConfig = dsVO.getConfig();
        setName(Key.NAME);
        BeanUtils.copyProperties(esConfig, this);
        setEndpoint("http://" + esConfig.getHost() + ":" + esConfig.getPort());
        String target = pushConfig.getToTable();
        setIndex(target.split("\\|")[0]);
        setType(target.split("\\|")[1]);
    }

    @Override
    protected void checkedConf(DataXPushTaskVO pushConfig) throws GlobalegrowExpcetion {

    }

    public static class Key {
        public final static String NAME = "elasticsearchwriter";
        public final static String COLUMNS = "columns";
    }
}