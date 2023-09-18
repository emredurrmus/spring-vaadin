package com.example.application.views.list;

import java.util.Collections;

import org.springframework.boot.context.config.ConfigDataEnvironmentUpdateListener;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "")
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout { 
    
	Grid<Contact> grid = new Grid<>(Contact.class); 
    TextField filterText = new TextField();
    ContactForm form;
    
    private CrmService crmService;
    
  
    public ListView(CrmService crmService) {
    	
    	this.crmService = crmService;
    	
        addClassName("list-view"); 
        setSizeFull();
        
        configureGrid();        
        configureForm();

        
        add(
        		getToolbar(), 
        		getContact()
        		
        		);
        
        updateList();
        
        
    }

    

	private void updateList() {
		grid.setItems(crmService.findAllContacts(filterText.getValue()));
		
	
	}



	private Component getContact() {
    	
		HorizontalLayout content = new HorizontalLayout(grid,form);
		content.setFlexGrow(2, grid); 
		content.setFlexGrow(1, form);
		content.addClassName("content");
		content.setSizeFull();
    	
		return content;
	}

	private void configureForm() {
		form = new ContactForm(crmService.findAllCompanies(), crmService.findAllStatuses());
		form.setWidth("25em");
		
	}

	private void configureGrid() {
        grid.addClassNames("contact-grid"); 
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email"); 
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status"); 
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true)); 
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");

        var toolbar = new HorizontalLayout(filterText, addContactButton); 
        toolbar.addClassName("toolbar"); 
        return toolbar;
    }
}