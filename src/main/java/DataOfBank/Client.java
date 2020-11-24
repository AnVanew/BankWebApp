package DataOfBank;

import java.util.Objects;

/*
В классе клиента предопределены поля и методы,
которые обязательно есть у каждого клиента банка.
 */
public class Client {

    private String firstName;
    private String lastName;
    private String passport;

    Client() {
    }

    public Client(String FirstName, String LastName, String Passport) {
        this.firstName = FirstName;
        this.lastName = LastName;
        this.passport = Passport;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    @Override
    public String toString() {
        return ("\n" + "Имя: " + getFirstName() + " " + "Фамилия: "
                + getLastName() + " " + "Серия,номер паспорта: " + getPassport());
    }

    /**
     * Клиенты считаются равными, если имеют одинаковое значение поля passport.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(passport, client.passport);
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(passport.substring(0, 3)) * 31 +
                Integer.parseInt(passport.substring(3, 6)) * 9;
    }
}
