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
 * 特殊处理主图和详情图片地址，便于区分
 */
public class M1688Picture2 implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {

	
		String src = web.getAttribute("data-lazyload-src");
		if("185".equals(web.getAttribute("width"))){ //广告图片不需要
			return "";
		}
		
		
		boolean flag = isMain(web);
		if(flag ){ //主图的小图，不需要下载
			String s = web.getAttribute("swipe-lazy-src");
			String ss = web.getAttribute("src");
			if( !StringUtils.isEmpty(s) && s.indexOf("460x460xz")!=-1){
				return "";
			}
			
			if( !StringUtils.isEmpty(ss) && ss.indexOf("460x460xz")!=-1){
				return "";
			}
		}
		
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
			
			//是主图
			if(isMain(web)){  //是主图片
				storage.getLastRow().set(2, "1");
			}
			return src;
		}
		
		if(  src.indexOf(".png")!=-1){
			System.out.println("错误链接："+src);
		}
		
		//是主图
		if(isMain(web) ){  //是主图片
			storage.getLastRow().set(2, "1");
		}
		return src;
	}
	
	private boolean isMain(WebElement web){
		String clazz = web.getAttribute("class");
//		String src = web.getAttribute("src");
		if("swipe-image".equals(clazz) ){
			return true ;
		}
		
		return false;
	}
	
	
}
