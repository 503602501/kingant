package com.crawler.handler.open.impl;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.entity.StoreData;
import com.crawler.entity.Vcard;
import com.crawler.handler.open.IOpen;
import com.crawler.util.ExcelUtil;
import com.crawler.util.FolderUtil;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

/*
 * 手机号码转换vcard处理类
 */
public class PhoneConvertBook implements IOpen {

	private static final Logger logger =  Logger.getLogger(PhoneConvertBook.class);

	/*
	 * 每次只转换一个excel
	 * 约定采集输入：需要转换的excel
	 * 默认只识别：姓名，公司，电话，这三个表头字段
	 * 转换后的vcard格式的文件，默认放在excel所在的文件夹下
	 * 电话号码为空的不处理导入
	 * 设置：对应的导入关系映射
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		Product product = storage.getInputUrlQueues().poll();
		String path = product.getUrl();  //excel的路径
		List<StoreData> list = ExcelUtil.readExcel(path);
		StoreData storeData= list.get(0);
		
		//约定只识别：表头的姓名、公司、电话
		List<String> headers = storeData.getHeader();
		Integer nameIndex = headers.indexOf("姓名");
		Integer orgIndex = headers.indexOf("公司");
		Integer telIndex = headers.indexOf("电话");
		
		if(telIndex==-1){
			storage.getEnv().showLogArea("\n错误提示：EXCEL不存在【电话】字段，软件无法识别电话号码所在的列!!!!!\n\n");
			return ; 
		}
		
		File file = new File(path+File.separator+System.currentTimeMillis()+".vcf");
		StringBuffer sb = new StringBuffer();
		String mobile = "";
		String org = "";
		String name = "";
		Set<String> set = new HashSet<>();
		for (List<String> row: storeData.getList()) {
			mobile = row.get(telIndex) ;
			
			if(orgIndex!=-1){
				org = row.get(orgIndex); 
			}
			
			if(nameIndex!=-1){
				name =  row.get(nameIndex); 
			}
			
			if(StringUtils.isEmpty(mobile)){
//			if(!NumberUtil.isMobileNO(mobile) && !NumberUtil.isPhone(mobile) ){
				logger.error("非法号码:"+row.toString());
				continue;
			}
			
			if(set.contains(mobile)){
				continue;
			}else{
				set.add(mobile);
			}
			
			if(StringUtils.isEmpty(org)){
				org = "";
			}
			if(StringUtils.isEmpty(name)){
				name = "";
			}
			
			mobile=mobile.replaceAll("\\s*", "").replaceAll("\r|\n", "");
			org=org.trim().replaceAll("\r|\n", "");
			name=name.trim().replaceAll("\r|\n", "");
//			sb.append( new Vcard(name+"-"+( (int)(Math.random()*1000)  ), org, mobile).toString());
//			sb.append( new Vcard("25-"+name+"-" +NumberUtil.autoNumber() , org, mobile).toString());
			sb.append( new Vcard(name, org, mobile).toString());
//			sb.append( new Vcard("天玺湾"+name , org, mobile).toString());
		}
		
		logger.info("总行数:"+storeData.getList().size()+",去掉重复号码和空号码后剩下："+set.size());
 		FolderUtil.writeFileContent(file, sb.toString());
	}
 
	
	public static void main(String[] args) {
		System.out.println( );
	}
	
}
