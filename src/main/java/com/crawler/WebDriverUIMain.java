package com.crawler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.crawler.entity.Item;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.WebBrowerWrap;
import com.crawler.entity.Xpath;
import com.crawler.handler.AnalyseXpath;
import com.crawler.ui.ItemRenderer;
import com.crawler.ui.JLabelUnderLineSimple;
import com.crawler.ui.MJTextField;
import com.crawler.ui.UiUtil;
import com.crawler.ui.Widgets;
import com.crawler.util.ConfigUtil;
import com.crawler.util.ExcelUtil;
import com.crawler.util.FolderUtil;
import com.crawler.util.StringUtils;

public class WebDriverUIMain {

	private static final Logger logger =  Logger.getLogger(WebDriverUIMain.class);
	private static String helpMessage = "使用帮助...";
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		 
		UIManager.put("OptionPane.messageFont", UiUtil.getFont());
	    JPanel setting = new JPanel();
	    setting.setLayout(new FlowLayout(FlowLayout.LEFT,10,15));
	    setting.setName("设置");
	    
	    Widgets widgets  = new Widgets(setting);
	    JWindow window = new JWindow();
	        
		final List<Item> items = getSelectItem() ;
		OpenUnit mainOpenUnit = items.get(0).getXpath().getMainOpen();  //首个主配置
		//默认选择第一个网站作为配置采集
		final WebBrowerWrap webBrower=new WebBrowerWrap(widgets );
		webBrower.init(items.get(0).getXpath());
		setHelpMessage(mainOpenUnit.getHelp());
		final DefaultTableModel model = new DefaultTableModel(new Object[][]{},webBrower.getShowHeaders());
	    final JTable table = new JTable(model);
	    table.getColumnModel().getColumn(0).setMaxWidth(50);
	    table.getColumnModel().getColumn(0).setMinWidth(50);
	    table.setRowHeight(20);
	    
        JPanel contentJPanel = new JPanel();
        contentJPanel.setName("数据信息");
    	
		contentJPanel.setLayout(new   GridLayout(1,1)); 
	    JScrollPane scrollPane = new JScrollPane(table);
	    contentJPanel.add(scrollPane);
		
		Container container=window.getContentPane(); //得到容器
	    URL url = WebDriverUIMain.class.getClass().getResource("/images/login.jpg"); //图片的位置
	    if(url != null){
	      container.add(new JLabel(new ImageIcon(url)),BorderLayout.CENTER);  //增加图片
	    }
	    window.pack(); //窗口适应组件尺寸
	    window.setLocationRelativeTo(null);
	    window.setVisible(true); //显示窗口
	    
		final JFrame jFrame = new JFrame();
		url = WebDriverUIMain.class.getClass().getResource("/images/logo.png");
		ImageIcon icon=new ImageIcon(url);
		jFrame.setIconImage(icon.getImage());
		
		// 设置为边界布局，组件间横向、纵向间距均为5像素
		jFrame.setLayout(new BorderLayout(5, 5));
		jFrame.setFont(UiUtil.getFont());
		JLabel urlLable = new JLabel("采集输入:");
		urlLable.setFont(new Font("黑体",Font.BOLD,16));
		urlLable.setSize(50, 28);
		
		//url输入框
		JPanel urlJPanel=new JPanel(); 
		urlJPanel.setLayout(new GridLayout(1,1)); 
		final JTextArea urlArea=new MJTextField();
		urlArea.setFont(new Font("黑体",Font.PLAIN,15)); 
		urlArea.setLineWrap(true);//激活自动换行功能 
		urlArea.setWrapStyleWord(true);//激活断行不断字功能 
		
	    JScrollPane urlPane=new JScrollPane(urlArea);  
//	     urlPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    urlJPanel.add(urlPane);
	    urlPane.setPreferredSize(new Dimension (725,40)); 
		
