package sample.admin.controller;

import data.server.model.Driver;
import data.server.model.User;
import data.server.model.UserActivity;
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
import sample.postoffice.controller.PostOfficeLoginController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by plesha on 31-May-18.
 */
public class AdminPanelController1 {

    private static final Integer port = 8081;
    private static final String userType = "admin";

    @FXML private TableView<User> table_1;

    @FXML private TableColumn<Driver, Integer> idColumn;
    @FXML private TableColumn<Driver, String> userTypeColumn;
    @FXML private TableColumn<Driver, String> usernameColumn;
    @FXML private TableColumn<Driver, String> passwordColumn;
    @FXML private TableColumn<Driver, String> firstNameColumn;
    @FXML private TableColumn<Driver, String> lastNameColumn;
    @FXML private TableColumn<Driver, String> sexColumn;
    @FXML private TableColumn<Driver, String> ageColumn;


    @FXML private TableView<UserActivity> table_2;

    @FXML private TableColumn<Driver, Integer> idActivityColumn;
    @FXML private TableColumn<Driver, Date> dateColumn;
    @FXML private TableColumn<Driver, String> activityColumn;
    @FXML private TableColumn<Driver, Integer> idUserColumn;

    // ---- user text fields
    @FXML private TextField tfUserId;
    @FXML private TextField tfUsername;
    @FXML private TextField tfPassword;
    @FXML private TextField tfFirstname;
    @FXML private TextField tfLastname;
    @FXML private TextField tfAge;
    // choice boxes
    @FXML private ChoiceBox userTypeChoice;
    @FXML private ChoiceBox sexChoice;

    // ---- activity text fields
    @FXML private TextField tfActivityId;
    @FXML private TextField tfActivityDetails;
    @FXML private TextField tfUserIdForActivity;

    @FXML private DatePicker activityDatePicker;

