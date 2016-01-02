/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeamediaplayer;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
// import com.mysql.jdbc.*;

/**
 *
 * @author sergio
 */
public class SQLConnector {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://96.126.116.70/ExeaMediaManager";

    //  Database credentials
    static final String USER = "exeadmin";
    static final String PASS = "YyaAlI4gdhOJ";
    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs;
    
    public SQLConnector() {
        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Open a connection with database
     */
    public void openConnection() {
        try {
            //Open a connection
            Logger.getLogger(SQLConnector.class.getName()).log(Level.INFO, "Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Clean-up environment
     */
    public void closeConnection() {
        try {
            rs.close();
            stmt.close();
            conn.close();
            Logger.getLogger(SQLConnector.class.getName()).log(Level.INFO, "Closed connection.");
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get user of database
     * @param username
     * @return user
     */
    public User getUser(String username) {
        try {
            this.openConnection();
            
            // Execute a query
            Logger.getLogger(SQLConnector.class.getName()).log(Level.INFO, "Creating statement...");
            stmt = conn.createStatement();
            
            String sql;
            sql = "SELECT u.id, u.username, u.password, u.first_name, u.last_name, c.country, u.device_id " +
                  "FROM users AS u " +
                  "JOIN countries AS c ON u.country_id = c.id " +
                  "WHERE u.username LIKE \"" + username +"\"";
            Logger.getLogger(SQLConnector.class.getName()).log(Level.INFO, "Executing query: \"{0}\" ...", sql);
            rs = stmt.executeQuery(sql);
            
            User user = new User();
            // Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setCountry(rs.getString("country"));
                user.setDevice(rs.getString("device_id"));
            }
            
            this.closeConnection();
            
            if(user.getPassword().equals("")) {
                return null;
            } else {
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}