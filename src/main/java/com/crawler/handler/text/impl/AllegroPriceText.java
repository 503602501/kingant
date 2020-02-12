package com.crawler.handler.text.impl;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;
import com.crawler.util.JsonUtil;

public class AllegroPriceText implements IText {

	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String ss = "";
		 try {
			 String content = webDriver.getPageSource() ;
			 content = content.substring(content.indexOf("opboxContext")-2);
			 content = content.substring(0, content.indexOf("prototypeName: 'allegro.showoffer.summary'"));
			 content  = content.substring(0, content.lastIndexOf(","));
			 
			 ss = content ;
			 text =JsonUtil.getJSONValue("schema->price", content);
			 
			 return text ;
		} catch (Exception e) {
			System.out.println("出错："+ss);
			e.printStackTrace();
		}
		 
		return text;
	}

}
