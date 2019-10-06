package org.datax.console.push.vo;
import lombok.Data;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;
import org.datax.console.push.entity.DataXPushTaskDO;

import java.util.List;
import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/24 17:17
 **/
@Data
public class DataXPushTaskVO extends DataXPushTaskDO {

    /**
     * 源数据源名称
     */
    private String fromSourceName;

    /**
     * 目标数据源名称
     */
    private String toSourceName;

    private Integer fromDsType;

    private Integer toDsType;

    /**
     * 数据源信息
     */
    private Map<String,DataXDsVO> dsInfo;

    /**
     * 读插件类型
     */
    private Integer readerType;

    /**
     * 写插件类型
     */
    private Integer writerType;

    private List<DataXColumn> partitionsColList;

    private Integer isPartitioned;

    private Boolean userHiveJdbc;

    private List<String> sourceColumnName;



}
