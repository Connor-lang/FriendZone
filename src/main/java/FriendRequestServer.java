
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author HP
 */
public class FriendRequestServer extends Application {

    private static TextArea ta;
    private Socket socket;
    private int count = 0;
    private DataInputStream inputFromUser;
    private DataOutputStream outputToUser;
    private ClientHandler clientHandler;
    static HashMap<String, ClientHandler> activeUsers = new HashMap<>(); 

    private void createConnection() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8002);
                Platform.runLater(() -> ta.appendText(new Date()
                        + " : Server started at socket 8002\n"));
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
                    clientHandler = new ClientHandler(socket, clientName, inputFromUser, outputToUser);
                    activeUsers.put(clientName, clientHandler);
                    Thread thread = new Thread(clientHandler); 
                    thread.start();
                }
            } catch (IOException ex) {
                Logger.getLogger(FriendRequestServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    @Override
    public void start(Stage primaryStage) {
        ta = new TextArea();
        ta.setEditable(false);
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Friend Request Server");
        primaryStage.setScene(scene);
        primaryStage.show();
        createConnection();
    }

    private class ClientHandler implements Runnable {

        private final DataInputStream inputFromUser;
        private final DataOutputStream outputToUser;
        private final String name;
        private final Socket socket;

        public ClientHandler(Socket socket, String name, DataInputStream inputFromUser,
                DataOutputStream outputToUser) {
            this.socket = socket;
            this.name = name; 
            this.inputFromUser = inputFromUser;
            this.outputToUser = outputToUser;
        }

        @Override
        public void run() {
            String user_Input;
            while (true) {
                try {
                    user_Input = inputFromUser.readUTF();
                    String recipient = user_Input.split("->")[1];
                    if(recipient.contains("-")) 
                        recipient = recipient.split("-")[0];
                    ta.appendText("Friend request from " + user_Input + '\n');
                    for (ClientHandler mc: FriendRequestServer.activeUsers.values()) {
                        if(mc.name.equals(recipient)) {
                            ta.appendText("Sending to " + recipient + "\n");
                            mc.outputToUser.writeUTF(user_Input);
                        }
                    }
                } catch (IOException ex) {
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
