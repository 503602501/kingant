package com.crawler.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.crawler.entity.StoreData;
import com.crawler.entity.Vcard;

public class ExcelUtil {

	private static final Logger logger =  Logger.getLogger(ExcelUtil.class);
	private static int threshold = 60000 ;       //excel页面的阀值
	
	public static String FOLDER_PICTURE = "picture";      
	
	private static String FOLDER_CREATE = "create";       
	
	private static String FOLDER_READ = "read" ;        
	
	private static String FOLDER_ERROR = "error" ;       
	
	private static String EXCEL_2003 = ".xls";  
	
	private static String EXCEL_2007= ".xlsx";   
	
	private static String CREATE_PREFIX = ".xlsx";  //创建excel的版本
	
	private static Integer STORE_DATA_INIT_SIZE = 10000;
	
//	private static List readLabels ;//  Arrays.asList(ConfigUtil.getConfigByKey("read.label.name").toString().split(","));
	
	/**
	 * 创建2007版Excel文件
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void create2007Excel(ArrayList<List> list ,List header,String folder) throws FileNotFoundException,
			Exception {
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
		
		FileOutputStream os =null;
		XSSFRow row = null;
		XSSFCell cell = null;
		List<String> rowData = null;  
		String cellString = null;
		try {
			for (int i = 0; i <= list.size(); i++) {

				row = sheet.createRow(i);// 创建一个行对象
				if(i==0){
					rowData = header;
				}else{
					rowData = list.get(i-1);
				}
				
				for (int j = 0; j < rowData.size(); j++) {
					cell=row.createCell(j);// 创建单元格
					cellString = rowData.get(j);
					/*if(NumberUtil.isInteger(cellString)){
						cell.setCellValue(new Integer(cellString));
					}else{
						cell.setCellValue(cellString);
					}*/
					
