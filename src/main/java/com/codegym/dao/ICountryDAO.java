package com.codegym.dao;

import com.codegym.model.User;

import java.sql.SQLException;
import java.util.List;

public interface ICountryDAO<T> {

    public void insertCountry(T country) throws SQLException;

    public User selectCountry(int id);

    public List<T> selectAllCountry();

    public boolean deleteCountry(int id) throws SQLException;

    public boolean updateCountry(T user) throws SQLException;

    public List<T> sortCountryByName();

    public List<T> findByCountry(T country);
}
