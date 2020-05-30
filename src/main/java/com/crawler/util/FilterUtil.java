package com.crawler.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.DefaultEditorKit.CutAction;

public class FilterUtil {
	
	
	public static String getRegexContent(String content, String regex) {
		return getRegexContent(content, regex, "");
	}
	
	public static String getRegexContent(String content, String regex, String params) {

		if(content == null){
			return "";
		}
		if (regex.indexOf("match") == 0) { // 正则表达式匹配字符串
			String regexMatch = regex.replace("match|", "");
			Pattern p = Pattern.compile(regexMatch);
			Matcher m = p.matcher(content);
			if (m.find()) {
				content = m.group(0);
			}
			
		}

		if (regex.indexOf("replace") == 0) {
			String regexReplace = regex.replace("replace|", "");
			System.out.println(content);
			content = content.replaceAll(regexReplace, params);
		}

		if (regex.indexOf("prefix") == 0) {
			String prefix =  regex.replace("prefix|", "");
			if(!content.startsWith(prefix)){
				content = prefix + content;
			}
		}
		
		return content;
	}
	
	/**
	 * 截取字符串
	 * @param from
	 * @param to
	 */
	public  static String  cutString(String content , String from ,String to) {
		String s =  getRegexContent(content, "match|(?<="+from+")(.*)(?="+to+")");
		if(s.indexOf(to)!=-1){
			s = s.substring(0,s.indexOf(to));
		}
		return s;
	}
	
	public static void main(String[] args) {
		
		String s = " (1黄埔1) 中山大道中金碧世纪花园";
		System.out.println(getRegexContent(s, "match|(?<=(1)(.*)(?=1))"));
//		System.out.println(cutString(s,"抖音号：<span>" , "</span>"));
	}
}
