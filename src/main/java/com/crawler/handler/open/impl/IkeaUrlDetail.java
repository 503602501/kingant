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
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

public class IkeaUrlDetail implements IOpen {

	private static final Logger logger =  Logger.getLogger(IkeaUrlDetail.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		StringBuffer sb = new StringBuffer();
		List<String> urlList= new ArrayList<>();
		List<Product> newList= new ArrayList<>();  //新的属性产品
		String number = "";
		String urls = "";
		List<Product> list = new LinkedList<>(storage.getInputUrlQueues());
		
		handlerUrl(storage, urlList, list,newList);
		
		storage.getQueues().clear();
		list = null;
		storage.getInputUrlQueues().clear();
	}

	private void handlerUrl(Storage storage, List<String> urlList, List<Product> list, List<Product> newList) throws Exception {
		String urls;
		Iterator<Product> iterator = list.iterator();
		while ( iterator.hasNext()) {
			Product  p = iterator.next();
			String number = p.getUrl().replaceAll("\\.", "");
			String ulr =null;
			if(number.substring(1, 2).equals("0") ){
				ulr = "https://www.ikea.cn/cn/zh/catalog/products/"+number;
			}else{
				number = number.replace("S", "");
				ulr ="https://www.ikea.cn/cn/zh/catalog/products/S"+number;
			}
			
			
			
			try {
				number = number.substring(0, 3)+"."+number.substring(3, 6)+"."+number.substring(6) ;
			} catch (Exception e1) {
				logger.error("字符转换失败:"+number);
				continue;
			}
			
			
			if(!urlList.contains(ulr)){
				urlList.add(p.getUrl());
			}else{
				continue;
			}
			
			if(storage.getEnv().isStop()){
				return ;
			}
 
			/*if(p.getUrl().substring(1, 2).equals("0") ){
				p.setUrl("https://www.ikea.cn/cn/zh/catalog/products/"+s);
			}else{
				p.setUrl("https://www.ikea.cn/cn/zh/catalog/products/S"+s);
			}*/
			
			
		/*	String attribute="";
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
			}*/
			
			try {
				String content = HttpUtil.getHtmlContent(ulr);
				String desc ="";
				if( content.indexOf("custMaterials")!=-1){
					desc =content.substring( content.indexOf("custMaterials"));
					desc = desc.substring(0, desc.indexOf("</div>"));
					desc = desc.substring(desc.indexOf(">")+1);
					desc = desc.replaceAll("<br/>", "\r\n");
				}
				
				String chicun = "";
				if( content.indexOf("metric")!=-1){
					chicun =content.substring( content.indexOf("metric"));
					chicun = chicun.substring(0, chicun.indexOf("</div>"));
					chicun = chicun.substring(chicun.indexOf(">")+1);
					chicun = chicun.replaceAll("<br/>", "\r\n");
				}
				
				String tuijian = "";
				if(content.indexOf("texts keyFeaturesmargin")!=-1){
					tuijian =content.substring( content.indexOf("texts keyFeaturesmargin")+2);
					tuijian = tuijian.replace("xts keyFeaturesmargin", "");
					tuijian = tuijian.replace("\">", "");
					tuijian = tuijian.substring(0, tuijian.indexOf("goodToKnowPart"));
					tuijian = tuijian.replace("<div id=\"", "");
					tuijian = tuijian.substring(0, tuijian.lastIndexOf("</div>"));
					tuijian = tuijian.substring(0, tuijian.lastIndexOf("</div>"));
				}
				
				String tishi = "";
				if(content.indexOf("id=\"goodToKnow\"")!=-1){
					tishi =content.substring( content.indexOf("id=\"goodToKnow\""));
					tishi = tishi.substring(tishi.indexOf(">")+1);
					tishi = tishi.substring(0, tishi.indexOf("</div>"));
				}
				
				String baoyang = "";
				if(content.indexOf("id=\"careInst\"")!=-1){
					baoyang =content.substring( content.indexOf("id=\"careInst\""));
					baoyang = baoyang.substring(baoyang.indexOf(">")+1);
					baoyang = baoyang.substring(0, baoyang.indexOf("</div>"));
				}
				
				String xugou= "";
				if(content.indexOf("id=\"soldSeparately\"")!=-1){
					xugou =content.substring( content.indexOf("id=\"soldSeparately\""));
					xugou = xugou.substring(xugou.indexOf(">")+1);
					xugou = xugou.substring(0, xugou.indexOf("</div>"));
				}
				
				
				if(StringUtils.isEmpty(content)){
					continue;
				}
				
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
				 storage.getStoreData().addText(number, "产品货号",storage);
				 storage.getStoreData().addText(name, "品名",storage);
				 storage.getStoreData().addText(price, "单价",storage);
				 storage.getStoreData().addText(chicun, "安装后尺寸",storage);
				 storage.getStoreData().addText(desc, "产品描述",storage);
				 storage.getStoreData().addText(tuijian, "推荐理由",storage);
				 storage.getStoreData().addText(tishi, "相关提示",storage);
				 storage.getStoreData().addText(baoyang, "保养说明",storage);
				 storage.getStoreData().addText(xugou, "另需购",storage);
				 
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
