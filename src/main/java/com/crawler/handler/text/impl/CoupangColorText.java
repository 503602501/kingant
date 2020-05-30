
package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;

public class CoupangColorText implements IText {

 

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		StringBuffer sb = new StringBuffer();
		try {
			String s =webDriver.getPageSource() ;
			
			s =FilterUtil.cutString(s, "\"displayComponentType\":\"BUTTON\"", "attributeVendorItemMap");
	    	s = s.replace(",\"attributes\":", "");
	    	s = s.replace("}],\"", "");
	    	JSONArray array =(JSONArray) JSONArray.parse(s);
	    	
	    	for (Object object : array) {
				JSONObject jsonObject = (JSONObject) object ;
				sb.append(jsonObject.get("name")+",");
			}
	    	
	    	/*System.out.println(sb.toString());
	    	
	    	
			List<WebElement>  list =webDriver.findElements(By.xpath("//ul[@class='Text-Select__Item__Container']/li"));
			
			
			sb = new StringBuffer();
			for (WebElement webElement : list) {
				sb.append(webElement.getText()+",");
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
		
	}

}
