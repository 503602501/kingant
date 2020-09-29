package com.crawler.entity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.alibaba.fastjson.JSONObject;
import com.crawler.util.HttpUtil;


public class Expire {

	private Date date ;
	private String message;
	private boolean isExpire;
	
	public Expire(String object) {
		JSONObject jsonObject=JSONObject.parseObject(object);
		String d = (String) jsonObject.get("date");
		this.message = (String) jsonObject.get("message");
		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        try {
        	this.date=simpleDateFormat.parse(d);
        	
        } catch(ParseException px) {
            px.printStackTrace();
            this.isExpire = false;
            return ;
        }
        
        try {
       /* 
		   TimeZone.setDefault(TimeZone.getTimeZone("GMT+8")); // 时区设置
		   URL url=new URL("http://bjtime.cn/");//取得资源对象
		   URLConnection uc=url.openConnection();//生成连接对象
		   uc.connect(); //发出连接
		   long ld=uc.getDate(); //取得网站日期时间（时间戳）
*/		   
//        	Date bjdate=new Date(ld); //转换为标准时间对象
        	String webTime=getNetworkTime();
        	Date bjdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(webTime);//定义起始日期
		   
		   if(bjdate.after(this.date)){
			   isExpire = true;
		   }else{
			   isExpire = false;
		   }
		} catch (Exception e) {
			 isExpire = false; 
			e.printStackTrace();
		}
        
	}
	
	   public static String getNetworkTime() {
	    	
	    	String webUrl="http://www.baidu.com";
	        try {
	            URL url=new URL(webUrl);
	            URLConnection conn=url.openConnection();
	            conn.connect();
	            long dateL=conn.getDate();
	            Date date=new Date(dateL);
	            SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd HH:mm");
	            return dateFormat.format(date);
	        }catch (MalformedURLException e) {
	            e.printStackTrace();
	        }catch (IOException e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
	        return "";
	    }
	   
 
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isExpire() {
		return isExpire;
	}

	public void setExpire(boolean isExpire) {
		this.isExpire = isExpire;
	}

	public static void main(String[] args) throws Exception {
		
		Expire e = new Expire("{date:'20180907',message:'运行异常'}");
		System.out.println(e.isExpire);
	}
	
}
