package sample.admin.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.police.controller.PoliceLoginController;

import java.io.IOException;

/**
 * Created by plesha on 31-May-18.
 */
public class AdminPanelController2 extends AdminPanelController1 {

    @Override
    public void initialize() {
        super.initialize();
        username = PoliceLoginController.username;
        usernameLabel.setText(username);
    }

    @Override
    public void logoutListener(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("policeLogin.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Post Office Employee Login Panel !");
        stage.setScene(new Scene(root));
        stage.setResizable( false );
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
