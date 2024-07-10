package com.james.networking;

import com.james.common.networking.Packet;
import com.james.common.networking.PacketType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Client implements Runnable {

    private final String host;
    private final int port;

    private volatile Socket connection;
    private volatile ObjectOutputStream output;
    private volatile ObjectInputStream input;

    private final Map<PacketType, Consumer<Object[]>> readListeners = new HashMap<>();
    private final List<Consumer<Client>> onServerStoppedListeners = new ArrayList<>();

    public Client(String host, int port) {
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
     */
    @Override
    public void run() {
        boolean failed = false;

        try {
            this.connection = new Socket(InetAddress.getByName(host), port);
            this.output = new ObjectOutputStream(connection.getOutputStream());
            this.input = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            failed = true;
            exceptionInConstruction(e);
        }

        if (failed) return;

        onSuccessfulConnection();

        try {
            while (true) {
                Packet packet = (Packet) input.readObject();
                Consumer<Object[]> listener = readListeners.get(packet.type);

                if (listener != null) {
                    listener.accept(packet.data);
                }
            }
        } catch (EOFException e) {
            for (Consumer<Client> listener : onServerStoppedListeners) {
                listener.accept(this);
            }
            disconnect();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    public void exceptionInConstruction(IOException e) {
        e.printStackTrace();
    }

    public void onSuccessfulConnection() { }

    public void setReadListener(PacketType type, Consumer<Object[]> listener) {
        readListeners.put(type, listener);
    }

    public void addOnServerStoppedListener(Consumer<Client> listener) {
        onServerStoppedListeners.add(listener);
    }

    public void sendPacket(Packet packet) {
        try {
            output.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface Action {
        void invoke();
    }

    private static Client instance;
    public static Client get() { return instance; }

}
