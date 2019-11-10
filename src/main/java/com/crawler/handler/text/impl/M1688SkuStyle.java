package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.DriverUtil;
import com.crawler.util.StringUtils;

/*
 * 1688商品款式的采集
 */
public class M1688SkuStyle implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
	     text=text.replaceAll("\r|\n", "");
	     text = text.replaceAll("–", "").replaceAll("\\+", "");
		return text;
	}

}
