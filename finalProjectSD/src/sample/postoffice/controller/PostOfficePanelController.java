package sample.postoffice.controller;

import data.server.model.Driver;
import data.server.model.Fine;
import data.server.model.nullobject.AbstractDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Date;
import java.util.*;

/**
 * Created by plesha on 25-May-18.
 */
@SuppressWarnings("Duplicates")
public class PostOfficePanelController {

    private static final Integer port = 8081;
    private static final String userType = "postoffice";

    @FXML private TableView<Driver> table_1;

    @FXML private TableColumn<Driver, Integer> idColumn;
    @FXML private TableColumn<Driver, String> nameColumn;
    @FXML private TableColumn<Driver, String> ageColumn;
    @FXML private TableColumn<Driver, String> addressColumn;
    @FXML private TableColumn<Driver, String> emailColumn;
    @FXML private TableColumn<Driver, String> phoneColumn;


    @FXML private TableView<Fine> table_2;

    @FXML private TableColumn<Fine, Integer> idFineColumn;
    @FXML private TableColumn<Fine, Date> dateCommitedColumn;
    @FXML private TableColumn<Fine, String> crimeColumn;
    @FXML private TableColumn<Fine, Integer> finePriceColumn;
    @FXML private TableColumn<Fine, Date> deadlineColumn;
    @FXML private TableColumn<Fine, Integer> driverIdFineColumn;
    @FXML private TableColumn<Fine, Integer> pointsColumn;


    @FXML public Button viewAllDriversButton;
    @FXML public Button viewAllFinesButton;

    @FXML private TextField tfDriverID;
    @FXML private TextField tfDriverName;

