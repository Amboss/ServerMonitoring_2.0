package serverMonitoring.logic.DAO;


import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import serverMonitoring.model.EmployeeEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit test for the {@link EmployeeJdbcDaoTest} class.
 * Testing DAO query to Data Base.
 * ApplicationContext will be loaded from "classpath:/application-context.xml"
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class EmployeeJdbcDaoTest extends AbstractJUnit4SpringContextTests {

//    protected static Logger employeeLogger = Logger.getLogger("EmployeeServiceImpl");
    private static ShaPasswordEncoder passwordEncoder;
    private static Timestamp timestamp;
    private EmployeeDao employeeDao;


    @Autowired
    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @BeforeClass
    public static void initiate() {
        passwordEncoder = new ShaPasswordEncoder(256);
        Date date = new Date();
        timestamp = new Timestamp(date.getTime());
    }

    /**
     * Testing registration off new EmployeeEntity to Data Base
     */
    @Test
    public void testAdd() {
        EmployeeEntity entity = new EmployeeEntity();
        String pass = passwordEncoder.encodePassword("testpass", null);
        entity.setEmployee_name("Test_DAO_add_Employee_Name");
        entity.setLogin("testDAOUser");
        entity.setPassword(pass);
        entity.setEmail("test_email@mail.com");
        entity.setCreated(timestamp);
        entity.setLastLogin(timestamp);
        entity.setActive(1);
        entity.setAdmin(0);
        employeeDao.add(entity);

        EmployeeEntity entity2 = employeeDao.findByLogin("testDAOUser");
        assertNotNull("failure - Employee entity2 must not be null", entity);
        assertEquals("failure - entity_name should be same", "Test_DAO_add_Employee_Name", entity2.getEmployee_name());
        assertEquals("failure - login should be same", "testDAOUser", entity2.getLogin());
        assertEquals("failure - password should be same", pass, entity2.getPassword());
        assertEquals("failure - password should be same", "test_email@mail.com", entity2.getEmail());
        assertEquals("failure - isActive should be same", (Object) 1, entity2.getActive());
        assertEquals("failure - isAdmin should be same", (Object) 0, entity2.getAdmin());
    }

    /**
     * Testing registration group off new EmployeeEntity to Data Base
     */

    @Test
    public void testAddGroup() {
        List<EmployeeEntity> entityList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            EmployeeEntity entity = new EmployeeEntity();
            String pass = passwordEncoder.encodePassword("testpass", null);
            entity.setEmployee_name("Test_DAO_addGroup_Employee_Name" + i);
            entity.setLogin("testDAOUser" + i);
            entity.setPassword(pass);
            entity.setEmail("test_email@mail.com");
            entity.setCreated(timestamp);
            entity.setLastLogin(timestamp);
            entity.setActive(1);
            entity.setAdmin(0);
            entityList.add(entity);
        }
        employeeDao.addGroup(entityList);
        List <EmployeeEntity> entity2 = employeeDao.findAll();
        assertNotNull(entity2);
    }


    /**
     * Testing selection off EmployeeEntity from Data Base
     */
    @Test
        public void testFindById() {
        EmployeeEntity entity = new EmployeeEntity();
        String pass = passwordEncoder.encodePassword("testlalala", null);
        entity.setEmployee_name("Test_DAO_findById_Employee_Name");
        entity.setLogin("testDAOUserByID");
        entity.setPassword(pass);
        entity.setEmail("test_email@mail.com");
        entity.setCreated(timestamp);
        entity.setLastLogin(timestamp);
        entity.setActive(1);
        entity.setAdmin(0);
        employeeDao.add(entity);

        EmployeeEntity entity2 = employeeDao.findByLogin("testDAOUserByID");
        assertNotNull("failure - Employee entity2 must not be null", entity2);
        Long id = entity2.getId();
        EmployeeEntity entity3 = employeeDao.findById(id);
        assertNotNull("failure - Employee entity2 must not be null", entity3);

        assertEquals("failure - entity_name should be same", "Test_DAO_findById_Employee_Name", entity3.getEmployee_name());
        assertEquals("failure - login should be same", "testDAOUserByID", entity3.getLogin());
        assertEquals("failure - password should be same", pass, entity3.getPassword());
        assertEquals("failure - password should be same", "test_email@mail.com", entity3.getEmail());
        assertEquals("failure - isActive should be same", (Object) 1, entity3.getActive());
        assertEquals("failure - isAdmin should be same", (Object) 0, entity3.getAdmin());
    }

    /**
     * Testing selection off all EmployeeEntity from Data Base
     */
    @Test
    public void testFindAll() {
        List<EmployeeEntity> entitiesList = employeeDao.findAll();
        assertNotNull("failure - Employee entitiesList must not be null", entitiesList);
    }

    /**
     * Testing update of existing EmployeeEntity in Data Base
     */
    @Test
    public void testUpdate() {
        // selecting existent entity
        EmployeeEntity entity = new EmployeeEntity();
        entity.setLogin("testDAOUser");
        entity.setEmail("new_test_email@mail.com");
        entity.setEmployee_name("DAO_Test_Update");
        entity.setPassword(passwordEncoder.encodePassword("33333", null));
        entity.setActive(0);
        employeeDao.update(entity);

        // selection and asserting
        EmployeeEntity entity2 = employeeDao.findByLogin(entity.getLogin());
        assertNotNull(entity2);
        assertEquals("failure - login =)) should be same", "testDAOUser", entity2.getLogin());
        assertEquals("failure - password should be same", "new_test_email@mail.com", entity2.getEmail());
        assertEquals("failure - name should be same", "DAO_Test_Update", entity2.getEmployee_name());
        assertEquals("failure - isActive should be same", (Object) 0, entity2.getActive());

    }

//    /**
//     * Testing termination off EmployeeEntity from Data Base
//     */
//    @Test
//    public void testDelete() {
//        EmployeeEntity entity = new EmployeeEntity();
//        entity.setLogin("testDAOUser");
//        entity = employeeDao.findByLogin(entity.getLogin());
//        employeeDao.delete(entity.getId());
//        EmployeeEntity entity2 = employeeDao.findByLogin("testDAOUser");
//        assertNull("the testDAOUser is not empty", entity2);
//
//        entity.setLogin("testDAOUser0");
//        entity = employeeDao.findByLogin(entity.getLogin());
//        employeeDao.delete(entity.getId());
//        entity2 = employeeDao.findByLogin("testDAOUser0");
//        assertNull("the testDAOUser0 is not empty", entity2);
//
//        entity.setLogin("testDAOUser1");
//        entity = employeeDao.findByLogin(entity.getLogin());
//        employeeDao.delete(entity.getId());
//        entity2 = employeeDao.findByLogin("testDAOUser1");
//        assertNull("the testDAOUser1 is not empty", entity2);
//
//        entity.setLogin("testDAOUser2");
//        entity = employeeDao.findByLogin(entity.getLogin());
//        employeeDao.delete(entity.getId());
//        entity2 = employeeDao.findByLogin("testDAOUser2");
//        assertNull("the testDAOUser2 is not empty", entity2);
//
//        entity.setLogin("testDAOUserByID");
//        entity = employeeDao.findByLogin(entity.getLogin());
//        employeeDao.delete(entity.getId());
//        entity2 = employeeDao.findByLogin("testDAOUserByID");
//        assertNull("the testDAOUserByID is not empty", entity2);
//    }
}
