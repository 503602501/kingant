package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;

public class JohnBagleyImage implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		text = text.substring(text.indexOf(",")+3);
		text = "http://www.johnbagley.com"+text.substring(0, text.indexOf("'"));
		
		return text ;
	}
	
	public static void main(String[] args) {
		String s = "showPreview('/u_file/product/19_06_17/335X335_472d7a0df5.jpg', '/u_file/product/19_06_17/472d7a0df5.jpg'); this.blur();";
	
		
	}
}
