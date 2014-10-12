package akb428.tkws;

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.sun.org.apache.xerces.internal.utils.ConfigurationError;

public class HbaseSample {
	
	public static void main(String [] args) {
		
		 Configuration conf = HBaseConfiguration.create();
		 
		 conf.addResource("/usr/local/Cellar/hbase/0.98.6.1/libexec/conf/hbase-policy.xml");
		 conf.addResource("/usr/local/Cellar/hbase/0.98.6.1/libexec/conf/hbase-site.xml");
		 
		 
		 // test
		 try {
			HTable table  = new HTable(conf, "test");
			
			Random rnd = new Random();
			byte[] key = Bytes.toBytes( String.valueOf(rnd.nextInt(1000000) ));
			byte[] val = Bytes.toBytes("valuuuuuuuuu");
			
			byte[] family = Bytes.toBytes("data");
			byte[] column = Bytes.toBytes("column");
			
			Put p  = new Put(key);
			p.add(family, column, val);
			
			table.put(p);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
