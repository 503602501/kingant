package com.crawler.handler.text.impl;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.DriverUtil;

public class IkeaText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
	
		try {
//			DriverUtil.snapshot((TakesScreenshot)webDriver,System.currentTimeMillis()+".png");
//			WebElement e1 = webDriver.findElement(By.xpath("//button[@class='sg-b-p-c']"));
//			 Actions action = new Actions(webDriver);
////			 action.click();// 鼠标左键在当前停留的位置做单击操作  
//			 action.click(e1);// 鼠标左键点击指定的元素
		} catch (Exception e) {
		}
		return "";
	}

}
