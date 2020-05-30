package com.crawler.handler.open.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.PageUnit;
import com.crawler.entity.Storage;
import com.crawler.entity.Unit;
import com.crawler.entity.UnitAdapter;
import com.crawler.handler.open.IOpen;
import com.crawler.util.StringUtils;

public class HaodanKu implements IOpen {

	private static final Logger logger =  Logger.getLogger(HaodanKu.class);
	
	public static Map<String, String> map = new HashMap<String, String>();
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		
		
		String from = storage.getLabelByIndex(0); 
		if(StringUtils.isEmpty(from) ){
			storage.getEnv().showLogArea("\n错误提示:/请输入开始页码!!!!!\n\n");
			storage.getEnv().showLogArea("\n错误提示:/请输入开始页码!!!!!\n\n");
			storage.getEnv().setStop();
			return ;
		}
		
		String to = storage.getLabelByIndex(1); 
		if(StringUtils.isEmpty(from) ){
			storage.getEnv().showLogArea("\n错误提示:/请输入结束页码!!!!!\n\n");
			storage.getEnv().showLogArea("\n错误提示:/请输入结束页码!!!!!\n\n");
			storage.getEnv().setStop();
			return ;
		}
		
		
	 	for (Unit  unit: openUnit.getChildUnit()) {
			UnitAdapter adapter = (UnitAdapter) unit;
			if(adapter instanceof PageUnit){
				PageUnit pageUnit = (PageUnit) adapter ;
				pageUnit.setCount(Integer.parseInt(to)-Integer.parseInt(from)+1) ;
				 break; 
			}
			
		}
		
	 	
	 	
	 	
		  
		
		WebDriver webDriver = driverEntity.getWebDriver() ;
		webDriver.get("https://www.haodanku.com/item/index");
		
		((JavascriptExecutor)webDriver).executeScript( "window.scrollTo(0,0)");
		
		webDriver.findElement(By.xpath("//span[text()='今日爆单榜']")).click();
		Thread.sleep(2000);
		
		WebElement page = webDriver.findElement(By.xpath("//input[@type='number']"));
		page.clear();
		Thread.sleep(1000);
		page.sendKeys(from);
		page.sendKeys(Keys.ENTER);
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
