package com.crawler.entity;

import com.crawler.util.StringUtils;

public class Vcard {

	//姓名
	private String N ; 
	
	//公司
	private String ORG ; 
	
	//电话
	private String TEL ;
	
	public String getN() {
		if(StringUtils.isEmpty(this.N)){
			return "空姓名"+System.currentTimeMillis();
		}
		return N;
	}
	public void setN(String n) {
		N = n;
	}
	
	public String getORG() {
		if(StringUtils.isEmpty(this.ORG)){
			return "";
		}
		return ORG;
	}
	public void setORG(String oRG) {
		ORG = oRG;
	}
	public String getTEL() {
		return TEL;
	}
	public void setTEL(String tEL) {
		TEL = tEL;
	}
	
	public Vcard(String N , String ORG, String TEL) {
		this.N=N;
		this.ORG = ORG;
		this.TEL = TEL;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("BEGIN:VCARD\r\n");
		sb.append("N:"+this.getN()+";\r\n");
		sb.append("FN:"+this.getN()+"\r\n");
		sb.append("ORG:"+this.getORG()+"\r\n");
		sb.append("TEL;HOME;CELL:"+this.TEL+"\r\n");
		sb.append("VERSION:3.0\r\n");
		sb.append("END:VCARD\r\n");
		sb.append("\r\n");
		return sb.toString();
	}
}
