
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
public class ChatPane extends BorderPane {

    TextField tf;
    ScrollPane scrollPane;
    VBox root;

    public ChatPane() {
        root = new VBox();
        root.setPadding(new Insets(10));
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-border-color: none");
        setCenter(scrollPane);
        scrollPane.setContent(root);
        setPadding(new Insets(5, 5, 5, 5));
    }

    public void sendChat(String text) {
        tf = new TextField();
        tf.setEditable(false);
        tf.setStyle("-fx-background-color: none");
        tf.setText(text);
        tf.setAlignment(Pos.CENTER_RIGHT);
        root.getChildren().add(tf);
    }

    public void receiveChat(String text) {
        tf = new TextField();
        tf.setEditable(false);
        tf.setStyle("-fx-background-color: none");
        tf.setText(text);
        root.getChildren().add(tf);
    }

}
