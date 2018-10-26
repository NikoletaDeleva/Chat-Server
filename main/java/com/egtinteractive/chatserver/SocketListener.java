package com.egtinteractive.chatserver;

public interface SocketListener {
    public void onConnected(final Channel channel);

    public void onDisconnected(final Channel channel);

    public void onReceived(final Channel channel, final String msg);
}
