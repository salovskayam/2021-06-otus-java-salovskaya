package ru.otus.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static ru.otus.server.utils.WebServerHelper.*;

@DisplayName("Тест сервера должен ")
class UsersWebServerImplTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String AUTH_HEADER = "Authorization";
    private static final String CLIENT_URL = "clients";
    private static final String API_CLIENT_URL = "api/client";

    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    private static final String DEFAULT_USER_LOGIN = "user1";
    private static final String DEFAULT_USER_PASSWORD = "user1";
    private static final String INCORRECT_USER_LOGIN = "BadUser";
    private static final String INCORRECT_USER_PASSWORD = "BadUser";

    private static Gson gson;
    private static UsersWebServer webServer;
    private static HttpClient http;
    private static LoginService loginService;
    private static DBServiceClient dbServiceClient;
    private static Client client;

    private static Base64.Encoder simpleEncoder = Base64.getEncoder();


    @BeforeAll
    static void setUp() throws Exception {
        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        http = HttpClient.newHttpClient();

        TemplateProcessor templateProcessor = mock(TemplateProcessor.class);
        dbServiceClient = mock(DBServiceClient.class);

        client = new Client("vasya");
        client.setAddress(new Address("any street"));
        client.addPhone(new Phone("300"));

        given(dbServiceClient.saveClient(any(Client.class))).willReturn(client);

        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        webServer = new UsersWebServerImpl(WEB_SERVER_PORT, gson, templateProcessor, loginService, dbServiceClient);
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        webServer.stop();
    }

    @DisplayName("возвращать 401 при запросе защищенных ресурсов, если не выполнен вход ")
    @Test
    void shouldReturnUnauthorizedStatusForUserRequestWhenUnauthorized() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, CLIENT_URL)))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @DisplayName("возвращать 200 при запросе защищенных ресурсов, если данные входа верны")
    @Test
    void shouldReturnOKStatusWhenLoggingInWithCorrectData() throws Exception {
        String encodedString = getEncodedString(DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, CLIENT_URL)))
                .setHeader(AUTH_HEADER, "Basic " + encodedString)
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @DisplayName("возвращать 401 при запросе защищенных ресурсов, если данные входа не верны")
    @Test
    void shouldReturnUnauthorizedStatusForUserRequestWithIncorrectData() throws Exception {
        String encodedString = getEncodedString(INCORRECT_USER_LOGIN, INCORRECT_USER_PASSWORD);

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, CLIENT_URL)))
                .header(AUTH_HEADER, "Basic " + encodedString)
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @DisplayName("возвращать корректные данные при добавлении клиента, если вход выполнен")
    @Test
    void shouldReturnCorrectUserWhenAuthorized() throws Exception {
        String encodedString = getEncodedString(DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, API_CLIENT_URL)))
                .header(AUTH_HEADER, "Basic " + encodedString)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(client)))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).isEqualTo(gson.toJson(client));
    }

    private String getEncodedString(String login, String pass) {
        String encoded = login + ":" + pass;
        return simpleEncoder.encodeToString(encoded.getBytes());
    }
}