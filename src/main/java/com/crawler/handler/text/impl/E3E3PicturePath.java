package com.crawler.handler.text.impl;

import java.util.List;

import org.apache.xalan.templates.ElemApplyImport;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class E3E3PicturePath implements IText {
	
	private E3E3Picture e3e3Picture = new E3E3Picture();

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String s = e3e3Picture.getText(text, web, webDriver, textUnit, storage);
		String[] urls = s.split(",");
		List<String> list = storage.getLastRow();
		String name =list.get(2);
		StringBuffer sb = new StringBuffer();
		for (String string : urls) {
			if(sb.length()!=0){
				sb.append("|");
			}
			sb.append("/"+name+ string.substring(string.lastIndexOf("/"))) ;
		}
		return sb.toString();
	}

}
