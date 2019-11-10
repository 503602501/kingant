package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class TaoBaoOrderHtml implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String s = StringUtils.delHtml(text);
		if(textUnit.getName().equals("物流公司")){
			s = s.substring(0,s.indexOf("运单号"));
			s = s.replace("发货物流公司：","");
		}
		
		if(textUnit.getName().equals("运单号")){
			s = s.substring(s.indexOf("运单号："),s.length());
			s = s.replace("运单号：","");
		}
		
		return s;
	}
	
	public static void main(String[] args) {
		String s = "发货物流公司：中通快递  运单号：218356516455";
		s = s.substring(0,s.indexOf("运单号"));
		System.out.println(s);
	}
	
}
