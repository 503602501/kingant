package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;


public class AutoCodeProduct implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		List<String>  row = storage.getLastRow(); //获取最后一行
		
		String code = row.get(7); //主商品编号
		return code+NumberUtil.autoNumber();
	}

}
