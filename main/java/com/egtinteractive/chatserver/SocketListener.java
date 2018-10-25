package com.egtinteractive.chatserver;

public interface SocketListener {
    void onConnected(Channel channel);

    void onDisconnected(Channel channel);

    void onReceived(Channel channel, String msg);
}
