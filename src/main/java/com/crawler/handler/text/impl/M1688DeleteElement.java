package com.crawler.handler.text.impl;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

/*
 * 1688商品的详情页面图片地址提取
 */
public class M1688DeleteElement implements IText {
	
	private static final Logger logger =  Logger.getLogger(M1688DeleteElement.class);

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		try {
			String s = "var m = document.getElementById('call-all-app-popver');m.parentNode.removeChild(m);var m2 = document.getElementsByClassName('call-all-app-mask');m2[0].parentNode.removeChild(m2[0]);";
			JavascriptExecutor  js = (JavascriptExecutor)webDriver;
			js.executeScript(s);
		} catch (Exception e) {
			logger.error("1688异常处理弹出的元素");
		}
		return text;
	}

}
