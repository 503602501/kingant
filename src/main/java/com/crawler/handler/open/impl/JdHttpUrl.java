package com.crawler.handler.open.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.entity.StoreData;
import com.crawler.entity.Vcard;
import com.crawler.handler.open.IOpen;
import com.crawler.handler.text.impl.JdEntity;
import com.crawler.util.ExcelUtil;
import com.crawler.util.FolderUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.StringUtils;
import com.us.codecraft.Xsoup;

/*
 * 手机号码转换vcard处理类
 */
public class JdHttpUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(JdHttpUrl.class);

	/*
	 * 人工天猫源码采集
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		Integer to = storage.getEnv().getWidgets().getToFieldValue() ; //*2-1;
		Integer from = storage.getEnv().getWidgets().getFromFieldValue(); //*2-1;
		
		String key = storage.getLabelByIndex(0);  
		List<JdEntity>  list ;
		for (int i = from; i <= to; i++) {
			list = getPageData(key, i);
			for (JdEntity object : list) {
				 storage.getStoreData().addText(object.getUrl() , "链接",storage);
				 storage.getStoreData().addText(object.getShopName(), "店铺",storage);
			}
			Thread.sleep(1000);
		}
		
	 
	}
	

	private static List<JdEntity> getPageData(String key,Integer page ) throws Exception {
		String url = "https://search.jd.com/s_new.php";
		String refer = "https://search.jd.com/Search";
		Integer s=( page-1)*30+1;
		List<JdEntity> list = new ArrayList<>();
		List<NameValuePair> urlParameters  = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("keyword", key)); //
		urlParameters.add(new BasicNameValuePair("enc", "utf-8"));
		urlParameters.add(new BasicNameValuePair("qrst", "1"));
		urlParameters.add(new BasicNameValuePair("rt", "1"));
		urlParameters.add(new BasicNameValuePair("stop", "1"));
		urlParameters.add(new BasicNameValuePair("vt", "2"));
		urlParameters.add(new BasicNameValuePair("cod", "1"));  //货到付款
		urlParameters.add(new BasicNameValuePair("psort", "4"));  //评论倒叙  psort=4
		urlParameters.add(new BasicNameValuePair("page", page+""));
		urlParameters.add(new BasicNameValuePair("s", s+""));

		
		urlParameters.add(new BasicNameValuePair("cid3", "13673"));  //货到付款 cid3=13673
		urlParameters.add(new BasicNameValuePair("cid2", "5026"));  //&cid2=5026
		
		
		if(page%2==0){
			urlParameters.add(new BasicNameValuePair("scrolling", "y"));
		}
		
		
		String content = HttpUtil.getHtmlContent(url,urlParameters,refer);
		if(content.indexOf("<script>")!=-1){
			content= content.substring(0,content.indexOf("<script>"));
		}
		
		System.out.println(content);
		
		Document doc =null;
		Document element = null;
		List<Node> nodes =null;
		doc = Jsoup.parse(content); 
		
		if(page%2!=0){
			content = Xsoup.select(doc, "//body/div/ul").get();
			System.out.println(content);
			content = content.substring(content.indexOf("li")-1);
			content = content.substring(0,content.lastIndexOf("ul")-2);
			doc = Jsoup.parse(content);
		}
		
		nodes = doc.body().childNodes();
		
		
		String href = "";
		String shopName = "";
		for (Node node : nodes) {
			if(StringUtils.isEmpty(node.toString())){
				continue;
			}
			
			element =  Jsoup.parse(node.toString());
//			System.out.println(node.toString());
			content = Xsoup.select(element, "//body/li/div/div[contains(@class,'p-shop')]/span/a").get();
			href ="http:"+content.substring(content.indexOf("href")+6, content.indexOf("html")+4);
			shopName =content.substring(content.indexOf("title")+6, content.indexOf("</a>"));
			shopName=shopName.substring(shopName.indexOf(">")+1);
			if(shopName.indexOf("京东自营")!=-1){  //去掉官方
				continue;
			}
			
			JdEntity e = new JdEntity();
			e.setShopName(shopName);
			e.setUrl(href);
			list.add(e);
			System.out.println(href+"|"+shopName);
		}
		return list;
	} 
	
 
}
