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
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Matchers.isNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    AccountDao  accountDao ;

    @InjectMocks
    AccountService accountService ;



    @Test
    public void canSaveAccount(){

        AccountValueObject accountValueObject = buildAccountValueObject("EUR", 1000);
        Account account = buildAccount(accountValueObject);
        BDDMockito.given(accountDao.save(any())).willReturn(Optional.of(account));

        AccountValueObject accountValueObjectExpected = accountService.create(accountValueObject);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountValueObjectExpected,accountValueObject,"accountNumber"));





    }

    @Test(expected = CurrencyNotAllowedException.class)
    public void canNotTransferInvalidCurrency(){

        AccountValueObject accountValueObject = buildAccountValueObject("USD", 1000);
        Account account = buildAccount(accountValueObject);
        BDDMockito.given(accountDao.save(any())).willReturn(Optional.of(account));

        AccountValueObject accountValueObjectExpected = accountService.create(accountValueObject);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountValueObjectExpected,accountValueObject));
        MatcherAssert.assertThat(accountValueObjectExpected.getAccountNumber(),is(not(nullValue())));



    }
    @Test(expected = InvalidAmountException.class)
    public void canNotTransferInvalidAmount(){

        AccountValueObject accountValueObject = buildAccountValueObject("EUR",-1000);
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
}
