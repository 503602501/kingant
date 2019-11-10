package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class TaoBaoZhuImage implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		if(StringUtils.isEmpty(text)){
			text = web.getAttribute("data-src");
			text ="https:"+text; 
		}
		text = text.replace("50x50.jpg", "800x800.jpg");
		return text ;
	}

}
