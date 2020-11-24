package ManageExeptions;

/**
 * Искючение вызванное отсутствие токена с хранилище.
 */
public class NoTokenExeption extends ManageExeption {

    public NoTokenExeption() {
        super("Токен не найден");
    }

    public NoTokenExeption(String message) {
        super(message);
    }

}
