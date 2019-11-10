package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;
import com.crawler.util.StringUtils;

public class Tmall618Price implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		text=text.replaceAll("\r|\n", ""); 
		text = text.substring(text.indexOf("J_actPrice"),text.indexOf("dock"));
		text = text.substring(0,text.indexOf("</em>"));
		text= text.replace("J_actPrice", "");
		text = StringUtils.delHtml(text);
		text = text.replace("\">", "");
		return text ;
	}

}
