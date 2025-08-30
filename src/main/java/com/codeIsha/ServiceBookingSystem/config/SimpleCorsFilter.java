package com.codeIsha.ServiceBookingSystem.config;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {
    @Value("${app.client.url}")
    private String clientAppUrl;
    private static final Logger logger = LoggerFactory.getLogger(SimpleCorsFilter.class);
    public SimpleCorsFilter() {
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        String originHeader = request.getHeader("origin");
        logger.info("Origin Header: {}", originHeader);
        if (clientAppUrl.equals(originHeader)) {
            response.setHeader("Access-Control-Allow-Origin", clientAppUrl);
            logger.info("Access-Control-Allow-Origin set to: {}", clientAppUrl);
        }
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            logger.info("OPTIONS request received, setting status to SC_OK");
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            logger.info("Proceeding with filter chain for request: {}", request.getRequestURI());
            chain.doFilter(req, res);
        }
    }
}
