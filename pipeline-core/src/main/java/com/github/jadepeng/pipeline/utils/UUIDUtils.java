package com.github.jadepeng.pipeline.utils;

import java.util.UUID;

/**
 * @Author yche4
 * @Date 2019/3/4 13:33
 **/
public class UUIDUtils {

	public static String getUUIDString(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
