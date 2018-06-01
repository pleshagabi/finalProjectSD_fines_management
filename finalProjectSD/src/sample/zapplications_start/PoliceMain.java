package sample.zapplications_start;

import data.server.model.nullobject.AbstractDriver;
import data.server.model.nullobject.DriverFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by plesha on 27-May-18.
 */
public class PoliceMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("policeLogin.fxml"));
        primaryStage.setTitle("Police Employee Login Panel");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable( false );
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException,InterruptedException {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        AbstractDriver driver1 = DriverFactory.getDriver( 13 );
        AbstractDriver driver2 = DriverFactory.getDriver( 699 );

        System.out.println(driver1.getIdDriver());
        System.out.println(driver2.getIdDriver());
        launch(args);

        /*Driver driver = new Driver();
        driver.setEmail("plesha.gabi@gmail.com");
        driver.setName("PLesa Gabriel-ioan");

        Fine fine = new Fine();

        Email.sendEmail(driver, fine);
        */
       // Person user = new User("pop","pop","popa","vasile","male",25,new PostOfficeUser() );
      //  user.create();
/*
        List<Notification> notificationList = Notification.read();

        HashMap<Driver,Fine> hashMap = new HashMap<>();

        Driver driver;
        Fine fine;
        for( Notification no : notificationList ){
            driver = Driver.findByID( no.getDriver_idDriver() );
            fine = Fine.findByID( no.getFine_idFine() );
            hashMap.put( driver, fine );
        }

        System.out.println("Acuma incepe printu" );

        for (Map.Entry<Driver, Fine> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey().toString());
            System.out.println(entry.getValue().toString());
            System.out.println();
        }*/


       //
    }
}

