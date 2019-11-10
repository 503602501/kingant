package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class SmartStoreOther implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		List<String> list= new ArrayList<>();
		List<WebElement> webElements =  webDriver.findElements(By.xpath(textUnit.getXpath()));
		String src = "";
		for (int i = 0; i < 5; i++) {
			if(i>=webElements.size()){
				break;
			}
			src = webElements.get(i).getAttribute("src");
			if(src.indexOf("?")!=-1){
				src = src.substring(0, src.indexOf("?") );
			}
			list.add(src.trim());
		}
		
		return list.toString().replace("[", "").replace("]", "");
	}
	
	public static void main(String[] args) {
		String s = "https://shop-phinf.pstatic.net/20180525_237/liuqingxing2018@outlook.com_1527259166863hBbY4_JPEG/50565467489956853_1283976826.jpg?type=f40";
		s = s.substring(0, s.indexOf(".jpg")+4 );
		System.out.println(s);
		
	}

}
