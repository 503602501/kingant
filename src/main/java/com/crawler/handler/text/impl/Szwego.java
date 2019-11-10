package com.crawler.handler.text.impl;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class Szwego implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String http ="";
		if(StringUtils.isEmpty(text)){
			text=web.getAttribute("href");
		}
		if(!text.startsWith("http")){
			text="https://www.szwego.com"+text;
		}
		return text ; 
		
	}

}
