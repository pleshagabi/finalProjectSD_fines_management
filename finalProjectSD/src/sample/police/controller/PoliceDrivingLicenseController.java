package sample.police.controller;

import data.server.model.DrivingLicense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by plesha on 29-May-18.
 */
public class PoliceDrivingLicenseController {
    private static final Integer port = 8081;
    private static final String userType = "police_driving_license";

    @FXML private TableView<DrivingLicense> table_1;

    @FXML private TableColumn<DrivingLicense, Integer> licenseIdColumn;
    @FXML private TableColumn<DrivingLicense, String> nameColumn;
    @FXML private TableColumn<DrivingLicense, Date> dateOfBirthColumn;
    @FXML private TableColumn<DrivingLicense, String> placeOfBirthColumn;
    @FXML private TableColumn<DrivingLicense, String> issuedByColumn;
    @FXML private TableColumn<DrivingLicense, Date> issuedDateColumn;
    @FXML private TableColumn<DrivingLicense, Date> validUntilColumn;
    @FXML private TableColumn<DrivingLicense, String> licenseCodeColumn;
    @FXML private TableColumn<DrivingLicense, String> categoriesColumn;
    @FXML private TableColumn<DrivingLicense, Integer> pointsColumn;
    @FXML private TableColumn<DrivingLicense, Integer> driverIdColumn;
    @FXML private TableColumn<DrivingLicense, String> suspendedColumn;

    // -------- text fields
    @FXML private TextField tfLicenseId;
    @FXML private TextField tfDriverName;
    @FXML private TextField tfBirthPlace;
    @FXML private TextField tfIssuedBy;
    @FXML private TextField tfLicenseCode;
    @FXML private TextField tfDrivingCategories;
    @FXML private TextField tfLicensePoints;
    @FXML private TextField tfDriverId;

    //-------- date pickers
    @FXML private DatePicker birthDatePicker;
    @FXML private DatePicker issuedDatePicker;
    @FXML private DatePicker validUntilDatePicker;

    @FXML private ChoiceBox suspendedChoice;

