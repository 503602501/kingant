package com.crawler.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
	
	private static volatile Integer autoValue=0;
	
	/*
	 * 自增数值
	 */
	public synchronized static String autoNumber() {
		autoValue+=1;
		String str = String.format("%05d", autoValue);  
		return str;
	}
	
	/*
	 * 自增数值
	 */
	public synchronized static void resetNumber() {
		autoValue = 0;
	}
	
	public static boolean isMobileNO(String mobiles){  
		  
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		  
		Matcher m = p.matcher(mobiles);  
		
		
		boolean flag = Pattern.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9]|17[0|6|7|8])\\d{8}$", mobiles);
		if(flag==false && m.matches()==false){
			return false;
		}
		return true;
	}  
	 
	
	public static boolean isNumeric(String s) {
	    if (s != null && !"".equals(s.trim()))
	        return s.matches("^[0-9.]*$");
	    else
	        return false;
	}  
	
	public static boolean isInteger(String s) {
		if (s != null && !"".equals(s.trim()))
			return s.matches("^[0-9]*$");
		else
			return false;
	}  
	
	public static Integer converInteger (String s) {
		if(StringUtils.isEmpty(s)){
			return 0 ;
		}
		return new Integer (s);
	}  
	
	  
    /** 
     * 电话号码验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isPhone(String str) {   
        Pattern p1 = null,p2 = null;  
        Matcher m = null;  
        boolean b = false;    
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的  
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的  
        if(str.length() >9)  
        {   m = p1.matcher(str);  
            b = m.matches();    
        }else{  
            m = p2.matcher(str);  
            b = m.matches();   
        }    
        return b;  
    } 
    
    public static  String formatTwo(double f) {
    	DecimalFormat df = new DecimalFormat("0.00");
    	return df.format(f);
    }
	
}
