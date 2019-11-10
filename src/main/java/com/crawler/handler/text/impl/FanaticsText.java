package com.crawler.handler.text.impl;

import java.util.List;

import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class FanaticsText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		StringBuffer sb = new StringBuffer();
		List<WebElement> list = webDriver.findElements(By.xpath("//div[@class='thumbnails text-center']/a/img"));
		if(list.size()==0){//提取主页
			WebElement webElement = webDriver.findElement(By.xpath("//div[@class='carousel default-pdp-image transition' or @class='carousel large-pdp-image transition']/img"));
			String s  =webElement.getAttribute("src").replace("w=35", "w=2000") ;
			s = s.replace("w=600", "w=2000");
			sb.append(s);
		}else{
			for (WebElement webElement : list) {
				String s  = webElement.getAttribute("src").replace("w=35", "w=2000"); 
				s = s.replace("w=600", "w=2000");
				sb.append(s  +",");
			}
		}
		
		return sb.toString();
	}

}
