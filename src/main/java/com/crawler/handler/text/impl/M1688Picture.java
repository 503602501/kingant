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
public class M1688Picture implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {

	
		String src = web.getAttribute("data-lazyload-src");
		if(StringUtils.isEmpty(src)){
			src = web.getAttribute("swipe-lazy-src");
		}
		
		if(StringUtils.isEmpty(src)){
			src = web.getAttribute("src");
		}
		if(StringUtils.isEmpty(src)){
			src="";
		}
		
		if(  src.indexOf(".jpg")!=-1){
			if(src.indexOf("http")==-1){
				src=DriverUtil.handleUrl(webDriver.getCurrentUrl(), src);
			}
			
			return src.replace("460x460", "800x800");
		}
		if(  src.indexOf(".png")!=-1){
			System.out.println("错误链接："+src);
		}
		
		return src.replace("460x460", "800x800");
	}
	
}
