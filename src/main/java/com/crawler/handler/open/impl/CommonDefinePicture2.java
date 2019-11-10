package com.crawler.handler.open.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.entity.StoreData;
import com.crawler.handler.open.IOpen;
import com.crawler.util.ExcelUtil;
import com.crawler.util.StringUtils;

public class CommonDefinePicture2 implements IOpen {

	private static final Logger logger =  Logger.getLogger(CommonDefinePicture2.class);
	
	/*
	 * 处理excel导入
	 * 通用自定义图片链接，图片名称，批量下载图片到本地
	 * 图片名称重复检查
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		String addressname = "";
		Product product = storage.getInputUrlQueues().poll();
		String path = product.getUrl();  //excel的路径
		List<StoreData> list =null;
		try {
			list = ExcelUtil.readExcel(path);
		} catch (Exception e) {
			e.printStackTrace();
			storage.getEnv().showLogArea("\n错误提示："+e.getMessage()+"!!!!!\n\n");
			return ; 
		}
		StoreData storeData= list.get(0);
		
		//约定只识别：表头的姓名、公司、电话
		List<String> headers = storeData.getHeader();
		Integer picIndex = headers.indexOf("图片链接");
		Integer nameIndex = headers.indexOf("图片名称");
		Integer addressIndex = null;
		if(headers.indexOf("收货地址 ")!=-1){
			 addressIndex = headers.indexOf("收货地址 ");
		}
		
		
		if(picIndex==-1){
			storage.getEnv().showLogArea("\n错误提示：EXCEL不存在【图片链接】字段，软件无法识别[图片链接]所在的列!!!!!\n\n");
			return ; 
		}
		
		if(nameIndex==-1){
			storage.getEnv().showLogArea("\n错误提示：EXCEL不存在【图片名称】字段，软件无法识别[图片名称]所在的列!!!!!\n\n");
			return ; 
		}
	
		Set<String> set = new HashSet<>();
		Set<String> duplicate = new HashSet<>(); //重复信息
		boolean isEmpty = false;
		//重复图片名称检查
		for (List row : storeData.getList()) {
			
			if(StringUtils.isEmpty(row.get(nameIndex))){
				isEmpty = true;
				continue; 
			}
			
			if(set.contains(row.get(nameIndex))){
				duplicate.add(""+row.get(nameIndex));
			}else{
				set.add(""+ row.get(nameIndex));
			}
		}
		
		if(isEmpty){
			storage.getEnv().showLogArea("\n错误提示：存在空的图片名称!!!!!\n\n");
			return ; 
		}
		
		if(!duplicate.isEmpty()){
			storage.getEnv().showLogArea("\n错误提示：存在重复的图片名称："+duplicate.toString()+"!!!!!\n\n");
			return ; 
		}
		
		for (List row : storeData.getList()) {
			storage.getStoreData().addText(row.get(nameIndex)+"", "图片名称",storage);
			if(addressIndex!=null && !StringUtils.isEmpty(row.get(addressIndex))){
				addressname  = ""+row.get(addressIndex) ;
				addressname = addressname.split(" ")[0];
				storage.getStoreData().addText(addressname.trim(), "文件夹名-地址",storage);
			}else{
				storage.getStoreData().addText("", "文件夹名-地址",storage);
			}
			
			storage.getStoreData().addText(row.get(picIndex)+"", "图片链接",storage);
			
		}
		storage.getInputUrlQueues().clear();
	}
 
}
