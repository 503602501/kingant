package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.DriverUtil;


/*
 * 公用的当前页面链接
 */
public class CommonConvertUrl implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		if(text.indexOf("http")==-1){
			return DriverUtil.handleUrl(webDriver.getCurrentUrl(), text);
		}
		return text;
	}

}
