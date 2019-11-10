package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class CpppcUrl implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		 return "http://www.cpppc.org:8083/efmisweb/ppp/projectLibrary/getProjInfoNational.do?projId="+text.replaceAll("\"", "");
	}

}
