package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class Gpsspg implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		try {
			text =text.substring(text.indexOf("谷歌地球"));
			text = text.substring(0,text.indexOf("北纬"));
			text =text.replace("谷歌地球：", "");
			return text ;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
		
	}

}
