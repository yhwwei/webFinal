package utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import web.UserServlet;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author yhw
 * @version 1.0
 **/
public class JDBCUtilsByDruid {
    private static DataSource ds;
    static {
        Properties properties = new Properties();
        try {
            InputStream is = UserServlet.class.getClassLoader().getResourceAsStream("druid.properties");
            properties.load(is);
            ds = DruidDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    public static void close(ResultSet set, Statement statement,Connection connection){
        try {
            if(set!=null){
                set.close();
            }
            if(statement!=null){
                statement.close();
            }
            if(connection!=null){
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
