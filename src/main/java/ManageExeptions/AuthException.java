package ManageExeptions;

/**
 * Исключение вызванное невыполненной авторизацией.
 */
public class AuthException extends ManageExeption {

    public AuthException() {
        super("Ошибка аутентификации");
    }

    public AuthException(String message) {
        super(message);
    }

}