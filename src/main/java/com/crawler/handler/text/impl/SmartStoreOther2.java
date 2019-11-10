package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class SmartStoreOther2 implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		List<String> list= new ArrayList<>();
		StringBuffer sb = new StringBuffer();
		List<WebElement> webElements =  webDriver.findElements(By.xpath(textUnit.getXpath()));
		for (int i = 0; i < webElements.size(); i++) {
			list.add(webElements.get(i).getAttribute("src").replace("type=f40", "type=m510"));
		}
		return list.toString().replace("[", "").replace("]", "");
	}
	

}
