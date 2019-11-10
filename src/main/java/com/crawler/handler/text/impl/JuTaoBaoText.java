package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;


//https://detail.ju.taobao.com/home.htm?id=10000096036426&item_id=569389299458

public class JuTaoBaoText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		text= text.substring(text.indexOf("item_id="));
		text = text.replace("item_id=", "");
		text = "https://detail.tmall.com/item.htm?id="+text ;
		return text;
	}
}
