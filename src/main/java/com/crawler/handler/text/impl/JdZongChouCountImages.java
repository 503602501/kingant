package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class JdZongChouCountImages implements IText {

 
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		if(text==null){
			text = "";
		}
		return ""+ text.split("<img").length;
	}

}
