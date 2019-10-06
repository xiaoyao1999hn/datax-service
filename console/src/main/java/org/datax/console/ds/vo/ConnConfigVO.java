package org.datax.console.ds.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/9/17 9:29
 **/
@Getter
@Setter
public class ConnConfigVO {

    private List<String> jdbcUrl;

    private List<String> table;

}
