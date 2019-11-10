package com.crawler.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static boolean isEmpty(Object object) {
		if(object==null|| "".equals(object.toString().trim())){
			return true;
		}
		return false;

	}
 
	public static String delHtml(String text) {
		   String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
	        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
	        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
	         
	        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
	        Matcher m_script=p_script.matcher(text); 
	        text=m_script.replaceAll(""); //过滤script标签 
	         
	        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
	        Matcher m_style=p_style.matcher(text); 
	        text=m_style.replaceAll(""); //过滤style标签 
	         
	        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
	        Matcher m_html=p_html.matcher(text); 
	        text=m_html.replaceAll(""); //过滤html标签 
	        text=text.replaceAll("\r|\n", "");
	        text=text.replaceAll("&nbsp;", "");
	        return text.replaceAll(" +"," "); //返回文本字符串 
	}
	
	public static String arrayToString(String[] objects) {
		return arrayToString(objects, 0);
	}
	
	/**
	 * 数组转成字符串,from开始位置
	 */
	public static String arrayToString(String[] array,Integer from ) {
		return arrayToString(array, from, "");
	}
	
	/**
	 * 数组转成字符串,from开始位置
	 */
	public static String arrayToString(String[] array,Integer from ,String prefix) {
		StringBuffer sb = new StringBuffer();
		for (int i = from; i < array.length; i++) {
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append(prefix+array[i]);
		}
		
		return sb.toString();
	}
	
	/**
	 * 数组转成字符串,from开始位置
	 */
	public static String arrayToString(List list,Integer from ,String prefix) {
		StringBuffer sb = new StringBuffer();
		for (int i = from; i < list.size(); i++) {
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append(prefix+list.get(i));
		}
		
		return sb.toString();
	}
	
	/**
	 * 数组转成字符串,from开始位置
	 */
	public static String arrayToString(List list) {
		return arrayToString(list, 0, "");
	}
	
	
}
