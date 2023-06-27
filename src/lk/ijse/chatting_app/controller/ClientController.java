package lk.ijse.chatting_app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
    public Label labelTime;
    public Label labelTime1;
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
         Timenow();
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
                            imageBox.setAlignment(Pos.TOP_LEFT);
                            File inputFile = new File(imagePath);
                            Image image = new Image(inputFile.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(100);
                            imageView.setFitWidth(100);
                            HBox hBox = new HBox(10);
                            Text text = new Text(strings[0]+": ");
                            text.setStyle("-fx-font-size: 15px; -fx-fill: white");

                            TextFlow textFlow = new TextFlow();
                            textFlow.getChildren().add(text);
                            textFlow.setPadding(new Insets(2,10,2,10));
                            VBox vBox = new VBox();
                            vBox.getChildren().add(imageView);
                            TextFlow textFlow2 = new TextFlow();
                            textFlow2.getChildren().addAll(textFlow,vBox);
                            textFlow2.setStyle("-fx-background-color:  black; -fx-background-radius: 0 10 10 10; -fx-font-style: white;");
                            textFlow2.setPadding(new Insets(2,10,2,10));

                            hBox.getChildren().add(textFlow2);
                            hBox.setAlignment(Pos.BASELINE_LEFT);

                            imageBox.getChildren().add(hBox);
                        });


                    }
                    else if(strings[1].startsWith("emj")){
                        String emoji = strings[1].substring(3);
                        Platform.runLater(() -> {
                            imageBox.setAlignment(Pos.TOP_LEFT);
                            TextFlow textFlow = new TextFlow();
                            TextFlow textFlow1 = new TextFlow();
                            TextFlow textFlow2 = new TextFlow();
                            Text text = new Text(strings[0]+": ");
                            text.setStyle("-fx-font-size: 15px; -fx-fill: white");
                            textFlow.getChildren().add(text);


                            Text emojiText = new Text(emoji);
                            emojiText.setStyle("-fx-font-size: 23px; -fx-fill: #FAD22D; -fx-border-width: 10");
                            textFlow1.getChildren().add(emojiText);
                            textFlow2.getChildren().addAll(text,emojiText);
                            textFlow2.setStyle("-fx-background-color:  black; -fx-background-radius: 0 10 10 10; -fx-font-style: white;");
                            textFlow2.setPadding(new Insets(2,10,2,10));

                            HBox hBox = new HBox();
                            hBox.getChildren().add(textFlow2);
                            hBox.setAlignment(Pos.BASELINE_LEFT);
//                            vBox1.setStyle("-fx-background-color:  black; -fx-background-radius: 0 10 10 10; -fx-font-style: white;");

                            // imageBox.setAlignment(Pos.BOTTOM_RIGHT);


                            imageBox.getChildren().add(hBox);
                        });
                    }
                    else {
//                        txtArea.appendText("\n "+message);
                        Platform.runLater(() -> {
                            imageBox.setAlignment(Pos.TOP_LEFT);
                            Text text = new Text("\n " + message);
                            text.setTextAlignment(TextAlignment.LEFT);
                            text.setStyle("-fx-fill: white");
                            TextFlow textFlow = new TextFlow();
                            textFlow.getChildren().add(text);
//                            VBox vBox = new VBox(5);
                            HBox hBox = new HBox(10);
                            hBox.getChildren().add(textFlow);
                            textFlow.setStyle("-fx-background-color:  black; -fx-background-radius: 0 10 10 10; -fx-fill: white;");

                            textFlow.setPadding(new Insets(2,10,2,10));

                            hBox.setAlignment(Pos.BASELINE_LEFT);

                           // imageBox.setAlignment(Pos.TOP_LEFT);
//                            vBox.getChildren().add(hBox);
////                            vBox.setStyle("-fx-background-color: white");
//                            vBox.setStyle("-fx-background-color:  #202C33 ; -fx-font-size: 12px; -fx-border-color: #202C33; -fx-border-width: 2px; -fx-border-radius: 2");
//
//                            vBox.setMaxHeight(20);
//                            vBox.setMaxWidth(10);
                            imageBox.getChildren().add(hBox);
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

    public void sendOnAction(MouseEvent actionEvent) throws IOException {
      //  imageBox.setAlignment(Pos.BOTTOM_RIGHT);
        String msg = sendText.getText();
        String name = lblName.getText();
//        txtArea.appendText("\nME : "+msg);
        Text text = new Text("\nME : "+msg);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-fill: white;");
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().add(text);
        textFlow.setStyle("-fx-background-color:  #005C4B; -fx-background-radius: 10 10 0 10; -fx-font-style: white;");
        textFlow.setPadding(new Insets(2,10,2,10));

        HBox vBox = new HBox(10);
//        vBox.setAlignment(Pos.BOTTOM_LEFT);
        vBox.setAlignment(Pos.BOTTOM_RIGHT);


        vBox.getChildren().add(textFlow);
//        vBox.setStyle("-fx-background-color:  #005C4B; -fx-background-radius: 0 10 0 0; -fx-font-style: white;");


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
            dataOutputStream.writeUTF(lblName.getText());
        }


        dataOutputStream.writeUTF("image"+ filePath);
        try {


            File inputFile = new File(filePath);
            Image image = new Image(inputFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            TextFlow textFlow = new TextFlow();
            textFlow.getChildren().add(imageView);
            textFlow.setStyle("-fx-background-color:  #005C4B; -fx-background-radius: 10 10 0 10");
            HBox vBox = new HBox(10);
            vBox.setAlignment(Pos.BOTTOM_RIGHT);


            // imageBox.setAlignment(Pos.BOTTOM_RIGHT);
            // imageBox.setAlignment(Pos.BOTTOM_LEFT);
            vBox.getChildren().add(textFlow);
            imageBox.getChildren().add(vBox);

        }catch (Exception e){}
        dataOutputStream.flush();
    }


    public void setEmojis(){
        List<String> emojiUnicodes = Arrays.asList(
                "\uD83D\uDE01",
                "\ud83d\ude08",
                "\uD83D\uDE0D",
                "\uD83D\uDE02",
                "\uD83D\uDE22",
                "\uD83D\uDE20",
                "\uD83D\uDD25",
                "\uD83D\uDE07",
                "\uD83D\uDE21",
                "\uD83D\uDC4C"

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

        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().add(emojiText);
        textFlow.setStyle("-fx-background-color:  #005C4B; -fx-background-radius: 10 10 0 10");
        textFlow.setPadding(new Insets(2,10,2,10));
        HBox vBox= new HBox(10);
        vBox.setAlignment(Pos.BOTTOM_RIGHT);
        dataOutputStream.writeUTF(lblName.getText());
         dataOutputStream.writeUTF("emj"+unicode);

        //imageBox.setAlignment(Pos.BASELINE_RIGHT);
        //imageBox.setAlignment(Pos.BOTTOM_LEFT);
        vBox.getChildren().add(textFlow);
//        vBox.setStyle("-fx-background-color:  #005C4B; -fx-background-radius: 10 10 0 10");

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
    private void Timenow(){
        Thread thread =new Thread(() ->{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM,  dd, yyyy");
            while (true){
                try{
                    Thread.sleep(1000);

                }catch (Exception e){
                    System.out.println(e);
                }
                final String timenow = sdf.format(new Date());
                String timenow1 = sdf1.format(new Date());

                Platform.runLater(() ->{
                    labelTime.setText(timenow);
                     labelTime.setStyle("-fx-font-size: 25px; -fx-text-fill: white");
                     labelTime1.setText(timenow1);
                    labelTime1.setStyle("-fx-font-size: 15px; -fx-text-fill: white");
                });
            }
        });
        thread.start();
    }
}
