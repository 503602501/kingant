package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class JdCommetValidate implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		
		
		String label = storage.getLabelByIndex(0);
		
		if(StringUtils.isEmpty(storage.getLastRow().get(0))){
			return "";
		}
		
		if("链接".equals(textUnit.getName())){
			return "https:"+text;
		}
		return text;
		
	}

}
