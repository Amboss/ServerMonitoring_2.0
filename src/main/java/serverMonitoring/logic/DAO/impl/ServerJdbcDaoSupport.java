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
import serverMonitoring.logic.DAO.ServerDao;
import serverMonitoring.model.ServerEntity;
import serverMonitoring.model.serverStateEnum.ServerState;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class embodies DAO functionality for ServerEntity
 */
@Transactional
@Repository
public class ServerJdbcDaoSupport implements ServerDao {

    protected static Logger daoSupportLogger = Logger.getLogger("ServerJdbcDaoSupport");
    private SimpleJdbcInsert insertEntity;
    private JdbcTemplate jdbcTemplate;

    private String nullError = "ServerEntity entity is empty!";
    private String db_table = "server_entity";
    private String raw_list = "id, server_name, address, port, url, state, response, created, lastCheck, active";
    private String raw_list_update = "(id = ?, server_name = ?, address = ?, port = ?, url = ?, " +
            "state = ?, response = ?, created = ?, lastCheck = ?, active = ?)";
    private String raw_value = "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    //@Resource(name = "dataSource")
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertEntity = new SimpleJdbcInsert(dataSource)
                .withTableName(db_table)
                .usingGeneratedKeyColumns("id");
    }

    /**
     * Adds new Server entity with new Id assignment
     */
    @Override
    public void add(ServerEntity entity){
        assert entity != null;
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("server_name", entity.getServer_name())
                    .addValue("address", entity.getAddress())
                    .addValue("port", entity.getPort())
                    .addValue("url", entity.getUrl())
                    .addValue("state", entity.getState())
                    .addValue("response", entity.getResponse())
                    .addValue("created", entity.getCreated())
                    .addValue("lastCheck", entity.getLastCheck())
                    .addValue("active", entity.getActive());
            Number newId = insertEntity.executeAndReturnKey(parameters);
            entity.setId(newId.longValue());
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Adds group of Server entities
     */
    @Override
    public void addGroup(final List<ServerEntity> entity) {
        assert entity != null;
        String query = "INSERT INTO " + db_table + raw_list + " VALUES " + raw_value;
        try {
            List<Object[]> parameters = new ArrayList<>();
            for (ServerEntity foo : entity) {
                parameters.add(new Object[]{foo.getServer_name(),
                        foo.getAddress(),
                        foo.getPort(),
                        foo.getUrl(),
                        foo.getState(),
                        foo.getResponse(),
                        foo.getCreated(),
                        foo.getLastCheck(),
                        foo.getActive()});
            }
            this.jdbcTemplate.batchUpdate(query, parameters);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Updating existing Server entity
     */
    @Override
    public void update(ServerEntity entity) {
        assert entity != null;
        String query = "UPDATE " + db_table + " SET " + raw_list_update + " where id = " + entity.getId();
        try {
            Object[] args = {entity.getServer_name(),
                    entity.getAddress(),
                    entity.getPort(),
                    entity.getUrl(),
                    entity.getState(),
                    entity.getResponse(),
                    entity.getCreated(),
                    entity.getLastCheck(),
                    entity.getActive()};
            this.jdbcTemplate.update(query, args);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Deleting existing Server entity
     */
    @Override
    public void delete(Long entity_id) {
        assert entity_id != null;
        String query = "DELETE FROM " + db_table + " WHERE id = " + entity_id;
        try {
            this.jdbcTemplate.update(query);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Retrieves Server entity entity
     *
     * @return ServerEntity object
     */
    @Override
    public ServerEntity findById(Long entity_id) {
        assert entity_id != null;
        String query = "SELECT " + raw_list + " FROM " + db_table + " WHERE id = " + entity_id;
        try {
            return this.jdbcTemplate.queryForObject(query, new EmployeeEntityMapper());
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Retrieves all Server entity entity
     *
     * @return ServerEntity list
     */
    @Override
    public List<ServerEntity> findAll() {
        String query = "SELECT * FROM " + db_table;
        try {
            return this.jdbcTemplate.query(query, new EmployeeEntityMapper());
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    /*
    * Supporting inner class for retrieves EmployeeEntity objects
    *
    * @return EmployeeEntity object
    */
    private static final class EmployeeEntityMapper implements RowMapper<ServerEntity> {
        public ServerEntity mapRow(ResultSet rs, int rowNum) {
            try {
                ServerEntity entity = new ServerEntity();
                entity.setServer_name(rs.getString("server_name"));
                entity.setAddress(rs.getString("address"));
                entity.setPort(rs.getInt("port"));
                entity.setUrl(rs.getString("url"));
                entity.setState(ServerState.getEnumFromString(rs.getString("state")));
                entity.setResponse(rs.getString("response"));
                entity.setCreated(rs.getTimestamp("created"));
                entity.setLastCheck(rs.getTimestamp("lastCheck"));
                entity.setActive(rs.getInt("active"));
                return entity;
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
    }
}