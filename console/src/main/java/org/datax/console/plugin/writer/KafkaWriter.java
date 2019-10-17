package org.datax.console.plugin.writer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.ds.entity.config.KafkaDsConfig;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;
import org.datax.console.plugin.DataXPlugin;
import org.datax.console.push.vo.DataXPushTaskVO;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/10/16 10:44
 **/
@Getter
@Setter
public class KafkaWriter extends DataXPlugin {

    private String topic;

    private String acks;

    private int retries;

    private int batchSize;

    private int lingerMs;

    private String groupId;

    private int bufferMemory;

    private String keySerializer;

    private String valueSerializer;

    private String bootstrapServers;

    private List<DataXColumn> columns;

    public KafkaWriter(DataXPushTaskVO pushConfig) {
        super(pushConfig);
    }

    @Override
    protected void initParams(DataXPushTaskVO pushConfig) {
        DataXDsVO dsVO = pushConfig.getDsInfo().get(DataXPlugin.Key.WRITER_DS_NAME);
        KafkaDsConfig dsConfigDO=dsVO.getConfig();
        BeanUtils.copyProperties(dsConfigDO,this);
        JSONObject config = JSONObject.parseObject(pushConfig.getConfigContent());
        JSONArray array=config.getJSONArray(Key.COLUMNS);
        if(array!=null){
            setColumns(array.toJavaList(DataXColumn.class));
        }
        this.setName(Key.NAME);
    }

    @Override
    protected void checkedConf(DataXPushTaskVO pushConfig) throws GlobalegrowExpcetion {

    }

    private static class Key {
        final static String COLUMNS = "columns";
        public final static String NAME = "kafkawriter";
    }
}
