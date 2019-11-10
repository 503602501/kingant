package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class FeiGuaText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		text = text.replace("视频时长：", "");
		if(text.indexOf("关联商品")!=-1){
			text = text.split("关联商品")[0];
		}
		
		return text.trim() ;
		
	}
}
