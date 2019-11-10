package com.crawler.handler.open.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.entity.DriverEntity;
import com.crawler.entity.OpenUnit;
import com.crawler.entity.Product;
import com.crawler.entity.Storage;
import com.crawler.handler.open.IOpen;
import com.crawler.util.FilterUtil;
import com.crawler.util.HttpUtil;
import com.crawler.util.StringUtils;

public class IkeaAllUrl implements IOpen {

	private static final Logger logger =  Logger.getLogger(IkeaAllUrl.class);
	
	
	/*
	 */
	@Override
	public void init(OpenUnit openUnit, Storage storage,DriverEntity driverEntity) throws Exception {
		
		
		
	}

}
