package org.bag.web;

import org.bag.web.persoane.FormPersoanaView;
import org.bag.web.persoane.NavigableGridPersonsView;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

/**
 * The main view contains a button and a click listener.
 */
@Route
public class MainView extends VerticalLayout implements RouterLayout {

	public MainView() {
        setMenuBar();
    }

    private void setMenuBar() {
        MenuBar mainMenu = new MenuBar();
        MenuItem homeMenu = mainMenu.addItem("Home");

        homeMenu.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        //
        MenuItem gridFormsClientiMenu = mainMenu.addItem("Persons");
        SubMenu gridFormsClientiMenuBar = gridFormsClientiMenu.getSubMenu();
        gridFormsClientiMenuBar.addItem("List Persons...",
                event -> UI.getCurrent().navigate(NavigableGridPersonsView.class));
        gridFormsClientiMenuBar.addItem("Form Edit Person...",
                event -> UI.getCurrent().navigate(FormPersoanaView.class));

        add(new HorizontalLayout(mainMenu));
    }
}
