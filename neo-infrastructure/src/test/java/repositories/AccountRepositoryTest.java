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
import java.util.Optional;

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

        assertTrue(EqualsBuilder.reflectionEquals(accountExpected.get(),account,"id","readWriteLock"));

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
        assertTrue(EqualsBuilder.reflectionEquals(accountExpected,accountSaved));

    }



}
