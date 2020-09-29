package com.crawler.handler.text.impl;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class ZamaPrice implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		if(!StringUtils.isEmpty(text)){
			return text ;
		}
		
		WebElement we=null ;
		try {
			
			we = webDriver.findElement(By.xpath("//span[@id='priceblock_saleprice']")); 
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		
		
		if(we==null ||  StringUtils.isEmpty(we.getText()) ){
			try {
				we = webDriver.findElement(By.xpath("//div[@id='availability']/span")); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(we!=null){
				return we.getText() ;
			}else{
				return "" ;
			}
			
		}{
			return we.getText() ;
		}
	 
	}
	
}
