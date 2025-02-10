package com.manulife.hometest.ui;

import com.manulife.hometest.entity.User;
import com.manulife.hometest.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.html.H1;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Route("/")
public class UserView extends VerticalLayout {

    private final UserService userService;

    private Grid<User> grid = new Grid<>(User.class);
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private TextField email = new TextField("Email");
    private DatePicker dateOfBirth = new DatePicker("Date of Birth");
    private ComboBox<String> gender = new ComboBox<>("Gender");
    private TextField searchField = new TextField("Search by ID");

    private Button saveButton = new Button("Save");
    private Button editButton = new Button("Edit");
    private Button deleteButton = new Button("Delete");
    private Button generateReportButton = new Button("Generate Report");

    private User selectedUser;

    @Autowired
    public UserView(UserService userService) {
        this.userService = userService;

        H1 title = new H1("Manulife Hometest");
        title.getStyle().set("color", "#15A858")
                .set("text-align", "center")
                .set("font-size", "30px")
                .set("margin-bottom", "20px");

        configureGrid();
        configureForm();

        generateReportButton.addClickListener(e -> generateReport());
        searchField.addValueChangeListener(e -> searchUserById(e.getValue()));

        dateOfBirth.setPlaceholder("DD-MM-YYYY");
        dateOfBirth.setHelperText("Format: DD-MM-YYYY");

        gender.setItems("Male", "Female");
        gender.setPlaceholder("Select Gender");

        FormLayout formLayout = new FormLayout(
                firstName,
                lastName,
                email,
                dateOfBirth,
                gender
        );
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        HorizontalLayout buttonLayout = new HorizontalLayout(
                saveButton,
                editButton,
                deleteButton,
                generateReportButton
        );
        buttonLayout.setSpacing(true);

        HorizontalLayout searchLayout = new HorizontalLayout(searchField);
        searchLayout.setWidthFull();

        add(
                title,
                formLayout,
                buttonLayout,
                searchLayout,
                grid
        );
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setSizeFull();

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("id", "firstName", "lastName", "email", "dateOfBirth", "gender");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(event -> toggleButtons());
    }

    private void configureForm() {
        saveButton.addClickListener(e -> saveUser());
        editButton.addClickListener(e -> editUser());
        deleteButton.addClickListener(e -> deleteUser());
    }

    private void toggleButtons() {
        Set<User> selectedUsers = grid.getSelectedItems();

        editButton.setEnabled(selectedUsers.size() == 1);
        deleteButton.setEnabled(!selectedUsers.isEmpty());
    }

    private void saveUser() {
        if (selectedUser == null) {
            selectedUser = new User();
        }
        selectedUser.setFirstName(firstName.getValue());
        selectedUser.setLastName(lastName.getValue());
        selectedUser.setEmail(email.getValue());

        selectedUser.setDateOfBirth(dateOfBirth.getValue() != null ? dateOfBirth.getValue().toString() : null);

        selectedUser.setGender(gender.getValue());

        if (selectedUser.getId() == null) {
            userService.saveUser(selectedUser);
        } else {
            userService.updateUser(selectedUser.getId(), selectedUser);
        }
        updateList();
        clearForm();
    }

    private void editUser(){
        Set<User> selectedUsers = grid.getSelectedItems();

        if (selectedUsers.size() == 1) {
            selectedUser = selectedUsers.iterator().next();
            populateForm(selectedUser);
        }
    }


    private void deleteUser() {
        Collection<User> selectedUsers = grid.getSelectedItems();
        if (!selectedUsers.isEmpty()) {
            selectedUsers.forEach(user -> userService.deleteUser(user.getId()));
            updateList();
            clearForm();
        }
    }

    private void populateForm(User user) {
        if (user != null) {
            selectedUser = user;
            firstName.setValue(user.getFirstName());
            lastName.setValue(user.getLastName());
            email.setValue(user.getEmail());

            if (user.getDateOfBirth() != null) {
                dateOfBirth.setValue(LocalDate.parse(user.getDateOfBirth()));
            }


            gender.setValue(user.getGender());
        } else {
            clearForm();
        }
    }

    private void clearForm() {
        selectedUser = null;
        firstName.clear();
        lastName.clear();
        email.clear();
        dateOfBirth.clear();
        gender.clear();
    }

    private void updateList() {
        grid.setItems(userService.getAllUsers());
    }

    private void searchUserById(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            try {
                Long userId = Long.parseLong(keyword);
                userService.getUserById(userId).ifPresent(user -> grid.setItems(user));
            } catch (NumberFormatException e) {
                grid.setItems(userService.getAllUsers().stream()
                        .filter(user -> user.getFirstName().toLowerCase().contains(keyword.toLowerCase())
                                || user.getLastName().toLowerCase().contains(keyword.toLowerCase())
                                || user.getGender().equalsIgnoreCase(keyword))
                        .toList());
            }
        } else {
            updateList();
        }
    }

    private void generateReport() {
        Set<User> selectedUsers = grid.getSelectedItems();

        if (!selectedUsers.isEmpty()) {
            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/reports/user-report?ids=");

            selectedUsers.forEach(user -> {
                urlBuilder.append(user.getId()).append(",");
            });

            String url = urlBuilder.toString();
            url = url.substring(0, url.length() - 1);
            UI.getCurrent().getPage().open(url);
        } else {
            String url = "http://localhost:8080/reports/user-report";
            UI.getCurrent().getPage().open(url);
        }
    }
}
