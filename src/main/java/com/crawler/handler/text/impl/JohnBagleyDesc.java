package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;
import com.crawler.util.StringUtils;

public class JohnBagleyDesc implements IText {

	private int i = 0;
	public JohnBagleyDesc(String s ) {
		i = Integer.parseInt(s);
	}
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {

		text = text.replace("&nbsp;", " ");
		
		String br[] = text.split("<br>");
		if(br.length>i ){
			if(!StringUtils.isEmpty(br[i]) ){
				System.out.println(br[i]);
				if(br[i].contains(":")){
					return br[i].split(":")[1];
				}
				if(br[i].contains("：")){
					return br[i].split("：")[1];
				}
			}
		}
		return "";
		
	}
}
