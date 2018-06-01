package data.server;

import data.server.model.*;
import data.server.model.nullobject.AbstractDriver;
import data.server.model.nullobject.DriverFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by plesha on 26-May-18.
 */
public class ServerPostOfficePanel implements Serializable {

    private static final long serialVersionUID = 6128016096756071380L;


    private static void sendDriversListThroughSocket( Socket socket ) throws IOException{
        List<Driver> driverList = Driver.read();

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( driverList );
    }

    private static void sendNotPaidFinesListThroughSocket( Socket socket ) throws IOException{
        List<Fine> fineList = Fine.read();
        List<Fine> notPaidFineList = new ArrayList<>();

        for( Fine fine : fineList ){
            if( fine.getIsPayd() == 0 ){
                notPaidFineList.add( fine );
            }
        }

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( notPaidFineList );
    }

    private static void sendFoundDriverByIdThroughSocket( Socket socket, String data ) throws IOException{
        //Driver foundDriver = Driver.findByID( Integer.parseInt( data ) );
        AbstractDriver foundDriver = DriverFactory.getDriver(  Integer.parseInt( data )  );

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( foundDriver );
    }

    private static void sendFoundDriverByNameThroughSocket( Socket socket, String data ) throws IOException {
        Driver foundDriver = Driver.findByName( data );

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( foundDriver );
    }

    private static void registerDriverPaymentAndRemoveFine( Socket socket, String data ) throws IOException {

        String[] tokens = data.split("#SEPARA#"); // tokens[0] e ID-u operatiei
                                                        // tokens[1] #DONT_DELETE# sau #DO_DELETE#
                                                        // tokens[2] driver id
                                                        // tokens[3] fine id

        //System.out.println("TOKENS= "+tokens[0] + " " +tokens[1] + " " +tokens[2] + " " +tokens[3] + " ");
        int idFine = Integer.parseInt(tokens[3]);
        int idDriver = Integer.parseInt(tokens[2]);

        Fine foundFine = Fine.findByID(idFine);
       // Driver foundDriver = Driver.findByID(idDriver);
        AbstractDriver foundDriver = DriverFactory.getDriver(  idDriver );


        if( tokens[1].equals("#DONT_REGISTER#") ) {


            HashMap<AbstractDriver,Fine> hashMap = new HashMap<>();
            hashMap.put( foundDriver, foundFine );

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(hashMap);
        }
        else if( tokens[1].equals("#REGISTER_PAYMENT#") ){
            //Fine.delete( idFine );
           // Byte isPaid = 1;

            Notification notification = new Notification( foundDriver.getIdDriver(),  foundFine.getIdFine() );
            Notification.create( notification );

            //PolicePanelController.numberOfNotifications = Notification.getNrOfNotifications();


            /*Fine registeredFine = new Fine(foundFine.getIdFine(), foundFine.getDateFineCommited(), foundFine.getCrimeType(), foundFine.getPrice(),
                    foundFine.getDeadlineDate(), foundFine.getDriver_idDriver(), isPaid, foundFine.getPoints());

            Fine.update( registeredFine );*/
        }

    }

    private static void sendFoundFineByIdThroughSocket( Socket socket, String data ) throws IOException {
        Fine foundFine = Fine.findByID( Integer.parseInt( data ) );

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( foundFine );
    }

    private static void sendFoundFinesByDriverIdThroughSocket( Socket socket, String data ) throws IOException {
        List<Fine> fineList = Fine.findFinesByDriverId( Integer.parseInt(data) );
        List<Fine> notPaidFines = new ArrayList<>();

        for( Fine fine : fineList ){
            if( fine.getIsPayd() == 0 ){
                notPaidFines.add( fine );
            }
        }

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( notPaidFines );
    }

    private static void sendFoundPaidFinesThroughSocket(Socket socket, String data ) throws IOException {
        List<Fine> fineList = Fine.findFinesByDriverId( Integer.parseInt(data) );
        List<Fine> paidFineList = new ArrayList<>();

        for( Fine fine : fineList ){
            if( fine.getIsPayd() == 1 ){
                paidFineList.add( fine );
            }
        }

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( paidFineList );
    }
    private static void insertActivitiesIntoDatabase(Socket socket, String userName ) throws IOException,ClassNotFoundException {

        //System.out.println("username="+userName);
        User user = User.findByUsername( userName );

        ObjectInputStream inObj = new ObjectInputStream( socket.getInputStream() );
        List<String> activities  = ( List<String>) inObj.readObject();

        String allActivities = "";
        for( String str : activities ){
            allActivities += str;
        }

        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);

        //System.out.println("Activ: "+allActivities);
        //System.out.println("user: "+ user.toString());

        UserActivity userActivity = new UserActivity(allActivities,sqlDate,user.getIdUser());

        UserActivity.createActivityInDatabase( userActivity );
    }


    protected static void processRequest(Socket socket, String data) throws IOException,ClassNotFoundException{

        // ----------------------------------------- DRIVER TABLE processing
        if( data.charAt(0) == '0' ){
            sendDriversListThroughSocket( socket );
        }
        else if( data.charAt(0) == '1' ) {
            data = truncateData( data );
            sendFoundDriverByIdThroughSocket( socket, data );
        }
        else if( data.charAt(0) == '2' ) {
            data = truncateData( data );
            sendFoundDriverByNameThroughSocket( socket, data );
        }
        else if( data.charAt(0) == '3' ) {
            data = truncateData( data );
            registerDriverPaymentAndRemoveFine( socket, data );
        }
        // ----------------------------------------- FINE TABLE processing
        else if( data.charAt(0) == '4' ){
            sendNotPaidFinesListThroughSocket( socket );
        }
        else if( data.charAt(0) == '5' ){
            data = truncateData( data );
            sendFoundFineByIdThroughSocket( socket, data );
        }
        else if( data.charAt(0) == '6' ){
            data = truncateData( data );
            sendFoundFinesByDriverIdThroughSocket( socket, data );
        }
        else if( data.charAt(0) == '7' ){
            data = truncateData( data );
            sendFoundPaidFinesThroughSocket( socket, data );
        }
        else if( data.charAt(0) == '8' ){
            data = truncateData( data );
            insertActivitiesIntoDatabase( socket, data );
        }
    }

    private static String truncateData( String data ){
        String resultData = "";
        for( int i = 1 ; i<data.length() ; i++ ){
            resultData += data.charAt(i);
        }
        return resultData;
    }

}
