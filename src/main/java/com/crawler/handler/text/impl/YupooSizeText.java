package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class YupooSizeText implements IText {

	private static String key  = System.getenv("MOBILE") ; 
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		String s = text;
		try {
			if(s.indexOf("尺寸")!=-1){
				s = s.substring(s.indexOf("尺寸"));
				if(s.indexOf("cm")!=-1){
					s = s.substring(0,s.indexOf("cm")+2 );
				}else if(s.indexOf("。")!=-1){
					s = s.substring(0,s.indexOf("。") );
				}
				s = s.replace("尺寸", "");
				s = s.replace(":", "").replace("：", "");
			}else if(s.indexOf("size")!=-1){
				s = s.substring(s.indexOf("size"));
				if(s.indexOf("cm")!=-1){
					s = s.substring(0,s.indexOf("cm")+2 );
				}else if(s.indexOf("。")!=-1){
					s = s.substring(0,s.indexOf("。") );
				}
				s = s.replace("size", "");
				s = s.replace(":", "").replace("：", "");
			}
		} catch (Exception e) {
//			e.printStackTrace();
			return text ;
		}
		
		return s;
	}
	
	public static void main(String[] args) {
		
		System.out.println(System.getenv("JAVA_HOMEs"));
		System.out.println(System.getenv("MOBILEs")==null);
		System.out.println(System.getProperty("MYaaa"));
	}

}
