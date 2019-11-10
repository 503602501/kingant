package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;


public class JdDieSore implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {

		if(StringUtils.isEmpty(text)){
			//s fou hai wai 
			try {
				WebElement e = webDriver.findElement(By.xpath("//div[@class='worldwide-menu-link']/a/span"));
				if(e.getText().indexOf("海外专营店")!=-1){
					return "海外专营店";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return text;
	}
}
