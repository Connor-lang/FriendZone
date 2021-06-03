/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author HP
 */
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.PriorityQueue;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ChattingServer extends Application {

    private static TextArea ta;
    private Socket socket;
    private int count = 0;
    private static DataInputStream inputFromUser;
    private static DataOutputStream outputToUser;
    static HashMap<String, ClientHandler> activeUsers = new HashMap<>();
    static HashMap<String, PriorityQueue<String>> keptMessages = new HashMap<>();
    static HashMap<String, PriorityQueue<String>> keptKeys = new HashMap<>();
    private ClientHandler clientHandler;

    private void createConnection() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> ta.appendText(new Date()
                        + " : Server started at socket 8000\n"));
                while (true) {
                    Platform.runLater(()
                            -> ta.appendText("Server started at " + new Date() + '\n'));
                    socket = serverSocket.accept();
                    count++;
                    Platform.runLater(() -> {
                        ta.appendText(new Date() + " User " + count + " joined the server" + '\n');
                        ta.appendText("User " + count + "'s IP address " + socket.getInetAddress().getHostAddress() + '\n');
                    });
                    inputFromUser = new DataInputStream(socket.getInputStream());
                    outputToUser = new DataOutputStream(socket.getOutputStream());
                    String clientName = inputFromUser.readUTF();
                    String recipientName = inputFromUser.readUTF();

                    clientHandler = new ClientHandler(socket, clientName, inputFromUser, outputToUser);
                    if (activeUsers.containsKey(clientName)) {
                        activeUsers.replace(clientName, clientHandler);
                        if (keptMessages.containsKey(recipientName + clientName)) {
                            while (!keptMessages.get(recipientName + clientName).isEmpty()) {
                                outputToUser.writeUTF(keptMessages.get(recipientName + clientName).remove());
                                outputToUser.writeUTF(keptKeys.get(recipientName + clientName).remove());                    
                            }
                            keptMessages.remove(recipientName + clientName);
                            keptKeys.remove(recipientName + clientName);
                        }
                    }
                    else {
                        activeUsers.put(clientName, clientHandler);
                    }
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                }
            } catch (IOException ex) {
                System.out.println("IOException");
            }
        }).start();
    }

    @Override
    public void start(Stage primaryStage) {
        ta = new TextArea();
        ta.setEditable(false);
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();
        createConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class ClientHandler implements Runnable {

        private final String name;
        private final DataInputStream inputFromUser;
        private final DataOutputStream outputToUser;
        private final Socket socket;
        boolean isLoggedIn;

        public ClientHandler(Socket socket, String name, DataInputStream inputFromUser,
                DataOutputStream outputToUser) {
            this.socket = socket;
            this.inputFromUser = inputFromUser;
            this.outputToUser = outputToUser;
            this.name = name;
            this.isLoggedIn = true;
        }

        @Override
        public void run() {
            String user_Input;
            String key;
            while (true) {

                try {
                    user_Input = inputFromUser.readUTF();
                    key = inputFromUser.readUTF();
                    String messageToSend = user_Input.substring(0, user_Input.lastIndexOf("|"));
                    String recipient = user_Input.substring(user_Input.lastIndexOf("|") + 1, user_Input.length());

                    if (messageToSend.equals("logout")) {
                        ta.appendText("Logging out\n");
                        this.isLoggedIn = false;
                        this.socket.close();
                        break;
                    }

                    ta.appendText("Text received from " + name + " to " + recipient + " : " + messageToSend + '\n');

                    for (ClientHandler mc : ChattingServer.activeUsers.values()) {
                        if (mc.name.equals(recipient) && mc.isLoggedIn) {
                            mc.outputToUser.writeUTF(messageToSend);
                            mc.outputToUser.writeUTF(key);
                            break;
                        } else if (mc.name.equals(recipient) && !mc.isLoggedIn) {
                            ta.appendText("Saving to priority queue\n");
                            saveMessage(name, recipient, messageToSend, key);
                        }
                    }

                } catch (IOException ex) {
                }
            }
        }
    }

    private static void saveMessage(String sender, String receiver, String messageToSend, String key) {
        String name = sender + receiver;
        if (keptMessages.containsKey(name)) {
            keptMessages.get(name).add(messageToSend);
            keptKeys.get(name).add(key);
        } else {
            keptMessages.put(name, new PriorityQueue<>());
            keptKeys.put(name, new PriorityQueue<>());
            keptMessages.get(name).add(messageToSend);
            keptKeys.get(name).add(key);
        }
    }
}
