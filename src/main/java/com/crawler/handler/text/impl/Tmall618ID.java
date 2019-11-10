package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;

public class Tmall618ID implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		text=text.replaceAll("\r|\n", ""); 
		text = text.substring(text.indexOf("item_id="));
		text = text.substring(0,text.indexOf("link-box"));
		text = text.replace("class=", "");
		text = text.replace("item_id=", "");
		text = text.replace("\"", "");
		return text;
	}

}
