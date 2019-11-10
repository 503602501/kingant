package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class SycmText implements IText {

	 
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		 List<WebElement> webs = webDriver.findElements(By.xpath("//div[@class='searchword-content']/table/thead/tr/th/div"));
		 for (int i = 0; i < webs.size(); i++) {
			 if(webs.get(i).getText().indexOf(textUnit.getName())!=-1){
				 String xpath = textUnit.getCurrentXpath().replace("[1]", "["+(i+1)+"]");
				 return  webDriver.findElement(By.xpath(xpath)).getText();
			 }
		}
		 return "";
	}

}
