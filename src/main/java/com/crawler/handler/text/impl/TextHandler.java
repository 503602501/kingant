package com.crawler.handler.text.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class TextHandler implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		text=text.replace("clickPhoto('", "");
		text =text.substring(0, text.indexOf("'"));
		return text;
	}
	public static void main(String[] args) {
		TextHandler te = new TextHandler();
		String s ="http://map.qq.com/?type=nav&tocoord=39.95292,116.35644&c=39.95292,116.35644&l=13";
	    Pattern p = Pattern.compile("");
        Matcher m = p.matcher(s);
        while (m.find()) {
        	System.out.println(m.group(0));
        } 
        
	}

}
