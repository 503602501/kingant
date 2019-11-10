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
import com.crawler.util.HttpUtil;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

public class ShopeePicture implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		/*String content = HttpUtil.getHtmlContent("https://shopee.ph/api/v2/item/get?itemid=304929873&shopid=3256461","utf-8");
		JSONObject jsonObject = (JSONObject) JSONObject.parseObject(content).get("item");
		JSONArray array = (JSONArray) jsonObject.get("images");
		for (int i = 0; i < array.size(); i++) {
			System.out.println("https://cf.shopee.ph/file/"+array.get(i));
		}
				
				
		 try {
			 
			 List<WebElement> webs=webDriver.findElements(By.xpath("//div[@class='animated-lazy-image']/div"));
			 String s = null;
			 for (WebElement webElement : webs) {
				 s = webElement.getAttribute("style");
				 if(StringUtils.isEmpty(s)){
					 continue;
				 }
     			 s = s.substring(s.indexOf("https"), s.indexOf(")")-1);
				 storage.getStoreData().addText(text, "文件夹名-商品名",storage);
				 storage.getStoreData().addText(s, "图片链接",storage);
				 storage.getStoreData().addText(text, "文件夹名-商品名",storage);
				 storage.getStoreData().addText(s.replace("_tn", ""), "图片链接",storage);
 			 }
			 
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		 return "";
	}

}
