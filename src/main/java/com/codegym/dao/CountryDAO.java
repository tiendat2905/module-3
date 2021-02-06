package com.codegym.dao;

import com.codegym.model.Country;
import com.codegym.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO implements ICountryDAO<Country> {
    public static final String SELECT_ALL_COUNTRY = "select * from countries;";
    private static final String INSERT_COUNTRY_SQL = "INSERT INTO users (countryCode, name) VALUES (?, ?);";


    @Override
    public void insertCountry(Country country) throws SQLException {
        try (
                Connection con = DaoUtils.getConnection();
                PreparedStatement statement = con.prepareStatement(INSERT_COUNTRY_SQL);
        ) {
            statement.setString(1, country.getCountryCode());
            statement.setString(2, country.getName());
//            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public User selectCountry(int id) {
        return null;
    }

    public List<Country> selectAllCountry() {
        List<Country> countries = new ArrayList<>();
        try (
                Connection con = DaoUtils.getConnection();
                PreparedStatement statement = con.prepareStatement(SELECT_ALL_COUNTRY);
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String countryCode = resultSet.getString("countryCode");
                countries.add(new Country(id, name, countryCode));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countries;
    }

    @Override
    public boolean deleteCountry(int id) throws SQLException {
        return false;
    }

    @Override
    public boolean updateCountry(Country user) throws SQLException {
        return false;
    }

    @Override
    public List<Country> sortCountryByName() {
        return null;
    }

    @Override
    public List<Country> findByCountry(Country country) {
        List<Country> countries = selectAllCountry();
        List<Country> result = new ArrayList<>();
        String countryName = country.getName().toLowerCase();
        for (Country ct : countries) {
            if (ct.getName().toLowerCase().contains(countryName)) {
                result.add(ct);
            }
        }
        return result;
    }


}
