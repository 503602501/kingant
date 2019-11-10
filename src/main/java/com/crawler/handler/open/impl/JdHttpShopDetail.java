package com.crawler.handler.open.impl;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.HttpUtil;
import com.crawler.util.ImageUtil;
import com.crawler.util.StringUtils;

public class JdHttpShopDetail implements IOpen {

	private static final Logger logger =  Logger.getLogger(JdHttpShopDetail.class);
	private List<Future<List<String>>> resultFuture   =new ArrayList<Future<List<String>>>();  
	
	private static Integer threads = 1;
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		String url = driverEntity.getWebDriver().getCurrentUrl();
		Iterator<Product> iterator = storage.getInputUrlQueues().iterator();

		String SHOP_URL ="https://mall.jd.com/index-%s.html";
		Integer to = storage.getEnv().getWidgets().getToFieldValue() ; // 起止id
		Integer from = storage.getEnv().getWidgets().getFromFieldValue(); //结束的id
		
		ConcurrentLinkedQueue <Product> urlQueues  = new ConcurrentLinkedQueue<>();  //用户输入的链接队列
		Product product = null;
		
		ConcurrentLinkedQueue <Product> inputs  = storage.getInputUrlQueues() ;  // 是否是链接呢？
		urlQueues.addAll(inputs);
		
		for (int i = from; i <= to ; i++) {
			product = new Product();
			product.setUrl( String.format(SHOP_URL, i) );
			urlQueues.add(product);
		}
	  
	/*	//多线程*********************************************************************************
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threads);
		Future<List<String>> future =null;
		for (int i = 0; i < threads; i++) {
			future=fixedThreadPool.submit(new TaskResult(urlQueues,storage));
			resultFuture.add(future);
		}
		 
		
		//遍历任务的结果 ,失败的链接重新启动
		long start = System.currentTimeMillis();
		logger.info("******************开始结果处理**********************");
        for (Future<List<String>> fs : resultFuture){
            while(!fs.isDone()) ; //Future返回如果没有完成，则一直循环等待，直到Future返回完成  
            
        }*/
		handlerData(storage, urlQueues);
		
