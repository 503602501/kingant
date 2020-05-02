package com.crawler.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.handler.text.impl.JdEntity;
import com.google.gson.JsonObject;
import com.us.codecraft.Xsoup;




public class HttpUtil {

	private static int socketTimeout = 10000;  //读超时时间（等待数据时间）
	private static int connectTimeout = 10000; //连接超时时间  
	private static int connectionRequestTimeout = 10000;  //从连接池中获取连接的等待时间
	private static int maxConnTotal = 300;   //最大不要超过1000  
	private static int maxConnPerRoute = 200;//路由的实际的单个连接池大小，如tps定为50，那就配置50  
	private static String UA="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
	private static final Logger logger =  Logger.getLogger(HttpUtil.class);
	private static 	CloseableHttpClient httpclient ;
	private static SSLContext sslContext ;
	static{
		
	 	//*******第一种https验证******//*
/*		 try {
			 sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {

				@Override
				public boolean isTrusted( java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
					return true;
				}
			 
			   }).build();
//			 sslContext = SSLContext.getInstance("TLS");
			 sslContext = SSLContext.getInstance("SSL");
		} catch (Exception e1) {
			e1.printStackTrace();
		} */
			sslContext = ImageUtil.createIgnoreVerifySSL();
		
			  
//		HttpHost proxy=new HttpHost("163.204.242.74", 9999);
			  
		RequestConfig config = RequestConfig.custom() //.setProxy(proxy)
		         .setSocketTimeout(socketTimeout)  
		         .setConnectTimeout(connectTimeout)  
		         .setConnectionRequestTimeout(connectionRequestTimeout).build();  
		httpclient = HttpClients.custom().setDefaultRequestConfig(config)  
		         .setMaxConnTotal(maxConnTotal)  
		         .setSslcontext(sslContext)
		         .setMaxConnPerRoute(maxConnPerRoute).build();  
//		httpclient.getConnectionManager().shutdown(); 未做关闭不知道是否有问题
	}
	
	
	public static String getHtmlContent(String httpPath) throws Exception {
		 return getHtmlContent( httpPath,"UTF-8","");
	}
	
	public static String getHtmlContent(String httpPath,String cookies) throws Exception {
		return getHtmlContent( httpPath,"UTF-8",cookies);
	}
	
