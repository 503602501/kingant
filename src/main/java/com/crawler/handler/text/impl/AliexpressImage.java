package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;


public class AliexpressImage implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		StringBuffer sb = new StringBuffer();
		try {
			List<WebElement> webElements = webDriver.findElements(By.xpath("//p/img"));
			if(webElements.size()==0){
				 webElements = webDriver.findElements(By.xpath("//div[@class='detailmodule_image']/img"));
			}
			
			if(webElements.size()==0){
				webElements = webDriver.findElements(By.xpath("//div/img[@alt]"));
			}
			
			if(webElements.size()==0){
				webElements = webDriver.findElements(By.xpath("//span/img"));
			}
			
			if(webElements.size()==0){
				webElements = webDriver.findElements(By.xpath("//div[@class='detailmodule_text-image']/img"));
			}
			
			
			for (WebElement webElement : webElements) {
				sb.append( webElement.getAttribute("src")+"," );
			}
		 
		} catch (Exception e) {
		 e.printStackTrace();
		}
			
		
		return sb.toString();
		
	}

}
