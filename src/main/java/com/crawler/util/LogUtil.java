package com.crawler.util;

import java.io.InputStream;

import org.apache.log4j.PropertyConfigurator;


public class LogUtil {
	
	/*
	 * 初始化log配置文件
	 */
	public static void initLog() throws Exception {
		InputStream in = FolderUtil.getInputStreamFromEncrypt("log4j.properties");
		PropertyConfigurator.configure(in);
	}
	
}
