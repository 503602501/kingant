package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;
import com.crawler.util.StringUtils;

public class Tmall618Li implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		text=text.replaceAll("\r|\n", ""); 
		if(text.indexOf("price-point")==-1){
			return "";
		}
		
		text = text.substring(text.indexOf("price-point"),text.indexOf("item-prices"));
		text = text.replace("price-point", "");
		text = text.split("</span>")[0];
		text = text.replace("\">", "");
		return StringUtils.delHtml(text);
	}

}
