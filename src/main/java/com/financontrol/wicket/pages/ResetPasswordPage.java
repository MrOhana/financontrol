package com.financontrol.wicket.pages;

import com.financontrol.service.UserService;
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

@WicketHomePage
public class ResetPasswordPage extends PublicBasePage {

    @SpringBean
    private UserService userService;

    public ResetPasswordPage(PageParameters parameters) {
        super(parameters);

        String token = parameters.get("token").toString();
        if (token == null || token.isEmpty()) {
            error("Invalid reset token.");
        }

        add(new FeedbackPanel("feedback"));
        add(new ResetForm("form", token));
    }

    private class ResetForm extends Form<Void> {
        private String token;
        private PasswordTextField passwordField;
        private PasswordTextField confirmPasswordField;

        public ResetForm(String id, String token) {
            super(id);
            this.token = token;

            passwordField = new PasswordTextField("password", Model.of(""));
            confirmPasswordField = new PasswordTextField("confirmPassword", Model.of(""));

            add(passwordField);
            add(confirmPasswordField);

            add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));
        }

        @Override
        protected void onSubmit() {
            try {
                if (token == null) {
                    error("Missing token.");
                    return;
                }
                userService.resetPassword(token, passwordField.getModelObject());
                getSession().success("Password reset successfully. Please login.");
                setResponsePage(LoginPage.class);
            } catch (Exception e) {
                error("Failed to reset password: " + e.getMessage());
            }
        }
    }
}
