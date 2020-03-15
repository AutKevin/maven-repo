package com.autumn.xls;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @description: 将Excel导入数据库
 * @Author 秋雨
 */
public class ImportExcelToDB {

    private static final Logger logger = LoggerFactory.getLogger(ImportExcelToDB.class);

    /**
     * 导入excel到数据库 - Excel的全部列作为values
     * @param filePath excel文件
     * @param sheetNo 表单号,从0开始,第一个表单填0
     * @param headLine 跳过的行数,如果第一行是标题就设为1,标题占两行就填2
     * @return 把excel内容转成insert批量插入的形式 例如: ('val11','val12','val13'),('val21','val22','val23')
     */
    public static String getSQLValues(String filePath, int sheetNo, int headLine) {
        Workbook wb = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            //Excel路径
            FileInputStream in = new FileInputStream(filePath);
            if (filePath.endsWith(".xlsx")){
                //Excel版本Office> 2007
                wb = new XSSFWorkbook(in);
            }else if(filePath.endsWith(".xls")){
                //Excel版本<=Office 2007
                wb = new HSSFWorkbook(in);
            }else {
                logger.warn("ImportExcel中文件"+filePath+"类型不是xlsx或者xls");
                wb = new HSSFWorkbook(in);
            }
            //sheet页， 从0开始，0表示第一个sheet
            Sheet sheet = wb.getSheetAt(sheetNo);

            for (Row row : sheet) {
                //第一行通常为表头，不读
                if (row.getRowNum() < headLine) {
                    continue;
                }
                int lastCell = row.getLastCellNum();
                stringBuffer.append("(");
                for (int col =0;col<lastCell;col++){
                    Cell cell = row.getCell(col);
                    String cellVal = cell.getStringCellValue();

                    stringBuffer.append("'");
                    stringBuffer.append(cellVal);
                    stringBuffer.append("'");
                    if (col!=lastCell-1){
                        stringBuffer.append(",");
                    }
                }
                stringBuffer.append(")");
                if (row.getRowNum()<sheet.getLastRowNum()){
                    stringBuffer.append(",");
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
