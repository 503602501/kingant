package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;

public class BeiKeImgText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		StringBuffer sb = new StringBuffer();
		try {
			List<WebElement> webs =   webDriver.findElements(By.xpath("//ol[@id='overviewThumbnail']/li"));
			
			for (WebElement webElement : webs) {
				sb.append(webElement.getAttribute("data-src")+",");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString() ;
	}
}
