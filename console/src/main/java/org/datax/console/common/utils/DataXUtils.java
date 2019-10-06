package org.datax.console.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.enums.ValueTypes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * User: jiangsongsong
 * Date: 2019/2/25
 * Time: 15:59
 */
public final class DataXUtils {

    public static final String DEFAULT_CONF_KEY_SEPARATOR = "\\.";


    private DataXUtils() {
        throw new IllegalStateException("no instance");
    }

    public static Object setJobConf(final JSONObject conf, final String key, final Object value, int keyIdxStart, boolean isOverride) {
        Objects.requireNonNull(conf, "conf");
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        String[] keys = key.split(DEFAULT_CONF_KEY_SEPARATOR);
        if (keyIdxStart < 0 || keyIdxStart >= keys.length) {
            throw new IllegalArgumentException(String.format("invalid key start index: %d, keys len: %d", keyIdxStart, keys.length));
        }

        keys = truncateKeys(keys, keyIdxStart);

        final String curKey = keys[0];
        if (keys.length == 1) {
            if (isOverride) {
                return conf.put(curKey, value);
            } else {
                return conf.putIfAbsent(curKey, value);
            }
        } else {
            JSONObject obj = conf.getJSONObject(curKey);
            if (obj == null) {
                obj = new JSONObject();
                conf.put(curKey, obj);
            }
            return setJobConf(obj, concatKeys(keys, 1), value, 0, isOverride);
        }
    }

    public static Object setJobConfIfAbsent(final JSONObject conf, final String key, final Object value, int keyIdxStart) {
        return setJobConf(conf, key, value, keyIdxStart, false);
    }

    private static String[] truncateKeys(String[] keys, int startIdx) {
        if (startIdx == 0) {
            return keys;
        }
        final String[] retKeys = new String[keys.length - startIdx];
        int retKeyIdx = 0;
        for (int i = startIdx; i < keys.length; i++) {
            retKeys[retKeyIdx++] = keys[i];
        }
        return retKeys;
    }

    private static String concatKeys(String[] keys, int startIdx) {
        String[] truncateKeys = truncateKeys(keys, startIdx);
        return String.join(DEFAULT_CONF_KEY_SEPARATOR, truncateKeys);
    }

    public static String resolveQueryCondition(String queryCondition){

        StringBuffer resultCondition=new StringBuffer();
        //如果包含这两个字符串说明是变量参数
        if(queryCondition.contains("#{")&&queryCondition.contains("}")){
            int index=0;
            while (index!=queryCondition.length()){
                queryCondition=queryCondition.substring(index,queryCondition.length());
                if(queryCondition.indexOf("#{")==-1){
                    if(queryCondition.length()!=0){
                        resultCondition.append(queryCondition);
                    }
                    break;
                }
                index=resolve(queryCondition,resultCondition);
            }
        }
        return resultCondition.toString();
    }

    private static int resolve(String temp,StringBuffer result){
        int begin=0;
        int end=0;
        int index=0;

        String str="#{";
        index=temp.indexOf(str);
        end=index;

        String condition=temp.substring(index,temp.indexOf("}")+1);
        index=temp.indexOf("}")+1;

        condition=condition.replace("#{","").replace("}","");
        String key=condition.split(",")[0];
        String value=condition.split(",")[1];
        if("now()".equals(key.trim())){
            result.append(temp.substring(begin,end));
            result.append("'").append(DateUtils.format(new Date(),value.trim())).append("'");
        }else{
            result.append(temp.substring(begin,index));
        }
        return index;
    }


    /**
     * 处理分区字段变量参数转换
     * 格式如下：#{value,format,method,variable}，目前只处理数字和时间格式的动态转换
     * value：具体的值（时间格式的数据可以用now()函数表示是今天），
     * format：数据的格式，如时间可以是yyyy-MM-dd，数字可以#0.00表示后面保留两位小数
     * method：+，-，*，/
     * variable: 变量的值，一般为整数，如果是字符串，则默认替换format里面的值
     * 例1：【时间类型】当前时间是2019-08-20，#{now(),yyyy-MM-dd,+,1}='2019-08-21'
     * 例2：【数字类型】#{20,#0.00,/,3}='6.66'
     * @param str
     * @param valueType
     * @return
     */
    public static String analysisParam(String str,ValueTypes valueType){

        str=str.replace("#{","").replace("}","");
        String[] params=str.split(",");

        String value=params[0].trim();
        String format=params[1].trim();
        String method=params[2].trim();
        String variable=params[3].trim();

        if(params.length!=4){
            throw new GlobalegrowExpcetion("格式不正确");
        }

        switch (valueType){
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case DECIMAL:
                if(!vadilateNumberType(value)||!vadilateNumberType(variable)){
                    throw new GlobalegrowExpcetion("非数字类型无法转换");
                }
                DecimalFormat decimalFormat = new DecimalFormat(format);
                BigDecimal bigDecimal=new BigDecimal(value);
                switch (method){
                    case "+":bigDecimal=bigDecimal.add(new BigDecimal(variable));break;
                    case "-":bigDecimal=bigDecimal.subtract(new BigDecimal(variable));break;
                    case "*":bigDecimal=bigDecimal.multiply(new BigDecimal(variable));break;
                    case "/":bigDecimal=bigDecimal.divide(new BigDecimal(variable));break;
                    default: throw new GlobalegrowExpcetion("运算符错误");
                }
                return decimalFormat.format(bigDecimal);
            case STRING:
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
                        throw new GlobalegrowExpcetion("时间格式异常");
                    }
                }
                //如果参数是0则说明不需要做任何运算
                if("0".equals(variable)){
                    return sdf.format(calendar.getTime());
                }
                switch (method){
                    case "+":calendar.add(Calendar.DAY_OF_YEAR,Integer.parseInt(variable));break;
                    case "-":calendar.add(Calendar.DAY_OF_YEAR,-Integer.parseInt(variable));break;
                    default: throw new GlobalegrowExpcetion("运算符异常,时间只支持加减运算");
                }
                return sdf.format(calendar.getTime());

            case BOOLEAN:throw new GlobalegrowExpcetion("Boolean类型数据无法转换");
            default: throw new GlobalegrowExpcetion("无可用类型");
        }
    }

    public final static boolean vadilateNumberType(String str){
        if (str != null && !"".equals(str.trim()))
            return str.matches("^(-?\\d+)(\\.\\d+)?$");
        else
            return false;
    }


}