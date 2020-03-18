package com.autumn.tool;

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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @program: DBHelper
 * @description: 数据库操作类
 * 使用DBCP连接池
 * @Author 秋雨
 **/
public class DBHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBHelper.class);

    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static int InitialSize;
    private static int MinIdle;
    private static int MaxActive;
    private static long MAXWAIT;
    private static String ValidationQuery;   //验证使用的SQL语句
    private static long MinEvictableIdleTimeMillis;    //池中的连接空闲n分钟后被回收
    private static long TimeBetweenEvictionRunsMillis;  //每n秒运行一次空闲连接回收器
    private static boolean TestOnBorrow;  //借出连接时不要测试，否则很影响性能
    private static boolean TestWhileIdle;

    //Apache commons DbUtils
    private static QueryRunner QUERY_RUNNER = new QueryRunner();
    /*数据库连接池*/
    private static BasicDataSource DATA_SOURCE;
    /*ThreadLocal使变量隔离,防止多个线程调用已经关闭的Connection,但是Tomcat用到了线程池,多次请求可能用到同一个线程,所以调用方法结束时要remove掉并关闭连接*/
    private static ThreadLocal<Connection> CONNECTION_HOLDER;

    /**
     * 静态代码块在类加载时运行,默认配置文件为dbconfig.properties,需要从新自定义配置文件名需要使用构造函数
     */
    static{
        Properties conf = PropsUtil.loadProps("dbconfig.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");
        String initialSizeStr = conf.getProperty("initialSize");
        InitialSize = PropsUtil.getInt(conf,initialSizeStr,10);
        String minIdle = conf.getProperty("minIdle");
        MinIdle = PropsUtil.getInt(conf,minIdle,5);
        String maxActive = conf.getProperty("maxActive");
        MaxActive = PropsUtil.getInt(conf,maxActive,50);
        String maxwaitStr = conf.getProperty("maxWait");
        MAXWAIT = PropsUtil.getLong(conf,maxwaitStr,200000);

        ValidationQuery = conf.getProperty("validationQuery");

        String minEvictableIdleTimeMillis = conf.getProperty("minEvictableIdleTimeMillis");
        MinEvictableIdleTimeMillis = PropsUtil.getLong(conf,minEvictableIdleTimeMillis,1800000);

        String timeBetweenEvictionRunsMillis = conf.getProperty("timeBetweenEvictionRunsMillis");
        TimeBetweenEvictionRunsMillis = PropsUtil.getLong(conf,timeBetweenEvictionRunsMillis,30000);

        String testOnBorrow = conf.getProperty("testOnBorrow");
        TestOnBorrow = PropsUtil.getBoolean(conf,testOnBorrow,false);
        String testWhileIdle = conf.getProperty("testWhileIdle");
        TestWhileIdle = PropsUtil.getBoolean(conf,testWhileIdle,true);


        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        //数据库连接池
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
        DATA_SOURCE.setInitialSize(InitialSize);
        DATA_SOURCE.setMinIdle(MinIdle);
        DATA_SOURCE.setMaxTotal(MaxActive);
        DATA_SOURCE.setMaxWaitMillis(MAXWAIT);

        DATA_SOURCE.setValidationQuery(ValidationQuery);
        DATA_SOURCE.setMinEvictableIdleTimeMillis(MinEvictableIdleTimeMillis);
        DATA_SOURCE.setTimeBetweenEvictionRunsMillis(TimeBetweenEvictionRunsMillis);
        DATA_SOURCE.setTestOnBorrow(TestOnBorrow);
        DATA_SOURCE.setTestWhileIdle(TestWhileIdle);

        /*使用连接池就不需要jdbc加载驱动了*/
        /*try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            LOGGER.error("can not load jdbc driver",e);
        }*/
    }

    /**
     * 初始化DBhelp
     * @param dbConfigPath 参数为DB配置文件,不配置默认用静态代码块的config.properties
     */
    public DBHelper(String dbConfigPath) {
        Properties conf = PropsUtil.loadProps(dbConfigPath);
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");
        String initialSizeStr = conf.getProperty("initialSize");
        InitialSize = PropsUtil.getInt(conf,initialSizeStr,10);
        String minIdle = conf.getProperty("minIdle");
        MinIdle = PropsUtil.getInt(conf,minIdle,5);
        String maxActive = conf.getProperty("maxActive");
        MaxActive = PropsUtil.getInt(conf,maxActive,50);
        String maxwaitStr = conf.getProperty("maxWait");
        MAXWAIT = PropsUtil.getLong(conf,maxwaitStr,200000);

        ValidationQuery = conf.getProperty("validationQuery");

        String minEvictableIdleTimeMillis = conf.getProperty("minEvictableIdleTimeMillis");
        MinEvictableIdleTimeMillis = PropsUtil.getLong(conf,minEvictableIdleTimeMillis,1800000);

        String timeBetweenEvictionRunsMillis = conf.getProperty("timeBetweenEvictionRunsMillis");
        TimeBetweenEvictionRunsMillis = PropsUtil.getLong(conf,timeBetweenEvictionRunsMillis,30000);

        String testOnBorrow = conf.getProperty("testOnBorrow");
        TestOnBorrow = PropsUtil.getBoolean(conf,testOnBorrow,false);
        String testWhileIdle = conf.getProperty("testWhileIdle");
        TestWhileIdle = PropsUtil.getBoolean(conf,testWhileIdle,true);


        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        //数据库连接池
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
        DATA_SOURCE.setInitialSize(InitialSize);
        DATA_SOURCE.setMinIdle(MinIdle);
        DATA_SOURCE.setMaxTotal(MaxActive);
        DATA_SOURCE.setMaxWaitMillis(MAXWAIT);

        DATA_SOURCE.setValidationQuery(ValidationQuery);
        DATA_SOURCE.setMinEvictableIdleTimeMillis(MinEvictableIdleTimeMillis);
        DATA_SOURCE.setTimeBetweenEvictionRunsMillis(TimeBetweenEvictionRunsMillis);
        DATA_SOURCE.setTestOnBorrow(TestOnBorrow);
        DATA_SOURCE.setTestWhileIdle(TestWhileIdle);
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        Connection conn = null;
        /*使用连接池后,不需要ThreadLocal了(ThreadLocal为了解决让每个线程有自己的连接而不是共享一个连接的问题)*/
        //conn = CONNECTION_HOLDER.get();
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
     * 关闭数据库连接 线程结束前需要调用此方法关闭conn并从ThreadLocal中移除,不然连接对象会一直存在(但是数据库中的连接超过最大时间会关闭)
     * 如果用数据库连接池，要注释此处
     */
     public static void closeConnection() throws SQLException {
         Connection conn = CONNECTION_HOLDER.get();
         CONNECTION_HOLDER.remove();

        if (conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error("close connection failure",e);
            }
        }
     }

    /**
     * 关闭所有资源
     */
    public static void closeAll(ResultSet resultSet, PreparedStatement preparedStatement, Connection connnection, CallableStatement callableStatement) {
        // 关闭结果集对象
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        // 关闭PreparedStatement对象
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        // 关闭CallableStatement 对象
        if (callableStatement != null) {
            try {
                callableStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        // 关闭Connection 对象
        if (connnection != null) {
            try {
                connnection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

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

    public static void main(String[] args) throws SQLException {
        //重新自定义配置文件需要new一下用构造函数覆盖静态代码块的初始值
        //DBHelper dbHelper = new DBHelper("dbconfig.properties");
        //直接获取默认的配置文件为dbconfig.properties
        Connection connection = DBHelper.getConnection();
        System.out.println(connection.toString());
        Connection connection1 = DBHelper.getConnection();
        System.out.println(connection1.toString());
        /*ThreadLocal保证一个线程只实例化一个Connection,再次请求会再实例化一个Connection*/
        new Thread(){
            @Override
            public void run() {
                Connection connection_t1 = DBHelper.getConnection();
                System.out.println(connection_t1.toString());
            }
        }.start();
        //System.out.println(connection.getCatalog());
    }
}
