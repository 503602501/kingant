package com.crawler.handler.open.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.entity.StoreData;
import com.crawler.handler.open.IOpen;
import com.crawler.util.ExcelUtil;
import com.crawler.util.StringUtils;

/*
 * 手机号码转换vcard处理类
 */
public class AddressHandler implements IOpen {

	private static final Logger logger =  Logger.getLogger(AddressHandler.class);

	 
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		Product product = storage.getInputUrlQueues().poll();
		String path = product.getUrl();  //excel的路径
		List<StoreData> list = ExcelUtil.readExcel(path);
		StoreData storeData= list.get(0);
		
		for (int i = 1; i < list.size(); i++) {
			for (List<String> row: list.get(i).getList()) {
				storeData.getList().add(row);
			}
		}
		
		 
		List<String> li = new LinkedList<>();
		StringBuffer sb = new StringBuffer();
		boolean  flag = false ;
		for (List<String> row: storeData.getList()) {
//			System.out.println("每行:"+row);
			String text = row.get(1) ;
			if(text.toLowerCase().startsWith("ship") ){
				flag = true ;
				sb.append(text+"$");
				continue;
			}
			
			
			if(flag!=true){
				continue;
			}
			
			if(StringUtils.isEmpty(text) || text.toLowerCase().startsWith("usps ") || text.indexOf("■")!=-1){
				continue ;
			}
			
			if(text.startsWith("93") ){
				if(text.replaceAll(" ", "").length()==22){  //结束位置
					flag = false;
					li.add(sb.toString()) ; 
					sb=new StringBuffer();
					continue ;
				}
				
				System.out.println("异常情况》》》》》》"+text);
			}
			
			sb.append(text+"$");
		}
	
		try {
			
			for (String str : li) {  
//				System.out.println(str);
				String [] array  =str.split("\\$")  ;
				if(array.length<=2){
					continue ;
				}
				storage.getStoreData().addText( array[0], "SHIP",storage);
				try {
					storage.getStoreData().addText( array[1], "地址1",storage);
					
				} catch (Exception e) {
					System.out.println(str);
				}
				String[] sss;
				if(array.length==4){
					storage.getStoreData().addText( array[2], "地址2",storage);
					  sss = array[3].split(" ");
				}else{
					storage.getStoreData().addText( "", "地址2",storage);
					  sss = array[2].split(" ");
				}
				
				System.out.println(StringUtils.arrayToString(sss));
				if(sss.length==1){
					storage.getStoreData().addText( sss[0], "地址3",storage);
					storage.getStoreData().addText( "", "地址4",storage);
					storage.getStoreData().addText( "", "地址5",storage);
					continue ;
				}
				
				String first = StringUtils.arrayToString(sss).replace(",", "").replace(sss[sss.length-2]+sss[sss.length-1], "");
				storage.getStoreData().addText( first, "地址3",storage);
				storage.getStoreData().addText( sss[sss.length-2], "地址4",storage);
				storage.getStoreData().addText( sss[sss.length-1], "地址5",storage);
			}  
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
	}
 
	
	
	public static void main(String[] args) {
		System.out.println( "aa$bb".split("\\$").length );
	}
	
}
