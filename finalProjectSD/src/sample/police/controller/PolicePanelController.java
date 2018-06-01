package sample.police.controller;

import data.server.model.Driver;
import data.server.model.Fine;
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
import sample.postoffice.controller.PostOfficePanelController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by plesha on 27-May-18.
 */
@SuppressWarnings("Duplicates")
public class PolicePanelController {

    private static final Integer port = 8081;
    private static final String userType = "police";

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


    @FXML private Button viewAllDriversButton;
    @FXML private Button viewAllFinesButton;

    // --------- driver text fields
    @FXML private TextField tfDriverID;
    @FXML private TextField tfDriverName;
    @FXML private TextField tfDriverAge;
    @FXML private TextField tfDriverAddress;
    @FXML private TextField tfDriverEmail;
    @FXML private TextField tfDriverPhone;


    // ----------- fine text fields
    @FXML private TextField tfFineID;
    @FXML private TextField tfCategory;
    @FXML private TextField tfFinePrice;
    @FXML private TextField tfDriverIdForFine;
    @FXML private TextField tfPoints;

    // -------------- box choices
    @FXML private ChoiceBox crimeChoice;
    @FXML private ChoiceBox choiceIsPaid;
    @FXML private ChoiceBox reportChoice;

    @FXML private DatePicker dateCommitted;
    @FXML private DatePicker deadlineDate;