		JPanel top = new JPanel();
		top.setPreferredSize(new Dimension (900,115)); 
		top.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));
		
        
        JLabel labelSelect=new JLabel("选择网站:");  
        labelSelect.setFont(new Font("黑体",Font.BOLD,16));
        final JComboBox comboBox=new JComboBox();  
        comboBox.setFont(UiUtil.getFont());
        comboBox.setPreferredSize(new Dimension(300, 30));
        
        for (Item item : items) {
        	comboBox.addItem(item);  
		}
        
        top.add(labelSelect);
        top.add(comboBox);
        
        comboBox.setRenderer(new ItemRenderer());
        comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
			    if (ItemEvent.SELECTED == e.getStateChange()) { 
			    	Item item = (Item) comboBox.getSelectedItem();
			    	setHelpMessage(item.getXpath().getMainOpen().getHelp());
			    	model.setRowCount(0);
			    	model.setColumnCount(0);
			    	
 			    	//重新初始化选项网站的基础信息
			    	try {
						webBrower.reloadInit(item.getXpath());
					} catch (Exception ep) {
						logger.error("重新初始化采集模块:"+ep.getMessage(),ep);
					} 
			    	//添加表头
			    	String[] headers = webBrower.getShowHeaders();
			    	model.setDataVector(null, headers);
				    table.getColumnModel().getColumn(0).setMaxWidth(50);
				    table.getColumnModel().getColumn(0).setMinWidth(50);
				    
                }  
			}
		});
        
		//图片的下载目录选择
		JLabel imageFolderJLabel = new JLabel("下载路径:");
		imageFolderJLabel.setFont(new Font("黑体",Font.BOLD,16));
		JLabelUnderLineSimple help = new JLabelUnderLineSimple("使用帮助");
		help.setFont(new Font("黑体",Font.BOLD,14));
		help.setForeground(Color.BLUE);
		help.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) { }
			@Override
			public void mousePressed(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) { }
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Object[] options = {};
				JOptionPane.showOptionDialog(null, helpMessage, "使用帮助", JOptionPane.CLOSED_OPTION, JOptionPane.CLOSED_OPTION, null, options, null);
			}
		});
		
		
		final JTextField imageFolderField = new JTextField();
		imageFolderField.setFont(UiUtil.getFont());
		imageFolderField.setPreferredSize(new Dimension (332,28)); 
		
		JButton imageSelectButton = new JButton("浏览");
		imageSelectButton.setFont(UiUtil.getFont());
		
		top.add(imageFolderJLabel);
		top.add(imageFolderField);
		top.add(imageSelectButton);
		
		top.add(help);
		
		top.add(urlLable);
		top.add(urlJPanel);
		
        JPanel logJPanel=new JPanel(); 
        logJPanel.setLayout(new GridLayout(1,1)); 
        logJPanel.setBorder(BorderFactory.createTitledBorder( "日志信息"));
        final JTextArea logArea=new JTextArea();//构造一个文本域  
        logArea.setFont(UiUtil.getFont()); 
        logArea.setLineWrap(true);//激活自动换行功能 
        logArea.setWrapStyleWord(true);//激活断行不断字功能 
        logArea.setEditable(false);
        JScrollPane logPane=new JScrollPane(logArea); 
        logJPanel.add(logPane);
        logArea.setSize(200, 500);
        logJPanel.setPreferredSize(new Dimension (230,500)); 
        
        final JButton startButton = new JButton("启动");
        final JButton stopButton = new JButton("停止");
		final JButton exportButton = new JButton("导出");
		
		stopButton.setEnabled(false);
		exportButton.setEnabled(false);
		
    	top.add(startButton);
		top.add(stopButton);
		

		//excel导出的目录选择
		JLabel folderJLabel = new JLabel("EXCEL存至");
		folderJLabel.setFont(new Font("黑体",Font.BOLD,16));
		final JTextField folderField = new JTextField();
		folderField.setFont(UiUtil.getFont());
		folderField.setPreferredSize(new Dimension (722,28)); 
		
		JButton selectButton = new JButton("浏览");
		selectButton.setFont(UiUtil.getFont());
		
