package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

/*
 * 个性推荐所有信息采集
 */
public class PmVideo implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,final TextUnit textUnit,Storage storage) {
		String page = webDriver.getPageSource();
		String s = page.substring(page.indexOf("og:video"));
		s=  s.substring(0,s.indexOf("data-rdm"));
		s =s.substring(s.indexOf("http"));
		s = s.replace("\"", "");
		return s ;
	}	
	
}