    @FXML private Label labelNotifications;
    public static int numberOfNotifications;


    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);

    @FXML private Label usernameLabel;
    private String username;

    private String activities;

    @SuppressWarnings("Duplicates")
    public void initialize() throws IOException,ClassNotFoundException{

        username = PoliceLoginController.username;
        usernameLabel.setText( username );

        activities = "";

        checkForNotification();

        ObservableList<String> crimeChoiceList = FXCollections.observableArrayList("accident rutier", "depasire pe linie continua","depasire pe linia dubla continua",
                "depasirea vitezei legale","limbaj ofensator in trafic","parcare neregulamentara");
        ObservableList<String> isPaidList = FXCollections.observableArrayList("Yes","No");
        ObservableList<String> reportFormatList = FXCollections.observableArrayList("PDF format","CSV format");

        crimeChoice.setItems( crimeChoiceList );
        choiceIsPaid.setItems( isPaidList);
        reportChoice.setItems( reportFormatList );




      //  notificationNrTextArea.setText( "0" );
        //label1.setText( " valoarea " );
        //notificationNrTextArea.setStyle("-fx-font-alignment: center");
        //notificationNrTextArea.setStyle("text-area-background: green;");

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
        //button_1.fire();
        //LocalDate.of(195, Month.APRIL,21);

    }

    private void alertGeneratedReport( String reportType ){
        alert.setTitle("Report generated successfully!");
        alert.setHeaderText(null);
        alert.setContentText("A "+reportType + " was generated to the following disk location: E:\\Police Office Report."+reportType);
        alert.showAndWait();
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

    public boolean checkValidInteger( String data ){
        //boolean isValid = true;

        try{
            Integer integerData = Integer.parseInt( data );
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

    public boolean checkValidPoints( String data ){
        try{
            Integer points = Integer.parseInt( data );
            if( points<1 || points >11 ){
                alert.setTitle("Wrong points number !");
                alert.setHeaderText(null);
                alert.setContentText("You need to enter a points number between 1 and 11!");
                alert.showAndWait();

                return false;
            }
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

    private boolean checkValidPhoneDigits( String data ){
       // boolean isValid = true;

        try{
            Double phone = Double.parseDouble( data );
        }
        catch ( NumberFormatException ex ){
            alert.setTitle("Wrong data !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a phone only with DIGITS !");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    /// driverName,driverAge,address,email,phone
    private boolean checkValidDriverData( String driverName, String driverAge, String address, String email, String phone){
        if( checkDriverEmptyTextFields(driverName,driverAge,address,email,phone) ){
            if( checkValidInteger(driverAge) ){
                Integer age = Integer.parseInt( driverAge );

                if( age<18 || age>100 ){
                    alert.setTitle("Wrong Driver Age !");
                    alert.setHeaderText(null);
                    alert.setContentText("You need to enter an age between 18 and 100 years !");
                    alert.showAndWait();
                    return false;
                }

                if( checkValidPhoneDigits(phone) )
                {
                    if( phone.length() != 10 ){
                        alert.setTitle("Invalid Phone Number !");
                        alert.setHeaderText(null);
                        alert.setContentText("You need to enter a phone number with 10 digits !");
                        alert.showAndWait();
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    private boolean checkDriverEmptyTextFields( String name, String age, String address, String email, String phone ){
        if( name.equals("") ||  age.equals("") ||  address.equals("") ||  email.equals("") ||  phone.equals("")  ){
            alert.setTitle("Empty text fields !");
            alert.setHeaderText(null);
            alert.setContentText("You need to complete all Driver Text fields in order to perform this action !");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void successfullyOperation(){
        alert.setTitle("Operation done!");
        alert.setHeaderText(null);
        alert.setContentText(" The clicked operation was Successfully Performed !");
        alert.showAndWait();
    }


    private Driver getDriverByIdFromServer( int option ) throws IOException,ClassNotFoundException {

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
            p.println("postoffice");

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
            p.println("postoffice");

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

    private Fine getFineByIdFromServer() throws IOException,ClassNotFoundException {

        Fine fine = new Fine(-1);
        String fineID = tfFineID.getText();

        if( !fineID.equals("") ) {
            if (checkValidInteger(fineID)) {
                Socket socket = new Socket("localhost", port);
                PrintStream p = new PrintStream(socket.getOutputStream());

                p.println("5" + fineID);
                p.println("postoffice");

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
        p.println("postoffice");

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
        p.println("postoffice");

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        fines = (List<Fine>) inObj.readObject();

        return fines;
    }
    public void finesHistoryListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        String driverId = tfDriverIdForFine.getText();

        if( checkValidInteger(driverId) ) {

            List<Fine> fineHistory = getFineHistoryOfDriver();

            if (fineHistory.size() == 0) {
                alert.setTitle("No fine history !");
                alert.setHeaderText(null);
                alert.setContentText("The driver with the ID " + driverId + " has no fines history.");
                alert.showAndWait();
            } else {
                printHistoryData(fineHistory);
            }
        }
    }



    // ---------------------------------- ------------ 0 first server process for police employee
    private void sendDriverDataToServerForInsert( String driverData ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("0" + "#SEPARA#" + driverData);
        p.println(userType);
    }

    private void sendDriverDataToServerForUpdate(String driverData) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("1" + "#SEPARA#" + driverData);
        p.println(userType);
    }

    private void sendDriverIdToServerForDelete( String driverId ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

       // System.out.println("Driverid = "+driverId);
        p.println("2" + driverId);
        p.println(userType);


        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        String found = (String) inObj.readObject();

        if( found.equals("foundAndDeleted") ){
            viewAllDriversButton.fire();
            successfullyOperation();
        }
        else{
            alert.setTitle("Driver not found!");
            alert.setHeaderText(null);
            alert.setContentText("Driver with ID "+driverId + " not found in database !");
            alert.showAndWait();
        }
    }

    private void sendFineDataToServerForInsert( String fineData ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("3" + "#SEPARA#" + fineData);
        p.println(userType);
    }

    private void sendFineDataToServerForUpdate( String fineData ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("4" + "#SEPARA#" + fineData);
        p.println(userType);
    }

    private void sendFineIdToServerForDelte( String fineId ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("5" + fineId);
        p.println(userType);


        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        String found = (String) inObj.readObject();

        if( found.equals("foundAndDeleted") ){
            viewAllFinesButton.fire();
            successfullyOperation();
        }
        else{
            alert.setTitle("Driver not found!");
            alert.setHeaderText(null);
            alert.setContentText("Driver with ID "+fineId + " not found in database !");
            alert.showAndWait();
        }
    }

    private void sendReportFormatToServer() throws IOException {

        if( reportChoice.getValue() != null ) {

            Socket socket = new Socket("localhost", port);
            PrintStream p = new PrintStream(socket.getOutputStream());

            String reportFormat = reportChoice.getValue().toString();
            if (reportFormat.equalsIgnoreCase("PDF Format")) {
                p.println("6"+"#SEPARA#"+ "pdf");
                p.println(userType);
                alertGeneratedReport("pdf");
            }else if( reportFormat.equalsIgnoreCase("CSV Format")){
                p.println("6"+"#SEPARA#"+ "csv");
                p.println(userType);
                alertGeneratedReport("csv");
            }

        }else{
            alert.setTitle("No format selected !");
            alert.setHeaderText(null);
            alert.setContentText("You need to select a format to generate the report !");
            alert.showAndWait();
        }
    }

    private void sendFineIdToServerToMarkAsPaid( String fineId ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("8" + "#SEPARA#" + fineId);
        p.println(userType);

        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        String found = (String) inObj.readObject();

        if( found.equals("foundAndDeleted") ){
            viewAllFinesButton.fire();
            activities += "Marked a fine as paid,";
            successfullyOperation();
        }
        else{
            alert.setTitle("Fine not found !");
            alert.setHeaderText("You can mark fine as paid only from notification table !");
            alert.setContentText("Fine with id "+fineId + " is not in notification table !");
            alert.showAndWait();
        }
    }

    private void checkForNotification() throws IOException,ClassNotFoundException{

        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("9");
        p.println(userType);

        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        int countNotif = (int) inObj.readObject();

        numberOfNotifications = countNotif;

        if( numberOfNotifications == 0 ) {
            labelNotifications.setVisible(false);
        }else {
            labelNotifications.setVisible(true);
            labelNotifications.setText(Integer.toString(numberOfNotifications));
        }
    }

    private void sendActivitiesToServer() throws IOException{
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream(socket.getOutputStream());

        p.println("#0"+"#SEPARA#"+username+"#SEPARA#"+activities);
        p.println(userType);
    }



    // ---------------------------------------------------  BUTTON listeners -------------------------------------------

    public void viewAllDrivers( ActionEvent event ) throws IOException,ClassNotFoundException {
        checkForNotification();

        ObservableList<Driver> drivers = FXCollections.observableArrayList( PostOfficePanelController.geDriversListFromServer() );
        table_1.setItems( drivers );

        activities += "Viewed all drivers,";
    }


    public void viewDriverByID( ActionEvent event ) throws IOException,ClassNotFoundException {
        checkForNotification();

        Driver driver = getDriverByIdFromServer(1);
        if( driver.getIdDriver() != -1 && driver.getIdDriver() != 0 ){
            ObservableList<Driver> foundDriver = FXCollections.observableArrayList( driver );
            table_1.setItems( foundDriver );

            activities += "Searched a driver by id,";
        }else if( driver.getIdDriver()== 0 ){
            alert.setTitle("Wrong Driver ID !");
            alert.setHeaderText(null);
            alert.setContentText("Driver ID not found in database.");
            alert.showAndWait();
        }



    }

    public void viewDriverByName( ActionEvent event ) throws IOException,ClassNotFoundException {
        checkForNotification();

        Driver driver = getDriverByNameFromServer();
        if( driver.getIdDriver() != -1 && driver.getIdDriver() != 0 ){
            ObservableList<Driver> foundDriver = FXCollections.observableArrayList( driver );
            table_1.setItems( foundDriver );

            activities += "Searched a driver by name,";
        }else if( driver.getIdDriver() == 0 ){
            alert.setTitle("Wrong Driver Name !");
            alert.setHeaderText(null);
            alert.setContentText("Driver with name: "+ tfDriverName.getText()+" not found in database !");
            alert.showAndWait();
        }


    }

    // ---------------------------------------------------  Driver CRUD listeners -------------------------------------------

    public void addDriverListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        checkForNotification();

        String driverName = tfDriverName.getText();
        String driverAge = tfDriverAge.getText();
        String address = tfDriverAddress.getText();
        String email = tfDriverEmail.getText();
        String phone = tfDriverPhone.getText();
        // phone = phone.substring(0,4) + " " + phone.substring(4,7) + " " + phone.substring(7,10);
        //System.out.println(phone);

        if ( checkValidDriverData(driverName,driverAge,address,email,phone) ) {

            phone = phone.substring(0, 4) + " " + phone.substring(4, 7) + " " + phone.substring(7, 10);
            String data = driverName + "#SEPARA#" + driverAge + "#SEPARA#" + address + "#SEPARA#" + email + "#SEPARA#" + phone;

            sendDriverDataToServerForInsert(data);

            viewAllDriversButton.fire();

            successfullyOperation();
            activities += "Added a driver,";

        }


    }

    public void updateDriverListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        checkForNotification();

        String driverId = tfDriverID.getText();
        String driverName = tfDriverName.getText();
        String driverAge = tfDriverAge.getText();
        String address = tfDriverAddress.getText();
        String email = tfDriverEmail.getText();
        String phone = tfDriverPhone.getText();

        if( checkValidInteger(driverId) ) {
            if (checkValidDriverData(driverName, driverAge, address, email, phone)) {

                phone = phone.substring(0, 4) + " " + phone.substring(4, 7) + " " + phone.substring(7, 10);

                String data = driverId + "#SEPARA#" + driverName + "#SEPARA#" + driverAge + "#SEPARA#" + address + "#SEPARA#" + email + "#SEPARA#" + phone;

                sendDriverDataToServerForUpdate(data);

                viewAllDriversButton.fire();

                successfullyOperation();

                activities += "Updated a driver,";
            }
        }
    }

    public void deleteDriverListener(ActionEvent event) throws IOException,ClassNotFoundException {
        checkForNotification();

        String driverId = tfDriverID.getText();

        if( checkValidInteger( driverId ) ){
            Driver driver = getDriverByIdFromServer(1);

            Alert alertDoubleCheck = new Alert(Alert.AlertType.CONFIRMATION);
            alertDoubleCheck.setTitle("Confirmation Dialog");
            alertDoubleCheck.setHeaderText("Are you sure that you want to delete the following Driver ?");
            alertDoubleCheck.setContentText(driver.toString());

            Optional<ButtonType> result = alertDoubleCheck.showAndWait();
            if (result.get() == ButtonType.OK){
                sendDriverIdToServerForDelete( driverId );

                activities += "Deleted a driver,";
            }
        }
    }

    // ---------------------------------------------------  Fine listeners -------------------------------------------

    public void viewAllFines( ActionEvent event ) throws IOException,ClassNotFoundException {
        checkForNotification();

        ObservableList<Fine> fines = FXCollections.observableArrayList( PostOfficePanelController.getFinesListFromServer() );
        table_2.setItems( fines );

        activities += "Viewed all fines,";
    }

    public void viewFineById( ActionEvent event ) throws IOException,ClassNotFoundException {
        checkForNotification();

        Fine fine = getFineByIdFromServer();
        if( fine.getIdFine() != -1 && fine.getIdFine() != 0 ){
            ObservableList<Fine> foundFine = FXCollections.observableArrayList( fine );
            table_2.setItems( foundFine );

            activities += "Searched a fine by id,";
        }else if( fine.getIdFine()== 0 ){
            alert.setTitle("Wrong Fine ID !");
            alert.setHeaderText(null);
            alert.setContentText("Fine ID not found in database.");
            alert.showAndWait();
        }
    }


    public void viewFinesByDriverId( ActionEvent event ) throws IOException,ClassNotFoundException{
        checkForNotification();

        if (checkValidInteger(tfDriverIdForFine.getText())) {
            List<Fine> fines = getFinesByDriverId();

            if (fines.size() == 0) {
                alert.setTitle("No Fine found !");
                alert.setHeaderText(null);
                alert.setContentText("The driver with the ID " + tfDriverID.getText() + " has no registered fine !");
                alert.showAndWait();

                activities += "Viewed fines by driver id,";
            } else {
                ObservableList<Fine> fineList = FXCollections.observableArrayList(fines);
                table_2.setItems(fineList);
            }
        }
    }

    public void viewFinesByCategory( ActionEvent event ) throws IOException,ClassNotFoundException{
        checkForNotification();

        String category = tfCategory.getText();

        if( !category.equals("") ) {

            List<Fine> fines = PostOfficePanelController.getFinesListFromServer();

            List<Fine> finesByCategory = new ArrayList<>();

            for (Fine fine : fines) {
                if (fine.getCrimeType().contains(category)) {
                    finesByCategory.add(fine);
                }
            }

            ObservableList<Fine> fineList = FXCollections.observableArrayList(finesByCategory);
            table_2.setItems(fineList);

            activities += "Viewed fines by category criteria,";
        }else{
            alert.setTitle("No category !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a category criteria for this search !");
            alert.showAndWait();
        }
    }

    // ---------------------------------------------------  Fine CRUD listeners -------------------------------------------
    public void addFineListener( ActionEvent event ) throws IOException,ClassNotFoundException {
       // System.out.println("AM ALES = " + crimeChoice.getValue());
        checkForNotification();

        LocalDate date = dateCommitted.getValue();
        String price = tfFinePrice.getText();
        LocalDate finalDate = deadlineDate.getValue();
        String driverId = tfDriverIdForFine.getText();
        String pointsStr = tfPoints.getText();

        String crimeType = "";
        if( crimeChoice.getValue() != null ){
            crimeType = crimeChoice.getValue().toString();
        }

        String isPaid = "";
        if( choiceIsPaid.getValue() != null ){
            isPaid = choiceIsPaid.getValue().toString();
        }

        /*String fineDate = date + "#SEPARA#" + crimeType + "#SEPARA#" + price + "#SEPARA#"
                + finalDate + "#SEPARA#" + driverId + "#SEPARA#" + isPaid;
        System.out.println(fineDate);*/

        if( date != null &&  finalDate != null && !crimeType.equals("") && !price.equals("") && !driverId.equals("") && !isPaid.equals("") && !pointsStr.equals("") ) {
            if( checkValidInteger(price) ){
                if( checkValidInteger(driverId)){
                    if( checkValidPoints(pointsStr) ) {

                        String fineData = date + "#SEPARA#" + crimeType + "#SEPARA#" + price + "#SEPARA#"
                                + finalDate + "#SEPARA#" + driverId + "#SEPARA#" + isPaid + "#SEPARA#" + pointsStr;

                        sendFineDataToServerForInsert(fineData);

                        viewAllFinesButton.fire();

                        successfullyOperation();

                        activities += "Added a fine,";
                    }
                }
            }
        }else{
            alert.setTitle("Empty text fields !");
            alert.setHeaderText(null);
            alert.setContentText("You need to complete all Fine text fields in order to create a fine!");
            alert.showAndWait();
        }
        //Date sqlDate = Date.valueOf( date );
        //System.out.println("Data selectata ="+  sal);
    }

    public void updateFineListener( ActionEvent event ) throws IOException,ClassNotFoundException{
        checkForNotification();

        String idFine = tfFineID.getText();
        LocalDate date = dateCommitted.getValue();
        String price = tfFinePrice.getText();
        LocalDate finalDate = deadlineDate.getValue();
        String driverId = tfDriverIdForFine.getText();
        String pointsStr = tfPoints.getText();

        String crimeType = "";
        if( crimeChoice.getValue() != null ){
            crimeType = crimeChoice.getValue().toString();
        }

        String isPaid = "";
        if( choiceIsPaid.getValue() != null ){
            isPaid = choiceIsPaid.getValue().toString();
        }

        if( date != null && finalDate != null && !crimeType.equals("") && !price.equals("")  && !driverId.equals("") && !isPaid.equals("") && !pointsStr.equals("") ) {
            if( checkValidInteger( idFine ) ) {
                if (checkValidInteger(price)) {
                    if (checkValidInteger(driverId)) {
                        if( checkValidPoints(pointsStr) ) {

                            String fineData = idFine + "#SEPARA#" + date + "#SEPARA#" + crimeType + "#SEPARA#" + price + "#SEPARA#"
                                    + finalDate + "#SEPARA#" + driverId + "#SEPARA#" + isPaid + "#SEPARA#" + pointsStr;

                            sendFineDataToServerForUpdate(fineData);

                            viewAllFinesButton.fire();

                            successfullyOperation();

                            activities += "Updated a fine,";
                        }
                    }
                }
            }
        }else{
            alert.setTitle("Empty text fields !");
            alert.setHeaderText(null);
            alert.setContentText("You need to complete all Fine text fields in order to update a fine!");
            alert.showAndWait();
        }
    }

    public void deleteFineListener( ActionEvent event ) throws IOException,ClassNotFoundException{
        checkForNotification();

        String fineId = tfFineID.getText();

        if( checkValidInteger( fineId ) ){

            Fine fine = getFineByIdFromServer();

            Alert alertDoubleCheck = new Alert(Alert.AlertType.CONFIRMATION);
            alertDoubleCheck.setTitle("Confirmation Dialog");
            alertDoubleCheck.setHeaderText("Are you sure that you want to delete the following Fine ?");
            alertDoubleCheck.setContentText(fine.toStringNice());

            Optional<ButtonType> result = alertDoubleCheck.showAndWait();
            if (result.get() == ButtonType.OK){
                sendFineIdToServerForDelte( fineId );

                activities += "Deleted a fine,";
            }


        }
    }


    // ---------------------------------------------------  logout listener -------------------------------------------

    public void logoutListener(ActionEvent event) throws IOException{

        sendActivitiesToServer();


        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("policeLogin.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Police Employee Login Panel");
        stage.setScene(new Scene(root));
        stage.setResizable( false );
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    // ----------------------- generate report listener

    public void generateReportListener(ActionEvent event ) throws IOException,ClassNotFoundException {
        checkForNotification();

        sendReportFormatToServer();
    }

    // ------------------------------ create driving license panel
    public void createDrivingLicensePanelListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        checkForNotification();

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("policeDrivingLicensePanel.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Driving license panel");
        stage.setScene(new Scene(root));
        stage.setResizable( false );
        stage.show();
    }

    // -------------------------- view notification listener

    public void viewNotificationListener( ActionEvent event ) throws IOException {

        if( numberOfNotifications == 0 ) {
            notificationAlert.setTitle("Nothing new !");
            notificationAlert.setHeaderText("You do not have new notifications !");
            notificationAlert.setContentText(null);
            notificationAlert.showAndWait();
        }else{
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("policeNotifications.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Notifications Panel");
            stage.setScene(new Scene(root));
            stage.setResizable( false );
            stage.show();
        }
    }

    // ------------- mark fine as paid

    public void markFineAsPaidListener( ActionEvent event)  throws IOException,ClassNotFoundException {
        String fineId = tfFineID.getText() ;
        //System.out.println( tfFineID.toString() );
        if( checkValidInteger(fineId) ){

            Fine fine = getFineByIdFromServer();

            Alert alertDoubleCheck = new Alert(Alert.AlertType.CONFIRMATION);
            alertDoubleCheck.setTitle("Confirmation Dialog");
            alertDoubleCheck.setHeaderText("Are you sure that you want to mark the following fine as paid ?");
            alertDoubleCheck.setContentText(fine.toStringNice());

            Optional<ButtonType> result = alertDoubleCheck.showAndWait();
            if (result.get() == ButtonType.OK){
                sendFineIdToServerToMarkAsPaid( fineId );
            }

        }


    }

}
