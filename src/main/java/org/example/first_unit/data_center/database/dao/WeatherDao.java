package org.example.first_unit.data_center.database.dao;

import org.example.first_unit.data_center.database.ConnectionJDBC;
import org.example.first_unit.data_center.database.entities.Weather;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class WeatherDao implements BaseDao<Weather> {
    Connection connection;

    public WeatherDao() {
        try {
            this.connection = new ConnectionJDBC().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Weather insert (Weather weather) {
        String sql = "INSERT INTO weather (id,weather_data) VALUES (?,?);";
        try {
            LocalDateTime now = LocalDateTime.now();

            System.out.println(weather.toString());

            String id_random = UUID.randomUUID().toString();

            PreparedStatement pst = this.connection.prepareStatement(sql);
            pst.setString(1, id_random);
            pst.setString(2, weather.getWeatherData());
            pst.execute();

            String sqlSelect = "select * from weather where id=?;";
            PreparedStatement pstSelect = this.connection.prepareStatement(sqlSelect);

            pstSelect.setString(1, id_random);
            ResultSet rs = pstSelect.executeQuery();

            if(rs.next()) {
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
            pst.setString(2, weather.getId() );
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
            if(rs.next()) {
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
