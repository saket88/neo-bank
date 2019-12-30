package repositories;

import com.bank.domain.Transfer;
import com.bank.repositories.TransferDao;
import com.google.inject.Guice;
import com.google.inject.Injector;
import module.TestRepositoryModule;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import com.bank.services.exception.NoTransactionAvailableException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TransferRepositoryTest {


    TransferDao transferDao;

    @Before
    public void setup(){
        Injector injector = Guice.createInjector(new TestRepositoryModule());
        transferDao = injector.getInstance(TransferDao.class);

    }

    @Test
    public void testSaveTransfer(){
        Transfer transfer= Transfer
                .builder()
                .createdOn(LocalDateTime.now())
                .transactionId("TXN-"+Math.round(100*Math.random()))
                .currency("EUR")
                .amount(new BigDecimal(100))
                .build();
        Optional<Transfer> transferExpected = transferDao.save(transfer);
        assertTrue(EqualsBuilder.reflectionEquals(transferExpected.get(),transfer,"id"));

    }

    @Test
    public void testTransactionAfterASave(){
        Transfer transfer= Transfer
                .builder()
                .createdOn(LocalDateTime.now())
                .transactionId("TXN-"+Math.round(100*Math.random()))
                .currency("EUR")
                .amount(new BigDecimal(100.0))
                .build();
        Transfer transferExpected = transferDao.save(transfer).get();

        Transfer transferGet = transferDao.getTransfer(transferExpected.getId()).get();

        assertThat(transferExpected.getAmount(), Matchers.comparesEqualTo(transfer.getAmount()));
        assertThat(transferExpected.getCurrency(),is(equalTo(transferGet.getCurrency())));
        assertThat(transferExpected.getTransactionId(),is(equalTo(transferGet.getTransactionId())));


    }

    @Test(expected = NoTransactionAvailableException.class)
    public void testTransactionWithNonExistingId(){
        Transfer transfer= Transfer
                .builder()
                .createdOn(LocalDateTime.now())
                .transactionId("TXN-"+Math.round(100*Math.random()))
                .currency("EUR")
                .amount(new BigDecimal(100))
                .build();
        Optional<Transfer> transferExpected = transferDao.save(transfer);

        Optional<Transfer> transferGet = transferDao.getTransfer(1l);
        assertTrue(EqualsBuilder.reflectionEquals(transferGet,transferExpected));

    }



}