					cell.setCellValue(cellString);
				}
			}
		
			os = new FileOutputStream(createFile(".xlsx",folder));
			
			workBook.write(os);// 将文档对象写入文件输出流
			logger.info("创建成功 office 2007 excel");
			
		} catch (Exception e) {
			logger.error("2007excel创建异常"+e.getMessage());
			throw new Exception(e.getMessage());
		}finally{
			if(os!=null){
				os.close();// 关闭文件输出流
			}
		}
	}
	
	/**
	 * 创建2007版多个sheet的同一个excel
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void create2007SheetsExcel(List<StoreData> storeDatas,String folder) throws FileNotFoundException,
	IOException {
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = null;
		
		FileOutputStream os =null;
		XSSFRow row = null;
		XSSFCell cell = null;
		List<String> rowData = null;  
		List<List> list = null;
		try {
			for (StoreData storeData : storeDatas) {
				if(storeData.getSheet()==null){
					storeData.setSheet("默认sheet");
				}
				
				sheet = workBook.createSheet(storeData.getSheet());// 创建一个工作薄对象
				list = storeData.getList();
				for (int i = 0; i <= list.size(); i++) {
					
					row = sheet.createRow(i);// 创建一个行对象
					if(i==0){
						rowData = storeData.getHeader();
					}else{
						rowData = list.get(i-1);
					}
					
					for (int j = 0; j < rowData.size(); j++) {
						cell=row.createCell(j);// 创建单元格
						cell.setCellValue(rowData.get(j));
					}
				}
			}
			
			os = new FileOutputStream(createFile(".xlsx",folder));
			
			workBook.write(os);// 将文档对象写入文件输出流
			logger.info("创建成功 office 2007 excel");
			
		} catch (Exception e) {
			logger.error("2007excel创建异常"+e.getMessage());
		}finally{
			if(os!=null){
				os.close();// 关闭文件输出流
			}
		}
	}

	
	public static File createFile(String prefix,String folder){
		
		
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd-HHmmss" );
		String fileName = sdf.format(new Date())+prefix;
		
		if(ConfigUtil.getBooleanConfigByKey(ConfigUtil.EXCEL_COVER)){
			fileName="数据结果"+prefix;
		}
		
		String path = null;
		if(!Arrays.asList(FOLDER_PICTURE,FOLDER_CREATE,FOLDER_ERROR,FOLDER_READ).contains(folder)){ 
			path = folder ; //用户配置的输出路径
		}else{
			path =System.getProperty("user.dir")+File.separator+"handler"+File.separator+folder;
		}
		
		File file = new File(path);
		if(!file.exists()){  //目录是否存在
			file.mkdirs();
		}
		path+=File.separator+fileName;
		
		return new File(path);
	}
	
	public static void createExcel(StoreData storeData,String path) throws Exception{
		List<String> filter = new ArrayList<>();
		List<List> showList = new ArrayList<>();
		List<Integer> showIndexs= new ArrayList<Integer>(); //存储需要显示的列
		for (int i = 0; i <storeData.getHeader().size(); i++) {
			if(storeData.getShowHeaders().contains(storeData.getHeader().get(i))){
				showIndexs.add(i);
			}
		}
		
		String filterRow = "";
		for (List row : storeData.getList()) {
			List<String> newRow = new ArrayList<>();
			for (Integer i : showIndexs) {
				newRow.add((String) row.get(i));
			}
			if(ConfigUtil.getBooleanConfigByKey(ConfigUtil.FILTER_DUPLICATE_DATA) ){ //过滤重复的行数据
				filterRow =org.apache.commons.lang3.StringUtils.join(newRow, ","); 
				if(!filter.contains(filterRow)){
					showList.add(newRow);
					filter.add(filterRow);
				}
			}else{
				showList.add(newRow);
			}
			
		}
		List<String> headers = new ArrayList<>();
		for (String header : storeData.getShowHeaders()) {
			headers.add(header.replace("文件夹名-", ""));
		}
		
		createExcel(showList, headers, path);
	}
	
	public static void createExcel(StoreData storeData) throws Exception{
		createExcel(storeData.getList(), storeData.getHeader(), FOLDER_CREATE);
	}
	
	public static void createSheetsExcel(List<StoreData> storeDatas) throws Exception{
		create2007SheetsExcel(storeDatas, FOLDER_CREATE);
	}
	
	public static void createErrorExcel(List<List> list ,List header) throws Exception{
		if(list.size()==0){
			logger.error("没有采集到数据!");
			return ; // 没有一次数据就不用啦
		}
		createExcel(list, header, FOLDER_ERROR);
	}
	
	/*
	 * 创建excel
	 */
	private static void createExcel(List<List> data ,List header,String folder) throws Exception{
		
		ArrayList<List> list = new ArrayList<List>(data);
		ArrayList<List> copy = new ArrayList<List>();
		int loop = list.size()/threshold ; 
		for (int i = 0; i <= loop; i++) {
			int end = list.size()<threshold ? list.size() : threshold ;
			copy.addAll(list.subList(0, end));
			list.subList(0, end).clear();
			if( CREATE_PREFIX.endsWith(EXCEL_2003)){ //2003
				create2003Excel(copy, header,folder);
			}else{
				create2007Excel(copy, header,folder);
			}
			copy.clear();
			Thread.sleep(1000);
		}
	}
	
	/**
	 * 对外提供读取excel 的方法
	 * 读取read的目录下的excel
	 * @param path excel所在的目录
	 * @throws Exception 
	 * @throws ExecutionException 
	 */
	public static List<StoreData> readExcel(String path) throws ExecutionException, Exception {
		
		List<StoreData> storeDatas = new ArrayList<>();
		File file = new File(path);
		boolean flag = true;
		if(!file.exists()){
			logger.error("不存在读取的excel目录:"+path);
			return null;
		}
		
		StoreData storeData = null;
		for (File item : file.listFiles()) {
			if(item.getName().endsWith(EXCEL_2003)){
				flag = false ;
				storeData= read2003Excel(item);
			}else if(item.getName().endsWith(EXCEL_2007)){
				flag = false ;
				storeData= read2007Excel(item);
			}else{
				continue;
			}
				
			
			if(storeData.getList().size()==0){
				continue;
			}
			storeDatas.add(storeData);
		}
		
		if(flag){
			logger.error(path+"路径下没有可读取的文件");
		}
		return storeDatas;
		
	}
	
	
	/**
	 * 创建2003版本的Excel文件
	 */
	private static void create2003Excel(ArrayList<List> list ,List header,String folder) throws FileNotFoundException,
			IOException {
		FileOutputStream os = null;
		HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
		HSSFSheet sheet = workBook.createSheet() ; //workBook.createSheet("sheet1");// 创建一个工作薄对象
		try {
			List<String> rowData = null;  
			HSSFRow row  = null;  //行
			HSSFCell cell = null; //单元格
			for (int i = 0; i <= list.size(); i++) {

				row = sheet.createRow(i);// 创建一个行对象
				if(i==0){
					rowData = header;
				}else{
					rowData = list.get(i-1);
				}
				
				for (int j = 0; j < rowData.size(); j++) {
					cell=row.createCell(j);// 创建单元格
					cell.setCellValue(rowData.get(j));
				}
			}
			os = new FileOutputStream(createFile(".xls",folder));
			workBook.write(os); // 将文档对象写入文件输出流
			logger.info("创建成功 office 2003 excel");
		} catch (Exception e) {
			logger.error("生成2003excel异常"+e.getMessage());
		}finally{
			if(os!=null){
				os.close();// 关闭文件输出流
			}
		}
	}


	/**
	 * 读取 office 2003 excel
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static StoreData read2003Excel(File file)
			throws IOException {
		List<String> headerLabels = new ArrayList<>(); //表头
		List<List> list = new LinkedList<List>();
		FileInputStream fis = new FileInputStream(file) ; 
		HSSFWorkbook hwb = new HSSFWorkbook(fis);
		HSSFSheet sheet = hwb.getSheetAt(0);
		String value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		ArrayList<String> rowData=null;
		Integer headers = (int) sheet.getRow(0).getLastCellNum(); //头部长度
		
		for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);  //行数据
			if (row == null) {
				logger.info("行为空:"+i);
				continue;
			}
			
			rowData = new ArrayList<>();
			for (int j = 0; j < headers; j++) {
				cell = row.getCell(j);
				if(cell==null){
					value = "";
				}else{
					value = getCellValue(cell);
				}
				
				if(i==0){ //表头处理
					headerLabels.add(value);
				}else{ //内容数据处理
					rowData.add(value);
				}
			}
			if(rowData.size()!=0 && !StringUtils.isEmpty(org.apache.commons.lang3.StringUtils.join(rowData, ""))){
				list.add(rowData);
			}
		}
		
		fis.close();
		
		StoreData storeData = new StoreData(STORE_DATA_INIT_SIZE);
		storeData.setList(list);
		storeData.setHeader(headerLabels);
		storeData.setShowHeaders(headerLabels);
		return storeData;
	}

	private static String getCellValue(HSSFCell cell){
		
		String value =null;
		switch (cell.getCellType()) {
	      case HSSFCell.CELL_TYPE_NUMERIC: // 数字
	          //如果为时间格式的内容
	          if (HSSFDateUtil.isCellDateFormatted(cell)) {      
	             //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
	             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	             value=sdf.format(HSSFDateUtil.getJavaDate(cell.
	             getNumericCellValue())).toString();                                 
	               break;
	           } else {
	               value = new DecimalFormat("0").format(cell.getNumericCellValue());
	           }
	          break;
	      case HSSFCell.CELL_TYPE_STRING: // 字符串
	          value = cell.getStringCellValue();
	          break;
	      case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
	          value = cell.getBooleanCellValue() + "";
	          break;
	      case HSSFCell.CELL_TYPE_FORMULA: // 公式
	          value = cell.getCellFormula() + "";
	          break;
	      case HSSFCell.CELL_TYPE_BLANK: // 空值
	          value = "";
	          break;
	      case HSSFCell.CELL_TYPE_ERROR: // 故障
	          value = "非法字符";
	          break;
	      default:
	          value = "未知类型";
	          break;
		}
		return value;
	}
	
	public static String getCellValue(XSSFCell cell) {
        Object o = null;
        int cellType = cell.getCellType();
        switch (cellType) {
        case XSSFCell.CELL_TYPE_BLANK:
            o = "";
            break;
        case XSSFCell.CELL_TYPE_BOOLEAN:
            o = cell.getBooleanCellValue();
            break;
        case XSSFCell.CELL_TYPE_ERROR:
            o = "Bad value!";
            break;
        case XSSFCell.CELL_TYPE_NUMERIC:
            o = getValueOfNumericCell(cell);
            break;
        case XSSFCell.CELL_TYPE_FORMULA:
            try {
                o = getValueOfNumericCell(cell);
            } catch (IllegalStateException e) {
                try {
                    o = cell.getRichStringCellValue().toString();
                } catch (IllegalStateException e2) {
                    o = cell.getErrorCellString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        default:
            o = cell.getRichStringCellValue().getString();
        }
        return o+"";
    }
	// 获取数字类型的cell值
    private static Object getValueOfNumericCell(XSSFCell cell) {
        Boolean isDate = DateUtil.isCellDateFormatted(cell);
        Double d = cell.getNumericCellValue();
        Object o = null;
        if (isDate) {
            o = DateFormat.getDateTimeInstance()
                    .format(cell.getDateCellValue());
        } else {
            o = getRealStringValueOfDouble(d);
        }
        return o;
    }
    
    // 处理科学计数法与普通计数法的字符串显示，尽最大努力保持精度
    private static String getRealStringValueOfDouble(Double d) {
        String doubleStr = d.toString();
        boolean b = doubleStr.contains("E");
        int indexOfPoint = doubleStr.indexOf('.');
        if (b) {
            int indexOfE = doubleStr.indexOf('E');
            // 小数部分
            BigInteger xs = new BigInteger(doubleStr.substring(indexOfPoint
                    + BigInteger.ONE.intValue(), indexOfE));
            // 指数
            int pow = Integer.valueOf(doubleStr.substring(indexOfE
                    + BigInteger.ONE.intValue()));
            int xsLen = xs.toByteArray().length;
            int scale = xsLen - pow > 0 ? xsLen - pow : 0;
            doubleStr = String.format("%." + scale + "f", d);
        } else {
            java.util.regex.Pattern p = Pattern.compile(".0$");
            java.util.regex.Matcher m = p.matcher(doubleStr);
            if (m.find()) {
                doubleStr = doubleStr.replace(".0", "");
            }
        }
        return doubleStr;
    }
	
	/**
	 * 读取Office 2007 excel
	 */
	private static StoreData read2007Excel(File file)
			throws IOException {

		List<List> list = new LinkedList<List>();
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook xwb = new XSSFWorkbook(fis);
		List<String> headerLabels = new ArrayList<>(); //表头
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(0);
		String value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		List<String> rowData = null;  
		
		Integer headers = (int) sheet.getRow(0).getLastCellNum(); //头部长度
		
		//处理第一行头部
		
		for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				logger.info("行为空:"+i);
				continue;
			}
			
			rowData = new ArrayList<>();
			
			for (int j = 0; j <headers; j++) {
				cell = row.getCell(j);
				if(cell==null){ //空单元格，作为结束了
					value = "";
				}else{
					value = getCellValue(cell);
				}
				
				if(i==0){ //表头处理
					headerLabels.add(value);
				}else{ //内容数据处理
					rowData.add(value);
				}
			}
		 
			if(rowData.size()!=0 && !StringUtils.isEmpty(org.apache.commons.lang3.StringUtils.join(rowData, ""))){
				list.add(rowData);
			}
		}
		
		fis.close();
		
		StoreData storeData = new StoreData(STORE_DATA_INIT_SIZE);
		storeData.setList(list);
		storeData.setHeader(headerLabels);
		storeData.setShowHeaders(headerLabels);
		return storeData;
	}
	
	public static boolean isNumeric(String str){ 
		Pattern pattern = Pattern.compile("[0-9]*"); 
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false; 
		} 
		return true; 
	}
	
	public static Integer converInteger(String str){ 
		Integer i = new Integer(str.toString());
		return i ;
	}
	
	public static Float converFloat(String str){ 
		Float i = Float.parseFloat(str.toString());
		return i ;
	}
	
	public static void main(String[] args) throws ExecutionException, Exception {
//		List<StoreData> list =  readExcel("C:\\Users\\db2admin\\Desktop\\测试");
//		List<StoreData> list2 =  readExcel("C:\\Users\\db2admin\\Desktop\\测试2");
//		System.out.println(list.get(0).getList().size()+"|"+list2.get(0).getList().size());
//		for (List row : list.get(0).getList()) {
//			boolean flag = false;
//			for (List row2 : list2.get(0).getList()) {
//				if(row.get(0).equals(row2.get(0))){
//					flag = true;
//					break;
//				}
//			}
//			if(flag ==false){
//				System.out.println(row.toString());
//			}
//		}
		
//		ExcelUtil.createExcel(list.get(0).getList(),list.get(0).getHeader(), "C:\\Users\\db2admin\\Desktop\\测试2");
		File file = new File("C:\\Users\\db2admin\\Desktop\\测试2\\test.vcf");
		Vcard vcard = new Vcard("测试好", "金蚁软件", "13535242304");
 		FolderUtil.writeFileContent(file, vcard.toString());
		
	}
	
}
