package org.datax.console.push.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/24 17:19
 **/
@Data
public class DataXPushTaskDO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 推数名称
     */
    private String name;

    /**
     * 源数据Id
     */
    private Long fromSourceId;

    /**
     * 源-数据项
     */
    private String fromTable;

    /**
     * 目标数据源ID
     */
    private Long toSourceId;

    /**
     * 目的-数据项
     */
    private String toTable;

    /**
     * 查询条件，类似datax的where
     */
    private String queryCondition;

    /**
     * 推数方式：0-datax，1-待定
     */
    private Integer pushWay;

    /**
     * 推数类型:0-全量 1-增量
     */
    private Integer pushType;

    /**
     * 通道数量-用于控制速度，channel数越大速度越快
     */
    private Integer channelNum;

    /**
     * 状态：0-未启用，1-已启用
     */
    private Integer status;

    /**
     * 执行状态:0-未启动,1-正常，2-异常
     */
    private Integer exeStatus;

    /**
     * datax配置
     */
    private String jobConf;

    /**
     * 任务执行次数
     */
    private Integer exeNum;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 计划任务类型：0-频率执行；1-固定时间；2-指定时间
     */
    private Integer type;

    /**
     * 周期执行类型
     */
    private Integer cycleType;

    /**
     * 运行频率
     */
    private String cronExpression;

    /**
     * 配置信息,个性化配置的信息都在这里,如es需要accessId等
     */
    private String configContent;

    /**
     * 监控点id
     */
    private Long checkPointId;

    /**
     * 回调接口：多个url用，号隔开
     */
    private String callback;

    /**
     * 创建用户id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改用户id
     */
    private Long updateUserId;

    /**
     *
     */
    private String updateUserName;

    /**
     * 修改时间
     */
    private Date updateDate;

    /**
     * 备注
     */
    private String remark;

}
