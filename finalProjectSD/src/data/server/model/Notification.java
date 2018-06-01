package data.server.model;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import sample.factory.session.MySessionFactory;

import javax.persistence.*;
import java.util.List;

/**
 * Created by plesha on 30-May-18.
 */
@Entity
@Table( name = "notification")
public class Notification {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column( name = "idNotification" )
    private int idNotification;

    @Column( name = "driver_idDriver")
    private int driver_idDriver;

    @Column( name = "fine_idFine")
    private int fine_idFine;

    public Notification(){

    }

    public Notification( int id ){
        this.idNotification = id;
    }

    public Notification(int idNotification, int driver_idDriver, int fine_idFine) {
        this.idNotification = idNotification;
        this.driver_idDriver = driver_idDriver;
        this.fine_idFine = fine_idFine;
    }


    public Notification(int driver_idDriver, int fine_idFine) {
        this.driver_idDriver = driver_idDriver;
        this.fine_idFine = fine_idFine;
    }

    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    public int getDriver_idDriver() {
        return driver_idDriver;
    }

    public void setDriver_idDriver(int driver_idDriver) {
        this.driver_idDriver = driver_idDriver;
    }

    public int getFine_idFine() {
        return fine_idFine;
    }

    public void setFine_idFine(int fine_idFine) {
        this.fine_idFine = fine_idFine;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "idNotification=" + idNotification +
                ", driver_idDriver=" + driver_idDriver +
                ", fine_idFine=" + fine_idFine +
                '}';
    }


    public static Notification findByID(Integer id) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        Notification driver = (Notification) session.load(Notification.class, id);

        try {
            System.out.println( driver.toString() );
        }catch (ObjectNotFoundException ex){
            return new Notification(-1);
        }

        session.close();

        return driver;
    }

    public static List<Notification> read() {
        Session session = MySessionFactory.getSessionFactory().openSession();

        @SuppressWarnings("unchecked")
        List<Notification> notificationList = session.createQuery("FROM Notification").list();

        session.close();

      //  System.out.println("Found " + notificationList.size() + " notifications");

        return notificationList;
    }


    public static void create(Notification notification) {

        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        session.save( notification );
        session.getTransaction().commit();
        session.close();
        System.out.println("Notification: " + notification.toString() + " created");

    }

    public static int getNrOfNotifications(){

        Session session = MySessionFactory.getSessionFactory().openSession();

        String sql = "SELECT count(*) FROM notification";
        SQLQuery query = session.createSQLQuery(sql);

        List list = query.list();

        int result = Integer.parseInt( list.get(0).toString() );

        session.close();

        return  result;
    }

    public static Notification findByFineId( int id ) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        String sql = "SELECT * FROM notification WHERE fine_idFine='"+id+"'";

        SQLQuery query = session.createSQLQuery(sql);


        List<Object[]> list = (List<Object[]>)query.list();

        Notification foundNotification = new Notification();

        for( Object[] driver : list ){
            Integer idNotification = (Integer)driver[0];
            Integer idFoundFine = (Integer)driver[1];
            Integer idFoundDriver = (Integer)driver[2];

            foundNotification = new Notification(idNotification, idFoundFine, idFoundDriver );
        }

        session.close();

        return foundNotification;
    }

    public static boolean delete(Integer id) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        Notification notificationFound = findByID(id);

        if( notificationFound.getIdNotification() != -1 ) {
            session.delete( notificationFound );
            session.getTransaction().commit();
            session.close();
            System.out.println("Successfully deleted  :" + notificationFound.toString());
            return true;
        }

        session.close();
        return false;
    }

}

