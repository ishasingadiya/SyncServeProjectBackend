//package com.codeIsha.ServiceBookingSystem.services.jwt;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.codeIsha.ServiceBookingSystem.utill.JwtUtill;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
//
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//    
//    @Autowired
//    private JwtUtill jwtUtill;
//    
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String username = null;
//
//        logger.info("Processing request: {}", request.getRequestURI());
//
//        if (request.getRequestURI().equals("/authenticate")) {
//            logger.info("Bypassing JWT filter for /authenticate endpoint");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//         if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//            username = jwtUtill.extractUsername(token);
//            logger.info("Extracted Username: {}", username);
//        } else {
//            logger.warn("No valid Authorization header found");
//        }
//
//         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            if (userDetails != null) {
//                logger.info("Loaded user details for: {}", username);
//
//                if (jwtUtill.validateToken(token, userDetails)) {
//                    logger.info("JWT token validated for user: {}", username);
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities());
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                } else {
//                    logger.warn("JWT token validation failed for user: {}", username);
//                }
//            } else {
//                logger.warn("UserDetails not found for username: {}", username);
//            }
//        }
//
//         filterChain.doFilter(request, response);
//    }
//
//}
