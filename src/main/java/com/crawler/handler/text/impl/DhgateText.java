package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;
import com.us.codecraft.Xsoup;

public class DhgateText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {

		
		try {
			String from = storage.getLabelByIndex(0); 
			if(StringUtils.isEmpty(from) ){
				storage.getEnv().showLogArea("\n错误提示:/请输入前缀!!!!!\n\n");
				storage.getEnv().showLogArea("\n错误提示:/请输入前缀!!!!!\n\n");
				storage.getEnv().setStop();
				return "请输入前缀" ;
			}
			
			
			WebElement webElement = webDriver.findElement(By.xpath("//input[@id='productname']")) ;
			if(webElement.getAttribute("value").startsWith(from)){
				return "" ;
			}
			
			String s = from+webElement.getAttribute("value") ;
			if(s.length()>180){
				s = s.substring(0,180);
			}
			
			webElement.clear() ;
			webElement.sendKeys(s);
			
			Thread.sleep(1000) ;
			webDriver.findElement(By.xpath("//input[@class='j-btn-submit']")).click();
		}catch (NoSuchElementException e) {
			e.printStackTrace() ;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

}
