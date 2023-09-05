package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    /**
     * constructors
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * methods
     */
    public Account login(Account credentials){
        return accountDAO.login(credentials);
    }

    public Account register(Account credentials){
        return accountDAO.register(credentials);
    }

}
