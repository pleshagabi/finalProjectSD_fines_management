package data.server;

import data.server.model.DrivingLicense;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.util.List;

/**
 * Created by plesha on 29-May-18.
 */
public class ServerPoliceDrivingLicensePanel {

    //private static final long serialVersionUID = 6128016096756071380L;

    private static void sendDrivingLicensesThroughSocket( Socket socket, String data ) throws IOException{
        List<DrivingLicense> licenseList = DrivingLicense.read();

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( licenseList );
    }

    private static void sendFoundLicenseByIdThroughSocket( Socket socket, String data ) throws IOException{
        String[] tokens = data.split("#SEPARA#");

        DrivingLicense drivingLicense = DrivingLicense.findByID( Integer.parseInt(tokens[1]) );

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( drivingLicense );
    }

    private static void sendFoundLicenseByDriverNameThroughSocket( Socket socket, String data ) throws IOException{
        String[] tokens = data.split("#SEPARA#");

        DrivingLicense drivingLicense = DrivingLicense.findByName( tokens[1] );

        System.out.println("FOUND="+drivingLicense.toString());

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( drivingLicense );
    }

    private static void insertLicenseFromSocket ( Socket socket, String data ) throws IOException {
        String[] tokens = data.split("#SEPARA#");

        Date sqlBirthDate = Date.valueOf(tokens[2]);
        Date sqlIssuedDate = Date.valueOf(tokens[5]);
        Date sqlValidUntilDate = Date.valueOf(tokens[6]);

        DrivingLicense newDrivingLicense = new DrivingLicense(tokens[1], sqlBirthDate, tokens[3], tokens[4], sqlIssuedDate, sqlValidUntilDate,
                    tokens[7], tokens[8], Integer.parseInt(tokens[9]), Integer.parseInt(tokens[10]), tokens[11]);

        DrivingLicense.create( newDrivingLicense );
    }

    private static void updateLicenseFromSocket ( Socket socket, String data ) throws IOException {
        String[] tokens = data.split("#SEPARA#");

        Date sqlBirthDate = Date.valueOf(tokens[2]);
        Date sqlIssuedDate = Date.valueOf(tokens[5]);
        Date sqlValidUntilDate = Date.valueOf(tokens[6]);

        // tokens[12] este idul licentei
        DrivingLicense newDrivingLicense = new DrivingLicense(Integer.parseInt(tokens[12]), tokens[1], sqlBirthDate, tokens[3], tokens[4], sqlIssuedDate, sqlValidUntilDate,
                tokens[7], tokens[8], Integer.parseInt(tokens[9]), Integer.parseInt(tokens[10]), tokens[11]);

        DrivingLicense.update( newDrivingLicense );
    }

    private static void deleteLicenseByIdFromSocket( Socket socket, String data ) throws IOException{

        String[] tokens = data.split("#SEPARA#");
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        if( DrivingLicense.delete( Integer.parseInt(tokens[1]) ) ){
            oos.writeObject("foundAndDeleted");
        }else{
            oos.writeObject("notFound");
        }
    }

    protected static void processRequest(Socket socket, String data) throws IOException {

        if( data.charAt(0) == '0' ){
            sendDrivingLicensesThroughSocket( socket, data );
        }else if( data.charAt(0) == '1' ){
            sendFoundLicenseByIdThroughSocket( socket, data );
        }else if( data.charAt(0) == '2' ){
            sendFoundLicenseByDriverNameThroughSocket( socket, data );
        }else if( data.charAt(0) == '3' ){
            insertLicenseFromSocket( socket, data );
        }else if( data.charAt(0) == '4' ){
            updateLicenseFromSocket( socket, data );
        }else if( data.charAt(0) == '5' ) {
            deleteLicenseByIdFromSocket( socket, data );
        }
    }

}
