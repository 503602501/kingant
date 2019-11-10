package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;
import com.crawler.util.StringUtils;

public class MilikeText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		String url = webDriver.getCurrentUrl();
		url = url.replaceAll("#", "");
		try {
			String bigs = getBigImage(url);
			String hxstring = getHx(url);
			return bigs+","+hxstring;
		} catch (Exception e) {
			e.printStackTrace();
			return "异常";
		}
		
	}
	
	public static void main(String[] args) throws Exception {
//		String url = "http://tj.milike.com/estate/3519692.htm";
		String url = "http://tj.milike.com/estate/2973834.htm";
		String hxstring = getHx(url);
		
		System.out.println(hxstring);
		
//		String tostr = getBigImage(s);
//		System.out.println(tostr);
		
		
		
	}

	private static String getHx(String url) throws Exception  {
		String hxurl = url.replace("estate/", "estate/hx-");
		String s = HttpUtil.getHtmlContent(hxurl);
		s = s.substring(s.indexOf("searchSubmit"));
		s = s.replace("earchSubmit(", "");
		s = s.substring(s.indexOf("ajaxDetail"),s.indexOf("hId,eId"));
		
		List<String> list=  new ArrayList<>();
		String ajaxurl = "";
		for (String string : s.split("ajaxDetail")) {
			if( string.contains(");") ){
				ajaxurl =  string.substring(1,string.indexOf(");"));
				ajaxurl= String.format("http://tj.milike.com/estate/detail/%s/%s.htm",ajaxurl.split(","));
				if(!list.contains(ajaxurl)){
					list.add(ajaxurl);
				}
			}
		}
		
		List<String> hx = new ArrayList<>();
		for (String string : list) {
			String content = 	HttpUtil.getHtmlContent(string);
			try {
				content = content.substring(content.indexOf("http://mfang360.oss-cn-shenzhen"));
				content = content.substring(0,content.indexOf("'"));
			} catch (Exception e) {
				e.printStackTrace();
				content = content.substring(content.indexOf("lazy-src=")+10);
				content = content.substring(0,content.indexOf("'"));
				content ="http://tj.milike.com"+content;
				
			}
			hx.add(content);
		}
		String hxstring = StringUtils.arrayToString(hx);
		return hxstring;
	}

	private static String getBigImage(String url ) throws Exception {
		String s= HttpUtil.getHtmlContent(url);
		s = s.substring(s.indexOf("XF.Vars.imageAlbumData[0]"));
		s = s.substring(0,s.indexOf("if("));
//		System.out.println(s);
		List<String> list =new ArrayList<>();
		String ss ="";
		for (String string : s.split(";")) {
			if(string.contains("big")){
				if(string.indexOf("http:")!=-1){
					 ss = string.substring(string.indexOf("http:")) ; 
				}else{
					ss = string.substring(string.indexOf("showPicture.htm")) ;  
					ss = "http://tj.milike.com/"+ss ;
				}
				ss = ss.replace("'", "");
				list.add(ss);
			}
		}
		String tostr= StringUtils.arrayToString(list);
		return tostr;
	}
}
