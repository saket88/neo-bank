package services;

import com.bank.domain.Account;
import com.bank.model.AccountValueObject;
import com.bank.repositories.AccountDao;
import com.bank.services.AccountService;
import cucumber.api.java.en_old.Ac;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    AccountDao  accountDao ;

    @InjectMocks
    AccountService accountService ;



    @Test
    public void canSaveAccount(){

        AccountValueObject accountValueObject = AccountValueObject.builder().
                balance(new BigDecimal(1000))
                .currency("EUR")
                .uniqueIdentificationNumber("abc123")
                .identificationType("Passport")
                .name("Test name").build();

        Account account = Account.builder()
                .balance(accountValueObject.getBalance())
                .currency(accountValueObject.getCurrency())
                .uniqueIdentificationNumber(accountValueObject.getUniqueIdentificationNumber())
                .identificationType(accountValueObject.getIdentificationType())
                .name(accountValueObject.getName()).build();

        BDDMockito.given(accountDao.save(any())).willReturn(account);

        AccountValueObject accountValueObjectExpected = accountService.create(accountValueObject);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(accountValueObjectExpected,accountValueObject));



    }
}
