package com.crawler.handler.open.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.FilterUtil;
import com.crawler.util.HttpUtil;

public class IkeaUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(IkeaUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		StringBuffer sb = new StringBuffer();
		List<String> urlList= new ArrayList<>();
		List<Product> newList= new ArrayList<>();  //新的属性产品
		String number = "";
		String urls = "";
		List<Product> list = new LinkedList<>(storage.getQueues());
		//list 需要循环的list
		while(true){
			if(newList.size()==0 && urlList.size()!=0){
				break;
			}else{
				list.addAll(newList);
				newList.clear();
			}
			
			handlerUrl(storage, urlList, number, list,newList);
			
		}
		
		storage.getQueues().clear();
		storage.getInputUrlQueues().clear();
	}

	private void handlerUrl(Storage storage, List<String> urlList, String number, List<Product> list, List<Product> newList) throws Exception {
		String urls;
		Iterator<Product> iterator = list.iterator();
		while ( iterator.hasNext()) {
			Product p = iterator.next();
			if(!urlList.contains(p.getUrl())){
				urlList.add(p.getUrl());
			}else{
				continue;
			}
			
			if(storage.getEnv().isStop()){
				return ;
			}
			try {
				number =  p.getUrl().substring( p.getUrl().lastIndexOf("products/")).replace("products", "").replace("/", "");
			} catch (Exception e) {
				logger.error(e);
				logger.error(p.getUrl());
			}
			number = number.substring(0, 3)+"."+number.substring(3, 6)+"."+number.substring(6) ;
			String s =  p.getUrl().replaceAll("\\.", "") ; 
			
			String content = HttpUtil.getHtmlContent(p.getUrl());
			String attribute="";
			if(content.indexOf("dropAllAttributes")!=-1){
				urls = content.substring(content.indexOf("dropAllAttributes"));
				urls=urls.substring(0,urls.indexOf("</select>"));
				for (String item : urls.split("<option")) {
					if(item.indexOf("https")==-1){
						continue;
					}
					
					attribute=FilterUtil.getRegexContent(item, "match|https(.+?)(?=value)").replaceAll("\"", "").trim();
					
					if(!urlList.contains(attribute)){
						ArrayList<String> ss = new ArrayList<String>();
						ss.add(attribute);
						Product product = new Product();
						product.setList(ss);
						product.setHeader(storage.getStoreData().getHeader());
						product.setUrl(""+ss.get(storage.getStoreData().getHeader().indexOf("链接"))); //默认第一个为链接
//						storage.push(product);
						newList.add(product);
//						storage.getStoreData().addProduct(ss, storage);
					}
				}
			}
			
			try {
				String regexMatch = "(?<=jProductData =)(.*)";
				Pattern pp = Pattern.compile(regexMatch);
				Matcher m = pp.matcher(content);
				m.find();
				String format = m.group(0).trim();
				if(format.length()==(format.lastIndexOf(";")+1)){
					format = format.substring(0, format.length()-1);
				}
				
			/*	 JSONObject object = (JSONObject) JSONObject.parse(format) ; //.fromObject(format);
				 JSONObject product =(JSONObject) object.get("product");*/
//				 JSONArray items =  (JSONArray) product.get("items") ;
				 
					String regexMatchs = "(?<=<meta name=\"item_id\")(.*/>)";
					Pattern ps = Pattern.compile(regexMatchs);
					Matcher ms = ps.matcher(content);
					ms.find();
					String formats = ms.group(0).trim();
					
					formats = formats.replace("content=", "").replace("\"", "").replace("/>", "").trim();
					
					 JSONObject object = (JSONObject) JSONObject.parse(format) ; //.fromObject(format);
					 JSONObject product =(JSONObject) object.get("product");
					 JSONArray items =  (JSONArray) product.get("items") ;
					 
					 JSONObject  item = null;
					 for (int i = 0; i < items.size(); i++) {
						 JSONObject j =  (JSONObject) items.get(i);
						if( formats.equals(j.get("catEntryId"))){
							item= j;
						}
					 }
					 
				 String name = (String) item.get("name")+"  "+item.get("type");
				 
				 
				 JSONArray validDesign = (JSONArray)item.get("validDesign");
				 StringBuffer stringBuffer = new StringBuffer(" ");
				 for (Object o : validDesign) {
					 if(stringBuffer.length()!=0){
						 stringBuffer.append(",");
					 }
					 stringBuffer.append(o);
				 }
				 name+=stringBuffer.toString();
				 
				 JSONObject prices =(JSONObject) item.get("prices");
				 JSONObject normal =(JSONObject) prices.get("normal");
				 JSONObject priceNormal =(JSONObject) normal.get("priceNormal");
				 
				 JSONArray zooms =(JSONArray) ((JSONObject) item.get("images")).get("zoom");
				 
				 String price = ""+ priceNormal.get("rawPrice");
				 storage.getStoreData().addText(p.getUrl(), "链接",storage);
				 storage.getStoreData().addText(number, "文件名-产品货号",storage);
				 storage.getStoreData().addText(name, "品名",storage);
				 storage.getStoreData().addText(price, "单价",storage);
				 
				 for (int i = 1; i <= 10; i++) {
					if( i<=zooms.size() ){
						storage.getStoreData().addText("https://www.ikea.cn"+zooms.get(i-1), "图片"+i,storage);
					}else{
						storage.getStoreData().addText("", "图片"+i,storage);
					}
				}
				Thread.sleep(RandomUtils.nextInt(1, 3000));
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				logger.info(number);
			}
			
		}
	}
	
 public static void main(String[] args) {
	 System.out.println(FilterUtil.getRegexContent("2017-9-16点评 通过移动设备发表", "match|.*点评"));
}

}
