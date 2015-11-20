@author: Zhaohong Jin

This software parses data from imdb online database ftp://ftp.fu-berlin.de/pub/misc/movies/database/ and put them into oracle mysql database(or other similar products).

1. Manually download the files from ftp://ftp.fu-berlin.de/pub/misc/movies/database/, unzip them, and put them under the "database" folder.

2. Go to the "database" folder, open the database.propertoes file, and configure the database configuration. If unclear about how to configure the file, check which database product is currently being used on the computer. Then, search online for the specific database configuration syntax for the 
                jdbc.url
part.
				jdbc.username
				jdbc.password
are your username and password for the current database.

3. run "start"

4. To report a problem or need any assistance, email zhjin@berkeley.edu
