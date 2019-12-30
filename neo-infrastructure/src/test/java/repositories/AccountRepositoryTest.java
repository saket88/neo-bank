package repositories;

import com.bank.domain.Account;
import com.bank.repositories.AccountDao;
import com.google.inject.Guice;

import com.google.inject.Injector;
import module.TestRepositoryModule;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AccountRepositoryTest {


    AccountDao accountDao ;


    @Before
    public void setup(){
        Injector injector = Guice.createInjector(new TestRepositoryModule());
        accountDao = injector.getInstance(AccountDao.class);

    }

    @Test
    public void testSaveAccount(){
       Account account= Account
                .builder()
                .balance(new BigDecimal(100))
                .identificationType("PASSPORT")
                .currency("EUR")
                .name("Test name")
                .uniqueIdentificationNumber("abc123")
                .build();
        Optional<Account> accountExpected = accountDao.save(account);
        assertTrue(EqualsBuilder.reflectionEquals(accountExpected.get(),account,"id","readWriteLock"));

    }

    @Test
    public void canGetAccountFromAccountNumber(){
        final String accounNumber = "NL123";
        Account account= Account
                .builder()
                .accountNumber(accounNumber)
                .balance(new BigDecimal(100))
                .identificationType("PASSPORT")
                .currency("EUR")
                .name("Test name")
                .uniqueIdentificationNumber("abc123")
                .build();

          accountDao.save(account);

        Optional<Account> accountExpected = accountDao.getFromAccountNumber(accounNumber);

        Account expectedAccount = accountExpected.get();

        assertThat(expectedAccount.getBalance(),is(equalTo(expectedAccount.getBalance())));
        assertThat(expectedAccount.getAccountNumber(),is(equalTo(expectedAccount.getAccountNumber())));
        assertThat(expectedAccount.getCurrency(),is(equalTo(expectedAccount.getCurrency())));
        assertThat(expectedAccount.getUniqueIdentificationNumber(),is(equalTo(expectedAccount.getUniqueIdentificationNumber())));
        assertThat(expectedAccount.getIdentificationType(),is(equalTo(expectedAccount.getIdentificationType())));

    }

    @Test
    public void canGetAllAccounts(){

        Account account1= Account
                .builder()
                .accountNumber("NL123")
                .balance(new BigDecimal(100))
                .identificationType("PASSPORT")
                .currency("EUR")
                .name("Test name")
                .uniqueIdentificationNumber("abc123")
                .build();

        Account account2= Account
                .builder()
                .accountNumber("NL456")
                .balance(new BigDecimal(100))
                .identificationType("PASSPORT")
                .currency("EUR")
                .name("Test name")
                .uniqueIdentificationNumber("abc123")
                .build();

        Optional<List<Account>> accounts = Optional.of(new ArrayList<>());
        accounts.get().add(account1);
        accounts.get().add(account2);

        accountDao.save(account1);
        accountDao.save(account2);



        Optional<List<Account>> accountExpected = accountDao.getAccounts();

        assertTrue(EqualsBuilder.reflectionEquals(accountExpected.get(),accounts.get(),"id","readWriteLock"));

    }

    @Test
    public void testTransactionAfterASave(){
        Account account= Account
                .builder()
                .balance(new BigDecimal(100))
                .identificationType("PASSPORT")
                .currency("EUR")
                .name("Test name")
                .uniqueIdentificationNumber("abc123")
                .build();
        Optional<Account> accountSaved = accountDao.save(account);

        Optional<Account> accountExpected = accountDao.getAccount(accountSaved.get().getId());

        assertThat(accountExpected.get().getBalance(),is(equalTo(accountExpected.get().getBalance())));
        assertThat(accountExpected.get().getAccountNumber(),is(equalTo(accountExpected.get().getAccountNumber())));
        assertThat(accountExpected.get().getCurrency(),is(equalTo(accountExpected.get().getCurrency())));
        assertThat(accountExpected.get().getUniqueIdentificationNumber(),is(equalTo(accountExpected.get().getUniqueIdentificationNumber())));
        assertThat(accountExpected.get().getIdentificationType(),is(equalTo(accountExpected.get().getIdentificationType())));

    }



}
