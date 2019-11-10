package com.crawler.handler.text;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;

public interface IText {
	String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage);
}
