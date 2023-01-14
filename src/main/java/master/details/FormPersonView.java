//package master.details;
//
//import java.sql.Date;
//import java.text.SimpleDateFormat;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
//import com.vaadin.flow.component.Focusable;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.datepicker.DatePicker;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.grid.editor.Editor;
//import com.vaadin.flow.component.html.H1;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.IntegerField;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.binder.BeanValidationBinder;
//import com.vaadin.flow.data.binder.Binder;
//import com.vaadin.flow.router.BeforeEvent;
//import com.vaadin.flow.router.HasUrlParameter;
//import com.vaadin.flow.router.OptionalParameter;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import org.bag.web.*;
//
//import BagDetails.entities.Bag;
//import BagDetails.entities.Person;
//
//@PageTitle("persons")
//@Route(value = "persons", layout = MainView.class)
//public class FormPersonView extends VerticalLayout implements HasUrlParameter<Integer>{
//		private static final long serialVersionUID = 1L;
//		
//		// Definire model date
//		private EntityManager em;
//		private Person 		person = null;
//		private Binder<Person> binder = new BeanValidationBinder<>(Person.class);
//		
//		// Definire componente view
//		// Definire Form-Master
//		private VerticalLayout  formLayoutToolbar;
//		private H1 				titluForm 	= new H1("Form Persons ");
//		private IntegerField 	id 	= new IntegerField("ID person:");
//		private TextField 		firstName = new TextField("First name: ");
//		//private TextField 		lastName = new TextField("Last name: ");
//		//private DatePicker 		dateOfBirth 	= new DatePicker("Date of birth:");
//		
//		// Definire componente actiuni Form-Master-Controller
//		private Button 			cmdAdaugare = new Button("Adauga");
//	    private Button 			cmdSterge 	= new Button("Sterge");
//	    private Button 			cmdAbandon 	= new Button("Abandon");
//	    private Button 			cmdSalveaza = new Button("Salveaza");
//
//	 // Definire Grid-Details
//		private Grid<Bag> 	bagDetailGrid = new Grid<>(Bag.class);
//		private Button 			cmdAdaugaBag  = new Button("Adauga persoana");
//		private Button 			cmdStergeBag  = new Button("Sterge persoana");
//	
//		// Start Form
//		public FormPersonView() {
//			//
//			initDataModel();
//			//
//			initViewLayout();
//			//
//			initControllerActions();
//		}
//		
//		// Navigation Management
//	    @Override
//	    public void setParameter(BeforeEvent event,
//	                             @OptionalParameter Integer id) {
//	        System.out.println("Person ID: " + id);
//	        if (id != null) {
//	            // EDIT Item
//	            this.person = em.find(Person.class, id);
//	            System.out.println("Selected person to edit:: " + person);
//	            if (this.person == null) {
//	                System.out.println("ADD person:: " + person);
//	                // NEW Item
//	                this.adaugaPersoana();
//	                this.person.setId(id);
//	                this.person.setFirstName("New Person " + id);
//	            }
//	        }
//	        this.refreshForm();   
//	    }
//	 // init Data Model
//	    private void initDataModel(){
//	        System.out.println("DEBUG START FORM >>>  ");
//
//	        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
//	        this.em = emf.createEntityManager();
//	        this.person = em
//	                .createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class)
//	                .getResultStream().findFirst().get();
//
//	        binder.forField(id).bind("id");
//	        binder.forField(firstName).bind("firstName");
//	        //
//	        refreshForm();
//	    }
//	    
//	    // init View Model 
//		private void initViewLayout() {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//			// Form-Master-Details -----------------------------------//
//			// Form-Master
//			FormLayout formLayout = new FormLayout();
//			formLayout.add(id, firstName/*, lastName*/);
//			formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
//			formLayout.setMaxWidth("400px");
//			// Toolbar-Actions-Master
//			HorizontalLayout actionToolbar = 
//					new HorizontalLayout(cmdAdaugare, cmdSterge, cmdAbandon, cmdSalveaza);
//			actionToolbar.setPadding(false);
//			// Grid details
//			bagDetailGrid.setColumns("bagId", "size", "culoare", "greutate");
//			
////			bagDetailGrid
////				.addColumn(r -> dateFormat.format(r.getDataPublicare()))
////				.setHeader("Data Publicare")
////				.setKey("dataPublicare");
////			//
//			bagDetailGrid.getColumns().forEach(col -> col.setAutoWidth(true));
//			//
//			initDetailsGridEditor();
//			//
//			HorizontalLayout gridDetailsToolbar = 
//					new HorizontalLayout(cmdAdaugaBag, cmdStergeBag);
//			// Grid Details Layout
//			VerticalLayout bagDetailGridLayout = new VerticalLayout(bagDetailGrid, gridDetailsToolbar);
//			
//			// 
//			this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar, bagDetailGridLayout);
//			// ---------------------------
//			this.add(titluForm, formLayoutToolbar);
//			//
//		}
//	    
//		// init Controller components
//		private void initControllerActions() {
//			// Transactional Master Actions
//			cmdAdaugare.addClickListener(e -> {
//				adaugaPersoana();
//				refreshForm();
//				//this.switchView();
//			});
//			cmdSterge.addClickListener(e -> {
//				stergePersoana(); 
//				// Navigate back to NavigableGridPersons
//				this.getUI().ifPresent(ui -> ui.navigate(
//						NavigableGridPersonView.class)
//				);
//			});
//			cmdAbandon.addClickListener(e -> {
//				// Navigate back to NavigableGridPersons 
//				this.getUI().ifPresent(ui -> ui.navigate(
//						NavigableGridPersonView.class, this.person.getId())
//				);
//			});
//			cmdSalveaza.addClickListener(e -> {
//				salveazaPersoana();
//				// refreshForm();
//				// Navigate back to NavigableGridPersons
//				this.getUI().ifPresent(ui -> ui.navigate(
//						NavigableGridPersonView.class, this.person.getId())
//				);
//			});
//			// Details Action
//			cmdAdaugaBag.addClickListener(e -> adaugaBag());
//			cmdStergeBag.addClickListener(e -> stergeBag());
//		}
//
//		// Details Actions
//		public void adaugaBag() {
//			Bag newBag = new Bag(null, "R-NEW", new Date(), this.person);
//			this.person.adaugaBag(newBag);
//			updateReleaseDetailGrid();
//			//
//			this.bagDetailGrid.asSingleSelect().setValue(newRelease);
//			this.bagDetailGrid.getEditor().editItem(newRelease);
//			//
//			Focusable gridColumnEditor = (Focusable) this.bagDetailGrid.getColumnByKey("numeCod").getEditorComponent();
//			gridColumnEditor.focus();
//		}
//	    
//		public void stergeRelease() {
//			Release curentRelease = bagDetailGrid.asSingleSelect().getValue();
//			if (curentRelease != null) {
//				this.person.getReleases().remove(curentRelease);
//				updateReleaseDetailGrid();
//			}
//		}
//		//
//		private void refreshForm() {
//			System.out.println("Persoana curenta: " + this.person);
//			if (this.person != null) {
//				binder.setBean(this.person);
//				updateReleaseDetailGrid();
//			}
//		}
//		
//		private void updateReleaseDetailGrid() {
//			if (this.person != null && this.person.getReleases() != null) {
//				bagDetailGrid.setItems(this.person.getReleases());
//			}
//		}
//	    
//		private void initDetailsGridEditor() {
//			// Init grid editor form
//			Binder<Release> gridBinder = new Binder<>(Release.class);
//			Editor<Release> gridEditor = bagDetailGrid.getEditor();
//			gridEditor.setBinder(gridBinder);
//			// numeCod editor
//			TextField numeCodField = new TextField();
//			numeCodField.setWidthFull();
//			gridBinder.bind(numeCodField, "numeCod");
//			bagDetailGrid.getColumnByKey("numeCod").setEditorComponent(numeCodField);
//			// indicativ editor
//			TextField indicativField = new TextField();
//			indicativField.setWidthFull();
//			gridBinder.bind(indicativField, "indicativ");
//			bagDetailGrid.getColumnByKey("indicativ").setEditorComponent(indicativField);
//			// descriere editor
//			TextField descriereField = new TextField();
//			descriereField.setWidthFull();
//			gridBinder.bind(descriereField, "descriere");
//			bagDetailGrid.getColumnByKey("descriere").setEditorComponent(descriereField);
//			// dataPublicare editor
//			DatePicker 	dataPublicareField 	= new DatePicker();
//			dataPublicareField.setWidthFull();
//			gridBinder.forField(dataPublicareField).withConverter(new LocalDateToDateConverter())
//	    		.bind("dataPublicare");
//			bagDetailGrid.getColumnByKey("dataPublicare").setEditorComponent(dataPublicareField);
//			//
//			bagDetailGrid.addItemDoubleClickListener(e -> {
//			    gridEditor.editItem(e.getItem());
//			    Component editorComponent = e.getColumn().getEditorComponent();
//			    if (editorComponent instanceof Focusable) {
//			        ((Focusable) editorComponent).focus();
//			    }
//			});
//		}
//		// CRUD actions
//		private void adaugaPersoana() {
//			this.person = new Person();
//			this.person.setId(999);
//			this.person.setFirstName("New Person");
//			//this.person.setLastName("New Person");
//			//this.person.setDateOfBirth(new Date());		
//		}
//		private void stergePersoana() {
//			System.out.println("To remove: " + this.person);
//			if (this.em.contains(this.person)) {
//				this.em.getTransaction().begin();
//				this.em.remove(this.person);
//				this.em.getTransaction().commit();
//			}
//		}
//		private void salveazaPersoana() {
//			try {
//				this.em.getTransaction().begin();
//				this.person = this.em.merge(this.person);
//				this.em.getTransaction().commit();
//				System.out.println("Persoana salvata");
//			} catch (Exception ex) {
//				if (this.em.getTransaction().isActive())
//					this.em.getTransaction().rollback();
//				System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
//				throw new RuntimeException(ex.getMessage());
//			}
//		}
//		
//		
//}
