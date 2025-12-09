package com.financontrol.wicket.pages;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends PublicBasePage {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    @SpringBean
    private AuthenticationManager authenticationManager;

    private String username;
    private String password;

    public LoginPage() {
        // Ensure session is bound so feedback messages persist
        getSession().bind();

        setDefaultModel(new CompoundPropertyModel<>(this));

        add(new FeedbackPanel("feedback"));

        // Check for "error" parameter (Spring Security default failure URL)
        if (getRequest().getRequestParameters().getParameterValue("error").toBoolean(false)) {
            error("Invalid username or password");
        }

        Form<Object> form = new Form<>("loginForm") {
            @Override
            protected void onSubmit() {
                logger.info("Attempting login for user: {}", username);
                try {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
                            password);
                    Authentication auth = authenticationManager.authenticate(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // Manually set session for Spring Security interaction
                    HttpServletRequest request = (HttpServletRequest) getRequest().getContainerRequest();
                    HttpSession session = request.getSession(true);
                    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                            SecurityContextHolder.getContext());

                    setResponsePage(DashboardPage.class);
                } catch (AuthenticationException e) {
                    logger.warn("Login failed for user {}: {}", username, e.getMessage());
                    error("Invalid username or password");
                } catch (Throwable e) {
                    logger.error("CRITICAL error during login for user {}", username, e);
                    error("System error: " + e.getMessage());
                }
            }

            @Override
            protected void onError() {
                logger.warn("Login form validation failed.");
                // Ensure feedback is clear; generic message if needed, though individual fields
                // should report errors
                if (!hasErrorMessage()) {
                    error("Validation failed. Please check your inputs.");
                }
            }
        };

        // IMPORTANT: Bind form directly to the page properties
        form.setModel(new CompoundPropertyModel<>(this));

        form.add(new TextField<>("username").setRequired(true));
        form.add(new PasswordTextField("password").setRequired(true));

        add(form);
    }
}
