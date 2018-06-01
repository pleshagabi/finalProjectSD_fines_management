package sample.police.controller;

import data.server.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by plesha on 27-May-18.
 */
public class PoliceLoginController implements Serializable {
    private static final long serialVersionUID = 6128016096756071380L;

    @FXML private TextField tf_username;
    @FXML private TextField tf_password;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public static String username;

    public void pressedButton( ActionEvent event ) throws IOException,ClassNotFoundException{
        System.out.println(tf_username.getText());
        System.out.println(tf_password.getText());

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        //------- send data through socket
        Socket socket = new Socket("localhost", 8081);
        PrintStream p = new PrintStream(socket.getOutputStream());
        p.println("data");
        p.println("login");

        // --------- get data from server
        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        @SuppressWarnings("unchecked")
        List<User> userList = (List<User>) inObj.readObject();

        boolean foundUser = false;

        for( User user : userList ){
            if( !foundUser ) {
                if (user.getUsername().equalsIgnoreCase(tf_username.getText()) &&
                        user.getPassword().equalsIgnoreCase(tf_password.getText()) &&
                        user.getUserType().equalsIgnoreCase("police")) {

                    foundUser = true;
                    username = user.getUsername();

                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("policePanel.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Police Employee Panel");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();

                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } else if (user.getUsername().equalsIgnoreCase(tf_username.getText()) &&
                        user.getPassword().equalsIgnoreCase(tf_password.getText()) &&
                        user.getUserType().equalsIgnoreCase("admin")) {

                    foundUser = true;
                    username = user.getUsername();

                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("adminPanel2.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Administrator Panel");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();

                    ((Node) (event.getSource())).getScene().getWindow().hide();
                }
            }
        }

        if( !foundUser ){
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Try again with a correct username and password.");
            alert.showAndWait();
        }
    }
}
