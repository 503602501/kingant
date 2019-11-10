package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class FeiGuaWait implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		try {
			
			 WebDriverWait wait = new WebDriverWait(webDriver,6);  
	         wait.until(new ExpectedCondition<WebElement>(){  
	            @Override  
	            public WebElement apply(WebDriver d) {  
	               WebElement element = d.findElement(By.xpath("//div[@id='bloggerfans']/div[@class='load-container js-loading-container']"));
	               

	                if(element.isDisplayed()){  //显示就继续等待
	                	return null;
	                }else{
	                	return element;
	                }
	            }
	          }); 
	         
	         web =   webDriver.findElement(By.xpath(textUnit.getXpath()));
	         
			return web.getText();
			
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
	}
}
