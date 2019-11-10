package com.crawler.entity;

/*
 * 图片过滤属性
 */
public class ImageProperty {
	
	private Integer minWidth ;
	private Integer width ;
	private Integer minHeight ;
	private Integer height ;
	private Integer minSize ;  //KB为单位
	private Integer size ;  //KB为单位
	private Integer maxSize ;  //KB为单位
	private Float ratioFromField ;  //宽高比例，小数点
	private Float ratioToField ;   
	private String suffix  ;
	
	public Integer getMinWidth() {
		return minWidth;
	}
	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
	}
	public Integer getMinHeight() {
		return minHeight;
	}
	public void setMinHeight(Integer minHeight) {
		this.minHeight = minHeight;
	}
	public Integer getMinSize() {
		return minSize;
	}
	public void setMinSize(Integer minSize) {
		this.minSize = minSize;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public Integer getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}
	public Float getRatioFromField() {
		return ratioFromField;
	}
	public void setRatioFromField(Float ratioFromField) {
		this.ratioFromField = ratioFromField;
	}
	public Float getRatioToField() {
		return ratioToField;
	}
	public void setRatioToField(Float ratioToField) {
		this.ratioToField = ratioToField;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	} 
	
}
