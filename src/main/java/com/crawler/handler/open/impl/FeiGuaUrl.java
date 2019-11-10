package com.crawler.handler.open.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.StringUtils;

public class FeiGuaUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(FeiGuaUrl.class);
	
	public static Map<String, String> map = new HashMap<String, String>();
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		map.clear();
		
		String sign = storage.getLabelByIndex(0); 
		if(StringUtils.isEmpty(sign) ){
			storage.getEnv().showLogArea("\n错误提示:/请输入第一个热点点赞阀值!!!!!\n\n");
			storage.getEnv().showLogArea("\n错误提示:/请输入第一个热点点赞阀值!!!!!\n\n");
			storage.getEnv().setStop();
			return ;
		}
		
		
		sign = storage.getLabelByIndex(1);  
		if(StringUtils.isEmpty(sign) ){
			storage.getEnv().showLogArea("\n错误提示:/请输入3天热点赞阀值!!!!!\n\n");
			storage.getEnv().showLogArea("\n错误提示:/请输入3天热点赞阀值!!!!!\n\n");
			storage.getEnv().setStop();
			return ;
		}
		
		sign = storage.getLabelByIndex(2);  
		if(StringUtils.isEmpty(sign) ){
			storage.getEnv().showLogArea("\n错误提示:/请输入7天热点赞阀值!!!!!\n\n");
			storage.getEnv().showLogArea("\n错误提示:/请输入7天热点赞阀值!!!!!\n\n");
			storage.getEnv().setStop();
			return ;
		}
		
		sign = storage.getLabelByIndex(3);  
		if(StringUtils.isEmpty(sign) ){
			storage.getEnv().showLogArea("\n错误提示:/请输入15天热点赞阀值!!!!!\n\n");
			storage.getEnv().showLogArea("\n错误提示:/请输入15天热点赞阀值!!!!!\n\n");
			storage.getEnv().setStop();
			return ;
		}
		
		sign = storage.getLabelByIndex(4);  
		if(StringUtils.isEmpty(sign) ){
			storage.getEnv().showLogArea("\n错误提示:/请输入30天热点赞阀值!!!!!\n\n");
			storage.getEnv().showLogArea("\n错误提示:/请输入30天热点赞阀值!!!!!\n\n");
			storage.getEnv().setStop();
			return ;
		}
		
	}

}
