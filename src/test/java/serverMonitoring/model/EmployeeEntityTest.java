package serverMonitoring.model;

import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test for the {@link EmployeeEntity} class.
 */
@Transactional
public class EmployeeEntityTest {

    EmployeeEntity entity = new EmployeeEntity();
    Date date = new Date();
    ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);
    String pass = passwordEncoder.encodePassword("12345", null);
    Timestamp timestamp = new Timestamp(date.getTime());

    @Test
    public void testSetEmployeeFiller() {
        entity.setId(2l);
        entity.setEmployee_name("Default_FirstName &LastName");
        entity.setLogin("Default_login");
        entity.setPassword(pass);
        entity.setEmail("default_email@mail.com");
        entity.setCreated(timestamp);
        entity.setLastLogin(timestamp);
        entity.setActive(1);
        entity.setAdmin(0);

        assertEquals("failure - id should be same", 1l, (Object) entity.getId());
        assertEquals("failure - entity_name should be same", "Default_FirstName &LastName",
                                                                    entity.getEmployee_name());
        assertEquals("failure - login should be same", "Default_login", entity.getLogin());
        assertEquals("failure - password should be same", pass, entity.getPassword());
        assertEquals("failure - created should be same", timestamp, entity.getCreated());
        assertEquals("failure - lastLogin should be same", timestamp, entity.getLastLogin());
        assertEquals("failure - isActive should be same", 1, (Object) entity.getActive());
        assertEquals("failure - isAdmin should be same", 0, (Object) entity.getAdmin());
    }
}
