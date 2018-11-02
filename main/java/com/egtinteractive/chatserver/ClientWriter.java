package com.egtinteractive.chatserver;

import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientWriter extends Thread{
    private final BlockingQueue<byte[]> messageQueue;
    private final OutputStream outputStream;
    
    public ClientWriter(OutputStream outputStream) {
	this.messageQueue =  new LinkedBlockingQueue<>(10000);
	this.outputStream = outputStream;
    }
    
    @Override 
    public void run() {
	
    }
}
