/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

//import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import core.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import models.Client;

/**
 *
 * @author nazif
 */
public class DbHelper {
    
    

    public String get(ServletContext context) {

        Connection mysqlConn = null;
        try {
            mysqlConn = DataSource.getInstance(context).getConnection();
        } catch (IOException | SQLException | PropertyVetoException ex) {
            Logger.getLogger(DbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mysqlConn.toString();
    }

    public static boolean isDestinationTableExisted(ServletContext context) {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            int count = 0;
            String query
                    = "SELECT COUNT(*) FROM information_schema.TABLES"
                    + " WHERE(information_schema.TABLES.TABLE_SCHEMA = 'modemgsm'"
                    + " AND information_schema.TABLES.TABLE_NAME = 'api__received_sms');";

            Connection mysqlConn = DataSource.getInstance(context).getConnection();
            ps = mysqlConn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {               
                count = rs.getInt(1);
            }

            return count > 0;

        } catch (Exception ex) {
            return false;
        } finally {
            tryClose(rs, ps);
        }
    }

    public static String createDestinationTable(ServletContext context) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String query
                    = "CREATE TABLE api__received_sms ("
                    + " trans_id VARCHAR(36) NOT NULL COMMENT 'GUID format',"
                    + " `from` TEXT,"
                    + " `to` TEXT,"
                    + " content TEXT,"
                    + " timestamp DATETIME,"
                    + " PRIMARY KEY (trans_id))"
                    + " COMMENT 'Testing table for all sms received';";
            
            Connection mysqlConn = DataSource.getInstance(context).getConnection();
            ps = mysqlConn.prepareStatement(query);
            boolean result = ps.execute();

            return "OK"; //"Execute status: " + result;

        } catch (Exception ex) {
            return ex.getMessage();
        } finally {
            tryClose(rs, ps);
        }

    }

    public static String insertSms(ServletContext context, String transId, String from, String to, String content) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String query
                    = "INSERT INTO api__received_sms"
                    + " (trans_id, `from`, `to`, content, timestamp) VALUES"
                    + " (?, ?, ?, ?, NOW());";

            Connection mysqlConn = DataSource.getInstance(context).getConnection();
            ps = mysqlConn.prepareStatement(query);

            ps.setString(1, transId);
            ps.setString(2, from);
            ps.setString(3, to);
            ps.setString(4, content);

            boolean result = ps.execute();

            return "OK"; //"Execute status: " + result;

        } catch (Exception ex) {
            return ex.getMessage();
        } finally {
            tryClose(rs, ps);
        }
    }

    public static Client getClient(ServletContext context, String clientId) {

        Client client = new Client();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String query = "SELECT * FROM client WHERE client_id = '" + clientId + "';";

            Connection mysqlConn = DataSource.getInstance(context).getConnection();
            ps = mysqlConn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                client.clientId = clientId;
                client.clientName = rs.getString("client_name");
                client.moUrl = rs.getString("mo_url");
                client.phoneNumberList = StringHelper.splitStrings(rs.getString("phone_numbers"), ",");
            }

        } catch (Exception ex) {
            //logger.error(ex.getMessage(), ex);
        } finally {
            tryClose(rs, ps);
        }

        return client;
    }

    private static void tryClose(Object... objs) {
        try {
            for (Object obj : objs) {
                if (obj == null) {
                    continue;
                }

                try {
                    if (obj instanceof ResultSet) {
                        ((ResultSet) obj).close();
                    }
                    if (obj instanceof CallableStatement) {
                        ((CallableStatement) obj).close();
                    }
                    if (obj instanceof PreparedStatement) {
                        ((PreparedStatement) obj).close();
                    }
                    if (obj instanceof Connection) {
                        ((Connection) obj).close();
                    }
                } catch (Exception ex) {
                    //logger.error(ex.getMessage());
                    //logger.error(ex.getStackTrace().toString());
                }
            }
        } catch (Exception ex) {
            //logger.error("Exception: " + ex.getMessage(), ex);
            ex.printStackTrace();
        }
    }

}
