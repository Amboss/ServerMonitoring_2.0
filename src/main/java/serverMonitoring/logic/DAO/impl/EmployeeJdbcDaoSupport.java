package serverMonitoring.logic.DAO.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import serverMonitoring.logic.DAO.EmployeeDao;
import serverMonitoring.model.EmployeeEntity;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class embodies DAO functionality for EmployeeEntity
 */
@Transactional
@Repository
public class EmployeeJdbcDaoSupport implements EmployeeDao {

    protected static Logger daoSupportLogger = Logger.getLogger("EmployeeJdbcDaoSupport");
    private SimpleJdbcInsert insertEntity;
    private JdbcTemplate jdbcTemplate;
    private String db_table = "employee_entity";
    private String raw_list = "id, employee_name, login, password, email, created, lastLogin, active, admin";
    private String raw_list_update = "id = ?, employee_name = ?, login = ?, password = ?, " +
            "email = ?, created = ?, lastLogin = ?, active = ?, admin = ?";


    //@Resource(name = "dataSource")
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertEntity = new SimpleJdbcInsert(dataSource)
                .withTableName(db_table)
                .usingGeneratedKeyColumns("id");
    }

    /**
     * Adds new Employee entity with new Id assignment
     */
    @Override
    public void add(EmployeeEntity entity) {
        assert entity != null;
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", entity.getId())
                    .addValue("employee_name", entity.getEmployee_name())
                    .addValue("login", entity.getLogin())
                    .addValue("password", entity.getPassword())
                    .addValue("email", entity.getEmail())
                    .addValue("created", entity.getCreated())
                    .addValue("lastLogin", entity.getLastLogin())
                    .addValue("active", entity.getActive())
                    .addValue("admin", entity.getAdmin());
            if(entity.getId() == null) {
                Number newId = insertEntity.executeAndReturnKey(parameters);
                entity.setId(newId.longValue());
            } else {
                insertEntity.execute(parameters);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException();

        }
    }

    /**
     * Adds group of Employee entities
     */
    @Override
    public void addGroup(final List<EmployeeEntity> entity) {
        String query = "INSERT INTO " + db_table + " (" + raw_list + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        assert entity != null;
        try {
            List<Object[]> parameters = new ArrayList<Object[]>();
            for (EmployeeEntity foo : entity) {
                parameters.add(new Object[]{
                        foo.getId(),
                        foo.getEmployee_name(),
                        foo.getLogin(),
                        foo.getPassword(),
                        foo.getEmail(),
                        foo.getCreated(),
                        foo.getLastLogin(),
                        foo.getActive(),
                        foo.getAdmin()});

            }
            this.jdbcTemplate.batchUpdate(query, parameters);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Updating existing Employee entity
     */
    @Override
    public void update(EmployeeEntity entity) {
        assert entity.getLogin() != null;

        // updating with full list of rows
        String query = "UPDATE " + db_table + " SET " + raw_list_update + " WHERE id = ? ";
        try {
            // selecting existent entity & replacing null with existent match
            EmployeeEntity entityInDB = findByLogin(entity.getLogin());
            assert entityInDB != null;
            if (entity.getId() == null) {
                entity.setId(entityInDB.getId());
            } if (entity.getEmployee_name() == null) {
                entity.setEmployee_name(entityInDB.getEmployee_name());
            } if (entity.getLogin() == null) {
                entity.setLogin(entityInDB.getLogin());
            } if (entity.getPassword() == null) {
                entity.setPassword(entityInDB.getPassword());
            } if (entity.getEmail() == null) {
                entity.setEmail(entityInDB.getEmail());
            } if (entity.getCreated() == null) {
                entity.setCreated(entityInDB.getCreated());
            } if (entity.getLastLogin() == null) {
                entity.setLastLogin(entityInDB.getLastLogin());
            } if (entity.getActive() == null) {
                entity.setActive(entityInDB.getActive());
            } if (entity.getAdmin() == null) {
                entity.setAdmin(entityInDB.getAdmin());
            }
            // creating entity fill in arguments
            Object[] args = {entity.getId(),
                    entity.getEmployee_name(),
                    entity.getLogin(),
                    entity.getPassword(),
                    entity.getEmail(),
                    entity.getCreated(),
                    entity.getLastLogin(),
                    entity.getActive(),
                    entity.getAdmin(),
                    entity.getId()};
            this.jdbcTemplate.update(query, args);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Deleting existing Employee entity
     */
    @Override
    public void delete(Long entity_id) {
        assert entity_id != null;
        String query = "DELETE FROM " + db_table + " where id= ?";
        try {
            Object[] args = {entity_id};
            this.jdbcTemplate.update(query, args);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Retrieves EmployeeEntity entity by Id
     *
     * @return EmployeeEntity object
     */
    @Override
    public EmployeeEntity findById(Long entity_id) {
        assert entity_id != null;
        String query = "SELECT " + raw_list + " FROM " + db_table + " WHERE id= ?";
        try {
            Object[] args = {entity_id};
            return this.jdbcTemplate.queryForObject(query, args, new EmployeeEntityMapper());
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Retrieves EmployeeEntity entity by login
     *
     * @return EmployeeEntity object
     */
    @Override
    public EmployeeEntity findByLogin(String entity_login) {
        assert entity_login != null;
        String query = "SELECT " + raw_list + " FROM " + db_table + " WHERE login= ?";
        try {
            Object[] args = {entity_login};
            return this.jdbcTemplate.queryForObject(query, args, new EmployeeEntityMapper());
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    /**
     * Retrieves all EmployeeEntity entity
     *
     * @return EmployeeEntity list
     */
    @Override
    public List<EmployeeEntity> findAll() {
        try {
            String query = "SELECT * FROM " + db_table;
            return this.jdbcTemplate.query(query, new EmployeeEntityMapper());
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /*
     * Supporting inner class to retrieve EmployeeEntity objects
     *
     * @return EmployeeEntity RowMapp object
     */
    private static final class EmployeeEntityMapper implements RowMapper<EmployeeEntity> {
        public EmployeeEntity mapRow(ResultSet rs, int rowNum) {
            try {
                EmployeeEntity entity = new EmployeeEntity();
                entity.setId(rs.getLong("id"));
                entity.setEmployee_name(rs.getString("employee_name"));
                entity.setLogin(rs.getString("login"));
                entity.setPassword(rs.getString("password"));
                entity.setEmail(rs.getString("email"));
                entity.setCreated(rs.getTimestamp("created"));
                entity.setLastLogin(rs.getTimestamp("lastLogin"));
                entity.setActive(rs.getInt("active"));
                entity.setAdmin(rs.getInt("admin"));
                return entity;
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
    }
}

