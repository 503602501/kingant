package com.crawler.handler.text.impl;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;


/*
 * 处理url的最后的一个斜杠为文件夹的名
 */
public class UrlFolderHandler implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		text = webDriver.getCurrentUrl();
		String folder = text.substring(text.lastIndexOf("/")+1);
		return folder;
	}
}
