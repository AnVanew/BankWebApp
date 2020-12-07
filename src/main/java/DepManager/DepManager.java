package DepManager;

import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;

import DBworkee.DBworker;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import CSVWork.CSVException;
import CSVWork.CSVWorker;
import DataOfBank.*;
import AccManager.TokenManager;
import ManageExeptions.DepositeException;
import ManageExeptions.NoTokenExeption;

/**
 * Класс DepManager реализует в себе интерфейс DepositManager.
 * Класс служит для работы с данными по депозитам, хранящихся в файе CSV.
 * Помимо методов из имплементируемого интерфейса также реализованы вспомогательные методы
 * для нахождения дохода по вкладам, поиска прошедших дней между двумя датами и
 * созданию депозита из прочитанной строки файла CSV.
 * <p>
 * В каждом реализованном методе имплементируемого интерфейса, кроме getAllDeposits, реализована проверка
 * токена на актуальность статическим методом checkToken класса TokenManager
 * (время жизни токена не более 30 мин).
 */

public class DepManager implements DepositManager {

    DBworker dBworker = new DBworker();

    /**
     * Метод добавляет в систему информацию о новом вкладе.
     *
     * @param pretermPercent процент при досрочном изъятии вклада.
     */
    @Override
    public Deposit addDeposit(Client client, double ammount, double percent, double pretermPercent, int termDays, Date startDate, boolean withPercentCapitalization, String token) throws DepositeException {
        try {
            if (!TokenManager.getInstance().checkToken(token)) throw new DepositeException();
        } catch (NoTokenExeption e) {
            System.out.println("Операция сейчас не доступна. Выполните повторную авторизацию");
            throw new DepositeException();
        }
        Deposit deposit = new Deposit(client, ammount, percent, pretermPercent, termDays, startDate, withPercentCapitalization);
        try {
            dBworker.addDepositDAO(deposit);
        } catch (SQLException e) {
            System.out.println("Ошибка создания депозита. Повторите попытку. ");
            throw new DepositeException();
        }
        return deposit;
    }

    /**
     * Метод возвращает список вкладов клиента.
     */
    @Override
    public List<Deposit> getClientDeposits(Client client, String token) throws DepositeException {
        try {
            if (!TokenManager.getInstance().checkToken(token)) throw new DepositeException();
        } catch (NoTokenExeption e) {
            System.out.println("Операция сейчас не доступна. Выполните повторную авторизацию");
            throw new DepositeException();
        }
        List<Deposit> deposits = new ArrayList<Deposit>();
        try {
            dBworker.getClientDepositsDAO(client);
        } catch (SQLException e) {
            throw new DepositeException();
        }
        return deposits;
    }

    /**
     * Метод возвращает список всех вкладов принятых банком.
     */
    @Override
    public List<Deposit> getAllDeposits() throws DepositeException {
        List<Deposit> deposits = new ArrayList<Deposit>();
        try {
           dBworker.getAllDepDAO();
        } catch (SQLException e) {
            throw new DepositeException();
        }
        return deposits;
    }

    /**
     * Метод возвращает текущий доход по вкладу.
     * <p>
     * Следует учитывать, что вклад может быть с выплатой прибыли по процентам
     * или же процент по вкладу может добавляться к сумме вклада.
     * За это отвечает поле withPercentCapitalization класса Deposit.
     */
    @Override
    public double getEarnings(Deposit deposit, Date currentDate, String token) throws DepositeException {
        try {
            if (!TokenManager.getInstance().checkToken(token)) throw new DepositeException();
        } catch (NoTokenExeption e) {
            System.out.println("Операция сейчас не доступна. Выполните повторную авторизацию");
            throw new DepositeException();
        }
        if (!deposit.isWithPercentCapitalization()) {
            return GetSimpleSum(currentDate, deposit, deposit.getPercent());
        } else {
            return GetProgressSum(currentDate, deposit, deposit.getPercent());
        }
    }

