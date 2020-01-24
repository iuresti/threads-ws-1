package com.learning.threads.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TelcelClientHandler implements Runnable {

    private BufferedReader inputStreamReader;
    private OutputStreamWriter outputStreamWriter;
    private InputStream inputStream;

    public TelcelClientHandler(Socket client) throws IOException {
        inputStreamReader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
        outputStreamWriter = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);
    }

    @Override
    public void run() {
        String request;
        Random random = new Random();

        try {
            while ((request = inputStreamReader.readLine()) != null) {
                TimeUnit.SECONDS.sleep(random.nextInt(2) + 1);
                outputStreamWriter.write(request + System.lineSeparator());
                outputStreamWriter.flush();
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
