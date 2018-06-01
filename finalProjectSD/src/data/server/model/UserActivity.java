package data.server.model;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import sample.factory.session.MySessionFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by plesha on 31-May-18.
 */
@Entity
@Table( name = "useractivity")
public class UserActivity implements Serializable {

    private static final long serialVersionUID = 6128016096756071380L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "iduserActivity")
    private int idUserActivity;

    @Column( name = "activity")
    private String activity;

    @Column( name = "date")
    private Date date;

    @Column( name = "user_idUser")
    private int user_idUser;

    public UserActivity(){

    }

    public UserActivity( int idUserActivity ){
        this.idUserActivity = idUserActivity;
    }

    public UserActivity(int idUserActivity, String activity, Date date, int user_idUser) {
        this.idUserActivity = idUserActivity;
        this.activity = activity;
        this.date = date;
        this.user_idUser = user_idUser;
    }

    public UserActivity(String activity, Date date, int user_idUser) {
        this.activity = activity;
        this.date = date;
        this.user_idUser = user_idUser;
    }

    public int getIdUserActivity() {
        return idUserActivity;
    }

    public void setIdUserActivity(int idUserActivity) {
        this.idUserActivity = idUserActivity;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUser_idUser() {
        return user_idUser;
    }

    public void setUser_idUser(int user_idUser) {
        this.user_idUser = user_idUser;
    }

    @Override
    public String toString() {
        return "UserActivity{" +
                "idUserActivity=" + idUserActivity +
                ", activity='" + activity + '\'' +
                ", date=" + date +
                ", user_idUser=" + user_idUser +
                '}';
    }

    public static List<UserActivity> read() {
        Session session = MySessionFactory.getSessionFactory().openSession();

        @SuppressWarnings("unchecked")
        List<UserActivity> userActivityList = session.createQuery("FROM UserActivity").list();

        session.close();

        System.out.println("Found " + userActivityList.size() + " activities");

        return userActivityList;
    }

    public static UserActivity findByID(Integer id) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        UserActivity userActivity = (UserActivity) session.load(UserActivity.class, id);

        try {
            System.out.println(userActivity.toString());
        }catch (ObjectNotFoundException ex){
            return new UserActivity(-1);
        }

        session.close();

        return userActivity;
    }

    public static void createActivityInDatabase(UserActivity userActivity) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        session.beginTransaction();
        session.save( userActivity );
        session.getTransaction().commit();

        session.close();

        System.out.println("Successfully. created: " + userActivity.toString());
    }

    public static void update(UserActivity userActivity) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        UserActivity updateActivity = (UserActivity) session.load(UserActivity.class, userActivity.getIdUserActivity() );
        updateActivity.setIdUserActivity(userActivity.getIdUserActivity());
        updateActivity.setActivity(userActivity.getActivity());
        updateActivity.setDate(userActivity.getDate());
        updateActivity.setUser_idUser(userActivity.getUser_idUser());

        session.getTransaction().commit();
        session.close();

        System.out.println("Successfully updated " + updateActivity.toString());
    }

    public static boolean delete(Integer id) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        UserActivity userActivity = findByID(id);

        if( userActivity.getIdUserActivity() != -1 ) {
            session.delete( userActivity );
            session.getTransaction().commit();
            session.close();
            System.out.println("Successfully deleted: " + userActivity.toString());
            return true;
        }

        session.close();
        return false;
    }


}
