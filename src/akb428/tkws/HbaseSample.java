package akb428.tkws;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import com.sun.org.apache.xerces.internal.utils.ConfigurationError;

public class HbaseSample {
	
	public static void main(String [] args) {
		
		 Configuration conf = HBaseConfiguration.create();
		
		 conf.addResource("");
		 conf.addResource("");
	}

}
