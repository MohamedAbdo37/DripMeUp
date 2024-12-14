package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
//        return request.getServletPath().startsWith("/api/5/users/login") ||
//                request.getServletPath().startsWith("/api/5/users/signup") ||
//                request.getServletPath().startsWith("/api/5/users/g/login") ||
//                request.getServletPath().startsWith("/api/5/users/g/signup") ||
//                request.getServletPath().startsWith("/api/6/admin/login");
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtService.extractRole(token);
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", List.of(new SimpleGrantedAuthority(role)));

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                Long id = jwtService.extractId(token) ;
                CustomAuthentication customAuth = new CustomAuthentication(id, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(customAuth);

            }
        }

        filterChain.doFilter(request, response);
    }

}