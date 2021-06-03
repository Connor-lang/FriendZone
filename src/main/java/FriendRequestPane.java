
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author HP
 */
public class FriendRequestPane extends BorderPane {

    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    VBox friendRequestPane;
    VBox vbox;
    HBox hbox;
    ScrollPane scrollPane;
    TextField tf1, tf2;
    Button rejectButton, acceptButton;
    static HashMap<String, VBox> hashMap = new HashMap<>();

    public FriendRequestPane() {
        friendRequestPane = new VBox();
        friendRequestPane.setPadding(new Insets(10));
        scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-border-color: none");
        setCenter(scrollPane);
        scrollPane.setContent(friendRequestPane);
        setPadding(new Insets(5, 5, 5, 5));
    }

    public void sendFriendRequest(String text) {
        vbox = new VBox();
        vbox.setFillWidth(true);
        tf1 = new TextField();
        tf2 = new TextField();
        tf1.setEditable(false);
        tf2.setEditable(false);
        tf1.setText(text);
        tf2.setText("Pending");
        tf2.setStyle("-fx-background-color: blue");
        vbox.getChildren().addAll(tf1, tf2);
        friendRequestPane.getChildren().add(vbox);
        hashMap.put(text, vbox);
    }
    
    public void updateList(String text) {
        friendRequestPane.getChildren().remove(hashMap.get(text));
        hashMap.remove(text);
    }

    public void receiveFriendRequest(String text, Socket socket, ArrayList arrayList1, ArrayList arrayList2) {
        try {
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            vbox = new VBox();
            hbox = new HBox();
            acceptButton = new Button();
            rejectButton = new Button();
            acceptButton.setText("Accept");
            rejectButton.setText("Reject");
            tf1 = new TextField();
            tf1.setEditable(false);
            String[] senderReceiver = text.split("->");
            tf1.setText(senderReceiver[0]);
            hbox.getChildren().addAll(rejectButton, acceptButton);
            vbox.getChildren().addAll(tf1, hbox);
            hashMap.put(text, vbox);
            friendRequestPane.getChildren().add(vbox);
            acceptButton.setOnAction((ActionEvent e) -> {
                try {
                    toServer.writeUTF(senderReceiver[1] + "->" + senderReceiver[0] + "-accept");
                } catch (IOException ex) {
                    Logger.getLogger(FriendRequestPane.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (arrayList1.contains(senderReceiver[0])) {
                    arrayList2.add(senderReceiver[0]);
                    arrayList1.remove(senderReceiver[0]);
                } else {
                    arrayList2.add(senderReceiver[0]);
                }
                FriendRequestDemo.friendListPane.confirmFriendRequest(text);
                updateList(text);
            });
            rejectButton.setOnAction((ActionEvent e) -> {
                updateList(text);
            });
        } catch (IOException ex) {
            Logger.getLogger(FriendRequestPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
