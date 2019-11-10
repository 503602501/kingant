package com.crawler.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.crawler.entity.Item;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Xpath;
import com.crawler.handler.AnalyseXpath;

/*
 * 提取配置文件信息
 */
public class XpathUtil {
	
	public static void main(String[] args) {
		String lastUrl = "https://search.jd.com/search?keyword=%E9%AA%91%E8%A1%8C%E5%A4%B4%E5%B7%BE&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E9%AA%91%E8%A1%8Ctouj&page=5&s=106&click=0";
		String s  =lastUrl.substring(lastUrl.indexOf("page="));
		s = s.substring(0,s.indexOf("&"));
		System.out.println(s);
		System.out.println(lastUrl);
	}
}
