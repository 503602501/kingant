package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class DingDongPic implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		
		StringBuffer sb = new StringBuffer();
		List<WebElement> webs =  webDriver.findElements(By.xpath("//div[@id='imageMenu']/ul/li/img"));
		for (WebElement webElement : webs) {
			sb.append(webElement.getAttribute("src")+",");
		}
		
		webs =  webDriver.findElements(By.xpath("//div[@class='main_wrap']/div[2]/img"));
		for (WebElement webElement : webs) {
			sb.append(webElement.getAttribute("src")+",");
		}
		
		return sb.toString() ;
		
		
	}

}
