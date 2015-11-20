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
public class Distributors implements Parser {
	private DBManager dbm = new DBManager();
	private String table;

	public void parse(File f) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException {
		System.out.println("Creating distributors table...");
		create();
		Scanner c = new Scanner(f, "latin1");
		String[] value = new String[4];
		for (String s : value) {
			s = new String();
		}
		while (!c.nextLine().contains("DISTRIBUTORS LIST")) {
		}
		c.nextLine();
		while (c.hasNextLine()) {
			String line = c.nextLine();
			line = line.replace("'", "''");
			if (line.contains("--------------------")) {
				System.out.println("Create distributors table complete!");
				c.close();
				return;
			}
			try {
				if (!line.contains("{") && !line.contains("}")) {
					String[] split1 = line.split("[\\(\\)]");
					value[0] = split1[0].replaceAll("\"", "");
					value[1] = (split1[1].contains("????")) ? "0000" : split1[1].replaceAll("[^0-9]", "");
					value[2] = "NULL";
					value[3] = split1[2].replaceAll("[\\t+]", "");
				}
				else if (line.contains("{") && line.contains("}")) {
					String[] split1 = line.split("[\\\"\\{\\}]");
					value[0] = split1[1];
					value[1] = (split1[2].contains("????")) ? "0000" : split1[2].replaceAll("[^0-9]", "");
					value[2] = split1[3];
					value[3] = split1[4].replaceAll("[\\t+]", "");
				}
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
		System.out.println("Create distributors table complete!");

	}

	public void create() throws ClassNotFoundException, SQLException, IOException {
		table = "distributors";
		String[][] columnName = new String[4][2];
		columnName[0][0] = "Movie";
		columnName[0][1] = "varchar(128)";
		columnName[1][0] = "Year";
		columnName[1][1] = "int";
		columnName[2][0] = "Description";
		columnName[2][1] = "varchar(128)";
		columnName[3][0] = "Distributors";
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
