package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;


public class JdImageText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {

		List<String> list = storage.getLastRow();
		String url = list.get(1);
		String image;
		try {
			image = getTwoImage(url);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "https:"+image;
	}
	

	private static String getTwoImage(String url) throws Exception {
		String s =	HttpUtil.getHtmlContent(url,"GBK","");
		s  = s.substring(s.indexOf("id=\"spec-list\""));
		s = s.substring(s.indexOf("<li"),s.indexOf(" </div>"));
		String[] lis = s.split("<li");
		String li = lis.length >2 ? lis[2] : lis[1];
//		StringUtils.delHtml(s);
		String dataurl =  li.substring(li.indexOf("src='")+4, li.indexOf("data-url='"));
		dataurl = dataurl.replace("img.com/n5", "img.com/n1");
		dataurl = dataurl.replace("'", "");
		return dataurl;
	}
	
}
