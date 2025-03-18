package ca.bc.gov.open.staffnet.configuration;

import java.io.IOException;
import java.net.HttpURLConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.util.Base64;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

@Configuration
public class WebServiceSenderWithAuth extends HttpUrlConnectionMessageSender {

    @Value("${security.web-service.username}")
    private String username;

    @Value("${security.web-service.password}")
    private String password;

    @Override
    protected void prepareConnection(HttpURLConnection connection) throws IOException {
        String input = username + ":" + password;
        String auth = Base64.getEncoder().encodeToString(input.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + auth);

        super.prepareConnection(connection);
    }
}
