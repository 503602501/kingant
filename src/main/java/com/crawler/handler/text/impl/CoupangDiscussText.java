package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class CoupangDiscussText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		try {
			String s = storage.getCurrentDoc().toString() ;
			
			if(s.indexOf("product-rating-total-count")>0 ) {
				return text ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return "";
		
	}

}
