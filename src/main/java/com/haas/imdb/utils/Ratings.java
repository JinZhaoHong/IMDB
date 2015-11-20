package com.haas.imdb.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import com.haas.imdb.database.DBManager;

/**
 * 
 * @author Zhaohong Jin
 *
 */
public class Ratings implements Parser {
	private DBManager dbm = new DBManager();
	private String top250table;
	private String bottom10table;
	private String alltable;

	/**
	 * input format: 0000000125 1498733 9.2 The Shawshank Redemption (1994)
	 * input formar: 0000000125 1498733 9.2 The Shawshank Redemption (1994)
	 * {...()}
	 */
	public void parse(File f) throws FileNotFoundException, SQLException,
			IOException, ClassNotFoundException {
		parseTop(f);
		parseBottom(f);
		parseAll(f);
	}

	/**
	 * 
	 */
	public void parseTop(File f) throws FileNotFoundException, SQLException,
			IOException, ClassNotFoundException {
		System.out.println("Creating Top 250 table...");
		createTop250(); // create the table
		Scanner c = new Scanner(f,"latin1");
		// skip first few lines
		while (!c.nextLine().contains("TOP 250 MOVIES")) {
		}
		;
		// skip fifteen more lines
		for (int i = 0; i < 15; i++) {
			c.nextLine();
		}
		int i = 0;
		while (c.hasNextLine() && i < 250) {
			String line = c.nextLine();
			//System.out.println(line);
			line = line.replace("'", "''");
			String[] values = new String[6];
			for (String s : values) { // initiate the array
				s = new String();
			}
			try {
				String[] split = line.split("\\s{2,}"); // split by two or more
														// spaces
				// for (String s : split) { //debug
				// System.out.println(s);
				// }
				values[0] = split[1];
				values[1] = split[2];
				values[2] = split[3];
				String sub = split[4]; // continue to split the later part
				if (!sub.contains("\"")) {
					String[] subsplit = sub.split("[\\(\\)]"); // split the
																// parenthesis
					values[3] = subsplit[0];
					values[4] = subsplit[1].replaceAll("[^0-9]", "");
					values[5] = "NULL";
				} else {
					String[] subsplit = sub.split("[\\\"\\{\\}]"); // split " "
																	// and { }
					values[3] = subsplit[1];
					values[4] = subsplit[2].replaceAll("[^A-Za-z0-9]", "");
					values[5] = subsplit[3];
				}
				insert(top250table, values);
				i++; // only need 250 lines of data
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println(line);
			}
		}
		c.close();
		System.out.println("Create Top 250 table complete!");
	}

	/**
	 * 
	 */
	public void parseBottom(File f) throws FileNotFoundException, SQLException,
			IOException, ClassNotFoundException {
		System.out.println("Creating bottom 10 table...");
		createBottom10(); // create the table
		Scanner c = new Scanner(f, "latin1");
		// skip useless data
		while (!c.nextLine().contains("BOTTOM 10 MOVIES")) {
		}
		c.nextLine();
		c.nextLine(); // skip two more lines
		int i = 0;
		while (c.hasNextLine() && i < 10) {
			String line = c.nextLine();
			line = line.replace("'", "''");
			String[] values = new String[6];
			for (String s : values) { // initiate the array
				s = new String();
			}
			try {
				String[] split = line.split("\\s{2,}"); // split by two or more
														// spaces
				values[0] = split[1];
				values[1] = split[2];
				values[2] = split[3];
				String sub = split[4]; // continue to split the later part
				if (!sub.contains("\"")) {
					String[] subsplit = sub.split("[\\(\\)]"); // split the
																// parenthesis
					values[3] = subsplit[0];
					values[4] = subsplit[1].replaceAll("[^0-9]", "");;
					values[5] = "NULL";
				} else {
					String[] subsplit = sub.split("[\\\"\\{\\}]"); // split " "
																	// and { }
					values[3] = subsplit[1];
					values[4] = subsplit[2].replaceAll("[^A-Za-z0-9]", "");
					values[5] = subsplit[3];
				}
				insert(bottom10table, values);
				i++; // only need 10 lines of data
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println(line);
			}
		}
		c.close();
		System.out.println("Creating bottom 10 table complete!");

	}
	
	

