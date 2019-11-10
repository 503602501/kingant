package com.crawler.handler.text.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;


public class AbbsTitle implements IText {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		
		try {
			String date = null;
			try {
				WebElement webElement = webDriver.findElement(By.xpath("//img[@alt='查看他的注册信息']/parent::a/parent::td"));
				 date = webElement.getText().substring(0, 10).replace("-", "").substring(2);
			} catch (Exception e) {
				e.printStackTrace();
				date = sdf.format(new Date());
			}
			
			return date+"_"+text+"_青岛宏景" ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return text;
		
	}

}
