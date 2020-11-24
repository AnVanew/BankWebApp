package ManageExeptions;

/**
 * Родительский класс всех исключений, связанных с ошибками при работе
 * менеджера аккаунтов или менеджера депозитов.
 */
public abstract class ManageExeption extends Exception {

    ManageExeption() {
        super("Ошибка менеджмента");
    }

    ManageExeption(String message) {
        System.err.println(message);
    }

}





