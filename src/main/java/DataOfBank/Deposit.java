package DataOfBank;

import java.util.Date;
import java.util.Objects;

/**
 * Класс представляет собой депозит.
 * Базой данных всех депозитов является файл Accounts.csv.
 */
public class Deposit {
    private Client client;
    private double ammount;
    private double percent;
    private double pretermPercent;
    private int termDays;
    private Date startDate;
    private boolean withPercentCapitalization;

    public Deposit() {
    }

    public Deposit(Client client, double ammount, double percent, double pretermPercent, int termDays, Date startDate, boolean withPercentCapitalization) {
        this.client = client;
        this.ammount = ammount;
        this.percent = percent;
        this.pretermPercent = pretermPercent;
        this.termDays = termDays;
        this.startDate = startDate;
        this.withPercentCapitalization = withPercentCapitalization;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getAmmount() {
        return ammount;
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public double getPretermPercent() {
        return pretermPercent;
    }

    public void setPretermPercent(double pretermPercent) {
        this.pretermPercent = pretermPercent;
    }

    public int getTermDays() {
        return termDays;
    }

    public void setTermDays(int termDays) {
        this.termDays = termDays;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isWithPercentCapitalization() {
        return withPercentCapitalization;
    }

    public void setWithPercentCapitalization(boolean withPercentCapitalization) {
        this.withPercentCapitalization = withPercentCapitalization;
    }

    public String getFirstName() {
        return client.getFirstName();
    }

    public String getLastName() {
        return client.getLastName();
    }

    public String getPassport() {
        return client.getPassport();
    }

    /**
     * Депозиты считаются равными, если их открыл один клиент в одно время.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deposit deposit = (Deposit) o;
        if (this.getClient().equals(deposit.getClient()) &&
                this.getStartDate().getTime() / 1000 == deposit.getStartDate().getTime() / 1000
        ) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.getClient().getPassport().substring(0, 3)) * 31 +
                Integer.parseInt(this.getClient().getPassport().substring(3, 6)) * 9 +
                (int) this.getStartDate().getTime() * 13;
    }
}
