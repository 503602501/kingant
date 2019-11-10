package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;
import com.crawler.util.StringUtils;

public class TaobaoText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		try {
			if(StringUtils.isEmpty(text)){
				WebElement webb = webDriver.findElement(By.xpath("//div[@aria-label='分享']/parent::div/div[2]"));
				text = webb.getText() ; 
			}
		} catch (Exception e) {
			
			try {
				WebElement webb = webDriver.findElement(By.xpath("//div[@aria-label='分享有礼']/parent::div/div[1]"));
				text = webb.getText() ; 
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return text ;
	}
}
