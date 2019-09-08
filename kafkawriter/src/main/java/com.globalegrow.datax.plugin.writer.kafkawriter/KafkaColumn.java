package com.globalegrow.datax.plugin.writer.kafkawriter;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 17:02
 **/
public class KafkaColumn {

    String name;

    String type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
