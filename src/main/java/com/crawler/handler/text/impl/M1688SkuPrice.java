package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.DriverUtil;
import com.crawler.util.StringUtils;

/*
 * 1688商品的详情页面图片地址提取
 */
public class M1688SkuPrice implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		if(text.indexOf("¥")!=0){
			try {
//				return text.substring(text.indexOf("¥")-1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return text;
	}

}