	/**
	 * 描述:获取html的内容片段
	 * 
	 * @param httpPath
	 *            :请求路径
	 * @return
	 * @throws Exception
	 */
	public static String getHtmlContent(String httpPath,String charset,String cookies) throws Exception {

		
		if(StringUtils.isEmpty(httpPath)){
			return "";
		}
		SslUtils.ignoreSsl();
		
		/*URIBuilder uriBuilder = new URIBuilder(httpPath);
		List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("cUserIds", "12506545");
        list.add(param1);
        uriBuilder.setParameters(list);*/
        /*URIBuilder uriBuilder = new URIBuilder(httpPath);
		List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("skuId", "13308189032");
        BasicNameValuePair param2 = new BasicNameValuePair("cat", "5025,5026,13674");
        BasicNameValuePair param3 = new BasicNameValuePair("venderId", "214561");

        BasicNameValuePair param4 = new BasicNameValuePair("area", "1_2800_55812_1");
        BasicNameValuePair param5 = new BasicNameValuePair("buyNum", "1");
        BasicNameValuePair param6 = new BasicNameValuePair("extraParam", "{\"originid\":'1'}");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);
//        list.add(param5);
        list.add(param6);
        uriBuilder.setParameters(list);
 */
        // 根据带参数的URI对象构建GET请求对象
//        HttpGet httpGet = new HttpGet(uriBuilder.build()); 
        
		// 一般爬虫请求都用Get，Get请求在HTTP请求协议里代表安全的查看:这个请求对象里可以添加http的请求头等
		HttpGet httpGet = new HttpGet(httpPath.trim());

		// 设置Get请求头的 User-Agent (模拟代理浏览器信息)
		httpGet.setHeader( "User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		httpGet.setHeader("Cookie",cookies);
//		httpGet.setHeader("Host","www.youlu.net");
//		httpGet.setHeader( "referer", "https://www.youlu.net/247689");
//		httpGet.setHeader("Version-Code","1");
		
	/*	httpGet.setHeader("sdk-version","1");
		httpGet.setHeader("X-SS-REQ-TICKET","1567654158335");
		httpGet.setHeader("x-tt-trace-id","00-cbf72dcec46295c3205ea79b96737eca-cbf72dcec46295c3-01");
		httpGet.setHeader("User-Agent","com.ss.android.ugc.aweme/790 (Linux; U; Android 5.1.1; zh_CN; SM-G955F; Build/JLS36C; Cronet/58.0.2991.0)");
		httpGet.setHeader("X-Gorgon","0300dc7640013be8f89b4eebebd3d615c4f4f2dcfe58d7402f50");
		httpGet.setHeader("X-Khronos","1567654158");
		*/
		
		
		// 用浏览器模拟对象httpClient，发送一个Get请求:可以通过这个响应对象获得很多http的响应信息
		InputStream is = null;
		FileOutputStream outputStream = null;
		CloseableHttpResponse respond = null;
		try {
			respond = httpclient.execute(httpGet);
			respond.setHeader("Connection", "close");
			if (HttpStatus.SC_OK != respond.getStatusLine().getStatusCode()) {
				httpGet.abort();
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
			// 获取返回的网页实体
			HttpEntity entity = respond.getEntity();
			if (entity != null) {
				is = entity.getContent();
				/*
				
				File file = new File("D:\\A\\aa.jpg");
				outputStream = new FileOutputStream(file);
		        byte b[] = new byte[5*1024];
		        int j = 0;
		  
		        while( (j = is.read(b)) != -1 )
		        {
		            outputStream.write(b,0,j);
		        }
		        outputStream.flush();
		        
		        return "";*/
				return IOUtils.toString(is,Charset.forName(charset));
			} else {
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("请求异常：" + httpPath + "|" + e.getMessage(), e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException io) {
				logger.error("关闭异常：" + httpPath + "," + io.getMessage(), io);
			}

			try {
				if (respond != null) {
					respond.close();
				}
			} catch (Exception e) {
				logger.error("关闭异常：" + httpPath + "," + e.getMessage(), e);
			}

			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("关闭异常：" + httpPath + "," + e.getMessage(), e);
			}
		}
		return null;
	}
	
	/**
	 * 无需cookies
	 * @param httpPath
	 * @param urlParameters
	 * @return
	 * @throws Exception
	 */
	public static String postHtmlContent(String httpPath,List<NameValuePair> urlParameters) throws Exception {
		 return postHtmlContent(  httpPath,   urlParameters,  "") ;
	}
	
	/**
	 * 
	 * @param httpPath
	 * @param params  需要提交的参数
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public static String postHtmlContent(String httpPath,HashMap<String,String> params ,String cookies) throws Exception {
		
		List<NameValuePair> list = new LinkedList<>();
        
		for(Map.Entry<String, String> entry : params.entrySet()){
		    String mapKey = entry.getKey();
		    String mapValue = entry.getValue();
		    BasicNameValuePair param = new BasicNameValuePair(mapKey, mapValue);
	        list.add(param);
		}
		
		return postHtmlContent(httpPath, list, cookies);
		
	}
	
	/**
	 * 
	 * @param httpPath
	 * @param params  需要提交的参数
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	
	public static String getHtmlContent(String httpPath,HashMap<String,String> params ,String refer,boolean changeUa) throws Exception {
		
		List<NameValuePair> list = new LinkedList<>();
		
		for(Map.Entry<String, String> entry : params.entrySet()){
			String mapKey = entry.getKey();
			String mapValue = entry.getValue();
			BasicNameValuePair param = new BasicNameValuePair(mapKey, mapValue);
			list.add(param);
		}
		
		return getHtmlContent(httpPath, list, refer,changeUa);
		
	}
	/**
	 * 描述:获取html的内容片段
	 * 
	 * @param httpPath
	 *            :请求路径
	 * @return
	 * @throws Exception
	 */
	public static String postHtmlContent(String httpPath,List<NameValuePair> urlParameters,String cookies) throws Exception {
		
		// 一般爬虫请求都用Get，Get请求在HTTP请求协议里代表安全的查看:这个请求对象里可以添加http的请求头等
		HttpPost httpPost= new HttpPost(httpPath.trim());
		
		// 设置Get请求头的 User-Agent (模拟代理浏览器信息)
		httpPost.setHeader( "User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		httpPost.setHeader("Cookie",cookies);
		
//		httpPost.setHeader("Referer", "http://www.dataoke.com/qlist/?px=zh&tm_jpmj=tm&xlqj=10000&page=1");
		// 用浏览器模拟对象httpClient，发送一个Get请求:可以通过这个响应对象获得很多http的响应信息
		InputStream is = null;
		FileOutputStream outputStream = null;
		CloseableHttpResponse respond = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(urlParameters,Consts.UTF_8));
			respond = httpclient.execute(httpPost);
			respond.setHeader("Connection", "close");
			if (HttpStatus.SC_OK != respond.getStatusLine().getStatusCode()) {
				httpPost.abort();
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
			// 获取返回的网页实体
			HttpEntity entity = respond.getEntity();
			if (entity != null) {
				is = entity.getContent();
				return IOUtils.toString(is,Charset.forName("UTF-8"));
//				return IOUtils.toString(is);
			} else {
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
		} catch (Exception e) {
			logger.error("请求异常：" + httpPath + "," + e.getMessage(), e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException io) {
				logger.error("关闭异常：" + httpPath + "," + io.getMessage(), io);
			}
			
			try {
				if (respond != null) {
					respond.close();
				}
			} catch (Exception e) {
				logger.error("关闭异常：" + httpPath + "," + e.getMessage(), e);
			}
			
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("关闭异常：" + httpPath + "," + e.getMessage(), e);
			}
		}
		return null;
	}
	
	/**
	 * 描述:获取html的内容片段
	 * 
	 * @param httpPath
	 *            :请求路径
	 * @return
	 * @throws Exception
	 */
	public static String getHtmlContent(String httpPath,List<NameValuePair> urlParameters,String refer) throws Exception {
		return getHtmlContent( httpPath, urlParameters, refer,false);
	}
	
	/**
	 *改变UA 
	 */
	private static void changeUa(){
		UA = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537."+(int)(Math.random()*1000) ;
	}
	
	/**
	 * 描述:获取html的内容片段
	 * 
	 * @param httpPath
	 *            :请求路径
	 * @return
	 * @throws Exception
	 */
	public static String getHtmlContent(String httpPath,List<NameValuePair> urlParameters,String refer,boolean changeUA) throws Exception {
		
		// 一般爬虫请求都用Get，Get请求在HTTP请求协议里代表安全的查看:这个请求对象里可以添加http的请求头等
		
		URIBuilder uriBuilder = new URIBuilder(httpPath.trim());
		uriBuilder.setParameters(urlParameters);
		HttpGet httpGet= new HttpGet(uriBuilder.build());
		if(!StringUtils.isEmpty(refer)){
			httpGet.addHeader(new BasicHeader("referer", refer));
		}
		
		// 设置Get请求头的 User-Agent (模拟代理浏览器信息)
		if(changeUA){
			changeUa();
		}
		
		httpGet.setHeader( "User-Agent", HttpUtil.UA); //+new Random(1).nextInt(1000));
//		httpGet.setHeader( "User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537."); //+new Random(1).nextInt(1000));
//		httpGet.setHeader("Cookie", "x-wl-uid=1o36XJZ+NsTtfHjt3KyJovicuOcTgYm6lccMIzcZB898cUvrYz/4c7Dx31z2q1UplbuimZ5kbOzY=; session-id=257-6158185-0705766; session-id-time=2082787201l; ubid-acbde=258-1570618-6095466; session-token=/DPNfIxCHJjcz85Xar3wmHIMKTobQUpYk33SOjtbh51RHqGlfThSnspT88q1XyanGZiITG2TteAdYdWkMoczzENeEdTuYwbHYMugVlJds32xYuNBfd5TJO4Kw+oVhDPDpchrOuu4l5iLSTT40ffA9Y5+QvPXPeSfzZTyeZEklfw1EOWEPWctDC3/A7ApKMH+; i18n-prefs=EUR; csm-hit=tb:s-DJ8WFTQDT6T96VKY45JB|1562203001073&t:1562203002110&adb:adblk_no");
		// 用浏览器模拟对象httpClient，发送一个Get请求:可以通过这个响应对象获得很多http的响应信息
		InputStream is = null;
		FileOutputStream outputStream = null;
		CloseableHttpResponse respond = null;
		try {
			
			respond = httpclient.execute(httpGet);
			respond.setHeader("Connection", "close");
			if (HttpStatus.SC_OK != respond.getStatusLine().getStatusCode()) {
				httpGet.abort();
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
			// 获取返回的网页实体
			HttpEntity entity = respond.getEntity();
			if (entity != null) {
				is = entity.getContent();
				return IOUtils.toString(is);
			} else {
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
		} catch (Exception e) {
			logger.error("请求异常：" + httpPath + "," + e.getMessage(), e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException io) {
				logger.error("关闭异常：" + httpPath + "," + io.getMessage(), io);
			}
			
			try {
				if (respond != null) {
					respond.close();
				}
			} catch (Exception e) {
				logger.error("关闭异常：" + httpPath + "," + e.getMessage(), e);
			}
			
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("关闭异常：" + httpPath + "," + e.getMessage(), e);
			}
		}
		return null;
	}
	
	
	public static  String getTaoBaoProductUrl(String id, String sign){
		JsonObject json = new JsonObject();  //586451114773
		json.addProperty ("id", id);
		json.addProperty("itemNumId", "596549543120");
		json.addProperty("exParams", String.format("{\"id\":\"%s\"}","596549543120"));
		return "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?appKey=12574478&sign="+sign+"&data="+URLEncoder.encode(json.toString());
	}
	
	public static  String getTaoBaoProductDescUrl(String id, String sign){
//		JsonObject json = new JsonObject();  //586451114773
//		json.addProperty("data", );
		
		String data = URLEncoder.encode(String.format("{\"id\":\"%s\"}",id)) ; //{"id":"596549543120","type":"1"};
		
		String url = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdesc/6.0/?appKey=12574478&sign="+sign+"&api=mtop.taobao.detail.getdesc&data="+data;
		return url;
	}
	
	public static String removeSpecilChar(String str){
		String result = "";
		if(null != str){
		Pattern pat = Pattern.compile("\\n|\r|\t");
		Matcher mat = pat.matcher(str);
		result = mat.replaceAll("");
		}
		return result;
	}
	
	
	/**
	 * @param args
     * @throws Exception 
	 */ 
	public static void main(String[] args) throws Exception {
		
	 	String contents= HttpUtil.getHtmlContent( "https://www.carid.com/weathertech/digitalfit-molded-floor-liners-1st-row-tan-mpn-458391.html");
	 	System.out.println(contents);
	 	if(contents!=null){
	 		return ;
	 	}
	/*	String s = FilterUtil.cutString(contents, "bookId=", "target");
		s = s.replace("\"", "").trim();
		String url = String.format("https://www.youlu.net/info3/shp.aspx?bookId=%s&rowCount=0&pageIndex=1", s) ;
		System.out.println(url);
//		url = "https://www.youlu.net/info3/shp.aspx?bookId=247689&rowCount=0&pageIndex=1";
//https://www.youlu.net/info3/shp.aspx?bookId=247689&rowCount=0&pageIndex=1 
		String data = HttpUtil.getHtmlContent(url,"UserProvinceCode=440000; UserProvinceName=%25E5%25B9%25BF%25E4%25B8%259C%25E7%259C%2581; CokVisitorId=20200211215324657_223.73.114.37; CokBrowserId=26a6cfff-0ff4-41ec-b2de-21d373e9d376; CokMemberNickname=; YLFirstSign=1; CokShpBuyer=20200211215325830_223.73.114.37; tencentSig=6495048704; CokUserArea=%7b%22ip%22%3a%22223.73.114.34%22%2c%22pro%22%3a%22%e5%b9%bf%e4%b8%9c%e7%9c%81%22%2c%22proCode%22%3a%22440000%22%2c%22city%22%3a%22%e5%b9%bf%e5%b7%9e%e5%b8%82%22%2c%22cityCode%22%3a%22440100%22%2c%22region%22%3a%22%22%2c%22regionCode%22%3a%220%22%2c%22addr%22%3a%22%e5%b9%bf%e4%b8%9c%e7%9c%81%e5%b9%bf%e5%b7%9e%e5%b8%82+%e7%a7%bb%e9%80%9a%22%2c%22regionNames%22%3a%22%22%2c%22err%22%3a%22%22%7d; Qs_lvt_103052=1581429243%2C1581476405; ASP.NET_SessionId=ofqqzhflsxjbu4s0d24snnrn; CokRecentViewBookList=9787806026007%3b0%3b%e6%ba%aa%e5%8f%a3%e5%a2%a8%e5%ae%9d%2f%e6%ba%aa%e5%8f%a3%e6%97%85%e6%b8%b8%e4%b8%9b%e4%b9%a6%3b%e7%8e%8b%e5%a4%a9%e8%8b%8d%3b247689%7c9787550015432%3b0%3b%e8%90%a4%e7%81%ab%e8%99%ab%e5%b0%8f%e5%b7%b7%3b%e5%85%8b%e8%8e%89%e4%b8%9d%e6%b1%80.%e6%b1%89%e5%a8%9c%3b3705013; Qs_pv_103052=358011691043138700%2C3081709735049110000%2C2525753221300187600%2C2742261762381516300%2C1208289355533555200; Hm_lvt_e9008369bc2cfc746263d552935f0791=1581431399,1581476408; Hm_lpvt_e9008369bc2cfc746263d552935f0791=1581482109; Hm_lvt_6ecaa2d1d7c184a6b549be11a906cbde=1581429244,1581476407; Hm_lpvt_6ecaa2d1d7c184a6b549be11a906cbde=1581482109; Hm_lvt_a126719936bc8fc47fd2c2d2ca323ae7=1581429244,1581476407; Hm_lpvt_a126719936bc8fc47fd2c2d2ca323ae7=1581482109; Hm_lvt_5bb1bd5b2b32959372056d2505ec36e1=1581429247,1581476409; Hm_lpvt_5bb1bd5b2b32959372056d2505ec36e1=1581482109; _qddaz=QD.n18g70.qf9rbd.k6hy54dv; mediav=%7B%22eid%22%3A%2275240%22%2C%22ep%22%3A%22%22%2C%22vid%22%3A%227Wn%2Fx9DSh%3F%3AU%234K)x9'%5B%22%2C%22ctn%22%3A%22%22%7D; ShopSortType=price; _qdda=3-1.1; _qddab=3-cyxjqu.k6iz96fi");
//		System.out.println("价格后的排序:"+data);
		
		String ss = StringEscapeUtils.unescapeHtml4(data) ;
		ss = ss.substring(12,ss.length()-2) ;
		
		Document doc = Jsoup.parse(ss);
		
		Elements eles = Xsoup.select(doc, "//body/li").getElements();
		for (int i = 1; i <= eles.size(); i++) {
			 String shopName = Xsoup.select(doc, "//body/li["+i+"]/div/div[@class='yl-seller-name']/a/text()").get();
			 String price = Xsoup.select(doc, "//body/li["+i+"]/div/div[@class='price']/span/em/text()").get();
			 String num = Xsoup.select(doc, "//body/li["+i+"]/div/span[@class='yl-seller-store']/text()").get();
			 System.out.println(shopName+"|"+price+"|"+num);
			 
		}
		*/
		
		
		/*
		String contents= HttpUtil.getHtmlContent("https://www.toutiao.com/i6737980139885298187");
		contents = contents.substring(contents.indexOf("BASE_DATA.galleryInfo ="));
		contents = contents.replace("BASE_DATA.galleryInfo =", "");
		contents = contents.substring(0, contents.indexOf("</script>"));
		String title = contents.substring(contents.indexOf("title:")+6, contents.indexOf(",")).replace("'", "");
		String gallery = contents.substring(contents.indexOf("JSON.parse(\"")+12,contents.indexOf("siblingList"));
		gallery = gallery.trim() ;
		gallery = gallery.substring(0,gallery.length()-3);
		gallery = StringEscapeUtils.unescapeJava(gallery);
		List<String> imgages = JsonUtil.getJSONListValue("sub_images->url", gallery);
		for (String string : imgages) {
			System.out.println(string);
		}
		System.out.println(gallery);
		System.out.println(title);
		System.out.println(contents);
		*/
		/*String contents= HttpUtil.getHtmlContent("https://www.houzz.com/hznb/photos/open-tread-staircase-traditional-staircase-seattle-phvw-vp~6081044");
		contents = contents.substring(contents.lastIndexOf("view-photo-visitor__image-area"));
		contents = contents.substring(contents.indexOf("src="),contents.indexOf("jpg")+3);
		contents = contents.replace("src=\"", "");
		System.out.println(contents );*/
		
		/*String content = HttpUtil.getHtmlContent("https://www.amazon.com/Pack-Belt-Keepers-Keeper-Metal/dp/B01MA54MRN/ref=zg_bs_318298011_7/140-0832142-1457758?_encoding=UTF8&psc=1&refRID=RYBJERRZBQWX4DZZBM4B" );
		content = removeSpecilChar(content);
		String s= FilterUtil.cutString( content, "Date First Available", "</td>");
		System.out.println(StringUtils.delHtml(s));
		
		  s= FilterUtil.cutString( content, "id=\"ASIN\"", ">");
		  s = s.replace("\"", "").replace("=", "");
		  s = s.substring(s.indexOf("value"));
		  System.out.println(s);
		  
		  s= FilterUtil.cutString( content, "total-review-count", "<");
		  s = s.replace("\"", "").replace("=", "").replace(">", "");
		  System.out.println(s);
		  
		  s= FilterUtil.cutString( content, "class=\"a-icon-alt\">", "<");
		  s = s.replace("\"", "").replace("=", "").replace(">", "");
		  System.out.println(s);
		  
	 	  s= FilterUtil.cutString( content, "a-color-secondary a-size-base prodDetSectionEntry", "</td>");
	 	  s = s.substring(s.indexOf("a-size-base"));
		  s = s.replace("\"", "").replace("=", "").replace(">", "").trim();
//		  System.out.println(StringUtils.delHtml( s) );
		  
		  String band = content.substring(content.indexOf("id=\"bylineInfo\""));
		  band = band.substring(0,band.indexOf("</div>"));
//		  s =   FilterUtil.cutString( content, , "</div>"); 
		  System.out.println("品牌："+StringUtils.delHtml("<"+band));
		  
		  s= FilterUtil.cutString( content, "a-size-medium a-color-price priceBlockBuyingPriceString", "<");
		  s = s.replace("\"", "").replace("=", "").replace(">", "");
		  System.out.println(s);
		  
//		  s= FilterUtil.cutString( content, "This item: </span><span>", "</span>");
		  s= FilterUtil.cutString( content, "<title>", "</title>");
		  s = s.replace("Amazon.com:", "").replace(": Gateway", "");
		  System.out.println(s);
		   */
		
		
//				"JSESSIONID=GZ00B046A9318EE04C69B9F7ED6384589572mrchstmGZ00; cna=rCV1EXHdDH8CAd9JOszJS5U8; NEW_ALIPAY_TIP=1; rtk=0LTlm7cUpkvgFfDJXLtZjtJCErNUQB4h2CZTNhgJGzYFQBIcNlP; spanner=u1AhqtqoQLQGwxXw1SxEIeemvNgqN79e4EJoL7C0n0A="; // "Cookie: thw=cn; UM_distinctid=16b66530a56274-0bf8f808cc30b3-3b3e5906-1aeaa0-16b66530a5748b; hng=CN%7Czh-CN%7CCNY%7C156; enc=BkxStKiL1FmvKz1qbPutChjuVSWbJwJSvG4rXQdLCAYVuk2C322LgX%2Fh7KG6tip6vKQKYluh8RbB0RyIIn8qRA%3D%3D; miid=516904341261201794; ali_ab=223.73.114.216.1560788325669.3; t=dd377dc99ed4e71e7fbb315a5cf1835b; _cc_=UtASsssmfA%3D%3D; tg=0; mt=ci%3D-1_0; _tb_token_=8DgLJ4AgAdziFdnZBs1j; cookie2=17eab5b8e678ba149dad6edeade7a1f0; x=4229698545; uc3=id2=&nk2=&lg2=; skt=d79c0c14cfedd316; sn=tb763755032%3A%E6%B5%8B%E8%AF%95; unb=2203055804648; tracknick=; csg=47852441; _m_h5_tk=eed2d4580cbd770b6663ecb65a80def1_1562942940121; _m_h5_tk_enc=f85015fcaf17233224286abc280a00d3; uc1=cookie14=UoTaGqSl9t97kQ%3D%3D&lng=zh_CN; v=0; l=cBLbWLr7vZs4G_BLBOfaquI8az7t5IRbzPVzw4_MNICP9OCyRlP1WZnOXhL2CnGVL609R3R5VC6DB7YoxyzniQqz2dElCt_O.; cna=rCV1EXHdDH8CAd9JOszJS5U8; isg=BE1NkJybLAT6PogdFTsLvMzpXGkHgoH6sz5DUI_SieRThm04VnqRzJsY9BrgQ5m0";
//		String url ="https://dy.feigua.cn/Aweme/Search?keyword=&tag=&likes=0&hours=72&duration=0&gender=0&age=0&province=0&city=0&sort=0&ispromotions=0&page=1";
	 /*	 String url ="https://www.aliexpress.com/item/33032642796.html?spm=2114.12010615.8148356.2.6af65c8aEolXe5";
		String content = HttpUtil.getHtmlContent(url,"");
		content = content.substring(content.indexOf(" data: "),content.indexOf("glo\"}}")+6);
		content = content.replace("data:", "");
		JSONArray zhutu = JsonUtil.getJSONArray("imageModule->imagePathList", content);
		for (Object object : zhutu) {
			System.out.println(object);  //主图主图
		}
		
		System.out.println("价格。。。。");
		JSONObject json =null;
		JSONArray prices = JsonUtil.getJSONArray("skuModule->skuPriceList", content);
		List<String> pricesList = new LinkedList<String>();
		for (int i = 0; i < prices.size(); i++) {
			json = (JSONObject) prices.get(i) ;
			json = (JSONObject) json.get("skuVal") ;
			pricesList.add(""+json.get("skuMultiCurrencyCalPrice")  );
		}
		
		
		JSONArray color = JsonUtil.getJSONArray("skuModule->productSKUPropertyList", content);
		//颜色图片
		System.out.println(color);
		if(color!=null){
			
		}*/
	/*	json =  (JSONObject) color.get(0);
		JSONArray colorarray =(JSONArray) json.get("skuPropertyValues");
		System.out.println("********颜色图片**********");
		for (int i = 0; i < colorarray.size(); i++) {
			json = (JSONObject) colorarray.get(i) ;
			System.out.println(pricesList.get(i)+"$"+json.get("propertyValueName")+"$"+json.get("skuPropertyImagePath"));
			
		}  */
	/*	JSONArray array = JsonUtil.getJSONArray("specsModule->props",content);
		for (Object object : array) {
			json = (JSONObject) object;
			System.out.println(json.get("attrName")+":"+json.get("attrValue"));
		}
		*/
		/*
		for (int i = 0; i <1; i++) {
			
		
		
//		String url = "https://www.amazon.de/Firsthgus-Wandleuchte-Nachttischlampe-schlafzimmer-wandleuchte/dp/B07TG1GVJT/ref=sr_1_39?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&keywords=Wandleuchte&qid=1562147619&refinements=p_n_availability%3A225433031&rnid=225431031&s=lighting&sr=1-39";
		String url = "https://www.amazon.de/MOKIU-Luftk%C3%BChler-Klimaanlage-Klimager%C3%A4t-Leistungsstufen/dp/B07SQ4RFMJ/ref=sr_1_8?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&keywords=Mini+Luftk%C3%BChler&qid=1562300279&refinements=p_n_availability%3A419126031&rnid=419124031&s=kitchen&sr=1-8";
		HashMap map = new HashMap<String, String>();
		map.put("keywords", HttpUtil.getQueryString(url, "keywords"));
		map.put("qid", HttpUtil.getQueryString(url, "qid"));
		map.put("rnid", HttpUtil.getQueryString(url, "rnid"));
		String s= HttpUtil.getHtmlContent(url,map,"",false);
		
		System.out.println(s.indexOf("Derzeit nicht verfügbar")==-1);
		
		String asin = s.substring(s.indexOf("ASIN</td><td"));
		  asin = asin.replace("ASIN</td><td","");
		asin = asin.substring(0, asin.indexOf("</tr>"));
		asin = asin.substring(asin.indexOf(">")+1,asin.indexOf("<"));
	 
		System.out.println(asin);
		
		
		//品牌可能为空
		String  marke="";
		try {
			if(s.indexOf("Technische Details")!=-1){
			 marke = s.substring(s.indexOf("Technische Details"));
			 marke = marke.substring(marke.indexOf("Marke"), marke.indexOf("</tr>"));
			 marke = marke.substring(marke.indexOf("value")+7,marke.lastIndexOf("</td>"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String rang = "";
		String paiming="";
		if(s.indexOf("Amazon Bestseller-Rang")!=-1){
			rang = s.substring(s.indexOf("Amazon Bestseller-Rang"));
			rang = rang.substring(0, rang.indexOf("</tr>"));
			paiming =rang.substring(rang.indexOf("Nr."),rang.indexOf("("));
			rang = rang.substring(rang.indexOf("class=\"zg_hrsr_rank\"") );
			paiming+=","+rang.substring(rang.indexOf("Nr."), rang.indexOf("</span>"));
			
		}
		
		String von = s.substring(s.indexOf("id=\"bylineInfo\""));
		von = von.substring(0, von.indexOf("</a>"));
		von = von.substring(von.lastIndexOf(">")+1);
		System.out.println("von"+von);
		System.out.println(paiming);
		
		System.out.println(s.indexOf("Amazon Bestseller-Rang")==-1);

		}
	
	
//		String ordergood = getQuestionCnt(cookies);
//		String ordergood = HttpUtil.getHtmlContent("http://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?isAbrdSellerYn=&isItalyAgencyYn=&listType=orderingConfirm&method=getUnapprovedOrderTotal", cookies);
//		String ordergood = getOrderGood( cookies);  //获取订单的数量
//		System.out.println(ordergood);
		//获取产品数量
//		getProductNum(cookies);
		
		//提取留言 ？？？？？
		
		
		/*
		System.out.println(s);
		String msg_count= JsonUtil.getJSONValue("msg_count",s);
		if("0".equals(msg_count)){
			System.out.println("没有数据啦！！！！！！！！！！！！！！！！！！");
		}
		String general_msg_list= JsonUtil.getJSONValue("general_msg_list",s);
		JSONObject jsonObject = JSONObject.parseObject(general_msg_list);
		System.out.println(jsonObject);
		List<String> list = JsonUtil.getJSONListValue("list->app_msg_ext_info->content_url", jsonObject.toJSONString());
		for (String contenturl : list) {
			System.out.println(contenturl);
		}
		System.out.println(general_msg_list);
		
		
	/*	String baseContent = Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
		String newText =  baseContent.replaceAll("\\s{2,}", "\n");
		String trueContent = newText.replaceFirst("\n", "").trim();
 System.out.println(trueContent);*/
//		String text = Jsoup.clean(s,Whitelist.none());
//		Document doc  = Jsoup.connect("https://mp.weixin.qq.com/s?__biz=MjM5ODYxMDA5OQ==&mid=2651962361&idx=1&sn=67bdcf5aa7435db757ace735f9968767&chksm=bd2d0e258a5a8733774350409ae2f3f1684e1536c105dbe92b1e9b3dbdaf83f2f67fbe7dad9b").execute().parse();
		
		
		
//	    Parser parser = Parser.htmlParser(); // createParser(new String(inputHtml.getBytes(),"GBK"), "GBK");
//	    parser.parse(html, baseUri)
				   
		/*Document doc =  Jsoup.parse(s);
		Element ele = doc.getElementById("js_article");
		
		WebClient  webClient = new WebClient();
		  HtmlPage page = webClient. .getPage(url);*/
//		String content = doc.body().select(cssQuery)
		
//		Element limit= Xsoup.select(doc, "//body/div[@id='js_article']");
//		Element limit = doc.body().selectFirst("div[class=html]");
		
/*		String s = HttpUtil.getHtmlContent("https://mp.weixin.qq.com/cgi-bin/appmsg?token=1194562356&lang=zh_CN&f=json&ajax=1&random=0.7707573761648039&action=list_ex&begin=5&count=10&query=&fakeid=MjM5ODYxMDA5OQ%3D%3D&type=9","UTF-8",cookies);
		List<String> list  = JsonUtil.getJSONListValue("app_msg_list->link", s);
		System.out.println(list.toString());
*/
		
///		String s = HttpUtil.postHtmlContent("https://item.taobao.com/item.htm?id=591758162649&wwlight=cntaobao%E5%9C%A8%E5%81%9A%E4%B8%80%E4%BC%9A-%7B591758162649%7D", map, cookies);
		/*
		 * String s = HttpUtil.getHtmlContent("https://detail.ju.taobao.com/home.htm?spm=608.2291429.102212b.1.6fd14f84Vw9Q1B&id=10000206429489&item_id=589814336859","gbk","");
		System.out.println(s);
//		String s = HttpUtil.getHtmlContent("https://detail.tmall.com/item.htm?spm=608.7065813.ne.1.52b72c7eW6AVos&id=548712023912&tracelog=jubuybigpic","GBK",  cookies);
		
		//天猫旗舰店
		if(s.indexOf("seller_nickname")!=-1){
			s = s.substring(s.indexOf("seller_nickname"));
			s = s.substring(0,s.indexOf("/>"));
			s = s.replace("seller_nickname\" value=\"", "");
			s = s.replace("\"", "");
		}else if(s.indexOf("sellerNick")!=-1){ //淘宝店铺
			s = s.substring(s.indexOf("sellerNick"));
			s = s.substring(0,s.indexOf(","));
			s = s.replace("sellerNick", "");
			s = s.replace("'", "");
			s = s.replace(":", "");
			s = s.replaceAll(" ", "");
		}else{
			s = s.substring(s.indexOf("data-nick="));
			s = s.substring(0,s.indexOf("data-tnick"));
			s = s.replace("data-nick=", "");
			s = s.replace("\"", "");
			s = s.replaceAll(" ", "");
		}*/
		
		
//		System.out.println("::"+s);  //获取淘宝的旺旺号，，，，商机。。。。
	/*	String cookies  = "Cookie: JSESSIONID=ABAAABAAADAAAEE801DB28141E5E5674DE0DC5DB4DCB663; user_trace_token=20190610101250-c276a2fd-a5c6-4c89-ad32-6927e8e8d726; _ga=GA1.2.1865789.1560132772; _gid=GA1.2.1002606687.1560132772; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1560132772; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1560132772; sajssdk_2015_cross_new_user=1; LGSID=20190610101251-402944a2-8b25-11e9-a22c-5254005c3644; PRE_UTM=; PRE_HOST=; PRE_SITE=; PRE_LAND=https%3A%2F%2Fpassport.lagou.com%2Flogin%2Flogin.html%3Fsignature%3DFE1B4782C7896ADA0AD04991D0AE154F%26service%3Dhttps%25253A%25252F%25252Feasy.lagou.com%25252Ftalent%25252Fsearch%25252Flist.htm%25253Fkeyword%25253D%252525E6%2525258B%2525259B%252525E8%25252581%25252598%252526pageNo%25253D1%252526city%25253D%252525E5%2525258C%25252597%252525E4%252525BA%252525AC%252526education%25253D%252525E6%2525259C%252525AC%252525E7%252525A7%25252591%252525E5%2525258F%2525258A%252525E4%252525BB%252525A5%252525E4%252525B8%2525258A%252526workYear%25253D3%252525E5%252525B9%252525B4-10%252525E5%252525B9%252525B4%252526industryField%25253D%252525E4%252525B8%2525258D%252525E9%25252599%25252590%252526expectSalary%25253D13k-25k%252526isBigCompany%25253D1%252526searchVersion%25253D1%26action%3Dlogin%26serviceId%3Daccount%26ts%3D1560132770821; LGRID=20190610101251-4029464a-8b25-11e9-a22c-5254005c3644; LGUID=20190610101251-402946b9-8b25-11e9-a22c-5254005c3644; LG_LOGIN_USER_ID=55ef98c339cb964f39a6aefcd5fc56c4f742716b0cef8ae9; LG_HAS_LOGIN=1; _putrc=CCDB5468444C87D5; login=true; unick=%E7%AE%A1%E7%90%86%E5%91%98; mds_login_authToken=\"KzgomPGgZr/cdGrbZTwQ9cSVFScgaMbQ2fIyfk2PdOcF+EJaG/PAaYAfgAczGLKhzBF12UMcv8fj3ZwfmqgOBLhHKPEY8Aag9cRFsuC/r9QUoMgkxbpN4ZC2TFC8UhILSSBb6+9lkIeG7k2TfxPa+ZloymJYA0mX6lndoHIFAE54rucJXOpldXhUiavxhcCELWDotJ+bmNVwmAvQCptcy5e7czUcjiQC32Lco44BMYXrQ+AIOfEccJKHpj0vJ+ngq/27aqj1hWq8tEPFFjdnxMSfKgAnjbIEAX3F9CIW8BSiMHYmPBt7FDDY0CCVFICHr2dp5gQVGvhfbqg7VzvNsw==\"; mds_u_n=%5Cu7ba1%5Cu7406%5Cu5458; mds_u_ci=165993; mds_u_cn=%5Cu5317%5Cu4eac%5Cu4fe1%5Cu94fe%5Cu79d1%5Cu6280%5Cu6709%5Cu9650%5Cu516c%5Cu53f8; mds_u_s_cn=%5Cu4fe1%5Cu94fe%5Cu79d1%5Cu6280; gate_login_token=4d4b0968d541ac3df831fa1ca75a4ed520d3dd36bce2e4ac; href=https%3A%2F%2Feasy.lagou.com%2Ftalent%2Fsearch%2Flist.htm%3Fkeyword%3D%25E6%258B%259B%25E8%2581%2598%26pageNo%3D1%26city%3D%25E5%258C%2597%25E4%25BA%25AC%26education%3D%25E6%259C%25AC%25E7%25A7%2591%25E5%258F%258A%25E4%25BB%25A5%25E4%25B8%258A%26workYear%3D3%25E5%25B9%25B4-10%25E5%25B9%25B4%26industryField%3D%25E4%25B8%258D%25E9%2599%2590%26expectSalary%3D13k-25k%26isBigCompany%3D1%26searchVersion%3D1; X_HTTP_TOKEN=493a485e1cc9d6b690823106512303ea9c4f3dc5aa; sensorsdata2015session=%7B%7D; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%227200826%22%2C%22%24device_id%22%3A%2216b3f28e00f3d-0cf207feec78f5-474a0521-1764000-16b3f28e010368%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_referrer_host%22%3A%22%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24os%22%3A%22Windows%22%2C%22%24browser%22%3A%22Chrome%22%2C%22%24browser_version%22%3A%2259.0.3071.115%22%2C%22easy_company_id%22%3A%22165993%22%2C%22lagou_company_id%22%3A%22174497%22%7D%2C%22first_id%22%3A%2216b3f28e00f3d-0cf207feec78f5-474a0521-1764000-16b3f28e010368%22%7D; qimo_seosource_551129f0-7fc2-11e6-bcdb-855ca3cec030=%E5%85%B6%E4%BB%96%E7%BD%91%E7%AB%99; qimo_seokeywords_551129f0-7fc2-11e6-bcdb-855ca3cec030=%E6%9C%AA%E7%9F%A5; accessId=551129f0-7fc2-11e6-bcdb-855ca3cec030; pageViewNum=2; gray=resume";
		String positionId = "6872761";
		String userId = "8179270";
		HashMap<String,String> map  = new HashMap<>();
		map.put("city", "北京");
		map.put("education", "本科及以上");
		map.put("expectSalary", "13k-25k");
		map.put("industryField", "不限");
		map.put("isBigCompany", "1");
		map.put("keyword", "招聘");
		map.put("pageNo", "1");
		map.put("searchVersion", "1");
		map.put("workYear", "3年-10年");
		String s = HttpUtil.postHtmlContent("https://easy.lagou.com/talent/search/list.json", map, cookies);
		System.out.println(s);*/
//		sendContent(cookies, positionId, userId);
		
//		System.out.println(URLDecoder.decode("https://easy.lagou.com/talent/search/list.htm?keyword=招聘&pageNo=4&city=北京&education=本科及以上&workYear=3年-10年&industryField=不限&expectSalary=13k-25k&isBigCompany=1&searchVersion=1", "UTF-8")  );
		
//		getPositionId();
		
		 
	 /*	String url =getTaoBaoProductDescUrl("596384214195", "aasdf"); //4066665511
//		String content = HttpUtil.getHtmlContent("https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdesc/6.0/?jsv=2.5.1&appKey=12574478&t=1560041755324&sign=1097fbb45b1759be09894edd79e3fd14&api=mtop.taobao.detail.getdesc&v=6.0&isSec=0&ecode=0&AntiFlood=true&AntiCreep=true&H5Request=true&type=jsonp&dataType=jsonp&callback=mtopjsonp2&data=%7B%22spm%22%3A%22a1z10.5-c-s.w4002-21086417433.37.22fb5e45EIqmcr%22%2C%22id%22%3A%22596549543120%22%2C%22type%22%3A%221%22%7D");
		String content = HttpUtil.getHtmlContent(url);
		JSONObject jsonObject = JSONObject.parseObject(content);
		JSONObject data = (JSONObject) jsonObject.get("data");
		String pcDescContent = ""+ data.get("pcDescContent");
		Document doc = Jsoup.parse(pcDescContent);
		Elements eles = Xsoup.select(doc, "//img").getElements();
		for (Element element : eles) {
			String src = "https:"+element.attr("src");
		}
		
		System.out.println(url);
		System.out.println(content); */
		 
		
		
		/*
		JSONArray jsonArray = null;
		JSONObject jsonObject=null;
		if(content.startsWith("[")){
			jsonArray = JSONArray.parseArray(content);
		}else{
			jsonObject=JSONObject.parseObject(content);
		}*/
		
		
		
		/*System.out.println(content);
		JSONObject jsonObject = JSONObject.parseObject(content);
		String succ = ""+jsonObject.get("ret");
		if(succ.indexOf("SUCCESS")==-1){
			System.out.println("请求失败");
			return ;
		}
		JSONObject data = (JSONObject) jsonObject.get("data");
		JSONObject skuBase = (JSONObject) data.get("skuBase");
		JSONArray jsonArray = (JSONArray) skuBase.get("props");
		JSONObject size = (JSONObject) jsonArray.get(0);
		JSONArray sizeArray  = (JSONArray) size.get("values");
		for (Object object : sizeArray) {
			System.out.println(((JSONObject)object).get("name"));
		}
		
		JSONObject color = (JSONObject) jsonArray.get(1);
		JSONArray colorArray  = (JSONArray)  color.get("values");
		for (Object object : colorArray) {
			System.out.println(((JSONObject)object).get("name"));
		}*/
		
//		String s= URLEncoder.encode("{\"originid\":\"\"}", "UTF-8");  
		
//		String url = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?jsv=2.5.1&appKey=12574478&t=1558888333992&sign=34cc81b35a28424a250b931972cd707d&api=mtop.taobao.detail.getdetail&v=6.0&isSec=0&ecode=0&AntiFlood=true&AntiCreep=true&H5Request=true&ttid=2018%40taobao_h5_9.9.9&type=jsonp&dataType=jsonp&callback=mtopjsonp1&data=%7B%22spm%22%3A%22a1z10.1-c.w4004-12534775751.7.4b257805iRnjyc%22%2C%22id%22%3A%22586451114773%22%2C%22itemNumId%22%3A%22586451114773%22%2C%22exParams%22%3A%22%7B%5C%22spm%5C%22%3A%5C%22a1z10.1-c.w4004-12534775751.7.4b257805iRnjyc%5C%22%2C%5C%22id%5C%22%3A%5C%22586451114773%5C%22%7D%22%2C%22detail_v%22%3A%228.0.0%22%2C%22utdid%22%3A%221%22%7D";
		
	/*	JsonObject json = new JsonObject();  //586451114773
		for (long i = 114773; i <= 114773; i++) {
			
			json.addProperty ("id", "586451"+i);
			json.addProperty("itemNumId", "586451"+i);
			json.addProperty("exParams", String.format("{\"id\":\"%s\"}","586451"+i));
			
			String url = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?data="+URLEncoder.encode(json.toString());
			String content = HttpUtil.getHtmlContent(url,"utf-8");  //材质保证
			System.out.println(content);
		}*/
		/*JSONObject jsonObject = (JSONObject) JSONObject.parseObject(content).get("item");
		JSONArray array = (JSONArray) jsonObject.get("images");
				for (int i = 0; i < array.size(); i++) {
					System.out.println("https://cf.shopee.ph/file/"+array.get(i));
				}
		*/
//		content = HttpUtil.getHtmlContent("http://item.jd.com/"+p1+".html");
		
	}

	private static String getTwoImage() throws Exception {
		String s =	HttpUtil.getHtmlContent("","GBK","");
		s  = s.substring(s.indexOf("id=\"spec-list\""));
		s = s.substring(s.indexOf("<li"),s.indexOf(" </div>"));
		String[] lis = s.split("<li");
		String li = lis.length >2 ? lis[2] : lis[1];
//		StringUtils.delHtml(s);
		String dataurl =  li.substring(li.indexOf("src='")+4, li.indexOf("data-url='"));
		dataurl = dataurl.replace("img.com/n5", "img.com/n1");
		dataurl = dataurl.replace("'", "");
		return dataurl;
	}

	private static String getQuestionCnt(String cookies) throws Exception {
		String ordergood = HttpUtil.getHtmlContent("http://soffice.11st.co.kr/marketing/SellerMenuAction.tmall?method=getEmerMainAlertStatAjax", cookies);
		ordergood = ordergood.replace("(", "");
		ordergood = ordergood.replace(")", "");
		System.out.println(JsonUtil.getJSONValue("emerStat00", ordergood));
		return ordergood;
	}
	/*
	 * 获取商品的数量
	 */
	private static void getProductNum(String cookies) throws Exception {
		String s =  HttpUtil.getHtmlContent("https://soffice.11st.co.kr/product/SellProductAction.tmall?method=getSellProductList","euc-kr",cookies);
		s = s.substring(s.indexOf("판매중 : "));
		s = s.substring(s.indexOf(">")+1,s.indexOf("</em>"));
		System.out.println(s);
	}
	
	/**
	 * 获取订单数量
	 * @param url
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public static String getOrderGood( String cookies)
			throws Exception {
		String url = "http://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?isAbrdSellerYn=&isItalyAgencyYn=&listType=orderingConfirm&method=getUnapprovedOrderTotal";
//		String url = "https://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?method=getUnapprovedOrderTotal";
		HashMap map = new HashMap<>();
		map.put("listType", "orderingConfirm");
		map.put("isAbrdSellerYn", "");
		map.put("isItalyAgencyYn", "");
		String s =  HttpUtil.postHtmlContent(url,map,cookies);
		System.out.println(s);
		String ordergood= JsonUtil.getJSONValue("unapprovedOrderTotal->order_good_202", s);
		return ordergood;
	}
	
	
	private static void sendContent(String cookies, String positionId,
			String userId) throws Exception {
		HashMap map = new HashMap<>();
		map.put("cUserIds", userId);
		String content = HttpUtil.postHtmlContent("https://easy.lagou.com/im/session/greetingList.json?", map, cookies);
		JSONObject res = JSONObject.parseObject(content);
		JSONObject con = (JSONObject) res.get("content");
		JSONObject data = (JSONObject) con.get("data");
		JSONArray greetingList = (JSONArray) data.get("greetingList");
		String sendContent = "";
		
		for (Object object : greetingList) {
			JSONObject json  = (JSONObject) object ; 
			if("true".equals(""+json.get("defaults"))){
				sendContent = ""+json.get("content");
				break;
			}
		}
		
		System.out.println(sendContent);
		
		HashMap param = new HashMap<>();
		param.put("inviteDeliver", "true");
		param.put("positionId", positionId);
		param.put("greetingContent", sendContent); //sendContent
        
		content = HttpUtil.postHtmlContent("https://easy.lagou.com/im/session/batchCreate/"+userId+".json?", param, cookies);
		System.out.println(content);
	}

	public static String urlEncode(String str) throws UnsupportedEncodingException {
	    StringBuilder sb = new StringBuilder();
	    //获得UTF-8编码的字节数组
	    byte[] utf8 = str.getBytes("UTF-8");
	    for (byte b : utf8) {
	      System.out.println(b);
	      //将字节转换成16进制，并截取最后两位
	      String hexStr = Integer.toHexString(b);
	      String temp = hexStr.substring(hexStr.length() - 2);
	      //添加%
	      sb.append("%");
	      sb.append(temp);
	    }
	    return sb.toString();
	  }
	 


	private static void getPageData(String key,Integer page ) throws Exception {
		String url = "https://search.jd.com/s_new.php";
		String refer = "https://search.jd.com/Search";
		Integer s=( page-1)*30+1;
		
		List<NameValuePair> urlParameters  = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("keyword", key)); //
		urlParameters.add(new BasicNameValuePair("enc", "utf-8"));
		urlParameters.add(new BasicNameValuePair("qrst", "1"));
		urlParameters.add(new BasicNameValuePair("rt", "1"));
		urlParameters.add(new BasicNameValuePair("stop", "1"));
		urlParameters.add(new BasicNameValuePair("vt", "2"));
//		urlParameters.add(new BasicNameValuePair("wq", key)); //"机械手表"
		urlParameters.add(new BasicNameValuePair("psort", "4"));
		urlParameters.add(new BasicNameValuePair("cod", "1"));
		urlParameters.add(new BasicNameValuePair("page", page+""));
		urlParameters.add(new BasicNameValuePair("s", s+""));
		
		
		urlParameters.add(new BasicNameValuePair("cid2", "5026"));
		urlParameters.add(new BasicNameValuePair("cid3", "13673"));
		
		if(page%2==0){
			urlParameters.add(new BasicNameValuePair("scrolling", "y"));
		}
		
		
		String content = HttpUtil.getHtmlContent(url,urlParameters,refer);
		if(content.indexOf("<script>")!=-1){
			content= content.substring(0,content.indexOf("<script>"));
		}
		
//		System.out.println(content);
		
		Document doc =null;
		Document element = null;
		List<Node> nodes =null;
		doc = Jsoup.parse(content); 
		
		if(page%2!=0){
			content = Xsoup.select(doc, "//body/div/ul").get();
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
			shopName=shopName.substring(shopName.indexOf(">"));
			JdEntity e = new JdEntity();
			e.setShopName(shopName);
			e.setUrl(url);
			System.out.println(href+"|"+shopName);
		}
	} 
	
	private static Map<String, String> toMap(String url) {
		  int index = url.indexOf("?");
	        String param = url.substring(index+1);

	        String[] params = param.split("&");

	        Map<String,String> map = new HashMap<>();

	        for (String item:params) {
	            String[] kv = item.split("=");
	            map.put(kv[0],kv.length==2 ? kv[1]:"" );
	        }
	        return map;
	        
	}

	public static String getQueryString(String url, String name) {
		return toMap(url).get(name);
	}
	 
}
