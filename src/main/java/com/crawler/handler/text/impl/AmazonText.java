package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class AmazonText implements IText {

	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		 try {
			 List<WebElement> list = webDriver.findElements(By.xpath("//div[@data-test-id='shipping-section-buyer-address']/span"));
			 if(list.size()<=5){
				 return "";
			 }
		      
		} catch (Exception e) {
			
		}
		 
		return text;
	}

}
