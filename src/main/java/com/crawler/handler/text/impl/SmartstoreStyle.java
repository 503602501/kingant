package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class SmartstoreStyle implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		 
		text = text.replace("</option>", "</option>,");
		String s=  StringUtils.delHtml(text);
//		s = s.substring(s.indexOf(","));
		if(s.endsWith(",")){
			s = s.substring(0, s.length()-1);
		}
		
		return s;
	}

}
