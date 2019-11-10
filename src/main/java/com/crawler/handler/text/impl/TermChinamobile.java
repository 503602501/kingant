package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;

public class TermChinamobile implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String mobile =storage.getLastRow().get(0);
		if(!NumberUtil.isMobileNO(mobile)){
			return "";
		}
		
		boolean flag = FilterChinamobile.check(webDriver, mobile);
		if(flag){
			return "";
		}
		
		String[] s = text.split(" - ");
		if(s.length==2){
			return s[1];
		}
		return text;
	}

}
