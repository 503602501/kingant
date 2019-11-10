package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;


public class DaTaoKePrice implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		List<String>  row = storage.getLastRow(); //获取最后一行
		double price1  = 0,price2=0;
		if(NumberUtil.isNumeric(row.get(0)+"")){
			price1= Double.parseDouble(row.get(0));
		}
		
		if(NumberUtil.isNumeric(row.get(1)+"")){
			price2= Double.parseDouble(row.get(1));
		}
		String s  = String.format("%.1f", (price1+price2)) ; 
		return  s.replace(".0", "");
	}

}
