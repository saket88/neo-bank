package services;

import com.bank.domain.Account;
import com.bank.model.AccountValueObject;
import com.bank.repositories.AccountDao;
import com.bank.services.AccountService;
import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Matchers.any;

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

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountValueObjectExpected,accountValueObject));



    }

    @Test(expected = CurrencyNotAllowedException.class)
    public void canNotTransferInvalidCurrency(){

        AccountValueObject accountValueObject = buildAccountValueObject("USD", 1000);
        Account account = buildAccount(accountValueObject);
        BDDMockito.given(accountDao.save(any())).willReturn(Optional.of(account));

        AccountValueObject accountValueObjectExpected = accountService.create(accountValueObject);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountValueObjectExpected,accountValueObject));



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
                .balance(accountValueObject.getBalance())
                .currency(accountValueObject.getCurrency())
                .uniqueIdentificationNumber(accountValueObject.getUniqueIdentificationNumber())
                .identificationType(accountValueObject.getIdentificationType())
                .name(accountValueObject.getName()).build();
    }

    private AccountValueObject buildAccountValueObject(String currency, int amount) {
        return AccountValueObject.builder().
                balance(new BigDecimal(amount))
                .currency(currency)
                .uniqueIdentificationNumber("abc123")
                .identificationType("Passport")
                .name("Test name").build();
    }
}