	/**
	 * problem code sample 1:      1.....0080      78   7.8  Zohar (Who''s Who) (2012)
	 * problem code sample 2:      0001211000     150   4.8  s/y Glädjen (1989)
	 */
	public void parseAll(File f) throws FileNotFoundException, SQLException,
			IOException, ClassNotFoundException {
		System.out.println("Creating ratings table...");
		createAll(); // create the table
		Scanner c = new Scanner(f, "latin1");
		// skip useless data
		while (!c.nextLine().contains("MOVIE RATINGS REPORT")) {
		}
		c.nextLine();
		c.nextLine(); // skips two more lines
		while (c.hasNextLine()) {
			String line = c.nextLine();
			line = line.replace("'", "''");
			if (line.contains("----------------------")) {
				c.close();
				System.out.println("Create ratings table complete!");
				return;
			}
			String[] values = new String[6];
			for (String s : values) { // initiate the array
				s = new String();
			}
			try {
				String[] split = line.split("\\s{2,}"); // split by two or more
														// spaces
				values[0] = split[1];
				values[1] = split[2];
				values[2] = split[3];
				String sub = split[4]; // continue to split the later part
				String[] subsplit = sub.split("[\\(\\)]"); // split the
															// parenthesis
				values[3] = subsplit[0].replace("\"", "");
				values[4] = subsplit[1].replaceAll("[^0-9]", "");
				values[5] = (subsplit.length > 2) ? subsplit[2].replaceAll(
						"[\\{\\}]", "") : "NULL";
				insert(alltable, values);
			} catch (SQLException e) {
				System.err.println(e);
				System.err.println(line);
			}

		}
		c.close();
		System.out.println("Create ratings table complete!");

	}

	/**
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * 
	 */
	public void createTop250() throws ClassNotFoundException, SQLException,
			IOException {
		top250table = "top250ratings";
		String[][] columnName = new String[6][2];
		columnName[0][0] = "Distribution";
		columnName[0][1] = "varchar(64)";
		columnName[1][0] = "Votes";
		columnName[1][1] = "int";
		columnName[2][0] = "Rank";
		columnName[2][1] = "double";
		columnName[3][0] = "Title";
		columnName[3][1] = "varchar(256)";
		columnName[4][0] = "Year";
		columnName[4][1] = "int";
		columnName[5][0] = "Description";
		columnName[5][1] = "varchar(256)";
		dbm.createTable(top250table, columnName);
	}

	/**
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * 
	 */
	public void createBottom10() throws ClassNotFoundException, SQLException,
			IOException {
		bottom10table = "bottom10ratings";
		String[][] columnName = new String[6][2];
		columnName[0][0] = "Distribution";
		columnName[0][1] = "varchar(64)";
		columnName[1][0] = "Votes";
		columnName[1][1] = "int";
		columnName[2][0] = "Rank";
		columnName[2][1] = "double";
		columnName[3][0] = "Title";
		columnName[3][1] = "varchar(256)";
		columnName[4][0] = "Year";
		columnName[4][1] = "int";
		columnName[5][0] = "Description";
		columnName[5][1] = "varchar(256)";
		dbm.createTable(bottom10table, columnName);

	}

	/**
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * 
	 */
	public void createAll() throws ClassNotFoundException, SQLException,
			IOException {
		alltable = "ratings";
		String[][] columnName = new String[6][2];
		columnName[0][0] = "Distribution";
		columnName[0][1] = "varchar(64)";
		columnName[1][0] = "Votes";
		columnName[1][1] = "int";
		columnName[2][0] = "Rank";
		columnName[2][1] = "double";
		columnName[3][0] = "Title";
		columnName[3][1] = "varchar(256)";
		columnName[4][0] = "Year";
		columnName[4][1] = "int";
		columnName[5][0] = "Description";
		columnName[5][1] = "varchar(256)";
		dbm.createTable(alltable, columnName);
	}

	/**
	 * 
	 * @param values
	 * @throws SQLException
	 */
	public void insert(String table, String[] values) throws SQLException {
		dbm.insert(table, values);
	}

}
