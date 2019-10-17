package org.datax.console.base.enums;

import java.util.Arrays;

/**
 * User: jiangsongsong
 * Date: 2019/2/25
 * Time: 17:25
 */
public enum HdfsFileTypeEnum {
    /**
     * hive hdfs file type
     */
    TEXT, CSV, SEQ, RC, ORC;


    public static HdfsFileTypeEnum fromType(String fileType) {
        return Arrays.stream(values()).filter(t -> t.name().equalsIgnoreCase(fileType)).findAny().orElseThrow(() -> new IllegalArgumentException("Invalid hive file type: " + fileType));
    }
}
