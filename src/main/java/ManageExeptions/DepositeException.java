package ManageExeptions;

/**
 * Исключение вызванное ошибкой при работе с депозитами.
 */
public class DepositeException extends ManageExeption {

    public DepositeException() {
        super();
    }

    public DepositeException(String message) {
        System.err.println(message);
    }

}
