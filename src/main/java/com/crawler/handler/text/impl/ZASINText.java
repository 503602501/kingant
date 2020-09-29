package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;


public class ZASINText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String url = webDriver.getCurrentUrl() ;
		
		 return url.substring(url.lastIndexOf("/")+1) ;
		 
	}

}
