package org.datax.console.plugin;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/20 14:47
 **/
@Getter
@Setter
@NoArgsConstructor
public class DataXJobConf {

    public static String build(List<DataXContent> content,DataXSetting setting){
        JSONObject result = new JSONObject();
        JSONObject conf=new JSONObject();
        conf.put("content",content);
        conf.put("setting",setting);
        result.put("job",conf);
        return result.toJSONString();
    }

    public static String build(DataXContent content,DataXSetting setting){
        List<DataXContent> list = new ArrayList<>();
        list.add(content);
        return build(list,setting);
    }


    public static String buildDefault(DataXPlugin reader,DataXPlugin writer){
        List<DataXContent> list = new ArrayList<>();
        list.add(DataXContent.defaultCreate(reader,writer));
        return build(list, DataXSetting.defaultCreate());
    }

    public static DataXJobConf createJobConf(){
        return new DataXJobConf();
    }

    public static DataXContent createContent(DataXPlugin reader,DataXPlugin writer){
        return new DataXContent(reader,writer);
    }

    public static DataXSetting createSetting(){
        return new DataXSetting();
    }

    @Setter
    @Getter
    public static class DataXContent{
        JSONObject writer;
        JSONObject reader;

        /**
         * 这里之所以不直接采用reader和writer转json，是因为里面如果插件属性每个都重新定义一遍会与数据源对象有冲突
         * 同时如果新增个性化字段不利于拓展
         * @param writer
         * @param reader
         */
        DataXContent(DataXPlugin reader,DataXPlugin writer){
            this.writer=writer.build();
            this.reader=reader.build();
        }

        public static DataXContent defaultCreate(DataXPlugin reader,DataXPlugin writer){
            return new DataXContent(reader,writer);
        }
    }

    @Setter
    @Getter
    public static class DataXSetting{
        JSONObject errorLimit =new JSONObject();
        JSONObject speed=new JSONObject();

        public  static  DataXSetting defaultCreate(){
            DataXSetting setting =new DataXSetting();
            setting.addSpeed("channel",5);
            setting.addSpeed("record",1);
            return setting;
        }

        public void addErrorLimitConf(String key,Object value){
            errorLimit.put(key,value);
        }

        public void addSpeed(String key,Object value){
            speed.put(key,value);
        }
    }
}