    @FXML private Button viewAllLicensesButton;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);

    public void initialize() {
        ObservableList<String> suspendedOption = FXCollections.observableArrayList("Yes","No");
        suspendedChoice.setItems( suspendedOption );

        licenseIdColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, Integer>("idDrivingLicense"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, String>("name"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, Date>("dateOfBirth"));
        placeOfBirthColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, String>("placeOfBirth"));
        issuedByColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, String>("issuedBy"));
        issuedDateColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, Date>("issuedDate"));

        validUntilColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, Date>("validUntil"));
        licenseCodeColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, String>("licenseCode"));
        categoriesColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, String>("categories"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, Integer>("licensePoints"));
        driverIdColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, Integer>("driver_idDriver"));
        suspendedColumn.setCellValueFactory(new PropertyValueFactory<DrivingLicense, String>("isSuspended"));


        licenseIdColumn.setStyle("-fx-alignment: CENTER;");
        driverIdColumn.setStyle("-fx-alignment: CENTER;");
        dateOfBirthColumn.setStyle("-fx-alignment: CENTER;");
        placeOfBirthColumn.setStyle("-fx-alignment: CENTER;");
        validUntilColumn.setStyle("-fx-alignment: CENTER;");
        pointsColumn.setStyle("-fx-alignment: CENTER;");
        nameColumn.setStyle("-fx-alignment: CENTER;");
        issuedByColumn.setStyle("-fx-alignment: CENTER;");
        licenseCodeColumn.setStyle("-fx-alignment: CENTER;");
        categoriesColumn.setStyle("-fx-alignment: CENTER;");
        suspendedColumn.setStyle("-fx-alignment: CENTER;");

    }

    private boolean checkValidInteger( String data ){

        try{
            Integer intData = Integer.parseInt( data );
        }
        catch ( NumberFormatException ex ){
            alert.setTitle("Wrong data !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a Number !");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private boolean checkValidPoints( String data ){
        try{
            Integer points = Integer.parseInt( data );
            if( points<1 || points >10 ){
                alert.setTitle("Wrong points number!");
                alert.setHeaderText(null);
                alert.setContentText("You need to enter a points number between 1 and 11!");
                alert.showAndWait();

                return false;
            }
        }
        catch ( NumberFormatException ex ){
            alert.setTitle("Wrong data!");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a Number !");
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

    //------------------------------------------------------- GETTING DATA FORM SERVER

    private List<DrivingLicense> getDrivingLicensesFromServer() throws IOException,ClassNotFoundException{

        Socket socket = new Socket("localhost", port);
        PrintStream printStream = new PrintStream( socket.getOutputStream() );

        printStream.println("0");
        printStream.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        @SuppressWarnings("unchecked")
        List<DrivingLicense> drivingLicenseList = (List<DrivingLicense>) inObj.readObject();

        return drivingLicenseList;
    }

    private DrivingLicense getLicenseByIdFromServer() throws IOException,ClassNotFoundException {

        DrivingLicense drivingLicense = new DrivingLicense(-1);
        String licenseId = tfLicenseId.getText();

        if (checkValidInteger(licenseId)) {

            Socket socket = new Socket("localhost", port);
            PrintStream printStream = new PrintStream(socket.getOutputStream());

            printStream.println("1" + "#SEPARA#" + licenseId);
            printStream.println(userType);

            // --------- get data from socket
            ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
            drivingLicense = (DrivingLicense) inObj.readObject();

            if (drivingLicense.getIdDrivingLicense() == -1) // not found
                return new DrivingLicense(0);
        }
        return drivingLicense;
    }

    private DrivingLicense getLicenseByDriverNameFromServer() throws IOException,ClassNotFoundException {

        DrivingLicense drivingLicense = new DrivingLicense(-1);
        String driverName = tfDriverName.getText();

        if( !driverName.equals("") ) {
            Socket socket = new Socket("localhost", port);
            PrintStream printStream = new PrintStream(socket.getOutputStream());

            printStream.println("2" + "#SEPARA#" + driverName);
            printStream.println(userType);

            // --------- get data from socket
            ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
            drivingLicense = (DrivingLicense) inObj.readObject();

        }else{
            alert.setTitle("No data entered !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a Driver Name in order to perform this action !");
            alert.showAndWait();
        }

        return drivingLicense;
    }

    private void sendLicenseDataToServerForInsert( String licenseData ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        printStream.println("3" + "#SEPARA#" + licenseData);
        printStream.println(userType);
    }

    private void sendLicenseDataToServerForUpdate( String licenseData ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        printStream.println("4" + "#SEPARA#" + licenseData);
        printStream.println(userType);
    }

    private void sendLicenseIdToServerForDelete( String licenseId ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        printStream.println("5" + "#SEPARA#"+ licenseId);
        printStream.println(userType);


        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        String found = (String) inObj.readObject();

        if( found.equals("foundAndDeleted") ){
            viewAllLicensesButton.fire();
            successfullyOperation();
        }
        else{
            alert.setTitle("Driving License not found!");
            alert.setHeaderText(null);
            alert.setContentText("Driving License with ID "+licenseId + " not found in database !");
            alert.showAndWait();
        }

    }
    //--------------------------------------------------------- button listeners

    public void viewAllLicensesListener(ActionEvent event) throws IOException,ClassNotFoundException {

        ObservableList<DrivingLicense> licenses = FXCollections.observableArrayList( getDrivingLicensesFromServer() );
        table_1.setItems( licenses );
    }

    public void searchLicenseByIdListener( ActionEvent event ) throws IOException,ClassNotFoundException {

        DrivingLicense drivingLicense = getLicenseByIdFromServer();

        if( drivingLicense.getIdDrivingLicense() != -1 && drivingLicense.getIdDrivingLicense() != 0 ){
            ObservableList<DrivingLicense> foundLicense = FXCollections.observableArrayList( drivingLicense );
            table_1.setItems( foundLicense );

        }else if( drivingLicense.getIdDrivingLicense() == 0 ){

            alert.setTitle("Wrong License ID !");
            alert.setHeaderText(null);
            alert.setContentText("License ID not found in database.");
            alert.showAndWait();
        }
    }

    public void searchByDriverNameListener( ActionEvent event ) throws IOException,ClassNotFoundException  {
        DrivingLicense drivingLicense = getLicenseByDriverNameFromServer();

        if( drivingLicense.getIdDrivingLicense() != -1 && drivingLicense.getIdDrivingLicense() != 0 ){
            ObservableList<DrivingLicense> foundDriver = FXCollections.observableArrayList( drivingLicense );
            table_1.setItems( foundDriver );
        }else if( drivingLicense.getIdDrivingLicense() == 0 ){
            alert.setTitle("Wrong Driver Name !");
            alert.setHeaderText(null);
            alert.setContentText("Driver with name: "+ tfDriverName.getText()+" not found in database !");
            alert.showAndWait();
        }
    }

    //--------------------------------------------------- ADD UPDATE DELETE license listeners

    public void addLicenseListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        String name = tfDriverName.getText();
        LocalDate dateOfBirth = birthDatePicker.getValue();
        String placeOfBirth = tfBirthPlace.getText();
        String issuedBy = tfIssuedBy.getText();
        LocalDate issuedDate = issuedDatePicker.getValue();
        LocalDate validUntil = validUntilDatePicker.getValue();

        String licenseCode = tfLicenseCode.getText();
        String categories = tfDrivingCategories.getText();
        String points = tfLicensePoints.getText();

        String driverId = tfDriverId.getText();

        String suspended = "";
        if(  suspendedChoice.getValue() != null )
            suspended = suspendedChoice.getValue().toString();

        if( dateOfBirth != null && issuedDate != null && validUntil != null && !name.equals("") && !placeOfBirth.equals("") && !suspended.equals("") &&
            !issuedBy.equals("") && !licenseCode.equals("") && !categories.equals("") && !points.equals("") && !driverId.equals("") ){
            if( checkValidInteger( driverId ) ){
                if( checkValidPoints( points ) ){

                    String data = name + "#SEPARA#" + dateOfBirth + "#SEPARA#" + placeOfBirth + "#SEPARA#" + issuedBy + "#SEPARA#" + issuedDate +
                             "#SEPARA#" + validUntil + "#SEPARA#" + licenseCode + "#SEPARA#" + categories + "#SEPARA#" + points + "#SEPARA#" + driverId + "#SEPARA#" + suspended ;

                    sendLicenseDataToServerForInsert(data);

                    viewAllLicensesButton.fire();

                    successfullyOperation();
                }
            }
        }
    }

    public void updateLicenseListener(ActionEvent event) throws IOException,ClassNotFoundException {
        String licenseId = tfLicenseId.getText();
        String name = tfDriverName.getText();
        LocalDate dateOfBirth = birthDatePicker.getValue();
        String placeOfBirth = tfBirthPlace.getText();
        String issuedBy = tfIssuedBy.getText();
        LocalDate issuedDate = issuedDatePicker.getValue();
        LocalDate validUntil = validUntilDatePicker.getValue();

        String licenseCode = tfLicenseCode.getText();
        String categories = tfDrivingCategories.getText();
        String points = tfLicensePoints.getText();

        String driverId = tfDriverId.getText();

        String suspended = "";
        if(  suspendedChoice.getValue() != null )
            suspended = suspendedChoice.getValue().toString();

        if( dateOfBirth != null && issuedDate != null && validUntil != null && !name.equals("") && !placeOfBirth.equals("") && !suspended.equals("") &&
                !issuedBy.equals("") && !licenseCode.equals("") && !categories.equals("") && !points.equals("") && !driverId.equals("") && !licenseId.equals("") ){
            if( checkValidInteger( driverId ) ){
                if( checkValidPoints( points ) ){

                    String data =  name + "#SEPARA#" + dateOfBirth + "#SEPARA#" + placeOfBirth + "#SEPARA#" + issuedBy + "#SEPARA#" + issuedDate +
                            "#SEPARA#" + validUntil + "#SEPARA#" + licenseCode + "#SEPARA#" + categories + "#SEPARA#" + points +
                            "#SEPARA#" + driverId + "#SEPARA#" + suspended + "#SEPARA#" + licenseId ;

                    sendLicenseDataToServerForUpdate(data);

                    viewAllLicensesButton.fire();

                    successfullyOperation();
                }
            }
        }
    }

    public void deleteLicenseListener( ActionEvent event ) throws IOException,ClassNotFoundException{
        String licenseId = tfLicenseId.getText();

        if( checkValidInteger( licenseId ) ){

            DrivingLicense license = getLicenseByIdFromServer();

            Alert alertDoubleCheck = new Alert(Alert.AlertType.CONFIRMATION);
            alertDoubleCheck.setTitle("Confirmation Dialog");
            alertDoubleCheck.setHeaderText("Are you sure that you want to delete the following driving license ?");
            alertDoubleCheck.setContentText(license.toString());

            Optional<ButtonType> result = alertDoubleCheck.showAndWait();
            if (result.get() == ButtonType.OK){
                sendLicenseIdToServerForDelete( licenseId );
            }
        }
    }

}
