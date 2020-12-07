import DBworkee.DBworker;
import DataOfBank.Client;
import DataOfBank.Deposit;
import ManageExeptions.AccException;
import ManageExeptions.AuthException;
import ManageExeptions.DepositeException;
import ManageExeptions.EqualAccException;

import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws SQLException, AuthException, AccException, DepositeException {
        Client client1 = new Client("Ivan", "Andreev", "12345678");
        Deposit ivanDep = new Deposit(client1, 10000, 0.01, 0.01, 233, new Date(), true);
        DBworker dBworker = new DBworker();

        try {
            dBworker.addAccountsDAO("Nikitta", "qwerty");
        } catch (EqualAccException e) {

        }
    }
}
