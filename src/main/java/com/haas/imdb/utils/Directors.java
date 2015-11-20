package com.haas.imdb.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import com.haas.imdb.database.DBManager;

public class Directors implements Parser {
	private DBManager dbm = new DBManager();
	private String table;

	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		String s = "Bigplume, Sandford	\"The Incredible Human Journey\" (2009) {The Americas (#1.5)}  (as Chief Bigplume)  [Himself - Tsuu T'ina Nation]";
		String[] splitted = s.split("[\\\"\\{\\}]");
		for (String ss : splitted) {
			System.out.println(ss);
		}
	}

	/**
	 * Input: Bigplume, Sandford \"The Incredible Human Journey\" (2009) {The
	 * Americas (#1.5)} (as Chief Bigplume)
	 */
	public void parse(File f) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException {
		System.out.println("Creating directors table...");
		create();
		Scanner c = new Scanner(f, "latin1"); // to avoid truncate of data

		// skip lines that don't contain the data
		while (!c.nextLine().contains("THE DIRECTORS LIST")) {
		}
		for (int i = 0; i < 4; i++) {
			c.nextLine(); // skip 4 more lines
		}
		String lastAuthor = new String(); // keeps track of the last author
											// selected
		while (c.hasNextLine()) {
			String line = c.nextLine();
			line = line.replace("'", "''");
			//System.out.println(line);
			if (line.contains("---------------")) { // end of the file
				c.close();
				System.out.println("Creating directors complete!");
				return;
			}
			String[] values = new String[4];
			for (String s : values) {
				s = new String();
			}
			try {
			if (!(line.length() < 1)) {
				if (line.charAt(0) != '\t') { // if the first character doesn't
												// start with a tab, then it
												// start
												// with a name
					if (line.contains("\"") || line.contains("{")) {
						String[] splitted = line.split("[\\\"\\{\\}]");
						lastAuthor = splitted[0];
						values[0] = splitted[0].replaceAll("[\\t+]", "");
						values[1] = splitted[1];
						values[2] = splitted[2].replaceAll("[^0-9]", "");
						values[3] = (splitted.length > 3) ? splitted[3] : "NULL";
					} else { // doesn't contain "
						String[] splitted = line.split("[\\t+]");
						// String[] splitted = line.split("[\\t+\\(\\)]");
						lastAuthor = splitted[0];
						values[0] = splitted[0];
						String cutted = new String();
						// put the later part of the string back
						for (int i = 1; i < splitted.length; i++) {
							cutted += splitted[i];
						}
						String[] subsplitted = cutted.split("[\\(\\)]");
						values[1] = subsplitted[0];
						values[2] = (subsplitted[1].contains("?")) ? "0000" : subsplitted[1].replaceAll("[^0-9]", "");
						values[3] = "NULL";

					}

				} else { // first character start with a tab
					String splitted = line.replaceAll("[//t+]", "");
					if (line.contains("\"") || line.contains("{")) {
						String[] split = splitted.split("[\\(\\)]");
						values[0] = lastAuthor;
						values[1] = split[0];
						values[2] = split[1].replaceAll("[^0-9]", "");
						values[3] = split[2].replaceAll("[\\{\\}]", "");
					} else {
						String[] split = line.split("[\\(\\)]");
						values[0] = lastAuthor;
						values[1] = split[0];
						values[2] = split[1];
						values[3] = "NULL";
					}
				}
				insert(values);
			}
			} catch (SQLException e) {
				System.err.println(line);
				System.err.println(e);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println(line);
				System.err.println(e);
			}
		}

		c.close();
		System.out.println("Creating directors complete!");

	}

	/**
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void create() throws ClassNotFoundException, SQLException, IOException {
		table = "directors";
		String[][] columnName = new String[4][2];
		columnName[0][0] = "Name";
		columnName[0][1] = "varchar(128)";
		columnName[1][0] = "Titles";
		columnName[1][1] = "varchar(256)";
		columnName[2][0] = "Year";
		columnName[2][1] = "int";
		columnName[3][0] = "Description";
		columnName[3][1] = "varchar(256)";
		dbm.createTable(table, columnName);
	}

	/**
	 * 
	 * @param values
	 * @throws SQLException
	 */
	public void insert(String[] values) throws SQLException {
		dbm.insert(table, values);
	}

}
