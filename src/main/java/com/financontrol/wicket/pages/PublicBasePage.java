package com.financontrol.wicket.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class PublicBasePage extends WebPage {

    public PublicBasePage() {
        super();
    }

    public PublicBasePage(PageParameters parameters) {
        super(parameters);
    }
}
