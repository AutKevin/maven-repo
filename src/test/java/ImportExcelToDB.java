import com.autumn.xls.ImportExcelToDB;

/**
 * Created by Administrator on 2020/3/15.
 */
public class ImportExcelToDB {

    public static void main(String[] args) {
        String filePath = "D:\\IDEAWorkspaces\\tbk\\classes\\artifacts\\tbk_war_exploded\\ExcelDownload\\b.xls";
        String result = com.autumn.xls.ImportExcelToDB.getSQLValuesBatch(filePath,0,1);
        System.out.println(result);


        String sql = "insert ignore into table(`col1`, `col2`, `col3`) "; //sql的列一定要全部与excel中的列对应
        int insertRows = com.autumn.xls.ImportExcelToDB.insertEachRow(filePath,0,1,"conf/db.properties",sql);
    }

}
