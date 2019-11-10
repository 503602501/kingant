package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class AnfuaText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		StringBuffer sb= new StringBuffer();
		WebElement webElement = webDriver.findElement(By.xpath("//span[@class='jqzoom']/img"));
		String big = webElement.getAttribute("jqimg");
		sb.append(big+",");
		List<WebElement> webs = webDriver.findElements(By.xpath("//div[@class='default']/img"));
		for (WebElement webElement2 : webs) {
			sb.append(webElement2.getAttribute("src")+",");
		}
	
		return sb.toString();
	}
}
