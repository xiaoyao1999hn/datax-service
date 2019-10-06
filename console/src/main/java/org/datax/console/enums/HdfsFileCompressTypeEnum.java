package org.datax.console.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * User: jiangsongsong
 * Date: 2019/2/25
 * Time: 17:53
 */
public enum HdfsFileCompressTypeEnum {
    /**
     *
     * hive hdfs file compress type
     */
    GZIP("gzip"), BZIP2("bzip2"), ZIP("zip"), LZO("lzo"), LZO_DEFLATE("lzo_deflate"), HADOOP_SNAPPY("hadoop-snappy"), FRAMING_SNAPPY("framing-snappy");


    @Getter
    private final String type;

    HdfsFileCompressTypeEnum(String type) {
        this.type = type;
    }

    public static HdfsFileCompressTypeEnum fromType(String type) {
        if ("snappy".equalsIgnoreCase(type)) {
            return HADOOP_SNAPPY;
        }
        return Arrays.stream(values()).filter(t -> t.type.equalsIgnoreCase(type)).findAny()
            .orElseThrow(() -> new IllegalArgumentException("Invalid hive file compress type: " + type));
    }
}
