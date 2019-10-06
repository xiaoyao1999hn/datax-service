package org.datax.console.base.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/10/6 11:59
 **/
@Data
public class BaseDO {


    /**
     * 创建人
     */
    Long createUserId;

    /**
     * 创建时间
     */
    Date createDate;

    /**
     * 更新人
     */
    @NotNull(message = "更新人不能为空")
    Long updateUserId;


    /**
     * 更新时间
     */
    @NotNull(message = "更新时间不能为空")
    Date updateDate;

    String remark;

}
