package com.xk.sb.common.recommend;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;

import java.sql.SQLException;

/**
 * @author Dover
 */
public class JDBCDataModelUtil {
    public static JDBCDataModel getDataModel() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        dataSource.setDatabaseName("lpf");
        dataSource.setServerTimezone("UTC");
        JDBCDataModel jdbcDataModel = new MySQLJDBCDataModel(dataSource, "user_preference", "user_id_long", "course_id_long", "val", "time");
        return jdbcDataModel;
    }
}
