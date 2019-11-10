package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.ImageUtil;
import com.crawler.util.NumberUtil;

public class CommonPictureText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		/* String http =  url.substring(0,url.lastIndexOf("/")+1);
		  String name = url.substring(url.lastIndexOf("/")+1);
		  String prefix = "";
		  if(name.lastIndexOf(".") >name.lastIndexOf("/") ){
			  prefix=name.substring(name.lastIndexOf("."));
		  }
		  name=name.replaceAll(prefix, "");
		  return http+"/"+NumberUtil.autoNumber()+ImageUtil.validatePrefix(name.hashCode()+prefix);
		  */
		return "";
	}

}
