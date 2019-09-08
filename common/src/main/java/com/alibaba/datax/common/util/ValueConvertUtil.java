package com.alibaba.datax.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * 字符串转换成数组，本工具类只实现了int，long，boolean，date等几种基本类型，有兴趣的可以自行添加
 * @author ChengJie
 * @desciption
 * @date 2019/2/13 17:50
 **/
public class ValueConvertUtil {

    //for lists
    public static <T, U> List<U> listConvertList(List<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }

    //for arrays
    public static <T, U> U[] arrayConvertArray(T[] from, Function<T, U> func, IntFunction<U[]> generator) {
        return Arrays.stream(from).map(func).toArray(generator);
    }


    /**
     * map的value转成list
     * @param map
     * @return
     */
    public static List<Object> MapValueToObjectList(Map<String,Object> map){
        if(map==null){
            return null;
        }
        List<Object> list = map.values().stream()
                .collect(Collectors.toList());
        return list;
    }


    /**
     * map的value转成list
     * @param map
     * @return
     */
    public static List<String> MapKeyToStringList(Map<String,Object> map){
        if(map==null){
            return null;
        }
        List<String> list = map.keySet().stream()
                .collect(Collectors.toList());
        return list;
    }


}
