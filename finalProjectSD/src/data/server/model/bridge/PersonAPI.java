package data.server.model.bridge;

/**
 * Created by plesha on 14-May-18.
 */
public interface PersonAPI {
    public void createUser(String username, String password, String firstName, String lastName, String sex, int age);
}
