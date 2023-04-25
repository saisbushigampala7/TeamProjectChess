package Server;

import java.sql.*;
import java.util.*;
import java.io.*;

public class Database
{
  private Connection con;
  //Add any other data fields you like – at least a Connection object is mandatory
  public void setConnection(String fn) throws IOException
  {
	//Create a properties object
	  //Use a FileInputStream as input to the Properties object for the reading the db.properties file
	  //Get the username, password, and url (MAC - change the url -> Windows)
	  //Set the con object
	  
	  Properties prop = new Properties();
	  FileInputStream fis = new FileInputStream(fn);
	  prop.load(fis);
	  String url = prop.getProperty("url");
	  String user = prop.getProperty("user");
	  String pass = prop.getProperty("password");    
	  
	  try {
		con = DriverManager.getConnection(url,user,pass);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public Connection getConnection()
  {
	  return con;
  }
  
  public ArrayList<String> query(String query)
  {
	  Statement stmt;
	  ResultSet rs;
	  ResultSetMetaData rmd;
	  ArrayList<String> list;
	  
    //Add your code here
	  //Using the Conn object create a Statement object
	  //Using the statement object executeQuery using the input query (Return the ResultSet)
	  //Use a while loop to process the rows - Create a , delimited record from each field
	  //Add each , delimited record to the ArrayList
	  //If no data found -> return null
	  try {
		  //Create a statement
		  stmt=con.createStatement(); 
		  
		  //Execute a query
		  rs=stmt.executeQuery(query);
		  
		  //Get metadata about the query
		  rmd = rs.getMetaData();
		  
		  //Get number of columns
		  int no_columns = rmd.getColumnCount();
		  
		  //Get a column name
		  String name = rmd.getColumnName(1);
		  
		  //Fetch each row using numeric numbering
		  list = new ArrayList<String>();
		  
		  while(rs.next()) 
		  {
			  String row = (rs.getString(1)+","+rs.getString(2));
			  list.add(row);
		  }
		  
		  if(list.isEmpty())
		  {
			  return null;
		  }
		  
		  return list;
	  }
	  catch (SQLException e)
	    {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } 
	
	  
	  return null; //CHANGE
  }
  
  public void executeDML(String dml) throws SQLException
  {
    //Add your code here
	  //1. Use the Conn object to create a Statement object
	  //2. Run dml on the execute method of Statement
	  
	  Statement stmt;
	  stmt=con.createStatement();  
	  stmt.execute(dml);
  }
  
  // Method for creating a new account.
  public boolean createNewAccount(String username, String password) throws SQLException
  {
	  //Stop if the user already exists
	  ArrayList<String> check = new ArrayList<String>();
	  check = query("select username from user;");
	  
	  if (check.contains(username))
		  return false;
	  
	  //Insert new account into user table
	  executeDML("insert into user values('"+username+"','"+password+"');");
	  
	  return true;
	  
  }
  
//Method for verifying a username and password.
 public boolean verifyAccount(String username, String password)
 {
   // Compare username and password to user table
	 ArrayList<String> check = new ArrayList<String>();
	 check = query("select * from user;");
	 
	 if (check.contains(username+","+password))
		 return true;
	 else
		 return false;
 }
  
  
}

