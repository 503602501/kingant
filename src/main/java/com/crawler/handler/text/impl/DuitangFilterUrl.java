package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

/*
 * 堆糖，通过收藏数量，点赞数量，采集指定的图片
 */
public class DuitangFilterUrl implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		
		Integer shouchang =StringUtils.isEmpty(storage.getEnv().getWidgets().getInputFields().get(0).getText())? null : Integer.parseInt(storage.getEnv().getWidgets().getInputFields().get(0).getText());
		Integer dianzhan =StringUtils.isEmpty(storage.getEnv().getWidgets().getInputFields().get(1).getText())? null : Integer.parseInt(storage.getEnv().getWidgets().getInputFields().get(1).getText());
		
		Integer sc = StringUtils.isEmpty(storage.getLastRow().get(0)) ? 0 : Integer.parseInt(storage.getLastRow().get(0));
		Integer dz = StringUtils.isEmpty(storage.getLastRow().get(1)) ? 0 : Integer.parseInt(storage.getLastRow().get(1));
		
	    if(shouchang!=null && sc<shouchang){
	    	return "";
	    }
		 
	    if(dianzhan!=null && dz<dianzhan){
	    	return "";
	    }
		
	    text =text.replace(".thumb.224_0", "");
	    
	    return text;
	}

}
