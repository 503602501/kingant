package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class SmartstorePrice implements IText {

	
	public SmartstorePrice(String s) {
		
	}
	
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		 try {
			 
			 if(StringUtils.isEmpty(text)){
				 web = webDriver.findElement(By.xpath("//strong[@class='info_cost ']/span[last()]"));
				 return web.getText();
			 }
		      
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return text;
	}

}
