package org.bag.web.persoane;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.bag.web.*;
import BagDetails.entities.Person;

@PageTitle("person")
@Route(value = "person", layout = MainView.class)
public class FormPersoanaView extends VerticalLayout implements HasUrlParameter<Integer>{
    private static final long serialVersionUID = 1L;

    // Definire model date
    private EntityManager em;
    private Person person = null;
    private Binder<Person> binder = new BeanValidationBinder<>(Person.class);

    // Definire componente view
    // Definire Form-Master
    private VerticalLayout  formLayoutToolbar;
    private H1 	titluForm 	= new H1("Form Person");
    private IntegerField 	id 	= new IntegerField("ID Person:");
    private TextField 		firstName = new TextField("firstName person: ");
    // Definire componente actiuni Form-Master-Controller
    private Button 			cmdAdaugare = new Button("Adauga");
    private Button 			cmdSterge 	= new Button("Sterge");
    private Button 			cmdAbandon 	= new Button("Abandon");
    private Button 			cmdSalveaza = new Button("Salveaza");

    // Start Form
    public FormPersoanaView() {
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
        System.out.println("Person ID: " + id);
        if (id != null) {
            // EDIT Item
            this.person = em.find(Person.class, id);
            System.out.println("Selected person to edit:: " + person);
            if (this.person == null) {
                System.out.println("ADD person:: " + person);
                // NEW Item
                this.adaugaPersoana();
                this.person.setId(id);
                this.person.setFirstName("New Person " + id);
            }
        }
        this.refreshForm();
    }
    // init Data Model
    private void initDataModel(){
        System.out.println("DEBUG START FORM >>>  ");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        this.em = emf.createEntityManager();
        this.person = em
                .createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class)
                .getResultStream().findFirst().get();

        binder.forField(id).bind("id");
        binder.forField(firstName).bind("firstName");
        //
        refreshForm();
    }

    // init View Model
    private void initViewLayout() {
        // Form-Master-Details -----------------------------------//
        // Form-Master
        FormLayout formLayout = new FormLayout();
        formLayout.add(id, firstName);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        formLayout.setMaxWidth("400px");
        // Toolbar-Actions-Master
        HorizontalLayout actionToolbar =
                new HorizontalLayout(cmdAdaugare, cmdSterge, cmdAbandon, cmdSalveaza);
        actionToolbar.setPadding(false);

        //
        this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar);
        // ---------------------------
        this.add(titluForm, formLayoutToolbar);
        //
    }

    // init Controller components
    private void initControllerActions() {
        // Transactional Master Actions
        cmdAdaugare.addClickListener(e -> {
            adaugaPersoana();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergePersoana();
            // Navigate back to NavigableGridClienteForm
            this.getUI().ifPresent(ui -> ui.navigate(
                    NavigableGridPersonsView.class)
            );
        });
        cmdAbandon.addClickListener(e -> {
            // Navigate back to NavigableGridClienteForm
            this.getUI().ifPresent(ui -> ui.navigate(
                    NavigableGridPersonsView.class, this.person.getId())
            );
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaPersoana();
            // refreshForm();
            // Navigate back to NavigableGridClienteForm
            this.getUI().ifPresent(ui -> ui.navigate(
                    NavigableGridPersonsView.class, this.person.getId())
            );
        });
    }
    //
    private void refreshForm() {
        System.out.println("Person curent: " + this.person);
        if (this.person != null) {
            binder.setBean(this.person);
        }
    }

    // CRUD actions
    private void adaugaPersoana() {
        this.person = new Person();
        this.person.setId(999);
        this.person.setFirstName("First Name");
    }

    private void stergePersoana() {
        System.out.println("To remove: " + this.person);
        if (this.em.contains(this.person)) {
            this.em.getTransaction().begin();
            this.em.remove(this.person);
            this.em.getTransaction().commit();
        }
    }

    private void salveazaPersoana() {
        try {
            this.em.getTransaction().begin();
            this.person = this.em.merge(this.person);
            this.em.getTransaction().commit();
            System.out.println("Person Salvat");
        } catch (Exception ex) {
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }
}
