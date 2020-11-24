package CSVWork;

/**
 * Исключение вызванное ошибкой при работе с CSV файлом.
 */
public class CSVException extends Exception {

    public CSVException() {
        super();
    }

    public CSVException(String message) {
        System.err.println(message);
    }
}
