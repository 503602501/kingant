package com.crawler.handler.text.impl;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

public class WinConverText implements IText {

	public WinConverText(String s) {
	}
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		try {
			Integer a = Integer.parseInt(textUnit.getParams().split("\\|")[0].trim());
			Integer b =  Integer.parseInt(textUnit.getParams().split("\\|")[1].trim());
			
			List<String> row = storage.getLastRow() ; 
			
			Integer aa = 0;
			if(!StringUtils.isEmpty(row.get(a))){
				 aa = Integer.parseInt(row.get(a).trim());
			}
			Integer bb =  0;
			if(!StringUtils.isEmpty(row.get(b))){
				bb = Integer.parseInt(row.get(b).trim());
			}
			
			
			if(bb==0){
				return "";
			}
			
			if(aa!=null && bb!=null){
				String s =  NumberUtil.formatTwo(aa*1.0/bb);
				if("0.00".equals(s)){
					return "0";
				}
				return s; 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
		return "空数据";
		 
	}
	 

}
