package com.example.finalproject.databaseChecker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DatabaseChecker {
    @Autowired
    private DataSource dataSource;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @PostConstruct
    public void checkIfAllTablesExists() {

        if (!doesProductTableExist()) {
            createProductTable();
        }
        if (!doesOrderTableExist()) {
            createOrderTable();
        }
        if (!doesOrderHasProductTableExist()) {
            createOrderHasProductTable();
        }

    }

    private boolean doesProductTableExist() {
        System.out.println("HERE");
        Pattern pattern = Pattern.compile(".*\\/([^\\?]+)\\?.*");
        Matcher matcher = pattern.matcher(databaseUrl);
        String databaseName = matcher.find() ? matcher.group(1) : "";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("USE " + databaseName);
        String sql = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = ? AND table_name = 'product')";
        boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, databaseName);
        return result;
    }

    private boolean doesOrderTableExist () {
        Pattern pattern = Pattern.compile(".*\\/([^\\?]+)\\?.*");
        Matcher matcher = pattern.matcher(databaseUrl);
        String databaseName = matcher.find() ? matcher.group(1) : "";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("USE " + databaseName);
        String sql = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = ? AND table_name = 'store_order')";
        boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, databaseName);
        return result;
    }

    private boolean doesOrderHasProductTableExist () {
        Pattern pattern = Pattern.compile(".*\\/([^\\?]+)\\?.*");
        Matcher matcher = pattern.matcher(databaseUrl);
        String databaseName = matcher.find() ? matcher.group(1) : "";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("USE " + databaseName);
        String sql = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = ? AND table_name = 'order_has_product')";
        boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, databaseName);
        return result;
    }

    private void createProductTable() {
        try (Connection connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword)) {
            Pattern pattern = Pattern.compile(".*\\/([^\\?]+)\\?.*");
            Matcher matcher = pattern.matcher(databaseUrl);
            String databaseName = matcher.find() ? matcher.group(1) : "";
            List<String> sqlStatements = Arrays.asList(
                    "USE " + databaseName,
                    "CREATE TABLE product (pid INTEGER NOT NULL PRIMARY KEY, product_name VARCHAR(255) NOT NULL, price INTEGER NOT NULL, description VARCHAR(255) NOT NULL)",
                    "INSERT INTO product (pid, product_name, price, description) VALUES (1, 'apple watch', 350, 'good for sport, nice quality')",
                    "INSERT INTO product (pid, product_name, price, description) VALUES (2, 'iphone',1400, 'iphone 14 512 GB Pro Max')",
                    "INSERT INTO product (pid, product_name, price, description) VALUES (3, 'macbook', 2200, 'macbook pro 1TB 15 inch')",
                    "INSERT INTO product (pid, product_name, price, description) VALUES (4, 'airpod', 300, 'airpod noise cancellation')",
                    "INSERT INTO product (pid, product_name, price, description) VALUES (5, 'iMac', 3500, 'best for everything')"
            );
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            for (String sql : sqlStatements) {
                jdbcTemplate.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating database", e);
        }
    }

    private void createOrderTable() {
        try (Connection connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword)) {
            Pattern pattern = Pattern.compile(".*\\/([^\\?]+)\\?.*");
            Matcher matcher = pattern.matcher(databaseUrl);
            String databaseName = matcher.find() ? matcher.group(1) : "";
            List<String> sqlStatements = Arrays.asList(
                    "USE " + databaseName,
                    "CREATE TABLE store_order (oid INT NOT NULL AUTO_INCREMENT, is_completed BOOLEAN NOT NULL, total_price INTEGER NOT NULL, PRIMARY KEY (oid))"
            );
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            for (String sql : sqlStatements) {
                jdbcTemplate.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating database", e);
        }
    }

    private void createOrderHasProductTable() {
        try (Connection connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword)) {
            Pattern pattern = Pattern.compile(".*\\/([^\\?]+)\\?.*");
            Matcher matcher = pattern.matcher(databaseUrl);
            String databaseName = matcher.find() ? matcher.group(1) : "";
            List<String> sqlStatements = Arrays.asList(
                    "USE " + databaseName,
                    "CREATE TABLE order_has_product (oid INT NOT NULL, pid INT NOT NULL, product_count INTEGER NOT NULL, PRIMARY KEY (oid, pid), FOREIGN KEY (oid) REFERENCES store_order(oid), FOREIGN KEY (pid) REFERENCES product(pid))"
            );
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            for (String sql : sqlStatements) {
                jdbcTemplate.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating database", e);
        }
    }
}