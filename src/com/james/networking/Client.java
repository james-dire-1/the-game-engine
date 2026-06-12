package com.james.networking;

import com.james.common.networking.Packet;
import templates.common.networking.PacketType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Client implements Runnable {

    private final String host;
    private final int port;

    private volatile Socket connection;
    private volatile ObjectOutputStream output;
    private volatile ObjectInputStream input;

    private final Map<PacketType, Consumer<Object[]>> readListeners = new HashMap<>();
    private volatile BiConsumer<Exception, Client> onServerDisconnectListener;

    // TODO: 2024-09-26 Why does preConnectTasks have to be done on this thread? Can in work in the run method?
    public Client(String host, int port) {
        preConnectTasks();

        this.host = host;
        this.port = port;

        instance = this;

        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * @implNote EOFException clause will run if the server side terminated the connection,
     * and the SocketException clause will run if the client side (this side) terminated the connection.
     * The difference is that the SocketException clause doesn't have the disconnect() call, which means
     * that disconnect() won't be called twice. (Remember that this clause can only run if disconnect()
     * had been called previously to forcibly close the connection, so it doesn't make sense to call it
     * again in the SocketException clause.)
     *
     * Update July 2025: Actually, there is more nuance to what is stated above. The EOFException clause
     * will run if the server was PROPERLY closed (i.e. closed by closing the streams and socket).
     * However, if the server was not properly closed, then the SocketException clause will run instead.
     * Thus, the way to determine whether a SocketException was caused by the server closing the connection
     * or by the client closing the connection is to see the exception message. A message of "Connection
     * reset" means the server closed the connection (improperly) and a message of "Socket closed" means
     * the client closed the connection. With this in mind, the code below has been updated.
     */
    @Override
    public void run() {
        boolean failed = false;

        try {
            this.connection = new Socket(InetAddress.getByName(host), port);
            this.output = new ObjectOutputStream(connection.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            failed = true;
            exceptionInConstruction(e);
        }

        if (failed) return;

        onSuccessfulConnection();

        Exception exception = null;

        try {
            while (true) {
                Packet packet = (Packet) input.readObject();
                Consumer<Object[]> listener;
                synchronized (readListeners) {
                    listener = readListeners.get(packet.type);
                }

                if (listener == null)
                    throw new RuntimeException("A listener was not set up! " + packet.type);

                listener.accept(packet.data);
            }
        } catch (EOFException e) {
            exception = e;
            disconnect();
        } catch (SocketException e) {
            exception = e;

            // If the message is not "Socket closed", then that means the exception was not thrown due to us
            // calling disconnect(). Thus, disconnect() hasn't been called yet, and so we need to call it.
            if (!e.getMessage().equals("Socket closed")) {
                e.printStackTrace();
                disconnect();
            }
        } catch (IOException | ClassNotFoundException e) {
            exception = e;
            e.printStackTrace();
            disconnect();
        } finally {
            if (onServerDisconnectListener != null) {
                onServerDisconnectListener.accept(exception, this);
            }
        }
    }

    public void preConnectTasks() { }

    public void exceptionInConstruction(IOException e) {
        e.printStackTrace();
    }

    public void onSuccessfulConnection() { }

    public void setReadListener(PacketType type, Consumer<Object[]> listener) {
        synchronized (readListeners) {
            readListeners.put(type, listener);
        }
    }

    public void setDisconnectListener(BiConsumer<Exception, Client> listener) {
        onServerDisconnectListener = listener;
    }

    public void sendPacket(Packet packet) {
        try {
            output.writeObject(packet);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            output.close();
            output.flush();
            input.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Client instance;
    public static Client get() { return instance; }

}
