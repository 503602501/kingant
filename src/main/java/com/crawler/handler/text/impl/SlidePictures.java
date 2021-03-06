package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;


public class SlidePictures implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		try {
			text = text.substring(text.indexOf("mylinkpic="));
			text = text.substring(0,text.indexOf("&amp;mytxt="));
			text = text.replaceAll("\\|", ",");
			text = text.replace("mylinkpic=","");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
}
