package com.globalegrow.bigdata.common.utils;

import org.yaml.snakeyaml.Yaml;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/7/18 9:53
 **/
public enum  YamlUtils {

    INSTANCE;
    Yaml yaml =new Yaml();

    public String yamlToString(Object data){
        return INSTANCE.yaml.dump(data);
    }

    public <T> T stringToObject(String content){
        return INSTANCE.yaml.load(content);
    }

}
