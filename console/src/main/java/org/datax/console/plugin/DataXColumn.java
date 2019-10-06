package org.datax.console.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/20 17:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataXColumn {

    private Integer index;
    /**
     * field type
     */
    private String type;
    /**
     * const value
     */
    private String value;
    /**
     * date format
     */
    private String format;

    private String name;

}
