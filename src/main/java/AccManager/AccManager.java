package AccManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

    /**
     * Поле содержит в себе файл, в котором хранятся данные аккаунтов клиентов.
     */
    private File file = new File("C:\\Users\\naic infa\\Desktop\\prog\\Java\\BankWebApp\\Accounts.csv");

    /**
     * Поле содержит в себе заголовки, по которым записаны поля в файле CSV.
     */
    private String[] AccountHeader = new String[]{"userName", "password"};

    /**
     * Метод добавляет нового пользователя системы.
     * <p>
     * Если пользователь с таким набором данных уже есть в системе, выбрасывается исключение EqualAccException.
     */
    @Override
    public void addAccount(String userName, String password) throws AccException, EqualAccException {
        if (userName == null || password == null) throw new AccException();
        if (getAllAccounts().contains(userName + "," + password)) throw new EqualAccException();
        Account newAcc = new Account(userName, password);
        try {
            CSVWorker.PrintInCVS(file, newAcc, AccountHeader);
        } catch (CSVException e) {
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
    public void removeAccount(String userName, String password) throws AccException {
        boolean flag = false;
        File tempFile = new File("temp.csv");
        List<String> newAllAcc = getAllAccounts();
        Iterator<String> accIterator = newAllAcc.iterator();
        while (accIterator.hasNext()) {
            String nextAcc = accIterator.next();
            if (nextAcc.equals(userName + "," + password)) {
                accIterator.remove();
                flag = true;
            }
        }
        if (!flag) throw new AccException("Ошибка удаления");  //Если удаление не произошло, генерируется исключение
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            for (String acc : newAllAcc) {
                bw.write(acc);
                bw.write("\n");
            }
        } catch (IOException e) {
            throw new AccException("Ошибка удаления");
        }
        file.delete();
        tempFile.renameTo(file);
    }

    /**
     * Метод возвращает строковый список всех аккаунтов.
     */
    @Override
    public List<String> getAllAccounts() throws AccException {
        List<String> Accounts = new ArrayList<String>();
        try (ICsvBeanReader reader = new CsvBeanReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE)) {
            ReadAccountRow row;
            while ((row = reader.read(ReadAccountRow.class, AccountHeader)) != null) {
                Accounts.add(row.getUserName() + "," + row.getPassword());
            }
        } catch (IOException e) {
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
        try (BufferedReader bw = new BufferedReader(new FileReader(file))) {
            String AccountRow;
            while ((AccountRow = bw.readLine()) != null) {
                if (AccountRow.equals(userName + "," + password)) {
                    return TokenManager.getInstance().setToken(currentTime);
                }
            }
        } catch (IOException e) {
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
