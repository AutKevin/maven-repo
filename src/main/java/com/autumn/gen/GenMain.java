package com.autumn.gen;

import com.autumn.tool.DBHelper;
import com.autumn.tool.PropsUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.autumn.gen.GenEntityFile.getAllTables;
import static com.autumn.gen.GenEntityFile.wirteToFile;
import static com.autumn.tool.DBHelper.closeAll;

/**
 * @program: aeo-tool->GenMain
 * @description: ${description}
 * @author: 秋雨
 * @createTime: 2020-03-13 15:14
 **/
public class GenMain {
    private String packagePath = "com.autumn.entity";    //指定实体生成所在包的路径
    //表名以","分隔;若为"*"则生成所有表的entity文件
    private String tableNames = "*";
    private String authorName = "邱宇";    //作者名字
    private boolean ismaven = false;    //作者名字


    private boolean f_util = false;  // 是否需要导入包java.util.*
    private boolean f_sql = false;  // 是否需要导入包java.sql.*

    {
        Properties dbProp = PropsUtil.loadProps("GenModuleConfig.properties");

        packagePath = dbProp.getProperty("package");
        tableNames = PropsUtil.getString(dbProp,"tablenames");
        authorName = PropsUtil.getString(dbProp,"author");

        ismaven = PropsUtil.getBoolean(dbProp,"ismaven");

        f_util = PropsUtil.getBoolean(dbProp,"isimport_util");
        f_sql = PropsUtil.getBoolean(dbProp,"isimport_sql");
    }

    public static void main(String[] args) {
        new GenMain();
    }

    public GenMain(){

        String[] tables = null;
        if (this.tableNames.trim().equals("*")) {
            tables = getAllTables();
        }else {
            tables = this.tableNames.split(",");
        }
        for (int i = 0; i < tables.length; i++) {
            new GenMain(tables[i]);
            f_util = false;
            f_sql = false;
        }
    }

    /**
     * 带表名构造函数实现
     * @param tableName
     */
    public GenMain(String tableName){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseMetaData dbmd = null;
        //ResultSetMetaData rsmd= null;
        List<FieldMeta> fmls = new ArrayList<FieldMeta>();
        String sql= "select * from "+tableName;
        try {
            DBHelper dbHelper = new DBHelper("GenModuleConfig.properties");
            con= DBHelper.getConnection();
            dbmd = con.getMetaData();
            rs = dbmd.getColumns(con.getCatalog(), null, tableName, null);
            while (rs.next()) {
                FieldMeta fm = new FieldMeta();
                fm.setFieldName(rs.getString("COLUMN_NAME"));
                if (rs.getString("TYPE_NAME").equalsIgnoreCase("datetime")
                        || rs.getString("TYPE_NAME").equalsIgnoreCase("date")) {
                    f_util = true;
                }
                if (rs.getString("TYPE_NAME").equalsIgnoreCase("image")
                        || rs.getString("TYPE_NAME").equalsIgnoreCase("text")) {
                    f_sql = true;
                }
                fm.setFieldDataType(rs.getString("TYPE_NAME"));
                fm.setFieldLength(Integer.parseInt(rs.getString("COLUMN_SIZE")));
                fm.setFieldComment(rs.getString("REMARKS").replace("\r\n", "  "));   //注释中的换行改为空格
                fmls.add(fm);
            }
            /* 从resultSet读取的ResultSetMetaData没有注释等详细信息
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                FieldMeta fm = new FieldMeta();
                fm.setFieldName(rsmd.getColumnName(i+1));
                if (rsmd.getColumnTypeName(i+1).equalsIgnoreCase("datetime")) {
                    f_util = true;
                }
                if (rsmd.getColumnTypeName(i+1).equalsIgnoreCase("image")
                        || rsmd.getColumnTypeName(i+1).equalsIgnoreCase("text")) {
                    f_sql = true;
                }
                fm.setFieldDataType(rsmd.getColumnTypeName(i+1));
                fm.setFieldLength(rsmd.getColumnDisplaySize(i+1));
                //fm.setFieldComment(fieldComment);
                fmls.add(fm);
            }*/

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            if (con!=null) {
                closeAll(rs, ps, con, null);
            }
        }
        //生成内容
        GenEntityContent genEC= new GenEntityContent();
        String content = genEC.genEntityContent(fmls,tableName);
        //System.out.println(content);
        //写入到文件
        wirteToFile(content,this.packagePath,tableName,ismaven);
    }
}
