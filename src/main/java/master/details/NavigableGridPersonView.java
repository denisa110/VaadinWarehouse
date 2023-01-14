//package master.details;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
//import org.bag.web.MainView;
//
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.html.H1;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.binder.BeanValidationBinder;
//import com.vaadin.flow.data.binder.Binder;
//import com.vaadin.flow.data.value.ValueChangeMode;
//import com.vaadin.flow.router.BeforeEvent;
//import com.vaadin.flow.router.HasUrlParameter;
//import com.vaadin.flow.router.OptionalParameter;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//
//import BagDetails.entities.Person;
//
//@PageTitle("personsreleases")
//@Route(value = "personsreleases", layout = MainView.class)
//public class NavigableGridPersonView extends VerticalLayout implements HasUrlParameter<Integer> {
//	private static final long serialVersionUID = 1L;
//
//	// Definire model date
//	private EntityManager em;
//	private List<Person> persons = new ArrayList<>();
//	private Person person = null;
//	private Binder<Person> binder = new BeanValidationBinder<>(Person.class);
//
//	// Definire componente view
//	private H1 titluForm = new H1("Lista Person-Releases");
//	// Definire componente suport navigare
//	private VerticalLayout gridLayoutToolbar;
//	private TextField filterText = new TextField();
//	private Button cmdEditPersoana = new Button("Editeaza persoana...");
//	private Button cmdAdaugaPersoana = new Button("Adauga persoana...");
//	private Button cmdStergePersoana = new Button("Sterge persoana");
//	private Grid<Person> grid = new Grid<>(Person.class);
//
//	// Form Master-Details
//
//	// Start Form
//	public NavigableGridPersonView() {
//		//
//		initDataModel();
//		//
//		initViewLayout();
//		//
//		initControllerActions();
//	}
//
//	// Navigation Management
//	@Override
//	public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
//		if (id != null) {
//			this.person = em.find(Person.class, id);
//			System.out.println("Back person: " + person);
//			if (this.person == null) {
//				// DELETED Item
//				if (!this.persons.isEmpty())
//					this.person = this.persons.get(0);
//			}
//			// else: EDITED or NEW Item
//		}
//		this.refreshForm();
//
//	}
//
//	// init Data Model
//	private void initDataModel() {
//		System.out.println("DEBUG START FORM >>>  ");
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
//		em = emf.createEntityManager();
//
//		List<Person> lst = em.createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class).getResultList();
//		persons.addAll(lst);
//
////			if (lst != null && !lst.isEmpty()){
////				Collections.sort(this.persons, (p1, p2) -> p1.getId().compareTo(p2.getId()));
////				this.person = persons.get(0);
////				System.out.println("DEBUG: person init >>> " + person.getId());
////				this.person.getReleases()
////					.sort((r1, r2) -> r1.getIdRelease().compareTo(r2.getIdRelease()));
////			}
//		//
//		grid.setItems(this.persons);
//		binder.setBean(this.person);
//		grid.asSingleSelect().setValue(this.person);
//	}
//
//	// init View Model
//	private void initViewLayout() {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//		// Layout navigare -------------------------------------//
//		// Toolbar navigare
//		filterText.setPlaceholder("Filter by name...");
//		filterText.setClearButtonVisible(true);
//		filterText.setValueChangeMode(ValueChangeMode.LAZY);
//		HorizontalLayout gridToolbar = new HorizontalLayout(filterText, cmdEditPersoana, cmdAdaugaPersoana,
//				cmdStergePersoana);
//		// Grid navigare
//		grid.setColumns("id", "firstName");
//		grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");
//		// Init Layout navigare
//		gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);
//		// ---------------------------
//		this.add(titluForm, gridLayoutToolbar);
//		//
//	}
//
//	// init Controller components
//	private void initControllerActions() {
//		// Navigation Actions
//		filterText.addValueChangeListener(e -> updateList());
//		cmdEditPersoana.addClickListener(e -> {
//			editPersoana();
//		});
//		cmdAdaugaPersoana.addClickListener(e -> {
//			adaugaPersoana();
//		});
//		cmdStergePersoana.addClickListener(e -> {
//			stergePersoana();
//			refreshForm();
//		});
//	}
//
//	//
//	private Component createGridActionsButtons(Person item) {
//		//
//		Button cmdEditItem = new Button("Edit");
//		cmdEditItem.addClickListener(e -> {
//			grid.asSingleSelect().setValue(item);
//			editPersoana();
//		});
//		Button cmdDeleteItem = new Button("Sterge");
//		cmdDeleteItem.addClickListener(e -> {
//			System.out.println("Sterge item: " + item);
//			grid.asSingleSelect().setValue(item);
//			stergePersoana();
//			refreshForm();
//		});
//		//
//		return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
//	}
//
//	//
//	private void editPersoana() {
//		this.person = this.grid.asSingleSelect().getValue();
//		System.out.println("Selected person:: " + person);
//		if (this.person != null) {
//			binder.setBean(this.person);
//			this.getUI().ifPresent(ui -> ui.navigate(FormPersonView.class, this.person.getId()));
//		}
//	}
//
//	//
//	private void updateList() {
//		try {
//			List<Person> lstPersoaneFiltrate = this.persons;
//
//			if (filterText.getValue() != null) {
//				lstPersoaneFiltrate = this.persons.stream()
//						.filter(p -> p.getFirstName().contains(filterText.getValue())).toList();
//
//				grid.setItems(lstPersoaneFiltrate);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	//
//	private void refreshForm() {
//		System.out.println("Persoana curenta: " + this.person);
//		if (this.person != null) {
//			grid.setItems(this.persons);
//			binder.setBean(this.person);
//			grid.select(this.person);
//		}
//	}
//
//	// CRUD actions
//	private void adaugaPersoana() {
//		this.getUI().ifPresent(ui -> ui.navigate(FormPersonView.class, 999));
//	}
//
//	private void stergePersoana() {
//		this.person = this.grid.asSingleSelect().getValue();
//		System.out.println("To remove: " + this.person);
//		this.persons.remove(this.person);
//		if (this.em.contains(this.person)) {
//			this.em.getTransaction().begin();
//			this.em.remove(this.person);
//			this.em.getTransaction().commit();
//		}
//
//		if (!this.persons.isEmpty())
//			this.person = this.persons.get(0);
//		else
//			this.person = null;
//	}
//
//	/*
//	 * public void setParameter(BeforeEvent event,
//	 * 
//	 * @OptionalParameter Integer id) {
//	 * 
//	 * if (id != null) { this.person = em.find(Person.class, id);
//	 * System.out.println("Back person: " + person); if (this.person == null) { //
//	 * NEW or DELETED Item List<Person> lst = em
//	 * .createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class)
//	 * .getResultList(); this.persons = new ArrayList<>(lst); Optional<Person>
//	 * newPerson = this.persons.stream() .filter(p -> p.getId().equals(id))
//	 * .findFirst(); // if (newPerson.isPresent()) { // NEW Item this.person =
//	 * newPerson.get(); System.out.println("NEW Person: " + this.person); }else {
//	 * // DELETED Item if (!this.persons.isEmpty()) this.person =
//	 * this.persons.get(0); } } // else: EDITED Item } this.refreshForm();
//	 * 
//	 * }
//	 * 
//	 */
//
//}
