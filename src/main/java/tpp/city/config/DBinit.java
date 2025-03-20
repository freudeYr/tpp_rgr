package tpp.city.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBinit implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DBinit(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        if (isDatabaseEmpty()) {
            initializeDatabase();
        }
    }

    private boolean isDatabaseEmpty() {
        Integer cityCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM City", Integer.class);
        return cityCount == null || cityCount == 0;
    }

    private void initializeDatabase() {
        jdbcTemplate.execute("INSERT INTO City (city_id, city_name) VALUES " +
                "(1, 'Kyiv'), (2, 'Lviv'), (3, 'Odesa');");

        jdbcTemplate.execute("INSERT INTO Street (street_id, city_id, street_name) VALUES " +
                "(1, 1, 'Khreshchatyk'), " +
                "(2, 1, 'Shevchenka'), " +
                "(3, 2, 'Halytska Square'), " +
                "(4, 3, 'Deribasivska');");

        jdbcTemplate.execute("INSERT INTO House (house_id, street_id, house_number) VALUES " +
                "(1, 1, '12'), " +
                "(2, 1, '24A'), " +
                "(3, 2, '7'), " +
                "(4, 3, '5B');");

        jdbcTemplate.execute("INSERT INTO Apartment (apartment_id, house_id, apartment_number) VALUES " +
                "(1, 1, 101), " +
                "(2, 1, 102), " +
                "(3, 2, 201), " +
                "(4, 3, 302), " +
                "(5, 4, 401);");
    }
}