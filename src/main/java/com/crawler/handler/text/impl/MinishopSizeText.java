
package com.crawler.handler.text.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;
import com.crawler.util.JsonUtil;

public class MinishopSizeText implements IText {

 

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
	/*	StringBuffer sb = new StringBuffer();
		try {
		 
	    	
			List<WebElement>  list =webDriver.findElements(By.xpath("//div[@id='coreGoodsOption']/div[@class='section_option_area']/div/div[2]/ul/li"));
			
			for (WebElement web2 : list) {
				
				sb.append(web2.getText()+",");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();*/
		
		Set<String> set;
		try {
			String s = webDriver.getPageSource();
			s =FilterUtil.cutString(s, "GmktItem.OptionParamCoreAbove.combOptionObj = ", "CombinationalOptionData3" );
			s = s.substring(0, s.length()-2)+"}";
			
			JSONArray array = JsonUtil.getJSONArray("CombinationalOptionData2->OptionValues", s);
			set = new LinkedHashSet();
			for (Object object : array) {
				JSONObject json = (JSONObject) object ;
				if(!set.contains(json.get("OptionValue1"))){
					set.add(json.get("OptionValue2")+"");
				}
			}
			return  org.apache.commons.lang3.StringUtils.join(set,",");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	

		return "";
		
	}

}
