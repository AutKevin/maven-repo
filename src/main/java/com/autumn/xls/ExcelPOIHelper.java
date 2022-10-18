package com.autumn.xls;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelPOIHelper {

	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";

	/**
	 * 判断Excel的版本,获取Workbook
	 * 
	 * @param in
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbok(InputStream in, String fileName)
			throws IOException {
		Workbook wb = null;
		if (fileName.endsWith(EXCEL_XLS)) { // Excel 2003
			wb = new HSSFWorkbook(in);
		} else if (fileName.endsWith(EXCEL_XLSX)) { // Excel 2007/2010
			wb = new XSSFWorkbook(in);
		}
		return wb;
	}

	/**
	 * 判断文件是否是excel
	 * 
	 * @throws Exception
	 */
	public static void checkExcelVaild(String fileName) throws Exception {
		/*
		 * if(!file.exists()){ throw new Exception("文件不存在"); }
		 */
		if (!(fileName.endsWith(EXCEL_XLS) || fileName.endsWith(EXCEL_XLSX))) {
			throw new Exception("文件不是Excel");
		}
	}

	/**
	 * 将inputStream转化为file
	 * 
	 * @param is
	 * @param file
	 *            要输出的文件目录
	 */
	public static void inputStream2File(InputStream is, File file)
			throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int len = 0;
			byte[] buffer = new byte[8192];

			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
		} finally {
			os.close();
			is.close();
		}
	}

	/**
	 * 导出excel
	 * @param fileName 文件名称
	 * @param title sheet标题
	 * @param headers 列标识
	 * @param dataset 导出数据
	 * @return
	 */
	public static boolean exportExcel(String fileName, String title,
			String[] headers, List<Map<String,Object>> dataset) {
		boolean flag = false;
		Workbook workbook = null;
		if (fileName.endsWith("xlsx")) {
			workbook = new XSSFWorkbook();
		} else if (fileName.endsWith("xls")) {
			workbook = new HSSFWorkbook();
		} else {
			try {
				throw new Exception("invalid file name, should be xls or xlsx");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		Sheet sheet = workbook.createSheet(title);
		CellStyle style = workbook.createCellStyle();

		// 列名
		Row row = sheet.createRow(0);

		//循环写入数据
		Iterator<Map<String,Object>> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			
			row = sheet.createRow(index);
			Map map = it.next();
			int num = 0;
			for (int i = 0; i < headers.length; i++) {
				Cell cell = row.createCell(num);
				num++;
				String key = headers[i];
				Object obj = map.get(key);
				if(obj!=null){
					cell.setCellValue(obj.toString());
				}else{
					cell.setCellValue("");
				}
			}
			index++;
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fileName);
			workbook.write(fos);
			fos.close();
			flag = true;
		} catch (FileNotFoundException e) {
			flag = false;
			e.printStackTrace();
		} catch (IOException e) {
			flag = false;
			e.printStackTrace();

		}
		return flag;
	}

	/**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param headers 头
     * @param dataset 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String[] title,String[] headers,List<Map<String, Object>> dataset, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
        	HSSFCell cell1 = row.createCell(i);
        	cell1.setCellValue(headers[i]);
		}
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		if(dataset!=null){
		        Iterator<Map<String,Object>> it = dataset.iterator();
				int index = 1;
				while (it.hasNext()) {
					
					row = sheet.createRow(index);
					Map map = it.next();
					for (int i = 0; i < title.length; i++) {
						HSSFCell cell = row.createCell(i);
						String key = title[i];
						if("number".equals(key)){
							cell.setCellValue(index);
						}else{
							Object obj = map.get(key);
							if(obj!=null){
								cell.setCellValue(obj.toString());
							}else{
								cell.setCellValue("");
							}
						}
					}
					index++;
				}
		}
        return wb;
    }

	public static void main(String[] args) throws FileNotFoundException {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("ca1", "序列");
        map.put("ca2", "姓名");
        map.put("ca3", "年龄");
        map.put("ca4", "户籍");
        list.add(map);
        Map<String,Object> map1=new HashMap<String,Object>();
        map1.put("ca1", 1);
        map1.put("ca2", "张三");
        map1.put("ca3", 20);
        map1.put("ca4", "陕西");
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        String[] headers = new String[]{"ca1","ca2","ca3","ca4"};

        String fileName="d:\\导出数据Excel.xls";//定义到处路径
        System.out.println(fileName);

        ExcelPOIHelper.exportExcel(fileName, "sheet1", headers, list);
	}

}
