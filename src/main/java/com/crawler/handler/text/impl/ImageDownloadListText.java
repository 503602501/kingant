package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

/*
 * 通用的一大批图片链接下载
 */
public class ImageDownloadListText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		String xpath = textUnit.getXpath();
		List<String> srcs = new ArrayList<>();
		List<WebElement> webElements = webDriver.findElements(By.xpath(xpath));
		for (WebElement webElement : webElements) {
			srcs.add(webElement.getAttribute("src"));
		}
		 
		return srcs.toString().replace("[", "").replace("]", "");
	}

}
