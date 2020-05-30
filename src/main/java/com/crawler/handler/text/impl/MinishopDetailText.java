
package com.crawler.handler.text.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;

public class MinishopDetailText implements IText {

 

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		StringBuffer sb = new StringBuffer();
		try {
		 
	    	
			List<WebElement>  list =webDriver.findElements(By.xpath("//div[@id='basic_detail_html']/img"));
			if(CollectionUtils.isEmpty(list)){
				list =webDriver.findElements(By.xpath("//div[@class='ee-image']/img"));
				
			}
			
			for (WebElement web2 : list) {
				
				sb.append(web2.getAttribute("src")+",");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
		
	}

}
