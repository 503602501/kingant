package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;

public class QiangTaoBaoProductUrl implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		
		if(com.crawler.util.StringUtils.isEmpty(textUnit) || null==HttpUtil.getQueryString(text, "id")){
			return "";
		}
		
		return "https://item.taobao.com/item.htm?id="+HttpUtil.getQueryString(text, "id");
		
		
	}

}
