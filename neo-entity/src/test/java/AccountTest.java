import com.bank.domain.Account;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AccountTest {

    Account fromAccount;
    Account toAccount;


    @Before
    public void setUp(){
        fromAccount = Account.builder()
                .accountNumber("NL123")
                .balance(new BigDecimal(100000))
                .currency("EUR")
                .build();

        toAccount= Account.builder()
                .accountNumber("NL123")
                .balance(new BigDecimal(200000))
                .currency("EUR")
                .build();
    }

    @Test
    public void withDrawAmount(){
        fromAccount.withdraw(new BigDecimal(100));
        assertThat(fromAccount.getBalance(),is(equalTo(new BigDecimal(99900))));
    }

    @Test
    public void depositAmount(){
        toAccount.deposit(new BigDecimal(100));
        assertThat(toAccount.getBalance(),is(equalTo(new BigDecimal(200100))));
    }

    @Test
    public  void accountIsThreadSafe() throws InterruptedException {
        int NUM_OF_THREADS=4;
         BigDecimal amount=new BigDecimal(100);
        Collection<Callable<Void>> tasks = new ArrayList<>(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            tasks.add(() -> {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
                return null;
            });
        }

        Executors.newFixedThreadPool(4).invokeAll(tasks);

        assertThat(fromAccount.getBalance(),is(equalTo(new BigDecimal(99600))));
        assertThat(toAccount.getBalance(),is(equalTo(new BigDecimal(200400))));

    }
}