    /**
     * Метод удаляет запись о вкладе и возвращает сумму к выплате в кассе.
     * Если вклад закрывается досрочно, то сумма к выплате рассчитывается исходя
     * из процента при досрочном изъятии.
     */
    @Override
    public double removeDeposit(Deposit deposit, Date closeDate, String token) throws DepositeException, SQLException {
        try {
            if (!TokenManager.getInstance().checkToken(token)) throw new DepositeException();
        } catch (NoTokenExeption e) {
            System.out.println("Операция сейчас не доступна. Выполните повторную авторизацию");
            throw new DepositeException();
        }


        dBworker.removeDepositDAO(deposit);
        /*File tempFile = new File("temp.csv");
        List<Deposit> newAllDep = getAllDeposits();
        boolean flag = newAllDep.remove(deposit);
        if (!flag) throw new DepositeException("Депозит отсутствует");
        try {
            tempFile.createNewFile();
            for (Deposit dep : newAllDep) CSVWorker.PrintInCVS(tempFile, dep, headerDeposit);
        } catch (IOException | CSVException e) {
            System.out.println("Ошибка записи в систему");
            tempFile.delete();
            throw new DepositeException();
        }
        file.delete();
        tempFile.renameTo(file);
*/


        if (DaysGone(deposit.getStartDate(), closeDate) < deposit.getTermDays()) {
            System.out.println("Срок не вышел, прошло " + DaysGone(deposit.getStartDate(), closeDate) + " дней, а срок " + deposit.getTermDays());
            if (deposit.isWithPercentCapitalization()) {
                return Math.ceil((deposit.getAmmount() + GetProgressSum(closeDate, deposit, deposit.getPretermPercent())) * 100) / 100;
            } else
                return Math.ceil((deposit.getAmmount() + GetSimpleSum(closeDate, deposit, deposit.getPretermPercent())) * 100) / 100;
        } else {
            System.out.println("Срок вышел");
            if (deposit.isWithPercentCapitalization()) {
                return Math.ceil((deposit.getAmmount() + GetProgressSum(closeDate, deposit, deposit.getPercent())) * 100) / 100;
            } else
                return Math.ceil((deposit.getAmmount() + GetSimpleSum(closeDate, deposit, deposit.getPercent())) * 100) / 100;
        }
    }

    /**
     * Метод возвращает прибыль по вкладу при условии withPercentCapitalization is FALSE.
     * <p>
     * Если процент по вкладу выплачивается, то прибыль до определенного момента можно вычислить по формуле
     * Sп=(Sв*%*Nд)/Nг, где
     * Sп – размер компенсации по вкладу
     * Sв – размер депозита
     * % — размер процентной ставки
     * Nд – количество дней , за которые банком начисляются проценты
     * Nг – количество дней в календарном году
     * <p>
     * Схема начислений:
     * Процентная ставка является годовой.
     * Процент начинает начисляться с момента открытия, то есть процент за первый год будет не целым.
     * Процент за год на момент запроса так же не будет целым.
     * <p>
     * Чтобы посчитать прибыль по вкладу при неокончании срока
     * необходимо узнать прибыли за первым и последний годы вклада
     * и, если имеются, за промежуточные года.
     */
    private double GetSimpleSum(Date currentDate, Deposit deposit, double percent) {
        GregorianCalendar CurrentDate = new GregorianCalendar();
        GregorianCalendar DepositeDate = new GregorianCalendar();
        CurrentDate.setTime(currentDate);
        DepositeDate.setTime(deposit.getStartDate());
        int FYDays;
        int AllFYDays;
        int AllLYDays;
        int LYDays = CurrentDate.get(Calendar.DAY_OF_YEAR);
        double Summary = 0;

        /*
        От того, являются ли первый и последний года вклада високосными, зависят:
        общее количество дней в первом и последних годах, количество дней вкладад первого года
         */

        if (CurrentDate.get(Calendar.YEAR) == DepositeDate.get(Calendar.YEAR)) {
            int DaysDeposit = DaysGone(deposit.getStartDate(), currentDate);
            if (DepositeDate.isLeapYear(DepositeDate.get(Calendar.YEAR))) {
                Summary += (deposit.getAmmount() * DaysDeposit * deposit.getPercent()) / 366;
                return Math.ceil(Summary * 100) / 100;
            } else {
                Summary += (deposit.getAmmount() * DaysDeposit * deposit.getPercent()) / 365;
                return Math.ceil(Summary * 100) / 100;
            }
        }
        if (CurrentDate.isLeapYear(CurrentDate.get(Calendar.YEAR))) AllLYDays = 366;
        else AllLYDays = 365;
        if (DepositeDate.isLeapYear(DepositeDate.get(Calendar.YEAR))) {
            FYDays = 366 - DepositeDate.get(Calendar.DAY_OF_YEAR) + 1;
            AllFYDays = 366;
        } else {
            FYDays = 365 - DepositeDate.get(Calendar.DAY_OF_YEAR) + 1;
            AllFYDays = 365;
        }
        Summary = deposit.getAmmount() * percent * (CurrentDate.get(Calendar.YEAR) - DepositeDate.get(Calendar.YEAR) - 1 + (float) FYDays / AllFYDays + (float) LYDays / AllLYDays);
        return Math.ceil(Summary * 100) / 100;
    }

