package ca.bc.gov.open.staffnet.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Autowired private ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authEx)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        Map<String, Object> resp = new HashMap<>();
        resp.put("path", request.getRequestURI());
        resp.put("error", "Unauthorized");
        resp.put("status", HttpStatus.UNAUTHORIZED.value());
        resp.put("timestamp", Instant.now().toString());

        writer.println(objectMapper.writeValueAsString(resp));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("staffnet");
        super.afterPropertiesSet();
    }
}
