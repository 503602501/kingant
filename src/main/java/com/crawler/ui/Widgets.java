package com.crawler.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.crawler.entity.ImageProperty;
import com.crawler.util.ExcelUtil;
import com.crawler.util.StringUtils;

/*
 * 控件
 */
public class Widgets {

	private JPanel setting ;
	private JTextArea logArea;
	private List<JTextField> inputFields;
	private List<Component> inputComponents ; 
	private JTextArea urlArea;
	private JTextField folderField;
	private JTextField imageFolderField;
	private JButton startButton;
	private JButton exportButton;
	private JButton filerButton;
	private JButton stopButton;
	private DefaultTableModel model; 
	private JTextField fromField;
	private JTextField toField;
	private JTextField pageParam;
	private JCheckBox autoBox;
	private JTextField sizeField;
	private JTextField maxSizeField;
	private JTextField pxHeightField ;
	private JTextField pxWidthField ;
	private JTextField ratioFromField ;
	private JTextField ratioToField ;
	private JCheckBox jpg;
	private JCheckBox jpeg;
	private JCheckBox png;
	private JCheckBox gif;
	private JCheckBox tiff;
	private JCheckBox bmp;
	private JCheckBox autoSaveBox;
	private JCheckBox requiredBox;
	private JTable table;
	
	public Widgets(JPanel setting) {
		this.setting = setting ;
		this.inputComponents = new ArrayList<>();
		this.inputFields = new ArrayList<>();
	}	
	
	public void init(JCheckBox autoSaveBox,JTable table,JTextArea logArea,DefaultTableModel model ,JTextArea urlArea,JTextField folderField, JButton startButton,
			JButton exportButton,JButton stopButton,JTextField imageFolderField,JTextField pageParam,JTextField fromField,
			JTextField toField,JButton filerButton,JCheckBox autoBox,JTextField sizeField,JTextField pxHeightField ,
			JTextField pxWidthField ,JCheckBox jpg  ,JCheckBox jpeg ,JCheckBox png ,JCheckBox gif ,JCheckBox tiff ,
			JCheckBox bmp ,JCheckBox requiredBox,
			JTextField maxSizeField,JTextField ratioFromField,JTextField ratioToField
			) {
 
		this.autoSaveBox = autoSaveBox ; 
		this.table = table ; 
		this.logArea = logArea ; 
		this.model = model ; 
		this.urlArea = urlArea ; 
		this.folderField = folderField ; 
		this.imageFolderField = imageFolderField ; 
		this.startButton = startButton ; 
		this.exportButton = exportButton ; 
		this.stopButton = stopButton ; 
		this.pageParam = pageParam ; 
		this.fromField = fromField ; 
		this.toField = toField ; 
		this.filerButton = filerButton ; 
		this.autoBox = autoBox;
		this.sizeField = sizeField; //最小大小
		this.maxSizeField = maxSizeField;
		this.pxHeightField = pxHeightField;
		this.pxWidthField = pxWidthField;
		
		this.ratioFromField = ratioFromField;
		this.ratioToField = ratioToField;
		
		this.jpg = jpg;
		this.jpeg = jpeg;
		this.png = png;
		this.gif = gif;
		this.tiff = tiff;
		this.bmp = bmp;
		this.requiredBox = requiredBox;
	}
	
	public JTextArea getLogArea() {
		return logArea;
	}
	public void setLogArea(JTextArea logArea) {
		this.logArea = logArea;
	}
	 
	public JTextArea getUrlArea() {
		return urlArea;
	}
	public void setUrlArea(JTextArea urlArea) {
		this.urlArea = urlArea;
	}

	public JTextField getFolderField() {
		return folderField;
	}

	public void setFolderField(JTextField folderField) {
		this.folderField = folderField;
	}

	public JButton getStartButton() {
		return startButton;
	}

	public void setStartButton(JButton startButton) {
		this.startButton = startButton;
	}

