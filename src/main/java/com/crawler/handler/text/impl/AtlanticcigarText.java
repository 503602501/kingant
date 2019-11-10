package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;


public class AtlanticcigarText implements IText {

	private String text;
	public AtlanticcigarText(String text ) {
		this.text=text;
	}
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		WebElement tr = webDriver.findElement(By.xpath("//table[@class='compatibility']/tbody/tr[1]"));
		String[] array = tr.getAttribute("innerHTML").split("</th>");
		Integer index = null;
		for (int i = 0; i < array.length; i++) {
			if(array[i].indexOf(textUnit.getParams())!=-1){
				index =i; 
				break;
			}
		}
		
		if(index==null){
			return "";
		}
	 
		String[] td = text.split("</td>");
		String content =  StringUtils.delHtml( td[index]);
		System.out.println(content);
//		content = content.substring(content.lastIndexOf(">")).replaceAll("</a>", "");
		return content ;
	}

}
