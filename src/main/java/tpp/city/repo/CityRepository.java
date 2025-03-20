package tpp.city.repo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import tpp.city.model.City;

@Slf4j
@Repository
public class CityRepository {

    private final JdbcTemplate jdbcTemplate;

    public CityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("CityRepository has been initialized");
    }

    public List<City> getAllCities() {
        log.info("Getting all cities");
        List<City> cities = jdbcTemplate.query("SELECT * FROM City",
            (rs, rowNum) -> new City(rs.getInt("city_id"), rs.getString("city_name")));
        log.info("Found {} cities", cities.size());
        return cities;
    }

    public int addCity(String name) {
        log.info("Adding city: {}", name);
        int rowsAffected = jdbcTemplate.update("INSERT INTO City (city_name) VALUES (?)", name);
        log.info("City {} added. Rows affected: {}", name, rowsAffected);
        return rowsAffected;
    }

    public int updateCity(int id, String name) {
        log.info("Updating city with ID {}: new name = {}", id, name);
        int rowsAffected = jdbcTemplate.update("UPDATE City SET city_name = ? WHERE city_id = ?", name, id);
        log.info("City with ID {} updated. Rows affected: {}", id, rowsAffected);
        return rowsAffected;
    }

    public int deleteCity(int id) {
        log.info("Deleting city with ID {}", id);
        int rowsAffected = jdbcTemplate.update("DELETE FROM City WHERE city_id = ?", id);
        log.info("City with ID {} deleted. Rows affected: {}", id, rowsAffected);
        return rowsAffected;
    }
}
