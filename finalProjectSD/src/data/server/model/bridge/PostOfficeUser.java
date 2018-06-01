package data.server.model.bridge;


import data.server.model.User;

/**
 * Created by plesha on 14-May-18.
 */
public class PostOfficeUser implements PersonAPI {

    /*  public PremiumUser(){

     }

    public PremiumUser(Subject subject){
         this.subject = subject;
         this.subject.attach(this);
     }
 */
    public void createUser(String username, String password, String firstName, String lastName, String sex, int age){
        User user = new User("postoffice", username, password, firstName, lastName, sex, age );
        User.createUserInDatabase( user );
    }

/*
    @Override
    public void update() {

        Email.sendEmail( subject.getRecieverName() , subject.getEmailTo(), subject.getShowName() );
        System.out.println( "Reciever Name: " +subject.getRecieverName() + " Email: " + subject.getEmailTo() + " Show name: " + subject.getShowName() );
    }
    */
}
