package ManageExeptions;

public class EqualAccException extends ManageExeption {

    public EqualAccException() {
        super("Пользователь с таким username уже зарегестрирован.");
    }

    public EqualAccException(String message) {
        super(message);
    }
}
