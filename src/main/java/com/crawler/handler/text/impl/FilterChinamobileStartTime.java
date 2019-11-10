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
 * 提取出相应截止时间
 */
public class FilterChinamobileStartTime implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String mobile =storage.getLastRow().get(0);
		if(!NumberUtil.isMobileNO(mobile)){
			return "";
		}
		
		boolean flag = FilterChinamobile.check(webDriver, mobile);
		if(flag){
			return "";
		}
		
		String start = textUnit.getName().replace("时间", "");
		 start =start.replace("开始", "");
		List<WebElement> webElements =  webDriver.findElements(By.xpath(textUnit.getXpath()));
		for (WebElement webElement : webElements) {
			if(webElement.getText().startsWith( start)){
				WebElement w= webDriver.findElement(By.xpath(textUnit.getXpath()+"/following-sibling::p"));
				String time = w.getText();
				String[] s = time.split(" - ");
				if(s.length==2){
					return s[0];
				}
			}
			
		}
		
		return "";
	}

}
