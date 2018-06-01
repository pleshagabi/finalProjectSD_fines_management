package data.server.model;

import data.server.model.nullobject.AbstractDriver;
import data.server.model.nullobject.DriverFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import sample.factory.session.MySessionFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by plesha on 26-May-18.
 */
@Entity
@Table(name="driver")
public class Driver extends AbstractDriver implements Serializable {

    private static final long serialVersionUID = 6128016096756071380L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDriver")
    private int idDriver;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phoneNumber;


    public Driver(){

    }

    public Driver( int idDriver ){
        this.idDriver = idDriver;
    }


    public Driver(String name, int age, String address, String email, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Driver(int idDriver, String name, int age, String address, String email, String phoneNumber) {
        this.idDriver = idDriver;
        this.name = name;
        this.age = age;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void setIdDriver(int idDriver) {
        this.idDriver = idDriver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "Driver{" +
                "idDriver=" + idDriver +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public int getIdDriver() {
        return idDriver;
    }

    @Override
    public boolean isNil() {
        return false;
    }

    // ------------------------------------------------------------------ HIBERANTE operations
    public static void createDriverInDatabase(Driver driver) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        session.beginTransaction();
        session.save( driver );
        session.getTransaction().commit();

        session.close();

        System.out.println("Successfully created " + driver.toString());
    }

    public static List<Driver> read() {
        Session session = MySessionFactory.getSessionFactory().openSession();

        @SuppressWarnings("unchecked")
        List<Driver> drivers = session.createQuery("FROM Driver").list();

        session.close();

        System.out.println("Found " + drivers.size() + " Employees");

        return drivers;
    }

/*
    public static Driver findByID(Integer id) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        Driver driver = (Driver) session.load(Driver.class, id);

        try {
            System.out.println( driver.toString() );
        }catch (ObjectNotFoundException ex){
            return new Driver(-1);
        }

        session.close();

        return driver;
    }
    */

    public static Driver findByName(String name) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        String sql = "SELECT * FROM driver WHERE name='"+name+"'";

        SQLQuery query = session.createSQLQuery(sql);


        List<Object[]> list = (List<Object[]>)query.list();

        Driver driverFound = new Driver();
        for( Object[] driver : list ){
            Integer idFound = (Integer)driver[0];
            String nameFound = (String)driver[1];
            Integer ageFound = (Integer)driver[2];
            String address = (String)driver[3];
            String email = (String)driver[4];
            String phone = (String)driver[5];

             driverFound = new Driver( idFound,nameFound,ageFound,address,email,phone );
        }

        session.close();

        return driverFound;
    }

    public static void update(Driver driver) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        Driver updateDriver = (Driver) session.load(Driver.class, driver.getIdDriver());
        updateDriver.setName(driver.getName());
        updateDriver.setAge(driver.getAge());
        updateDriver.setAddress(driver.getAddress());
        updateDriver.setEmail(driver.getEmail());
        updateDriver.setPhoneNumber(driver.getPhoneNumber());

        session.getTransaction().commit();
        session.close();

        System.out.println("Successfully updated " + driver.toString());
    }

    public static boolean delete(Integer id) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        //Driver driver = findByID(id);
        AbstractDriver driver = DriverFactory.getDriver( id );

        if( driver.getIdDriver() != -1 ) {
            session.delete( driver );
            session.getTransaction().commit();
            session.close();
            System.out.println("Successfully deleted " + driver.toString());
            return true;
        }

        session.close();
        return false;
    }

}
