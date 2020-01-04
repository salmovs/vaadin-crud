package hello;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

@Route
public class MainView extends VerticalLayout {

// old
    /*public MainView() {
        add(new Button("Click me", e -> Notification.show("Hello Spring+Vaadin user!")));
    }*/
//
    private final CustomerRepository repo;
    private final CustomerEditor editor;

    final Grid<Customer> grid;
    final TextField filter;
    private final Button addNewBtn;

    public MainView(CustomerRepository repo, CustomerEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New Button", VaadinIcon.PLUS.create());
        //add(grid);

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter,addNewBtn);
        add(actions, grid, editor);


/*        TextField filter = new TextField();
        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));
        add(filter, grid);*/

        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "lastName");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filter by Last name");

        //Hook logic to components

        //Replace listing witch filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        //Connect selected Customers the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("","")));

        //listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(()-> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        // Initialize listing
        listCustomers(null);

        // old variant
        //listCustomers(filter.getValue());
    }

/*    private void listCustomers() {
        grid.setItems(repo.findAll());
    }*/

    // tag::listCustomers[]
    void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        }
        else {
            grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }
    //end::listCustomers

}
