package cn.qinwweico.util.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zqw
 * @date 2024/1/29
 */
@Service
public final class SqlQuery {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private SqlQuery() {}

    @SuppressWarnings("unchecked")
    public <T> T queryForObject(String sql, Class<?> objType) {
        try {
            return (T) jdbcTemplate.queryForObject(sql, objType);
        } catch (EmptyResultDataAccessException ex) {
            //
        } catch (DataAccessException e) {
            throw e;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T queryForObject(String sql, Object[] args, Class<?> objType) {

        try {
            return (T) jdbcTemplate.queryForObject(sql, objType, args);
        } catch (EmptyResultDataAccessException ex) {
            //
        } catch (DataAccessException e) {
            throw e;
        }

        return null;
    }

    @SuppressWarnings("unchecked")

    public <T> List<T> queryForList(String sql) throws DataAccessException {
        return (List<T>) jdbcTemplate.queryForList(sql);

    }

    @SuppressWarnings("unchecked")

    public <T> List<T> queryForList(String sql, Object[] args) throws DataAccessException {
        return (List<T>) jdbcTemplate.queryForList(sql, args);

    }

    @SuppressWarnings("unchecked")

    public <T> List<T> queryForList(String sql, Class<?> elmType) throws DataAccessException {
        return (List<T>) jdbcTemplate.queryForList(sql, elmType);

    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryForList(String sql, Object[] args, Class<?> elmType) throws DataAccessException {
        return (List<T>) jdbcTemplate.queryForList(sql, elmType, args);

    }


    public Map<String, Object> queryForMap(String sql) {
        try {
            return jdbcTemplate.queryForMap(sql);
        } catch (EmptyResultDataAccessException ex) {
            //
        } catch (DataAccessException e) {
            throw e;
        }
        return null;

    }

    public Map<String, Object> queryForMap(String sql, Object[] args) {
        try {
            return jdbcTemplate.queryForMap(sql, args);
        } catch (EmptyResultDataAccessException ex) {
            //
        } catch (DataAccessException e) {
            throw e;
        }
        return null;
    }

    public int update(String sql) throws DataAccessException {
        return jdbcTemplate.update(sql);
    }

    public int[] batchUpdate(String sql, List<Object[]> list) throws DataAccessException {
        return jdbcTemplate.batchUpdate(sql, list);
    }

    public int update(String sql, Object[] args) throws DataAccessException {
        return jdbcTemplate.update(sql, args);
    }

    public void execute(String sql) throws DataAccessException {
        jdbcTemplate.execute(sql);
    }
}
