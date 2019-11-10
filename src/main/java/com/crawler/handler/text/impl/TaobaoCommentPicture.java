package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;
import com.crawler.util.StringUtils;

public class TaobaoCommentPicture implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String src = web.getAttribute("data-src");
		if(StringUtils.isEmpty(src)){
			src=web.getAttribute("src");
		}
		
		text = FilterUtil.getRegexContent(src, "prefix|https:");
		text = text.replace("40x40", "800x800");
		text = text.replace("400x400", "800x800");
		return text;
	}

}
