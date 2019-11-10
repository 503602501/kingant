package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class SmartStorePic2 implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		List<String> list= new ArrayList<>();
		
		try {
			String s = web.getAttribute("data-src");
			if(!StringUtils.isEmpty(s)){
				list.add(s);
			}
		} catch (Exception e1) {
		}
		
		List<WebElement> webElements = null;
		try {
			webElements = webDriver.findElements(By.xpath(textUnit.getXpath()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(webElements==null || webElements.size()==0){
			try {
				webElements = webDriver.findElements(By.xpath("//img[contains(@id,'SEDOC')]"));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		
		if(webElements==null || webElements.size()==0){
			try {
				webElements = webDriver.findElements(By.xpath("//div[@class='detail_view']/div/div/img"));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		
		
		String src = "";
		for (int i = 1; i < webElements.size(); i++) {
			src = webElements.get(i).getAttribute("src");
			if(src.startsWith("data")){
				src = webElements.get(i).getAttribute("data-src");
			}
			if(src.indexOf("type=")!=-1){
				src = src.substring(0, src.indexOf(".jpg")+4 );
			}
				
			list.add(src);
		}
		
		return list.toString().replace("[", "").replace("]", "");
	}

	public static void main(String[] args) {
		String s = "https://shop-phinf.pstatic.net/20180525_237/liuqingxing2018@outlook.com_1527259166863hBbY4_JPEG/50565467489956853_1283976826.jpg?type=f40";
		System.out.println();
	}
}
