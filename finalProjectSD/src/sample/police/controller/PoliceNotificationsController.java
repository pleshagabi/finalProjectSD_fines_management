package sample.police.controller;

import data.server.model.Driver;
import data.server.model.Fine;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by plesha on 30-May-18.
 */
public class PoliceNotificationsController {

    private static final String userType = "police";
    private static final Integer port = 8081;

    @FXML private TextArea textArea;

    public void initialize() throws IOException,ClassNotFoundException {

        String textAreaData = "         Notification(s)\n\n";

        Socket socket = new Socket("localhost", port);
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        printStream.println("7");
        printStream.println(userType);


        HashMap<Driver, Fine> hashMap;
        // get data from server

        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        hashMap = (HashMap<Driver, Fine>) inObj.readObject();

        textAreaData += ("Please mark the following Driver fines as paid:");
        for (Map.Entry<Driver, Fine> entry : hashMap.entrySet()) {

            textAreaData += ("\n\n\nDriver________________________________________________" + "\n")
                    + "Driver Id: " + entry.getKey().getIdDriver() + "\n"
                    + "Driver Name: " + entry.getKey().getName() + "\n"
                    + "Driver Age: " + entry.getKey().getAge() + "\n"
                    + "Driver Address: " + entry.getKey().getAddress() + "\n"
                    + "Driver E-mail: " + entry.getKey().getEmail() + "\n"
                    + "Driver Phone: " + entry.getKey().getPhoneNumber() + "\n"
                    + entry.getValue().toStringNice();

        }
        textArea.setFont( new Font("Serif", 20) );
        textArea.setText(textAreaData);
    }
}
