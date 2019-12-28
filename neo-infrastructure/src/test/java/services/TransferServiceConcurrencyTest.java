package services;

import com.bank.domain.Account;
import com.bank.domain.Transfer;
import com.bank.model.TransferValueObject;
import com.bank.repositories.AccountDao;
import com.bank.repositories.TransferDao;
import com.bank.services.TransferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceConcurrencyTest {

    @Mock
    AccountDao accountDao ;

    @Mock
    TransferDao transferDao;

    @InjectMocks
    TransferService transferService ;



    @Test
    public void canMakeThreadSafeTransactions() throws InterruptedException {

        TransferValueObject transferValueObject = TransferValueObject.builder().
                amount(new BigDecimal(1.0))
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
                .balance(new BigDecimal(1000000.50))
                .currency("EUR")
                .identificationType("Passport")
                .uniqueIdentificationNumber("abc123")
                .accountNumber("NL123")
                .build();

        Account toAccount = Account.builder()
                .id(456l)
                .balance(new BigDecimal(2000000.25))
                .currency("EUR")
                .identificationType("Passport")
                .uniqueIdentificationNumber("abc456")
                .accountNumber("NL456")
                .build();


        Transfer mockedTransfer = mock(Transfer.class);
        BDDMockito.given(accountDao.getFromAccountNumber("NL123")).willReturn(Optional.of(fromAccount));
        BDDMockito.given(accountDao.getFromAccountNumber("NL456")).willReturn(Optional.of(toAccount));
        BDDMockito.given(transferDao.save(anyObject())).willReturn(Optional.of(mockedTransfer));

        int NUM_OF_THREADS=4000;
        Collection<Callable<Void>> tasks = new ArrayList<>(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            tasks.add(() -> {
                 transferService.sendMoney(transferValueObject);
                 return null;
            });
        }

        Executors.newFixedThreadPool(4).invokeAll(tasks);

        verify(transferDao,times(NUM_OF_THREADS)).save(any());

        assertThat(fromAccount.getBalance(),is(equalTo(new BigDecimal(996000.5))));
        assertThat(toAccount.getBalance(),is(equalTo(new BigDecimal(2004000.25))));


    }
}
