package com.crawler.handler.text.impl;

import java.net.SocketTimeoutException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.HttpUtil;

public class DesignbyhumansUrl implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String zoom ="";
		String content;
		
		for (int i = 0; i < 3; i++) {

			
			try {
				
				content = HttpUtil.getHtmlContent(text);
				content = content.substring(content.indexOf("PRODUCT_IMAGES_MAG="));
				content = content.replace("PRODUCT_IMAGES_MAG=", "");
				content = content.substring(0,content.indexOf("</script>"));
				content = content.replaceAll(";", ""); 
				
				JSONObject object = (JSONObject) JSONObject.parse(content) ;
				object= (JSONObject) object.get("images");
				String[] urls = object.toJSONString().split("https");
				for (String url : urls) {
					if(url.indexOf(".jpg")==-1 || url.indexOf("1200x1200")!=-1){
						continue;
					}
					url = url.replace("\"", "");
					url = url.substring(0,url.indexOf(".jpg")+4);
					url="https"+url;
					return url;
				}
				
			 
			}catch(SocketTimeoutException se){
				se.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		
		return zoom ;
		
	}

}
