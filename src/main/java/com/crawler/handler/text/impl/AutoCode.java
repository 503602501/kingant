package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;


public class AutoCode implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String code ="";
		if(storage.getEnv().getWidgets().getInputFields().size()>0){
			code=storage.getEnv().getWidgets().getInputFields().get(0).getText();
		}
		return System.currentTimeMillis()+code+NumberUtil.autoNumber();
	}

}
