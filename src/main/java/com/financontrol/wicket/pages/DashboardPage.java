package com.financontrol.wicket.pages;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.financontrol.domain.Expense;
import com.financontrol.service.ExpenseService;

@AuthorizeInstantiation("USER")
public class DashboardPage extends BasePage {

    @SpringBean
    private ExpenseService expenseService;

    public DashboardPage() {
        super();

        // Model to fetch monthly expenses
        IModel<List<Expense>> expensesModel = new LoadableDetachableModel<>() {
            @Override
            protected List<Expense> load() {
                LocalDate today = LocalDate.now();
                return expenseService.findAllInMonth(today);
            }
        };

        // ListView
        add(new ListView<>("expenses", expensesModel) {
            @Override
            protected void populateItem(ListItem<Expense> item) {
                Expense expense = item.getModelObject();

                item.add(new Label("name", expense.getName()));

                // Format Date: dd/MM/yyyy
                java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                item.add(new Label("date", expense.getDate().format(dtf)));

                item.add(new Label("category",
                        expense.getCategory() != null ? expense.getCategory().getName() : ""));
                item.add(new Label("amount",
                        String.format("R$ %.2f", expense.getValue())));
            }
        });

        // Add a label for total amount
        add(new Label("totalAmount",
                new LoadableDetachableModel<String>() {
                    @Override
                    protected String load() {
                        BigDecimal total = expensesModel.getObject().stream()
                                .map(Expense::getValue)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        return String.format("R$ %.2f", total);
                    }
                }));
    }
}