	   /*************结束啦**************************/
	    storage.getInputUrlQueues().clear();
	}

	private void handlerData(Storage storage, 
			ConcurrentLinkedQueue<Product> urlQueues) throws Exception {
		
		String scoreAndCash = "";
		String content ="";
		String shopName="";
		
		Product product;
		while (!urlQueues.isEmpty() && !storage.getEnv().isStop()) {
			
			
			shopName="";
			scoreAndCash = "";
			content ="";
			
			
			product = urlQueues.poll() ;
			
			content =HttpUtil.getHtmlContent(product.getUrl());  //请求店铺内容
			if(StringUtils.isEmpty(content) && product.getThreshold()<4 ){
				product.setAddThreshold();
				urlQueues.add(product);
				Thread.sleep(500);
				continue;
			}
			
			if(StringUtils.isEmpty(content)){
				content="";
				scoreAndCash="请求店铺异常";
			}
			
			
			//获取评分或者 货到付款
			try {
				if(!StringUtils.isEmpty(content)){
					scoreAndCash =getShopScore(content);  
				}
			} catch (Exception e) {
				scoreAndCash ="处理异常"+e.getMessage();
			}
			
			if(content.indexOf("<title>")!=-1){
				shopName = content.substring(content.indexOf("<title>")+7,content.indexOf("</title>"));
				shopName=shopName.replace("- 京东", "").trim();
			}else{
				shopName = "";
			}
			
			
			//设置内容
			storage.getStoreData().addText(product.getUrl(), "店铺链接",storage);
			storage.getStoreData().addText(shopName, "店铺名称",storage);
			if(scoreAndCash.indexOf("无分")!=-1){
				storage.getStoreData().addText(scoreAndCash.split("\\|")[0], "评分",storage);
//				storage.getStoreData().addText(scoreAndCash.split("\\|")[1], "货到付款",storage);
				storage.getStoreData().addText("", "货到付款",storage);
			}else{
				storage.getStoreData().addText(scoreAndCash, "评分",storage);
				storage.getStoreData().addText("", "货到付款",storage);
			}
			
		}
//		return scoreAndCash;
	}
 
	private static String getShopScore(String content) throws Exception {
		String html = content;
		
		
		if(content.indexOf("view_search")==-1){
			return "店铺不存在";
		}
		
		content = content.substring(content.indexOf("view_search"));
		content = content.substring(content.indexOf("+"),content.indexOf("'-'"));
		String appId = content.replace("+", "").trim();
		String score="";
		//三次循环，检查是否有内容
		for (int i = 0; i < 3; i++) {
			 score =HttpUtil.getHtmlContent("https://mall.jd.com/view/getJshopHeader.html?appId="+appId);
			if(!StringUtils.isEmpty(score)){
				break;
			}
			if(i!=0){
				Thread.sleep(500);
			}
		}
		
		
		if(score.indexOf("京东自营")!=-1 ){
			return "京东自营";
		}
		
		if(  score.indexOf("服务明星店铺")!=-1 ){
			return "服务明星店铺";
		}
		
		if(  score.indexOf("大药房")!=-1 ){
			return "大药房";
		}
		
		if(  score.indexOf("海外专营店")!=-1 ){
			return "海外专营店";
		}
		String NULL = validateShopHasProduct(html);
		 if(NULL==null){
			 return "空店";
		 }
		//存在说明是有分
		if(score.contains(">高</span>") || score.contains(">中</span>") ||score.contains(">低</span>")){
			return "有分";
		}
		return "无分";
	/*	if(score.indexOf("与行业相比")==-1){
			String  huodaofukuan ="无需检测"; //validateShopHasProduct(html);
			
			if(huodaofukuan==null){ //true，有商品
				return "无任何商品";
			}else{
				return "无分|"+huodaofukuan;
			}
			
		}else{
			score = score.substring(score.indexOf("em")+2);
			score = score.substring(0,score.indexOf("em")-2);
			score = score.substring(score.indexOf(">")+1);
			return score ;
		}*/
	}

