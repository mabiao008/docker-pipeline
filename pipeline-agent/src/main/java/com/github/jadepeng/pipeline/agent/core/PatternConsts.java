package com.github.jadepeng.pipeline.agent.core;

import java.util.regex.Pattern;

/**
 * @author jqpeng (jqpeng@iflytek.com)
 * @Description 公共PATTERN库
 * @date 2018/9/12 14:29
 */
public class PatternConsts {

    public static final Pattern VERSION_PATTERN = Pattern.compile("([0-9]{1,}\\.[0-9]{1,}\\.[0-9]{1,})");
    public static final Pattern PRETTY_NAME_PATTERN = Pattern.compile("PRETTY_NAME=\"(.*?)\"");

}
