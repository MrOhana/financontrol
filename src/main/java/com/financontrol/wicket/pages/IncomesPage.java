package com.financontrol.wicket.pages;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.LocalDateConverter;

import com.financontrol.domain.Income;
import com.financontrol.repository.ExpenseCategoryRepository;
import com.financontrol.service.IncomeService;
import com.financontrol.wicket.converter.MoneyConverter;

public class IncomesPage extends BasePage {

    @SpringBean
    private IncomeService incomeService;

    @SpringBean
    private ExpenseCategoryRepository categoryRepository;

    private Income newIncome = new Income();

    private LocalDate filterStartDate = LocalDate.now().withDayOfMonth(1);
    private LocalDate filterEndDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

    public IncomesPage() {
        super();

        newIncome.setDate(LocalDate.now());

        // === Filter Form ===
        Form<Void> filterForm = new Form<>("filterForm");

        filterForm.add(
                new TextField<>("startDate", new PropertyModel<>(this, "filterStartDate")) {
                    @Override
                    protected String[] getInputTypes() {
                        return new String[] { "date" };
                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public <C> IConverter<C> getConverter(Class<C> type) {
                        if (LocalDate.class.isAssignableFrom(type)) {
                            return (IConverter<C>) new LocalDateConverter() {
                                @Override
                                public DateTimeFormatter getDateTimeFormatter(
                                        Locale locale) {
                                    return DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                }
                            };
                        }
                        return super.getConverter(type);
                    }
                }.setRequired(true));

        filterForm.add(new TextField<>("endDate", new PropertyModel<>(this, "filterEndDate")) {
            @Override
            protected String[] getInputTypes() {
                return new String[] { "date" };
            }

            @Override
            @SuppressWarnings("unchecked")
            public <C> IConverter<C> getConverter(Class<C> type) {
                if (LocalDate.class.isAssignableFrom(type)) {
                    return (IConverter<C>) new LocalDateConverter() {
                        @Override
                        public DateTimeFormatter getDateTimeFormatter(Locale locale) {
                            return DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        }
                    };
                }
                return super.getConverter(type);
            }
        }.setRequired(true));

        add(filterForm);

        // === New Income Form ===
        Form<Income> form = new Form<Income>("form", new CompoundPropertyModel<>(newIncome)) {
            @Override
            protected void onSubmit() {
                try {
                    incomeService.save(getModelObject());
                    getSession().success("Income saved successfully!");
                    setResponsePage(IncomesPage.class);
                } catch (Exception e) {
                    error("Error saving income: " + e.getMessage());
                }
            }
        };

        form.add(new TextField<>("name").setRequired(true));
        form.add(new TextArea<>("description"));

        // Value field with Money Mask and Converter
        TextField<BigDecimal> valueField = new TextField<>("value", BigDecimal.class) {
            @Override
            @SuppressWarnings("unchecked")
            public <C> IConverter<C> getConverter(Class<C> type) {
                if (BigDecimal.class.isAssignableFrom(type)) {
                    return (IConverter<C>) new MoneyConverter();
                }
                return super.getConverter(type);
            }
        };
        valueField.setRequired(true);
        valueField.add(new org.apache.wicket.behavior.AttributeAppender("class", " money"));
        form.add(valueField);

        form.add(new TextField<>("date", LocalDate.class) {
            @Override
            protected String[] getInputTypes() {
                return new String[] { "date" };
            }

            @Override
            @SuppressWarnings("unchecked")
            public <C> IConverter<C> getConverter(Class<C> type) {
                if (LocalDate.class.isAssignableFrom(type)) {
                    return (IConverter<C>) new LocalDateConverter() {
                        @Override
                        public DateTimeFormatter getDateTimeFormatter(Locale locale) {
                            return DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        }
                    };
                }
                return super.getConverter(type);
            }
        }.setRequired(true));

        add(form);

        // === List ===
        LoadableDetachableModel<List<Income>> listModel = new LoadableDetachableModel<List<Income>>() {
            @Override
            protected List<Income> load() {
                // Use the filter dates
                return incomeService.findAll(filterStartDate, filterEndDate);
            }
        };

        add(new ListView<Income>("incomes", listModel) {
            @Override
            protected void populateItem(ListItem<Income> item) {
                Income income = item.getModelObject();
                item.add(new Label("name", income.getName()));

                // Format Value: R$ 1.445,76
                NumberFormat nf = NumberFormat
                        .getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
                item.add(new Label("value", nf.format(income.getValue())));

                // Format Date: dd/MM/yyyy
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                item.add(new Label("date", income.getDate().format(dtf)));
            }
        });
    }
}
