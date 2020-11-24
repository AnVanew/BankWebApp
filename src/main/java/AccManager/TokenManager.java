package AccManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ManageExeptions.NoTokenExeption;

/**
 * Класс реализует управление токенами: их создание, размещение в хранилище, проверку при авторизации.
 * Класс реализует паттерн Singleton.
 */
public class TokenManager {

    private static final TokenManager singleton = new TokenManager();

    private TokenManager() {
    }

    public static TokenManager getInstance() {
        return singleton;
    }

    /**
     * Поле является хранилищем токенов, где ключом является сам токен, а значением является время его создания.
     */
    private Map<String, Date> tokenStorage = new HashMap();

    /**
     * Метод возвращает сгенерированное значение токена.
     */
    private String GenerateToken() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            str.append((int) (Math.random() * 273));
        }
        return str.toString();
    }

    /**
     * Метод возвращает токен, размещенный им в хранилище.
     */
    String setToken(Date currentTime) {
        String token = GenerateToken();
        tokenStorage.put(token, currentTime);
        return token;
    }

    /**
     * Метод проверяет время сессии токена.
     * Необходимое условие валидации токена: время существования менее 30 мин.
     * При отсутствии токена выбрасывается исключение NoTokenException.
     * Если время сессии истекло, то токен удаляется из tokenStorage.
     */
    public boolean checkToken(String token) throws NoTokenExeption {
        Date currentDate = new Date();
        Date sessionDate = tokenStorage.get(token);
        if (sessionDate == null) throw new NoTokenExeption();
        if ((currentDate.getTime() - sessionDate.getTime()) <= ((long) 30 * 60 * 1000)) return true;
        tokenStorage.remove(token);
        return false;
    }
}