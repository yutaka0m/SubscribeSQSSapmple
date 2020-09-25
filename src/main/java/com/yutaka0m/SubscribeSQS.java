package com.yutaka0m;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class SubscribeSQS {
    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        var queueUrl = "http://localhost:9324/queue/test";

        SqsClient sqsClient = SqsClient.builder()
            .credentialsProvider(DefaultCredentialsProvider.create())
            .region(Region.AP_NORTHEAST_1)
            .endpointOverride(new URI("http://localhost:9324")) // ElasticMQコンテナ向けのURL
            .build();

        for (int i = 0; i < 20; i++) {
            // 受信
            ReceiveMessageRequest receiveMessageRequest =
                ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(5)
                    .build();
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            messages.forEach(m -> System.out.println(m.body()));

            // 削除
            messages.forEach(m -> {
                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(m.receiptHandle())
                    .build();
                DeleteMessageResponse delRes =
                    sqsClient.deleteMessage(deleteMessageRequest);
            });

            Thread.sleep(10000);
        }
    }
}
