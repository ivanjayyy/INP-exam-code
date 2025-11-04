package com.ijse.gdse73.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// server (Console)
public class Server {
    ServerSocket serverSocket;
    List<ClientHandler> clients = new CopyOnWriteArrayList<>(); // a list to store multiple clients

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.serverSocket = new ServerSocket(4000); // server start
            System.out.println("[Server Started on port 4000]\n");

            while (true) {
                Socket socket = server.serverSocket.accept(); // accept client connections
                ClientHandler clientHandler = new ClientHandler(socket, server);
                server.clients.add(clientHandler); // add clients to the list

                Thread thread = new Thread(clientHandler);
                thread.start(); // start new threads for each client
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // broadcast msg to every client
    public void broadcast(String message) {
        System.out.println(message);

        // select clients one by one from the list
        for (ClientHandler client : clients) {
            client.sendText(message);
        }
    }

    // inner class (clients store in the list as objects of this class)
    static class ClientHandler implements Runnable {
        private final DataInputStream dataInputStream;
        private final DataOutputStream dataOutputStream;
        private final Server server;

        // inner class constructor
        public ClientHandler(Socket socket, Server server) throws IOException {
            this.server = server;

            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }

        /**
         * Runs this operation.
         */
        @Override
        public void run() {
            while (true) {
                try {
                    String message = dataInputStream.readUTF(); // read msg sent from clients

                    if (message.contains("disconnected")) {
                        server.clients.remove(this); // remove client from the list
                    }

                    server.broadcast(message);

                } catch (IOException e) {
                    server.clients.remove(this);
                    throw new RuntimeException(e);
                }
            }
        }

        public void sendText(String message) {
            try {
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
