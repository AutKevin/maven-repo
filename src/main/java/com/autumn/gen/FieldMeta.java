package com.autumn.gen;

/**
 * @program: aeo-tool->FieldMeta
 * @description: 字段信息表
 * @author: 秋雨
 * @createTime: 2020-03-13 14:35
 **/
class FieldMeta {
    private String fieldName;    //字段名
    private String fieldDataType;   //字段类型
    private int fieldLength;    //字段长度
    private String fieldComment;    //字段备注

    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDataType() {
        /*fieldDataType字段类型有可能带有Unsigned字符,需要把这个过滤掉*/
        fieldDataType = fieldDataType.replaceAll("UNSIGNED","").trim();

        if(fieldDataType.equalsIgnoreCase("bit")){
            return "boolean";
        }else if(fieldDataType.equalsIgnoreCase("tinyint")){
            return "byte";
        }else if(fieldDataType.equalsIgnoreCase("smallint")){
            return "short";
        }else if(fieldDataType.equalsIgnoreCase("int")){
            return "int";
        }else if(fieldDataType.equalsIgnoreCase("bigint")){
            return "long";
        }else if(fieldDataType.equalsIgnoreCase("float")){
            return "float";
        }else if(fieldDataType.equalsIgnoreCase("decimal") || fieldDataType.equalsIgnoreCase("numeric")
                || fieldDataType.equalsIgnoreCase("real") || fieldDataType.equalsIgnoreCase("money")
                || fieldDataType.equalsIgnoreCase("smallmoney")){
            return "double";
        }else if(fieldDataType.equalsIgnoreCase("varchar") || fieldDataType.equalsIgnoreCase("char")
                || fieldDataType.equalsIgnoreCase("nvarchar") || fieldDataType.equalsIgnoreCase("nchar")
                || fieldDataType.equalsIgnoreCase("text") || fieldDataType.equalsIgnoreCase("LONGTEXT")){
            return "String";
        }else if(fieldDataType.equalsIgnoreCase("datetime")){
            return "Date";
        }else if(fieldDataType.equalsIgnoreCase("image")){
            return "Blod";
        }
        return fieldDataType;
    }
    public void setFieldDataType(String fieldDataType) {
        if(fieldDataType.equalsIgnoreCase("bit")){
            this.fieldDataType = "boolean";
        }else if(fieldDataType.equalsIgnoreCase("tinyint")){
            this.fieldDataType = "byte";
        }else if(fieldDataType.equalsIgnoreCase("smallint")){
            this.fieldDataType = "short";
        }else if(fieldDataType.equalsIgnoreCase("int")){
            this.fieldDataType = "int";
        }else if(fieldDataType.equalsIgnoreCase("bigint")){
            this.fieldDataType = "long";
        }else if(fieldDataType.equalsIgnoreCase("float")){
            this.fieldDataType = "float";
        }else if(fieldDataType.equalsIgnoreCase("decimal") || fieldDataType.equalsIgnoreCase("numeric")
                || fieldDataType.equalsIgnoreCase("real") || fieldDataType.equalsIgnoreCase("money")
                || fieldDataType.equalsIgnoreCase("smallmoney")){
            this.fieldDataType = "double";
        }else if(fieldDataType.equalsIgnoreCase("varchar") || fieldDataType.equalsIgnoreCase("char")
                || fieldDataType.equalsIgnoreCase("nvarchar") || fieldDataType.equalsIgnoreCase("nchar")
                || fieldDataType.equalsIgnoreCase("text") || fieldDataType.equalsIgnoreCase("LONGTEXT")
                || fieldDataType.equalsIgnoreCase("TIMESTAMP")){
            this.fieldDataType = "String";
        }else if(fieldDataType.equalsIgnoreCase("datetime") || fieldDataType.equalsIgnoreCase("date")){
            this.fieldDataType = "Date";
        }else if(fieldDataType.equalsIgnoreCase("image")){
            this.fieldDataType = "Blod";
        }else {
            this.fieldDataType = fieldDataType;
        }
    }

    public int getFieldLength() {
        return fieldLength;
    }
    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getFieldComment() {
        return fieldComment;
    }
    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }

}
