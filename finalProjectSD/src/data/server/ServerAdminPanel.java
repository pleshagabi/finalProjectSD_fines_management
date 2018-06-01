package data.server;

import data.server.model.User;
import data.server.model.UserActivity;
import data.server.model.bridge.AdminUser;
import data.server.model.bridge.Person;
import data.server.model.bridge.PoliceUser;
import data.server.model.bridge.PostOfficeUser;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.util.List;

/**
 * Created by plesha on 31-May-18.
 */
public class ServerAdminPanel {


    private static void sendUsersThroughSocket( Socket socket ) throws IOException{
        List<User> userList = User.read();

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( userList );
    }

    private static void sendUserActivityThroughSocket( Socket socket ) throws IOException{
        List<UserActivity> userActivityList = UserActivity.read();

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( userActivityList );
    }

    private static void sendFoundUserByIdThroughSocket( Socket socket, String data ) throws IOException{
        String[] tokens = data.split("#SEPARA#");

        User foundUser = User.findByID( Integer.parseInt(tokens[1]) );

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( foundUser );

    }

    private static void sendFoundUserByUsernameThroughSocket ( Socket socket, String data )  throws IOException {
        String[] tokens = data.split("#SEPARA#");

        User foundUser = User.findByUsername( tokens[1] );

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( foundUser );
    }

    private static void insertUseFromSocketData( Socket socket, String data ) throws IOException {
        String[] tokens = data.split("#SEPARA#");

        Person person = person = new User();

        // create user using bridge
        if( tokens[1].equalsIgnoreCase("Police") )
            person = new User( tokens[2], tokens[3], tokens[4], tokens[5], tokens[6],Integer.parseInt(tokens[7]), new PoliceUser());
        else if( tokens[1].equalsIgnoreCase("Post Office") )
            person = new User( tokens[2], tokens[3], tokens[4], tokens[5], tokens[6],Integer.parseInt(tokens[7]), new PostOfficeUser());
        else if( tokens[1].equalsIgnoreCase("Administrator") )
            person = new User( tokens[2], tokens[3], tokens[4], tokens[5], tokens[6],Integer.parseInt(tokens[7]), new AdminUser());

        person.create();
    }

    private static void updateUseFromSocketData( Socket socket, String data ) throws IOException {
        String[] tokens = data.split("#SEPARA#");

        User user = new User();

        if( tokens[2].equalsIgnoreCase("Police") )
            user = new User( Integer.parseInt(tokens[1]),"police", tokens[3], tokens[4], tokens[5], tokens[6], tokens[7],Integer.parseInt(tokens[8]), new PoliceUser());
        else if( tokens[2].equalsIgnoreCase("Post Office") )
            user = new User( Integer.parseInt(tokens[1]),"postoffice",tokens[3], tokens[4], tokens[5], tokens[6], tokens[7],Integer.parseInt(tokens[8]), new PostOfficeUser());
        else if( tokens[2].equalsIgnoreCase("Administrator") )
            user = new User( Integer.parseInt(tokens[1]),"admin",tokens[3], tokens[4], tokens[5], tokens[6], tokens[7],Integer.parseInt(tokens[8]), new AdminUser());

        User.update( user );
    }

    private static void deleteUserByIdFromSocketData( Socket socket, String data ) throws IOException {
        String[] tokens = data.split("#SEPARA#");

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        if( User.delete( Integer.parseInt(tokens[1]) ) ){
            oos.writeObject("foundAndDeleted");
        }else{
            oos.writeObject("notFound");
        }
    }



    private static void sendFoundActivityByIdThroughSocket( Socket socket, String data ) throws IOException{
        String[] tokens = data.split("#SEPARA#");

        UserActivity foundUserActivity = UserActivity.findByID( Integer.parseInt(tokens[1]) );

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( foundUserActivity );

    }

    private static void insertActivityFromSocketData( Socket socket, String data ) throws IOException {
        String[] tokens = data.split("#SEPARA#");

        UserActivity userActivity = new UserActivity( tokens[1], Date.valueOf(tokens[2]), Integer.parseInt(tokens[3]));

        UserActivity.createActivityInDatabase( userActivity );
    }

    private static void updateActivityFromSocketData( Socket socket, String data ) throws IOException {
        String[] tokens = data.split("#SEPARA#");

        UserActivity userActivity = new UserActivity( Integer.parseInt(tokens[1]), tokens[2], Date.valueOf(tokens[3]), Integer.parseInt(tokens[4]));

        UserActivity.update( userActivity );
    }


    private static void deleteActivityByIdFromSocket( Socket socket, String data ) throws IOException{
        String[] tokens = data.split("#SEPARA#");

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        if( UserActivity.delete( Integer.parseInt(tokens[1]) ) ){
            oos.writeObject("foundAndDeleted");
        }else{
            oos.writeObject("notFound");
        }
    }


    protected static void processRequest(Socket socket, String data) throws IOException {
        if (data.charAt(0) == '0') {
            sendUsersThroughSocket(socket);
        } else if (data.charAt(0) == '1') {
            sendUserActivityThroughSocket(socket);
        } else if (data.charAt(0) == '2') {
            sendFoundUserByIdThroughSocket(socket, data);
        } else if (data.charAt(0) == '3') {
            sendFoundUserByUsernameThroughSocket(socket, data);
        } else if (data.charAt(0) == '4'){
            insertUseFromSocketData( socket, data );
        }else if (data.charAt(0) == '5') {
            updateUseFromSocketData(socket, data);
        }else if (data.charAt(0) == '6') {
            deleteUserByIdFromSocketData(socket, data);
        } else if (data.charAt(0) == '7'){
            sendFoundActivityByIdThroughSocket( socket, data );
        }else if (data.charAt(0) == '8'){
            insertActivityFromSocketData( socket, data );
        }else if (data.charAt(0) == '9'){
            updateActivityFromSocketData( socket, data );
        }else if (data.charAt(0) == '#' && data.charAt(1) == '0'){
            deleteActivityByIdFromSocket( socket, data);
        }
    }
}
