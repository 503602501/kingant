package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class TaoBaoDetail implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		StringBuffer sb= new StringBuffer();
		List<WebElement> webs = webDriver.findElements(By.xpath(textUnit.getXpath()));
		for (int i = 1; i < webs.size(); i++) {
			sb.append("https:"+webs.get(i).getAttribute("data-src").replace("50x50.jpg", "800x800.jpg")+",");
		}
		
		String ss = sb.toString();
		if(ss.endsWith(",")){
			ss = ss.substring(0,ss.length()-1);
		}
		
		return ss;
	}

}
