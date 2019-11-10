package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class TaoBaoPrice implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		text = text.replace("¥", "");
		if(text.indexOf("-")!=-1){
			text = text.substring(text.indexOf("-")+1);
		}
		return text ;
	}

}
