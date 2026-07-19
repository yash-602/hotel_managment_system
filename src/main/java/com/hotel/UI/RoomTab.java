package com.hotel.UI;

import com.hotel.controller.RoomController;
import com.hotel.model.Room;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RoomTab {
    private RoomController controller;
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin@123";

    public RoomTab(RoomController controller) {
        this.controller = controller;
    }

    public Tab createTab() {
        Tab tab = new Tab("🛏  Rooms");
        tab.setClosable(false);
        tab.setContent(buildLoginScreen(tab));
        return tab;
    }

    // ── Login screen ──────────────────────────────────────────
    private VBox buildLoginScreen(Tab tab) {
        Label lockIcon = new Label("🔒");
        lockIcon.setStyle("-fx-font-size: 48px;");

        Label title = new Label("Restricted Access");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #00d4ff;");

        Label sub = new Label("Enter admin credentials to manage rooms");
        sub.setStyle("-fx-text-fill: #4a6fa5; -fx-font-size: 15px;");

        TextField tfUser = new TextField();
        tfUser.setPromptText("Username");
        tfUser.setMaxWidth(300);
        tfUser.setStyle(
            "-fx-background-color: #060b18; -fx-text-fill: #e0f4ff;" +
            "-fx-border-color: #1a3a5c; -fx-border-radius: 6;" +
            "-fx-background-radius: 6; -fx-padding: 10; -fx-font-size: 15px;");

        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("Password");
        pfPass.setMaxWidth(300);
        pfPass.setStyle(
            "-fx-background-color: #060b18; -fx-text-fill: #e0f4ff;" +
            "-fx-border-color: #1a3a5c; -fx-border-radius: 6;" +
            "-fx-background-radius: 6; -fx-padding: 10; -fx-font-size: 15px;");

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: #ff4757; -fx-font-size: 14px;" +
                         "-fx-font-weight: bold;");

        Button btnLogin = new Button("🔓  Login");
        btnLogin.setStyle(
            "-fx-background-color: #00d4ff; -fx-text-fill: #0a0f1e;" +
            "-fx-font-weight: bold; -fx-background-radius: 6;" +
            "-fx-padding: 10 30; -fx-font-size: 15px; -fx-cursor: hand;");
        btnLogin.setMaxWidth(300);

        btnLogin.setOnAction(e -> {
            String user = tfUser.getText().trim();
            String pass = pfPass.getText().trim();
            if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
                tab.setContent(buildRoomScreen());
            } else {
                lblError.setText("❌ Invalid username or password.");
                pfPass.clear();
            }
        });

        // Allow Enter key to trigger login
        pfPass.setOnAction(e -> btnLogin.fire());
        tfUser.setOnAction(e -> pfPass.requestFocus());

        VBox loginBox = new VBox(16,
            lockIcon, title, sub,
            new Separator(),
            new Label("Username:"), tfUser,
            new Label("Password:"), pfPass,
            lblError,
            btnLogin
        );
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setMaxWidth(360);
        loginBox.setPadding(new Insets(40));
        loginBox.setStyle(
            "-fx-background-color: #060b18;" +
            "-fx-border-color: #1a3a5c;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;");

        // Center the login box
        StackPane wrapper = new StackPane(loginBox);
        wrapper.setStyle("-fx-background-color: #0d1426;");
        wrapper.setPadding(new Insets(60));

        VBox outer = new VBox(wrapper);
        outer.setAlignment(Pos.CENTER);
        outer.setPadding(new Insets(28));
        return outer;
    }

    // ── Room management screen (shown after login) ────────────
    private VBox buildRoomScreen() {
        Label header = new Label("🛏  Room Management");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        header.setStyle("-fx-text-fill: #00d4ff;");

        Label authBadge = new Label("🔓 Logged in as Admin");
        authBadge.setStyle(
            "-fx-text-fill: #00d48a; -fx-font-size: 13px;" +
            "-fx-font-weight: bold;");

        HBox headerRow = new HBox(16, header, authBadge);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // Form
        TextField tfNumber = new TextField();
        tfNumber.setPromptText("Room Number");
        tfNumber.setPrefWidth(240);

        ComboBox<String> cbType = new ComboBox<>();
        cbType.getItems().addAll("Single", "Double", "Deluxe", "Suite");
        cbType.setValue("Single");
        cbType.setPrefWidth(240);

        TextField tfPrice = new TextField();
        tfPrice.setPromptText("Price per Day");
        tfPrice.setPrefWidth(240);

        Button btnAdd = new Button("➕  Add Room");
        btnAdd.setStyle(
            "-fx-background-color: #00d48a; -fx-text-fill: #0a0f1e;" +
            "-fx-font-weight: bold; -fx-background-radius: 6;" +
            "-fx-padding: 9 22; -fx-cursor: hand;");

        Label lblMsg = new Label();
        lblMsg.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        GridPane form = new GridPane();
        form.setHgap(16); form.setVgap(12);
        form.setPadding(new Insets(16));
        form.setStyle(
            "-fx-background-color: #060b18; -fx-background-radius: 8;" +
            "-fx-border-color: #1a3a5c; -fx-border-radius: 8;");
        form.add(new Label("Room Number:"), 0, 0); form.add(tfNumber, 1, 0);
        form.add(new Label("Room Type:"),   0, 1); form.add(cbType,   1, 1);
        form.add(new Label("Price/Day:"),   0, 2); form.add(tfPrice,  1, 2);
        form.add(btnAdd, 1, 3);
        form.add(lblMsg, 1, 4);

        // Filter row
        ComboBox<String> cbFilter = new ComboBox<>();
        cbFilter.getItems().addAll("All", "Single", "Double", "Deluxe", "Suite");
        cbFilter.setValue("All");
        cbFilter.setPrefWidth(180);

        CheckBox cbAvail = new CheckBox("Available only");

        HBox filterRow = new HBox(16,
            new Label("Filter by type:"), cbFilter, cbAvail);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        filterRow.setPadding(new Insets(8, 0, 8, 0));

        // Table
        TableView<Room> table = new TableView<>();
        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Room, Integer> colNum = new TableColumn<>("Room No");
        colNum.setCellValueFactory(d -> new javafx.beans.property
            .SimpleIntegerProperty(d.getValue().getRoomNumber()).asObject());

        TableColumn<Room, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(d.getValue().getRoomType()));

        TableColumn<Room, String> colPrice = new TableColumn<>("Price/Day");
        colPrice.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty("Rs. " +
                String.format("%.0f", d.getValue().getPricePerDay())));

        TableColumn<Room, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(d.getValue().isAvailable()
                ? "✅ Available" : "🔴 Booked"));
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle(item.contains("Available")
                        ? "-fx-text-fill: #00d48a; -fx-font-weight: bold;" +
                          "-fx-font-size: 15px;"
                        : "-fx-text-fill: #ff4757; -fx-font-weight: bold;" +
                          "-fx-font-size: 15px;");
                }
            }
        });

        table.getColumns().add(colNum);
        table.getColumns().add(colType);
        table.getColumns().add(colPrice);
        table.getColumns().add(colStatus);

        Runnable applyFilter = () -> {
            String typeFilter = cbFilter.getValue();
            boolean availOnly = cbAvail.isSelected();
            var list = controller.getAllRooms().stream()
                .filter(r -> typeFilter.equals("All") ||
                             r.getRoomType().equals(typeFilter))
                .filter(r -> !availOnly || r.isAvailable())
                .toList();
            table.setItems(FXCollections.observableArrayList(list));
        };

        cbFilter.setOnAction(e -> applyFilter.run());
        cbAvail.setOnAction(e -> applyFilter.run());
        applyFilter.run();

        btnAdd.setOnAction(e -> {
            try {
                int num   = Integer.parseInt(tfNumber.getText().trim());
                double price = Double.parseDouble(tfPrice.getText().trim());
                controller.addRoom(num, cbType.getValue(), price);
                lblMsg.setText("✅ Room " + num + " added!");
                lblMsg.setStyle("-fx-text-fill: #00d48a;");
                tfNumber.clear(); tfPrice.clear();
                applyFilter.run();
            } catch (NumberFormatException ex) {
                lblMsg.setText("❌ Invalid number or price.");
                lblMsg.setStyle("-fx-text-fill: #ff4757;");
            } catch (IllegalArgumentException ex) {
                lblMsg.setText("❌ " + ex.getMessage());
                lblMsg.setStyle("-fx-text-fill: #ff4757;");
            }
        });

        Label tblHeader = new Label("All Rooms");
        tblHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        tblHeader.setStyle("-fx-text-fill: #7ab8d4;");

        VBox layout = new VBox(16,
            headerRow, form,
            new Separator(),
            tblHeader, filterRow, table
        );
        layout.setPadding(new Insets(28));
        return layout;
    }
}