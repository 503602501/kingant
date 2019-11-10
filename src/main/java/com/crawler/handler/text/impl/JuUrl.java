package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;


public class JuUrl implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {

		text = text.substring(text.indexOf("item_id="));
		text = text.replace("item_id=", "https://detail.m.tmall.com/item.htm?id=");
		return text;
	}
}
