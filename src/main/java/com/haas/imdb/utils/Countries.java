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
 *
 */
public class Countries implements Parser {
	private DBManager dbm = new DBManager();
	private String table;

	public void parse(File f) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("Creating countries table...");
		create();
		Scanner c = new Scanner(f, "latin1");
		String[] value = new String[4];
		for (String s : value) {
			s = new String();
		}
		while (!c.nextLine().contains("COUNTRIES LIST")) {
		}
		c.nextLine();
		while (c.hasNextLine()) {
			String line = c.nextLine();
			line = line.replace("'", "''");
			if (line.contains("--------------------")) {
				System.out.println("Create countries table complete!");
				c.close();
				return;
			}
			try {
				String[] split1 = line.split("[\\t+]");

				value[3] = split1[split1.length - 1]; // store the country

				String s = new String();
				for (int i = 0; i < split1.length - 1; i++) {
					s += split1[i];
				}
				String[] split2 = s.split("[\\(\\)\\{\\}]");
				value[0] = split2[0].replaceAll("\"", "");
				value[1] = (split2[1].contains("????")) ? "0000" : split2[1].replaceAll("[^0-9]", "");
				value[2] = (split2.length > 2) ? split2[2] : "NULL";

				insert(value);
			} catch (SQLException e) {
				System.err.println(line);
				System.err.println(e);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println(line);
				System.err.println(e);
			}
		}
		c.close();
		System.out.println("Create countries table complete!");

	}

	public void create() throws ClassNotFoundException, SQLException, IOException {
		table = "countries";
		String[][] columnName = new String[4][2];
		columnName[0][0] = "Movie";
		columnName[0][1] = "varchar(128)";
		columnName[1][0] = "Year";
		columnName[1][1] = "int";
		columnName[2][0] = "Description";
		columnName[2][1] = "varchar(128)";
		columnName[3][0] = "Country";
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
