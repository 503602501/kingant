package com.crawler.entity;

import java.io.File;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.crawler.ui.Widgets;
import com.crawler.util.ConfigUtil;
import com.crawler.util.FolderUtil;
import com.crawler.util.StringUtils;

public class Setting {
	
	private String urlArea;
	private String folderField;
	private String imageFolderField;
	private String sizeField;
	private String maxSizeField;
	private String pxHeightField ;
	private String pxWidthField ;
	
	private String ratioFromField ;
	private String ratioToField ;
	
	private boolean autoBox;  //采集完自动处理
	private boolean jpg;
	private boolean jpeg;
	private boolean png;
	private boolean gif;
	private boolean tiff;
	private boolean bmp;
	private boolean required;
	
	private boolean autoSaveBox; //自动保存设置
	
	private String fromField;
	private String toField;
	private String pageParam;
	private static final String FILE_NAME = "setting.json";
	
	private static final String SETTING_KEY = "setting";
	
	private static final Logger logger =  Logger.getLogger(Setting.class);
	
	/*
	 * 用户打开软件，初始化组件的基本参数
	 */
	public  void initWidgets(Widgets widgets,String id ) {
		//初始化到内存中
		File file = new File(FILE_NAME);
		if(!file.exists()) return ;
		String json = FolderUtil.readFileContent(file);
		JSONObject settings =JSONObject.parseObject(json);
		ConfigUtil.setCache(SETTING_KEY, settings.toString()); //初始化设置缓存
		String settingstring = (String) settings.get(id);
		if(settingstring==null){
			return ;
		}
		JSONObject jsonObject =JSONObject.parseObject(settingstring);
		
		Setting setting =  JSONObject.toJavaObject(jsonObject, Setting.class);
		initSetting(widgets, setting);
	}
	
	/*
	 * 保存所有配置信息
	 */
	public void save(Widgets widgets,String id   ) {
		setUrlArea(widgets.getUrlArea().getText());
		setFolderField(widgets.getFolderField().getText());
		setImageFolderField(widgets.getImageFolderField().getText());
		setSizeField(widgets.getSizeField().getText());
		setMaxSizeField(widgets.getMaxSizeField().getText());
		setRatioFromField(widgets.getRatioFromField().getText());
		setRatioToField(widgets.getRatioToField().getText());
		setPxHeightField(widgets.getPxHeightField().getText());
		setPxWidthField(widgets.getPxWidthField().getText());
		setAutoBox(widgets.getAutoBox().isSelected());
		setJpg(widgets.getJpg().isSelected());
		setJpeg(widgets.getJpeg().isSelected());
		setPng(widgets.getPng().isSelected());
		setGif(widgets.getGif().isSelected());
		setTiff(widgets.getTiff().isSelected());
		setBmp(widgets.getBmp().isSelected());
		setRequired(widgets.getRequiredBox().isSelected());
		setAutoSaveBox(widgets.getAutoSaveBox().isSelected());
		setFromField(widgets.getFromField().getText());
		setToField(widgets.getToField().getText());
		setPageParam(widgets.getPageParamValue());
		
		String json = ConfigUtil.getStringConfigByKey(SETTING_KEY); //缓存
		JSONObject setting =null;
		if(StringUtils.isEmpty(json)){
			setting  =  new JSONObject();
		}else{
			setting = JSONObject.parseObject(json);
		}
		setting.put(id, JSONObject.toJSONString(this));
		
		ConfigUtil.setCache(SETTING_KEY,setting.toString());
		FolderUtil.writeFileContent(new File(FILE_NAME), setting.toString());
		JSONObject settingObject =JSONObject.parseObject(setting.toString());
		Setting s =  JSONObject.toJavaObject(settingObject, Setting.class);
		initSetting(widgets ,s);
		/*JSONObject jsonObject =JSONObject.parseObject(json);
		jsonObject.put(id, );
		
		 String json = "";
		if(file.exists()){
			json = FolderUtil.readFileContent(file);
		}else{
			JSONObject settingJson = new JSONObject();
			settingJson.put("common", "test");
			json = settingJson.toString();
		} */


	} 

