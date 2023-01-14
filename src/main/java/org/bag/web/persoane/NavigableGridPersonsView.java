package org.bag.web.persoane;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.bag.web.MainView;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import BagDetails.entities.Person;

@PageTitle("persons")
@Route(value = "persons", layout = MainView.class)
public class NavigableGridPersonsView extends VerticalLayout implements HasUrlParameter<Integer>{
    private static final long serialVersionUID = 1L;

    // Definire model date
    private EntityManager em;
    private List<Person> 	persons = new ArrayList<>();
    private Person 			person = null;
    private Binder<Person> 	binder = new BeanValidationBinder<>(Person.class);

    // Definire componente view
    private H1 				titluForm 	= new H1("Lista Persons");
    // Definire componente suport navigare
    private VerticalLayout  gridLayoutToolbar;
    private TextField 		filterText = new TextField();
    private Button 			cmdEditClient = new Button("Editeaza person...");
    private Button 			cmdAdaugaClient = new Button("Adauga person...");
    private Button 			cmdStergeClient = new Button("Sterge person");
    private Grid<Person> 	grid = new Grid<>(Person.class);

    // Start Form
    public NavigableGridPersonsView() {
        //
        initDataModel();
        //
        initViewLayout();
        //
        initControllerActions();
    }
    // Navigation Management
    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter Integer id) {
        if (id != null) {
            this.person = em.find(Person.class, id);
            System.out.println("Back person: " + person);
            if (this.person == null) {
                // DELETED Item
                if (!this.persons.isEmpty())
                    this.person = this.persons.get(0);
            }
            // else: EDITED or NEW Item
        }
        this.refreshForm();

    }
    // init Data Model
    private void initDataModel(){
        System.out.println("DEBUG START FORM >>>  ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();

        List<Person> lst = em
                .createQuery("SELECT c FROM Person c ORDER BY c.id", Person.class)
                .getResultList();
        persons.addAll(lst);

        grid.setItems(this.persons);
        binder.setBean(this.person);
        grid.asSingleSelect().setValue(this.person);
    }

    // init View Model
    private void initViewLayout() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Layout navigare -------------------------------------//
        // Toolbar navigare
        filterText.setPlaceholder("Filter by nume...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        HorizontalLayout gridToolbar = new HorizontalLayout(filterText,
                cmdEditClient, cmdAdaugaClient, cmdStergeClient);
        // Grid navigare
        grid.setColumns("id", "firstName");
        grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");
        // Init Layout navigare
        gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);
        // ---------------------------
        this.add(titluForm, gridLayoutToolbar);
        //
    }

    // init Controller components
    private void initControllerActions() {
        // Navigation Actions
        filterText.addValueChangeListener(e -> updateList());
        cmdEditClient.addClickListener(e -> {
            editClient();
        });
        cmdAdaugaClient.addClickListener(e -> {
            adaugaClient();
        });
        cmdStergeClient.addClickListener(e -> {
            stergeClient();
            refreshForm();
        });
    }

    //
    private Component createGridActionsButtons(Person item) {
        //
        Button cmdEditItem = new Button("Edit");
        cmdEditItem.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editClient();
        });
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {
            System.out.println("Sterge item: " + item);
            grid.asSingleSelect().setValue(item);
            stergeClient();
            refreshForm();
        }	);
        //
        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }
    //
    private void editClient() {
        this.person = this.grid.asSingleSelect().getValue();
        System.out.println("Selected client:: " + person);
        if (this.person != null) {
            binder.setBean(this.person);
            this.getUI().ifPresent(ui -> ui.navigate(
                    FormPersoanaView.class, this.person.getId())
            );
        }
    }
    //
    private void updateList() {
        try {
            List<Person> lstPersonsFiltrate = this.persons;

            if (filterText.getValue() != null) {
            	lstPersonsFiltrate = this.persons.stream()
                        .filter(c -> c.getFirstName()
                                .contains(filterText.getValue()))
                        .collect(Collectors.toList());
//

                grid.setItems(lstPersonsFiltrate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    private void refreshForm() {
        System.out.println("Person curent: " + this.person);
        if (this.person != null) {
            grid.setItems(this.persons);
            binder.setBean(this.person);
            grid.select(this.person);
        }
    }

    // CRUD actions
    private void adaugaClient() {
        this.getUI().ifPresent(ui -> ui.navigate(FormPersoanaView.class, 999));
    }

    private void stergeClient() {
        this.person = this.grid.asSingleSelect().getValue();
        System.out.println("To remove: " + this.person);
        this.persons.remove(this.person);
        if (this.em.contains(this.person)) {
            this.em.getTransaction().begin();
            this.em.remove(this.person);
            this.em.getTransaction().commit();
        }

        if (!this.persons.isEmpty())
            this.person = this.persons.get(0);
        else
            this.person = null;
    }
}
