package QuickConnect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Connector {
    private String HOST;
    private final int PORT=3306;
    private String DATABASE;
    private String USERNAME;
    private String PASSWORD;
    private Connection connection;
    private PreparedStatement stmt=null;
    
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
    
	public ResultSet select(String query, String[]... val) throws SQLException{
		stmt=connection.prepareStatement(query);
		for(int i=1;i<=val.length;i++){
			if(val[i-1][0].equalsIgnoreCase("l")) stmt.setLong(i,Long.parseLong(val[i-1][1]));
			else if(val[i-1][0].equalsIgnoreCase("i")) stmt.setLong(i,Integer.parseInt(val[i-1][1]));
			else stmt.setString(i,val[i-1][1]);
		}
		ResultSet res = stmt.executeQuery();
		return res;
	}
	
	public ResultSet select(String query, String... val) throws SQLException{
		stmt=connection.prepareStatement(query);
		for(int i=1;i<=val.length;i++) stmt.setString(i,val[i-1]);
		ResultSet res = stmt.executeQuery();
		return res;
	}
	
	public ResultSet select(String query) throws SQLException{
		stmt=connection.prepareStatement(query);
		ResultSet res = stmt.executeQuery();
		return res;
	}
	
	public void update(String query, String[]... val) throws SQLException{
		stmt=connection.prepareStatement(query);
		for(int i=1;i<=val.length;i++){
			if(val[i-1][0].equalsIgnoreCase("l")) stmt.setLong(i,Long.parseLong(val[i-1][1]));
			else if(val[i-1][0].equalsIgnoreCase("i")) stmt.setLong(i,Integer.parseInt(val[i-1][1]));
			else stmt.setString(i,val[i-1][1]);
		}
		stmt.executeUpdate();
	}
	
	public void update(String query, String... val) throws SQLException{
		stmt=connection.prepareStatement(query);
		for(int i=1;i<=val.length;i++) stmt.setString(i,val[i-1]);
		stmt.executeUpdate();
	}
	
	public void update(String query) throws SQLException{
		stmt=connection.prepareStatement(query);
		stmt.executeUpdate();
	}
	
	public boolean check(String query, String[]... val) throws SQLException{
		ResultSet res=select(query,val);
		if(res.next()) return true;
		return false;
	}
	
	public boolean check(String query, String... val) throws SQLException{
		ResultSet res=select(query,val);
		if(res.next()) return true;
		return false;
	}
	
	public boolean check(String query) throws SQLException{
		ResultSet res=select(query);
		if(res.next()) return true;
		return false;
	}
}
