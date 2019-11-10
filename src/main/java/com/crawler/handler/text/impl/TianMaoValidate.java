package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class TianMaoValidate implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		try {
			WebElement wet  = webDriver.findElement(By.xpath("//a[@class='sufei-tb-dialog-close sufei-tb-overlay-close']"));
			wet.click();
			Thread.sleep(100);
		} catch (Exception e) {
			
		}
		return text ;
	}
	
}
