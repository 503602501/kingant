package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class YupooText implements IText {

	private static String key  = System.getenv("MOBILE") ; 
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		StringBuffer sb = new StringBuffer();
		List<WebElement> webs = webDriver.findElements(By.xpath("//div[@class='showalbum__children image__main']/div[@data-type='photo']/img"));
		for (WebElement webElement : webs) {
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("https://pic.yupoo.com"+webElement.getAttribute("data-path"));
			
		}
		
		return sb.toString() ;
	}
	
	public static void main(String[] args) {
		
		System.out.println(System.getenv("JAVA_HOMEs"));
		System.out.println(System.getenv("MOBILEs")==null);
		System.out.println(System.getProperty("MYaaa"));
	}

}
