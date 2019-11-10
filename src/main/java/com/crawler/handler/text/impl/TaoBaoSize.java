package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class TaoBaoSize implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		if("尺码".equals(textUnit.getName()) && text==null ){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		List<WebElement> webs = webDriver.findElements(By.xpath(textUnit.getXpath()));
		for (WebElement webElement : webs) {
			if("颜色选项图片".equals(textUnit.getName())){
				String s = webElement.getAttribute("style");
				if(StringUtils.isEmpty(s)){
					return "";
				}
				
				s = s.substring(s.indexOf("url(")+5);
				s = s.substring(0,s.indexOf(")")-1);
				sb.append("https:"+s+",");
			}else if("颜色分类".equals(textUnit.getName())){
				sb.append(webElement.getAttribute("innerHTML")+",");
			}else{
				sb.append(webElement.getText()+",");
			}
		}
		String ss = sb.toString();
		if(ss.endsWith(",")){
			ss = ss.substring(0,ss.length()-1);
		}
		return ss;
	}

}
