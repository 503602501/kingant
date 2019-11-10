package com.crawler.ui;

import java.awt.Font;

import javax.swing.JTextArea;

public class UiUtil {
	
	/*
	 * 用户界面日志信息域
	 */
	private static JTextArea jTextArea;  
	
	public JTextArea getjTextArea() {
		return jTextArea;
	}

	public static void setLogArea(JTextArea textArea) {
		jTextArea = textArea;
	}

	public static void showLogArea(String message) {
		jTextArea.append(message+"\r\n");
		jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
	}
	
	public static Font getFont() {
		return new Font("黑体",Font.PLAIN,16); 
	}
	
	
}
