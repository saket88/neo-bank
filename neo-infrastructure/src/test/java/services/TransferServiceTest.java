package services;


import com.bank.domain.Account;
import com.bank.domain.Transfer;
import com.bank.model.TransferValueObject;
import com.bank.repositories.AccountDao;
import com.bank.repositories.TransferDao;
import com.bank.services.TransferService;
import com.bank.services.exception.CurrencyNotAllowedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceTest {

    @Mock
    AccountDao accountDao ;

    @Mock
    TransferDao transferDao;

    @InjectMocks
    TransferService transferService ;



    @Test
    public void canMakeTransfer(){

        TransferValueObject transferValueObject = TransferValueObject.builder().
                amount(new BigDecimal(10))
                .currency("EUR")
                .fromAccount("NL123")
                .toAccount("NL456")
                .build();

        Transfer transfer = Transfer.builder()
                .id(123l)
                .amount(transferValueObject.getAmount())
                .currency(transferValueObject.getCurrency())
                .transactionId("txn123")
                .createdOn(LocalDateTime.now())
                .build();

        Account fromAccount = Account.builder()
                .id(123l)
                .balance(new BigDecimal(100))
                .currency("EUR")
                .identificationType("Passport")
                .uniqueIdentificationNumber("abc123")
                .accountNumber("NL123")
                .build();

        Account toAccount = Account.builder()
                .id(456l)
                .balance(new BigDecimal(200))
                .currency("EUR")
                .identificationType("Passport")
                .uniqueIdentificationNumber("abc456")
                .accountNumber("NL456")
                .build();


        Transfer mockedTransfer = mock(Transfer.class);
        BDDMockito.given(accountDao.getFromAccountNumber("NL123")).willReturn(Optional.of(fromAccount));
        BDDMockito.given(accountDao.getFromAccountNumber("NL456")).willReturn(Optional.of(toAccount));
        BDDMockito.given(transferDao.save(anyObject())).willReturn(Optional.of(mockedTransfer));

        transferService.sendMoney(transferValueObject);

        verify(transferDao,times(1)).save(any());

        assertThat(fromAccount.getBalance(),is(equalTo(new BigDecimal(90))));
        assertThat(toAccount.getBalance(),is(equalTo(new BigDecimal(210))));


    }

    @Test(expected = CurrencyNotAllowedException.class)
    public void cannotMakeTransferWithInvalidCurrency(){

        TransferValueObject transferValueObject = TransferValueObject.builder().
                amount(new BigDecimal(10))
                .currency("USD")
                .fromAccount("NL123")
                .toAccount("NL456")
                .build();

        Transfer transfer = Transfer.builder()
                .id(123l)
                .amount(transferValueObject.getAmount())
                .currency(transferValueObject.getCurrency())
                .transactionId("txn123")
                .createdOn(LocalDateTime.now())
                .build();

        Account fromAccount = Account.builder()
                .id(123l)
                .balance(new BigDecimal(100))
                .currency("EUR")
                .identificationType("Passport")
                .uniqueIdentificationNumber("abc123")
                .accountNumber("NL123")
                .build();

        Account toAccount = Account.builder()
                .id(456l)
                .balance(new BigDecimal(200))
                .currency("EUR")
                .identificationType("Passport")
                .uniqueIdentificationNumber("abc456")
                .accountNumber("NL456")
                .build();


        Transfer mockedTransfer = mock(Transfer.class);
        BDDMockito.given(accountDao.getFromAccountNumber("NL123")).willReturn(Optional.of(fromAccount));
        BDDMockito.given(accountDao.getFromAccountNumber("NL456")).willReturn(Optional.of(toAccount));
        BDDMockito.given(transferDao.save(anyObject())).willReturn(Optional.of(mockedTransfer));

        transferService.sendMoney(transferValueObject);

        verify(transferDao,times(1)).save(any());

        assertThat(fromAccount.getBalance(),is(equalTo(new BigDecimal(90))));
        assertThat(toAccount.getBalance(),is(equalTo(new BigDecimal(210))));


    }

}
