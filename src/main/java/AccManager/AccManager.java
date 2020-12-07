package AccManager;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import DBworkee.DBworker;
import ManageExeptions.EqualAccException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import CSVWork.CSVException;
import CSVWork.CSVWorker;
import ManageExeptions.AccException;
import ManageExeptions.AuthException;

/**
 * Класс AccManager  реализует интерфейс AccountManager.
 * Класс служит для работы с аккаунтами пользователей.
 * Хранение данных аккаунтов пользователей происходит в CSV файле.
 */

public class AccManager implements AccountManager {

    DBworker dBworker = new DBworker();

    /**
     * Метод добавляет нового пользователя системы.
     * <p>
     * Если пользователь с таким набором данных уже есть в системе, выбрасывается исключение EqualAccException.
     */
    @Override
    public void addAccount(String userName, String password) throws AccException, EqualAccException, SQLException {
        if (userName == null || password == null) throw new AccException();
        if (dBworker.findAccount(userName, password)) throw new EqualAccException();
        try {
            dBworker.addAccountsDAO(userName, password);
        } catch (SQLException e) {
            throw new AccException("Ошибка добавления в систему");
        }
    }

    /**
     * Метод удаляет пользователя системы.
     * <p>
     * Удаление производится путем создания нового CSV файла, не содержащего удаляемого пользователя,
     * после чего старый файл с пользователями удаляется, а новый приобретает его имя.
     */
    @Override
    public void removeAccount(String userName, String password) throws AccException, SQLException {
        dBworker.removeAccountDAO(userName,password);
    }

    /**
     * Метод возвращает строковый список всех аккаунтов.
     */
    @Override
    public List<String> getAllAccounts() throws AccException {
        List<String> Accounts = new ArrayList<String>();
        try {
            dBworker.getAllAccDAO();
            } catch (SQLException e) {
            throw new AccException("Ошибка доступа к системе");
              }
        return Accounts;
    }

    /**
     * Метод авторизует пользователя и возвращает Token для доступа к методам менеджера депозитов.
     * Если пользователь с переданными userName и password не найдены, то выбрасывается исключение AuthException.
     * Если произошла ошибка при поиске данного пользователя в системе, выбрасывается исключение AccException.
     */
    @Override
    public String authorize(String userName, String password, Date currentTime) throws AuthException, AccException {
        try {
            if (dBworker.findAccount(userName, password)) {
                return TokenManager.getInstance().setToken(currentTime);
            }
        } catch (SQLException e) {
            throw new AccException("Ошибка доступа к системе");
        }
        throw new AuthException("Неверный логин/пароль");
    }

    /**
     * Класс представляет собой аккаунт, регистрируемый пользователем.
     * Служит для работы с CSV файлом.
     */
    protected class Account {
        private String userName;
        private String password;

        public Account(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}
