package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    /**
     * Constructor, no args 
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    /**
     * Constructor, parameterized 
     * @param accountDAO, an AccountDAO object
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    
    /**
     * Register
     * @param credentials, an Account object
     * @return either the account object if registered successfully or null if registration fails
     */
    public Account register(Account credentials){
        return accountDAO.register(credentials);
    }

    /** 
     * Login
     * @param credentials, an Account object
     * @return either an account object with matching credentials or null if login fails
     */
    public Account login(Account credentials){
        return accountDAO.login(credentials);
    }

}
