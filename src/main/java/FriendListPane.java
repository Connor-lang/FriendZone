
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
public class FriendListPane extends BorderPane{
    
    VBox friendListPane;
    ScrollPane scrollPane; 
    Button friend; 

    public FriendListPane() {
        friendListPane = new VBox(); 
        friendListPane.setFillWidth(true);
        friendListPane.setPadding(new Insets(10));
        scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-border-color: none"); 
        setCenter(scrollPane);
        scrollPane.setContent(friendListPane);
        setPadding(new Insets(5, 5, 5, 5));     
    }
    
    public void confirmFriendRequest(String text, ArrayList arrayList1, ArrayList arrayList2) {
        friend = new Button(); 
        String[] senderReceiver = text.split("->");
        friend.setText(senderReceiver[0]);
        Login.recipient = senderReceiver[0];
        friendListPane.getChildren().add(friend);
        arrayList1.remove(senderReceiver[0]); 
        arrayList2.add(senderReceiver[0]);
        friend.setOnAction((ActionEvent e) -> {
            Platform.runLater(() -> {
                try {
                    new ClientGUI().start(new Stage());
                } catch (Exception ex) {
                    Logger.getLogger(FriendListPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }
    
    public void confirmFriendRequest(String text) {
        friend = new Button(); 
        String[] senderReceiver = text.split("->");
        friend.setText(senderReceiver[0]);
        Login.recipient = senderReceiver[0];
        friendListPane.getChildren().add(friend);
        friend.setOnAction((ActionEvent e) -> {
            Platform.runLater(() -> {
                try {
                    new ClientGUI().start(new Stage());
                } catch (Exception ex) {
                    Logger.getLogger(FriendListPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }
}