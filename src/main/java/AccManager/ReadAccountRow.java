package AccManager;

/**
 * Класс представляет собой прочитанную строку аккаунта из CVS файла.
 * Является вспомогательным классом.
 */
public class ReadAccountRow {

    private String userName;
    private String password;

    public ReadAccountRow() {
    }

    public ReadAccountRow(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
