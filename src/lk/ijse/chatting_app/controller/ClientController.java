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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import javax.swing.text.Element;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public AnchorPane logPane;
    public AnchorPane msgPane;
    public VBox imageBox;
    public VBox emojiBox;
    public ScrollPane pane;
    public ImageView imaView;
    public AnchorPane imgPane;
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
        imgPane.setVisible(false);
        logPane.setVisible(false);
        msgPane.setVisible(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        imageBox.setSpacing(10);
        try {
            setEmojis();
            emojis();
            emojiBox.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            try {
                 socket = new Socket("localhost",5003);
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
                            imageView.setFitHeight(100);
                            imageView.setFitWidth(100);
                            HBox hBox = new HBox(10);
                            Text text = new Text(strings[0]+": ");
                            text.setStyle("-fx-font-size: 15px; -fx-fill: white");
                            hBox.getChildren().add(text);
                            hBox.setMaxHeight(20);
                            hBox.setMaxWidth(10);
                            hBox.setAlignment(Pos.BOTTOM_LEFT);
                            //VBox imageBox = new VBox();
                            VBox vBox= new VBox(15);
                            vBox.setAlignment(Pos.BOTTOM_LEFT);


                            // imageBox.setAlignment(Pos.BOTTOM_RIGHT);
                            imageBox.setAlignment(Pos.TOP_LEFT);
                            vBox.getChildren().add(imageView);
                            imageBox.getChildren().addAll(hBox,vBox);
                        });


                    }
                    else if(strings[1].startsWith("emj")){
                        String emoji = strings[1].substring(3);
                        Platform.runLater(() -> {
                            HBox hBox = new HBox(10);
                            Text text = new Text(strings[0]+": ");
                            text.setStyle("-fx-font-size: 15px; -fx-fill: white");
                            hBox.getChildren().add(text);
                            hBox.setMaxHeight(20);
                            hBox.setMaxWidth(10);

                            Text emojiText = new Text(emoji);
                            emojiText.setStyle("-fx-font-size: 23px; -fx-fill: #FAD22D; -fx-border-width: 10");
                            HBox vBox = new HBox(10);
                            vBox.setAlignment(Pos.BOTTOM_LEFT);

                            vBox.getChildren().add(emojiText);
                            VBox vBox1= new VBox(10);
                            vBox1.getChildren().addAll(hBox,vBox);
                            vBox1.setStyle("-fx-background-color:  black; -fx-background-radius: 0 10 10 10; -fx-font-style: white;");
                            vBox1.setMaxWidth(10);
                            // imageBox.setAlignment(Pos.BOTTOM_RIGHT);
                            imageBox.setAlignment(Pos.TOP_LEFT);

                            imageBox.getChildren().add(vBox1);
                        });
                    }
                    else {
//                        txtArea.appendText("\n "+message);
                        Platform.runLater(() -> {
                            Text text = new Text("\n " + message);
                            text.setTextAlignment(TextAlignment.LEFT);
                            text.setStyle("-fx-text-fill: white");
                            VBox vBox = new VBox(5);
                            HBox hBox = new HBox();
                            hBox.getChildren().add(text);
                            hBox.setStyle("-fx-background-color:  #005C4B ; -fx-font-size: 12px; -fx-fill: white; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 0 0 0 10");

                            hBox.setPrefWidth(5);
                            hBox.setPrefHeight(20);
                            vBox.setPrefHeight(38);
                            vBox.setPrefWidth(10);
                            vBox.setMaxHeight(100);
                            hBox.setAlignment(Pos.BOTTOM_LEFT);
                            vBox.setAlignment(Pos.BOTTOM_LEFT);
                            imageBox.setAlignment(Pos.TOP_LEFT);
                            vBox.getChildren().add(hBox);
//                            vBox.setStyle("-fx-background-color: white");
                            vBox.setStyle("-fx-background-color:  #202C33 ; -fx-font-size: 12px; -fx-border-color: #202C33; -fx-border-width: 2px; -fx-border-radius: 2");

                            vBox.setMaxHeight(20);
                            vBox.setMaxWidth(10);
                            imageBox.getChildren().add(vBox);
//                            imageBox.setStyle("-fx-background-color: grey ; -fx-font-size: 12px; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 10");

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
        Text text = new Text("\nME : "+msg);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-fill: white;");
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.BOTTOM_RIGHT);

       // imageBox.setAlignment(Pos.BOTTOM_RIGHT);
        vBox.getChildren().add(text);
        vBox.setStyle("-fx-background-color:  #005C4B; -fx-background-radius: 0 10 0 0; -fx-font-style: white;");

        vBox.setMaxHeight(20);
        vBox.setMaxWidth(25);
        imageBox.getChildren().add(vBox);

        dataOutputStream.writeUTF(name);
        dataOutputStream.writeUTF(msg);
        dataOutputStream.flush();
    }

//    public void imageOnAction(ActionEvent actionEvent) throws IOException {
//        FileChooser fileChooser = new FileChooser();
//        File selectedFile = fileChooser.showOpenDialog(null);
//        if (selectedFile != null) {
//             filePath = selectedFile.getAbsolutePath();
//        }
//
//     dataOutputStream.writeUTF(lblName.getText());
//        dataOutputStream.writeUTF("image"+ filePath);
//        File inputFile = new File(filePath);
//        Image image = new Image(inputFile.toURI().toString());
//        ImageView imageView = new ImageView(image);
//        imageView.setFitHeight(50);
//        imageView.setFitWidth(50);
//        VBox vBox= new VBox(15);
//    vBox.setAlignment(Pos.BOTTOM_RIGHT);
//
//
//       // imageBox.setAlignment(Pos.BOTTOM_RIGHT);
//        imageBox.setAlignment(Pos.BOTTOM_LEFT);
//        vBox.getChildren().add(imageView);
//        imageBox.getChildren().add(vBox);
//
//
//        dataOutputStream.flush();
//    }
    public  void emojis() throws IOException {
        String emoji = "\uD83D\uDE01"; // Unicode escape sequence for a smiling face emoji

        VBox vBox = new VBox(10);
        Text text = new Text(emoji);
        text.setStyle("-fx-font-size: 24px; -fx-fill: #FAD22D; -fx-border-width: 10");
        vBox.getChildren().add(text);
        vBox.setAlignment(Pos.BOTTOM_RIGHT);
        imageBox.setAlignment(Pos.BOTTOM_RIGHT);
        imageBox.getChildren().add(vBox);



    }

    public void imageOnAction(MouseEvent mouseEvent) throws IOException {
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
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        VBox vBox= new VBox(50);
    vBox.setAlignment(Pos.BOTTOM_RIGHT);

        vBox.setPrefHeight(38);
       // imageBox.setAlignment(Pos.BOTTOM_RIGHT);
       // imageBox.setAlignment(Pos.BOTTOM_LEFT);
        vBox.getChildren().add(imageView);
        imageBox.getChildren().add(vBox);


        dataOutputStream.flush();
    }


    public void setEmojis(){
        List<String> emojiUnicodes = Arrays.asList(
                "\uD83D\uDE01", // Smiling face emoji
                "\ud83d\ude08", // Thumbs up emoji
                "\uD83D\uDE0D",
                "\uD83D\uDE02",
                "\uD83D\uDE22",
                "\uD83D\uDE20",
                "\uD83D\uDD25"// Ghost emoji
                // Add more emoji unicodes here
        );

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        int columns = 4;
        int rows = (int) Math.ceil(emojiUnicodes.size() / (double) columns);
        System.out.println(rows);
        for (int row = 0; row < rows; row++) {
            HBox hbox = new HBox();
            hbox.setSpacing(10);

            for (int col = 0; col < columns; col++) {
                int index = row * columns + col;
                if (index < emojiUnicodes.size()) {
                    String unicode = emojiUnicodes.get(index);
                    Text emojiText = new Text(unicode);
                    hbox.getChildren().add(emojiText);
                    hbox.setStyle("-fx-font-size: 24px; -fx-fill: black; -fx-border-width: 10");
                    emojiText.setOnMouseClicked(event -> {
                        try {
                            handleEmojiSelection(unicode);
                            pane.setVisible(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            vBox.getChildren().add(hbox);

        }
//        for (String unicode : emojiUnicodes) {
//            Text emojiText = new Text(unicode);
//            hBox.getChildren().add(emojiText);
//            hBox.setAlignment(Pos.TOP_RIGHT);
//        }

//        emojiBox.getChildren().clear(); // Clear any existing content
        emojiBox.getChildren().add(vBox);
        emojiBox.setVisible(true);

    }

    private void handleEmojiSelection(String unicode) throws IOException {
        Text emojiText = new Text(unicode);
        emojiText.setStyle("-fx-font-size: 25px; -fx-fill: #FAD22D; -fx-border-width: 10");
        VBox vBox= new VBox(50);
        vBox.setAlignment(Pos.BOTTOM_RIGHT);
        dataOutputStream.writeUTF(lblName.getText());
         dataOutputStream.writeUTF("emj"+unicode);

        // imageBox.setAlignment(Pos.BOTTOM_RIGHT);
        //imageBox.setAlignment(Pos.BOTTOM_LEFT);
        vBox.getChildren().add(emojiText);
        vBox.setStyle("-fx-background-color:  #005C4B; -fx-background-radius: 10 10 0 10");
   vBox.setMaxWidth(20);

        imageBox.getChildren().add(vBox);


       dataOutputStream.flush();
    }

    public void emojiSend(MouseEvent mouseEvent) {
        emojiBox.setVisible(true);
        pane.setVisible(true);
    }

    public void emojiExit(MouseEvent mouseEvent) {
        emojiBox.setVisible(false);
        pane.setVisible(false);
    }
}
