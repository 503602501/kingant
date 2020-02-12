package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.JsonUtil;

public class AllegroText implements IText {

	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		 try {
			 StringBuffer sb = new StringBuffer();
			 String content = webDriver.getPageSource() ;
			 content = content.substring(content.indexOf("opboxContext")-2);
			 content = content.substring(0, content.indexOf("prototypeName: 'allegro.showoffer.summary'"));
			 content  = content.substring(0, content.lastIndexOf(","));
			 System.out.println(content);
			 JSONArray array =JsonUtil.getJSONArray("gallery->images", content);
			 for (Object object : array) {
				 JSONObject json = (JSONObject) object;
				 sb.append(json.get("original")+",");
			 }
			 
			 // System.out.println(HttpUtil.removeSpecilChar(content));
		     return sb.toString() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return text;
	}

}