    /**
     * Метод возвращает прибыль по вкладу при условии withPercentCapitalization is TRUE.
     * <p>
     * Если процент по вклау суммируется, то прибыль вычисляется по формуле
     * Sп=Sв*(1+%)п-Sв, где
     * Sп – сумма дохода по депозиту
     * Sв – размер вклада
     * n – число периодов капитализации
     * % — размер процентной ставки в периоде капитализации
     * <p>
     * Схема начислений:
     * Каждый прошедший период капитализации процент добавляется к вкладу.
     * Если вклад изымается до завершения расчетного периода, то сумма остается той же,
     * что и была на момент завершения прошлого расчетного периода.
     * Процент percent является годовым.
     * Периодизацию будем считать ежемесячной.
     * <p>
     * Для вычисления дохода используется эффективная процентная ставка, вычисляемая по формуле
     * ( (1+ Процентная Ставка/12)^T -1) * 12/T, где
     * Т - срок размещения вклада в месяцах.
     * При помощи данной ставки можно посчитать прибыль с использованием метода
     * нахождения прибыли без капитализации процентов.
     */
    private double GetProgressSum(Date currentDate, Deposit deposit, double percent) {
        GregorianCalendar CurrentDate = new GregorianCalendar();
        GregorianCalendar DepositeDate = new GregorianCalendar();
        CurrentDate.setTime(currentDate);
        DepositeDate.setTime(deposit.getStartDate());
        int mounthGone = (int) ((CurrentDate.getTimeInMillis() - DepositeDate.getTimeInMillis()) / ((long) 1000 * 60 * 60 * 24 * 30));
        percent = (Math.pow((1 + percent / 12), mounthGone) - 1) * 12 / mounthGone; //вычисление эффективной процентной ставки
        return GetSimpleSum(currentDate, deposit, percent);
    }

    /**
     * Метод возвращает количество дней, прошедших между двумя датами.
     */
    private int DaysGone(Date start, Date finish) {
        return (int) ((finish.getTime() - start.getTime()) / ((long) 1000 * 60 * 60 * 24));
    }

    /**
     * Метод возвращает депозит из прочитанной строки.
     * Необходимо преобразовать строку даты из CVS файла в объект типа Date.
     * Для этого парсим строку в соответствующем формате.
     */
    private Deposit CreareRowDeposit(ReadDepositeRow row) throws CSVException {
        Date parseDate;
        try {
            parseDate = new SimpleDateFormat("E MMM d hh:mm:ss z yyyy").parse(row.getStartDate());
        } catch (ParseException e) {
            throw new CSVException();
        }
        return new Deposit(
                new Client(row.getFirstName(), row.getLastName(), row.getPassport()),   //инизилизация поля client
                Double.parseDouble(row.getAmmount()),   //инизилизация поля amount
                Double.parseDouble(row.getPercent()),   //инизилизация поля percent
                Double.parseDouble(row.getPretermPercent()),    //инизилизация поля pretermPercent
                Integer.parseInt(row.getTermDays()),    //инизилизация поля termDays
                parseDate,  //инизилизация поля startDate
                Boolean.parseBoolean(row.getWithPercentCapitalization())   //инизилизация поля withPercentCapitalization
        );
    }
}
