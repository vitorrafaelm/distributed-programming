package org.example.first_unit.data_center.database.services;

import org.example.first_unit.data_center.database.dao.BaseDao;
import org.example.first_unit.data_center.database.dao.WeatherDao;
import org.example.first_unit.data_center.database.entities.Weather;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WeatherService {
    BaseDao<Weather> dao;

    public WeatherService() {
        try {
            this.dao = new WeatherDao();
        } catch (final SQLException e) {
            throw new RuntimeException("Error initializing WeatherService", e);
        }
    }

    public Weather add(final Weather dto) {
        return dao.insert(dto);
    }

    public List<Weather> list() {
        final List<Weather> weathers = new ArrayList<>();
        final var rs = dao.findAll();

        try {
            while (rs.next()) {
                final var weather = new Weather();
                weather.setId(rs.getString("id"));
                weather.setWeatherData(rs.getString("weather_data"));

                weathers.add(weather);
            }
            return weathers;
        } catch (final SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean update(final Weather weather) throws SQLException {
        return false;
    }

    public boolean delete(final Weather weather) {
        return false;
    }

    public Weather search(final Weather weather) {
        if (!Objects.equals(weather.getId(), "")) {
            return dao.findById(weather);
        } else
            return null;
    }

    public int getRegistersQuantity() {
        return dao.getRegistersQuantity();
    }
}
