package com.financontrol.wicket.pages;

import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.financontrol.service.UserService;

public class ForgotPasswordPage extends PublicBasePage {

    @SpringBean
    private UserService userService;

    private String email;

    public ForgotPasswordPage() {
        setDefaultModel(new CompoundPropertyModel<>(this));

        add(new FeedbackPanel("feedback"));

        Form<Void> form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                userService.initiatePasswordRecovery(email);
                info("If the email exists, a recovery link has been sent.");
            }
        };

        form.add(new EmailTextField("email").setRequired(true));

        add(form);
    }
}
