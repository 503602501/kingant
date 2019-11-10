package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

/*
 * 推荐信息
 */
public class CcscmTuijianBiz implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String tuijian = storage.getLastRow().get(12);
		String [] s = tuijian.split("<>");
		if(s.length>1){
			return s[1];
		}
		return "";
	}
	
}
