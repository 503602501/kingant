package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class IkeaUrlText implements IText {
	
	private static List<String> cache = new ArrayList<>();

	@Override
	public String getText(String text,WebElement webss,WebDriver webDriver,TextUnit textUnit,Storage storage) {
	
		text = text.replace("https://www.ikea.cn/cn/zh/catalog/products/", "");
		text=text.replace("/", "");
		
		if(cache.size()!=0 && !cache.contains(webDriver.getCurrentUrl())){
			cache.clear();
		}
	
		if( !cache.contains(webDriver.getCurrentUrl())){
			getRegexFilter(webDriver, storage);
			cache.add(webDriver.getCurrentUrl());
		}
			
	
		
		return text;
	}

	private void getRegexFilter(WebDriver webDriver, Storage storage) {
		List<WebElement>  webs = webDriver.findElements(By.xpath("//a[contains(@onclick,'ProductClicked')]"));
			WebElement web = webDriver.findElement(By.xpath("//div[@id='productLists']"));
			long s = System.currentTimeMillis() ;
			String str = web.getAttribute("innerHTML"); 
			str =str.replaceAll("\t|\r|\n","");
			str = str.replace("</script>", "");
			str = StringUtils.delHtml(str);
			for (String string : str.split("jsonPartNumbers.push")) {
				if(string.indexOf(")")==-1){
					continue;
				}
				
				string = string.substring(1, string.indexOf(")"));
				string = string.replace(")", "");
				string = string.replace("[", "");
				string = string.replace("]", "");
				string = string.replace("\"", "");
				 
				for (String  h : string.split(",")) {
					if(StringUtils.isEmpty(h)){
						continue;
					}
					try {
						storage.getStoreData().addText(h, "货号",storage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
	}

}
