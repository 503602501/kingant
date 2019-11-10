package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class SxappText implements IText {

	private static String key  = System.getenv("MOBILE") ; 
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		if(StringUtils.isEmpty( key )){
			return "";
		}
		
		if(text.indexOf(textUnit.getName())==-1){
			return "";
		}
		
		text = text.substring(text.indexOf(textUnit.getName()));
		text = text.replace(textUnit.getName(), "");
		if(text.indexOf("，")!=-1){
			text = text.substring(1, text.indexOf("，"));
		}
		text = text.replace("：", "");
		return text ;
	}
	
	public static void main(String[] args) {
		
		System.out.println(System.getenv("JAVA_HOMEs"));
		System.out.println(System.getenv("MOBILEs")==null);
		System.out.println(System.getProperty("MYaaa"));
	}

}
