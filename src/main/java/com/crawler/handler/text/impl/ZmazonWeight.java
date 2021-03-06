package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class ZmazonWeight implements IText {

	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		 try {
			 
			 if(StringUtils.isEmpty(text)){
				 web = webDriver.findElement(By.xpath("//b[contains(text(),'Shipping Weight')]/parent::li"));
				 return web.getText().replace("Shipping Weight:", "");
			 }
		      
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return text;
	}

}
