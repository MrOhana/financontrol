package com.financontrol.wicket.pages;

import com.financontrol.domain.Expense;
import com.financontrol.domain.ExpenseCategory;
import com.financontrol.domain.Goal;
import com.financontrol.repository.ExpenseCategoryRepository;
import com.financontrol.repository.GoalRepository;
import com.financontrol.service.ExpenseService;
import com.financontrol.wicket.converter.MoneyConverter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.LocalDateConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ExpensesPage extends BasePage {

    @SpringBean
    private ExpenseService expenseService;

    @SpringBean
    private ExpenseCategoryRepository categoryRepository;

    @SpringBean
    private GoalRepository goalRepository;

    private Expense newExpense = new Expense();

    private LocalDate filterStartDate = LocalDate.now();
    private LocalDate filterEndDate = LocalDate.now();

    public ExpensesPage() {
        super();

        newExpense.setDate(LocalDate.now());

        // === Filter Form ===
        Form<Void> filterForm = new Form<>("filterForm");
        // styling
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

        // === New Expense Form ===
        Form<Expense> form = new Form<Expense>("form", new CompoundPropertyModel<>(newExpense)) {
            @Override
            protected void onSubmit() {
                try {
                    expenseService.save(getModelObject());
                    getSession().success("Expense saved successfully!");
                    setResponsePage(ExpensesPage.class);
                } catch (Exception e) {
                    error("Error saving expense: " + e.getMessage());
                }
            }
        };

        form.add(new TextField<>("name").setRequired(true));
        form.add(new TextArea<>("description"));

        // Value field with Money Mask and Converter
        TextField<BigDecimal> valueField = new TextField<>("value", BigDecimal.class) {
            @Override
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

        // Date Field with Converter
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

        form.add(new CheckBox("fixed"));

        // Categories Dropdown
        List<ExpenseCategory> categories = categoryRepository.findAll();
        form.add(new DropDownChoice<>("category", categories, new ChoiceRenderer<>("name", "id")));

        // Goal Dropdown (Optional)
        List<Goal> goals = goalRepository.findAll();
        form.add(new DropDownChoice<>("goal", goals, new ChoiceRenderer<>("name", "id")));

        add(form);

        // === List ===
        LoadableDetachableModel<List<Expense>> listModel = new LoadableDetachableModel<List<Expense>>() {
            @Override
            protected List<Expense> load() {
                // Use the filter dates
                return expenseService.findAll(filterStartDate, filterEndDate);
            }
        };

        add(new ListView<Expense>("expenses", listModel) {
            @Override
            protected void populateItem(ListItem<Expense> item) {
                Expense expense = item.getModelObject();
                item.add(new Label("name", expense.getName()));

                // Format Value: R$ 1.445,76
                java.text.NumberFormat nf = java.text.NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                item.add(new Label("value", nf.format(expense.getValue())));

                // Format Date: dd/MM/yyyy
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                item.add(new Label("date", expense.getDate().format(dtf)));

                item.add(new Label("category", expense.getCategory() != null ? expense.getCategory().getName() : "-"));
                item.add(new Label("goal", expense.getGoal() != null ? expense.getGoal().getName() : "-"));
                item.add(new Label("fixed", expense.isFixed() ? "Yes" : "No"));
            }
        });
    }
}
