package com.crawler.handler.text.impl;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;
import com.crawler.util.HttpUtil;


public class DaTaoKeProduct implements IText {

	private static final Logger logger =  Logger.getLogger(DaTaoKeProduct.class);
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		String id  = storage.getLastRow().get(2);
		String content=null;
		String url = "http://www.dataoke.com/gettpl?gid="+id+"&_="+System.currentTimeMillis();
		for (int i = 0; i < 5; i++) {
			try {
				content = HttpUtil.getHtmlContent(url);
			} catch (Exception e) {
				logger.error("异常链接:"+e.getMessage());
			}
			
			if(content==null){
				continue;
			}else{
				break;
			}
		}
		
		if(content==null){
			content="";
		}
		
		String s = content.substring(content.lastIndexOf("http"));
		
		return FilterUtil.getRegexContent(s, "match|(.+?)(?=</a>)");
	}

}
