package api;

import com.bank.api.TransferResource;
import com.bank.domain.Transfer;
import com.bank.model.TransferValueObject;
import com.bank.services.TransferService;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;

public class TransferResourceTest extends BaseResourceTest{
    TransferService transferService = mock(TransferService.class);
    TransferResource transferResource = new TransferResource(new Gson(),transferService);


    @Before
    public void setUp() throws Exception {
        super.setUp();
        transferResource.registerTransferRoutes();

    }

    @Test
    public void canTransferMoney() throws FileNotFoundException {

        BDDMockito.given(transferService.sendMoney(anyObject())).willReturn(
                Optional.of(Transfer.builder()
                        .id(1l)
        .amount(new BigDecimal(100)).build()));

        final TransferValueObject payload = new Gson().fromJson(new JsonReader(new FileReader(
                getClass().getClassLoader().getResource("transfer_money.json").getFile())), TransferValueObject.class);

        TransferValueObject  valueObject = given()
                .contentType(ContentType.JSON)
                .body(new Gson().toJson(payload))
                .post("/api/v1/transfer")
                .then()
                .statusCode(201)
                .extract()
                .as(TransferValueObject.class);

        assertThat(valueObject.getId(),is(notNullValue()));


    }


}
