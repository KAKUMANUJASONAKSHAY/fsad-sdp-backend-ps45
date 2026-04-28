package com.klef.fsad.sdp.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.klef.fsad.sdp.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException
    {
        String path = request.getServletPath();

        List<String> publicPaths = List.of(
                "/auth",
                "/swagger-ui",
                "/v3/api-docs",
                "/swagger-ui.html",
                "/studentapi/registration",
                "/mail",
                "/otp",
                "/files",
                "/payment"
        );

        boolean isPublic = publicPaths.stream().anyMatch(path::startsWith);

        if (isPublic)
        {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer "))
        {
            sendErrorResponse(response, 401, "Authorization header is missing or must start with 'Bearer '");
            return;
        }

        String token = header.substring(7).trim();

        try
        {
            String username = jwtUtil.extractUsername(token);

            if (username == null)
            {
                sendErrorResponse(response, 401, "Invalid token: Username could not be extracted");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null)
            {
                UserDetails userDetails = service.loadUserByUsername(username);

                if (userDetails != null && jwtUtil.validateToken(token, userDetails))
                {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                else
                {
                    sendErrorResponse(response, 401, "Invalid or expired token");
                    return;
                }
            }
        }
        catch (Exception e)
        {
            sendErrorResponse(response, 401, "Invalid token: " + e.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException
    {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String error = (status == 401) ? "Unauthorized" : "Forbidden";
        String jsonResponse = "{\"error\":\"" + error + "\",\"message\":\"" + message + "\"}";

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
