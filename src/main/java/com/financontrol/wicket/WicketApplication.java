package com.financontrol.wicket;

import com.financontrol.wicket.pages.*;
import com.giffing.wicket.spring.boot.starter.app.WicketBootStandardWebApplication;
import org.apache.wicket.Page;
import org.springframework.stereotype.Component;

@Component
public class WicketApplication extends WicketBootStandardWebApplication {

    @Override
    protected void init() {
        super.init();

        // Mount pages to pretty URLs
        mountPage("/login", LoginPage.class);
        mountPage("/register", RegisterPage.class);
        mountPage("/forgot-password", ForgotPasswordPage.class);
        mountPage("/dashboard", DashboardPage.class);
        mountPage("/expenses", ExpensesPage.class);
        mountPage("/incomes", IncomesPage.class);
        mountPage("/verify", VerifyAccountPage.class);
        mountPage("/reset-password", ResetPasswordPage.class);

        getCspSettings().blocking().disabled(); // TODO: Disable strict CSP for development ease/bootstrap cdn if used
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return DashboardPage.class;
    }
}
