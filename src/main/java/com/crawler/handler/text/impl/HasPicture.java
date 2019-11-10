package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class HasPicture implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		if(text.indexOf("WB_pic")!=-1 && text.indexOf("<img")!=-1 && text.indexOf(".jpg")!=-1){
			return "有";
		}
		return "否";
	}

}
