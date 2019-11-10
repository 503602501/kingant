package com.crawler.entity;

import java.util.ArrayList;
import java.util.List;

import com.crawler.util.ConfigUtil;

public class StoreData {

	private List<List> list ;
	
	private List<String>  header ;  //所有的头部
	
	private String sheet ;
	
	private Integer total;
	
	private List<String> showHeaders; //显示的头部
	
	public void setList(List<List> list) {
		this.list = list;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public List<List> getList() {
		return list;
	}

	public List<String> getHeader() {
		return header;
	}

	public String getSheet() {
		return sheet;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}

	/*
	 * 初始化数组大小
	 */
	public StoreData(Integer total) {
		this.total = total;
		this.header = new ArrayList<>();
		this.showHeaders = new ArrayList<>();
		initList();
	}

	public void initList(){
		list = new ArrayList<>(this.total);
	}
	
	/*
	 * 存储数据,如果已经采集完一行数据，就显示到用户控制台
	 */
	public void addText(String context,String label,Storage storage) throws Exception {
		if(label.equals(header.get(0))){ //如果是第一个字段，新增一行
			ArrayList<String> s = new ArrayList<String>();
			s.add(context);
			list.add(s);
			if(isLastLabel(label)){
				if(storage.isMajor()  && storage.getEnv().isExistsDetailThread() ){
					addProduct(s, storage);
				}else{
					storage.getEnv().showTableArea(s,header, showHeaders);
				}
			}
			return ;
		}
		
		List<String> lastRow = list.get(list.size()-1); //获取最后一行
		int index = header.lastIndexOf(label);
		
		//该字段已经存在数据，需要重新复制一行,并清除该字段后面的的数据
		if((lastRow.size()-1)>=index && lastRow.get(index)!=null){
			ArrayList<String> copyList = new ArrayList<String>();
			copyList.addAll(lastRow.subList(0, index));
			copyList.add(context);
			list.add(copyList);
			
			if(isLastLabel(label)){
				if( storage.isMajor() && storage.getEnv().isExistsDetailThread()){
					addProduct(copyList, storage);
				}else{
					storage.getEnv().showTableArea(  copyList,header, showHeaders);
				}
			}
			
		}else{ //没有数据可以填充
			lastRow.add(index, context);
			
			if(isLastLabel(label)){
				if( storage.isMajor() && storage.getEnv().isExistsDetailThread()){
					addProduct(lastRow, storage);
					
				}else{
					storage.getEnv().showTableArea( lastRow,header, showHeaders);
				}
			}
		}
		
			
	}
	
	/*
	 *是否填充到最后一个字段
	 */
	private boolean isLastLabel(String label){
		
		if(label.equals(getHeader().get(getHeader().size()-1))){
			return true ; 
		}
		return false;
	}
	
	/*
	 * 初始化excel表头字段
	 */
	public void addHeader(String text){
		header.add(text);
	}
	
	
	public List<String> getShowHeaders() {
		return showHeaders;
	}
	

	public void setShowHeaders(List<String> showHeaders) {
		this.showHeaders = showHeaders;
	}

	public void addShowHeaders(String text) {
		this.showHeaders.add(text);
	}

	/*
	 * 添加产品到队列中去
	 * 
	 */
	public void addProduct(List row, Storage storage) throws InterruptedException {
		if(row==null){
			return ;
		}
		List dest = new ArrayList<>(row);
		Product product = new Product();
		product.setList(dest);
		product.setHeader(getHeader());
		product.setUrl(""+dest.get(this.getHeader().indexOf(storage.getUrlLabel()))); //默认第一个为链接,已修复为配置
		storage.push(product);
	}

}
