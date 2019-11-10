package com.crawler.handler.open.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.HttpUtil;
import com.crawler.util.StringUtils;
import com.google.gson.JsonObject;
import com.us.codecraft.Xsoup;

public class TaoBaoProduct implements IOpen {

	private static final Logger logger =  Logger.getLogger(TaoBaoProduct.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		String sign = storage.getLabelByIndex(0); //sign签名，手工输入,目前还没破解他啊
		if(StringUtils.isEmpty(sign) ){
			storage.getEnv().showLogArea("\n错误提示:/请输入sign签名!!!!!\n\n");
			return ;
		}
		
		
		Iterator<Product> iterator = storage.getInputUrlQueues().iterator();
		while ( iterator.hasNext()) {
			
			if(storage.getEnv().isStop()){
				break;
			}
			
			Product p = iterator.next();
			
			String crrenturl = p.getUrl();
			Storage.addText("链接", crrenturl, storage);
			
			String id =crrenturl.substring(crrenturl.indexOf("id=")+3);
			
			sign = handleDetail(storage, sign, id);  //处理商品的信息字段
			
			handleDescImg(storage, sign, id);  //处理商品详情的图片
			
			Thread.sleep(2000);
		}
		
		storage.getInputUrlQueues().clear();
		
	}


	private void handleDescImg(Storage storage, String sign, String id) {
		
		
		JSONObject jsonObject =null;
		String content=null;
		String size = "";
		String color = "";
		List<String> list= new ArrayList<>();
		try {
			
			for (int i = 0; i <100; i++) {
				String url = getTaoBaoProductDescUrl(id,sign);
				content = HttpUtil.getHtmlContent(url);
				if(!StringUtils.isEmpty(content)){
					
					jsonObject = JSONObject.parseObject(content);
					String succ = ""+jsonObject.get("ret");
					if(succ.indexOf("SUCCESS")==-1){
						storage.getEnv().showLogArea("\n错误提示:/sign签名请求失败,请输入新的sign签名!!!!!\n\n");
						Thread.sleep(10000);
						sign = storage.getLabelByIndex(0); //sign签名，手工输入,目前还没破解他啊
					}else{
						break;
					}
					
				}else{
					System.out.println("循环");
					Thread.sleep(1000);
				}
			}
		
			jsonObject = JSONObject.parseObject(content);
			JSONObject data = (JSONObject) jsonObject.get("data");
			String pcDescContent = ""+ data.get("pcDescContent");
			Document doc = Jsoup.parse(pcDescContent);
			Elements eles = Xsoup.select(doc, "//img").getElements();
			for (Element element : eles) {
				list.add("https:"+element.attr("src"));
			}
			
		
			JSONArray itemProperties = (JSONArray) data.get("itemProperties");
			for (Object objec : itemProperties) {
				JSONObject json = (JSONObject) objec;
				if("尺码".equals(""+json.get("name")) ||"尺寸".equals(""+json.get("name"))){
					size =""+ json.get("value");
				}
				
				if("颜色分类".equals(""+json.get("name")) || "主要颜色".equals(""+json.get("name"))){
					color =""+ json.get("value");
				}
			}
			
			
		}catch (Exception e) {
			System.out.println(content);
			e.printStackTrace();
		}finally{
			System.out.println("异常的ID:"+id);
			try {
				Storage.addText("详情图", StringUtils.arrayToString( list), storage);
				Storage.addText("尺码", size, storage);
				Storage.addText("颜色", color, storage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private String handleDetail(Storage storage, String sign, String id) {
		
		JSONObject jsonObject =null;
		String content =null;
		String price = "";
		String detailimgs="";
		String main="";
		String title ="";
		try {
			
			for (int i = 0; i <100; i++) {
				String url = getTaoBaoProductUrl(id,sign);
				content= HttpUtil.getHtmlContent(url);
				if (!StringUtils.isEmpty(content)) {
					
					jsonObject = JSONObject.parseObject(content);
					String succ = ""+jsonObject.get("ret");
					if(succ.indexOf("SUCCESS")==-1){
						storage.getEnv().showLogArea("\n错误提示:/sign签名请求失败,请输入新的sign签名!!!!!\n\n");
						Thread.sleep(10000);
						sign = storage.getLabelByIndex(0); //sign签名，手工输入,目前还没破解他啊
					}else{
						break;
					}
					
				}else{
					System.out.println("循环");
					Thread.sleep(1000);
				}
			}
			JSONObject data = (JSONObject) jsonObject.get("data");
			JSONObject item =(JSONObject) data.get("item");
			title = (String) item.get("title");
			JSONArray images = (JSONArray) item.get("images");
		    main ="https:"+ images.get(0);
			
			List<String> s = new ArrayList<>();
			for (Object object : images) {
				s.add(object+"");
			}
			 detailimgs = StringUtils.arrayToString(s, 1, "https:");
			
			 price = content.substring(content.indexOf("priceText"));
			price = price.substring(0, price.indexOf(","));
			price = price.replace("priceText", "");
			price = price.replace(":", "");
			price = price.replaceAll("}", "");
			price = price.replaceAll("\"","");
			price = price.replaceAll("\\\\","");
			
			
	
		}catch (Exception e) {
			System.out.println(content);
			e.printStackTrace();
			System.out.println("运行异常的链接："+id);
		}finally{
			
			try {
				Storage.addText("标题", title, storage);
				Storage.addText("价格", price, storage);
				Storage.addText("主图", main, storage);
				Storage.addText("细节图", detailimgs, storage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sign;
	}
	

	public static void main(String[] args) {
		/*String url = "https://h5.m.taobao.com/awp/core/detail.htm?spm=a1z10.5-c-s.w4002-21086417433.55.22fb5e45EIqmcr&id=596530967262";
		String id =url.substring(url.indexOf("id=")+3);
		String ss = getTaoBaoProductUrl("596549543120", "1097fbb45b1759be09894edd79e3fd14");
//		String content = HttpUtil.getHtmlContent(url);
		System.out.println(ss);*/
		String content = "transmitPrice:{\"priceText\":\"32.8\"}},asdfskuBase";
		String price = content.substring(content.indexOf("priceText"));
		price = price.substring(0, price.indexOf(","));
		price = price.replace("priceText", "");
		price = price.replace(":", "");
		price = price.replaceAll("\"", "");
		price = price.replaceAll("}", "");
		System.out.println(price);
	}
	
	public static  String getTaoBaoProductUrl(String id,String sign){
		JsonObject json = new JsonObject();  //586451114773
		json.addProperty ("id", id);
		json.addProperty("itemNumId", id);
		json.addProperty("exParams", String.format("{\"id\":\"%s\"}",id));
		return "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?appKey=12574478&sign="+sign+"&data="+URLEncoder.encode(json.toString());
	}
	
 
	public  String getTaoBaoProductDescUrl(String id, String sign){
//		JsonObject json = new JsonObject();  //586451114773
//		json.addProperty("data", );
		
		String data = URLEncoder.encode(String.format("{\"id\":\"%s\"}",id)) ; //{"id":"596549543120","type":"1"};
		
		String url = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdesc/6.0/?appKey=12574478&sign="+sign+"&api=mtop.taobao.detail.getdesc&data="+data;
		return url;
	}
	
	 

}
