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
public class Composers implements Parser {
	private DBManager dbm = new DBManager();
	private String table;



	/**
	 * Input: Bigplume, Sandford \"The Incredible Human Journey\" (2009) {The
	 * Americas (#1.5)} (as Chief Bigplume) [Himself - Tsuu T'ina Nation]
	 */
	public void parse(File f) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException {
		System.out.println("Creating composers table...");
		create();
		Scanner c = new Scanner(f, "latin1");
		// skip lines that don't contain the data
		while (!c.nextLine().equals("THE COMPOSERS LIST")) {
		}
		for (int i = 0; i < 4; i++) {
			c.nextLine(); // skip 4 more lines
		}
		String lastAuthor = new String(); // keeps track of the last author
											// selected
		while (c.hasNextLine()) {
			String line = c.nextLine();
			line = line.replace("'", "''");
			if (line.contains("----------------------")) { // end of the file
				c.close();
				System.out.println("Creating composers table complete!");
				return;
			}
			String[] values = new String[5];
			for (String s : values) {
				s = new String();
			}
			for (String s : values) {
				s = new String();
			}
			try {
				if (!(line.length() < 1)) {
					if (line.charAt(0) != '\t') { // if the first character
													// doesn't
													// start with a tab, then it
													// start
													// with a name
						if (line.contains("\"") && !line.contains("{")) {
							String[] splitted = line.split("[\\\"\\[\\]]");
							lastAuthor = splitted[0];
							values[0] = splitted[0].replaceAll("[\\t+]", "");
							values[1] = splitted[1];
							values[2] = (splitted[2].contains("????")) ? "0000" : splitted[2].replaceAll("[^0-9]", "");
							values[3] = "NULL";
							values[4] = (splitted.length > 3) ? splitted[3] : "NULL";
						} else if (line.contains("\"") && line.contains("{")) {
							String[] splitted = line.split("[\\\"\\{\\}\\[\\]]");
							lastAuthor = splitted[0];
							values[0] = splitted[0].replaceAll("[\\t+]", "");
							values[1] = splitted[1];
							values[2] = (splitted[2].contains("????")) ? "0000" : splitted[2].replaceAll("[^0-9]", "");
							values[3] = splitted[3];
							values[4] = (splitted.length > 4) ? splitted[4].replaceAll("\\s{2,}", "") : "NULL";

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
							String[] subsplitted = cutted.split("[\\(\\)\\[\\]]");
							values[1] = subsplitted[0];
							values[2] = (subsplitted[1].contains("????")) ? "0000" : subsplitted[1].replaceAll("[^0-9]", "");
							values[3] = "NULL";
							values[4] = (subsplitted.length > 3) ? subsplitted[3] : "NULL";

						}

					} else { // first character start with a tab
						String splitted = line.replaceAll("[//t+]", "");
						if (line.contains("\"") && !line.contains("{")) {
							String[] split = splitted.split("[\\(\\)]");
							values[0] = lastAuthor;
							values[1] = split[0].replaceAll("[\"\\s{2,}]", "");
							values[2] = (split[1].contains("????")) ? "0000" : split[1].replaceAll("[^0-9]", "");
							values[3] = "Null";
							values[4] = (split.length > 2) ? split[2].replaceAll("\\s{2,}\\[\\]", "") : "NULL";
						} else if (line.contains("\"") && line.contains("{")) {
							String[] split = line.split("[\\\"\\{\\}\\[\\]]");
							values[0] = lastAuthor;
							values[1] = split[1];
							values[2] = (split[2].contains("????")) ? "0000" : split[2].replaceAll("[^0-9]", "");
							values[3] = split[3];
							values[4] = (split.length > 5) ? split[5].replaceAll("\\s{2,}", "") : "NULL";

						} else {
							String[] split = line.split("[\\(\\)]");
							values[0] = lastAuthor;
							values[1] = split[0];
							values[2] = (split[1].contains("????")) ? "0000" : split[1].replaceAll("[^0-9]", "");
							values[3] = "NULL";
							values[4] = (split.length > 3) ? split[3] : "NULL";
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
		System.out.println("Creating composers table complete!");

	}

	/**
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void create() throws ClassNotFoundException, SQLException, IOException {
		table = "composers";
		String[][] columnName = new String[5][2];
		columnName[0][0] = "Name";
		columnName[0][1] = "varchar(128)";
		columnName[1][0] = "Titles";
		columnName[1][1] = "varchar(256)";
		columnName[2][0] = "Year";
		columnName[2][1] = "int";
		columnName[3][0] = "Description";
		columnName[3][1] = "varchar(256)";
		columnName[4][0] = "CharacterName";
		columnName[4][1] = "varchar(128)";
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
