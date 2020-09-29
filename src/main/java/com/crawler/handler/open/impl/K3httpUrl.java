package com.crawler.handler.open.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.Expire;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.DriverUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.JsonUtil;
import com.crawler.util.StringUtils;

public class K3httpUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(K3httpUrl.class);
	
	
	/**
	 * 
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		 
		
		
		
		Expire e = new Expire(openUnit.getExpire());
		if(e.isExpire()){
			storage.getEnv().showLogArea("\n运行异常\n");
			return ;
		}
		
		
			String content = "";
			Iterator<Product> iterator = storage.getInputUrlQueues().iterator();
			while ( iterator.hasNext()) {
				if(storage.getEnv().isStop()){
					break ;
				}
				Product p = iterator.next();
				String url = p.getUrl().replace("=#0#0", "") ;
				content = HttpUtil.getHtmlContent(url);
				if( StringUtils.isEmpty(content)  || content.indexOf("对不起")!=-1){
					storage.addText("无效链接", p.getUrl(), storage);
					continue ;
				}
				
				Document doc = Jsoup.parse(content); // 使用jsoup 进行语言转换
				String userId = doc.select("input#data-show").attr("data-user_id");// 商品标题 #使用css方式
				String dataHash = doc.select("input#data-show").attr("data-hash");// 商品标题 #使用css方式
				String dataPproductId = doc.select("input#data-show").attr("data-product_id");// 商品标题 #使用css方式
				
//			 	System.out.println(dataHash);
			 	
				url =  String.format("http://www.k3.cn/ajax/product/get_status_category/%s/%s/%s?_=%s",dataPproductId, userId,dataHash,System.currentTimeMillis());
				content= HttpUtil.getHtmlContent(url);
				String state = JsonUtil.getJSONValue("data->product->state", content) ;
				System.out.println("state:"+content);
				System.out.println( "0".equals(state) ? "已下架": "weilai");

				//已经下架的状态
				if("0".equals(state)){
					storage.addText("无效链接", p.getUrl(), storage);
					continue ;
				}
				
//				storage.addText("有效链接", p.getUrl(), storage);
				
				Thread.sleep(5000) ;
				
			}
		 
	 
		 
	}
	
	/*
	 * 获取商品的数量
	 */
	private static String getProductNum(String cookies) throws Exception {
		String s =  HttpUtil.getHtmlContent("https://soffice.11st.co.kr/product/SellProductAction.tmall?method=getSellProductList","euc-kr",cookies);
		System.out.println(s);
		s = s.substring(s.indexOf("판매중 : "));
		s = s.substring(s.indexOf(">")+1,s.indexOf("</em>"));
		return s; 
	}
	
	
/**
 * 获取留言
 * @param cookies
 * @return
 * @throws Exception
 */
	private static String getQuestionCnt(String cookies) throws Exception {
		String ordergood = HttpUtil.getHtmlContent("http://soffice.11st.co.kr/marketing/SellerMenuAction.tmall?method=getEmerMainAlertStatAjax", cookies);
		ordergood = ordergood.replace("(", "");
		ordergood = ordergood.replace(")", "");
		return JsonUtil.getJSONValue("emerStat00", ordergood);
	}
	
	
	/**
	 * 获取订单数量
	 * @param url
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	private static String getOrderGood( String cookies)
			throws Exception {
		String ordergood = HttpUtil.getHtmlContent("http://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?isAbrdSellerYn=&isItalyAgencyYn=&listType=orderingConfirm&method=getUnapprovedOrderTotal", cookies);

		
//		String ordergood= HttpUtil.getOrderGood(cookies);
		System.out.println(ordergood);
		/*String ordergood="**";
		
		for (int i = 0; i < 3; i++) {
			try {
				String url = "https://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?method=getUnapprovedOrderTotal";
				HashMap map = new HashMap<>();
				map.put("listType", "orderingConfirm");
				map.put("isAbrdSellerYn", "");
				map.put("isItalyAgencyYn", "");
				String s =  HttpUtil.postHtmlContent(url,map,cookies);
				System.out.println(s);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				Thread.sleep(1000);
			}
		}*/
			
		ordergood = JsonUtil.getJSONValue("unapprovedOrderTotal->order_good_202", ordergood);
		return ordergood;
	}
	

}
