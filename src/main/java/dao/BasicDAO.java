package dao;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.JDBCUtilsByDruid;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author yhw
 * @version 1.0
 * 编写基本DAO，是其他DAO的父类，
 *
 * 方便我们后面的增删改查
 **/
public abstract class BasicDAO<T>{
    private QueryRunner qr = new QueryRunner();


    //进行update，insert，select
    public int update(String sql,Object...parameters){
        Connection connection=null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            int effectRows = qr.update(connection, sql, parameters);
            return  effectRows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

    /**
     *
     * @param sql
     * @param clazz   传入与表对应的类的Class对象，用如：Actor.class，等获取Class对象。
     * @param parameters
     * @return
     */
    //select多行结果
    public List<T> queryMulti(String sql,Class<T> clazz,Object...parameters){
        Connection connection =null;


        try {
            connection = JDBCUtilsByDruid.getConnection();
            List<T> result = qr.query(connection, sql, new BeanListHandler<>(clazz), parameters);
            return  result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

    //单行查询结果
    public T queryOne(String sql,Class<T> clazz,Object... parameters){
        Connection connection =null;

        try {
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection,sql,new BeanHandler<>(clazz),parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

    //单行单列
    public Object queryScalar(String sql,Object... parameters){
        Connection connection =null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection,sql,new ScalarHandler<>(),parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }
}
