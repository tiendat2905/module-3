package com.codegym.dao;

import com.codegym.model.Country;
import com.codegym.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDAO implements IUserDAO<User> {

    private static final String SELECT_ALL_USER = "select users.*, countries.name as country from users inner join countries on users.countryID = countries.id order by users.id";
    private static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, countryID) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,email,countryID from users where id =?";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, countryID =? where id = ?;";
    private static final String SORT_USERS_BY_NAME = "SELECT * FROM demo.users ORDER BY name";

    @Override
    public boolean insertUser(User user) throws SQLException {
        boolean affect = false;
//        System.out.println(INSERT_USERS_SQL);
        try (
                Connection con = DaoUtils.getConnection();
                PreparedStatement statement = con.prepareStatement(INSERT_USERS_SQL);
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setInt(3, user.getCountry().getId());

            affect = statement.executeUpdate() > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return affect;
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        try (
                Connection con = DaoUtils.getConnection();
                PreparedStatement statement = con.prepareStatement(SELECT_USER_BY_ID);
        ) {
            statement.setInt(1, id);
            System.out.println(statement);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                int countryID = rs.getInt("countryID");
                Country country = new Country(countryID);
                user = new User(id, name, email, country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> selectAllUser() {
        List<User> users = new ArrayList<>();
        try (
                Connection con = DaoUtils.getConnection();
                PreparedStatement statement = con.prepareStatement(SELECT_ALL_USER);
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                Country country1 = new Country(country);
                users.add(new User(id, name, email, country1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (
                Connection con = DaoUtils.getConnection();
                PreparedStatement statement = con.prepareStatement(DELETE_USERS_SQL);
        ) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (
                Connection con = DaoUtils.getConnection();
                PreparedStatement statement = con.prepareStatement(UPDATE_USERS_SQL);
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setInt(3, user.getCountry().getId());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public List<User> sortUserByName() {
        List<User> users = new ArrayList<>();
        try (
                Connection con = DaoUtils.getConnection();
                PreparedStatement statement = con.prepareStatement(SORT_USERS_BY_NAME);
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                int countryId = resultSet.getInt("countryID");
                Country country = new Country(countryId);
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> findByCountry(String country) {
        List<User> users = selectAllUser();
        List<User> result = new ArrayList<>();
        country = country.toLowerCase();
        for (User user: users){
            if (user.getCountry().getName().toLowerCase().contains(country)){
                result.add(user);
            }
        }
        return result;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable throwable : ex) {
            if (throwable instanceof SQLException) {
                throwable.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) throwable).getSQLState());
                System.err.println("Error Code: " + ((SQLException) throwable).getErrorCode());
                System.err.println("Message: " + throwable.getMessage());
                Throwable t = ex.getCause();

                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
