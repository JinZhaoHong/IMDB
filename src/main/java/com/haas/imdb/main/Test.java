package com.haas.imdb.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {
	
	public static void main(String args[]) throws FileNotFoundException {
		String dir = System.getProperty("user.dir");
		File f = new File(dir + "/database/movies.list");
		Scanner c = new Scanner(f, "latin1");
		while (c.hasNextLine()) {
			System.out.println(c.nextLine());
		}
		c.close();
	}

}
