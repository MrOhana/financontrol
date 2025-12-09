package com.financontrol.wicket.pages;

import com.financontrol.domain.User;
import com.financontrol.service.UserService;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterPage extends PublicBasePage {

    private static final Logger logger = LoggerFactory.getLogger(RegisterPage.class);

    @SpringBean
    private UserService userService;

    private String name;
    private String email;
    private String password;

    public RegisterPage() {
        setDefaultModel(new CompoundPropertyModel<>(this));

        add(new FeedbackPanel("feedback"));

        Form<Void> form = new Form<Void>("registerForm") {
            @Override
            protected void onSubmit() {
                logger.info("Attempting to register user with email: {}", email);
                try {
                    User newUser = User.builder()
                            .name(name)
                            .email(email)
                            .password(password)
                            .build();
                    userService.registerUser(newUser);
                    logger.info("User registered successfully: {}", email);
                    getSession().success("Registration successful! Please check your email to verify your account.");
                    setResponsePage(LoginPage.class);
                } catch (Exception e) {
                    logger.error("Registration failed for user: " + email, e);
                    error("Registration failed: " + e.getMessage());
                }
            }
        };

        form.add(new TextField<>("name").setRequired(true));
        form.add(new EmailTextField("email").setRequired(true));
        form.add(new PasswordTextField("password").setRequired(true));

        add(form);
    }
}
