package core;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DataSource {

    private static DataSource datasource;
    private BoneCP connectionPool;

    private DataSource(ServletContext ctx) throws IOException, SQLException, PropertyVetoException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
//            String log4jConfigFile = ctx.getInitParameter("db-config-location");
//            String fullPath = ctx.getRealPath("") + File.separator + log4jConfigFile;
            //logger.info("Configuring db with " + fullPath);

            String dbUrl = "jdbc:mysql://localhost:3306/modemgsm";
            String dbUser = "modemgsm_user";
            String dbPass = "123456";
            int minConn = 50;
            int maxConn = 500;
            int partition = 1;
            int idleConnectionTestPeriodInMInutes = 2;

            String currentWorkingDir = System.getProperty("user.dir") + "\\webapps\\_configfiles";
            File dbPropertyFile = new File(currentWorkingDir, "mo_dbconfig.properties");

            if (dbPropertyFile.exists()) {

                try (InputStream input = new FileInputStream(dbPropertyFile)) {
                    
                    Properties prop = new Properties();
                    prop.load(input);

                    dbUrl = prop.getProperty("mysql_url");
                    dbUser = prop.getProperty("mysql_userName");
                    dbPass = prop.getProperty("mysql_password");
                    minConn = Integer.parseInt(prop.getProperty("mysql_min_connections"));
                    maxConn = Integer.parseInt(prop.getProperty("mysql_max_connections"));
                    partition = Integer.parseInt(prop.getProperty("mysql_partition"));
                    idleConnectionTestPeriodInMInutes = Integer.parseInt(prop.getProperty("mysql_idle_connection_test_period"));
                }
            }
            
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUser);
            config.setPassword(dbPass);
            config.setMinConnectionsPerPartition(minConn);
            config.setMaxConnectionsPerPartition(maxConn);
            config.setPartitionCount(partition);
            config.setIdleConnectionTestPeriodInMinutes(idleConnectionTestPeriodInMInutes);
            config.setConnectionTestStatement("/* ping */ SELECT 1");

            //logger.info("DB Config: " + new Gson().toJson(config));
            connectionPool = new BoneCP(config);
            
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public static DataSource getInstance(ServletContext ctx) throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource(ctx);
            return datasource;
        } else {
            return datasource;
        }
    }

    public static void destroy() {
        if (datasource != null) {
            try {
                //logger.info("Shutting db pool down..");
                datasource.shutdown();
                //logger.info("Done");
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    public Connection getConnection() throws SQLException {
        return this.connectionPool.getConnection();
    }

    public void shutdown() {
        this.connectionPool.shutdown();
    }

}