    @FXML private Button viewAllUsersButton;
    @FXML private Button viewAllActivitiesButton;


    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);

    @FXML protected Label usernameLabel;
    protected String username;


    public void initialize(){

        username = PostOfficeLoginController.username;
        usernameLabel.setText(username);

        ObservableList<String> userTypes = FXCollections.observableArrayList("Post Office","Police","Administrator");
        ObservableList<String> sexList = FXCollections.observableArrayList("Male","Female");

        userTypeChoice.setItems( userTypes );
        sexChoice.setItems( sexList );

        idColumn.setCellValueFactory(new PropertyValueFactory<Driver,Integer>("idUser"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("userType"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("password"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("lastName"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("sex"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("age"));

        idColumn.setStyle( "-fx-alignment: CENTER;");
        ageColumn.setStyle( "-fx-alignment: CENTER;");
        userTypeColumn.setStyle( "-fx-alignment: CENTER;");
        usernameColumn.setStyle( "-fx-alignment: CENTER;");
        passwordColumn.setStyle( "-fx-alignment: CENTER;");
        firstNameColumn.setStyle( "-fx-alignment: CENTER;");
        sexColumn.setStyle( "-fx-alignment: CENTER;");
        lastNameColumn.setStyle( "-fx-alignment: CENTER;");

        idActivityColumn.setCellValueFactory(new PropertyValueFactory<Driver,Integer>("idUserActivity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Driver,Date>("date"));
        activityColumn.setCellValueFactory(new PropertyValueFactory<Driver,String>("activity"));
        idUserColumn.setCellValueFactory(new PropertyValueFactory<Driver,Integer>("user_idUser"));

        idActivityColumn.setStyle( "-fx-alignment: CENTER;");
        dateColumn.setStyle( "-fx-alignment: CENTER;");
        idUserColumn.setStyle( "-fx-alignment: CENTER;");

    }

    private void successfullyOperation(){
        alert.setTitle("Operation done!");
        alert.setHeaderText(null);
        alert.setContentText(" The clicked operation was Successfully Performed !");
        alert.showAndWait();
    }


    private boolean checkValidInteger( String data ){

        try{
            Integer id = Integer.parseInt( data );
        }
        catch ( NumberFormatException ex ){
            alert.setTitle("Wrong data !");
            alert.setHeaderText(null);
            alert.setContentText("You need to enter a Number");
            alert.showAndWait();
            return false;
        }
        return true;
    }


    // --------- server operation

    private List<User> getAllUsersFromServer() throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("0");
        p.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream( socket.getInputStream() );
        @SuppressWarnings("unchecked")
        List<User> userList = (List<User>) inObj.readObject();

        return userList;
    }

    private List<UserActivity> getAllUserActivityFromServer() throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("1");
        p.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream( socket.getInputStream() );
        @SuppressWarnings("unchecked")
        List<UserActivity> userActivityList = (List<UserActivity>) inObj.readObject();

        return userActivityList;
    }

    private User getUserByIdFromServer( String userId ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("2"+"#SEPARA#"+userId);
        p.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream( socket.getInputStream() );
        @SuppressWarnings("unchecked")
        User user = (User)inObj.readObject();

        return user;
    }

    private User getUserByUsernameFromServer( String username ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost",port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("3"+"#SEPARA#"+username);
        p.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream( socket.getInputStream() );
        @SuppressWarnings("unchecked")
        User user = (User)inObj.readObject();

        return user;
    }

    private void sendUserDataToServerForInsert( String userData ) throws IOException,ClassNotFoundException{
        Socket socket = new Socket("localhost",port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("4"+"#SEPARA#"+userData);
        p.println(userType);
    }
    // 5 to update
    // 6 to delete

    private void sendUserDataToServerForUpdate( String userData ) throws IOException,ClassNotFoundException{
        Socket socket = new Socket("localhost",port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("5"+"#SEPARA#"+userData);
        p.println(userType);
    }

    private void sendUserIdToServerForDelete( String userId ) throws IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost",port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("6"+"#SEPARA#"+userId);
        p.println(userType);

        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        String found = (String) inObj.readObject();

        if( found.equals("foundAndDeleted") ){
            viewAllActivitiesButton.fire();
        }
        else{
            alert.setTitle("User not found !");
            alert.setHeaderText(null);
            alert.setContentText("User with ID "+userId + " not found in database !");
            alert.showAndWait();
        }
    }


    // ------ activity
    private UserActivity getActivityByIdFromServer( String idActivity ) throws IOException,ClassNotFoundException{
        Socket socket = new Socket("localhost", port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("7"+"#SEPARA#"+idActivity);
        p.println(userType);

        // --------- get data from socket
        ObjectInputStream inObj = new ObjectInputStream( socket.getInputStream() );
        @SuppressWarnings("unchecked")
        UserActivity userActivity = (UserActivity)inObj.readObject();

        return userActivity;
    }

    private void sendActivityDataToServerForInsert( String userData ) throws IOException,ClassNotFoundException{
        Socket socket = new Socket("localhost",port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("8"+"#SEPARA#"+userData);
        p.println(userType);
    }

    private void sendActivityDataToServerForUpdate( String userData ) throws IOException,ClassNotFoundException{
        Socket socket = new Socket("localhost",port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("9"+"#SEPARA#"+userData);
        p.println(userType);
    }

    private void sendActivityIdToServerForDelete( String idActivity ) throws IOException,ClassNotFoundException{
        Socket socket = new Socket("localhost",port);
        PrintStream p = new PrintStream( socket.getOutputStream() );

        p.println("#0"+"#SEPARA#"+idActivity);
        p.println(userType);

        ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
        String found = (String) inObj.readObject();

        if( found.equals("foundAndDeleted") ){
            viewAllActivitiesButton.fire();
        }
        else{
            alert.setTitle("Activity not found!");
            alert.setHeaderText(null);
            alert.setContentText("Activity with ID "+idActivity + " not found in database !");
            alert.showAndWait();
        }
    }

    // ------------------- listeners
    public void viewAllUsersListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        ObservableList<User> users = FXCollections.observableArrayList( getAllUsersFromServer()  );
        table_1.setItems( users );
    }

    public void viewAllUserActivityListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        ObservableList<UserActivity> userActivities = FXCollections.observableArrayList( getAllUserActivityFromServer()  );
        table_2.setItems( userActivities );
    }

    public void searchUserByIdListener( ActionEvent event ) throws IOException,ClassNotFoundException  {

        String userId = tfUserId.getText();
        if( checkValidInteger(userId) ) {

            User user = getUserByIdFromServer(userId);
            if( user.getIdUser() != -1 ) {
                ObservableList<User> foundUser = FXCollections.observableArrayList( user );
                table_1.setItems(foundUser);
            }
            else{
                alert.setTitle("User not found !");
                alert.setHeaderText(null);
                alert.setContentText("User with ID " + userId + " not found in database !");
                alert.showAndWait();
            }

        }
    }

    public void searchByUsernameListener( ActionEvent event ) throws IOException,ClassNotFoundException  {

        String username = tfUsername.getText();
        if( !username.equals("") ){
            User user = getUserByUsernameFromServer( username );
            if( user.getIdUser() != -1 ) {
                ObservableList<User> foundUser = FXCollections.observableArrayList( user );
                table_1.setItems(foundUser);
            }
            else{
                alert.setTitle("User not found !");
                alert.setHeaderText(null);
                alert.setContentText("User with username: " + username + " not found in database !");
                alert.showAndWait();
            }
        }
        else{
            alert.setTitle("Empty text field !!");
            alert.setHeaderText(null);
            alert.setContentText("You need to insert a username in order to perform this action !");
            alert.showAndWait();
        }
    }

    public void addUserListener( ActionEvent event ) throws IOException,ClassNotFoundException  {

      //  String idUser = tfUserId.toString();
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        String firstname = tfFirstname.getText();
        String lastname = tfLastname.getText();
        String age = tfAge.getText();


        String userType = "";
        if(  userTypeChoice.getValue() != null )
            userType = userTypeChoice.getValue().toString();

        String sex = "";
        if( sexChoice.getValue() != null )
            sex =  sexChoice.getValue().toString();


        //System.out.println("age = " + age);
        if( checkValidInteger(age) ) {
            if (!username.equals("") && !password.equals("") && !firstname.equals("") && !lastname.equals("")) {

                String userData = userType +"#SEPARA#"+ username + "#SEPARA#" + password + "#SEPARA#" + firstname + "#SEPARA#" + lastname + "#SEPARA#" +
                                  sex + "#SEPARA#" + age;

                sendUserDataToServerForInsert( userData );

                successfullyOperation();
                viewAllUsersButton.fire();
            }
        }
    }
    // update // delete

    public void updateUserListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        String userId = tfUserId.getText();
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        String firstname = tfFirstname.getText();
        String lastname = tfLastname.getText();
        String age = tfAge.getText();


        String userType = "";
        if(  userTypeChoice.getValue() != null )
            userType = userTypeChoice.getValue().toString();

        String sex = "";
        if( sexChoice.getValue() != null )
            sex =  sexChoice.getValue().toString();

        if( checkValidInteger(userId) ){
            if( checkValidInteger(age) ){
                if (!username.equals("") && !password.equals("") && !firstname.equals("") && !lastname.equals("")) {

                    String userData = userId +"#SEPARA#"+  userType +"#SEPARA#"+ username + "#SEPARA#" + password + "#SEPARA#" + firstname + "#SEPARA#" + lastname + "#SEPARA#" +
                            sex + "#SEPARA#" + age;

                    sendUserDataToServerForUpdate( userData );

                    successfullyOperation();
                    viewAllUsersButton.fire();
                }
            }
        }

    }

    public void deleteUserListener( ActionEvent event ) throws IOException,ClassNotFoundException {

        String userId = tfUserId.getText();

        if( checkValidInteger(userId) ){

            User user = getUserByIdFromServer( userId );

            Alert alertDoubleCheck = new Alert(Alert.AlertType.CONFIRMATION);
            alertDoubleCheck.setTitle("Confirmation Dialog");
            alertDoubleCheck.setHeaderText("Are you sure that you want to delete the following user ?");
            alertDoubleCheck.setContentText(user.toString());

            Optional<ButtonType> result = alertDoubleCheck.showAndWait();
            if (result.get() == ButtonType.OK) {

                sendUserIdToServerForDelete(userId);

                successfullyOperation();
                viewAllUsersButton.fire();
            }
        }
    }



    // ----------------  activity listeners


    public void searchActivityByIdListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        String idActivity = tfActivityId.getText();

        if( checkValidInteger(idActivity) ){
            UserActivity userActivity = getActivityByIdFromServer( idActivity );
            if( userActivity.getIdUserActivity() != -1 ) {
                ObservableList<UserActivity> foundActivity = FXCollections.observableArrayList( userActivity );
                table_2.setItems(foundActivity);
            }
            else{
                alert.setTitle("Activity not found !");
                alert.setHeaderText(null);
                alert.setContentText("Activity with ID " + idActivity + " not found in database !");
                alert.showAndWait();
            }
        }
    }

    public void findActivitiesOfUserListener( ActionEvent event ) throws IOException,ClassNotFoundException  {
        String userId = tfUserIdForActivity.getText();

        if( checkValidInteger(userId) ) {

            List<UserActivity> allUserActivityList = getAllUserActivityFromServer();
            List<UserActivity> activitiesOfUser = new ArrayList<>();

            for( UserActivity ua : allUserActivityList ){
                if( ua.getUser_idUser() == Integer.parseInt(userId) ){
                    activitiesOfUser.add( ua );
                }
            }

            if( activitiesOfUser.size() != 0 ){
                ObservableList<UserActivity> foundActivities = FXCollections.observableArrayList( activitiesOfUser );
                table_2.setItems(foundActivities);
            }
            else{
                alert.setTitle("Activities not found !");
                alert.setHeaderText(null);
                alert.setContentText("Activities of user with ID " + userId + " not found in database !");
                alert.showAndWait();
            }
        }
    }

    public void addActivityListener( ActionEvent event ) throws IOException,ClassNotFoundException {

        //String idActivity = tfActivityId.getText();
        String activity = tfActivityDetails.getText();
        String userId = tfUserIdForActivity.getText();

        String date = "";
        if (activityDatePicker.getValue() != null)
            date = activityDatePicker.getValue().toString();

        if (checkValidInteger(userId)) {


            String activityData = activity + "#SEPARA#" + date + "#SEPARA#" + userId;

            sendActivityDataToServerForInsert(activityData);

            successfullyOperation();
            viewAllActivitiesButton.fire();

        }
    }

    public void updateActivityListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        String idActivity = tfActivityId.getText();
        String activity = tfActivityDetails.getText();
        String userId = tfUserIdForActivity.getText();

        String date = "";
        if (activityDatePicker.getValue() != null)
            date = activityDatePicker.getValue().toString();

        if( checkValidInteger(idActivity) ) {
            if (checkValidInteger(userId)) {
                String activityData = idActivity + "#SEPARA#" + activity + "#SEPARA#" + date + "#SEPARA#" + userId;

                successfullyOperation();
                sendActivityDataToServerForUpdate(activityData);

                viewAllActivitiesButton.fire();
            }
        }
    }

    public void deleteActivityListener( ActionEvent event ) throws IOException,ClassNotFoundException {
        String idActivity = tfActivityId.getText();

        if( checkValidInteger(idActivity) ){

            UserActivity activity = getActivityByIdFromServer( idActivity );

            Alert alertDoubleCheck = new Alert(Alert.AlertType.CONFIRMATION);
            alertDoubleCheck.setTitle("Confirmation Dialog");
            alertDoubleCheck.setHeaderText("Are you sure that you want to delete the following activity ?");
            alertDoubleCheck.setContentText(activity.toString());

            Optional<ButtonType> result = alertDoubleCheck.showAndWait();
            if (result.get() == ButtonType.OK) {

                sendActivityIdToServerForDelete(idActivity);

                successfullyOperation();
                viewAllActivitiesButton.fire();
            }
        }

    }

    // -- logout listener
    public void logoutListener(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("postOfficeEmployeeLogin.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Post Office Employee Login Panel !");
        stage.setScene(new Scene(root));
        stage.setResizable( false );
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
