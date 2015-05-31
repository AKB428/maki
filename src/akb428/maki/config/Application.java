package akb428.maki.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Properties;

public class Application {

	public static Properties properties = null;
	public static String searchTargetId = null;

	public static String configFilename = "./config/application.properties";

	public Application(String configFilename) throws UnsupportedEncodingException, IOException, ClassNotFoundException, SQLException {

		Application.configFilename = configFilename;
		InputStream inStream = new FileInputStream(Application.configFilename);
		properties = new Properties();
		properties.load(new InputStreamReader(inStream, "UTF-8"));

		searchTargetId = Application.properties.getProperty("twitter.searchTargetId");
	}

}
