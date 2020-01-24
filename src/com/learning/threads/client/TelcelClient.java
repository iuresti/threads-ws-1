package com.learning.threads.client;

public class TelcelClient {

    private final TelcelClientChannel telcelClientChannel;
    private String response;

    public TelcelClient(TelcelClientChannel telcelClientChannel) {
        this.telcelClientChannel = telcelClientChannel;
    }

    public String processSale(String echoToken) {
        telcelClientChannel.sendRequest(echoToken, this);

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
