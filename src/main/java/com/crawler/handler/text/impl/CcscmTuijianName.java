package com.crawler.handler.text.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

/*
 * 推荐信息
 */
public class CcscmTuijianName implements IText {

	public CcscmTuijianName(String params) {
	}
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		String tuijian = storage.getLastRow().get(12);
		String [] s = tuijian.split("<>");
		if(s.length==0){
			return "";
		}
		return getName(s[0], textUnit.getParams());
	}
	
	public static String getName(String labels, String name){
		String[] list = labels.split("\\|");
		for (String string : list) {
			if(string.indexOf(name)!=-1){
				string=string.replace("近三个月平均移动数据上网流量【月】", "");
				string=string.replace("近三个月通话平均时长【月】", "");
				string=string.replace("出账-近三个月平均收入【月】", "");
				string=string.replace("app类别", "");
				string=string.replace("线下购物偏好得分:", "");
				string=string.replace("线上购物偏好得分:", "");
				string = string.replace("[", "");
				string = string.replace("]", "");
				return string ;
			}
		}
		return "";
	}
	
}
