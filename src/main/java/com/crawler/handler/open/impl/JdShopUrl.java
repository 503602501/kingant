package com.crawler.handler.open.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.WebBrower;
import com.crawler.handler.open.IOpen;
import com.crawler.util.DriverUtil;
import com.crawler.util.FolderUtil;

public class JdShopUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(JdShopUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		
		storage.getInputUrlQueues().clear();
		
		String url = driverEntity.getWebDriver().getCurrentUrl();
		
		
		Integer to = storage.getEnv().getWidgets().getToFieldValue()*2-1;
		Integer from = storage.getEnv().getWidgets().getFromFieldValue()*2-1;
		String urlss = "";
		for (int i = from; i <= to; i++) {
			if(i%2!=0){
				Product p = new Product();
				p.setUrl(getPageUrl(url, i) );
				storage.getInputUrlQueues().add(p);
			}
		}
		 
	}


	private String  getPageUrl(String url,Integer page ) {
		
		if(url.indexOf("page=")==-1){
			url= url+"&page=1&1=1";
		}
		
		String s  =url.substring(url.indexOf("page="));
		s = s.substring(0,s.indexOf("&"));
		url = url.replace(s, "page="+page);
		return url;
	}
	 

}
