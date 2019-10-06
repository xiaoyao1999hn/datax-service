package org.datax.console.ds.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.datax.console.ds.entity.DataXDsDO;
import org.datax.console.enums.DataSourceType;
import java.util.Arrays;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 10:30
 **/
@Data
public class DataXDsVO extends DataXDsDO {

    /**
     * 机房名称
     */
    private String zoneName;

    /**
     * 监控点名称
     */
    private String checkPointName;

    /**
     * 配置信息
     */
    Object config;


    public void createConfig(){
        if(StringUtils.isNotEmpty(this.getConfigContent())){
            Arrays.stream(DataSourceType.values()).forEach(x->{
                if(x.getType().equals(this.getType())){
                    this.config=JSONObject.parseObject(this.getConfigContent(),x.getConfigClass());
                }
            });
        }
    }

    public <T> T getConfig(){
        if(this.config==null){
            createConfig();
        }
        return (T)config;
    }
}
