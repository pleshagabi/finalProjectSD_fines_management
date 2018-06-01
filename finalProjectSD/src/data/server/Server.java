package data.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Created by plesha on 26-May-18.
 */
public class Server {

    public static void main(String[] args) throws IOException,ParseException,ClassNotFoundException {

        System.out.println("Server is running...\nWaiting for client...");
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        ServerSocket serverSocket = new ServerSocket(8081);

        try {
            while (true) {

                Socket socket = serverSocket.accept();

                try {
                    Scanner sc = new Scanner(socket.getInputStream());

                    String data = sc.nextLine();

                    System.out.println(data);
                    System.out.println("acum In server=" + data );

                    String typeOfUser = sc.nextLine();
                    System.out.println("acum In server=" + typeOfUser );


                    if( typeOfUser.equalsIgnoreCase("login") ){
                        ServerLogin.processLogin(socket);
                    }
                    else if( typeOfUser.equalsIgnoreCase("postoffice") ){
                        ServerPostOfficePanel.processRequest(socket,data);
                    }
                    else if( typeOfUser.equalsIgnoreCase("police") ){
                        ServerPolicePanel.processRequest(socket,data);
                    }
                    else if( typeOfUser.equalsIgnoreCase("police_driving_license") ){
                        ServerPoliceDrivingLicensePanel.processRequest(socket,data);
                    }
                    else if( typeOfUser.equalsIgnoreCase("admin") ){
                        ServerAdminPanel.processRequest(socket,data);
                    }

                } finally {
                    socket.close();
                }
            }
        }
        finally {
            serverSocket.close();
        }

    }


}
