package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account login(Account credentials) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, credentials.getUsername());
            preparedStatement.setString(2, credentials.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));

                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

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
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_user_id = (int) pkeyResultSet.getInt(1);
                return new Account(generated_user_id, credentials.getUsername(), credentials.getPassword());
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
