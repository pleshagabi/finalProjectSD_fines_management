package data.server.model;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import sample.factory.session.MySessionFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by plesha on 29-May-18.
 */
@Entity
@Table(name = "drivinglicense")
public class DrivingLicense implements Serializable {

    private static final long serialVersionUID = 6128016096756071380L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "idDrivingLicense")
    private int idDrivingLicense;

    @Column(name = "name")
    private String name;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "placeOfBirth")
    private String placeOfBirth;

    @Column(name = "issuedBy")
    private String issuedBy;

    @Column(name = "issuedDate")
    private Date issuedDate;

    @Column(name = "validUntil")
    private Date validUntil;

    @Column(name = "licenseCode")
    private String licenseCode;

    @Column(name = "categories")
    private String categories;

    @Column(name = "licensePoints")
    private Integer licensePoints;

    @Column(name = "driver_idDriver")
    private Integer driver_idDriver;

    @Column(name = "isSuspended")
    private String isSuspended;

    public DrivingLicense(){

    }

    public DrivingLicense( int idDrivingLicense ){
        this.idDrivingLicense = idDrivingLicense;
    }

    public DrivingLicense(int idDrivingLicense, String name, Date dateOfBirth, String placeOfBirth, String issuedBy, Date issuedDate, Date validUntil, String licenseCode, String categories, Integer licensePoints, Integer driver_idDriver, String isSuspended) {
        this.idDrivingLicense = idDrivingLicense;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.issuedBy = issuedBy;
        this.issuedDate = issuedDate;
        this.validUntil = validUntil;
        this.licenseCode = licenseCode;
        this.categories = categories;
        this.licensePoints = licensePoints;
        this.driver_idDriver = driver_idDriver;
        this.isSuspended = isSuspended;
    }

    public DrivingLicense(String name, Date dateOfBirth, String placeOfBirth, String issuedBy, Date issuedDate, Date validUntil, String licenseCode, String categories, Integer licensePoints, Integer driver_idDriver, String isSuspended) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.issuedBy = issuedBy;
        this.issuedDate = issuedDate;
        this.validUntil = validUntil;
        this.licenseCode = licenseCode;
        this.categories = categories;
        this.licensePoints = licensePoints;
        this.driver_idDriver = driver_idDriver;
        this.isSuspended = isSuspended;
    }


    public int getIdDrivingLicense() {
        return idDrivingLicense;
    }

    public void setIdDrivingLicense(Integer idDrivingLicense) {
        this.idDrivingLicense = idDrivingLicense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Integer getLicensePoints() {
        return licensePoints;
    }

    public void setLicensePoints(Integer licensePoints) {
        this.licensePoints = licensePoints;
    }

    public Integer getDriver_idDriver() {
        return driver_idDriver;
    }

    public void setDriver_idDriver(Integer driver_idDriver) {
        this.driver_idDriver = driver_idDriver;
    }

    public String getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(String isSuspended) {
        this.isSuspended = isSuspended;
    }

    @Override
    public String toString() {
        return "DrivingLicense{" +
                "idDrivingLicense=" + idDrivingLicense +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", issuedBy='" + issuedBy + '\'' +
                ", issuedDate=" + issuedDate +
                ", validUntil=" + validUntil +
                ", licenseCode='" + licenseCode + '\'' +
                ", categories='" + categories + '\'' +
                ", licensePoints=" + licensePoints +
                ", driver_idDriver=" + driver_idDriver +
                ", isSuspended='" + isSuspended + '\'' +
                '}';
    }

    // ------------------------------------------------------------------ HIBERANTE operations

    public static void create(DrivingLicense drivingLicense) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        session.beginTransaction();
        session.save( drivingLicense );
        session.getTransaction().commit();
        session.close();

        System.out.println("Successfully created: " + drivingLicense.toString());
    }

    public static List<DrivingLicense> read() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Session session = MySessionFactory.getSessionFactory().openSession();

        @SuppressWarnings("unchecked")
        List<DrivingLicense> drivingLicenseList = session.createQuery("FROM DrivingLicense").list();

        session.close();

        System.out.println("Found " + drivingLicenseList.size() + " licenses");

        return drivingLicenseList;
    }

    public static DrivingLicense findByID(Integer id) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        DrivingLicense drivingLicense = (DrivingLicense) session.load(DrivingLicense.class, id);

        try {
            System.out.println(drivingLicense.toString());
        }catch (ObjectNotFoundException ex){
            return new DrivingLicense(-1);
        }

        session.close();

        return drivingLicense;
    }

    public static DrivingLicense findByName(String name) {

        Session session = MySessionFactory.getSessionFactory().openSession();

        String sql = "SELECT * FROM drivinglicense WHERE name='"+name+"'";

        SQLQuery query = session.createSQLQuery(sql);

        List<Object[]> list = (List<Object[]>)query.list();

        DrivingLicense foundDrivingLicense = new DrivingLicense();

        for( Object[] license : list ){

            Integer id = (Integer) license[0];
            String nameFound = (String) license[1];
            Date birthDate = (Date) license[2];
            String placeOfBirth = (String) license[3];
            String issuedBy = (String)license[4];
            Date issuedDate = (Date)license[5];
            Date validUntil = (Date)license[6];
            String licenseCode = (String)license[7];
            String categories = (String)license[8];
            Integer points = (Integer) license[9];
            Integer driverId = (Integer) license[10];
            String suspended = (String)license[11];

            foundDrivingLicense = new DrivingLicense( id, nameFound, birthDate, placeOfBirth, issuedBy, issuedDate, validUntil,
                                                        licenseCode, categories, points, driverId, suspended);
        }

        session.close();

        return foundDrivingLicense;
    }

    public static void update(DrivingLicense oldLicense) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        DrivingLicense updateLicense = (DrivingLicense) session.load(DrivingLicense.class, oldLicense.getIdDrivingLicense());
        System.out.println("Successfully updated " + oldLicense.toString());
        updateLicense.setName(oldLicense.getName());
        updateLicense.setDateOfBirth(oldLicense.getDateOfBirth());
        updateLicense.setPlaceOfBirth(oldLicense.getPlaceOfBirth());
        updateLicense.setIssuedBy(oldLicense.getIssuedBy());
        updateLicense.setIssuedDate(oldLicense.getIssuedDate());
        updateLicense.setValidUntil(oldLicense.getValidUntil());
        updateLicense.setLicenseCode(oldLicense.getLicenseCode());
        updateLicense.setCategories(oldLicense.getCategories());
        updateLicense.setLicensePoints(oldLicense.getLicensePoints());
        updateLicense.setDriver_idDriver(oldLicense.getDriver_idDriver());
        updateLicense.setIsSuspended(oldLicense.getIsSuspended());

        session.getTransaction().commit();
        session.close();

        System.out.println("Successfully updated " + oldLicense.toString());
    }

    public static boolean delete(Integer id) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        DrivingLicense drivingLicense = findByID(id);

        if( drivingLicense.getIdDrivingLicense() != -1 ) {
            session.delete( drivingLicense );
            session.getTransaction().commit();
            session.close();
            System.out.println("Successfully deleted:  " + drivingLicense.toString());
            return true;
        }
        session.close();
        return false;
    }
}
