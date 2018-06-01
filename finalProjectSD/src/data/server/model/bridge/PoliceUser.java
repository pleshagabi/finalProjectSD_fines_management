package data.server.model.bridge;

import data.server.model.User;

/**
 * Created by plesha on 14-May-18.
 */
public class PoliceUser implements PersonAPI {

    public void createUser(String username, String password, String firstName, String lastName, String sex, int age){
        User user = new User("police", username, password, firstName, lastName, sex, age );
        User.createUserInDatabase( user );
    }

}
