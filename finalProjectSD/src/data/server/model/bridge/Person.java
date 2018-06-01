package data.server.model.bridge;

/**
 * Created by plesha on 14-May-18.
 */
public abstract class Person {
    protected PersonAPI personAPI;

    protected Person( PersonAPI personAPI ){
        this.personAPI = personAPI;
    }

    public abstract void create();

    public Person(){
    }


}
