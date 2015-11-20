package com.haas.imdb.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * 
 * @author Zhaohong Jin
 *
 */
public class DBManager {
	private Connection connection;
	public Properties props;

	/**
	 * load the database.properties into the props (key -> value map)
	 * 
	 * @throws IOException
	 */
	public void readProperties() throws IOException {
		props = new Properties();
		try {
			String dir = System.getProperty("user.dir");
			FileInputStream in = new FileInputStream(dir
					+ "/database/database.properties");
			props.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			String dir = "E:/workstation/haas-imdb-database";
			FileInputStream in = new FileInputStream(dir
					+ "/database/database.properties");
			props.load(in);
			in.close();
		}
	}

	/**
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void openConnection() throws SQLException, IOException,
			ClassNotFoundException {
		readProperties();
		Class.forName("com.mysql.jdbc.Driver");
		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null)
			System.setProperty("jdbc.drivers", drivers);

		String url = props.getProperty("jdbc.url");
		System.out.println(url);
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");

		connection = DriverManager.getConnection(url, username, password); // use
																			// given
																			// info
																			// to
																			// open
																			// the
																			// database
		System.out.println("Opening database connection successful.");
		System.out.println("User: " + username);
	}

	/**
	 * create a specific table with a table name and column names
	 * 
	 * @param name
	 * @param columnName
	 *            the first [] is the colunmName, the second[] is the data type
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void createTable(String name, String[][] columnName)
			throws SQLException, ClassNotFoundException, IOException {
		openConnection();

		Statement stat = connection.createStatement(); // used for executing a
														// static SQL statement
														// and returning the
														// results it produces.
		// Delete the table first if any
		try {
			stat.executeUpdate("DROP TABLE " + name);
		} catch (Exception e) {
		}

		String query = "CREATE TABLE " + name + " (";
		int length = columnName.length;
		// create column name and units
		for (int i = 0; i < length; i++) {
			String colName = columnName[i][0];
			String unit = columnName[i][1];
			if (i < length - 1) {
				query += colName + " " + unit + ", ";
			} else {
				query += colName + " " + unit;
			}
		}
		query += ")";
		System.out.println(query);

		// Create the table
		// Executes the given SQL statement, which may be an INSERT, UPDATE, or
		// DELETE statement or an SQL statement that returns nothing, such as an
		// SQL DDL statement.
		stat.executeUpdate(query);
		System.out.println("Initiate table " + name + " complete!");
	}

	

	/**
	 * 
	 * @param table
	 * @return whether a specific table exists or not
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean tableExist(String table) throws SQLException, IOException,
			ClassNotFoundException {
		openConnection();
		Statement stat = connection.createStatement();
		String query = "SELECT count(*) FROM information_schema.tables WHERE table_schema = "
				+ "'mysql'" + "AND table_name =" + "'" + table + "'";
		ResultSet result = stat.executeQuery(query);
		result.next();
		int count = result.getInt("count(*)");
		if (count > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * insert into a specific table
	 * @param name
	 * @param values
	 * @throws SQLException 
	 */
	public void insert(String name, String[] values) throws SQLException {
		Statement stat = connection.createStatement();
		String query = "INSERT INTO " + name + " VALUES (";
		int len = values.length;
		for (int i = 0; i < len; i++) {
			String val = values[i];
			if (i < len - 1) {
				query += "'" + val + "', ";
			} else {
				query += "'" + val + "'";
			}
		}
		query += ")";
		try {
			 //System.out.println("Executing "+query);
			stat.executeUpdate(query);
		} catch (MySQLSyntaxErrorException e) {
			System.err.println(e);
			System.err.println("Error Query: " + query);
		} catch (MysqlDataTruncation e) {
			System.err.println(e);
			System.err.println("Error Query: " + query);
		}
	}
}

	