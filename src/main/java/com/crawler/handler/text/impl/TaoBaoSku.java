package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;


public class TaoBaoSku implements IText {

	public TaoBaoSku(String params) {
	}
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		text= text.substring(text.indexOf("</span>"));
		text=text.replace("</span>","");
		String[] array = text.split("&nbsp;&nbsp;");
		Integer index = Integer.parseInt(textUnit.getParams());
		
		for (int i = 0; i < array.length; i++) {
			if(array.length>index){
				return array[Integer.parseInt(textUnit.getParams())];
			}
		}
		return "";
	}
}
