package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

/*
 * 1688商品的详情页面图片地址提取
 */
public class DelHTMLTagHandler implements IText {

	public DelHTMLTagHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public DelHTMLTagHandler(String s) {
	}
	
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		return   StringUtils.delHtml(text);
	}

}
