package data.server;

import data.server.model.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;

/**
 * Created by plesha on 26-May-18.
 */

public class ServerLogin implements Serializable {

    private static final long serialVersionUID = 6128016096756071380L;

    protected static void processLogin(Socket socket) throws IOException{
        List<User> userList = User.read();

        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
        oos.writeObject( userList );
    }

}
