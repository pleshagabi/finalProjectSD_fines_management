package data.server.model;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import sample.factory.session.MySessionFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by plesha on 26-May-18.
 */
@Entity
@Table( name = "fine" )
public class Fine implements Serializable{

    private static final long serialVersionUID = 6128016096756071380L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "idFine" )
    private Integer idFine;

    @Column( name = "dateFineCommited" )
    private Date dateFineCommited;

    @Column( name = "crimeType" )
    private String crimeType;

    @Column( name = "price" )
    private Integer price;

    @Column( name = "paymentDeadlineDate" )
    private Date deadlineDate;

    @Column( name = "Driver_idDriver" )
    private Integer Driver_idDriver;

    @Column( name = "isPayd")
    private Byte isPayd;

    @Column( name = "points")
    private Integer points;



    public Fine(){

    }

    public Fine( int idFine ){
        this.idFine = idFine;
    }

    public Fine(Date dateFineCommited, String crimeType, Integer price, Date deadlineDate, Integer driver_idDriver, Byte isPayd, Integer points) {
        this.dateFineCommited = dateFineCommited;
        this.crimeType = crimeType;
        this.price = price;
        this.deadlineDate = deadlineDate;
        this.Driver_idDriver = driver_idDriver;
        this.isPayd = isPayd;
        this.points = points;
    }

    public Fine(int idFine, Date dateFineCommited, String crimeType, Integer price, Date deadlineDate, Integer driver_idDriver, Byte isPayd, Integer points) {
        this.idFine = idFine;
        this.dateFineCommited = dateFineCommited;
        this.crimeType = crimeType;
        this.price = price;
        this.deadlineDate = deadlineDate;
        this.Driver_idDriver = driver_idDriver;
        this.isPayd = isPayd;
        this.points = points;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdFine() {
        return idFine;
    }

    public void setIdFine(Integer idFine) {
        this.idFine = idFine;
    }

    public Date getDateFineCommited() {
        return dateFineCommited;
    }

    public void setDateFineCommited(Date dateFineCommited) {
        this.dateFineCommited = dateFineCommited;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public Integer getDriver_idDriver() {
        return Driver_idDriver;
    }

    public void setDriver_idDriver(Integer driver_idDriver) {
        Driver_idDriver = driver_idDriver;
    }

    public Byte getIsPayd() {
        return isPayd;
    }

    public void setIsPayd(Byte isPayd) {
        this.isPayd = isPayd;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Fine{" +
                "idFine=" + idFine +
                ", dateFineCommited=" + dateFineCommited +
                ", crimeType='" + crimeType + '\'' +
                ", price=" + price +
                ", deadlineDate=" + deadlineDate +
                ", Driver_idDriver=" + Driver_idDriver +
                ", isPayd=" + isPayd +
                ", points=" + points +
                '}';
    }

    public String toStringNice(){

        return   "Fine___________________________________________________\n"
                + "Fine Id: " + idFine + "\n"
                + "Date Crime Committed: " + dateFineCommited+ "\n"
                + "Crime Type: " + crimeType + "\n"
                + "Fine Price: " + price + " lei\n"
                + "Fine Payment Deadline Date: " + deadlineDate + "\n"
                + "Driver Id: " + Driver_idDriver + "\n"
                + "Is Paid: " + isPayd + "\n"
                + "Points: " + points + "\n"
                + "_____________________________________________________" + "\n";
    }

    public static void createFineInDatabase(Fine fine) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        session.beginTransaction();
        session.save( fine );
        session.getTransaction().commit();

        session.close();

    }

    public static List<Fine> read() {
        Session session = MySessionFactory.getSessionFactory().openSession();

        @SuppressWarnings("unchecked")
        List<Fine> fines = session.createQuery("FROM Fine").list();

        session.close();

        System.out.println("Found " + fines.size() + " Employees");

        return fines;
    }

    public static Fine findByID(Integer id) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        Fine fine = (Fine) session.load(Fine.class, id);

        try {
            System.out.println( fine.toString() );
        }catch (ObjectNotFoundException ex){
            return new Fine(-1);
        }

        session.close();

        return fine;
    }

    public static boolean delete(Integer id) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        Fine fine = findByID(id);

        if( fine.getIdFine() != -1 ) {

            session.delete( fine );
            session.getTransaction().commit();

            session.close();

            return true;
        }

        session.close();
        return false;
    }

   public static void update(Fine fine) {
       Session session = MySessionFactory.getSessionFactory().openSession();
       session.beginTransaction();

       Fine updateFine = (Fine) session.load(Fine.class, fine.getIdFine());
       updateFine.setDateFineCommited(fine.getDateFineCommited());
       updateFine.setCrimeType(fine.getCrimeType());
       updateFine.setPrice(fine.getPrice());
       updateFine.setDeadlineDate(fine.getDeadlineDate());
       updateFine.setIsPayd(fine.getIsPayd());
       updateFine.setDriver_idDriver(fine.getDriver_idDriver());
       updateFine.setPoints(fine.getPoints());

       session.getTransaction().commit();
       session.close();

       System.out.println("Successfully updated " + fine.toString());

   }


    public static List<Fine> findFinesByDriverId( Integer idDriver ) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        String sql = "SELECT * FROM fine WHERE Driver_idDriver='"+idDriver+"'";

        SQLQuery query = session.createSQLQuery(sql);


        List<Object[]> list = (List<Object[]>)query.list();

        List<Fine> fines = new ArrayList<Fine>();
        Fine fineFound = new Fine();

        for( Object[] driver : list ){
            Integer idFound = (Integer)driver[0];
            Date dateFound = (Date)driver[1];
            String crimeType = (String)driver[2];
            Integer price = (Integer)driver[3];
            Date deadlineDate = (Date)driver[4];
            Integer driver_idDriver = (Integer)driver[5];
            Byte isPayd = (Byte)driver[6];
            Integer points = (Integer)driver[7];

            fineFound = new Fine(idFound,dateFound,crimeType,price,deadlineDate,driver_idDriver, isPayd, points );

            fines.add( fineFound );
        }

        session.close();

        return fines;
    }


}
