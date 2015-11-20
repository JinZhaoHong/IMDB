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
public class Genres implements Parser {
	private DBManager dbm = new DBManager();
	private String table;

	/**
	 * 
	 */
	public void parse(File f) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException {
		create();
		Scanner c = new Scanner(f, "latin1");

		while (!c.nextLine().contains("THE GENRES LIST")) {
		} // skip lines with no data
		c.nextLine();
		c.nextLine(); // skip two more lines to reach data
		while (c.hasNextLine()) {
			String line = c.nextLine();
			line = line.replace("'", "''");
			String[] values = new String[3];
			for (String s : values) {
				s = new String();
			}
			try {
				if (line.length() > 1) {
					String[] split = line.split("[\\t+]");
					values[2] = split[split.length - 1]; // store the genre
															// first
					String sub = new String();
					// return the left haft of the String
					for (int i = 0; i < split.length - 1; i++) {
						sub += split[i];
					}
					String[] subsplit = sub.split("[\\(\\)]");
					values[0] = subsplit[0].replaceAll("[\\\"]", "");
					values[1] = (subsplit[1].contains("?")) ? "0000" : subsplit[1].replaceAll("[^0-9]", "");

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

	}

	/**
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * 
	 */
	public void create() throws ClassNotFoundException, SQLException, IOException {
		table = "genres";
		String[][] columnName = new String[3][2];
		columnName[0][0] = "Title";
		columnName[0][1] = "varchar(128)";
		columnName[1][0] = "Year";
		columnName[1][1] = "int";
		columnName[2][0] = "Genre";
		columnName[2][1] = "varchar(64)";
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
