package com.crawler.handler.text.impl;

import java.util.List;

import org.apache.xalan.templates.ElemApplyImport;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class E3E3Picture implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		List<WebElement> elements = webDriver.findElements(By.xpath("//ul[@id='thumblist']/li/a[contains(@href,'.jpg')]"));
		StringBuffer sb = new StringBuffer();
		for (WebElement webElement : elements) {
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append(webElement.getAttribute("href"));
		}
		
		//买卖网
		if(webDriver.getCurrentUrl().indexOf("www.2mm.cn")!=-1){
			List<WebElement> es = webDriver.findElements(By.xpath("//img[contains(@data-url,'750.jpg')]"));
			for (WebElement webElement : es) {
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append(webElement.getAttribute("data-url"));
			}
		}else{
			List<WebElement> es = webDriver.findElements(By.xpath("//img[contains(@src,'750.jpg')]"));
			for (WebElement webElement : es) {
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append(webElement.getAttribute("src"));
			}
		}
		
		return sb.toString();
	}

}
