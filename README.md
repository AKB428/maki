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



