
package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class CoupangColorText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		List<WebElement>  list =webDriver.findElements(By.xpath("//ul[@class='Text-Select__Item__Container']/li"));
		
		
		StringBuffer sb = new StringBuffer();
		for (WebElement webElement : list) {
			sb.append(webElement.getText()+",");
		}
		
		return sb.toString();
		
	}

}
