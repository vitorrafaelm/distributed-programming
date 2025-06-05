package org.example.first_unit.data_center.database.dao;

import org.example.first_unit.data_center.database.ConnectionJDBC;
import org.example.first_unit.data_center.database.entities.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

public class WeatherDao implements BaseDao<Weather> {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherDao.class.getSimpleName());

    private final Connection connection;

    public WeatherDao() throws SQLException {
        this.connection = new ConnectionJDBC().getConnection();
    }

    public Weather insert(Weather weather) {
        LOG.info("Inserindo: {}", weather);
        String sql = "INSERT INTO weather (id,weather_data) VALUES (?,?);";
        try {
            LOG.info(weather.toString());

            String id_random = UUID.randomUUID().toString();

            PreparedStatement pst = this.connection.prepareStatement(sql);
            pst.setString(1, id_random);
            pst.setString(2, weather.getWeatherData());
            pst.execute();

            String sqlSelect = "select * from weather where id=?;";
            PreparedStatement pstSelect = this.connection.prepareStatement(sqlSelect);

            pstSelect.setString(1, id_random);
            ResultSet rs = pstSelect.executeQuery();

            if (rs.next()) {
                weather.setId(rs.getString("id"));
                weather.setWeatherData(rs.getString("weather_data"));

                return weather;
            }

            return null;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(Weather weather) {
        String sql = "DELETE FROM weather WHERE id=?;";
        try {
            PreparedStatement pst = this.connection.prepareStatement(sql);
            pst.setString(1, weather.getId());
            pst.execute();

            return true;

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Weather weather) {
        String sql = "UPDATE service_orders SET weather_data=? WHERE id=?";
        try {
            PreparedStatement pst = this.connection.prepareStatement(sql);
            pst.setString(1, weather.getWeatherData());
            pst.setString(2, weather.getId());
            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public Weather findById(Weather weather) {
        String sql = "SELECT * FROM service_orders WHERE id=?;";
        try {
            PreparedStatement pst = this.connection.prepareStatement(sql);
            pst.setString(1, weather.getId());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                weather.setWeatherData(rs.getString("weather_data"));
            }

            return weather;

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return weather;
        }
    }

    public ResultSet findAll() {
        String sql = "SELECT * FROM weather;";
        try {
            PreparedStatement pst = this.connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
            return null;
        }
    }

    public Integer getRegistersQuantity() {
        String sql = "SELECT count(*) as data_quantity FROM weather;";
        try {
            PreparedStatement pst = this.connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt("data_quantity");
        } catch (SQLException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
            return null;
        }
    }
}
