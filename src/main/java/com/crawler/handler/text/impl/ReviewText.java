package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class ReviewText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		List list = storage.getLastRow();
		String ss = ""+list.get(2);
		if(ss.indexOf(",")!=-1 && ss.split(",").length>3){
			String re  =  ss.split(",")[3];
			 
			return re;
		}
		
		return "";
	}
}
