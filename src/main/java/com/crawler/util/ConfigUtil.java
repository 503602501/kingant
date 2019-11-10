package com.crawler.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtil {
	
	private static final Logger logger =  Logger.getLogger(ConfigUtil.class);
	private static final HashMap<String,Object> cache = new HashMap<>();
	
	//是否覆盖excel
	public  static final String EXCEL_COVER = "excel.cover";  
	
	//是否去掉重复的字段
	public static final String FILTER_DUPLICATE_DATA ="excel.filter.duplicate.data";
//	new Boolean(ConfigUtil.getStringConfigByKey(""));
	
	//是否导出所有数据，包括漏采集的行数据
	public static final String EXPORT_ALL_DATA ="excel.all.and.miss.data" ; 
	
	//导出排序
	public static final String EXPORT_SORT ="excel.export.sort" ; 
	
	
 	static{   //初始化配置文件信息
		InputStream in = null ; 
		try {
			Properties properties = new Properties();
			in = FolderUtil.getConfigInputStream("config.properties");
			properties.load(in);     ///加载属性列表
			Iterator<String> it=properties.stringPropertyNames().iterator();
	        while(it.hasNext()){
	             String key=it.next();
	             cache.put(key, properties.getProperty(key));
//	             System.out.println("配置文件属性:"+key+"="+properties.getProperty(key));
	         }
//	        System.out.println("*********************配置文件初始化完毕************************************");
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	} 
 	
	public static void setCache(String key , String value) {
		 cache.put(key, value);
	}
 	
	public static Object  getConfigByKey(String key) {
		return cache.get(key);
	}
	
	public static Integer  getIntegerConfigByKey(String key) {
		
		return Integer.parseInt(cache.get(key)+"");
	}
	
	public static String  getStringConfigByKey(String key) {
		if(cache.get(key)==null){
			return null;
		}
		
		return  cache.get(key)+"" ;
	}
	
	public static boolean  getBooleanConfigByKey(String key) {
		if(cache.get(key)==null){
			return false;
		}
		
		return  new Boolean(cache.get(key)+"") ;
	}
	
}
