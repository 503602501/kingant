package com.crawler.handler.open.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.StringUtils;

public class HaodanKu implements IOpen {

	private static final Logger logger =  Logger.getLogger(HaodanKu.class);
	
	public static Map<String, String> map = new HashMap<String, String>();
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		WebDriver webDriver = driverEntity.getWebDriver() ;
		webDriver.get("https://www.haodanku.com/item/index");
		
		webDriver.findElement(By.xpath("//span[text()='今日爆单榜']")).click();
		Thread.sleep(2000);
		
		/*map.clear();
		
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
		}*/
		
	}

}
