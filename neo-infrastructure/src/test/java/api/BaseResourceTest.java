package api;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import spark.Spark;

import static spark.Spark.*;

public class BaseResourceTest {

    @Before
    public void setUp() throws Exception {
        Spark.stop();
        RestAssured.port =8080;
        port(8080);
//        awaitInitialization();

    }

    @After
    public void tearDown() throws Exception {
        Spark.stop();
        awaitStop();
    }
}
