package ManageExeptions;

/**
 * Исключение вызванное добавлением уже существующего пользователя.
 */
public class AccException extends ManageExeption {

    public AccException() {
        super("Ошибка аутентификации");
    }

    public AccException(String message) {
        super(message);
    }

}
