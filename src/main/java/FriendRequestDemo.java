
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
public class FriendRequestDemo extends Application {

    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    private final String host = "localhost";
    private final TextArea ta1 = new TextArea();
    private ArrayList<String> recommendationList = new ArrayList<>();
    private static final ArrayList<String> pendingList = new ArrayList<>();
    private static final ArrayList<String> friendList = new ArrayList<>();
    private static FriendRequestPane friendRequestPane;
    public  static FriendListPane friendListPane;

    protected BorderPane getPane() {
        HBox paneForButtons = new HBox(20);
        Button fRecommendButton = new Button("Friend Recommender");
        Button RequestListButton = new Button("Friend Request");
        Button fListButton = new Button("Friend List");
        paneForButtons.getChildren().addAll(fRecommendButton, RequestListButton, fListButton);
        paneForButtons.setAlignment(Pos.CENTER);
        paneForButtons.setStyle("-fx-border-color: green");

        HBox paneForButtons2 = new HBox(20);
        Button noButton = new Button("No");
        Button yesButton = new Button("Yes");
        paneForButtons2.getChildren().addAll(noButton, yesButton);
        paneForButtons2.setAlignment(Pos.CENTER);
        paneForButtons2.setStyle("-fx-border-color: green");

        BorderPane pane = new BorderPane();
        pane.setTop(paneForButtons);
        pane.setBottom(paneForButtons2);

        Pane paneForUserRecommendation = new Pane();
        ta1.setEditable(false);
        paneForUserRecommendation.getChildren().add(ta1);
        ScrollPane wrapperPane = new ScrollPane();
        wrapperPane.setFitToWidth(true);
        wrapperPane.setFitToHeight(true);
        wrapperPane.setContent(paneForUserRecommendation);
        pane.setCenter(wrapperPane);

        friendRequestPane = new FriendRequestPane();
        friendListPane = new FriendListPane();

        yesButton.setOnAction((ActionEvent e) -> {
            String user_Data = recommendationList.remove(0);
            user_Data = user_Data.split("\n")[1].split(" ")[2];
            friendRequestPane.sendFriendRequest(user_Data);
            friendList.add(user_Data);
            ta1.setText(recommendationList.get(0));
            try {
                toServer.writeUTF(Login.accountOwner + "->" + user_Data);
                toServer.flush();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });

        fRecommendButton.setOnAction((ActionEvent e) -> {
            wrapperPane.setFitToWidth(true);
            wrapperPane.setFitToHeight(true);
            wrapperPane.setContent(paneForUserRecommendation);
            pane.setCenter(wrapperPane);
        });

        RequestListButton.setOnAction((ActionEvent e) -> {
            wrapperPane.setFitToHeight(true);
            wrapperPane.setContent(friendRequestPane);
            pane.setCenter(wrapperPane);
        });

        fListButton.setOnAction((ActionEvent e) -> {
            wrapperPane.setFitToHeight(true);
            wrapperPane.setContent(friendListPane);
            pane.setCenter(wrapperPane);
        });

        return pane;
    }

    @Override
    public void start(Stage primaryStage) {
        recommendationList = RDatabase.getUserData();
        ta1.setText(recommendationList.get(0));
        Scene scene = new Scene(getPane(), 450, 295);
        primaryStage.setTitle(Login.accountOwner);
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            Socket socket = new Socket("localhost", 8002);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            toServer.writeUTF(Login.accountOwner);
            new Thread(new ServerHandler(socket, fromServer, toServer)).start();
        } catch (IOException ex) {
        }
    }

    private static class ServerHandler implements Runnable {

        private final Socket socket;
        private final DataInputStream fromServer;
        private final DataOutputStream toServer;
        private String server_Input;

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
                    System.out.println(server_Input);
                    if (!server_Input.contains("-accept")) {
                        Platform.runLater(() -> {
                            friendRequestPane.receiveFriendRequest(server_Input, socket, pendingList, friendList);
                            
                        });
                    }
                    else {
                        Platform.runLater(() -> {
                            friendListPane.confirmFriendRequest(server_Input, pendingList, friendList);
                            friendRequestPane.updateList(server_Input.split("-")[0]);
                        });
                    }
                } catch (IOException ex) {
                    System.err.println(ex.toString());
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
