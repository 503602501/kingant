
package com.crawler.handler.text.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class CoupangSizeText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		 WebElement  webElement =webDriver.findElement (By.xpath("//ul[@class='Dropdown-Select__Dropdown']"));
		
		 String s = webElement.getAttribute("innerHTML");
		 s =s.replaceAll("</li>", ",</li>");
		 
		 s = StringUtils.delHtml(s);
		 return s ;
		
	}

}
