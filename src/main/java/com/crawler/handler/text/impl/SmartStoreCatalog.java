package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class SmartStoreCatalog implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
	  text = text.substring(text.indexOf(">")+1);
	  if(text.indexOf("|")!=-1){
		 text =  text.substring(0, text.indexOf("|"));
	  }
	  text = text.replace("더보기", "");
	  
	  try {
		  WebElement webElement = webDriver.findElement(By.xpath("//div[@class='_selectbox_auto']/select/option[2]"));
		  webElement.click();
	} catch (Exception e) {
		e.printStackTrace();
	}
	  return text ;
	}

}
