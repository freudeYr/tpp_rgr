package tpp.city.repo;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import tpp.city.model.Street;

@Slf4j
@Repository
public class StreetRepository {

    private final JdbcTemplate jdbcTemplate;

    public StreetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("StreetRepository initialized");
    }

    public List<Street> getAllStreets() {
        log.info("Fetching all streets from database");
        String sql = "SELECT * FROM Street";
        List<Street> streets = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Street street = new Street();
            street.setStreetId(rs.getInt("street_id"));
            street.setCityId(rs.getInt("city_id"));
            street.setStreetName(rs.getString("street_name"));
            return street;
        });
        log.info("Retrieved {} streets", streets.size());
        return streets;
    }

    public void addStreet(String streetName, int cityId) {
        log.info("Adding new street: '{}' to city with ID {}", streetName, cityId);
        String sql = "INSERT INTO Street (city_id, street_name) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, cityId, streetName);
        log.info("Street '{}' added successfully. Rows affected: {}", streetName, rowsAffected);
    }

    public void deleteStreet(int streetId) {
        log.info("Deleting street with ID {}", streetId);
        String sql = "DELETE FROM Street WHERE street_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, streetId);
        if (rowsAffected > 0) {
            log.info("Street with ID {} deleted successfully", streetId);
        } else {
            log.warn("Street with ID {} not found for deletion", streetId);
        }
    }

    public void updateStreet(int streetId, String streetName) {
        log.info("Updating street with ID {} to new name '{}'", streetId, streetName);
        String sql = "UPDATE Street SET street_name = ? WHERE street_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, streetName, streetId);
        if (rowsAffected > 0) {
            log.info("Street with ID {} updated successfully", streetId);
        } else {
            log.warn("Street with ID {} not found for update", streetId);
        }
    }

    public Street getStreetById(int streetId) {
        log.info("Fetching street with ID {}", streetId);
        try {
            String sql = "SELECT * FROM Street WHERE street_id = ?";
            Street street = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Street s = new Street();
                s.setStreetId(rs.getInt("street_id"));
                s.setCityId(rs.getInt("city_id"));
                s.setStreetName(rs.getString("street_name"));
                return s;
            }, streetId);
            log.info("Street found: {}", street);
            return street;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Street with ID {} not found", streetId);
            return null;
        }
    }
}