package com.learning.threads.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class TelcelClientChannel extends Thread {
    private OutputStreamWriter outputStreamWriter;
    private Map<String, TelcelClient> requests = new HashMap<>();
    private Semaphore semaphore;

    public TelcelClientChannel() throws InterruptedException {
        this.semaphore = new Semaphore(1);
        semaphore.acquire();
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 5000);

            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            semaphore.release();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String response;

                while ((response = reader.readLine()) != null) {
                    System.out.println("Received: " + response);
                    TelcelClient telcelClient = requests.get(response.trim());

                    telcelClient.setResponse(response);

                    synchronized (telcelClient) {
                        telcelClient.notify();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(String echoToken, TelcelClient telcelClient) {
        try {
            System.out.println("Sent: " + echoToken);
            semaphore.acquire();
            outputStreamWriter.write(echoToken);
            outputStreamWriter.write(System.lineSeparator());
            outputStreamWriter.flush();
            requests.put(echoToken, telcelClient);
            semaphore.release();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
