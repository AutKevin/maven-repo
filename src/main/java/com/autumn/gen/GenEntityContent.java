package com.autumn.gen;

import com.autumn.tool.PropsUtil;
import com.autumn.tool.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @program: aeo-tool->genFrame
 * @description: 生成表的实体内容部分(package import field Method)
 * @author: 秋雨
 * @createTime: 2020-03-13 14:48
 **/
public class GenEntityContent {

    Properties dbProp = PropsUtil.loadProps("GenModuleConfig.properties");
    private String packagePath = dbProp.getProperty("package");    //指定实体生成所在包的路径
    private boolean f_util = PropsUtil.getBoolean(dbProp,"isimport_util");  // 是否需要导入包java.util.*
    private boolean f_sql = PropsUtil.getBoolean(dbProp,"isimport_sql");  // 是否需要导入包java.sql.*
    private String authorName = PropsUtil.getString(dbProp,"author");    //作者名字

    /**
     * 生成类主题框架
     */
    public String genEntityContent(List<FieldMeta> fmls, String tableName){
        StringBuilder sb = new StringBuilder();
        //package空间
        sb.append("package "+this.packagePath+";\r\n\r\n");
        // 判断是否导入工具包
        if (f_util) {
            sb.append("import java.util.Date;\r\n");
        }
        if (f_sql) {
            sb.append("import java.sql.*;\r\n");
        }
        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * 名   称：" + tableName + "\r\n");
        sb.append(" * 描   述：\r\n");
        sb.append(" * 作   者：" + this.authorName + "\r\n");
        sb.append(" * 时   间：" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + "\r\n");
        sb.append(" * --------------------------------------------------" + "\r\n");
        sb.append(" * 修改历史" + "\r\n");
        sb.append(" * 序号    日期    修改人     修改原因 "+ "\r\n");
        sb.append(" * 1" + "\r\n");
        sb.append(" * **************************************************" + "\r\n");
        sb.append(" */\r\n");
        // 实体部分
        sb.append("public class " + StringUtil.upFirstChar(tableName) + "{\r\n");
        //字段部分
        genAttrs(sb, fmls);
        //方法部分
        genMethods(sb, fmls);
        sb.append("}\r\n");
        return new String(sb);
    }

    /**
     * 功能：生成所有属性
     */
    public void genAttrs(StringBuilder sb,List<FieldMeta> fmls) {
        for (int i = 0; i < fmls.size(); i++) {
            sb.append("\t");
            sb.append("private " + fmls.get(i).getFieldDataType() + " "
                    + fmls.get(i).getFieldName() + ";");
            sb.append("\t/*"+fmls.get(i).getFieldComment()+"\tlen: "+fmls.get(i).getFieldLength()+"*/\r\n");
        }
        sb.append("\r\n");
    }

    /**
     * 功能：生成所有方法
     */
    public void genMethods(StringBuilder sb,List<FieldMeta> fmls){
        for (int i = 0; i < fmls.size(); i++) {
            sb.append("\tpublic void set"+StringUtil.upFirstChar(fmls.get(i).getFieldName())+"("+fmls.get(i).getFieldDataType()+" "+fmls.get(i).getFieldName()+"){\r\n");
            sb.append("\t\tthis."+fmls.get(i).getFieldName()+"="+fmls.get(i).getFieldName()+";\r\n");
            sb.append("\t}\r\n");
            sb.append("\tpublic "+fmls.get(i).getFieldDataType()+" get"+StringUtil.upFirstChar(fmls.get(i).getFieldName())+"(){\r\n");
            sb.append("\t\treturn this."+fmls.get(i).getFieldName()+";\r\n");
            sb.append("\t}\r\n");
            sb.append("\r\n");
        }
        sb.append("");
    }
}
