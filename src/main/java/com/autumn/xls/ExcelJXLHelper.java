package com.autumn.xls;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelJXLHelper {
    /**
     * 分隔符
     */
    private final static String SEPARATOR = "|";

    /**
     * 由List导出至指定的Sheet,带total行(最后一行)
     * @param wb   模板的workbook
     * @param sheetNum   第几个表单
     * @param targetFilePath 生成文件夹路径
     * @param l 内容list集合,以|分割的对象string集合
     * @param headInfoRows  头信息的行数
     * @param columnsLength  列数
     * @param remarkRowNumber 备注所在行
     * @param remark  备注
     * @return
     * @throws IOException
     * int
     */
    public static int exportExcelFromList(Workbook wb, int sheetNum,
                                          String targetFilePath, List<String> l, int headInfoRows,
                                          int columnsLength, int remarkRowNumber, String remark) throws WriteException, IOException {
        // 创建可写入的Excel工作薄对象
        WritableWorkbook wwb = null;
        int writeCount = 0;

        // 单元格样式
        // WritableFont bold = new
        // WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
        WritableCellFormat normalFormat = new WritableCellFormat(
                NumberFormats.TEXT);
        normalFormat.setBorder(Border.ALL, BorderLineStyle.THIN,
                jxl.format.Colour.BLACK);


        //设置字体;
        WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);
        WritableCellFormat normalFormat_total = new WritableCellFormat(
                font);
        normalFormat_total.setBorder(Border.ALL, BorderLineStyle.THIN,
                jxl.format.Colour.BLACK);

        try {

            // 创建可写入的Excel工作薄对象
            wwb = jxl.Workbook.createWorkbook(new File(targetFilePath), wb);
            WritableSheet ws = wwb.getSheet(0);

            Label cellRemark = new Label(0, remarkRowNumber, remark,
                    normalFormat);
            ws.addCell(cellRemark);

            int row = l.size();
            int columns = columnsLength;
            String[] ary = new String[120];

            for (int i = 0; i < row; i++) {
                ary = l.get(i).split("\\" + SEPARATOR,-1);
                for (int j = 0; j < columns; j++) {

                    if(i==row-1)
                    {
                        Label cell = new Label(j, i + headInfoRows, ary[j],
                                normalFormat_total);
                        ws.addCell(cell);
                    }else
                    {
                        Label cell = new Label(j, i + headInfoRows, ary[j],
                                normalFormat);
                        ws.addCell(cell);
                    }
                }
                writeCount++;
            }
            wwb.write();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (wwb != null) {
                wwb.close();
            }

        }

        return writeCount;

    }

    /**
     * 导出不需要合计行
     * @param wb
     * @param sheetNum
     * @param targetFilePath
     * @param l
     * @param headInfoRows
     * @param columnsLength
     * @param remarkRowNumber
     * @param remark
     * @return
     * @throws WriteException
     * @throws IOException
     */
    public static int exportExcelFromListNoTotal(jxl.Workbook wb, int sheetNum,
                                                 String targetFilePath, List<String> l, int headInfoRows,
                                                 int columnsLength, int remarkRowNumber, String remark) throws WriteException, IOException {
        // 创建可写入的Excel工作薄对象
        WritableWorkbook wwb = null;
        int writeCount = 0;

        // 单元格样式
        // WritableFont bold = new
        // WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
        WritableCellFormat normalFormat = new WritableCellFormat(
                NumberFormats.TEXT);
        normalFormat.setBorder(Border.ALL, BorderLineStyle.THIN,
                jxl.format.Colour.BLACK);


        //设置字体;
        WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
        WritableCellFormat normalFormat_total = new WritableCellFormat(
                font);
        normalFormat_total.setBorder(Border.ALL, BorderLineStyle.THIN,
                jxl.format.Colour.BLACK);

        try {

            // 创建可写入的Excel工作薄对象
            wwb = jxl.Workbook.createWorkbook(new File(targetFilePath), wb);
            WritableSheet ws = wwb.getSheet(0);

            Label cellRemark = new Label(0, remarkRowNumber, remark,
                    normalFormat);
            ws.addCell(cellRemark);

            int row = l.size();
            int columns = columnsLength;
            String[] ary = new String[120];

            for (int i = 0; i < row; i++) {
                ary = l.get(i).split("\\" + SEPARATOR,-1);
                for (int j = 0; j < columns; j++) {


                    Label cell = new Label(j, i + headInfoRows, ary[j],
                            normalFormat);
                    ws.addCell(cell);
                }
                writeCount++;
            }
            wwb.write();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (wwb != null) {
                wwb.close();
            }

        }

        return writeCount;

    }

    /**
     * 生成简单Excel（head + content）
     * @param path 导出路径
     * @param header Excel头
     * @param contentlist Excel内容
     * @return 成功返回true,失败返回false
     */
    public static boolean exportSimpleExcel(String path, String header[], List<List<String>> contentlist){
        WritableWorkbook book = null;
        try{
            book = Workbook.createWorkbook(new File(path));
            //生成名为eccif的工作表，参数0表示第一页
            WritableSheet sheet = book.createSheet("Sheet1", 0);
            //表头导航
            for(int j=0;j<header.length;j++){
                Label label = new Label(j, 0, header[j]);
                sheet.addCell(label);
            }
            /*遍历List,记录集合*/
            for(int i=0;i<contentlist.size();i++){
                List row = contentlist.get(i);
                if (row!=null){
                    /*遍历单条记录的数据列*/
                    for (int j = 0;j<row.size();j++){
                        sheet.addCell(new Label(j,i+1, (String) row.get(i)));
                    }
                }
            }
            // 写入数据并关闭文件
            book.write();
        } catch (Exception e) {
            e.printStackTrace();
            return false;  //导出失败
        }finally{
            if(book!=null){
                try {
                    book.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        /*导出路径*/
        String p = "D://测试.xls";
        /*Excel头*/
        String[] h = {"学号","学生姓名","系","班级"};
        /*Excel内容*/
        List<List<String>> list = new ArrayList();
        List<String> row1 = new ArrayList();
        row1.add("学号1");
        row1.add("姓名1");
        row1.add("系1");
        row1.add("班级1");
        list.add(row1);
        List<String> row2 = new ArrayList();
        row2.add("学号2");
        row2.add("姓名2");
        row2.add("系2");
        row2.add("班级2");
        list.add(row2);

        /*调用函数导出Excel*/
        exportSimpleExcel(p,h,list);
    }

}
