package com.crawler.handler.text.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.crawler.entity.Storage;
import com.crawler.entity.TextUnit;
import com.crawler.handler.open.impl.TianmaoListUrl;
import com.crawler.handler.text.IText;
import com.crawler.util.FilterUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.StringUtils;

public class ASINMarkeText implements IText {
	
	private static final Logger logger =  Logger.getLogger(TianmaoListUrl.class);
	private static int count = 0;
	
	
	@Override
	public String getText(String text,WebElement web,WebDriver webDriver,TextUnit textUnit,Storage storage) {
		
		String string= "";
		try {
			
			List list = storage.getLastRow();
			String url = ""+list.get(1);
			HashMap map = new HashMap<String, String>();
			map.put("keywords", HttpUtil.getQueryString(url, "keywords"));
			map.put("qid", HttpUtil.getQueryString(url, "qid"));
			map.put("rnid", HttpUtil.getQueryString(url, "rnid"));
			
			String s = HttpUtil.getHtmlContent(url,map,"",count!=0 && count%10==0 ? true : false);
//			logger.info("返回的请求信息:"+s);
			//反采集，禁止访问
			if( s.indexOf("To discuss automated access to Amazon data")!=-1){  
				s = HttpUtil.getHtmlContent(url,map,"",true);
				if(s.indexOf("To discuss automated access to Amazon data")!=-1){
					logger.info("**********************变化后还是反采集");
//					logger.info(s);
					logger.info("**********************变化后还是反采集"+s);
					s= "反采集";
					return s ;
				}
			}
			
			/*if(s.indexOf("Derzeit nicht verfügbar")==-1){  //居然不是失效的商品
				return "有效商品";
			}*/
			
			count+=1;
			
			String asin = s.substring(s.indexOf("ASIN</td><td"));
			asin = asin.replace("ASIN</td><td","");
			asin = asin.substring(0, asin.indexOf("</tr>"));
			asin = asin.substring(asin.indexOf(">")+1,asin.indexOf("<"));
			string =asin ; 
			System.out.println(asin);
			
			//品牌可能为空
			String  marke = getMarke(s);
			string =string+","+marke;
			
			//获取排名
			String rank = getRank(s);
			string =string+","+rank;
			
			
			/* String review= FilterUtil.cutString( s, "total-review-count", "<");
			 review = review.replace("\"", "").replace("=", "").replace(">", "");
			 string =string+","+review;*/
			if(s.indexOf("Kundenrezensionen")!=-1){
				String review= s.substring(0, s.indexOf("Kundenrezensionen"));
				review = review.substring(review.lastIndexOf(">")+1);
				review = review.replace("Kundenrezensionen", "");
				string =string+","+review;
			}
			
			//amazon
			if(s.indexOf("id=\"merchant-info\"")!=-1){
				String review= s.substring(s.indexOf("id=\"merchant-info\""));
				review = review.substring(0,review.indexOf("script ")+1);
				if(review.indexOf("Amazon")!=-1){
					string =string+","+"Amazon";
				}else{
					string =string+","+"";
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return string ;
		
	}

	private String  getRank(String s) {
		String rang = "";
		String paiming="";
		try {
			if(s.indexOf("Amazon Bestseller-Rang")!=-1){
				rang = s.substring(s.indexOf("Amazon Bestseller-Rang"));
				rang = rang.substring(0, rang.indexOf("</tr>"));
				paiming =rang.substring(rang.indexOf("Nr."),rang.indexOf("("));
				rang = rang.substring(rang.indexOf("class=\"zg_hrsr_rank\"") );
				paiming+="|"+rang.substring(rang.indexOf("Nr."), rang.indexOf("</span>"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		paiming = paiming.replace(",", "");
		return paiming ;
	}

	private String getMarke(String s) {
		String marke ="";
		try {
			if(s.indexOf("Technische Details")!=-1){
				marke = s.substring(s.indexOf("Technische Details"));
				marke = marke.substring(marke.indexOf("Marke"), marke.indexOf("</tr>"));
				marke = marke.substring(marke.indexOf("value")+7,marke.lastIndexOf("</td>"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			marke="";
		}
		
		if(StringUtils.isEmpty(marke)){
			String von = s.substring(s.indexOf("id=\"bylineInfo\""));
			von = von.substring(0, von.indexOf("</a>"));
			von = von.substring(von.lastIndexOf(">")+1);
			marke = von;
		}
		
		return marke;
	}
}
