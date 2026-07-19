package com.hotel.UI;
import javafx.scene.control.ScrollPane;
import com.hotel.controller.*;
import com.hotel.model.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingTab {
    private BookingController bookingController;
    private RoomController roomController;
    private CustomerController customerController;

    public BookingTab(BookingController bc, RoomController rc,
                      CustomerController cc) {
        this.bookingController  = bc;
        this.roomController     = rc;
        this.customerController = cc;
    }

    public Tab createTab() {
        Tab tab = new Tab("📋  Bookings");
        tab.setClosable(false);

        Label header = new Label("Booking Management");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        header.setStyle("-fx-text-fill: #00d4ff;");

        // ── Inputs ────────────────────────────────────────────
        TextField tfCustId = new TextField();
        tfCustId.setPromptText("Customer ID");
        tfCustId.setPrefWidth(240);

        TextField tfRoomNo = new TextField();
        tfRoomNo.setPromptText("Room Number");
        tfRoomNo.setPrefWidth(240);

        DatePicker dpIn  = new DatePicker(LocalDate.now());
        DatePicker dpOut = new DatePicker(LocalDate.now().plusDays(1));
        dpIn.setPrefWidth(240);
        dpOut.setPrefWidth(240);

        // ── Amenities checkboxes ──────────────────────────────
        CheckBox cbFridge     = new CheckBox("🧊 Mini Fridge       +Rs.500");
        CheckBox cbSnackBar   = new CheckBox("🍫 Snack Bar         +Rs.500");
        CheckBox cbExtraBed   = new CheckBox("🛏 Extra Bed         +Rs.1000");
        CheckBox cbToiletries = new CheckBox("🧴 Extra Toiletries  +Rs.1000");

        String cbStyle =
            "-fx-text-fill: #b8d4e8; -fx-font-size: 14px;";
        cbFridge.setStyle(cbStyle);
        cbSnackBar.setStyle(cbStyle);
        cbExtraBed.setStyle(cbStyle);
        cbToiletries.setStyle(cbStyle);

        Label lblAmenities = new Label("Add Amenities:");
        lblAmenities.setStyle("-fx-text-fill: #7ab8d4; -fx-font-size: 14px;" +
                             "-fx-font-weight: bold;");

        Label lblAmenityTotal = new Label("Amenities Cost: Rs. 0");
        lblAmenityTotal.setStyle(
            "-fx-text-fill: #f4a261; -fx-font-size: 13px;" +
            "-fx-font-weight: bold;");

        // Update amenity cost label live
        Runnable updateAmenityTotal = () -> {
            double cost = 0;
            if (cbFridge.isSelected())     cost += 500;
            if (cbSnackBar.isSelected())   cost += 500;
            if (cbExtraBed.isSelected())   cost += 1000;
            if (cbToiletries.isSelected()) cost += 1000;
            lblAmenityTotal.setText("Amenities Cost: Rs. " +
                String.format("%.0f", cost));
        };
        cbFridge.setOnAction(e -> updateAmenityTotal.run());
        cbSnackBar.setOnAction(e -> updateAmenityTotal.run());
        cbExtraBed.setOnAction(e -> updateAmenityTotal.run());
        cbToiletries.setOnAction(e -> updateAmenityTotal.run());

        VBox amenityBox = new VBox(8,
            lblAmenities,
            cbFridge, cbSnackBar, cbExtraBed, cbToiletries,
            lblAmenityTotal);
        amenityBox.setPadding(new Insets(12));
        amenityBox.setStyle(
            "-fx-background-color: #08102a;" +
            "-fx-border-color: #1a3a5c;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;");

        // ── Book button ───────────────────────────────────────
        Button btnBook = new Button("🛏  Book Room");
        btnBook.setStyle(
            "-fx-background-color: #00d48a; -fx-text-fill: #0a0f1e;" +
            "-fx-font-weight: bold; -fx-background-radius: 6;" +
            "-fx-padding: 10 26; -fx-font-size: 15px; -fx-cursor: hand;");

        Label lblBook = new Label();
        lblBook.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        // Create table ONCE
        TableView<Booking> table = buildTable();
        refreshTable(table);

        btnBook.setOnAction(e -> {
            try {
                int cid = Integer.parseInt(tfCustId.getText().trim());
                int rno = Integer.parseInt(tfRoomNo.getText().trim());
                Customer cust = customerController.findCustomer(cid);
                Room room     = roomController.findRoom(rno);

                if (cust == null) {
                    lblBook.setText("❌ Customer not found.");
                    lblBook.setStyle("-fx-text-fill: #ff4757;");
                    return;
                }
                if (room == null) {
                    lblBook.setText("❌ Room not found.");
                    lblBook.setStyle("-fx-text-fill: #ff4757;");
                    return;
                }
                if (!room.isAvailable()) {
                    lblBook.setText("❌ Room already booked!");
                    lblBook.setStyle("-fx-text-fill: #ff4757;");
                    return;
                }
                if (!dpOut.getValue().isAfter(dpIn.getValue())) {
                    lblBook.setText("❌ Check-out must be after check-in.");
                    lblBook.setStyle("-fx-text-fill: #ff4757;");
                    return;
                }

                // Build amenities list and cost
                List<String> amenities = new ArrayList<>();
                double amenitiesCost = 0;
                if (cbFridge.isSelected()) {
                    amenities.add("Mini Fridge"); amenitiesCost += 500; }
                if (cbSnackBar.isSelected()) {
                    amenities.add("Snack Bar"); amenitiesCost += 500; }
                if (cbExtraBed.isSelected()) {
                    amenities.add("Extra Bed"); amenitiesCost += 1000; }
                if (cbToiletries.isSelected()) {
                    amenities.add("Extra Toiletries"); amenitiesCost += 1000; }

                boolean loyalty = cust.isLoyaltyMember();

                Booking b = bookingController.book(
                    cid, rno,
                    dpIn.getValue(), dpOut.getValue(),
                    room.getPricePerDay(),
                    loyalty, amenities, amenitiesCost);

                roomController.setAvailability(rno, false);
                cust.setAllocatedRoomNumber(rno);
                customerController.save();

                String loyaltyNote = loyalty
                    ? " | ⭐ 20% loyalty discount applied!" : "";
                lblBook.setText(
                    "✅ Booking #" + b.getBookingId() +
                    " confirmed for " + cust.getName() +
                    "! Total: Rs." + String.format("%.2f", b.getTotalBill()) +
                    loyaltyNote);
                lblBook.setStyle(
                    "-fx-text-fill: #00d48a; -fx-font-weight: bold;" +
                    "-fx-font-size: 14px;");

                // Reset
                tfCustId.clear(); tfRoomNo.clear();
                cbFridge.setSelected(false);
                cbSnackBar.setSelected(false);
                cbExtraBed.setSelected(false);
                cbToiletries.setSelected(false);
                updateAmenityTotal.run();
                refreshTable(table);

            } catch (NumberFormatException ex) {
                lblBook.setText("❌ Invalid ID or Room number.");
                lblBook.setStyle("-fx-text-fill: #ff4757;");
            }
        });

        // Book form — SMALLER font size
        // Left side — input form
        GridPane inputForm = new GridPane();
        inputForm.setHgap(14); inputForm.setVgap(12);
        inputForm.setPadding(new Insets(14));
        inputForm.setStyle(
            "-fx-background-color: #060b18; -fx-background-radius: 8;" +
            "-fx-border-color: #1a3a5c; -fx-border-radius: 8;");
        inputForm.add(makeLabel("Customer ID:"), 0, 0); inputForm.add(tfCustId, 1, 0);
        inputForm.add(makeLabel("Room Number:"), 0, 1); inputForm.add(tfRoomNo, 1, 1);
        inputForm.add(makeLabel("Check-In:"),    0, 2); inputForm.add(dpIn,     1, 2);
        inputForm.add(makeLabel("Check-Out:"),   0, 3); inputForm.add(dpOut,    1, 3);

        // Side by side: input form LEFT, amenities RIGHT
        HBox bookRow = new HBox(20, inputForm, amenityBox);
        bookRow.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        HBox.setHgrow(inputForm, Priority.ALWAYS);
        HBox.setHgrow(amenityBox, Priority.ALWAYS);

        VBox bookFormBox = new VBox(12, bookRow, btnBook, lblBook);
        bookFormBox.setPadding(new Insets(14));
        bookFormBox.setStyle(
            "-fx-background-color: #060b18; -fx-background-radius: 8;" +
            "-fx-border-color: #1a3a5c; -fx-border-radius: 8;");

        // ── Checkout ──────────────────────────────────────────
        TextField tfBookingId = new TextField();
        tfBookingId.setPromptText("Booking ID");
        tfBookingId.setPrefWidth(200);

        Button btnCheckout = new Button("🚪  Checkout");
        btnCheckout.setStyle(
            "-fx-background-color: #ff4757; -fx-text-fill: white;" +
            "-fx-font-weight: bold; -fx-background-radius: 6;" +
            "-fx-padding: 10 26; -fx-font-size: 15px; -fx-cursor: hand;");

        Label lblCheckout = new Label();
        lblCheckout.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        btnCheckout.setOnAction(e -> {
            try {
                int bid = Integer.parseInt(tfBookingId.getText().trim());
                Booking b = bookingController.getAllBookings().stream()
                        .filter(bk -> bk.getBookingId() == bid)
                        .findFirst().orElse(null);

                if (b == null || !b.isActive()) {
                    lblCheckout.setText("❌ Invalid or already checked-out.");
                    lblCheckout.setStyle("-fx-text-fill: #ff4757;");
                    return;
                }

                Customer c  = customerController.findCustomer(b.getCustomerId());
                Room r      = roomController.findRoom(b.getRoomNumber());
                long days   = b.getCheckOut().toEpochDay() -
                              b.getCheckIn().toEpochDay();
                String name = c != null ? c.getName()     : "Unknown";
                String type = r != null ? r.getRoomType() : "N/A";
                double price= r != null ? r.getPricePerDay() : 0.0;

                bookingController.checkout(bid);
                roomController.setAvailability(b.getRoomNumber(), true);
                if (c != null) {
                    c.setAllocatedRoomNumber(-1);
                    customerController.save();
                }

                lblCheckout.setText(
                    "✅ Checked out! Room " + b.getRoomNumber() +
                    " is now free.");
                lblCheckout.setStyle("-fx-text-fill: #00d48a;");
                tfBookingId.clear();
                refreshTable(table);

                // Amenities breakdown for bill
                String amenityLines = "";
                if (b.getAmenities() != null && !b.getAmenities().isEmpty()) {
                    amenityLines =
                        "Amenities    : " +
                        String.join(", ", b.getAmenities()) + "\n" +
                        "Amenity Cost : Rs. " +
                        String.format("%.2f", b.getAmenitiesCost()) + "\n";
                }

                String discountLine = b.getDiscountAmount() > 0
                    ? "Loyalty Disc : -Rs. " +
                      String.format("%.2f", b.getDiscountAmount()) + "\n"
                    : "";

                String billText =
                    "==========================================\n" +
                    "        HOTEL MANAGEMENT SYSTEM          \n" +
                    "             CHECKOUT RECEIPT            \n" +
                    "==========================================\n" +
                    "Booking ID   : " + b.getBookingId()     + "\n" +
                    "Customer     : " + name                  + "\n" +
                    "Room No      : " + b.getRoomNumber()     +
                                " (" + type + ")\n"           +
                    "Check-In     : " + b.getCheckIn()        + "\n" +
                    "Check-Out    : " + b.getCheckOut()        + "\n" +
                    "Days Stayed  : " + days + " day(s)\n"    +
                    "Price/Day    : Rs. " +
                        String.format("%.2f", price)          + "\n" +
                    "Room Subtotal: Rs. " +
                        String.format("%.2f", b.getRoomBill()) + "\n" +
                    amenityLines +
                    discountLine +
                    "------------------------------------------\n" +
                    "TOTAL BILL   : Rs. " +
                        String.format("%.2f", b.getTotalBill()) + "\n" +
                    "==========================================\n" +
                    "     Thank you for staying with us!      \n" +
                    "==========================================";

                TextArea ta = new TextArea(billText);
                ta.setEditable(false);
                ta.setFont(Font.font("Courier New", 14));
                ta.setPrefSize(500, 460);
                ta.setStyle(
                    "-fx-control-inner-background: #111111;" +
                    "-fx-text-fill: #FFD700;" +
                    "-fx-background-color: #111111;" +
                    "-fx-border-color: #FFD700;" +
                    "-fx-border-width: 1;" +
                    "-fx-font-size: 14px;");

                Button btnClose = new Button("✖  Close");
                btnClose.setStyle(
                    "-fx-background-color: #FFD700;" +
                    "-fx-text-fill: #111111;" +
                    "-fx-font-weight: bold; -fx-padding: 9 28;" +
                    "-fx-background-radius: 6; -fx-cursor: hand;");

                Stage billStage = new Stage();
                btnClose.setOnAction(ev -> billStage.close());

                VBox popup = new VBox(16, ta, btnClose);
                popup.setPadding(new Insets(24));
                popup.setAlignment(Pos.CENTER);
                popup.setStyle("-fx-background-color: #111111;");

                Scene billScene = new Scene(popup, 540, 540);
                billStage.setTitle(
                    "Receipt — Booking #" + b.getBookingId());
                billStage.setScene(billScene);
                billStage.setResizable(false);
                billStage.show();

            } catch (NumberFormatException ex) {
                lblCheckout.setText("❌ Invalid booking ID.");
                lblCheckout.setStyle("-fx-text-fill: #ff4757;");
            }
        });

        HBox checkoutBox = new HBox(14,
            makeLabel("Booking ID:"), tfBookingId,
            btnCheckout, lblCheckout);
        checkoutBox.setAlignment(Pos.CENTER_LEFT);
        checkoutBox.setPadding(new Insets(14));
        checkoutBox.setStyle(
            "-fx-background-color: #060b18; -fx-background-radius: 8;" +
            "-fx-border-color: #1a3a5c; -fx-border-radius: 8;");

        // Section labels
        Label lblS1 = sectionLabel("Book a Room");
        Label lblS2 = sectionLabel("Checkout");
        Label lblS3 = sectionLabel("All Bookings");

        // Table font BIGGER — set row height
        table.setFixedCellSize(38);
        table.setStyle("-fx-font-size: 15px;");

        VBox layout = new VBox(14,
        header,
        lblS1, bookFormBox,       // changed from bookForm to bookFormBox
        new Separator(),
        lblS2, checkoutBox,
        new Separator(),
        lblS3, table
        );
        layout.setPadding(new Insets(24));

        // Wrap in ScrollPane so it scrolls
        ScrollPane scroll = new ScrollPane(layout);
        scroll.setFitToWidth(true);
        scroll.setStyle(
            "-fx-background-color: #0d1426; -fx-background: #0d1426;");

        tab.setContent(scroll);
        return tab;
    }

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #b8d4e8; -fx-font-size: 13px;");
        return l;
    }

    private Label sectionLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        l.setStyle("-fx-text-fill: #7ab8d4;");
        return l;
    }

    private TableView<Booking> buildTable() {
        TableView<Booking> table = new TableView<>();
        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Booking, Integer> colId = new TableColumn<>("Booking ID");
        colId.setCellValueFactory(d -> new javafx.beans.property
            .SimpleIntegerProperty(d.getValue().getBookingId()).asObject());

        TableColumn<Booking, String> colCust = new TableColumn<>("Customer");
        colCust.setCellValueFactory(d -> {
            Customer c = customerController.findCustomer(
                d.getValue().getCustomerId());
            return new javafx.beans.property.SimpleStringProperty(
                c != null ? c.getName() :
                "ID:" + d.getValue().getCustomerId());
        });

        TableColumn<Booking, Integer> colRoom = new TableColumn<>("Room");
        colRoom.setCellValueFactory(d -> new javafx.beans.property
            .SimpleIntegerProperty(d.getValue().getRoomNumber()).asObject());

        TableColumn<Booking, String> colIn = new TableColumn<>("Check-In");
        colIn.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(d.getValue().getCheckIn().toString()));

        TableColumn<Booking, String> colOut = new TableColumn<>("Check-Out");
        colOut.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(d.getValue().getCheckOut().toString()));

        TableColumn<Booking, String> colBill = new TableColumn<>("Bill (Rs.)");
        colBill.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty("Rs. " +
                String.format("%.2f", d.getValue().getTotalBill())));

        TableColumn<Booking, String> colDiscount = new TableColumn<>("Discount");
        colDiscount.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(
                d.getValue().getDiscountAmount() > 0
                ? "⭐ -Rs." + String.format("%.0f",
                    d.getValue().getDiscountAmount())
                : "—"));
        colDiscount.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); } else {
                    setText(item);
                    setStyle(item.contains("⭐")
                        ? "-fx-text-fill: #FFD700; -fx-font-weight: bold;"
                        : "-fx-text-fill: #4a6fa5;");
                }
            }
        });

        TableColumn<Booking, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(d -> new javafx.beans.property
            .SimpleStringProperty(
                d.getValue().isActive() ? "🟡 Active" : "✅ Checked Out"));
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); } else {
                    setText(item);
                    setStyle(item.contains("Active")
                        ? "-fx-text-fill: #f4a261; -fx-font-weight: bold;"
                        : "-fx-text-fill: #00d48a; -fx-font-weight: bold;");
                }
            }
        });

        table.getColumns().add(colId);
        table.getColumns().add(colCust);
        table.getColumns().add(colRoom);
        table.getColumns().add(colIn);
        table.getColumns().add(colOut);
        table.getColumns().add(colBill);
        table.getColumns().add(colDiscount);
        table.getColumns().add(colStatus);

        return table;
    }

    private void refreshTable(TableView<Booking> table) {
        table.setItems(FXCollections.observableArrayList(
            bookingController.getAllBookings()));
        table.refresh();
    }
}
