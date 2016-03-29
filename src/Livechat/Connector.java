package Livechat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {
    private String HOST;
    private final int PORT=3306;
    private String DATABASE;
    private String USERNAME; 
    private String PASSWORD;
    private Connection connection;
    
    public Connector(String HOST, String DB, String UN, String PW){
    	this.HOST=HOST;
    	DATABASE=DB;
    	USERNAME=UN;
    	PASSWORD=PW;
        try{
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://"+HOST+":"+PORT+"/"+DATABASE;
			connection=DriverManager.getConnection(url,USERNAME,PASSWORD);
		}catch(ClassNotFoundException|SQLException e){
			e.printStackTrace();
			System.exit(1);
		}
    }
    
    public Connection getConnection(){
    	return connection;
    }
    
    public ResultSet doQuery(String query) throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(query);
        return res;
    }
    
    public void doUpdate(String query) throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
    }
}