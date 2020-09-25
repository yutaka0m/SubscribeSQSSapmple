# SQS 送信サンプル

SQSへパブリッシュするだけのサンプルプログラム

## テスト

```bash
# 適当な値を入れます
$ aws configure 
AWS Access Key ID [None]: xxxxxxxxxxxxxxxxxx
AWS Secret Access Key [None]: xxxxxxxxxxxxxxxxx

# SQS互換のイメージ起動
docker run -p 9324:9324 softwaremill/elasticmq 

# キューの作成
$ aws sqs create-queue --queue-name test --endpoint-url http://localhost:9324
{
    "QueueUrl": "http://localhost:9324/queue/test"
}

# Javaアプリケーション実行

# SQSメッセージの送信
$ aws sqs send-message --queue-url http://localhost:9324/queue/test --message-body "test" --endpoint-url http://localhost:9324
```

## イメージ作成

jibを使用しています

```bash
$ ./gradlew jibDockerBuild 

$ docker run yutaka0m/send-sqs
```

## 参考

- [Amazon SQSを利用するJavaアプリを開発する（その１）](https://qiita.com/kknmts/items/b7358b9ccb1936a915c5)
- [AWS 認証情報の使用](https://docs.aws.amazon.com/ja_jp/sdk-for-java/v1/developer-guide/credentials.html)