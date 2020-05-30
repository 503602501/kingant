package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class CoupangDetailText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		StringBuffer sb=new StringBuffer();
		List<WebElement> list=new ArrayList();
		try {
			try {
				list = webDriver.findElements(By.xpath("//div[@class='subType-IMAGE']/img"));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(list.size()==0){
					list =webDriver.findElements(By.xpath("//div[@class='subType-TEXT']/img"));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			try {
				if(list.size()==0){
					list =webDriver.findElements(By.xpath("//div[@class='subType-TEXT']/center/img"));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (WebElement webElement : list) {
//				storage.getEnv().showLogArea("\n"+webElement.getAttribute("src"));
				sb.append(webElement.getAttribute("src")+",");
			}
			
//			storage.getEnv().showLogArea("\n"+list.size());
			
			
		} catch (Exception e) {
//			storage.getEnv().showLogArea("\n"+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
		
	}

}
