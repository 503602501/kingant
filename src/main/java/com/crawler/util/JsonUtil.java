package com.crawler.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

public class JsonUtil {


	/**
	 * 数组形式返回
	 */
	public static JSONArray  getJSONArray(String format ,String jsonString ) {
		Object json = JSONObject.parse(jsonString);
		return (JSONArray)parseJSON(format, (JSON) json);
	}
	/**
	 * 对象形式返回
	 * @param format
	 * @param jsonString
	 * @return
	 */
	public static JSONObject  getJSONObject(String format ,String jsonString ) {
		Object json = JSONObject.parse(jsonString);
		return (JSONObject)parseJSON(format, (JSON) json);
	}
	
	/**
	 * 直接返回字符串
	 * @param format
	 * @param jsonString
	 * @return
	 */
	public static String  getJSONValue(String format ,String jsonString ) {
		String lastKey = format.substring(format.lastIndexOf("->")+2);
		if(format.lastIndexOf("->")!=-1){
			format = format.substring(0,format.lastIndexOf("->"));
			
			Object json = JSONObject.parse(jsonString);
			JSONObject object =  (JSONObject)parseJSON(format, (JSON) json);
			if(object.containsKey(lastKey)){
				return ""+object.get(lastKey);
			}
			
		}else{  //就只是一层
			JSONObject object =  JSONObject.parseObject(jsonString);
			return ""+object.get(format);
		}
		
		
		return null;
	}
	
	/**
	 * 便捷获取数组里的对象的值,只有一层数组，数组下就是对象
	 * 返回集合形式的
	 * @param format
	 * @param jsonString
	 * @return
	 */
	public static List<String>  getJSONListValue(String format ,String jsonString ) {
		String lastKey = format.substring(format.lastIndexOf("->")+2);
		List<String> list=  new ArrayList<>();
		List<JSONObject> jsonList=  new ArrayList<>();
		if(format.lastIndexOf("->")!=-1){
			format = format.substring(0,format.lastIndexOf("->"));
			Object json = JSONObject.parse(jsonString);
			JSONArray array =  (JSONArray)parseJSON(format, (JSON) json);
			for (Object object : array) {
				JSONObject jsonObject = (JSONObject) object;
				list.add(jsonObject.get(lastKey)+"");		
			}
		}else{
			JSONObject json = (JSONObject) JSONObject.parse(jsonString);
			list.add(""+json.get(format));
		}
		return list;
	}
	
	
	/**
	 * 解析的格式中，如果中间是数组，就直接返回，只处理一次数组
	 * 对象的格式可以一直解析到最后
	 * 递归循环获取json信息
	 * 
	 */
	private static JSON parseJSON(String format , JSON json ) {
		
		String start  = format.indexOf("->")!=-1 ? format.split("->")[0] : format;
		String next = "";
		if(format.split("->").length>1){
			 next = format.substring(format.indexOf("->")+2);
		}
						
		if(json instanceof JSONArray){ //如果是数组，解析数组下的对象，直接返回了
			JSONArray  array =(JSONArray) json;
			JSONArray  returnArray =new JSONArray();
			JSONObject jsonObject;
			for (Object object : array) {
				if(object instanceof JSONObject){  //对象
					jsonObject= (JSONObject) object;
					if(jsonObject.containsKey(start)){
						returnArray.add( jsonObject.get(start) );
					}
				}else{
					System.out.println("未处理逻辑*****************");
				}
//				list.add(jsonObject.get(next));
			}
			
			return returnArray;
//			JSONArray array = (JSONArray) json  ;
//			jsonObject= (JSONObject) json;
//			List<JSONObject> list = new ArrayList<>();
			/*for (Object object : array) {
				jsonObject= (JSONObject) object;
//				list.add(jsonObject.get(next));
				System.out.println();
			}*/
//			return (JSON) jsonObject.get(next);
			
		}else if(json instanceof JSONObject){
			JSONObject object = (JSONObject) json  ;
			if(format.indexOf("->")==-1){  //最底层的数据了
				return (JSON)object.get(start); 
			}
			
			return parseJSON(next, (JSON)object.get(start));
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
	
	public static void main(String[] args) throws Exception {
		String url = getTaoBaoProductUrl("596568995834","5291855b92ad461e2f9cf63d8dfa8352");
		String content  = HttpUtil.getHtmlContent(url);
		String format = "data->seller->sellerNick"; //  ->tmallLevelBackgroundColor  ->基本信息
		String s = JsonUtil.getJSONValue(format, content);
//		JSONObject s = getJSONObject(format, content);
//		JSONArray s = getJSONArray(format, content);
		System.out.println(s);
		System.out.println(s==null);
	}
	
}
