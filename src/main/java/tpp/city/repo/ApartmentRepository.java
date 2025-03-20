package tpp.city.repo;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import tpp.city.model.Apartment;

@Slf4j
@Repository
public class ApartmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public ApartmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("ApartmentRepository initialized");
    }

    public List<Apartment> getAllApartments() {
        log.info("Fetching all apartments from database");
        String sql = "SELECT * FROM Apartment";
        List<Apartment> apartments = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Apartment apartment = new Apartment();
            apartment.setApartmentId(rs.getInt("apartment_id"));
            apartment.setHouseId(rs.getInt("house_id"));
            apartment.setApartmentNumber(rs.getInt("apartment_number"));
            return apartment;
        });
        log.info("Retrieved {} apartments", apartments.size());
        return apartments;
    }

    public void addApartment(int apartmentNumber, int houseId) {
        log.info("Adding new apartment '{}' to house with ID {}", apartmentNumber, houseId);
        String sql = "INSERT INTO Apartment (house_id, apartment_number) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, houseId, apartmentNumber);
        log.info("Apartment '{}' added successfully. Rows affected: {}", apartmentNumber, rowsAffected);
    }

    public void deleteApartment(int apartmentId) {
        log.info("Deleting apartment with ID {}", apartmentId);
        String sql = "DELETE FROM Apartment WHERE apartment_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, apartmentId);
        if (rowsAffected > 0) {
            log.info("Apartment with ID {} deleted successfully", apartmentId);
        } else {
            log.warn("Apartment with ID {} not found for deletion", apartmentId);
        }
    }

    public void updateApartment(int apartmentId, int apartmentNumber) {
        log.info("Updating apartment with ID {} to new number '{}'", apartmentId, apartmentNumber);
        String sql = "UPDATE Apartment SET apartment_number = ? WHERE apartment_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, apartmentNumber, apartmentId);
        if (rowsAffected > 0) {
            log.info("Apartment with ID {} updated successfully", apartmentId);
        } else {
            log.warn("Apartment with ID {} not found for update", apartmentId);
        }
    }

    public Apartment getApartmentById(int apartmentId) {
        log.info("Fetching apartment with ID {}", apartmentId);
        try {
            String sql = "SELECT * FROM Apartment WHERE apartment_id = ?";
            Apartment apartment = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Apartment a = new Apartment();
                a.setApartmentId(rs.getInt("apartment_id"));
                a.setHouseId(rs.getInt("house_id"));
                a.setApartmentNumber(rs.getInt("apartment_number"));
                return a;
            }, apartmentId);
            log.info("Apartment found: {}", apartment);
            return apartment;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Apartment with ID {} not found", apartmentId);
            return null;
        }
    }
}
