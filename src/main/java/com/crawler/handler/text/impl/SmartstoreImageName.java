package com.crawler.handler.text.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;


public class SmartstoreImageName implements IText {

	public SmartstoreImageName(String s ) {
		
	}
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		String s = storage.getLastRow().get(Integer.parseInt(textUnit.getParams()));
		List<String> imgs = new ArrayList<>();
		for (String string : s.split(",")) {
			string = string.substring(string.lastIndexOf("/")+1);
			imgs.add(string.trim());
		}
		return imgs.toString().replace("[", "").replace("]", "").replace(", ", ",");
	}

}
