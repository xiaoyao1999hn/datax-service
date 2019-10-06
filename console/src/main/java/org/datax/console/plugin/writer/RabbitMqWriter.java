package org.datax.console.plugin.writer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.ds.entity.config.RabbitMQDsConfig;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;
import org.datax.console.plugin.DataXPlugin;
import org.datax.console.push.vo.DataXPushTaskVO;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/21 8:49
 **/
@Getter
@Setter
public class RabbitMqWriter extends DataXPlugin {

    /**
     * 主机名
     */
    String host;

    /**
     * 端口
     */
    String port;

    /**
     * 用户名
     */
    String userName;

    /**
     * 密码
     */
    String password;

    /**
     * 超时时间
     */
    Integer timeout;

    /**
     * 虚拟主机名
     */
    String vhost;

    /**
     * 交换器
     */
    String exchange;

    /**
     * 路由key
     */
    String routeKey;

    /**
     * server push消息时的队列长度 - 同一时刻服务器只会发一条消息给消费者  - 非必需
     */
    Integer basicQos;

    /**
     * 消息是否确认
     */
    Integer ack;

    /**
     * 队列名称
     */
    String queueName;

    List<DataXColumn> column;

    public RabbitMqWriter(DataXPushTaskVO pushConfig) {
        super(pushConfig);
    }


    @Override
    public void initParams(DataXPushTaskVO pushConfig) {
        DataXDsVO dsVO = pushConfig.getDsInfo().get(DataXPlugin.Key.WRITER_DS_NAME);
        RabbitMQDsConfig dsConfigDO=dsVO.getConfig();
        BeanUtils.copyProperties(dsConfigDO,this);
        JSONObject config = JSONObject.parseObject(pushConfig.getConfigContent());
        JSONArray array=config.getJSONArray(Key.COLUMNS);
        if(array!=null){
            setColumn(array.toJavaList(DataXColumn.class));
        }
        this.setName(Key.NAME);
    }

    @Override
    protected void checkedConf(DataXPushTaskVO pushConfig) throws GlobalegrowExpcetion {

    }

    private static class Key {
        final static String COLUMNS = "columns";
        public final static String NAME = "rabbitmqwriter";
    }
}
