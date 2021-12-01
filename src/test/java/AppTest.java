
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.DBServerInstance;
import io.ebean.EbeanServer;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;
import io.ebean.DB;
import io.ebean.Transaction;
import pojo.Error;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static EbeanServer db;
    private static Transaction transaction;

    @BeforeAll
    public static void beforeAll() {
        app = Main.getApp();
        app.start(0);
        int port = app.port();
        db = DBServerInstance.getInstance();
        baseUrl = "http://localhost:" + port + "/v1.0/";
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @BeforeEach
    void beforeEach() {
        transaction = DB.beginTransaction();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Test
    void testRoot() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testUsers() {
        HttpResponse<String> response = Unirest
                .get(baseUrl + "users/")
                .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testNewUser_negative() throws JsonProcessingException {

        HttpResponse<String> response = Unirest
                .post(baseUrl + "users/")
                .asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testCreateUser() {

        String firstName = "Aleksandr";
        String lastName = "Vasiliev";
        String birthdate = "26/10/1970";

        HttpResponse<String> responsePost = Unirest
                .post(baseUrl + "users/")
                .field("name", firstName)
                .field("lastname", lastName)
                .field("birthdate", birthdate)
                        .asString();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        User actualUser = db.find(User.class)
                .where().eq("lastname", lastName)
                .findOne();


        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getName()).isEqualTo(firstName);
        assertThat(actualUser.getLastname()).isEqualTo(lastName);
    }

    @Test
    void testCreateUserWithIncorrectData() {

        String firstName = "";
        String lastName = "Petroff";
        String email = "yandex.ru";
        String password = "as";

        HttpResponse<String> responsePost = Unirest
                .post(baseUrl + "/users")
                .field("firstName", firstName)
                .field("lastName", lastName)
                .field("email", email)
                .field("password", password)
                .asString();

        assertThat(responsePost.getStatus()).isEqualTo(422);

        User actualUser = db.find(User.class)
                .where().eq("lastname", lastName)
                .findOne();

        assertThat(actualUser).isNull();

        String content = responsePost.getBody();

        assertThat(content).contains("yandex.ru");
        assertThat(content).contains("Petroff");
        assertThat(content).contains("Имя не должно быть пустым");
        assertThat(content).contains("Должно быть валидным email");
        assertThat(content).contains("Пароль должен содержать не менее 4 символов");
    }
}
