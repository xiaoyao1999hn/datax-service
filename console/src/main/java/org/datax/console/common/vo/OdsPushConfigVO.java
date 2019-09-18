package org.datax.console.common.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.globalegrow.bigdata.bean.DataXColumn;
import com.globalegrow.bigdata.domain.OdsPushConfigDO;
import com.globalegrow.bigdata.enums.EnumJobType;
import com.globalegrow.bigdata.ods.admin.common.utils.EnumCycleType;
import com.globalegrow.bigdata.ods.admin.common.utils.TaskCronExpUtil;
import com.globalegrow.bigdata.utils.StringUtils;
import com.globalegrow.bigdata.utils.push.PushJobUtil;
import com.globalegrow.bigdata.vo.push.OdsPushColumnConfigVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * @author DKF
 * @date 2019/1/8
 */
@Data
public class OdsPushConfigVO extends OdsPushConfigDO {
    private String fromSourceName;
    private String toSourceName;
    /**
     * 数据源类型
     */
    private Integer fromDsType;

    /**
     * 目标源类型
     */
    private Integer toDsType;

    /**
     * 选择时间(多个时间以[英文逗号,]分隔)
     */
    private String selectTimes;

    /**
     * 周期执行时间
     */
    private String cycleTime;

    private List<OdsPushColumnConfigVO> columns;


    //============================ ES Parameters Start =======================================//
    private String indexName;
    private String typeName;
    //============================ ES Parameters End =========================================//


    private String fromDataSourceType;
    private String toDataSourceType;

    private List<String> sourceColumnName;
    private List<String> targetColumnName;
    private List<String> targetColumnType;

    //=============================hdfs 参数===============================================//
    /**
     * hdfs reader指定用户名
     */
    private String readerHdfsUserName;

    /**
     * hdfs writer指定用户名
     */
    private String writerHdfsUserName;

    /**
     * 配置信息（json格式）
     */
    private List<DataXColumn> partitionsColList;

    /**
     * 0-表示hdfs ，1-表示jdbc
     */
    private Integer userHiveJdbc;

    //=============================hdfs 参数===============================================//


    public OdsPushConfigVO() {
        super();
    }

    public OdsPushConfigVO(OdsPushConfigDO odsPushConfigDO) {
        super();
        // 父类转化为子类
        BeanUtils.copyProperties(odsPushConfigDO, this);
        setTaskInfo(odsPushConfigDO);
        setHdfsUserName();
        setPartitionsInfo();
    }

    private void setTaskInfo(OdsPushConfigDO odsPushConfigDO) {
        // 将cron 表达式解析出来
        Map<String, String> cronExpMap = TaskCronExpUtil.INSTANCE.conversionToCronExpMap(odsPushConfigDO.getCronExpression());
        // 周期执行时间
        setCycleTime(TaskCronExpUtil.INSTANCE.conversionToCycleTime(cronExpMap));
        // 选择时间
        setSelectTimes(TaskCronExpUtil.INSTANCE.conversionToSelectTime(cronExpMap,
                EnumJobType.getByType(odsPushConfigDO.getType()),
                EnumCycleType.getByType(odsPushConfigDO.getCycleType())));
    }

    public void setPartitionsInfo(){
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(this.getConfigContent())){
            JSONObject json=JSONObject.parseObject(this.getConfigContent());
            JSONArray array=json.getJSONArray("partitionsColList");
            if(array!=null){
                this.setPartitionsColList(array.toJavaList(DataXColumn.class));
            }
            if(json.getInteger("userHiveJdbc")==null||json.getInteger("userHiveJdbc")!=1){
                this.setUserHiveJdbc(0);
            }else {
                this.setUserHiveJdbc(1);
            }
        }
    }

    public void setHdfsUserName(){
        String jobConf=this.getJobConf();
        if(StringUtils.isNotEmpty(jobConf)){

            JSONObject reader= PushJobUtil.getReader(jobConf);
            JSONObject parameter=reader.getJSONObject("parameter");
            String hdfsUserName=parameter.getString("hdfsUserName");
            this.setReaderHdfsUserName(hdfsUserName);

            JSONObject writer= PushJobUtil.getWriter(jobConf);
            JSONObject parameter2=writer.getJSONObject("parameter");
            String hdfsUserName2=parameter2.getString("hdfsUserName");
            this.setWriterHdfsUserName(hdfsUserName2);
        }

    }
}
