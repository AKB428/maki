package akb428.maki;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import twitter4j.FilterQuery;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import akb428.maki.dao.IMediaUrlDao;
import akb428.maki.dao.h2.MediaUrlDao;
import akb428.maki.model.HbaseConfModel;
import akb428.maki.model.MediaConfModel;
import akb428.maki.model.TwitterModel;
import akb428.maki.thread.MediaDownloderThread;
import akb428.util.Calender;

public class SearchMain {

	public static void main(String[] args) throws ClassNotFoundException,
			JsonParseException, JsonMappingException, IOException {

		TwitterModel twitterModel = null;
		HbaseConfModel hbaseConfModel;
		MediaConfModel mediaConfModel;

		// 追記モード
		File csv = new File("logs/" +  Calender.yyyymmddhhmmss() +".csv"); // CSVデータファイル
	    BufferedWriter bufferedWriter 
	        = new BufferedWriter(new FileWriter(csv, false)); 
		
		// TODO 設定ファイルでMariaDBなどに切り替える
		// Class.forName("org.sqlite.JDBC");
		Class.forName("org.h2.Driver");

		if (args.length != 2) {
			try {
				twitterModel = TwitterConfParser
						.readConf("conf/twitter_conf.json");
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} else {
			try {
				twitterModel = TwitterConfParser.readConf(args[1]);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		Configuration conf = HBaseConfiguration.create();
		ApplicationConfParser applicationConfParser = new ApplicationConfParser(
				"./conf/application.json");
		hbaseConfModel = applicationConfParser.getHbaseConfModel();
		mediaConfModel = applicationConfParser.getMediaConfModel();

		if (hbaseConfModel.isExecute()) {
			List<String> resources = hbaseConfModel.getResource();
			for (String resource : resources) {
				conf.addResource(resource);
			}
		}

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.setOAuthConsumer(twitterModel.getConsumerKey(),
				twitterModel.getConsumerSecret());
		twitterStream.setOAuthAccessToken(new AccessToken(twitterModel
				.getAccessToken(), twitterModel.getAccessToken_secret()));

		twitterStream.addListener(new MyStatusAdapter(applicationConfParser,
				conf, bufferedWriter));
		ArrayList<String> track = new ArrayList<String>();
		track.addAll(Arrays.asList(args[0].split(",")));

		String[] trackArray = track.toArray(new String[track.size()]);

		// 400のキーワードが指定可能、５０００のフォローが指定可能、２５のロケーションが指定可能
		twitterStream.filter(new FilterQuery(0, null, trackArray));

		if (mediaConfModel.isExecute()) {
			MediaDownloderThread mediaDownloderThread = new MediaDownloderThread();
			mediaDownloderThread.start();
		}
	}

}

class MyStatusAdapter extends StatusAdapter {

	HbaseConfModel hbaseConfModel;
	MediaConfModel mediaConfModel;
	Configuration hbaseConf;
	BufferedWriter bufferedWriter;

	public MyStatusAdapter(ApplicationConfParser applicationConfParser,
			Configuration conf, BufferedWriter bufferedWriter) {
		hbaseConfModel = applicationConfParser.getHbaseConfModel();
		mediaConfModel = applicationConfParser.getMediaConfModel();
		hbaseConf = conf;
		this.bufferedWriter = bufferedWriter;
	}

	public void onStatus(Status status) {
		System.out.println("@" + status.getUser().getScreenName());
		System.out.println(status.getId());
		System.out.println(status.getText());
		System.out.println(status.getSource());
		System.out.println(status.getRetweetCount());
		System.out.println(status.getFavoriteCount());
		System.out.println(status.getCreatedAt());

		if (hbaseConfModel.isExecute()) {
			// HBaseに登録する
			try {
				registHbase(status);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (mediaConfModel.isExecute()) {
			registMediaUrl(status);
		}
		
		writTwitterStreamToCSV(status);
		
	}
	
	
	public void writTwitterStreamToCSV(Status status) {
	      try {
			bufferedWriter.write("\"" +status.getId() 
					+ "\",\"" + status.getUser().getScreenName() 
					+ "\",\"" + status.getText()
					+ "\",\"" + status.getSource() 
					+ "\",\"" + status.getRetweetCount() 
					+ "\",\"" + status.getFavoriteCount() 
					+ "\",\"" + status.getCreatedAt()
					+ "\""
					);
		    bufferedWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void registMediaUrl(Status status) {
		// TODO 設定ファイルでMariaDBなどに切り替える
		IMediaUrlDao dao = new MediaUrlDao();

		MediaEntity[] arrMedia = status.getMediaEntities();

		if (arrMedia.length > 0) {
			System.out.println("メディアURLが見つかりました");
		}
		for (MediaEntity media : arrMedia) {
			// http://kikutaro777.hatenablog.com/entry/2014/01/26/110350
			System.out.println(media.getMediaURL());

			if (!dao.isExistUrl(media.getMediaURL())) {
				// TODO keywordを保存したいがここでは取得できないため一時的にtextをそのまま保存
				dao.registUrl(media.getMediaURL(), status.getText(), status
						.getUser().getScreenName());
			}
		}
	}

	public void registHbase(Status status) throws IOException {
		// TODO テーブルを作成するロジックをかく
		HTable table = new HTable(hbaseConf, "twitter_01");

		byte[] key = Bytes.toBytes(String.valueOf(status.getId()));
		byte[] family = Bytes.toBytes("data");
		Put p = new Put(key);
		p.add(family, Bytes.toBytes("Text"), Bytes.toBytes(status.getText()));
		p.add(family, Bytes.toBytes("ScreenName"),
				Bytes.toBytes(status.getUser().getScreenName()));
		p.add(family, Bytes.toBytes("Source"),
				Bytes.toBytes(status.getSource()));
		p.add(family, Bytes.toBytes("RetweetCount"),
				Bytes.toBytes(status.getRetweetCount()));
		p.add(family, Bytes.toBytes("FavoriteCount"),
				Bytes.toBytes(status.getFavoriteCount()));
		table.put(p);
	}
}
