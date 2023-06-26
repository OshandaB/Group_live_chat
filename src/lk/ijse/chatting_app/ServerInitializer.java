package lk.ijse.chatting_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.chatting_app.controller.ClientHandlerController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerInitializer  {
   private static ArrayList<ClientHandlerController> clients = new ArrayList<>();
    private static Socket socket;


    public static void main(String[] args)  {


       System.out.println("HO" + clients);

       try {
           ServerSocket serverSocket = new ServerSocket(5003);
           while (true) {
               System.out.println("server Listning");
               socket = serverSocket.accept();
               System.out.println("connect");

               //Thread clientThread = new Thread(String.valueOf(clients.add(new ClientHandlerController(socket, clients))));
               ClientHandlerController handlerController = new ClientHandlerController(socket, clients);
               Thread clientThread = new Thread(handlerController);
               clients.add(handlerController);
               System.out.println(clients);
               clientThread.start();
           }
       } catch (IOException e) {
           e.printStackTrace();
       }





////        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
//
//           String message = dataInputStream.readUTF();
//            System.out.println(message);
////            msg= br.readLine();
//            dataOutputStream.writeUTF("hi");
//            dataOutputStream.flush();




    }
}
