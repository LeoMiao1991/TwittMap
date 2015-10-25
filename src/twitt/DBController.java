package twitt;

import java.sql.*;

import twitter4j.GeoLocation;
import twitter4j.Status;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.TimeZone;

public class DBController 
{
	// set mySQL DB configurations
    static final String USER = "";
    static final String PASS = "";
    static final String url = "";
    static final String DB_URL = "jdbc:mysql://" + url;
    
    // set the format of the table
    static final String tableField = "(statusID, userName, latitude, longitude, createdTime, keyword, content)";
    static final String valueField = "(?, ?, ?, ?, ?, ?, ?)";
	static final String insertSQL = "INSERT INTO tweet " + tableField + " VALUES " + valueField;
	static final String condition = "WHERE createdTime > ? AND keyword = ?";
	static final String selectSQL = "SELECT latitude, longitude, statusID from tweet " + condition;
	static final String deleteSQL = "DELETE FROM tweet ORDER BY statusID LIMIT 100";
	
	// JDBC connection and statement
    Connection conn;
    PreparedStatement stmt;
    
    public void setConnnection()
    {
    	try
        {
        	System.out.println("configure JDBC driver");
        	Class.forName("com.mysql.jdbc.Driver");
        	conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    public void closeConnection()
    {
    	try
    	{
    		stmt.close();
    		conn.close();
    	}catch(SQLException se){se.printStackTrace();}
    }
    
    
    public void updateTable()
    {
    	try 
    	{
    		PreparedStatement updateStmt = conn.prepareStatement("SELECT COUNT(1) FROM tweet");
			ResultSet resultSet = updateStmt.executeQuery();
			resultSet.next();
			int counter = resultSet.getInt(1);
			if(counter > 1000)
				deleteEntry();
			resultSet.close();
		} catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void deleteEntry()
    {
    	try 
    	{
			PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
			deleteStmt.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
    }
    
    
    public void writeToDB(Status status, String keyword)
    {
    	GeoLocation location = status.getGeoLocation();
    	String statusID = "" + status.getId();
    	String name = status.getUser().getScreenName();
    	String text = status.getText();
    	if(text.length() > 255)
    		text = text.substring(0, 254);
    	Timestamp timestamp = uDateToSDate(status.getCreatedAt());
    	try
        {
    		stmt = conn.prepareStatement(insertSQL);
    		stmt.setString(1, statusID);
        	stmt.setString(2, name);
        	stmt.setDouble(3, location.getLatitude());
        	stmt.setDouble(4, location.getLongitude());
        	stmt.setTimestamp(5, timestamp);
        	stmt.setString(6, keyword);
        	stmt.setString(7, text);
            stmt.executeUpdate();
            /*
        	System.out.println(keyword + " new entry inserted " 
            + location.getLatitude() + " " + location.getLongitude());
            */
        }catch(SQLException se){se.printStackTrace();}
    }
    
    public List<GeoInfor> getGeoInfor(String message)
    {
    	String[] para = message.split("\\s");
    	List<GeoInfor> ans = new ArrayList<GeoInfor>();
    	if(para.length < 2)
    		return ans;
    	String timeslot = para[0], type = para[1];
    	int range = Integer.parseInt(timeslot) * 60;
    	try
    	{
    		Calendar curTime = Calendar.getInstance(TimeZone.getTimeZone("EST"));
  		  	curTime.add(Calendar.SECOND, 0 - range);
  		  	java.sql.Timestamp startTime = uDateToSDate(curTime.getTime());
  		  	PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
  		  	List<String> types = new ArrayList<>();
  		  	if(type.equals("all"))
  		  	{
  		  		types.add("tech");
  		  		types.add("music");
  		  		types.add("food");
  		  		types.add("sports");	
  		  	}
  		  	else
  		  		types.add(type);
  		  	for(String eleType : types)
  		  	{
  		  		selectStmt.setTimestamp(1, startTime);
  		  		selectStmt.setString(2, eleType);
  		  		ResultSet rs = selectStmt.executeQuery();
  		  		while(rs.next())
  		  		{
  		  			double latitude = rs.getDouble("latitude");
  		  			double longitude = rs.getDouble("longitude");
  		  			String statusID = rs.getString("statusID");
  		  			GeoInfor tmp = new GeoInfor(latitude, longitude, statusID);
  		  			ans.add(tmp);
  		  		}
  		  		rs.close();
  		  	}
    	}catch(SQLException e){e.printStackTrace();}
    	return ans;
    }
    
    public static java.sql.Timestamp uDateToSDate(java.util.Date uDate)
    {
    	java.sql.Timestamp sDate = new java.sql.Timestamp(uDate.getTime());
    	return sDate;
    }
}