	public JButton getExportButton() {
		return exportButton;
	}

	public void setExportButton(JButton exportButton) {
		this.exportButton = exportButton;
	}

	public JButton getStopButton() {
		return stopButton;
	}

	public void setStopButton(JButton stopButton) {
		this.stopButton = stopButton;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public void setModel(DefaultTableModel model) {
		this.model = model;
	}

	public JTextField getImageFolderField() {
		return imageFolderField;
	}

	public void setImageFolderField(JTextField imageFolderField) {
		this.imageFolderField = imageFolderField;
	}

	public Integer getFromFieldValue() {
		if(StringUtils.isEmpty(fromField.getText())){
			return 1;
		}
		return ExcelUtil.converInteger(fromField.getText());
	}

	public void setFromField(JTextField fromField) {
		this.fromField = fromField;
	}

	public Integer getToFieldValue() {
		if(StringUtils.isEmpty(toField.getText())){
			return 100;  // 默认此埃及100页
		}
		
		return ExcelUtil.converInteger(toField.getText());
	}

	public void setToField(JTextField toField) {
		this.toField = toField;
	}

	public String getPageParamValue() {
		if(StringUtils.isEmpty(pageParam.getText())){
			return "";
		}
		return pageParam.getText().trim();
	}

	public void setPageParam(JTextField pageParam) {
		this.pageParam = pageParam;
	}

	public void clickFilerButton() {
		filerButton.doClick();
	}

	public void setFilerButton(JButton filerButton) {
		this.filerButton = filerButton;
	}

	public JCheckBox getAutoBox() {
		return autoBox;
	}

	public void setAutoBox(JCheckBox autoBox) {
		this.autoBox = autoBox;
	}

	public JButton getFilerButton() {
		return filerButton;
	}
	
	public ImageProperty getImageProperty() {
		ImageProperty image = new ImageProperty();
		String sizeImage = sizeField.getText();
		String maxSizeImage = maxSizeField.getText();
		String width = pxWidthField.getText();
		String height = pxHeightField.getText();
		
		//最小大小
		if( !StringUtils.isEmpty(sizeImage)){
			image.setMinSize(ExcelUtil.converInteger(sizeImage));
		}
		//最大的大小
		if( !StringUtils.isEmpty(maxSizeImage)){
			image.setMaxSize(ExcelUtil.converInteger(maxSizeImage));
		}
		
		//最小的比例
		String ratioFrom  = ratioFromField.getText() ;
		if( !StringUtils.isEmpty(ratioFrom)){
			image.setRatioFromField(ExcelUtil.converFloat(ratioFrom));
		}
		
		//最大的比例
		String ratioTo  = ratioToField.getText() ;
		if( !StringUtils.isEmpty(ratioTo)){
			image.setRatioToField(ExcelUtil.converFloat(ratioTo));
		}
		
		if( !StringUtils.isEmpty(height)){
			image.setMinHeight(ExcelUtil.converInteger(height));
		}
		if( !StringUtils.isEmpty(width)){
			image.setMinWidth(ExcelUtil.converInteger(width));
		}
		
	    StringBuffer allImageFormat = new StringBuffer(); //所有的图片格式
        if(jpg.isSelected()){
        	allImageFormat.append("."+jpg.getText());
        }
        if(jpeg.isSelected()){
        	allImageFormat.append("."+jpeg.getText());
        }
        if(png.isSelected()){
        	allImageFormat.append("."+png.getText());
        }
        if(gif.isSelected()){
        	allImageFormat.append("."+gif.getText());
        }
        if(tiff.isSelected()){
        	allImageFormat.append("."+tiff.getText());
        }
        if(bmp.isSelected()){
        	allImageFormat.append("."+bmp.getText());
        }
	    /**************以上为所有的图片格式**************/
		image.setSuffix(allImageFormat.toString());

		return image ;
	}
	
	/*
	 *初始化用户定制的输入 
	 */
	public void initInputs(List<String> labels) {
		
		//清理之前的用户定制信息
		for (Component component : inputComponents) {
			this.setting.remove(component);
		}
		inputComponents.clear();
		inputFields.clear();
		
	       /*******用户的特殊定制需求输入框********/
        for (String name : labels) {
        	JLabel label = new JLabel(name+":");
        	label.setFont(UiUtil.getFont());
        	
        	JTextField inputField = new JTextField(10);
        	inputField.setFont(UiUtil.getFont());
        	
        	JLabel blank = new JLabel("  ") ;
        	
        	this.setting.add(label);
        	this.setting.add(inputField);
        	this.setting.add(blank);
        	
        	inputComponents.add(label);
        	inputComponents.add(inputField);
        	inputComponents.add(blank);
        	
        	inputFields.add(inputField); //用户定制输入
		}
        /*******用户的特殊定制需求输入框********/
        
	}
	
	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JTextField getSizeField() {
		return sizeField;
	}

	public void setSizeField(JTextField sizeField) {
		this.sizeField = sizeField;
	}

	public JTextField getPxHeightField() {
		return pxHeightField;
	}

	public void setPxHeightField(JTextField pxHeightField) {
		this.pxHeightField = pxHeightField;
	}

	public JTextField getPxWidthField() {
		return pxWidthField;
	}

	public void setPxWidthField(JTextField pxWidthField) {
		this.pxWidthField = pxWidthField;
	}

	public JCheckBox getJpg() {
		return jpg;
	}

	public void setJpg(JCheckBox jpg) {
		this.jpg = jpg;
	}

	public JCheckBox getJpeg() {
		return jpeg;
	}

	public void setJpeg(JCheckBox jpeg) {
		this.jpeg = jpeg;
	}

	public JCheckBox getPng() {
		return png;
	}

	public void setPng(JCheckBox png) {
		this.png = png;
	}

	public JCheckBox getGif() {
		return gif;
	}

	public void setGif(JCheckBox gif) {
		this.gif = gif;
	}

	public JCheckBox getTiff() {
		return tiff;
	}

	public void setTiff(JCheckBox tiff) {
		this.tiff = tiff;
	}

	public JCheckBox getBmp() {
		return bmp;
	}

	public void setBmp(JCheckBox bmp) {
		this.bmp = bmp;
	}

	public JCheckBox getAutoSaveBox() {
		return autoSaveBox;
	}

	public void setAutoSaveBox(JCheckBox autoSaveBox) {
		this.autoSaveBox = autoSaveBox;
	}

	public JTextField getFromField() {
		return fromField;
	}

	public JTextField getToField() {
		return toField;
	}

	public JTextField getPageParam() {
		return pageParam;
	}

	public boolean isRequiredBox() {
		return requiredBox.isEnabled();
	}

	public void setRequiredBox(JCheckBox requiredBox) {
		this.requiredBox = requiredBox;
	}

	public JCheckBox getRequiredBox() {
		return requiredBox;
	}

	public List<JTextField> getInputFields() {
		return inputFields;
	}

	public void setInputFields(List<JTextField> inputFields) {
		this.inputFields = inputFields;
	}

	public JTextField getMaxSizeField() {
		return maxSizeField;
	}

	public void setMaxSizeField(JTextField maxSizeField) {
		this.maxSizeField = maxSizeField;
	}

	public JTextField getRatioFromField() {
		return ratioFromField;
	}

	public void setRatioFromField(JTextField ratioFromField) {
		this.ratioFromField = ratioFromField;
	}

	public JTextField getRatioToField() {
		return ratioToField;
	}

	public void setRatioToField(JTextField ratioToField) {
		this.ratioToField = ratioToField;
	}

	public JPanel getSetting() {
		return setting;
	}

	public void setSetting(JPanel setting) {
		this.setting = setting;
	}

 
}
