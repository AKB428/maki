#maki

## Analytics Twitter Stream on Hadoop


##これはなに？

Twitter Stream を Hadoop上で解析するためのツールです

TwitterのStreaming APIを使用します。

##事前準備

Twitterの開発者アカウントを取得し

* consumer_key
* consumer_secret
* access_token
* access_token_secret

を取得し、conf/twitter_conf.jsonに書いておいてください。


##起動方法

``java -jar maki.jar private/application.properties private/twitter_conf.json``

##コンパイル方法

コードは全部Javaで書かれています。

Eclipseでプロジェクト指定するかantでコンパイルしてください。(バイナリjarはjava7用)


##実行環境

Java8+

## Google BigQueryでログファイルを解析する時のDDL

```
id: STRING ,name: STRING,tweet_text: STRING,source: STRING,retweet_count: INTEGER,favorite_count: INTEGER,created_at: STRING,latitude: STRING,longitude: STRING,media_url1: STRING,media_url2: STRING,media_url3: STRING,media_url4: STRING,unixtime: STRING
```



