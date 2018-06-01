package JUnitTests;

import data.server.model.Driver;
import data.server.model.Fine;
import data.server.model.User;
import org.junit.Test;
import sample.police.controller.PoliceLoginController;
import sample.police.controller.PolicePanelController;
import sample.postoffice.controller.PostOfficePanelController;

import java.util.logging.Level;

import static org.junit.Assert.assertEquals;

/**
 * Created by plesha on 01-Jun-18.
 */

public class MyUnitTest {
    @Test
    public void testCheckValidUserCrime() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Fine fine  = Fine.findByID( 1 );

        String result = fine.getCrimeType();

        assertEquals("depasirea vitezei legale", result);
    }

    @Test
    public void testCheckValidFinePrice() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Fine fine  = Fine.findByID( 1 );

        int result = fine.getPrice();

        assertEquals(1500, result);

    }

    @Test
    public void testCheckValidUsernameFromDatabase() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Driver driver  = Driver.findByName( "Alina Medesan" );

        String result = driver.getPhoneNumber();


        assertEquals("0755 491 927", result);

    }

    @Test
    public void testCheckValidEmailData() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Driver driver  = Driver.findByName( "Plesa Gabriel" );

        String result = driver.getEmail();

        assertEquals("plesha_gabi@yahoo.com", result);

    }

    @Test
    public void testCheckValidSex() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        User user  = User.findByUsername("anca");

        String result = user.getSex();

        assertEquals("Female", result);

    }

    @Test
    public void testCheckValidUserType() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        User user  = User.findByID(8);

        String result = user.getUserType();

        assertEquals("postoffice", result);

    }
}
