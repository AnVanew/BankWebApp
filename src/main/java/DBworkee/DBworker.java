package DBworkee;

import AccManager.TokenManager;
import CSVWork.CSVException;
import CSVWork.CSVWorker;
import DataOfBank.Client;
import DataOfBank.Deposit;
import ManageExeptions.AccException;
import ManageExeptions.AuthException;
import ManageExeptions.DepositeException;
import ManageExeptions.EqualAccException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBworker {
    Connection connection;
    private String URL = "jdbc:postgresql://localhost:5433/bank";
    private String user = "postgres";
    private String password = "1709dada99";

    public Connection getConnection () throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, user, password);
        } catch (SQLException e) {
            System.out.println("Sorry, don`t connect");
            throw new SQLException();
        }
        return connection;
    }

    private void CloseConnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("NO CLOSE!");
        }
    }

    public List<Deposit> getAllDepDAO() throws SQLException {
        List <Deposit> allDep= new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM deposits;");
            while (resultSet.next())
                allDep.add(new Deposit(new Client(resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("passport")),
                        resultSet.getDouble("ammount"), resultSet.getDouble("percent"), resultSet.getDouble("preter_percent"), resultSet.getInt("term_days"),
                        resultSet.getDate("start_date"), resultSet.getBoolean("with_percent_capitalization")));
        }catch (SQLException e){
            throw new SQLException();
        }
        finally {
            CloseConnect();
        }
        return allDep;
    }

    public void addDepositDAO(Deposit deposit) throws SQLException {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO deposits (passport, first_name, last_name, ammount, percent, preterm_percent, term_days, start_date, with_percent_capitalization) VALUES (?,?,?,?,?,?,?,?,?)");
            statement.setString(1, deposit.getPassport());
            statement.setString(2, deposit.getFirstName());
            statement.setString(3, deposit.getLastName());
            statement.setDouble(4, deposit.getAmmount());
            statement.setDouble(5, deposit.getPercent());
            statement.setDouble(6, deposit.getPretermPercent());
            statement.setInt(7, deposit.getTermDays());
            java.sql.Date date = new java.sql.Date(deposit.getStartDate().getTime());
            statement.setDate(8, date);
            statement.setBoolean(9, deposit.isWithPercentCapitalization());
            statement.executeUpdate();
        }catch (SQLException e){
            throw new SQLException();
        }
        finally {
            CloseConnect();
        }
    }

    public void addAccountsDAO(String username, String password) throws SQLException, EqualAccException {
        if (findAccount(username, password)) throw new EqualAccException();
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO accounts (username, password) VALUES (?, ?)");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        }catch (SQLException e){
            throw new SQLException();
        }
        finally {
            CloseConnect();
        }
    }

    public List<String> getAllAccDAO() throws SQLException {
        List <String> allAcc= new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM accounts");
            while (resultSet.next())
                allAcc.add(resultSet.getString("username") + "," + resultSet.getString("password"));
        }catch (SQLException e){
            throw new SQLException();
        }
        finally {
            CloseConnect();
        }
        return allAcc;
    }

    public List<Deposit> getClientDepositsDAO(Client client) throws SQLException {
        List <Deposit> allDep= new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM deposits WHERE passport = '" + client.getPassport() + "'");
            while (resultSet.next())
                allDep.add(new Deposit(new Client(resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("passport")),
                        resultSet.getDouble("ammount"), resultSet.getDouble("percent"), resultSet.getDouble("preterm_percent"), resultSet.getInt("term_days"),
                        resultSet.getDate("start_date"), resultSet.getBoolean("with_percent_capitalization")));
        }catch (SQLException e){
            throw new SQLException();
        }
        finally {
            CloseConnect();
        }
        return allDep;
    }

    public String authorize (String userName, String password, java.util.Date currentTime) throws AuthException, AccException, SQLException {
        ResultSet resultSet = null;
        try {
            Statement statement = getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM accounts WHERE username = '" + userName + "' AND password = '" + password + "'");
        }catch (SQLException e){
            throw new SQLException();
        }
        finally {
            CloseConnect();
        }
        if (resultSet.next()) return TokenManager.getInstance().setToken(currentTime);  //settoken private
        else throw new AuthException("Неверный логин/пароль");
    }

    public void removeDepositDAO(Deposit deposit) throws SQLException, DepositeException {
        int resultSet = 0;
        try {
            Statement statement = getConnection().createStatement();
            java.sql.Date date = new java.sql.Date(deposit.getStartDate().getTime());
            resultSet = statement.executeUpdate("DELETE FROM deposits WHERE start_date ='" + date + "'" + "AND passport = '" + deposit.getPassport() + "'");
        }catch (SQLException e){
            throw new SQLException();
        }
        finally {
            CloseConnect();
        }
        if (resultSet==0) throw new DepositeException("Депозит отсутствует");
    }

    public boolean findAccount(String userName, String password) throws SQLException {
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM accounts WHERE username = '" + userName + "' AND password ='" + password + "'");
        }catch (SQLException e){
            throw new SQLException();
        }
        finally {
            CloseConnect();
        }
        return resultSet.next();
    }

    public void removeAccountDAO(String userName, String password) throws SQLException, AccException {
        int resultSet = 0;
        try {
            Statement statement = getConnection().createStatement();
            resultSet = statement.executeUpdate("DELETE FROM accounts WHERE username = '" + userName + "' AND password ='" + password + "'");
        }catch (SQLException e){
            throw new SQLException();
        }
        finally {
            CloseConnect();
        }
        CloseConnect();
        if (resultSet==0) throw new AccException("Аккаунт отсутствует");
    }



}
