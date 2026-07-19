package com.hotel.UI;

import com.hotel.controller.*;
import com.hotel.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BillingTab {
    private BookingController bookingController;
    private CustomerController customerController;
    private RoomController roomController;

    public BillingTab(BookingController bc, CustomerController cc, RoomController rc) {
        this.bookingController  = bc;
        this.customerController = cc;
        this.roomController     = rc;
    }

    public Tab createTab() {
        Tab tab = new Tab("💰  Billing");
        tab.setClosable(false);

        Label header = new Label("Billing System");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        header.setStyle("-fx-text-fill: #00d4ff;");

        TextField tfBookingId = new TextField();
        tfBookingId.setPromptText("Enter Booking ID");
        tfBookingId.setPrefWidth(220);

        Button btnSearch = new Button("🔍  Generate Bill");
        btnSearch.setStyle(
            "-fx-background-color: #00d4ff; -fx-text-fill: #0a0f1e;" +
            "-fx-font-weight: bold; -fx-background-radius: 6;" +
            "-fx-padding: 9 22; -fx-cursor: hand;");

        HBox searchRow = new HBox(12,
            new Label("Booking ID:"), tfBookingId, btnSearch);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        searchRow.setPadding(new Insets(14));
        searchRow.setStyle(
            "-fx-background-color: #060b18; -fx-background-radius: 8;" +
            "-fx-border-color: #1a3a5c; -fx-border-radius: 8;");

        HBox summaryCards = new HBox(16);
        summaryCards.setVisible(false);

        TextArea billArea = new TextArea();
        billArea.setEditable(false);
        billArea.setPrefHeight(340);
        billArea.setFont(Font.font("Courier New", 14));
        billArea.setVisible(false);

        btnSearch.setOnAction(e -> {
            try {
                int bid = Integer.parseInt(tfBookingId.getText().trim());
                Booking b = bookingController.getAllBookings().stream()
                    .filter(bk -> bk.getBookingId() == bid)
                    .findFirst().orElse(null);

                if (b == null) {
                    billArea.setText("❌ Booking ID not found.");
                    billArea.setVisible(true);
                    summaryCards.setVisible(false);
                    return;
                }

                Customer c = customerController.findCustomer(b.getCustomerId());
                Room r     = roomController.findRoom(b.getRoomNumber());
                long days  = b.getCheckOut().toEpochDay() - b.getCheckIn().toEpochDay();

                String customerName    = c != null ? c.getName()       : "Unknown";
                String customerContact = c != null ? c.getContact()    : "N/A";
                String roomType        = r != null ? r.getRoomType()   : "N/A";
                double pricePerDay     = r != null ? r.getPricePerDay() : 0.0;

                summaryCards.getChildren().clear();
                summaryCards.getChildren().addAll(
                    makeMiniCard("📅 Days Stayed",
                        String.valueOf(days), "#00d4ff"),
                    makeMiniCard("🛏 Room Type",
                        roomType, "#00d48a"),
                    makeMiniCard("💵 Price/Day",
                        "Rs. " + String.format("%.0f", pricePerDay), "#f4a261"),
                    makeMiniCard("🧾 Total Bill",
                        "Rs. " + String.format("%.2f", b.getTotalBill()), "#c77dff")
                );
                summaryCards.setVisible(true);

                String bill =
                    "==========================================\n" +
                    "        HOTEL MANAGEMENT SYSTEM          \n" +
                    "                 INVOICE                 \n" +
                    "==========================================\n" +
                    "Booking ID   : " + b.getBookingId()       + "\n" +
                    "Customer     : " + customerName           +
                                " (ID: " + b.getCustomerId()  + ")\n" +
                    "Contact      : " + customerContact        + "\n" +
                    "------------------------------------------\n" +
                    "Room No      : " + b.getRoomNumber()      +
                                " (" + roomType + ")\n"        +
                    "Check-In     : " + b.getCheckIn()         + "\n" +
                    "Check-Out    : " + b.getCheckOut()        + "\n" +
                    "Days Stayed  : " + days + " day(s)\n"     +
                    "Price/Day    : Rs. " +
                        String.format("%.2f", pricePerDay)     + "\n" +
                    "------------------------------------------\n" +
                    "TOTAL BILL   : Rs. " +
                        String.format("%.2f", b.getTotalBill()) + "\n" +
                    "Status       : " +
                        (b.isActive() ? "Active" : "Checked Out") + "\n" +
                    "==========================================\n" +
                    "      Thank you for staying with us!     \n" +
                    "==========================================";

                billArea.setText(bill);
                billArea.setVisible(true);

            } catch (NumberFormatException ex) {
                billArea.setText("❌ Please enter a valid booking ID number.");
                billArea.setVisible(true);
                summaryCards.setVisible(false);
            }
        });

        Label summaryHeader = new Label("Bill Summary");
        summaryHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        summaryHeader.setStyle("-fx-text-fill: #7ab8d4;");

        Label billHeader = new Label("Full Invoice");
        billHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        billHeader.setStyle("-fx-text-fill: #7ab8d4;");

        VBox layout = new VBox(16,
            header,
            searchRow,
            new Separator(),
            summaryHeader,
            summaryCards,
            new Separator(),
            billHeader,
            billArea
        );
        layout.setPadding(new Insets(28));
        tab.setContent(layout);
        return tab;
    }

    private VBox makeMiniCard(String title, String value, String color) {
        Label lTitle = new Label(title);
        lTitle.setStyle("-fx-text-fill: #4a6fa5; -fx-font-size: 13px;");

        Label lValue = new Label(value);
        lValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lValue.setStyle("-fx-text-fill: " + color + ";");

        VBox card = new VBox(4, lTitle, lValue);
        card.setPadding(new Insets(14, 20, 14, 20));
        card.setMinWidth(180);
        card.setStyle(
            "-fx-background-color: #060b18;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 0 0 0 3;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        return card;
    }
}