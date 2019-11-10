package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class ZmazonCreateDate implements IText {

	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		 if(!StringUtils.isEmpty(text)){
			 try {
				web = webDriver.findElement(By.xpath("//th[contains(text(),'上架时间')]/following-sibling::td"));
				 return web.getText() ;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 
		return text;
	}

}
