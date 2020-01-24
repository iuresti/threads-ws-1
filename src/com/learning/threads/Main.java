package com.learning.threads;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.learning.threads.client.TelcelClient;
import com.learning.threads.client.TelcelClientChannel;
import com.learning.threads.server.TelcelServer;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        TelcelClientChannel telcelClientChannel = new TelcelClientChannel();
        Thread telcelServerThread = new Thread((new TelcelServer()));

        telcelServerThread.start();
        telcelClientChannel.start();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        AtomicInteger echoToken = new AtomicInteger(1);
        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                TelcelClient telcelClient = new TelcelClient(telcelClientChannel);
                String request = String.valueOf(echoToken.getAndIncrement());
                String response = telcelClient.processSale(request);

                System.out.println(request + " <> " + response + " <> " + request.equals(response));
            });
        }

        System.out.println("Finished");
    }

}
