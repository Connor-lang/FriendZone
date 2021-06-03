
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
public class ClientGUI extends Application {

    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    private final String host = "localhost";
    private static ChatPane chatPane;
    private String message;
    private static java.io.File log;
    private static java.io.PrintWriter output;
    private WordPreprocessing troll;
    private static VigenereCipher vigenereCipher;

    protected BorderPane getPane() {

        HBox paneForButtons = new HBox(10);
        TextField tf = new TextField();
        tf.setPrefWidth(375);
        TextField contactInformation = new TextField();
        contactInformation.setText(Login.recipient);
        contactInformation.setEditable(false);
        contactInformation.setStyle("-fx-background-color: pink");
        Button sendButton = new Button("Send");
        sendButton.setAlignment(Pos.BASELINE_RIGHT);
        paneForButtons.getChildren().add(tf);
        paneForButtons.getChildren().add(sendButton);
        paneForButtons.setStyle("-fx-border-color: green");

        BorderPane pane = new BorderPane();
        pane.setBottom(paneForButtons);
        pane.setTop(contactInformation);

        chatPane = new ChatPane();
        pane.setCenter(chatPane);

        tf.setOnAction(e -> {
            try {
                String input = tf.getText();
                String trollInput = troll.processSentence(input);
                String key = vigenereCipher.generateKey(trollInput);
                String encryptedInput = vigenereCipher.encrypt(trollInput, key);
                String sentMessage = encryptedInput + "|" + Login.recipient;
                if (input.isEmpty()) {
                } else {
                    toServer.writeUTF(sentMessage);
                    toServer.writeUTF(key);
                    output.append(input + "\n");
                    output.close();
                    toServer.flush();
                    chatPane.sendChat(input);
                    tf.clear();
                }
                if (input.equals("logout")) 
                    System.exit(0);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });

        sendButton.setOnAction(e -> {
            try {
                String input = tf.getText();
                String trollInput = troll.processSentence(input);
                String key = vigenereCipher.generateKey(trollInput);
                String encryptedInput = vigenereCipher.encrypt(trollInput, key);
                String sentMessage = encryptedInput + "|" + Login.recipient;
                if (input.isEmpty()) {
                } else {
                    toServer.writeUTF(sentMessage);
                    toServer.writeUTF(key);
                    output.append(input + "\n");
                    output.close();
                    toServer.flush();
                    chatPane.sendChat(input);
                    tf.clear();
                }
                if (input.equals("logout")) 
                    System.exit(0);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
        return pane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(getPane(), 450, 200);
        primaryStage.setTitle(Login.accountOwner);
        primaryStage.setScene(scene);
        primaryStage.show();
        log = new java.io.File(Login.accountOwner + Login.recipient + ".txt");
        try {
            if (!log.exists()) {
                System.out.println("New File");
                log.createNewFile();
            }
            output = new java.io.PrintWriter(new java.io.FileWriter(log, true));
        } catch (IOException ex) {
        }
        troll = new WordPreprocessing();
        vigenereCipher = new VigenereCipher();

        try {
            Socket socket = new Socket("localhost", 8000);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            toServer.writeUTF(Login.accountOwner);
            toServer.writeUTF(Login.recipient);
            new Thread(new ServerHandler(socket, fromServer, toServer)).start();
        } catch (IOException ex) {
            chatPane.sendChat(ex.toString() + '\n');
        }
    }

    public static void main(String[] args) {
        launch(args);

    }

    private static class ServerHandler implements Runnable {

        private final Socket socket;
        private final DataInputStream fromServer;
        private final DataOutputStream toServer;
        private String server_Input;
        private String key;

        public ServerHandler(Socket socket, DataInputStream fromServer,
                DataOutputStream toServer) {
            this.socket = socket;
            this.fromServer = fromServer;
            this.toServer = toServer;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    server_Input = fromServer.readUTF();
                    key = fromServer.readUTF();
                    String reply = vigenereCipher.decrypt(server_Input, key);
                    output.append(reply + "|" + Login.recipient + "\n");
                    output.close();
                    Platform.runLater(() -> {
                        chatPane.receiveChat(reply);
                    });
                } catch (IOException ex) {
                }
            }
        }
    }
}
