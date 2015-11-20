package com.haas.imdb.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.haas.imdb.utils.*;

/**
 * 
 * @author Zhaohong Jin
 *
 */
public class Main {
	private File[] file;
	private static String dir = System.getProperty("user.dir");

	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		//File[] files = new File[1];
		//files[0] = new File(dir + "/database/composers.list");
		try {
			//parseAllFiles(files);
			parseAllFiles(getAllFiles(dir + "\\database"));
	    //prevent the program from crashing.
	    //Catch every possible exception.
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * 
	 * @param folder
	 * @return
	 */
	public static File[] getAllFiles(String folder) {
		File[] files = new File(folder).listFiles();
		return files;
	}

	/**
	 * 
	 * @param files
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 */
	public static void parseAllFiles(File[] files) throws ClassNotFoundException, FileNotFoundException, SQLException, IOException {
		for (File f : files) {
			String name = f.getName();
			if (name.equals("actors.list")) {
				Actors actors = new Actors();
				actors.parse(f);
			} else if (name.equals("actresses.list")) {
                Actress actress = new Actress();
                actress.parse(f);
			} else if (name.equals("ratings.txt") || name.equals("ratings.list")) {
				Ratings ratings = new Ratings();
				ratings.parse(f);
			} else if (name.equals("directors.list")) {
				Directors directors = new Directors();
				directors.parse(f);
			} else if (name.equals("genres.list")) {
				Genres genres = new Genres();
				genres.parse(f);
			} else if (name.equals("movies.list")) {
				Movies movies = new Movies();
				movies.parse(f);
			} else if (name.equals("countries.list")) {
				Countries countries = new Countries();
				countries.parse(f);
			} else if (name.equals("distributors.list")) {
				Distributors distributors = new Distributors();
				distributors.parse(f);
			} else if (name.equals("producers.list")) {
				Producers producers = new Producers();
				producers.parse(f);
			} else if (name.equals("composers.list")) {
				Composers composers = new Composers();
				composers.parse(f);
			}
		}
	}
}
