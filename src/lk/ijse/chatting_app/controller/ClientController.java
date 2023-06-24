package lk.ijse.chatting_app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import javax.swing.text.Element;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public AnchorPane logPane;
    public AnchorPane msgPane;
    public VBox imageBox;
    @FXML
    private Label lblName;

    @FXML
    private Button sendOnAction;

    @FXML
    private TextField sendText;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField userTxt;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private String filePath;
    private ListView<ImageView> imageListView;


    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        String name = userTxt.getText();
        lblName.setText(name);

        logPane.setVisible(false);
        msgPane.setVisible(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        new Thread(()->{
            try {
                 socket = new Socket("localhost",5002);
                 dataInputStream = new DataInputStream(socket.getInputStream());
                 dataOutputStream = new DataOutputStream(socket.getOutputStream());

//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String msg = "";
                while (!msg.equals("stop")){
                     String message = dataInputStream.readUTF();
                    System.out.println(message);

                   String[] strings = message.split(":");
                    System.out.println(strings[0]);
                    System.out.println(strings[1]);

                    if (strings[1].startsWith("image")) {

                        String imagePath = strings[1].substring(5);
                        System.out.println(imagePath);

                        Platform.runLater(() -> {
                            File inputFile = new File(imagePath);
                            Image image = new Image(inputFile.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(50);
                            imageView.setFitWidth(50);
                            //VBox imageBox = new VBox();
                            VBox vBox= new VBox(15);
                            vBox.setAlignment(Pos.BOTTOM_LEFT);


                            // imageBox.setAlignment(Pos.BOTTOM_RIGHT);
                            imageBox.setAlignment(Pos.BOTTOM_RIGHT);
                            vBox.getChildren().add(imageView);
                            imageBox.getChildren().add(vBox);
                        });


                    }else {
//                        txtArea.appendText("\n "+message);
                        Platform.runLater(() -> {
                            Text text = new Text("\n " + message);
                            VBox vBox = new VBox(10);
                            vBox.setAlignment(Pos.TOP_LEFT);
                            imageBox.setAlignment(Pos.TOP_LEFT);
                            vBox.getChildren().add(text);
                            imageBox.getChildren().add(vBox);
                        });
                    }
//                msg= br.readLine();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void sendOnAction(ActionEvent actionEvent) throws IOException {
        String msg = sendText.getText();
        String name = lblName.getText();
//        txtArea.appendText("\nME : "+msg);
        Text text = new Text("\nME "+msg);
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.BOTTOM_RIGHT);
        imageBox.setAlignment(Pos.BOTTOM_RIGHT);
        vBox.getChildren().add(text);
        imageBox.getChildren().add(vBox);
        dataOutputStream.writeUTF(name);
        dataOutputStream.writeUTF(msg);
        dataOutputStream.flush();
    }

    public void imageOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
             filePath = selectedFile.getAbsolutePath();
        }

     dataOutputStream.writeUTF(lblName.getText());
        dataOutputStream.writeUTF("image"+ filePath);
        File inputFile = new File(filePath);
        Image image = new Image(inputFile.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        VBox vBox= new VBox(15);
    vBox.setAlignment(Pos.BOTTOM_RIGHT);


       // imageBox.setAlignment(Pos.BOTTOM_RIGHT);
        imageBox.setAlignment(Pos.BOTTOM_LEFT);
        vBox.getChildren().add(imageView);
        imageBox.getChildren().add(vBox);


        dataOutputStream.flush();
    }
}
