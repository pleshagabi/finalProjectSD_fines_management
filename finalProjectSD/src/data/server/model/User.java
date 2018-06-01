package data.server.model;



import data.server.model.bridge.*;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import sample.factory.session.MySessionFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by plesha on 25-May-18.
 */
@Entity
@Table(name="user")
public class User extends Person implements Serializable{

    private static final long serialVersionUID = 6128016096756071380L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "idUser")
    private int idUser;

    @Column(name = "userType")
    private String userType;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "sex")
    private String sex;

    @Column(name = "age")
    private int age;


    public User(){

    }

    public User( int idUser ){
        this.idUser = idUser;
    }

    public User(int idUser, String userType, String username, String password, String firstName, String lastName, String sex, int age, PersonAPI personAPI) {
        super( personAPI );
        this.userType = userType;
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
    }

    public User( String username, String password, String firstName, String lastName, String sex, int age, PersonAPI personAPI){
        super(  personAPI );
        this.username = username;
        this.password = password;
        this.firstName =firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
    }

    public User(String userType, String username, String password, String firstName, String lastName, String sex, int age) {
        this.userType = userType;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
    }


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public static Integer createUserInDatabase(User user) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        session.beginTransaction();
        session.save( user );
        session.getTransaction().commit();

        session.close();

        System.out.println("Successfully created " + user.toString());
        return user.getIdUser();

    }

    public static List<User> read() {
        Session session = MySessionFactory.getSessionFactory().openSession();

        @SuppressWarnings("unchecked")
        List<User> users = session.createQuery("FROM User").list();

        session.close();

        System.out.println("Found " + users.size() + " Employees");

        return users;
    }

    public static void update(User user) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        User updateUser = (User) session.load(User.class, user.getIdUser());
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getFirstName());
        updateUser.setAge(user.getAge());
        updateUser.setUserType(user.getUserType());
        updateUser.setUsername(user.getUsername());
        updateUser.setPassword(user.getPassword());
        updateUser.setSex(user.getSex());

        session.getTransaction().commit();
        session.close();

        System.out.println("Successfully updated " + user.toString());
    }

    public static boolean delete(Integer id) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        User user = findByID(id);

        if( user.getIdUser() != -1 ) {
            session.delete( user );
            session.getTransaction().commit();
            session.close();
            System.out.println("Successfully deleted- " + user.toString());
            return true;
        }

        session.close();
        return false;
    }


    public static User findByID(Integer id) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        User u = (User) session.load(User.class, id);

        try {
            System.out.println(u.toString());
        }catch (ObjectNotFoundException ex){
            return new User(-1);
        }

        session.close();

        return u;
    }

    public static User findByUsername( String username ){
        Session session = MySessionFactory.getSessionFactory().openSession();

        String sql = "SELECT * FROM user WHERE username='"+username+"'";

        SQLQuery query = session.createSQLQuery(sql);


        List<Object[]> list = (List<Object[]>)query.list();

        User foundUser = new User();

        for( Object[] user : list ){
            int idUser = (int)user[0];
            String userType = (String) user[1];
            String foundUsername = (String) user[2];
            String password = (String) user[3];
            String firstname = (String) user[4];
            String lastname = (String) user[5];
            String sex = (String) user[6];
            int age = (int) user[7];

            if( userType.equals("police") )
                foundUser = new User(idUser,userType, foundUsername, password, firstname, lastname, sex, age, new PoliceUser());
            else if( userType.equals("postoffice") )
                foundUser = new User(idUser,userType, foundUsername, password, firstname, lastname, sex, age, new PostOfficeUser());
            else if( userType.equals("admin") )
                foundUser = new User(idUser,userType, foundUsername, password, firstname, lastname, sex, age, new AdminUser());
        }

        session.close();

        return foundUser;
    }

    public static void deleteAll() {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        org.hibernate.Query query = session.createQuery("DELETE FROM User");
        query.executeUpdate();

        session.getTransaction().commit();
        session.close();

        System.out.println("Successfully deleted all employees.");

    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", userType='" + userType + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }

    public void create(){
        personAPI.createUser(username, password, firstName, lastName, sex, age);
    }
}
