package com.hotel.UI;

import com.hotel.controller.CustomerController;
import com.hotel.model.Customer;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomerTab {
    private CustomerController controller;

    public CustomerTab(CustomerController controller) {
        this.controller = controller;
    }

    public Tab createTab() {
        Tab tab = new Tab("👤  Customers");
        tab.setClosable(false);

        Label header = new Label("Customer Management");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        header.setStyle("-fx-text-fill: #00d4ff;");

        // ── Form ──────────────────────────────────────────────
        TextField tfName = new TextField();
        tfName.setPromptText("Customer Name");
        tfName.setPrefWidth(260);

        TextField tfContact = new TextField();
        tfContact.setPromptText("Contact Number");
        tfContact.setPrefWidth(260);

        CheckBox cbLoyalty = new CheckBox(
            "⭐ Loyalty Program Member (20% discount on all bookings)");
        cbLoyalty.setStyle(
            "-fx-text-fill: #FFD700; -fx-font-size: 14px;" +
            "-fx-font-weight: bold;");

        Button btnAdd = new Button("➕  Add Customer");
        btnAdd.setStyle(
            "-fx-background-color: #00d48a; -fx-text-fill: #0a0f1e;" +
            "-fx-font-weight: bold; -fx-background-radius: 6;" +
            "-fx-padding: 10 22; -fx-cursor: hand;");

        Label lblMsg = new Label();
        lblMsg.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        GridPane form = new GridPane();
        form.setHgap(16);
        form.setVgap(12);
        form.setPadding(new Insets(16));
        form.setStyle(
            "-fx-background-color: #060b18; -fx-background-radius: 8;" +
            "-fx-border-color: #1a3a5c; -fx-border-radius: 8;");
        form.add(new Label("Name:"),    0, 0); form.add(tfName,    1, 0);
        form.add(new Label("Contact:"), 0, 1); form.add(tfContact, 1, 1);
        form.add(new Label("Loyalty:"), 0, 2); form.add(cbLoyalty, 1, 2);
        form.add(btnAdd,  1, 3);
        form.add(lblMsg,  0, 4, 2, 1);

        // ── Search bar ────────────────────────────────────────
        TextField tfSearch = new TextField();
        tfSearch.setPromptText("🔍  Search by name or contact...");
        tfSearch.setPrefWidth(340);

        HBox searchRow = new HBox(12, new Label("Search:"), tfSearch);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        // ── Table ─────────────────────────────────────────────
        TableView<Customer> table = new TableView<>();
        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setFixedCellSize(36);

        TableColumn<Customer, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new javafx.beans.property
            .SimpleIntegerProperty(d.getValue().getCustomerId()).asObject());

        TableColumn<Customer, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(d.getValue().getName()));

        TableColumn<Customer, String> colContact = new TableColumn<>("Contact");
        colContact.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(d.getValue().getContact()));

        TableColumn<Customer, String> colRoom = new TableColumn<>("Allocated Room");
        colRoom.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(
                d.getValue().getAllocatedRoomNumber() == -1
                ? "None"
                : "Room " + d.getValue().getAllocatedRoomNumber()));

        // Color-coded loyalty column
        TableColumn<Customer, String> colLoyalty = new TableColumn<>("Loyalty");
        colLoyalty.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(
                d.getValue().isLoyaltyMember() ? "⭐ Member" : "—"));
        colLoyalty.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle(item.contains("Member")
                        ? "-fx-text-fill: #FFD700; -fx-font-weight: bold;" +
                          "-fx-font-size: 14px;"
                        : "-fx-text-fill: #4a6fa5; -fx-font-size: 14px;");
                }
            }
        });

        table.getColumns().add(colId);
        table.getColumns().add(colName);
        table.getColumns().add(colContact);
        table.getColumns().add(colRoom);
        table.getColumns().add(colLoyalty);

        // ── Live search filter ────────────────────────────────
        Runnable refreshTable = () -> {
            String q = tfSearch.getText().toLowerCase().trim();
            var list = controller.getAllCustomers().stream()
                .filter(c -> q.isEmpty()
                    || c.getName().toLowerCase().contains(q)
                    || c.getContact().contains(q))
                .toList();
            table.setItems(FXCollections.observableArrayList(list));
        };

        tfSearch.textProperty().addListener(
            (obs, oldVal, newVal) -> refreshTable.run());
        refreshTable.run();

        // ── Add customer action ───────────────────────────────
        btnAdd.setOnAction(e -> {
            if (tfName.getText().isBlank() || tfContact.getText().isBlank()) {
                lblMsg.setText("❌ Please fill in all fields.");
                lblMsg.setStyle("-fx-text-fill: #ff4757;");
                return;
            }
            controller.addCustomer(
                tfName.getText().trim(),
                tfContact.getText().trim(),
                cbLoyalty.isSelected()
            );
            if (cbLoyalty.isSelected()) {
                lblMsg.setText(
                    "⭐ Loyalty member added! 20% discount on all bookings.");
                lblMsg.setStyle(
                    "-fx-text-fill: #FFD700; -fx-font-weight: bold;");
            } else {
                lblMsg.setText("✅ Customer added successfully!");
                lblMsg.setStyle("-fx-text-fill: #00d48a;");
            }
            tfName.clear();
            tfContact.clear();
            cbLoyalty.setSelected(false);
            refreshTable.run();
        });

        // ── Section label ─────────────────────────────────────
        Label tblHeader = new Label("All Customers");
        tblHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        tblHeader.setStyle("-fx-text-fill: #7ab8d4;");

        // ── Loyalty info note ─────────────────────────────────
        Label loyaltyNote = new Label(
            "ℹ️  Loyalty members receive a 20% discount " +
            "automatically applied at the time of booking.");
        loyaltyNote.setStyle(
            "-fx-text-fill: #4a6fa5; -fx-font-size: 13px;" +
            "-fx-font-style: italic;");
        loyaltyNote.setWrapText(true);

        // ── Final layout ──────────────────────────────────────
        VBox layout = new VBox(16,
            header,
            form,
            loyaltyNote,
            new Separator(),
            tblHeader,
            searchRow,
            table
        );
        layout.setPadding(new Insets(28));
        tab.setContent(layout);
        return tab;
    }
}