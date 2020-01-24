package com.learning.threads.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TelcelServer implements Runnable {

    private ServerSocket serverSocket;

    public TelcelServer() throws IOException {
        serverSocket = new ServerSocket(5000);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Socket client = serverSocket.accept();
                new Thread(new TelcelClientHandler(client)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