//		   UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//		   UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//		String lookAndFeel =UIManager.getSystemLookAndFeelClassName();
  
        JPanel imageJPanel = new JPanel(); 
        imageJPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,15));  
        imageJPanel.setName("图片过滤");
        
        JLabel pxWidth = new JLabel("最小宽度:");
        final JTextField pxWidthField = new JTextField(6);
        pxWidthField.setFont(UiUtil.getFont());
        pxWidth.setFont(UiUtil.getFont());
        
        JLabel pxw = new JLabel("像素");
        JLabel pxh = new JLabel("像素");
        pxw.setFont(UiUtil.getFont());
        pxh.setFont(UiUtil.getFont());
        
        
        JLabel pxHeight = new JLabel("  最小高度:");
        pxHeight.setFont(UiUtil.getFont());
        final JTextField pxHeightField = new JTextField(6);
        pxHeightField.setFont(UiUtil.getFont());
        
        JLabel size = new JLabel("大小范围:");
        size.setFont(UiUtil.getFont());
        JLabel m = new JLabel("至");
        m.setFont(UiUtil.getFont());
        final JTextField sizeField = new JTextField(6);
        sizeField.setFont(UiUtil.getFont());
        
        size.setFont(UiUtil.getFont());
        JLabel bm = new JLabel("KB");
        bm.setFont(UiUtil.getFont());
        final JTextField bigSizeField = new JTextField(6);
        bigSizeField.setFont(UiUtil.getFont());
        
        //宽高比例
        JLabel ratio = new JLabel("宽/高比例范围:");
        ratio.setFont(UiUtil.getFont());
        JLabel ratioLabel = new JLabel("至");
        ratioLabel.setFont(UiUtil.getFont());
        final JTextField ratioFromField = new JTextField(6);
        ratioFromField.setFont(UiUtil.getFont());
        final JTextField ratioToField = new JTextField(6);
        ratioToField.setFont(UiUtil.getFont());
        
        
        final JButton filerButton = new JButton("过滤");
        filerButton.setFont(UiUtil.getFont());
        
