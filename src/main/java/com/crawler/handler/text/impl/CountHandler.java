package com.crawler.handler.text.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class CountHandler implements IText {

	private static String params  ; 
	public CountHandler(String  params) {
		this.params = params;
	}
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		Pattern pattern = Pattern.compile(this.params);    
        Matcher matcher = pattern.matcher(text);    
        int count=0;  
        while(matcher.find()){  
            count++;  
        }  
        return count+"";
	}

}
