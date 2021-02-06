package com.codegym.controller;
import com.codegym.dao.CountryDAO;
import com.codegym.dao.UserDAO;
import com.codegym.model.Country;
import com.codegym.model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    CountryDAO countryDAO = new CountryDAO();

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                try {
                    insertUser(request, response);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "edit":
                try {
                    updateUser(request, response);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "create":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                case "sort":
                    sortUserByName(request, response);
                    break;
                case "find":
                    findByCountry(request,response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<User> listUser = userDAO.selectAllUser();
        System.out.println(listUser);
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        List<Country> countryList = countryDAO.selectAllCountry();
        request.setAttribute("countries",countryList);
        dispatcher.forward(request, response);
    }
//
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        User existingUser = userDAO.selectUser(id);
        List<Country> list =  countryDAO.selectAllCountry();
        request.setAttribute("countries",list);
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);

    }
//
    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int countryId = Integer.parseInt(request.getParameter("country"));
        Country country = new Country(countryId);
        User newUser = new User(name, email, country);
        System.out.println("new user : " + newUser);
        userDAO.insertUser(newUser);
        response.sendRedirect( request.getContextPath() + "/users?action=users");
    }
//
    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int countryId = Integer.parseInt(request.getParameter("country"));
        Country country = new Country(countryId);
        User book = new User(id, name, email, country);

        userDAO.updateUser(book);
        response.sendRedirect( request.getContextPath() + "/users?action=users");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);

        List<User> listUser = userDAO.selectAllUser();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }

    private void sortUserByName(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException{
        List<User> listUser = userDAO.sortUserByName();
        System.out.println(listUser);
        request.setAttribute("listUsers", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/sort.jsp");
        dispatcher.forward(request, response);
    }

    private void findByCountry(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException{
        String country = request.getParameter("country");
        System.out.println("ok vo find");
        List<User> listUser = userDAO.findByCountry(country);
//        System.out.println("inlist");
        System.out.println(listUser);
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }
}