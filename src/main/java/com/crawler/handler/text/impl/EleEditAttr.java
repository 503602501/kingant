package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class EleEditAttr implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		List<WebElement> elements = webDriver.findElements(By.xpath("//li[contains(@class,'menucategory-active')]/following-sibling::li"));
		if(elements.size()>1){
			String ss = elements.get(1).getAttribute("class");
			JavascriptExecutor  js = (JavascriptExecutor)webDriver;
			
			for (int i = 0; i <= elements.size(); i++) {
				js.executeScript("document.getElementsByClassName('"+ss+"')["+i+"].style.padding='0cm';");
			}
			
		}
		
		return text;
	}

}
