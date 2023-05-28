package org.nextlevel.security.cors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class NextLevelCorsFilter implements Filter {

    private final Logger LOG = LoggerFactory.getLogger(NextLevelCorsFilter.class);

    public NextLevelCorsFilter() {
        LOG.info("NextLevelCorsFilter initialised...");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        LOG.info("**** Origin: {}", origin);

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, PATCH");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Max-Age", "300");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Origin");

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
        LOG.info("NextLevelCorsFilter destroyed...");
    }

}
