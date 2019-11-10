package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class FeiGuaDetailText implements IText {

	private String s = null;
	public FeiGuaDetailText(String s) {
		this.s =s;
	}
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String re ="";
		 text = text.replace("</span>", "|</span>");
		 text = StringUtils.delHtml(text);
		 String [] array = text.split("\\|");
		 for (String string : array) {
			 if(string.contains(s)){
				 re= string.replace(s, "").replace("：", "");
				 break;
			 }
		}
		
		return re.trim() ;
	}
}
