package sample.zapplications_start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;

public class PostOfficeMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("postOfficeEmployeeLogin.fxml"));
        primaryStage.setTitle("Post Office Employee Login Panel");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable( false );
        primaryStage.show();
    }

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        launch(args);
    }

}
