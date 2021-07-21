package com.github.jadepeng.pipeline.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by junmeng on 2019/1/31
 */
public class StrUtil {

    private static Pattern chinese = Pattern.compile("[\u4e00-\u9fa5]");

    private static String symbol = "[\n`~!@#$%^&*()\\-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";

    /**
     * 字符串首字母转小写
     *
     * @param str 待转换的字符串
     * @return
     */
    public static String toLowerCaseFirstOne(String str) {
        if (StringUtils.isBlank(str) || Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }

    /**
     * 字符串首字母转大写
     *
     * @param str 待转换的字符串
     * @return
     */
    public static String toUpperCaseFirstOne(String str) {
        if (StringUtils.isBlank(str) || Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }

    public static String toCamelCase(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        List<String> strings = Arrays.asList(str.split(" "));
        if (null == strings || 0 == strings.size()) {
            return toLowerCaseFirstOne(str);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(toLowerCaseFirstOne(strings.get(0)));
        for (int index = 1; index < strings.size(); index++) {
            sb.append(toUpperCaseFirstOne(strings.get(index)));
        }
        return sb.toString();
    }

    /**
     * 去除字符串中的特殊字符
     */
    public static String excludeSymbol(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        Pattern pattern = Pattern.compile(symbol);
        Matcher m = pattern.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 去除特殊字符且进行驼峰处理
     * @param str
     * @return
     */
    public static String toCamelCaseNoSymbol(String str) {
        return StrUtil.excludeSymbol(StrUtil.toCamelCase(str));
    }

    /**
     * 是否是英文
     *
     * @param charaString
     * @return
     */
    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }

    /**
     * 是否包含中文字符
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Matcher m = chinese.matcher(str);
        return m.find();
    }

    /**
     * 数据集字段格式校验
     * @param fieldName
     * @return
     */
    public static boolean isDsField(String fieldName) {
        return fieldName.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
    }

    /**
     * 验证excle导入的表头是否符合规范
     * @param fieldName
     * @return
     */
    public static boolean isLegalHead(String fieldName) {
        //不含特殊字符，数字不能开头
        String regEx = "[`~!@#$%^*()+|{}':;',<>?~￥%……【】‘；：”“’。、？]|^[0-9]";
        return !fieldName.matches(regEx);
    }


    public static void main(String[] args) {
        String _str = "芒果~!@#$%^&*()_+{}|_:\"";
        System.out.println(excludeSymbol(_str));
    }

    /**
     * 校验字段名是否合法
     * 常规变量命名，适用于概念名、属性名等情况
     * @param fieldName
     * @return
     */
    public static boolean isLegalField(String fieldName) {
        //只能以英文字母和下划线开头，英文+数字+下划线的组合，长度限制1-30
        String regExp = "^[A-Za-z_$][a-zA-Z0-9_]{0,29}$";
        Pattern pa = Pattern.compile(regExp);
        Matcher matcher = pa.matcher(fieldName);
        return matcher.matches();
    }

    public static List<Map.Entry<String,Integer>> map2list(Map<String,Integer> map){
        List<Map.Entry<String,Integer>> mapList = new LinkedList<>();
        for(Map.Entry<String, Integer> entity:map.entrySet()){
            mapList.add(entity);
        }
        return mapList;
    }

}
