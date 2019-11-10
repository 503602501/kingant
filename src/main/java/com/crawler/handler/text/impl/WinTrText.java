package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class WinTrText implements IText {

	public WinTrText(String s) {
	}
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		text = StringUtils.delHtml(text);
		
		if(StringUtils.isEmpty(text)){
			return "";
		}
		
		String param = textUnit.getParams().split("\\|")[0];
		String index = textUnit.getParams().split("\\|")[1];
		
		text = text.replace(param, ",");
		
		if(index.equals("0")){
			return text.split(",")[0];
		}else{
			return text.split(",")[1];
		}
	}

}
