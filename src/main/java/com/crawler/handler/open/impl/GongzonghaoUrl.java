package com.crawler.handler.open.impl;

import java.util.List;


import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.DriverUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.JsonUtil;
import com.crawler.util.StringUtils;

/**
 * 公众号链接采集
 */
public class GongzonghaoUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(GongzonghaoUrl.class);
	
	
	/**
	 * 
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		//cookies信息
		String cookies = storage.getLabelByIndex(0); 
		if(StringUtils.isEmpty(cookies)){
			storage.getEnv().showLogArea("\n错误提示:/cookies信息不能为空!!!!!\n\n");
			return ;
		}
		Product product = storage.getInputUrlProduct();
		
		
//		String "https://mp.weixin.qq.com/cgi-bin/appmsg?token=1240112176&lang=zh_CN&f=json&ajax=1&action=list_ex&begin=0&count=5&query=&fakeid=MjM5ODYxMDA5OQ%3D%3D&type=9";
		String url = product.getUrl() ;
		String offset = HttpUtil.getQueryString(url, "offset");
		if(StringUtils.isEmpty(offset)){
			offset = "10";
		}
		if(url.indexOf("count")==-1){
			url+="&count=10";
		}
		//默认最多是200页
		for (int i = 0; i < 200; i++) {
			String s =  HttpUtil.getHtmlContent(url.replace("offset="+offset, "offset="+i*10),"UTF-8",cookies);
			System.out.println(s);
			String msg_count= JsonUtil.getJSONValue("msg_count",s);
			if("0".equals(msg_count)){
				System.out.println("公众号没有没有数据啦！！！！！！！！！！！！！！！！！！");
				break;
			}
			String general_msg_list= JsonUtil.getJSONValue("general_msg_list",s);
			JSONObject jsonObject = JSONObject.parseObject(general_msg_list);
			if(jsonObject==null){
				System.out.println(general_msg_list);
			}
			List<String> list = JsonUtil.getJSONListValue("list->app_msg_ext_info->content_url", jsonObject.toJSONString());
			for (String contenturl : list) {
				storage.addText("链接", contenturl, storage);
			}
			Thread.sleep(5000);
			
		}
 
		
		
		/*
		 *
		 * 微信公众号后端采集，出现反采集。。。。
		 String content =  HttpUtil.getHtmlContent(url,"UTF-8",cookies);  //第一页
		if(content==null){
			System.out.println(content);
		}
		String app_msg_cnt = JsonUtil.getJSONValue("app_msg_cnt", content);  //总条数
		Integer pages = (Integer.parseInt(app_msg_cnt) - 1) / 5 + 1;   //总页数
		Integer pageSize = Integer.parseInt( HttpUtil.getQueryString(url, "count"));  //每页的条数
		
		String begin = HttpUtil.getQueryString(url, "begin");
		String currentUrl = "";
		for (int i = 1; i <= pages; i++) {
			Integer start = (i-1)*pageSize ;
			currentUrl =  url.replaceFirst("begin="+begin, "begin="+start);
			content =  HttpUtil.getHtmlContent(currentUrl,"UTF-8",cookies);  
			Thread.sleep(5000);  //停止5秒！！！！！！！！！！！！
			System.out.println("返回的内容:"+content);
			List<String> list = JsonUtil.getJSONListValue("app_msg_list->link", content);
			for (String string : list) {
				storage.addText("链接", string, storage);
			}
			
		}*/
		
		storage.getInputUrlQueues().clear();
		 
	}

}
