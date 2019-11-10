package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class WinTimeText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
	/*	 
		String state = storage.getLastRow().get(2);
		if(StringUtils.isEmpty(state) || state.indexOf("完")!=-1){
			return "";
		}*/
		
		text = text.replace("showgoallist(", "");
		text = text.replace(")", "");
		return "http://www.win0168.com/detail/"+text+"sb.htm";
		
	}

}
