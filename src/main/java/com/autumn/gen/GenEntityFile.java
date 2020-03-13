package com.autumn.gen;

import com.autumn.tool.DBHelper;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.autumn.tool.DBHelper.closeAll;
import static com.autumn.tool.StringUtil.upFirstChar;

/**
 * @program: aeo-tool->GenEntityFile
 * @description:
 * @author: 秋雨
 * @createTime: 2020-03-13 15:02
 **/
public class GenEntityFile {
    /**
     * 写入文件
     */
    public static void wirteToFile(String content, String packagePath, String tableName,boolean isMaven){
        PrintWriter pw = null;
        BufferedWriter bw = null;
        File dir = new File("");    //根据空文件获取项目路径
        String classpath = "\\src\\";
        if (isMaven){
            classpath = "\\src\\main\\java\\";
        }
        //entity文件父文件夹全路径
        String dirpath = dir.getAbsolutePath() + classpath+packagePath.replace(".", "\\");
        File parentDir = new File(dirpath);
        if (!parentDir.exists()) {    //判断是否存在该路径
            parentDir.mkdirs();     //不存在则创建
        }
        String filePath = dirpath+"\\"+upFirstChar(tableName)+".java";    //java文件路径
        File file = new File(filePath);
        try {
            if (!file.exists()) {    //判断是否存在java文件
                file.createNewFile();    //不存在则创建
            }else {
                System.out.print("表     "+tableName+"   的entity实体已存在于:");
            }
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));    //OutputStreamWriter为了解决乱码
            bw = new BufferedWriter(pw);
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if (bw!=null) {
                    bw.close();
                }
                if (pw!=null) {
                    pw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(filePath);
    }

    /**
     * 获取所有表名
     */
    public static String[] getAllTables(){
        List<String> stringList = new ArrayList<String>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs =null;

        DBHelper dbHelper = new DBHelper("GenModuleConfig.properties");
        con = DBHelper.getConnection();
        String sql = "show tables";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                stringList.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            closeAll(rs, ps, con, null);
        }
        return stringList.toArray(new String[stringList.size()]);
    }
}
