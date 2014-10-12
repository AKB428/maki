package akb428.tkws;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import akb428.tkws.model.HbaseConfModel;

import com.sun.org.apache.xerces.internal.utils.ConfigurationError;

public class HbaseSample {
	
	public static void main(String [] args) throws JsonParseException, JsonMappingException, IOException {
		
		 Configuration conf = HBaseConfiguration.create();
		 ApplicationConfParser applicationConfParser = new ApplicationConfParser("./conf/application.json");
		 HbaseConfModel hbaseConfModel = applicationConfParser.getHbaseConfModel();
		 List<String> resources = hbaseConfModel.getResource();
		 
		 for(String resource: resources) {
			 conf.addResource(resource);
		 //conf.addResource("/usr/local/Cellar/hbase/0.98.6.1/libexec/conf/hbase-policy.xml");
		 //conf.addResource("/usr/local/Cellar/hbase/0.98.6.1/libexec/conf/hbase-site.xml");
		 }
		 
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
