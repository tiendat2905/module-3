package com.codegym.dao;

import com.codegym.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO<T> {
    public boolean insertUser(User user) throws SQLException;

    public User selectUser(int id);

    public List<T> selectAllUser();

    public boolean deleteUser(int id) throws SQLException;

    public boolean updateUser(User user) throws SQLException;

    public List<T> sortUserByName();

    public List<T> findByCountry(String country);
}
