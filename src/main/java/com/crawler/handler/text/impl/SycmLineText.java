package com.crawler.handler.text.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class SycmLineText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		List<String> list = storage.getLastRow();
		if(textUnit.getName().equals("相关搜索词")){
			if(!list.get(0).equals(text)){
				return "";
			}
			return text ;
		}
			
		if(StringUtils.isEmpty(list.get(0)) || !list.get(0).equals(list.get(1))){
			return "";
		}
		return text;
	}
}
