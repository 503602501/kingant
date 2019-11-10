package com.crawler.handler.text.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.text.IText;

public class MobileFilter implements IText {

	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		return checkCellphone(text);
		
	}
	
	
	
	 public static void main(String[] args){
	        //要提前号码的字符串
	        String str="n13977777777s18911111111你好15988888888hha0955-7777777sss0775-6678111";
	        //提取手机号码
	        checkCellphone(str);
	        //提取固定电话号码
	        checkTelephone(str);
	    }
	     
	    /**
	     * 查询符合的手机号码
	     * @param str
	     */
	    public static String checkCellphone(String str){
	    	StringBuffer sb =new StringBuffer();
	            // 将给定的正则表达式编译到模式中
	        Pattern pattern = Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}");
	        // 创建匹配给定输入与此模式的匹配器。
	        Matcher matcher = pattern.matcher(str);
	        //查找字符串中是否有符合的子字符串
	        while(matcher.find()){
	                //查找到符合的即输出
	        	if(sb.length()!=0){
	        		sb.append(",");
	        	}
	        	sb.append(matcher.group() );
//	            System.out.println("查询到一个符合的手机号码："+matcher.group());
	        }
//	        System.out.println(sb.toString());
	        return sb.toString();
	    }
	     
	    /**
	     * 查询符合的固定电话
	     * @param str
	     */
	    public static void checkTelephone(String str){
	         // 将给定的正则表达式编译到模式中
	        Pattern pattern = Pattern.compile("(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)");
	        // 创建匹配给定输入与此模式的匹配器。
	        Matcher matcher = pattern.matcher(str);
	        //查找字符串中是否有符合的子字符串
	        while(matcher.find()){
	                 //查找到符合的即输出
	            System.out.println("查询到一个符合的固定号码："+matcher.group());
	        }
	    }
	    
}
