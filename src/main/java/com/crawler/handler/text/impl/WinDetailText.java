package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.StringUtils;

public class WinDetailText implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		boolean flag = false;
		List<String> row  =  storage.getLastRow();
		
		String s5 = row.get(4);
		if(!StringUtils.isEmpty(s5)){
			Float f = Float.parseFloat(s5);
			if(f.compareTo(new Float(1.15))==1 || f.compareTo(new Float(1.15))==0){
				flag = true;
			}
		}
		
		String s6 =row.get(5);
		if(!StringUtils.isEmpty(s6)){
			Float f = Float.parseFloat(s6);
			if(f.compareTo(new Float(1.15))==1 || f.compareTo(new Float(1.15))==0){
				flag = true;
			}
		}
		
		if(!StringUtils.isEmpty(text)){
			Float f = Float.parseFloat(text);
			if(f.compareTo(new Float(1.15))==1 || f.compareTo(new Float(1.15))==0){
				flag = true;
			}
		}
		
		if(flag==false){  //如果没有大于1.15,清空
			row.set(4,"");
			row.set(5,"");
			text= "";
		}
		
		return text;
		
	}

}
