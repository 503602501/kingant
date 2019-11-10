package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;


/*
 * 过滤出name开头的字符串
 */
public class FilterChinamobile implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		String mobile =storage.getLastRow().get(0);
		if(!NumberUtil.isMobileNO(mobile)){
			return "";
		}
		
		boolean flag = check(webDriver, mobile);
		if(flag){
			return "";
		}
		
		List<WebElement> webElements =  webDriver.findElements(By.xpath(textUnit.getXpath()));
		for (WebElement webElement : webElements) {
			if(webElement.getText().startsWith(textUnit.getName())){
				return webElement.getText();
			}
			
		}
		return "";
	}
	
	public static boolean  check(WebDriver webDriver , String mobile) {
		List<WebElement> elements =  webDriver.findElements(By.xpath("//table[@class='aui_dialog']/tbody/tr/td/div[@class='aui_content']"));
		for (WebElement webElement : elements) {
			if(webElement.getText().indexOf(mobile)!=-1){
				return true;
			}
		}
		
		return false;
	}
}
