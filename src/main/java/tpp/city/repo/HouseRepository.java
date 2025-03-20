package tpp.city.repo;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import tpp.city.model.House;

@Slf4j
@Repository
public class HouseRepository {

    private final JdbcTemplate jdbcTemplate;

    public HouseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("HouseRepository initialized");
    }

    public List<House> getAllHouses() {
        log.info("Fetching all houses from database");
        String sql = "SELECT * FROM House";
        List<House> houses = jdbcTemplate.query(sql, (rs, rowNum) -> {
            House house = new House();
            house.setHouseId(rs.getInt("house_id"));
            house.setStreetId(rs.getInt("street_id"));
            house.setHouseNumber(rs.getString("house_number"));
            return house;
        });
        log.info("Retrieved {} houses", houses.size());
        return houses;
    }

    public void addHouse(String houseNumber, int streetId) {
        log.info("Adding new house '{}' to street with ID {}", houseNumber, streetId);
        String sql = "INSERT INTO House (street_id, house_number) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, streetId, houseNumber);
        log.info("House '{}' added successfully. Rows affected: {}", houseNumber, rowsAffected);
    }

    public void deleteHouse(int houseId) {
        log.info("Deleting house with ID {}", houseId);
        String sql = "DELETE FROM House WHERE house_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, houseId);
        if (rowsAffected > 0) {
            log.info("House with ID {} deleted successfully", houseId);
        } else {
            log.warn("House with ID {} not found for deletion", houseId);
        }
    }

    public void updateHouse(int houseId, String houseNumber) {
        log.info("Updating house with ID {} to new number '{}'", houseId, houseNumber);
        String sql = "UPDATE House SET house_number = ? WHERE house_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, houseNumber, houseId);
        if (rowsAffected > 0) {
            log.info("House with ID {} updated successfully", houseId);
        } else {
            log.warn("House with ID {} not found for update", houseId);
        }
    }

    public House getHouseById(int houseId) {
        log.info("Fetching house with ID {}", houseId);
        try {
            String sql = "SELECT * FROM House WHERE house_id = ?";
            House house = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                House h = new House();
                h.setHouseId(rs.getInt("house_id"));
                h.setStreetId(rs.getInt("street_id"));
                h.setHouseNumber(rs.getString("house_number"));
                return h;
            }, houseId);
            log.info("House found: {}", house);
            return house;
        } catch (EmptyResultDataAccessException e) {
            log.warn("House with ID {} not found", houseId);
            return null;
        }
    }
}
