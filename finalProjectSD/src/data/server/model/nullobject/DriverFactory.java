package data.server.model.nullobject;

import data.server.model.Driver;

import java.util.List;

/**
 * Created by plesha on 01-Jun-18.
 */
public class DriverFactory {
    public static final List<Driver> driverList = Driver.read();

    public static AbstractDriver getDriver(int id ){
        for( Driver driver : driverList ){
            if( driver.getIdDriver() == id )
                return new Driver( id, driver.getName(), driver.getAge(), driver.getAddress(), driver.getEmail(), driver.getPhoneNumber() );
        }
        return new NullDriver();
    }

}
