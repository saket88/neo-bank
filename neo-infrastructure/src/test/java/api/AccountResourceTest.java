package api;

import com.bank.api.AccountResource;
import com.bank.model.AccountValueObject;
import com.bank.services.AccountService;
import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import javax.management.BadAttributeValueExpException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;


public class AccountResourceTest extends BaseResourceTest{



    AccountService accountService = mock(AccountService.class);
    AccountResource accountResource = new AccountResource(new Gson(),accountService);



    @Before
    public void setUp() throws Exception {
        super.setUp();
       accountResource.initialize();

    }



    @Test
    public void canCreateAnAccount() throws FileNotFoundException, BadAttributeValueExpException {


        final AccountValueObject payload = new Gson().fromJson(new JsonReader(new FileReader(
                getClass().getClassLoader().getResource("create_account.json").getFile())), AccountValueObject.class);

        BDDMockito.given(accountService.create(any())).willReturn(payload);
        AccountValueObject account = given()
                .contentType(ContentType.JSON)
                .body(new Gson().toJson(payload))
                .post("/api/v1/account")
                .then()
                .statusCode(201)
                .extract()
                .as(AccountValueObject.class);

        assertTrue(EqualsBuilder.reflectionEquals(payload,account));



    }


    @Test
    public void canGetAllTheAccounts() throws FileNotFoundException {


        final ArrayList<AccountValueObject> payload = new Gson().fromJson(new JsonReader(new FileReader(
                getClass().getClassLoader().getResource("account_list.json").getFile())), ArrayList.class);

        BDDMockito.given(accountService.getAccounts()).willReturn(payload);

        ArrayList expected  = given()
                .contentType(ContentType.JSON)
                .get("/api/v1/account")
                .then()
                .statusCode(200)
                .extract()
                .as(ArrayList.class);

        assertTrue(EqualsBuilder.reflectionEquals(expected,payload));



    }


    @Test
    public void shouldThrowBadRequestExceptionOnPassingWrongAmount() throws BadAttributeValueExpException {


        BDDMockito.given(accountService.create(any()))
                .willThrow(new InvalidAmountException("This is not allowed"));
        given()
                .contentType(ContentType.JSON)
                .body("{\"ok\":\"ok\"}")
                .post("/api/v1/account")
                .then()
                .statusCode(400);

    }

    @Test
    public void shouldThrowBadRequestExceptionOnPassingWrongCurrency() throws BadAttributeValueExpException {


        BDDMockito.given(accountService.create(any()))
                .willThrow(new CurrencyNotAllowedException("This is not allowed"));
        given()
                .contentType(ContentType.JSON)
                .body("{\"ok\":\"ok\"}")
                .post("/api/v1/account")
                .then()
                .statusCode(400);

    }


}