/*	public String getShopChat(String content) {
		
	}*/
	
	private static String  getShopStatus(String content) throws Exception {
		content = content.substring(content.lastIndexOf("sidebar.js"));
		content =content.substring(0,content.lastIndexOf("type")-2);
		String venderId = content.substring(content.lastIndexOf("id=")+3) ; //71306
		String callback =HttpUtil.getHtmlContent("https://mall.jd.com/view/getShopSignStatus.html?callback=callback&venderId="+venderId+"&_="+System.currentTimeMillis());
		
		callback = callback.replace("callback(", "");
		callback = callback.replace(");", "");
		JSONObject json =   JSONObject.parseObject(callback);
		if("true".equals(json.get("result")+"") && "3".equals(json.get("user_sign_on")+"")){  //0 在线，3留言
			return "留言卖家";
		}
		
		if("true".equals(json.get("result")+"") && "0".equals(json.get("user_sign_on")+"")){  //0 在线，3留言
			return "联系卖家";
		}
		return json.toString();
	}
	
	

	/*
	 * 验证店铺商品是否全部都下架了
	 * 是否货到付款
	 * 返回空表示没有商品
	 */
	private static String validateShopHasProduct(String content) throws Exception {
		
		String[] ps = content.split("jdprice=");
		String p1 =null;
		if(ps.length==0 || ps.length==1){
			
			if(content.indexOf("//item.jd.com/")==-1){
				return null;
			}
			
			//通过另一种方式看是否能够取出商品skuid
			String s = content.substring( content.indexOf("//item.jd.com/") );
			p1= s.substring(0,s.indexOf(".html")).replace("//item.jd.com/", "");
			
		}else{
			 p1 = ps[1].substring(1, ps[1].indexOf("></span>")-2);
		}
		
		if(p1.indexOf(">")!=-1){
			p1=p1.replace("\"", "").replace("\\", "");
			p1 = p1.substring(0,p1.indexOf(">"));
		}
		
		for (int i = 0; i < 3; i++) {
			content = HttpUtil.getHtmlContent("https://item.jd.com/"+p1+".html","GBK");
			if(!StringUtils.isEmpty(content)){
				break;
			}
			if(i!=0){
				Thread.sleep(500);
			}
			
		}
//		System.out.println(content);
		
		//返回的信息调到了首页，说明这个店铺的链接有问题，直接作为没有商品的店铺
		if(content==null || content.indexOf("J_service")!=-1 || content.indexOf("error-img")!=-1  ){
			return null;
		}

		//存在有的商品
		if(content.indexOf("该商品已下柜")==-1){
			
			//判断是否货到付款
			return cashonDelivery(content);
		}
		
		
		if(ps.length>2){
			
			String p2 = null;
			try {
				p2 = ps[2].substring(1, ps[2].indexOf("></span>")-2);
			} catch (Exception e) {
				System.out.println(p2);
				e.printStackTrace();
			}
			
			for (int i = 0; i < 3; i++) {
				content = HttpUtil.getHtmlContent("http://item.jd.com/"+p2+".html","GBK");
				if(!StringUtils.isEmpty(content)){
					break;
				}
				
				if(i!=0){
					Thread.sleep(500);
				}
			}
			
			//返回的信息调到了首页，说明这个店铺的链接有问题，直接作为没有商品的店铺
			if(content.indexOf("J_service")!=-1 || content.indexOf("error-img")!=-1  ){
				return null;
			}
			
			
			if(content.indexOf("该商品已下柜")==-1){
				
				return cashonDelivery(content);
			}
		}
	 
		return null; //第一个和第二个商品都是下架，那这个店铺就没有商品了
	}
	
	/*
	 * 货到付款
	 */
	public static String cashonDelivery(String content ) throws Exception{
		
		String skuId= content.substring(content.indexOf("skuid")+6,content.indexOf("skuMarkJson"));
		skuId = skuId.replace(",","").trim();
		
		String cat= content.substring(content.indexOf("cat:")+6,content.indexOf("forceAdUpdate"));
		cat = cat.substring(0,cat.lastIndexOf("]"));
		
		String venderId=content.substring(content.indexOf("venderId:")+9,content.indexOf("shopId:"));
		venderId = venderId.replace(",", "").trim();
		
		String area= "1_2800_55812_0";  //固定地址，从网页分析
		
		String url = "https://c0.3.cn/stock?skuId="+skuId+"&cat="+cat+"&venderId="+venderId+"&area="+area+"&buyNum=1&extraParam="+URLEncoder.encode("{\"originid\":\"\"}", "GBK");
		
		String result=null;
		for (int i = 0; i < 3; i++) {
			 result = HttpUtil.getHtmlContent(url,"GBK");
			 if(!StringUtils.isEmpty(result)){
				 break;
			 }
			 
			 if(i!=0){
				 Thread.sleep(500);
			 }
			 
		}
		
		
		
		if(result.indexOf("货到付款")!=-1){
			return "货到付款";
		}
		
		return "未检出货到付款";
	}
	
	public class TaskResult implements Callable<List<String>>{

		private ConcurrentLinkedQueue <Product> quenues=null;
		private  Storage storage =null;
		public TaskResult(ConcurrentLinkedQueue <Product> quenues,Storage storage) {
			this.quenues = quenues;
			this.storage = storage;
		}
		
		@Override
		public List<String> call() throws Exception {
			
			handlerData(storage, quenues);
			return null;
			 
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
//		String content = HttpUtil.getHtmlContent("https://mall.jd.com/index-20066.html");
		String content = HttpUtil.getHtmlContent("https://mall.jd.com/index-20001.html");
		String s = getShopScore(content);
		String has =validateShopHasProduct(content);
		System.out.println(has);
		System.out.println(s);
		
//		String s = cashonDelivery(content);
//		System.out.println("a|b".split("\\|")[0]);
//		System.out.println(content);
//		validateShopHasProduct(content);
		
	}
}
