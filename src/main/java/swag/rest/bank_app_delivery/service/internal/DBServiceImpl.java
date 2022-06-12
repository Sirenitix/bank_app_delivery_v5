package swag.rest.bank_app_delivery.service.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import swag.rest.bank_app_delivery.dao.AccountMapper;
import swag.rest.bank_app_delivery.entity.Account;
import swag.rest.bank_app_delivery.service.BankCore;
import swag.rest.bank_app_delivery.service.DBService;

import java.util.List;

@Component
public class DBServiceImpl implements DBService {

    @Autowired
    private final JdbcTemplate jdbcTemplate;



    private final String SQL_GET_ALL = "select * from account where clientid = ?";
    private final String SQL_GET_MAX_ID = "select max(bankid) from account";
    private final String SQL_FIND_ACCOUNT = "select * from account where bankID = ? and clientid = ?";
    private final String SQL_INSERT_ACCOUNT = "insert into account(accountType, id, clientID,  bankID,  balance, withdrawAllowed) values(?, ?, ?, ?, ?, ?)";
    private final String SQL_UPDATE_BALANCE = "update account set balance = ? where bankid = ?";

    private final String SQL_DELETE_ACCOUNT = "DELETE FROM account WHERE bankid=? and clientid = ?";

    public DBServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void insertData() {
        jdbcTemplate.execute("insert into account(id,clientID,bankID,balance,withdrawAllowed,accountType) values(1,'1',1,0.0,false,'FIXED')");
        jdbcTemplate.execute("insert into account(id,clientID,bankID,balance,withdrawAllowed,accountType) values(1,'1',2,0.0,true,'SAVING')");
    }

    public List<Account> getClientAccounts(int id) {
        return jdbcTemplate.query(SQL_GET_ALL, new Object[] { id }, new AccountMapper());
    }

    public void createNewAccount(Account account) {
        jdbcTemplate.update(SQL_INSERT_ACCOUNT, String.valueOf(account.getAccountType()), account.getId(), account.getClientID(), account.getBankID(), account.getBalance(), account.isWithdrawAllowed());
    }

    public Account getClientAccountById(int id, int clientID) {
        return jdbcTemplate.queryForObject(SQL_FIND_ACCOUNT, new Object[] { id , clientID }, new AccountMapper());
    }

    public int getClientAccountMaxId() {
        if(jdbcTemplate.queryForObject(SQL_GET_MAX_ID, Integer.class) == null){
            return 0;
        }else {
            return jdbcTemplate.queryForObject(SQL_GET_MAX_ID, Integer.class);
        }
    }

    public void updateAccount(Account account) {
        jdbcTemplate.update(SQL_UPDATE_BALANCE, account.getBalance(), account.getBankID());
    }

    @Override
    public void deleteClientAccountById(int id, int clientID) {
        jdbcTemplate.update(SQL_DELETE_ACCOUNT,id, clientID);
    }


}
