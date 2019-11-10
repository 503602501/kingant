package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class E3E3Item implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		WebElement element = webDriver.findElement(By.xpath("//div[@class='properties-box-c']"));
		
		WebElement priceElement = webDriver.findElement(By.xpath("//p[@class='price']/strong"));
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject =null ;
		
		String[] names = text.split("\\s+");
		String[] colors =  element.getText().split("\\s+");
		
		for (int i = 0; i < names.length; i++) {
			for (int j = 0; j < colors.length; j++) {
				
				jsonObject = new JSONObject();
				jsonObject.put("name", names[i]+"/"+colors[j]);
				jsonObject.put("price", priceElement.getText());
				jsonArray.add(jsonObject);
			}
		}
		
		return jsonArray.toJSONString();
		
	}

}
