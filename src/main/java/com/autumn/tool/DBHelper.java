package org.smart4j.framework.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @program: DBHelper
 * @description: 数据库操作类
 **/
public class DBHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBHelper.class);

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    //Apache commons DbUtils
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    /*数据库连接池*/
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final BasicDataSource DATA_SOURCE;
    /**
     * 静态代码块在类加载时运行
     */
    static{
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        //数据库连接池
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
        /*使用连接池就不需要jdbc加载驱动了*/
        /*try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            LOGGER.error("can not load jdbc driver",e);
        }*/
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        Connection conn = null;
        conn = CONNECTION_HOLDER.get();

        if (conn==null){  //当从连接池中获取的connection为null时新建一个Connection
            try {
                /*JDBC获取连接*/
                //conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                /*数据库连接池获取连接*/
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();  //在catlina.out中打印
                LOGGER.error("get connection failure",e);
            } finally {
                /*数据库连接池，新建的情况要把新建的connection放入到池中*/
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * 如果用数据库连接池，要注释此处
     * @param conn
     */
    /* public static void closeConnection(Connection conn){
        if (conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error("close connection failure",e);
            }
        }
    }*/


    /**
     * 查询实体列表
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList;
        Connection conn = getConnection();
        try {
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            //e.printStackTrace();
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        } finally {
            //连接池不需要关闭conn
            /*closeConnection(conn);*/
        }
        return entityList;
    }

    /**
     * 查询实体
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params){
        T entity;
        Connection conn = getConnection();
        try {
            entity=QUERY_RUNNER.query(conn,sql,new BeanHandler<T>(entityClass),params);
        } catch (SQLException e) {
            //e.printStackTrace();
            LOGGER.error("query entity failure",e);
            throw new RuntimeException(e);
        } finally {
            //连接池不需要关闭conn
            /*closeConnection(conn);*/
        }
        return entity;
    }

    /**
     * 多表查询，其中的Map表示列明与列值的映射关系
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String,Object>> executeQuery(String sql, Object... params){
        List<Map<String,Object>> result;
        Connection conn = getConnection();
        try {
            result = QUERY_RUNNER.query(conn,sql,new MapListHandler(),params);
        } catch (SQLException e) {
            //e.printStackTrace();
            LOGGER.error("execute query failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 执行更新语句（包括update，insert，delete）
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params){
        int rows =0;
        Connection conn = getConnection();
        try {
            rows = QUERY_RUNNER.update(conn,sql,params);
        } catch (SQLException e) {
            //e.printStackTrace();
            LOGGER.error("execute update failure",e);
            throw new RuntimeException(e);
        } finally {
            //连接池不需要关闭conn
            /*closeConnection(conn);*/
        }
        return rows;
    }

    /**
     * 插入实体（根据executeUpdate方法）
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String,Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = "insert into "+getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "),columns.length(),")");
        values.replace(values.lastIndexOf(", "),values.length(),")");
        sql += columns+" VALUES" +values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql,params)==1;
    }

    /**
     * 修改
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String,Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = "update "+getTableName(entityClass) + " set ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql+= columns.substring(0,columns.lastIndexOf(", "))+"where id=?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return executeUpdate(sql,params)==1;
    }

    /**
     * 删除
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id){
        String sql = "delete from "+getTableName(entityClass)+" where id =?";
        return executeUpdate(sql,id)==1;
    }

    /**
     * 获取类名（不包含报名和后缀）
     * @param entityClass
     * @return
     */
    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName().toLowerCase();
    }

    /**
     * 逐行执行sql文件
     * @param filePath
     */
    public static void executeSqlFile(String filePath){
        //获取sql文件的InputStream流
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        //将InputStream流转为Reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            String sql;
            while ((sql = reader.readLine())!=null){  //逐行读取
                executeUpdate(sql);  //执行读取出来的一行sql语句
            }
        } catch (IOException e) {
            //e.printStackTrace();
            LOGGER.error("execute sql file failure",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 开启事务
     */
    public static void beginTransaction(){
        Connection conn = getConnection();   //获取连接(同一线程每次获取的是统一conn)
        if (conn!=null){
            try {
                conn.setAutoCommit(false);  //将自动提交属性改为false
            } catch (SQLException e) {
                LOGGER.error("begin transaction failure",e);
                throw new RuntimeException(e);
                //e.printStackTrace();
            }finally {
                CONNECTION_HOLDER.set(conn);   //将改动属性的conn放入容器中
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction(){
        Connection conn = getConnection();
        if (conn != null){
            try {
                conn.commit();   //提交事务
                conn.close();    //关闭连接
            } catch (SQLException e) {
                LOGGER.error("commit transaction failure",e);
                throw new RuntimeException(e);
                //e.printStackTrace();
            } finally {
                CONNECTION_HOLDER.remove();   //从容器中移除连接
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction(){
        Connection conn = getConnection();
        if (conn != null){
            try {
                conn.rollback();   //回滚
                conn.close();    //关闭连接
            } catch (SQLException e) {
                LOGGER.error("rollback transaction failure",e);
                throw new RuntimeException(e);
                //e.printStackTrace();
            } finally {
                CONNECTION_HOLDER.remove();   //从容器中移除连接
            }
        }
    }
}
