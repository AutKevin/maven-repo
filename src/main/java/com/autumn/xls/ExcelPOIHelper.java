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
	 * �ж�Excel�İ汾,��ȡWorkbook
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
	 * �ж��ļ��Ƿ���excel
	 * 
	 * @throws Exception
	 */
	public static void checkExcelVaild(String fileName) throws Exception {
		/*
		 * if(!file.exists()){ throw new Exception("�ļ�������"); }
		 */
		if (!(fileName.endsWith(EXCEL_XLS) || fileName.endsWith(EXCEL_XLSX))) {
			throw new Exception("�ļ�����Excel");
		}
	}

	/**
	 * ��inputStreamת��Ϊfile
	 * 
	 * @param is
	 * @param file
	 *            Ҫ������ļ�Ŀ¼
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
	 * ����excel
	 * @param fileName �ļ�����
	 * @param title sheet����
	 * @param headers �б�ʶ
	 * @param dataset ��������
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

		// ����
		Row row = sheet.createRow(0);

		//ѭ��д������
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
     * ����Excel
     * @param sheetName sheet����
     * @param title ����
     * @param headers ͷ
     * @param dataset ����
     * @param wb HSSFWorkbook����
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String[] title,String[] headers,List<Map<String, Object>> dataset, HSSFWorkbook wb){

        // ��һ��������һ��HSSFWorkbook����Ӧһ��Excel�ļ�
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // �ڶ�������workbook�����һ��sheet,��ӦExcel�ļ��е�sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
        	HSSFCell cell1 = row.createCell(i);
        	cell1.setCellValue(headers[i]);
		}
        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ
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
        map.put("ca1", "����");
        map.put("ca2", "����");
        map.put("ca3", "����");
        map.put("ca4", "����");
        list.add(map);
        Map<String,Object> map1=new HashMap<String,Object>();
        map1.put("ca1", 1);
        map1.put("ca2", "����");
        map1.put("ca3", 20);
        map1.put("ca4", "����");
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        list.add(map1);
        String[] headers = new String[]{"ca1","ca2","ca3","ca4"};

        String fileName="d:\\��������Excel.xls";//���嵽��·��
        System.out.println(fileName);

        ExcelPOIHelper.exportExcel(fileName, "sheet1", headers, list);
	}

}
