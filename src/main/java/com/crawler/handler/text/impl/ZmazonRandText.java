package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class ZmazonRandText implements IText {

	
	private String s ;
	
	public ZmazonRandText(String s ) {
		this.s = s;
	}
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		 try {
			 
			 if(StringUtils.isEmpty(text)){
				 web = webDriver.findElement(By.xpath("//b[contains(text(),'Amazon Best Sellers Rank')]/parent::li"));
				 String[]  a =  web.getText().replace("Amazon Best Sellers Rank:", "").split("#");
				 if(a.length>Integer.parseInt(s)){
					 return a[Integer.parseInt(s)];
				 }
				 
			 }
		      
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return text;
	}

	public static void main(String[] args) {
		String a[] = "#42 in Beauty & Personal Care (See Top 100 in Beauty & Personal Care)#1 in Makeup Brush Sets & Kits".split("#");
		for (String string : a) {
			System.out.println(string);
		}
	}
	
}
