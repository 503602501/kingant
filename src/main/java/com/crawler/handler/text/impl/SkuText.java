package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;

public class SkuText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		text = webDriver.getCurrentUrl();
		String folder = text.substring(text.lastIndexOf("/")+1);
		
		return FilterUtil.getRegexContent(folder, "match|.*\\?").replace("?", "");	
		
	}
	public static void main(String[] args) {
	}

}
