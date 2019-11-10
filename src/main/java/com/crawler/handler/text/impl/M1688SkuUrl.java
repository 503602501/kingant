package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.DriverUtil;
import com.crawler.util.StringUtils;

/*
 * 店铺采集商品的链接，需要特殊处理成手机端的商品链接
 */
public class M1688SkuUrl implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		text= text.replace("//detail.1688.com/", "//m.1688.com/");
		return text ;
	}

}