//        JLabel blank2 = new JLabel("");
//        blank2.setPreferredSize(new Dimension(50, 30));
//        blank2.setVisible(true);
        
        imageJPanel.add(pxWidth);
        imageJPanel.add(pxWidthField);
        imageJPanel.add(pxw);
        imageJPanel.add(pxHeight);
        imageJPanel.add(pxHeightField);
        imageJPanel.add(pxh);
        imageJPanel.add(size);
        imageJPanel.add(sizeField);
        imageJPanel.add(m);
        
        imageJPanel.add(bigSizeField);
        imageJPanel.add(bm);
        
        imageJPanel.add(ratio);
        imageJPanel.add(ratioFromField);
        imageJPanel.add(ratioLabel);
        imageJPanel.add(ratioToField);
        
        //图片格式
        JLabel imgFormat = new JLabel("  格式:");
        imgFormat.setFont(UiUtil.getFont());
        final JCheckBox jpg = new JCheckBox("jpg");
        final JCheckBox jpeg = new JCheckBox("jpeg");
        final JCheckBox png = new JCheckBox("png");
        final JCheckBox gif = new JCheckBox("gif");
        final JCheckBox tiff = new JCheckBox("tiff");
        final JCheckBox bmp = new JCheckBox("bmp");
        
        imageJPanel.add(imgFormat);
        imageJPanel.add(jpg);
        imageJPanel.add(jpeg);
        imageJPanel.add(png);
        imageJPanel.add(gif);
        imageJPanel.add(tiff);
        imageJPanel.add(bmp);
        JLabel remark = new JLabel("备注:过滤出符合上述条件的图片,下载路径作为图片过滤的路径. ");
        remark.setFont(new Font("黑体",Font.PLAIN,15));
        
        JLabel autoJLabel = new JLabel("下载完自动过滤:");
        autoJLabel.setFont(UiUtil.getFont());
        JCheckBox autoBox = new JCheckBox();

        imageJPanel.add(remark);
        imageJPanel.add(autoJLabel);
        imageJPanel.add(autoBox);
        imageJPanel.add(filerButton);
        
        JLabel page = new JLabel("    分页参数:");
        page.setFont(UiUtil.getFont());
        final JTextField pageField = new JTextField(10);
        pageField.setFont(UiUtil.getFont());
        
        JLabel  fromJLabel = new JLabel("  从");
        fromJLabel.setFont(UiUtil.getFont());
        
        final JTextField fromField = new JTextField(10);
        fromField.setFont(UiUtil.getFont());
        
        JLabel zhiJLabel = new JLabel("至");
        zhiJLabel.setFont(UiUtil.getFont());
        
        final JTextField toField = new JTextField(10);
        toField.setFont(UiUtil.getFont());
        
        JLabel pageJLabel = new JLabel("页");
        pageJLabel.setFont(UiUtil.getFont());
        
        pageJLabel.setFont(UiUtil.getFont());
        final JCheckBox requiredBox = new JCheckBox("必填");
        requiredBox.setFont(UiUtil.getFont());
        
        JLabel autoSave = new JLabel("保存全局参数:");
        autoSave.setFont(UiUtil.getFont());
        JCheckBox autoSaveBox = new JCheckBox();
        setting.add(autoSave);
        setting.add(autoSaveBox);
        
        setting.add(page);
        setting.add(pageField);
        setting.add(fromJLabel);
        setting.add(fromField);
        setting.add(zhiJLabel);
        setting.add(toField);
        setting.add(pageJLabel);
        setting.add(requiredBox);
        
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setFont(UiUtil.getFont());
        jTabbedPane.add(contentJPanel);
        jTabbedPane.add(imageJPanel);
        jTabbedPane.add(setting);
        jTabbedPane.setSelectedIndex(0);
        
	    widgets.init(autoSaveBox,table,logArea, model, urlArea, folderField,startButton,exportButton,stopButton,imageFolderField,pageField,
	    		fromField,toField,filerButton,autoBox, sizeField,pxHeightField,pxWidthField,jpg,jpeg,png,gif,tiff,bmp,requiredBox,
	    		bigSizeField,ratioFromField,ratioToField
	    		);

		final JFileChooser jfc = new JFileChooser();
		selectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 jfc.setFileSelectionMode(1);// 设定只能选择到文件夹  
		            int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句  
		            if (state == 1) {
		                return;  
		            } else {
		                File f = jfc.getSelectedFile();// f为选择到的目录  
		                folderField.setText(f.getAbsolutePath());  
		            }  
			}
		});
		
		imageSelectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jfc.setFileSelectionMode(1);// 设定只能选择到文件夹  
				int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句  
				if (state == 1) {
					return;  
				} else {
					File f = jfc.getSelectedFile();// f为选择到的目录  
					imageFolderField.setText(f.getAbsolutePath());  
				}  
			}
		});
		
		
		exportButton.setFont(UiUtil.getFont());
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				webBrower.showLogArea("导出中!");
				exportButton.setEnabled(false);
				webBrower.export();
				exportButton.setEnabled(true);
				webBrower.showLogArea("导出完毕!");
			}
		});
		
		startButton.setFont(UiUtil.getFont());
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopButton.setEnabled(true);
				startButton.setEnabled(false);
				exportButton.setEnabled(false);
				//*********开启运行浏览器*********//
				webBrower.start();
			}
		});
		

		//图片过滤
		filerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				OpenUnit mainOpenUnit = webBrower.getMainOpenUnit();
				if(!mainOpenUnit.isFilter()){ //未开启就不给过滤
					JOptionPane.showMessageDialog(null,"未开启过滤功能，请联系店家！", "提示 " ,JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				
				
				if(StringUtils.isEmpty(imageFolderField.getText())){
					JOptionPane.showMessageDialog(null,"请选择需要过滤的图片路径！", "提示 " ,JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
 
				String width = pxWidthField.getText();
				if(!StringUtils.isEmpty(width) && !ExcelUtil.isNumeric(width)){
					JOptionPane.showMessageDialog(null,"请输入正确的整数的最小宽度！", "提示 " ,JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				
				String height = pxHeightField.getText();
				if(!StringUtils.isEmpty(height) && !ExcelUtil.isNumeric(height)){
					JOptionPane.showMessageDialog(null,"请输入正确的整数的最小高度！", "提示 " ,JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				
				String sizeImage = sizeField.getText();
				if(!StringUtils.isEmpty(sizeImage) && !ExcelUtil.isNumeric(sizeImage)){
					JOptionPane.showMessageDialog(null,"请输入正确的整数的大小！", "提示 " ,JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				
				webBrower.showLogArea("正在过滤图片。。。");
				webBrower.getEnv().setStop(false);
				webBrower.handerImage();
				filerButton.setEnabled(false);
			}
		});
		
		stopButton.setFont(UiUtil.getFont());
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				startButton.setEnabled(true);
				stopButton.setEnabled(false);
				exportButton.setEnabled(true);
				webBrower.stop();
				webBrower.showLogArea("正在停止！");
			}
		});
	
		top.add(folderJLabel);
		top.add(folderField);
		top.add(selectButton);
		top.add(exportButton);

		//按钮占位置
	/*	JLabel white=new JLabel("");
		white.setPreferredSize(new Dimension(100, 30));
		white.setVisible(true);
		top.add(white);*/

		
		jFrame.getContentPane().add("North", top); // 将按钮添加到窗口中
		jFrame.getContentPane().add("Center",jTabbedPane);
		jFrame.getContentPane().add("East", logJPanel);
		
//		jFrame.getContentPane().add("West", logJPanel ); //logJPanel
//		jFrame.getContentPane().add("South", new JButton("South"));

		//右上角的叉叉关闭
		jFrame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent arg0) {

				webBrower.showLogArea("正在退出，请稍等。。。");
				if( !webBrower.getEnv().isStop()) {
					int result = JOptionPane.showConfirmDialog(null, "未采集完毕,是否确定退出?", "退出提示", 0);
					if(result==0){
						webBrower.quit();
						jFrame.dispose();
					}else{
						return ;  //不退出
					}
				}else{
					webBrower.quit();
					jFrame.dispose();
				}
				System.exit(0);
			}
		});
		
		webBrower.initWidgets(mainOpenUnit.getId());
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		Integer jframeWidth = (int) (screenSize.getHeight()*0.8>800 ? 800 : screenSize.getHeight()*0.8) ;
//		jFrame.setTitle("【金蚁软件】仅作学习研究之用，严禁于非法用途！ ");
		jFrame.setTitle("【金蚁软件】仅作学习研究之用，严禁于非法用途！ 微信:zhcsoftware");
//		jFrame.setTitle(" 欢迎加入 青岛宏景数字科技  qq 49932388");
		jFrame.pack();
		jFrame.setSize(985, jframeWidth);
		jFrame.setResizable(false);// 禁止用户改变窗体大小
//		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//		jFrame.setLocationRelativeTo(null); // 让窗体居中显示
		int width = (int) ((screenSize.getWidth()-1050)/2) ;
//		int width = (int) ((screenSize.getWidth()-1050)/2 >520 ? (screenSize.getWidth()-1050)/2 : 520);      //显示居于相对屏幕右边位置
		int height =  (int) (((screenSize.getHeight()-800)/2) <=50 ? 50 : ((screenSize.getHeight()-800)/2)); //显示居于相对屏幕顶部位置
		jFrame.setLocation(width,height);
		
		window.dispose();  //启动时的界面图片显示
		
		jFrame.setVisible(true);  //显示运行的主程序
	 
	}
	
	/*
	 * 下拉框信息读取
	 */
	public static List<Item> getSelectItem() throws Exception {
		File dir = new File("config");
		File[] files = dir.exists() ? FolderUtil.getFolderFiles(dir)  : new File("encrypt").listFiles();
		
		List<Xpath> mainXpaths = new ArrayList<>();
		List<Xpath> detailXpaths = new ArrayList<>();
		List<Item> items = new ArrayList<>();
		Set<String> ids  = new  HashSet<String>();
		OpenUnit openUnit  =null;
		for (File file : files) {
			if(!file.getName().endsWith(".yml")){
				continue;
			}
			System.out.println(file.getName());
			if(file.getPath().startsWith("config")){
				openUnit = new AnalyseXpath(file.getPath().substring(6)).parseConfig();
			}else{
				openUnit = new AnalyseXpath(file.getName()).parseConfig();
			}
			if(openUnit.isMajor()){
				if(!StringUtils.isEmpty(openUnit.getId()) && ids.contains(openUnit.getId())){
	    			logger.error(openUnit.getId()+"存在重复的id"+file.getName());
	    			return null;
	    		}else{
	    			ids.add(openUnit.getId());
	    		}
				mainXpaths.add(new Xpath(openUnit));
			}else{
				detailXpaths.add(new Xpath(openUnit));
			}
		}
		
	  	//主配置对应的子配置
		for (Xpath main : mainXpaths) {
			String id = main.getId();
			for (Xpath detail : detailXpaths) {
				if(id!=null && id.equals(detail.getId())){
					 main.setDetailOpen(detail.getMainOpen());
				}
			}
			
		} 
		
	 	for (Xpath xpath : mainXpaths) {
	 		items.add(new Item(xpath));
	 		if(xpath.getMainOpen().isFilter()){
	 			ConfigUtil.setCache("filter", "true");
	 		}
		}
	 	
		return items;
	}
	
	private  static void setHelpMessage(String message){
		helpMessage = message;
	}
	
}