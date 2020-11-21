package com.example.demo.filter;

import com.example.demo.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    SessionRegistry sessionRegistry;

    final private String ANOTHER_JSON_FORMAT = "application/x-www-form-urlencoded";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String verify_code = (String) request.getSession().getAttribute("verify_code");

        System.out.println(request.getContentType());
        System.out.println((MediaType.APPLICATION_JSON_VALUE));
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE) ||  request.getContentType().contains(ANOTHER_JSON_FORMAT)) {

            Map<String, String> loginData = new HashMap<>();
            try {
                loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            } catch (IOException e) {
            }
            /**
             * 用不着验证码
             */
//            finally {
//                String code = loginData.get("code");
//                checkCode(response, code, verify_code);
//            }
            String username = loginData.get("name");
            String password = loginData.get("password");

            System.out.println(password);
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }

            //去除userName头尾之间的空格部分
            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, password);
            setDetails(request, authRequest);
            Student principal = new Student();
            principal.setName(username);
            sessionRegistry.registerNewSession(request.getSession(true).getId(), principal);
            this.getAuthenticationManager().authenticate(authRequest).getCredentials();
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
//            checkCode(response, request.getParameter("code"), verify_code);
            return super.attemptAuthentication(request, response);
        }
    }

    /**
     * 验证相关部分 现在暂时用不着
     */
//    public void checkCode(HttpServletResponse resp, String code, String verify_code) {
//        if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
//            //验证码不正确
//            throw new AuthenticationServiceException("验证码不正确");
//        }
//    }
}
