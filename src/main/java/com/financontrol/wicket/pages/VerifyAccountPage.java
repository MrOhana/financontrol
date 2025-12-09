package com.financontrol.wicket.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.financontrol.service.UserService;
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

@WicketHomePage
public class VerifyAccountPage extends BasePage {

    @SpringBean
    private UserService userService;

    public VerifyAccountPage(PageParameters parameters) {
        super(parameters);

        String token = parameters.get("token").toString();

        if (token == null || token.isEmpty()) {
            error("Invalid verification token.");
        } else {
            try {
                userService.verifyUser(token);
                getSession().success("Account verified successfully! You can now login.");
                setResponsePage(LoginPage.class);
            } catch (Exception e) {
                error("Verification failed: " + e.getMessage());
            }
        }
    }
}
