package com.haas.imdb.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface Parser {
	public void parse(File f) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException;
}
