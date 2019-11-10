package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;

public class ShChuangYiYuanText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		

		text = text.substring(text.indexOf("地址："));
		text = text.substring(0,text.indexOf("号")+1);
		text = text.replace("地址：", "");
		return text ;
		
	}
	public static void main(String[] args) {
	}

}
