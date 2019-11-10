package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class SxappYNText implements IText {

	
	private static String key  = System.getenv("MOBILE") ; 
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		
		if(StringUtils.isEmpty( key )){
			return "";
		}
		
		
		if(StringUtils.isEmpty(text)){ 
			return "否";
		}
		
		return "是";
	}

}
