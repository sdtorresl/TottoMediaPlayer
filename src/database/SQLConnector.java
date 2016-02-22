/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.CircularArrayList;
import java.time.LocalTime;
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
            sql = "SELECT u.id, u.username, u.password, u.first_name, u.last_name, u.country_id, c.name AS country, u.device_id " +
                  "FROM users AS u " +
                  "JOIN countries AS c ON u.country_id = c.id " +
                  "WHERE u.username LIKE \"" + username +"\"";
            Logger.getLogger(SQLConnector.class.getName()).log(Level.INFO, "Executing query: \"{0}\" ...", sql);
            rs = stmt.executeQuery(sql);
            
            if (!rs.next()){
                //ResultSet is empty
                Logger.getLogger(SQLConnector.class.getName()).log(Level.WARNING, "The user {0} doesn't exist.", username);
                return null;
            }

            User user = new User();
            
            // Extract data from result set
            if(rs.first()){
                //Retrieve by column name
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setCountry(rs.getInt("country_id"));
                user.setDevice(rs.getString("device_id"));
            }
            
            this.closeConnection();
            
            return user;
            
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Get event of database
     * @param username
     * @param countryId
     * @param dayOfWeek
     * @return Event
     */
    public Event getEvent(String username, int countryId, String dayOfWeek) {
        try {
            this.openConnection();
            
            // Execute a query
            Logger.getLogger(SQLConnector.class.getName()).log(Level.INFO, "Creating statement...");
            stmt = conn.createStatement();
            
            String sql;
            sql = "SELECT e.name as 'Event', \n" +
                  "    p.name AS 'Playlist', \n" +
                  "    c.name AS 'Country', \n" +
                  "    u.username AS 'Username',\n" +
                  "    cat.name AS 'Category',\n" +
                  "    cat.description AS 'Category description',\n" +
                  "    cat.folder_name AS 'Folder name',\n" +
                  "    e.start_time AS 'Start Time',\n" +
                  "    e.end_time AS 'End Time',\n" +
                  "    ec.order_number AS 'Order'\n" +
                  "FROM events AS e, playlists AS p, \n" +
                  "    countries AS c, users AS u, \n" +
                  "    categories AS cat,\n" +
                  "    events_categories AS ec,\n" +
                  "    events_playlists AS ep\n" +  
                  "WHERE c.id = " + countryId + "\n" +
                  "AND p.country_id = c.id\n" +
                  "AND e.id = ep.event_id\n" +
                  "AND p.id = ep.playlist_id\n" +
                  "AND ec.event_id = e.id\n" +
                  "AND ec.category_id = cat.id\n" +
                  "AND e.status = 1\n" +
                  "AND e.day_of_week LIKE '%" + dayOfWeek + "%'\n" +
                  "AND u.username LIKE '" + username + "'\n" +
                  "AND \"" + LocalTime.now() + "\" BETWEEN e.start_time AND e.end_time \n" +
                  "ORDER by e.name, ec.order_number";
            Logger.getLogger(SQLConnector.class.getName()).log(Level.INFO, "Executing query: \"{0}\" ...", sql);
            rs = stmt.executeQuery(sql);
            
            int totalRows = 0;
            if (rs.last()) {
                totalRows = rs.getRow();
                // Move to beginning
                rs.beforeFirst();
            }
            CircularArrayList<Category> categories = new CircularArrayList<>(totalRows);
            
            Event event = new Event();
            
            
            // Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                Category category = new Category();
                category.setName(rs.getString("Category"));
                category.setDescription(rs.getString("Category description"));
                category.setFolderName(rs.getString("Folder name"));
                
                categories.add(category);
                
                event.setName(rs.getString("Event"));
                event.setStartTime(rs.getTime("Start Time"));
                event.setEndTime(rs.getTime("End Time"));
            }
            
            event.setCategories(categories);
            
            this.closeConnection();
            
            return event;
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Event getEvent(String username, int countryId) {
        String currentDay = "Monday";
        
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        switch(dayOfWeek) {
            case Calendar.MONDAY: currentDay = "Monday"; break;
            case Calendar.TUESDAY: currentDay = "Tuesday"; break;
            case Calendar.WEDNESDAY: currentDay = "Wednesday"; break;
            case Calendar.THURSDAY: currentDay = "Thursday"; break;
            case Calendar.FRIDAY: currentDay = "Friday"; break;
            case Calendar.SATURDAY: currentDay = "Saturday"; break;
            case Calendar.SUNDAY: currentDay = "Sunday"; break;
        }
        
        return this.getEvent(username, countryId, currentDay);
    }
}