package com.crawler.handler.open.impl;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.FilterUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.StringUtils;

public class ZmazonhttpUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(ZmazonhttpUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		 
		
	/*	driverEntity = storage.getDriverEntity(openUnit.getBrower(),openUnit.getImage()); //获取驱动
		WebDriver webDriver = driverEntity.getWebDriver();*/
		
		//打开网页
	 
		 for (Product product : storage.getInputUrlQueues()) {
		 
			if(storage.getEnv().isStop()){
				break;
			}
				
			try {
				String content = HttpUtil.getHtmlContent(product.getUrl());
				String url  = URLDecoder.decode(product.getUrl());
				String keyword = "";
				if(url.indexOf("keywords=")!=-1){
					keyword = url.substring(url.indexOf("keywords="));
					keyword = keyword.replace("keywords=", "");
					keyword = keyword.substring(0,keyword.indexOf("&"));
				}
					
				content = HttpUtil.removeSpecilChar(content);
				String date = FilterUtil.cutString( content, "Date First Available", "</td>");
				date =StringUtils.delHtml(date);
				
//				String asin= FilterUtil.cutString( content, "data-detailPageAsin", ">");
//				asin = asin.replace("\"", "").replace("=", "");
				
				String asin= FilterUtil.cutString( content, "id=\"ASIN\"", ">");
				asin = asin.replace("\"", "").replace("=", "");
				asin= asin.substring(asin.indexOf("value") );
				asin = asin.replace("value", "");
				  
				  
				  
				 String review= FilterUtil.cutString( content, "acrCustomerReviewText", "</span>");
				 review = review.substring(review.indexOf(">"));
				 if(review.indexOf(">")!=-1){
					 review = review.replace("ratings", "").replace(">", "");
				 }else{
					 review="";
				 }
				  
				 
				 String rank = FilterUtil.cutString( content, "class=\"a-icon-alt\">", "<");
				 rank = rank.replace("\"", "").replace("=", "").replace(">", "");
				 rank = rank.replace("out of 5 stars", "");
				 if(rank.length()>10){
					 rank="";
				 }
				/* String band = FilterUtil.cutString( content, "a-color-secondary a-size-base prodDetSectionEntry", "</td>");
				 band = band.substring(band.indexOf("a-size-base"));
				 band = band.replace("\"", "").replace("=", "").replace(">", "").replace("a-size-base", "").trim();
				 if(StringUtils.isEmpty(band)){ 
					 band = FilterUtil.cutString( content, "id=\"bylineInfo\"", "</div>"); 
					 band= StringUtils.delHtml("<"+band);
				 }
				 if(band.indexOf("line-height")!=-1){
					 band = "";
				 }*/
				 String band = "";
				 if(content.indexOf("id=\"bylineInfo\"")!=-1){
					 band = content.substring(content.indexOf("id=\"bylineInfo\""));
					 band = band.substring(0,band.indexOf("</div>"));
					 band = StringUtils.delHtml("<"+band);
					 band = band.replace("Visit the", "").replace("Store", "").replace("Brand:", "").trim();
				 }
				 
				  String price= FilterUtil.cutString( content, "a-size-medium a-color-price priceBlockBuyingPriceString", "<");
				  if(StringUtils.isEmpty(price)){
					   price= FilterUtil.cutString( content, "a-size-medium a-color-price priceBlockSalePriceString", "<");
				  }
				  
				  if(StringUtils.isEmpty(price)){
					  price= FilterUtil.cutString( content, "olp-message a-color-price", "</span>");
					  price = price.substring(price.indexOf("US"));
				  }
				  
				  
				  price = price.replace("\"", "").replace("=", "").replace(">", "");
				  if(price.length()>100){
					  
					  price= FilterUtil.cutString( content, "priceblock_pospromoprice", "</span>");
					  
					  if(!StringUtils.isEmpty(price)){
						  price =price.substring(price.indexOf("$"));
					  }else{
						  price="" ;
					  }
				  }
				  
				  if(price.length()>100  ){
					  price= FilterUtil.cutString( content, "a-section a-spacing-small a-spacing-top-small", "</span>");
					  
					  if(price.indexOf("$")!=-1){
						  price =price.substring(price.indexOf("$"));
					  }
					  
				  }
				  
				  
				  if(price.length()>100){
					  price ="" ;
				  }
				  
				  String seller= FilterUtil.cutString( content, "sellerProfileTriggerId'", "</a>");
				  seller = seller.replace("\"", "").replace("=", "").replace(">", "");
				  if(seller.indexOf("!doctype")!=-1){
					  seller="";
				  }
				  
				  String title = FilterUtil.cutString( content, "<title>", "</title>");
				  title =title.replace("Amazon.com:", "").replace(": Gateway", "");
				  
				  storage.getStoreData().addText(product.getUrl(), "产品链接",storage);
				  storage.getStoreData().addText(keyword, "关键词",storage);
				  storage.getStoreData().addText(title, "标题",storage);
				  storage.getStoreData().addText(price, "售价",storage);
				  storage.getStoreData().addText(review, "评论数",storage);
				  storage.getStoreData().addText(rank, "星级",storage);
				  storage.getStoreData().addText(band, "品牌",storage);
				  storage.getStoreData().addText(seller, "卖家",storage);
				  storage.getStoreData().addText(date, "创建时间",storage);
				  storage.getStoreData().addText(asin, "ASIN",storage);	
			
			}catch (SocketTimeoutException ee) {
				ee.printStackTrace();
				product.setAddThreshold();
				if(product.getThreshold()<3){
					storage.getInputUrlQueues().add(product);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		 }
		 
		 storage.getInputUrlQueues().clear();
		 
	}
	 public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(URLDecoder.decode("https://www.amazon.com/gp/slredirect/picassoRedirect.html/ref=pa_sp_atf_aps_sr_pg1_1?ie=UTF8&adId=A0111330LLURY22408EJ&url=%2FMofei-Nissan-Infiniti-Cover-Soft-Protector%2Fdp%2FB07QC1STSX%2Fref%3Dsr_1_1_sspa%3Fkeywords%3DTPU%2Bkey%2Bcover%26link_code%3Dqs%26qid%3D1566054068%26s%3Dgateway%26sourceid%3DMozilla-search%26sr%3D8-1-spons%26psc%3D1&qualifier=1566054068&id=3259827406709467&widgetName=sp_atf"));
	}
}
