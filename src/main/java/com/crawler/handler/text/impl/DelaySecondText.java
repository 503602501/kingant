package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

/*
 *通用的用户时间延迟类
 */
public class DelaySecondText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		Integer shouchang =StringUtils.isEmpty(storage.getEnv().getWidgets().getInputFields().get(0).getText())? 0 : Integer.parseInt(storage.getEnv().getWidgets().getInputFields().get(0).getText());
		try {
			Thread.sleep(shouchang*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}
	
}
