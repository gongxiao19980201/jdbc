package com.mysql.jdbc;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.jdbc.PreparedStatementWrapper;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

public class MysqlTest {
   /**
    *
    * @throws Exception
    * new Driver() 注册驱动
    */
   @Test
   public void test01()throws Exception{
      //注册数据库驱动
      DriverManager.registerDriver(new Driver());
      //获取连接对象
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db2019?serverTimezone=GMT%2B8",
              "root","123456");
      //获取执行sql语句的statement对象
      String sql = "select * from payment";
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      boolean next = resultSet.next();
      int id = resultSet.getInt(1);
      String serial = resultSet.getString("serial");
      System.out.println(id+":"+serial);
   }

   /**
    *
    * @throws Exception
    * 抽取properties文件，反射注册驱动
    */

   @Test
    public void test02()throws Exception{
       Properties properties = new Properties();
       properties.load(new FileInputStream("src/main/resources/jdbc.properties"));
       String username = properties.getProperty("username");
       String password = properties.getProperty("password");
       String url = properties.getProperty("url");
       String driver = properties.getProperty("driver");
       //加载驱动
       Class.forName(driver);
       //获取连接
       Connection connection = DriverManager.getConnection(url,username,password);
       String sql = "select * from payment";
       Statement statement = connection.createStatement();
       PreparedStatement preparedStatement = connection.prepareStatement(sql);
       ResultSet resultSet = statement.executeQuery(sql);
       int id = resultSet.getInt(1);
       String serial = resultSet.getString("serial");
       System.out.println(id+":"+serial);
   }

   /**
    *
    * @throws Exception
    * 批量操作
    */
   @Test
   public void test03()throws Exception{
      Properties properties = new Properties();
      properties.load(new FileInputStream("src/main/resources/jdbc.properties"));
      String username = properties.getProperty("username");
      String password = properties.getProperty("password");
      String url = properties.getProperty("url");
      String driver = properties.getProperty("driver");
      //加载驱动
      Class.forName(driver);
      //获取连接
      Connection connection = DriverManager.getConnection(url,username,password);
      String sql = "insert payment values(null,?)";
      PreparedStatement statement = connection.prepareStatement(sql);
      for (int i = 0;i<500;i++){
         statement.setString(1, String.valueOf(i));
         statement.addBatch();
      }
      statement.executeBatch();
      statement.clearBatch();
   }

   /**
    * 连接池
    * @throws Exception
    */
   @Test
   public void test04()throws Exception{
      Properties properties = new Properties();
      properties.load(new FileInputStream("src/main/resources/datasource.properties"));
      DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
      Connection connection = dataSource.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement("select * from payment limit 10");
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()){
         System.out.println(resultSet.getString("serial"));
      }

   }
   @Test
   public void test05()throws Exception{
      Properties properties = new Properties();
      properties.load(new FileInputStream("src/main/resources/datasource.properties"));
      DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
      Connection connection = dataSource.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement("select count(*) c from payment");
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()){
         System.out.println(resultSet.getString("c"));
      }

   }

}
