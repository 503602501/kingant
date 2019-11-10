package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;

public class Tmall618Count implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		text=text.replaceAll("\r|\n", ""); 
		if(text.indexOf("J_soldnum")!=-1){
			text = text.substring(text.indexOf("J_soldnum"));
			text = text.substring(0,text.indexOf("</em>"));
			text = text.replace("J_soldnum", "");
			text = text.replace("\">", "");
		}else{
			text = text.substring(text.indexOf("sold-num"));
			if(text.indexOf("</em>")==-1 ){
				return "";
			}
			text = text.substring(0,text.indexOf("</em>"));
			text = text.replace("sold-num", "");
			text = text.replace("<em>", "");
			text = text.replace("\">", "");
		}
		
		return text.trim();
	}

}
