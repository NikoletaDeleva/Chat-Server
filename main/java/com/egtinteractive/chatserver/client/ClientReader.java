package com.egtinteractive.chatserver.client;

import java.io.InputStream;

public class ClientReader extends Thread {
    private final InputStream inputStream;
    
    public ClientReader(final InputStream inputStream) {
	this.inputStream = inputStream;
    }
    
    
    @Override 
    public void run() {
	
    }
}