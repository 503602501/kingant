package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;

public class TaojinbiText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String cookies ="";
//		String s = HttpUtil.postHtmlContent("https://item.taobao.com/item.htm?id=591758162649&wwlight=cntaobao%E5%9C%A8%E5%81%9A%E4%B8%80%E4%BC%9A-%7B591758162649%7D", map, cookies);
		String s="";
		String content = "";
		for (int i = 0; i < 3; i++) {
			try {
				s = HttpUtil.getHtmlContent(text,"GBK",  cookies);
				content = s;
				if(s!=null){
					break;
				}else{
					Thread.sleep(2000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		String s = HttpUtil.getHtmlContent("https://detail.tmall.com/item.htm?spm=608.7065813.ne.1.52b72c7eW6AVos&id=548712023912&tracelog=jubuybigpic","GBK",  cookies);
		
		try {
			//天猫旗舰店
			if(s.indexOf("seller_nickname")!=-1){
				s = s.substring(s.indexOf("seller_nickname"));
				s = s.substring(0,s.indexOf("/>"));
				s = s.replace("seller_nickname\" value=\"", "");
				s = s.replace("\"", "");
			}else if(s.indexOf("sellerNick")!=-1){ //淘宝店铺
				s = s.substring(s.indexOf("sellerNick"));
				s = s.substring(0,s.indexOf(","));
				s = s.replace("sellerNick", "");
				s = s.replace("'", "");
				s = s.replace(":", "");
				s = s.replaceAll(" ", "");
			}else{
				s = s.substring(s.indexOf("data-nick="));
				s = s.substring(0,s.indexOf("data-tnick"));
				s = s.replace("data-nick=", "");
				s = s.replace("\"", "");
				s = s.replaceAll(" ", "");
			}
		} catch (Exception e) {
//			System.out.println(content);
//			System.out.println(text);
			e.printStackTrace();
			return "";
		}
		
		return s;
	}

}
