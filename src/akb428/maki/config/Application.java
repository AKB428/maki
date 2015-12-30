package akb428.maki.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Application {

	public static Properties properties = null;
	public static String searchKeyword = null;
	public static String locations = null;
	public static double[][] locations2DArray = null;

	public static String configFilename = "./config/application.properties";

	public static void load(String configFilename) throws UnsupportedEncodingException, IOException, ClassNotFoundException, SQLException {

		Application.configFilename = configFilename;
		InputStream inStream = new FileInputStream(Application.configFilename);
		properties = new Properties();
		properties.load(new InputStreamReader(inStream, "UTF-8"));

		searchKeyword = Application.properties.getProperty("twitter.searchKeyword");
		locations = Application.properties.getProperty("twitter.locations");


		List <String>locationList = Arrays.asList(Application.locations.split(","));


		if (locationList.size() == 0) {
			return;
		}

		int lsize = locationList.size() / 2;
		locations2DArray = new double[lsize][lsize];

		int y = 0;
        for(int i=0; i < locationList.size(); i+=2) {
			locations2DArray[y][0]= Double.valueOf(locationList.get(i));//南西 緯度軽度
			locations2DArray[y][1]= Double.valueOf(locationList.get(i + 1));//北東  緯度軽度
			y++;
		}
	}

}