    @FXML private TextField tfDriverIdForFine;
    @FXML private TextField tfFineID;
    @FXML private TextField tfCategory;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);

    @FXML private Label usernameLabel;
    private String userName;

    private List<String> activities = new ArrayList<>();

    public void initialize(){
        userName = PostOfficeLoginController.username;
        usernameLabel.setText(userName);

        activities.clear();

        idColumn.setCellValueFactory(new PropertyValueFactory<Driver,Integer>("idDriver"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("Name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("Age"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("phoneNumber"));

        idColumn.setStyle( "-fx-alignment: CENTER;");
        ageColumn.setStyle( "-fx-alignment: CENTER;");
        phoneColumn.setStyle( "-fx-alignment: CENTER;");
        addressColumn.setStyle( "-fx-alignment: CENTER;");
        nameColumn.setStyle( "-fx-alignment: CENTER;");



        idFineColumn.setCellValueFactory(new PropertyValueFactory<Fine,Integer>("idFine"));
        dateCommitedColumn.setCellValueFactory(new PropertyValueFactory<Fine,Date>("dateFineCommited"));
        crimeColumn.setCellValueFactory(new PropertyValueFactory<Fine,String>("crimeType"));
        finePriceColumn.setCellValueFactory(new PropertyValueFactory<Fine,Integer>("price"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<Fine,Date>("deadlineDate"));
        driverIdFineColumn.setCellValueFactory(new PropertyValueFactory<Fine,Integer>("Driver_idDriver"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<Fine,Integer>("points"));

        idFineColumn.setStyle( "-fx-alignment: CENTER;");
        dateCommitedColumn.setStyle( "-fx-alignment: CENTER;");
        finePriceColumn.setStyle( "-fx-alignment: CENTER;");
        deadlineColumn.setStyle( "-fx-alignment: CENTER;");
        driverIdFineColumn.setStyle( "-fx-alignment: CENTER;");
        pointsColumn.setStyle( "-fx-alignment: CENTER;");

        //viewAllDriversButton.fire();
        //LocalDate.of(195, Month.APRIL,21);
    }


    private boolean checkValidInteger( String data ){

        try{
            Integer id = Integer.parseInt( data );
        }
        catch ( NumberFormatException ex ){
            alert.setTitle("Wrong data !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a Number.");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private boolean registerFinePaymentConfirmation( Fine fine, Driver driver ){

        alertConfirmation.setTitle("Confirmation Dialog !");
        alertConfirmation.setHeaderText("Are you sure that you want to Register the following fine payment ?");
        alertConfirmation.setContentText(
                "____________________________________________________________\n"+
                        "ID Driver: " + driver.getIdDriver()  + "\n" +
                             "Name: " + driver.getName()  + "\n" +
                             "Age: " + driver.getAge()  + "\n" +
                             "Address: " + driver.getAge()  + "\n" +
                             "E-Mail: " + driver.getEmail()  + "\n" +
                             "Phone: " + driver.getPhoneNumber()  + "\n" +
                             "____________________________________________________________\n\n"+
                        "____________________________________________________________\n"+
                        "Fine ID: "+ fine.getIdFine() + "\n" +
                             "Date Fine Commited: " + fine.getDateFineCommited() + "\n" +
                             "Crime Type: " + fine.getCrimeType() + "\n" +
                             "Price: " + fine.getPrice() +" lei" + "\n" +
                             "Payment Deadline Date: " + fine.getDeadlineDate() + "\n" +
                "____________________________________________________________"+ "\n\n"
        );


        Optional<ButtonType> result = alertConfirmation.showAndWait();

        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("Duplicates")
    public static List<Driver> geDriversListFromServer() throws IOException,ClassNotFoundException{

        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("0");
        p.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        @SuppressWarnings("unchecked")
        List<Driver> userList = (List<Driver>) inObj.readObject();

        return userList;
    }


    public Driver getDriverByIdFromServer( int option ) throws IOException,ClassNotFoundException {

        Driver driver = new Driver(-1);
        String driverID;

        if( option == 0 )
             driverID = tfDriverIdForFine.getText();
        else
            driverID = tfDriverID.getText();

        if (checkValidInteger(driverID)) {

            Socket socket = new Socket("localhost", port);
            PrintStream p = new PrintStream(socket.getOutputStream());

            p.println("1" + driverID);
            p.println(userType);

            // --------- get data from socket
            ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
            driver = (Driver) inObj.readObject();

            if (driver.getIdDriver() == -1) // not found
                return new Driver(0);
        }

        return driver;
    }

    private Driver getDriverByNameFromServer() throws IOException,ClassNotFoundException {

        String driverName = tfDriverName.getText();
        Driver driver = new Driver(-1);

        if( !driverName.equals("") ) {
            Socket socket = new Socket("localhost", port);
            PrintStream p = new PrintStream(socket.getOutputStream());

            p.println("2" + driverName);
            p.println(userType);

            // --------- get data from socket
            ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
            driver = (Driver) inObj.readObject();
        }else{
            alert.setTitle("No data entered !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a Driver Name in order to perform this action !");
            alert.showAndWait();
        }
        return driver;
    }

    private void sendDriverIdAndFineIdForRegister() throws IOException,ClassNotFoundException{

        String driverID = tfDriverID.getText();
        String fineID = tfFineID.getText();

        if( !driverID.equals("") && !fineID.equals("") ) {
            if (checkValidInteger(driverID) && checkValidInteger(fineID)) {

                Socket socket = new Socket("localhost", port);
                PrintStream p = new PrintStream(socket.getOutputStream());

                p.println("3" + "#SEPARA#" + "#DONT_REGISTER#" + "#SEPARA#" + driverID + "#SEPARA#" + fineID);
                p.println(userType);

                // --------- get data from socket
                ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
                @SuppressWarnings("unchecked")
                HashMap<AbstractDriver, Fine> hashMap = (HashMap<AbstractDriver, Fine>) inObj.readObject();

                AbstractDriver foundDriver = new Driver();
                Fine foundFine = new Fine();

                for (Map.Entry<AbstractDriver, Fine> entry : hashMap.entrySet()) {
                    foundDriver = entry.getKey();
                    foundFine = entry.getValue();
                }

                if (foundDriver.getIdDriver() == foundFine.getDriver_idDriver()) { // daca idu soferului din key si value coincid
                                                                                // pun soferul si amenda in lista de notificari
                    if (registerFinePaymentConfirmation(foundFine, (Driver)foundDriver)) {
                        socket = new Socket("localhost", port);
                        p = new PrintStream(socket.getOutputStream());

                        p.println("3" + "#SEPARA#" + "#REGISTER_PAYMENT#" + "#SEPARA#" + driverID + "#SEPARA#" + fineID );
                        p.println(userType);

                        activities.add("Registered a driver who paid a fine,");

                        // ------- get nr of notification from socket
                        /*ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
                        @SuppressWarnings("unchecked")
                        HashMap<Driver, Fine> hashMap = (HashMap<Driver, Fine>) inObj.readObject();
                        */

                       // viewAllFinesButton.fire();

                    }
                } else {
                    alert.setTitle("Wrong Driver-Fine Pair !");
                    alert.setHeaderText("Wrong Driver ID and Fine ID pair !");
                    alert.setContentText("Driver: " + ((Driver)foundDriver).getName() + " with ID: " + foundDriver.getIdDriver() + "\n" +
                            "Doesn't committed the Crime: \"" + foundFine.getCrimeType() + "\" For fine With ID: " + foundFine.getIdFine());
                    alert.showAndWait();
                }
            }
        }else{
            alert.setTitle("No data entered !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a Driver ID and a Fine ID in order to perform this action !");
            alert.showAndWait();
        }
    }

    public static List<Fine> getFinesListFromServer() throws IOException,ClassNotFoundException{

        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("4");
        p.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        @SuppressWarnings("unchecked")
        List<Fine> fineList = (List<Fine>) inObj.readObject();

        return fineList;
    }

    private Fine getFineByIdFromServer() throws IOException,ClassNotFoundException {

        Fine fine = new Fine(-1);
        String fineID = tfFineID.getText();

        if( !fineID.equals("") ) {
            if (checkValidInteger(fineID)) {
                Socket socket = new Socket("localhost", port);
                PrintStream p = new PrintStream(socket.getOutputStream());

                p.println("5" + fineID);
                p.println(userType);

                // --------- get data from socket
                ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
                fine = (Fine) inObj.readObject();

                if (fine.getIdFine() == -1) // not found
                    return new Fine(0);
            }
        }else{
            alert.setTitle("No data entered !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a Fine ID in order to perform this action !");
            alert.showAndWait();
        }
        return fine;
    }

    private List<Fine> getFinesByDriverId() throws IOException,ClassNotFoundException {
        List<Fine> fines = new ArrayList<>();
        String driverID = tfDriverIdForFine.getText();


        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("6" + driverID);
        p.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        fines = (List<Fine>) inObj.readObject();


        return fines;
    }

    private  List<Fine> getFineHistoryOfDriver() throws IOException,ClassNotFoundException {

        List<Fine> fines;
        String driverId = tfDriverIdForFine.getText();


        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("7" + driverId);
        p.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        fines = (List<Fine>) inObj.readObject();

        return fines;
    }

    private void sendActivityListToServer() throws IOException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("8" + userName);
        p.println(userType);

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject(activities);
    }



    private void printHistoryData( List<Fine> fineHistory ) throws IOException,ClassNotFoundException {
        String printData = "";
        Driver driver = getDriverByIdFromServer(0);



        for( Fine fine : fineHistory ){

            printData +=
                            "ID Fine: " + fine.getIdFine() + "\n" +
                            "Date Committed: " + fine.getDateFineCommited() + "\n" +
                            "Crime type: \"" + fine.getCrimeType() + "\"\n" +
                            "Fine Price: " + fine.getPrice() + " lei\n" +
                            "Fine Deadline Date: " + fine.getDeadlineDate() + "\n\n";
        }

        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Found Fine history !");
        alert.setHeaderText(" Driver: " + driver.getName() +" with ID "+ driver.getIdDriver() +"\n");
        alert.setContentText(printData);
        alert.showAndWait();
    }



    // ---------------------------------------------------  BUTTON listeners -------------------------------------------

    public void viewAllDrivers( ActionEvent event ) throws IOException,ClassNotFoundException {
       ObservableList<Driver> drivers = FXCollections.observableArrayList( geDriversListFromServer() );
       table_1.setItems( drivers );
       activities.add("Viewed all drivers,");
    }

    public void viewAllFines( ActionEvent event ) throws IOException,ClassNotFoundException {
        ObservableList<Fine> fines = FXCollections.observableArrayList( getFinesListFromServer() );
        table_2.setItems( fines );
        activities.add("Viewed all fines,");
    }

    @SuppressWarnings("Duplicates")
    public void viewDriverByID( ActionEvent event ) throws IOException,ClassNotFoundException {

        Driver driver = getDriverByIdFromServer(1);
        if( driver.getIdDriver() != -1 && driver.getIdDriver() != 0 ){
            ObservableList<Driver> foundDriver = FXCollections.observableArrayList( driver );
            table_1.setItems( foundDriver );
            activities.add("Searched a driver by id,");
        }else if( driver.getIdDriver()== 0 ){
            alert.setTitle("Wrong Driver ID !");
            alert.setHeaderText(null);
            alert.setContentText("Driver ID not found in database.");
            alert.showAndWait();
        }

    }

    public void viewDriverByName( ActionEvent event ) throws IOException,ClassNotFoundException {
        Driver driver = getDriverByNameFromServer();
        if( driver.getIdDriver() != -1 && driver.getIdDriver() != 0 ){
            ObservableList<Driver> foundDriver = FXCollections.observableArrayList( driver );
            table_1.setItems( foundDriver );
            activities.add("Searched a driver by name,");
        }/*else if( driver.getIdDriver() == 0 ){
            alert.setTitle("Wrong Driver Name !");
            alert.setHeaderText(null);
            alert.setContentText("Driver with name: "+ tfDriverName.getText()+" not found in database !");
            alert.showAndWait();
        }*/

    }

    public void registerDriverPayment( ActionEvent event ) throws IOException,ClassNotFoundException {
        sendDriverIdAndFineIdForRegister();
    }

    public void viewFineById( ActionEvent event ) throws IOException,ClassNotFoundException {
        Fine fine = getFineByIdFromServer();
        if( fine.getIdFine() != -1 && fine.getIdFine() != 0 ){
            ObservableList<Fine> foundFine = FXCollections.observableArrayList( fine );
            table_2.setItems( foundFine );
            activities.add("Searched a fine by id,");
        }else if( fine.getIdFine()== 0 ){
            alert.setTitle("Wrong Fine ID !");
            alert.setHeaderText(null);
            alert.setContentText("Fine ID not found in database.");
            alert.showAndWait();
        }

    }

    public void viewFinesByDriverId( ActionEvent event ) throws IOException,ClassNotFoundException{

        if (checkValidInteger(tfDriverIdForFine.getText())) {

            List<Fine> fines = getFinesByDriverId();

            if (fines.size() == 0) {
                alert.setTitle("No Fine found !");
                alert.setHeaderText(null);
                alert.setContentText("The driver with the ID " + tfDriverIdForFine.getText() + " has no registered fine !");
                alert.showAndWait();
            } else {
                ObservableList<Fine> fineList = FXCollections.observableArrayList(fines);
                table_2.setItems(fineList);
                activities.add("Searched fines of a driver by id,");
            }
        }
    }

    public void viewFinesByCategory( ActionEvent event ) throws IOException,ClassNotFoundException{
        String category = tfCategory.getText();

        if( !category.equals("") ) {

            List<Fine> fines = getFinesListFromServer();

            List<Fine> finesByCategory = new ArrayList<>();

            for (Fine fine : fines) {
                if (fine.getCrimeType().contains(category)) {
                    finesByCategory.add(fine);
                }
            }

            ObservableList<Fine> fineList = FXCollections.observableArrayList(finesByCategory);
            table_2.setItems(fineList);
            activities.add("Viewed fines by category criteria,");
        }else{
            alert.setTitle("No category !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a category criteria for this search !");
            alert.showAndWait();
        }
    }


    public void viewHistoryByDriverId( ActionEvent event ) throws IOException,ClassNotFoundException{

        if( checkValidInteger(tfDriverIdForFine.getText()) ) {

            List<Fine> fineHistory = getFineHistoryOfDriver();

            if (fineHistory.size() == 0) {
                alert.setTitle("No fine history !");
                alert.setHeaderText(null);
                alert.setContentText("The driver with the ID " + tfDriverIdForFine.getText() + " has no fines history.");
                alert.showAndWait();
            } else {
                printHistoryData(fineHistory);
            }
        }

    }


    public void logoutListener(ActionEvent event) throws IOException{


        sendActivityListToServer();

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("postOfficeEmployeeLogin.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Post Office Employee Login Panel");
        stage.setScene(new Scene(root));
        stage.setResizable( false );
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }




}
