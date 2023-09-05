package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    /**
     * User registrations
     * @param credentials, an Account object
     * @return new account object if registration is successful otherwise returns null
     */
    public Account register(Account credentials) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, credentials.getUsername());
            preparedStatement.setString(2, credentials.getPassword());

            /*
             * CHECK IF
             * username is not blank
             * password is at least 4 characters long
             */
            if (credentials.getUsername().length()>0 && credentials.getPassword().length()>3)
                preparedStatement.executeUpdate();
            
            ResultSet resultSetWPKey = preparedStatement.getGeneratedKeys();

            if(resultSetWPKey.next()){ // If registration is successful
                int genKey_user_id = resultSetWPKey.getInt(1);
                return new Account(genKey_user_id, credentials.getUsername(), credentials.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // If registration failed
    }

    /**
     * User Login
     * @param credentials, an Account object
     * @return an account object if login is successful otherwise returns null
     */
    public Account login(Account credentials) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, credentials.getUsername());
            preparedStatement.setString(2, credentials.getPassword());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) { // If login is successful
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));

                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // If login failed
    }

}
