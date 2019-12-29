package services;

import com.bank.domain.Account;
import com.bank.model.AccountValueObject;
import com.bank.repositories.AccountDao;
import com.bank.services.AccountService;
import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.management.BadAttributeValueExpException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    AccountDao  accountDao ;

    @InjectMocks
    AccountService accountService ;



    @Test
    public void canSaveAccount() throws BadAttributeValueExpException {

        AccountValueObject accountValueObject = buildAccountValueObject("EUR", 1000);
        Account account = buildAccount(accountValueObject);
        BDDMockito.given(accountDao.save(any())).willReturn(Optional.of(account));

        AccountValueObject accountValueObjectExpected = accountService.create(accountValueObject);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountValueObjectExpected,accountValueObject,"accountNumber"));





    }

    @Test
    public void canGetAccounts(){

        AccountValueObject accountValueObject1 = buildAccountValueObject("EUR", 100);

        AccountValueObject accountValueObject2 = buildAccountValueObject("EUR", 10);


        Account account1 = buildAccount(accountValueObject1);
        Account account2 = buildAccount(accountValueObject2);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        BDDMockito.given(accountDao.getAccounts()).willReturn(Optional.of(accounts));

        List<AccountValueObject> accountsExpexted = accountService.getAccounts();

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountsExpexted,accounts));





    }

    @Test(expected = CurrencyNotAllowedException.class)
    public void canNotCreateAccountWithInvalidCurrency() throws BadAttributeValueExpException {

        AccountValueObject accountValueObject = buildAccountValueObject("USD", 1000);
        Account account = buildAccount(accountValueObject);
        BDDMockito.given(accountDao.save(any())).willReturn(Optional.of(account));

        AccountValueObject accountValueObjectExpected = accountService.create(accountValueObject);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountValueObjectExpected,accountValueObject));
        MatcherAssert.assertThat(accountValueObjectExpected.getAccountNumber(),is(not(nullValue())));



    }
    @Test(expected = InvalidAmountException.class)
    public void canNotCreateAccountInvalidAmount() throws BadAttributeValueExpException {

        AccountValueObject accountValueObject = buildAccountValueObject("EUR",-1000);
        Account account = buildAccount(accountValueObject);
        BDDMockito.given(accountDao.save(any())).willReturn(Optional.of(account));

        AccountValueObject accountValueObjectExpected = accountService.create(accountValueObject);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountValueObjectExpected,accountValueObject));



    }


    @Test(expected = BadAttributeValueExpException.class)
    public void canNotCreateAccountWithBlankField() throws BadAttributeValueExpException {

        AccountValueObject accountValueObject = buildAccountValueObject("EUR",1000,"");
        Account account = buildAccount(accountValueObject);
        BDDMockito.given(accountDao.save(any())).willReturn(Optional.of(account));

        AccountValueObject accountValueObjectExpected = accountService.create(accountValueObject);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountValueObjectExpected,accountValueObject));



    }



    private Account buildAccount(AccountValueObject accountValueObject) {
        return Account.builder()
                .accountNumber(accountValueObject.getAccountNumber())
                .balance(accountValueObject.getBalance())
                .currency(accountValueObject.getCurrency())
                .uniqueIdentificationNumber(accountValueObject.getUniqueIdentificationNumber())
                .identificationType(accountValueObject.getIdentificationType())
                .name(accountValueObject.getName()).build();
    }

    private AccountValueObject buildAccountValueObject(String currency, int amount) {
        return AccountValueObject.builder()
                .accountNumber("NL123")
                .balance(new BigDecimal(amount))
                .currency(currency)
                .uniqueIdentificationNumber("abc123")
                .identificationType("Passport")
                .name("Test name").build();
    }

    private AccountValueObject buildAccountValueObject(String currency, int amount,String firstName) {
        return AccountValueObject.builder()
                .accountNumber("NL123")
                .balance(new BigDecimal(amount))
                .currency(currency)
                .uniqueIdentificationNumber("abc123")
                .identificationType("Passport")
                .name(firstName).build();
    }
}
