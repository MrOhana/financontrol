package com.financontrol.wicket.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.feedback.FeedbackCollector;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class BasePage extends WebPage {

    public BasePage() {
        this(null);
    }

    public BasePage(PageParameters parameters) {
        super(parameters);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser"))
                ? auth.getName()
                : "Guest";

        add(new Label("username", Model.of(username)));

        // Navigation Links (Desktop)
        add(new BookmarkablePageLink<>("dashboardLink", DashboardPage.class));
        add(new BookmarkablePageLink<>("expensesLink", ExpensesPage.class));
        add(new BookmarkablePageLink<>("incomesLink", IncomesPage.class));
        add(new org.apache.wicket.markup.html.link.ExternalLink("logoutLink", "/logout"));

        // Navigation Links (Mobile)
        add(new BookmarkablePageLink<>("dashboardLinkMobile", DashboardPage.class));
        add(new BookmarkablePageLink<>("expensesLinkMobile", ExpensesPage.class));
        add(new BookmarkablePageLink<>("incomesLinkMobile", IncomesPage.class));
        add(new org.apache.wicket.markup.html.link.ExternalLink("logoutLinkMobile", "/logout"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        // 1. Collect messages from components (validation errors)
        List<FeedbackMessage> collectedMessages = new FeedbackCollector(getPage()).collect();

        // 2. Collect messages from session (success/info/error added via getSession())
        FeedbackMessages sessionMessages = getSession().getFeedbackMessages();

        // 3. Combine them without duplicates
        Set<FeedbackMessage> allMessages = new HashSet<>();
        allMessages.addAll(collectedMessages);
        for (FeedbackMessage msg : sessionMessages) {
            allMessages.add(msg);
        }

        if (!allMessages.isEmpty()) {
            StringBuilder js = new StringBuilder();

            for (FeedbackMessage message : allMessages) {
                // Skip already rendered messages
                if (message.isRendered()) {
                    continue;
                }

                // Escape quotes and newlines
                String text = message.getMessage().toString()
                        .replace("'", "\\'")
                        .replace("\n", " ");

                String colorClass = "grey darken-3"; // Default

                if (message.isError()) {
                    colorClass = "red darken-2";
                } else if (message.isSuccess()) {
                    colorClass = "green darken-2";
                } else if (message.isInfo()) {
                    colorClass = "blue darken-2";
                }

                // Log to console for debugging
                js.append(String.format("console.log('Feedback: %s');", text));

                // M.toast
                js.append(String.format(
                        "if(typeof M !== 'undefined') { M.toast({html: '%s', classes: 'rounded %s', displayLength: 6000}); } else { console.error('Materialize M not defined'); }",
                        text, colorClass));

                message.markRendered();
            }

            // Clear session messages explicitly
            sessionMessages.clear();

            if (js.length() > 0) {
                response.render(OnDomReadyHeaderItem.forScript(js.toString()));
            }
        }
    }
}
