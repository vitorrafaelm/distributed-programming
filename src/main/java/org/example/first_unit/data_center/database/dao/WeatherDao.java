package org.example.first_unit.data_center.database.dao;

import org.example.first_unit.data_center.database.ConnectionJDBC;
import org.example.first_unit.data_center.database.entities.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class WeatherDao implements BaseDao<Weather> {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherDao.class.getSimpleName());

    private final Connection connection;

    public WeatherDao() throws SQLException {
        this.connection = ConnectionJDBC.getInstance().getConnection();
    }

    public Weather insert(final Weather weather) {
        LOG.info("Inserindo: {}", weather);
        final var sql = "INSERT INTO weather (id,weather_data) VALUES (?,?);";

        LOG.info("{}", weather);
        final var id_random = UUID.randomUUID().toString();

        try (final var pst = this.connection.prepareStatement(sql)) {
            pst.setString(1, id_random);
            pst.setString(2, weather.getWeatherData());
            pst.execute();

            final var search = new Weather();
            search.setId(id_random);
            return this.findById(search);
        } catch (final SQLException e) {
            LOG.error("", e);
            return null;
        }
    }

    public boolean delete(final Weather weather) {
        final var sql = "DELETE FROM weather WHERE id=?;";

        try (final var pst = this.connection.prepareStatement(sql)) {
            pst.setString(1, weather.getId());
            pst.execute();

            return true;
        } catch (final SQLException e) {
            LOG.error("", e);
            return false;
        }
    }

    public boolean update(final Weather weather) {
        final var sql = "UPDATE service_orders SET weather_data=? WHERE id=?";

        try (final var pst = this.connection.prepareStatement(sql)) {
            pst.setString(1, weather.getWeatherData());
            pst.setString(2, weather.getId());
            pst.executeUpdate();
            return true;
        } catch (final SQLException e) {
            LOG.error("", e);
            return false;
        }
    }

    public Weather findById(final Weather weather) {
        final var sql = "SELECT * FROM weather WHERE id=?;";

        try (final var pst = this.connection.prepareStatement(sql)) {
            pst.setString(1, weather.getId());
            final var rs = pst.executeQuery();

            if (rs.next()) {
                weather.setWeatherData(rs.getString("weather_data"));
            }

            return weather;
        } catch (final SQLException e) {
            LOG.error("", e);
            return weather;
        }
    }

    public List<Weather> findAll() {
        final var sql = "SELECT * FROM weather;";

        try (final var pst = this.connection.prepareStatement(sql)) {
            final var resultSet = pst.executeQuery();
            final List<Weather> all = new ArrayList<>();

            while (resultSet.next()) {
                final var weather = new Weather()
                        .setId(resultSet.getString("id"))
                        .setWeatherData(resultSet.getString("weather_data"));

                all.add(weather);
            }

            return all;
        } catch (final SQLException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Integer getRegistersQuantity() {
        final var sql = "SELECT count(*) as data_quantity FROM weather;";

        try (final var pst = this.connection.prepareStatement(sql)) {
            final var rs = pst.executeQuery();
            rs.next();
            return rs.getInt("data_quantity");
        } catch (final SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
