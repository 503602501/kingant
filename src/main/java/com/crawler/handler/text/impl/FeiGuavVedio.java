package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class FeiGuavVedio implements IText {

	private String s ;
	public FeiGuavVedio(String s) {
		this.s=s;
	}
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		text = text.replace("</i>", "</i>|");
		text = StringUtils.delHtml(text);
		String ss[] = text.split("\\|");
		String retu ="";
		try {
			
			 retu =ss[Integer.parseInt(s)];
			 
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return retu.trim();
		
	}
}
