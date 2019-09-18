package org.datax.console.common.entity;

import java.util.Date;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/9/18 9:10
 **/
@Getter
@Setter
public class BaseEntity {
    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * 更新人名称
     */
    private String updateUserName;

    /**
     * 备注
     */
    private String remark;

}
