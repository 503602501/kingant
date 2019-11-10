package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;


public class ShoppPrice implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		text = text.replace("$", "").replace("RM", "");
		String radio = "1";
		if(storage.getEnv().getWidgets().getInputFields().get(1)!=null){
			radio = storage.getEnv().getWidgets().getInputFields().get(1).getText();
			if(StringUtils.isEmpty(radio)){
				radio="1";
			}
		}
		
		if(NumberUtil.isNumeric(text)){
			Double i = new Double(text);
			double price = i* Double.parseDouble(radio);
			return ""+Math.round(price);
		}
		
	 return "";
	}

}
