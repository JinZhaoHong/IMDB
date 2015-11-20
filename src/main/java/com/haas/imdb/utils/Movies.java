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
public class Movies implements Parser {
	private DBManager dbm = new DBManager();
	private String table;

	/**
	 * 
	 */
	public void parse(File f) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException {
		System.out.println("Creating movies table...");
		create();
		Scanner c = new Scanner(f, "latin1");

		while (!c.nextLine().contains("MOVIES LIST")) {
		} // skip lines with no data
		c.nextLine();
		c.nextLine(); // skip two more lines to reach data
		while (c.hasNextLine()) {
			String line = c.nextLine();
			line = line.replace("'", "''");
			if (line.contains("------------------------")) {
				c.close();
				System.out.println("Create movies table complete!");
			}
			String[] values = new String[4];
			for (String s : values) {
				s = new String();
			}
			try {
				if (line.contains("\"") || line.contains("{")) {
					String[] split = line.split("[\\\"\\{\\}]");
					if (split.length > 3) {
						values[0] = split[1];
						values[1] = split[2].replaceAll("[^0-9]", "");
						if (values[1].length() < 1)
							values[1] = "0000";
						values[2] = split[3];
						values[3] = split[4].replaceAll("[\\t+]", "");
					} else {
						String[] newSplit = line.split("[\\(\\)]");
						values[0] = newSplit[0].replace("\"", "");
						values[1] = newSplit[1].replaceAll("[^0-9]", "");
						if (values[1].length() < 1)
							values[1] = "0000";
						values[2] = "NULL";
						values[3] = newSplit[2].replace("[\\t+]", "");
					}
				} else {
					String[] split = line.split("[\\t+]");
					values[3] = split[1].replace("[\\t+]", "");
					String cut = new String();
					for (int i = 0; i < split.length - 1; i++) {
						cut += split[i];
					}
					String[] subsplitted = cut.split("[\\(\\)]");
					values[0] = subsplitted[0];
					values[1] = subsplitted[2].replaceAll("[^0-9]", "");
					if (values[1].length() < 1)
						values[1] = "0000";
					values[2] = "NULL";
				}
				insert(values);
			} catch (SQLException e) {
				System.err.println(e);
				System.err.println(line);
			}
		}
		c.close();
		System.out.println("Creating movies table complete!");

	}

	/**
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * 
	 */
	public void create() throws ClassNotFoundException, SQLException, IOException {
		table = "movies";
		String[][] columnName = new String[4][2];
		columnName[0][0] = "Title";
		columnName[0][1] = "varchar(128)";
		columnName[1][0] = "Year";
		columnName[1][1] = "int";
		columnName[2][0] = "Description";
		columnName[2][1] = "varchar(128)";
		columnName[3][0] = "ReleaseYear";
		columnName[3][1] = "varchar(64)";
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
