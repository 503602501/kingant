package com.crawler.handler.row.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.crawler.entity.Environment;
import com.crawler.entity.Product;
import com.crawler.handler.row.IRowHander;
import com.crawler.util.FolderUtil;
import com.crawler.util.ImageUtil;
import com.crawler.util.NumberUtil;
import com.crawler.util.StringUtils;

/*
 * 1688，特殊定制了主图和详情页面的图片分离文件夹
 * $main$，做为主图的标识
 * 
 */
public class AlibabaMainPicHandler implements IRowHander {

	private volatile  List<Integer> indexs ;  //文件夹的索引位置
	private volatile  Integer nameIndex ;  //文件名的索引位置
	private Lock lock ;

	public AlibabaMainPicHandler() {
		lock= new ReentrantLock();
	}
	
	@Override
	public String handerRow(Product product,Environment env) {
		
		String path  = env.getDownloadPath();
		//获取标题列的名称作为文件夹名
		if(indexs==null){
			lock.lock(); //加锁  
			// 1.处理文件夹层级
			if(indexs==null){
				List folders= new ArrayList<>();
				for (int i = 0; i < product.getHeader().size(); i++) {
					if(product.getHeader().get(i).indexOf("文件夹名")!=-1){
						folders.add(i);
					}
					
					if(product.getHeader().get(i).indexOf("文件名")!=-1){  //久的逻辑规则
						nameIndex = i;
					}
				}
				indexs=folders;  //解决高并发 ，出现的一些图片没有创建文件夹的情况!!!!!
			}
			
			//2.处理文件的名称索引位置
			if(!StringUtils.isEmpty(env.getFileName())){
				for (int i = 0; i < product.getHeader().size(); i++) {
					if(product.getHeader().get(i).contains(env.getFileName())){
						nameIndex = i ;
					}
				}
			}
			lock.unlock();//解锁
		}
		
		String folder = "";
		if(indexs.size()!=0){
			for (Integer index: indexs) {
				folder=folder+File.separator+FolderUtil.filterFolderName(product.getList().get(index));
//				folder=folder+File.separator+ product.getList().get(index) ;
			}
		}else{
			folder = File.separator+"download";
		}
			
		path=path+File.separator+folder;
		String url = product.getUrl();
		
		String isMain = product.getList().get(2);
		
		if("1".equals(isMain)){
			path= path+File.separator+"主图";
		}else{
			path= path+File.separator+"详情图";
		}
		
		File file =new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		
	    
//	    String imgName =nameIndex!=null ? product.getList().get(nameIndex) :  FolderUtil.filterFolderName( url.substring( url.substring(0, url.lastIndexOf("/")).lastIndexOf("/")) );
	    String imgName =nameIndex!=null ? product.getList().get(nameIndex) :  FolderUtil.filterFolderName( url.substring(url.lastIndexOf("/")));
	    if(StringUtils.isEmpty(imgName)){
	    	imgName=NumberUtil.autoNumber()+System.currentTimeMillis();
	    }
	    
//		String imgName = FolderUtil.filterFolderName(url.substring(url.lastIndexOf("/")+1 ) ) ;  //图片名
		/*imgName = ImageUtil.validatePrefix(imgName);
		//图片名大于100,特殊处理以系统时间作为图片名
		 */
		if(imgName.length()>200){
			String imageNumber=NumberUtil.autoNumber()+System.currentTimeMillis();
			if(imgName.lastIndexOf(".")!=-1){
				imageNumber+=imgName.substring(imgName.lastIndexOf("."));
			}
			imgName=imageNumber;
		}
		if(!StringUtils.isEmpty(env.getSuffix())){
			imgName = imgName.endsWith(env.getSuffix()) ? imgName : imgName+env.getSuffix();
		}else{
			imgName=ImageUtil.validatePrefix(imgName);
		}
		
//		imgName = System.currentTimeMillis()+NumberUtil.autoNumber()+"-"+imgName;
		//没有后缀,默认使用png
		
		String fullPath =path+File.separator+imgName  ;  
		
		return fullPath;
	}
	public static void main(String[] args) {
		String s = "http://cdn1.ykso.co/mjboutique/product/crystal-butterfly-venetian-masquerade-mask-64223/images/39a4bfa/1448657451/generous.jpg";
//		String folder = s.substring( s.substring(0, s.lastIndexOf("/")).lastIndexOf("/"));
		String folder = s.substring(s.lastIndexOf("/"));
		System.out.println(FolderUtil.filterFolderName(folder));
		
		
	}

}
