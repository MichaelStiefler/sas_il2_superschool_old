package com.sas1946.fac.tools;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class RConClient {

    public static final int RCONCLIENT_WAITING           = 0;
    public static final int RCONCLIENT_MESSAGE_RECEIVED  = 2;
    public static final int RCONCLIENT_MESSAGE_SENT      = 3;
    public static final int RCONCLIENT_CONNECTED         = 1;
    public static final int RCONCLIENT_CONNECTION_FAILED = -1;
    public static final int RCONCLIENT_READ_ERROR        = -2;
    public static final int RCONCLIENT_WRITE_ERROR       = -3;

    public static enum COMMAND {
        none, mystatus, auth, getconsole, getplayerlist, serverstatus, kick, ban, banuser, unbanall, serverinput, sendstatnow, cutchatlog, chatmsg, opensds, spsget, spsreset, shutdown
    };

    private static final RConClient         instance;
    private AsynchronousSocketChannel       rConSockChannel;
    private CallBack<Void, Integer, String> rConCallback;
    private COMMAND                         lastCommand;

    private RConClient() {
    }

    public static RConClient getInstance() {
        return instance;
    }

    public void setRConCallback(CallBack<Void, Integer, String> rConCallback) {
        this.rConCallback = rConCallback;
    }

    private void callback(int val1, String val2) {
        this.rConCallback.call(val1, val2);
    }

    public COMMAND getLastCommand() {
        return this.lastCommand;
    }

    public void spsget() {
        this.lastCommand = COMMAND.spsget;
        RConClient.this.doStartWrite(this.rConSockChannel, "spsget");
    }

    public void auth(String user, String pass) {
        this.lastCommand = COMMAND.auth;
        RConClient.this.doStartWrite(this.rConSockChannel, "auth " + user + " " + pass);
    }

    public void connect(String host, int port) {
        try {
            this.doConnect(host, port);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void doConnect(String host, int port) throws IOException {

        final AsynchronousSocketChannel sockChannel = AsynchronousSocketChannel.open();

        sockChannel.connect(new InetSocketAddress(host, port), sockChannel, new CompletionHandler<Void, AsynchronousSocketChannel>() {
            @Override
            public void completed(Void result, AsynchronousSocketChannel channel) {
                RConClient.this.callback(RCONCLIENT_CONNECTED, "remote console connected");
                RConClient.this.rConSockChannel = channel;

                RConClient.this.doStartRead(channel);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                RConClient.this.callback(RCONCLIENT_CONNECTION_FAILED, "failed to connect to remote console");
                RConClient.this.rConSockChannel = null;
            }

        });

    }

    private void doStartRead(final AsynchronousSocketChannel sockChannel) {
        final ByteBuffer buf = ByteBuffer.allocate(2048);

        sockChannel.read(buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel) {
                buf.flip();
                final short len = (short) (((buf.get() & 0xFF) + ((buf.get() & 0xFF) * 256)) - 1);

                if (len > 0) {
                    final StringBuilder message = new StringBuilder(len);
                    for (int i = 0; i < len; i++) {
                        message.append((char) buf.get());
                    }
                    RConClient.this.callback(RCONCLIENT_MESSAGE_RECEIVED, "Read message:" + message);
                } else {
                    RConClient.this.callback(RCONCLIENT_WAITING, "waiting...");
                }
                RConClient.this.doStartRead(channel);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                RConClient.this.callback(RCONCLIENT_READ_ERROR, "fail to read message from server");
            }

        });

    }

    private void doStartWrite(final AsynchronousSocketChannel sockChannel, final String message) {
        final int messageLen = message.length() + 1;
        final ByteBuffer buf = ByteBuffer.allocate(messageLen + 2);
        buf.clear();
        buf.put((byte) (messageLen % 256));
        buf.put((byte) (messageLen / 256));
        buf.put(message.getBytes());
        buf.put((byte) 0);

        buf.flip();
        sockChannel.write(buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel) {
                RConClient.this.callback(RCONCLIENT_MESSAGE_SENT, "write message completed, result=" + result);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                RConClient.this.callback(RCONCLIENT_WRITE_ERROR, "Fail to write the message to server");
            }
        });
    }

    static {
        instance = new RConClient();
        instance.lastCommand = COMMAND.none;
        instance.rConSockChannel = null;
    }

}