	//设置所有的组件的参数信息
	public void initSetting(Widgets widgets,Setting setting){
		widgets.getUrlArea().setText(setting.getUrlArea());
		widgets.getFolderField().setText(setting.getFolderField());
		widgets.getImageFolderField().setText(setting.getImageFolderField());
		widgets.getSizeField().setText(setting.getSizeField());
		widgets.getMaxSizeField().setText(setting.getMaxSizeField());
		
		widgets.getRatioFromField().setText(setting.getRatioFromField());
		widgets.getRatioToField().setText(setting.getRatioToField());
		
		widgets.getPxHeightField().setText(setting.getPxHeightField());
		widgets.getPxWidthField().setText(setting.getPxWidthField());
		widgets.getAutoBox().setSelected(setting.isAutoBox());
		boolean filter = ConfigUtil.getBooleanConfigByKey("filter");
		if(!filter){
			widgets.getAutoBox().setEnabled(false);  
		}
		widgets.getJpg().setSelected(setting.isJpg());
		widgets.getJpeg().setSelected(setting.isJpeg());
		widgets.getPng().setSelected(setting.isPng());
		widgets.getGif().setSelected(setting.isGif());
		widgets.getTiff().setSelected(setting.isTiff());
		widgets.getBmp().setSelected(setting.isBmp());
		widgets.getRequiredBox().setSelected(setting.isRequired());
		widgets.getAutoSaveBox().setSelected(setting.isAutoSaveBox());
		widgets.getFromField().setText(setting.getFromField());
		widgets.getToField().setText(setting.getToField());
		widgets.getPageParam().setText(setting.getPageParam());
	}
	
	/*
	 * 清空所有组件的参数
	 */
	public void clear(String id ) {
		File file = new File(FILE_NAME);
		String json = ConfigUtil.getStringConfigByKey(SETTING_KEY); //缓存
		JSONObject setting =  new JSONObject();
		if(!StringUtils.isEmpty(json)){
			setting = JSONObject.parseObject(json);
		}
		setting.remove(id);
		FolderUtil.writeFileContent(file, setting.toString());
	}

	

	public String getUrlArea() {
		return urlArea;
	}


	public void setUrlArea(String urlArea) {
		this.urlArea = urlArea;
	}


	public String getFolderField() {
		return folderField;
	}


	public void setFolderField(String folderField) {
		this.folderField = folderField;
	}


	public String getImageFolderField() {
		return imageFolderField;
	}


	public void setImageFolderField(String imageFolderField) {
		this.imageFolderField = imageFolderField;
	}


	public String getSizeField() {
		return sizeField;
	}


	public void setSizeField(String sizeField) {
		this.sizeField = sizeField;
	}


	public String getPxHeightField() {
		return pxHeightField;
	}


	public void setPxHeightField(String pxHeightField) {
		this.pxHeightField = pxHeightField;
	}


	public String getPxWidthField() {
		return pxWidthField;
	}


	public void setPxWidthField(String pxWidthField) {
		this.pxWidthField = pxWidthField;
	}


	public boolean isAutoBox() {
		return autoBox;
	}


	public void setAutoBox(boolean autoBox) {
		this.autoBox = autoBox;
	}


	public boolean isJpg() {
		return jpg;
	}


	public void setJpg(boolean jpg) {
		this.jpg = jpg;
	}


	public boolean isJpeg() {
		return jpeg;
	}


	public void setJpeg(boolean jpeg) {
		this.jpeg = jpeg;
	}


	public boolean isPng() {
		return png;
	}


	public void setPng(boolean png) {
		this.png = png;
	}


	public boolean isGif() {
		return gif;
	}


	public void setGif(boolean gif) {
		this.gif = gif;
	}


	public boolean isTiff() {
		return tiff;
	}


	public void setTiff(boolean tiff) {
		this.tiff = tiff;
	}


	public boolean isBmp() {
		return bmp;
	}


	public void setBmp(boolean bmp) {
		this.bmp = bmp;
	}


	public boolean isAutoSaveBox() {
		return autoSaveBox;
	}


	public void setAutoSaveBox(boolean autoSaveBox) {
		this.autoSaveBox = autoSaveBox;
	}


	public String getFromField() {
		return fromField;
	}


	public void setFromField(String fromField) {
		this.fromField = fromField;
	}


	public String getToField() {
		return toField;
	}


	public void setToField(String toField) {
		this.toField = toField;
	}


	public String getPageParam() {
		return pageParam;
	}


	public void setPageParam(String pageParam) {
		this.pageParam = pageParam;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getMaxSizeField() {
		return maxSizeField;
	}

	public void setMaxSizeField(String maxSizeField) {
		this.maxSizeField = maxSizeField;
	}

	public String getRatioFromField() {
		return ratioFromField;
	}

	public void setRatioFromField(String ratioFromField) {
		this.ratioFromField = ratioFromField;
	}

	public String getRatioToField() {
		return ratioToField;
	}

	public void setRatioToField(String ratioToField) {
		this.ratioToField = ratioToField;
	}
	
	
}
