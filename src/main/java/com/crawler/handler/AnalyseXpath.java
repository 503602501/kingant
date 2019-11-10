package com.crawler.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.testng.internal.PropertyUtils;

import com.alibaba.fastjson.JSON;
import com.crawler.entity.ClickUnit;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Unit;
import com.crawler.entity.UnitAdapter;
import com.crawler.util.FolderUtil;

/*
 * 解析配置文件的xpath
 */
public class AnalyseXpath {
	
	private String xpathConfig ;
	private OpenUnit openUnit ; 
	private static final Logger logger =  Logger.getLogger(AnalyseXpath.class);
	private HashMap<Integer, Unit> map =new LinkedHashMap<Integer, Unit>();  //顺序存储
	
	public AnalyseXpath(String xpathConfig) {
		this.xpathConfig = xpathConfig ; 
	}
	
	public void buildPage(Unit unit) throws Exception{
		
//		Object object=getSubClass(unit);
		Object object=unit;
		
		List<Unit> list = new ArrayList<Unit>(); //存储比unit层级小的对象
		for (Entry<Integer, Unit> entry : map.entrySet()) {
			if( unit.getLevel() > entry.getKey()){
				list.add(entry.getValue());
			}
		}
		
		//存在父类
		if(list.size()!=0){
			Unit lastUnit = list.get(list.size()-1) ; //最后的一个单元，即是 unit的父节点
			lastUnit.addChildUnit((Unit) object);
		}else{
			setOpenUnit((OpenUnit) object);
		}
		
		map.put(unit.getLevel(), (Unit)object); //存放到全局map，子类转成父类
	}
	
	private Class getClassByName(String str) throws Exception{
		  String  name = str.trim();
		  String className = name.replaceFirst(name.substring(0, 1),name.substring(0, 1).toUpperCase()) ;
		  Class clazz = Class.forName("com.crawler.entity."+className+"Unit") ; 
		  return clazz;
	}
	
	/*
	 * 解析配置信息
	 */
	public OpenUnit parseConfig() throws Exception{
		List<String> list = readConfig();
		String content =null;
		String type =null;
		for (String line : list) {
		   content =  line.substring(line.indexOf(":")+1).trim();
		   type =  line.substring(0,line.indexOf(":"));
		   Class clazz = getClassByName(type);
		   Unit unit = (Unit) JSON.parseObject(content,clazz); 
		   unit.setType(type.trim());
		   unit.setLevel(type.replaceAll("[a-zA-Z]","").length());
		   System.out.println(multipleSpaces(unit.getLevel())+content+":"+unit.getType());
		   unit.init(); //初始化自己的逻辑
		   buildPage(unit); //建造xpath层级结构
		}
		
		return getOpenUnit();
	}
	
	public List<String> readConfig() throws Exception{
		List<String> list = new ArrayList<String>();
		InputStream in = FolderUtil.getConfigInputStream(xpathConfig);
		BufferedReader br = null;
		try {
			br = new BufferedReader( new InputStreamReader(in,"utf-8"));
			String line=null;
			while((line = br.readLine())!=null){
				if(null==line || "".equals(line.trim())){
					continue;
				}
				list.add(line);
			}
			
		} catch (Exception e) {
			logger.error(xpathConfig+"配置读取失败:"+e.getMessage());
		}finally{
			br.close();   
			in.close();
		}
		return list ;
	}
	
	public void setOpenUnit(OpenUnit openUnit) {
		this.openUnit = openUnit;
	}

	private OpenUnit getOpenUnit() {
		return openUnit;
	}
	
	public static void main(String[] args) {
		
		ClickUnit click = new ClickUnit();
		Unit unit = click;
		UnitAdapter apAdapter = (UnitAdapter) unit;
		
		System.out.println(apAdapter);
	}
	
	private String multipleSpaces(int n){
		   String output = "";
		 
		   for(int i=0; i<n; i++)
		      output += " ";
		 
		   return output;
	}
}
