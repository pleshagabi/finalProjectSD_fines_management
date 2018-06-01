package data.server;

import data.server.model.*;
import data.server.model.nullobject.AbstractDriver;
import data.server.model.nullobject.DriverFactory;
import sample.factory.report.Report;
import sample.factory.report.ReportFactory;
import sample.send.email.Email;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Created by plesha on 28-May-18.
 */
public class ServerPolicePanel implements Serializable {

    private static final long serialVersionUID = 6128016096756071380L;

    // ---------------------------------------------------------- driver processing--------------------------------------
    private static void insertDriverFromSocketData( String data ){
        String[] tokens = data.split("#SEPARA#");
        Driver newDriver = new Driver(tokens[1],Integer.parseInt(tokens[2]),tokens[3],tokens[4],tokens[5]);

        Driver.createDriverInDatabase( newDriver );
    }

    private static void updateDriverFromSocketData( String data ){
        String[] tokens = data.split("#SEPARA#");
        Driver updateDriver = new Driver(Integer.parseInt(tokens[1]),tokens[2],Integer.parseInt(tokens[3]),tokens[4],tokens[5],tokens[6]);

        Driver.update( updateDriver );
    }

    private static void deleteDriverByIdFromSocket( Socket socket, String data ) throws IOException{

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        data = data.substring(1,data.length());

        if( Driver.delete( Integer.parseInt(data) ) ){
            oos.writeObject("foundAndDeleted");
        }else{
            oos.writeObject("notFound");
        }
    }

    // ---------------------------------------------------------- fine processing--------------------------------------

    private static void insertFineFromSocket( String data ) throws ParseException {
        String[] tokens = data.split("#SEPARA#");

        Date crimeCommittedDate = Date.valueOf( tokens[1] );
        Date fineDeadlineDate = Date.valueOf( tokens[4] );

        Byte isPaid = 0;
        if( tokens[6].equals("Yes") )
            isPaid = 1;

        Fine newFine = new Fine(crimeCommittedDate, tokens[2], Integer.parseInt(tokens[3]), fineDeadlineDate,
                                Integer.parseInt(tokens[5]), isPaid, Integer.parseInt(tokens[7]) );

        Fine.createFineInDatabase( newFine );
    }

    private static void updateFineFromSocket( String data ) throws ParseException {
        String[] tokens = data.split("#SEPARA#");

        Date crimeCommittedDate = Date.valueOf( tokens[2] );
        Date fineDeadlineDate = Date.valueOf( tokens[5] );

        Byte isPaid = 0;
        if( tokens[7].equals("Yes") )
            isPaid = 1;

        Fine newFine = new Fine( Integer.parseInt(tokens[1]),crimeCommittedDate, tokens[3], Integer.parseInt(tokens[4]),
                                 fineDeadlineDate, Integer.parseInt(tokens[6]), isPaid, Integer.parseInt(tokens[8]));

        Fine.update( newFine );
    }

    private static void deleteFineByIdFromSocket( Socket socket, String data  ) throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        data = data.substring(1,data.length());

        if( Fine.delete( Integer.parseInt(data) ) ){
            oos.writeObject("foundAndDeleted");
        }else{
            oos.writeObject("notFound");
        }

    }

    private static void createDesiredReport( String data ) throws IOException {
        String[] tokens = data.split("#SEPARA#");

        List<Fine> fineList = Fine.read();
        List<Driver> driverList = Driver.read();

        if( tokens[1].equals("pdf") ) {
            ReportFactory reportFactory = new ReportFactory();
            Report report = reportFactory.getReportType("pdf");
            report.createReportFile(fineList,driverList);
        }else if ( tokens[1].equals("csv") ) {
            ReportFactory reportFactory = new ReportFactory();
            Report report = reportFactory.getReportType("csv");
            report.createReportFile(fineList,driverList);
        }
    }

    private static void sendNotificationData( Socket socket )  throws IOException {

        List<Notification> notificationList = Notification.read();

        HashMap<Driver,Fine> hashMap = new HashMap<>();

        AbstractDriver driver;
        Fine fine;
        for( Notification no : notificationList ){
           // driver = Driver.findByID( no.getDriver_idDriver() );
             driver = DriverFactory.getDriver(  no.getDriver_idDriver() );
             fine = Fine.findByID( no.getFine_idFine() );
            hashMap.put( (Driver)driver, fine );
        }

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(hashMap);

    }

    private static void markFineAsPaidAndDeleteNotification( Socket socket, String data ) throws IOException {
        String[] tokens = data.split("#SEPARA#");

        int fineId =  Integer.parseInt(tokens[1]);

        Fine fine = Fine.findByID( fineId );

        if( fine.getIdFine() != -1 ){ // found the fine

            Notification foundNotification = Notification.findByFineId( fineId );

            if( foundNotification.getIdNotification() != 0 ) {
                // update the fine to mark as paid
                Byte isPaid = 1;
                fine.setIsPayd(isPaid);
                Fine.update(fine);

                // send an e-mail to notify the driver that the fine was successfully registered !
               // Driver driver = Driver.findByID( fine.getDriver_idDriver() );
                AbstractDriver driver = DriverFactory.getDriver(  fine.getDriver_idDriver() );
                Email.sendEmail( (Driver)driver, fine );
            }


            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            if(  Notification.delete(foundNotification.getIdNotification()) ) {
                oos.writeObject("foundAndDeleted");
            }else{
                oos.writeObject("notFound");
            }
        }
    }

    private static void sendNrOfNotificationsThroughSocket( Socket socket ) throws IOException{

        int nr = Notification.getNrOfNotifications();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(nr);
    }

    private static void insertActivitiesIntoDatabase( Socket socket, String data ) throws IOException{
        String[] tokens = data.split("#SEPARA#");

        User user = User.findByUsername( tokens[1] );

        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);

        UserActivity userActivity = new UserActivity(tokens[2],sqlDate,user.getIdUser());

        UserActivity.createActivityInDatabase( userActivity );
    }

    protected static void processRequest( Socket socket, String data) throws IOException,ParseException{
        // ----------------------------------------- DRIVER TABLE processing
        if( data.charAt(0) == '0' ){ // driver processing
            insertDriverFromSocketData( data );
        } else if( data.charAt(0) == '1' ){
            updateDriverFromSocketData( data );
        } else if( data.charAt(0) == '2' ){
            deleteDriverByIdFromSocket( socket, data );
        } else if( data.charAt(0) == '3' ){  // fine processing
            insertFineFromSocket( data );
        }else if( data.charAt(0) == '4' ){
            updateFineFromSocket( data );
        }else if( data.charAt(0) == '5' ){
            deleteFineByIdFromSocket( socket, data );
        }else if( data.charAt(0) == '6' ){
            createDesiredReport( data );
        }else if( data.charAt(0) == '7' ){ // send notifications data
            sendNotificationData( socket );
        }else if( data.charAt(0) == '8' ){ // mark fine as paid
            markFineAsPaidAndDeleteNotification( socket, data );
        }else if( data.charAt(0) == '9' ){
            sendNrOfNotificationsThroughSocket( socket );
        }else if( data.charAt(0) == '#' ){
            insertActivitiesIntoDatabase(socket,data);
        }
    }


}
