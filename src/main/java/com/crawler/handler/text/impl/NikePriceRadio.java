package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;


public class NikePriceRadio implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
//	 return "";
		String price  =storage.getLastRow().get(3).replaceAll(",", "").replace("￥", "");
		String price2  =storage.getLastRow().get(4).replaceAll(",", "").replace("￥", "");
		return String.format("%.2f", Double.parseDouble(price2)/Double.parseDouble(price) );
	}
}
