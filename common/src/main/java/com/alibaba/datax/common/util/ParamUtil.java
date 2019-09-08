package com.alibaba.datax.common.util;

import com.alibaba.datax.common.enums.ValueTypes;
import com.alibaba.datax.common.exception.CommonErrorCode;
import com.alibaba.datax.common.exception.DataXException;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/9/5 12:20
 **/
public class ParamUtil {

    public  static final String PREFIX="#{";

    public static final String SUFFIX="}";

    public static final String  DELIMITER=",";

    public static final String  ADD="+";

    public static final String  SUBTRACT="-";

    public static final String  MULTIPLY="*";

    public static final String  DIVIDE="/";

    public static String multipleAnalysisParam(String condition){
        StringBuffer resultCondition=new StringBuffer();
        //如果包含这两个字符串说明是变量参数
        int beginIndex=condition.indexOf("#{");
        int endIndex=condition.indexOf("}")+1;
        int index=0;
        Pair<ValueTypes,String> result;
        while (beginIndex!=-1&&endIndex!=-1){
            String temp=condition.substring(beginIndex,endIndex);
            result=analysisParam(temp);
            temp=result.getValue();
            if(ValueTypes.NUMBER.equals(result.getKey())){
                resultCondition.append(condition.substring(index,beginIndex)).append(temp);
            }else{
                resultCondition.append(condition.substring(index,beginIndex)).append("'").append(temp).append("'");
            }
            condition=condition.substring(endIndex,condition.length());
            beginIndex=condition.indexOf("#{");
            endIndex=condition.indexOf("}")+1;
        }
        return resultCondition.toString();
    }

    /**
     * 处理分区字段变量参数转换
     * 格式如下：#{value,format,method,variable}，目前只处理数字和时间格式的动态转换
     * value：具体的值（时间格式的数据可以用now()函数表示是今天），
     * format：数据的格式，如时间可以是yyyy-MM-dd，数字可以#0.00表示后面保留两位小数
     * method：+，-，*，/
     * variable: 变量的值，一般为整数，如果是字符串，则默认替换format里面的值
     * 例1：【时间类型】当前时间是2019-08-20，#{String,now(),yyyy-MM-dd,+,1}='2019-08-21'
     * 例2：【数字类型】#{Double,20,#0.00,/,3}='6.66'
     * @param str
     * @return
     */
    public static Pair<ValueTypes,String> analysisParam(String str){

        /**
         * 如果#{和}不成对出现则按照原样返回
         */
        if(!(str.contains(PREFIX)&&str.contains(SUFFIX))){
            return new Pair<>(ValueTypes.STRING,str);
        }

        str=str.replace(PREFIX,"").replace(SUFFIX,"");
        String[] params=str.split(DELIMITER);

        String type=params[0].trim();
        String value=params[1].trim();
        String format=params[2].trim();
        String method=params[3].trim();
        String variable=params[4].trim();

        if(params.length!=5){
            throw new DataXException(CommonErrorCode.PARAMS_ERROR,"格式不正确");
        }

        ValueTypes valueType=ValueTypes.getNameType(type);

        switch (valueType){
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case DECIMAL:
                if(!vadilateNumberType(value)||!vadilateNumberType(variable)){
                    throw new DataXException(CommonErrorCode.PARAMS_ERROR,"非数字类型无法转换");
                }
                DecimalFormat decimalFormat = new DecimalFormat(format);
                BigDecimal bigDecimal=new BigDecimal(value);
                switch (method){
                    case ADD:bigDecimal=bigDecimal.add(new BigDecimal(variable));break;
                    case SUBTRACT:bigDecimal=bigDecimal.subtract(new BigDecimal(variable));break;
                    case MULTIPLY:bigDecimal=bigDecimal.multiply(new BigDecimal(variable));break;
                    case DIVIDE:bigDecimal=bigDecimal.divide(new BigDecimal(variable));break;
                    default: throw new DataXException(CommonErrorCode.PARAMS_ERROR,"运算符错误");
                }
                return new Pair<>(ValueTypes.NUMBER,decimalFormat.format(bigDecimal));
            case TIMESTAMP:
            case DATE:
                SimpleDateFormat sdf= new SimpleDateFormat(format);
                Calendar calendar =Calendar.getInstance();
                if("now()".equals(value)){
                    calendar.setTime(new Date());
                }else{
                    try{
                        calendar.setTime(sdf.parse(value));
                    }catch (ParseException e){
                        throw new DataXException(CommonErrorCode.PARAMS_ERROR,"时间格式异常");
                    }
                }
                //如果参数是0则说明不需要做任何运算
                if("0".equals(variable)){
                    return new Pair<>(ValueTypes.DATE,sdf.format(calendar.getTime()));
                }
                switch (method){
                    case ADD:calendar.add(Calendar.DAY_OF_YEAR,Integer.parseInt(variable));break;
                    case SUBTRACT:calendar.add(Calendar.DAY_OF_YEAR,-Integer.parseInt(variable));break;
                    default: throw new DataXException(CommonErrorCode.PARAMS_ERROR,"运算符异常,时间只支持加减运算");
                }
                return new Pair<>(ValueTypes.DATE,sdf.format(calendar.getTime()));

            case BOOLEAN:throw new DataXException(CommonErrorCode.PARAMS_ERROR,"Boolean类型数据无法转换");
            case STRING: throw new DataXException(CommonErrorCode.PARAMS_ERROR,"字符串类型数据无法转换");
            default: throw new DataXException(CommonErrorCode.PARAMS_ERROR,"无可用类型");
        }
    }

    public final static boolean vadilateNumberType(String str){
        if (str != null && !"".equals(str.trim()))
            return str.matches("^(-?\\d+)(\\.\\d+)?$");
        else
            return false;
    }

    public static void main(String[] args){
        String sql="select * from table a where a=#{Date,now(),yyyyMMdd,+,0} and b=#{Date,2019-06-20,yyyy-MM-dd,+,2} and c=#{Double,22.6,,*,5}";

        multipleAnalysisParam(sql);
    }
}
