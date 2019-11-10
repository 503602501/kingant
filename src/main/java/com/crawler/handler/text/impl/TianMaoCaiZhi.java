package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class TianMaoCaiZhi implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		 
		if(!StringUtils.isEmpty(text)){
			return text ;
		}
		try {
			WebElement webb = webDriver.findElement(By.xpath("//dl[@id='J_StrPriceModBox']/dd/span[@class='tm-price']"));
			text = webb.getText() ; 
			if(!StringUtils.isEmpty(text)){
				return text;
			}
		} catch (Exception e) {
		}
		
		try {
			WebElement webb = webDriver.findElement(By.xpath("//div[@class='real-price']/span/span[@class='price']"));
			text = webb.getText() ; 
		} catch (Exception e) {
		}
		
		if(StringUtils.isEmpty(text)){
			WebElement webb = webDriver.findElement(By.xpath("//div[@class='price-real']/span[@class='num']"));
			text = webb.getText() ; 
		}
		
		return text;
	}
	 

}
