package com.hotel.UI;

import com.hotel.controller.BookingController;
import com.hotel.controller.RoomController;
import com.hotel.model.Booking;
import com.hotel.model.Room;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.List;

public class DashboardTab {
    private RoomController roomController;
    private BookingController bookingController;

    private Label valTotal   = new Label("0");
    private Label valAvail   = new Label("0");
    private Label valBooked  = new Label("0");
    private Label valRevenue = new Label("Rs. 0");
    private FlowPane roomGrid = new FlowPane();

    public DashboardTab(RoomController rc, BookingController bc) {
        this.roomController    = rc;
        this.bookingController = bc;
    }

    public Tab createTab() {
        Tab tab = new Tab("🏠  Dashboard");
        tab.setClosable(false);

        Label header = new Label("Hotel Dashboard");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        header.setStyle("-fx-text-fill: #00d4ff;");

        Label sub = new Label("Live overview of your hotel");
        sub.setStyle("-fx-text-fill: #4a6fa5; -fx-font-size: 15px;");

        VBox headerBox = new VBox(4, header, sub);
        headerBox.setPadding(new Insets(0, 0, 16, 0));

        VBox card1 = makeStatCard("🏨 Total Rooms",   valTotal,   "#00d4ff");
        VBox card2 = makeStatCard("✅ Available",      valAvail,   "#00d48a");
        VBox card3 = makeStatCard("🔴 Booked",        valBooked,  "#ff4757");
        VBox card4 = makeStatCard("💰 Total Revenue", valRevenue, "#c77dff");

        HBox statsRow = new HBox(16, card1, card2, card3, card4);
        statsRow.setPadding(new Insets(0, 0, 20, 0));

        Label gridHeader = new Label("Room Availability Grid");
        gridHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        gridHeader.setStyle("-fx-text-fill: #7ab8d4;");

        Label gridSub = new Label(
            "🟢 Green = Available    🔴 Red = Booked    Click a room to view details");
        gridSub.setStyle("-fx-text-fill: #4a6fa5; -fx-font-size: 14px;");

        roomGrid.setHgap(16);
        roomGrid.setVgap(16);
        roomGrid.setPadding(new Insets(12, 0, 0, 0));

        ScrollPane gridScroll = new ScrollPane(roomGrid);
        gridScroll.setFitToWidth(true);
        gridScroll.setStyle(
            "-fx-background-color: transparent; -fx-background: #0d1426;");
        gridScroll.setPrefHeight(460);

        Button btnRefresh = new Button("🔄  Refresh");
        btnRefresh.setOnAction(e -> refresh());

        VBox layout = new VBox(14,
            headerBox,
            statsRow,
            new Separator(),
            gridHeader, gridSub,
            gridScroll,
            btnRefresh
        );
        layout.setPadding(new Insets(28));

        refresh();

        Timeline auto = new Timeline(new KeyFrame(
            Duration.seconds(5), e -> refresh()));
        auto.setCycleCount(Timeline.INDEFINITE);
        auto.play();

        tab.setOnSelectionChanged(e -> { if (tab.isSelected()) refresh(); });
        tab.setContent(layout);
        return tab;
    }

    private void refresh() {
        List<Room> all = roomController.getAllRooms();
        long available = all.stream().filter(Room::isAvailable).count();
        long booked    = all.stream().filter(r -> !r.isAvailable()).count();
        double revenue = bookingController.getAllBookings().stream()
                            .mapToDouble(Booking::getTotalBill).sum();

        valTotal.setText(String.valueOf(all.size()));
        valAvail.setText(String.valueOf(available));
        valBooked.setText(String.valueOf(booked));
        valRevenue.setText("Rs. " + String.format("%.0f", revenue));

        roomGrid.getChildren().clear();
        for (Room r : all) {
            roomGrid.getChildren().add(makeRoomCard(r));
        }
    }

    private VBox makeRoomCard(Room r) {
        boolean avail      = r.isAvailable();
        String bg          = avail ? "#0a2a1a" : "#2a0a0f";
        String border      = avail ? "#00d48a" : "#ff4757";
        String status      = avail ? "✅ Available" : "🔴 Booked";
        String statusColor = avail ? "#00d48a"     : "#ff4757";
        String numColor    = avail ? "#00d48a"     : "#ff4757";

        Label lblNum = new Label("Room " + r.getRoomNumber());
        lblNum.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblNum.setStyle("-fx-text-fill: " + numColor + ";");

        Label lblType = new Label(r.getRoomType());
        lblType.setStyle("-fx-text-fill: #7ab8d4; -fx-font-size: 15px;");

        Label lblPrice = new Label(
            "Rs. " + String.format("%.0f", r.getPricePerDay()) + "/day");
        lblPrice.setStyle("-fx-text-fill: #b8d4e8; -fx-font-size: 15px;");

        Label lblStatus = new Label(status);
        lblStatus.setStyle(
            "-fx-text-fill: " + statusColor + ";" +
            "-fx-font-size: 14px; -fx-font-weight: bold;");

        VBox card = new VBox(8, lblNum, lblType, lblPrice, lblStatus);
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMinWidth(210);
        card.setMaxWidth(210);
        card.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-border-color: " + border + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );

        card.setOnMouseClicked(e -> showRoomPopup(r));
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: " + (avail ? "#0d3a22" : "#3a0d14") + ";" +
            "-fx-border-color: " + border + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-border-color: " + border + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        ));

        return card;
    }

    private void showRoomPopup(Room r) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room Details");
        alert.setHeaderText("Room " + r.getRoomNumber() + " — " + r.getRoomType());
        alert.setContentText(
            "Room Number : " + r.getRoomNumber()  + "\n" +
            "Room Type   : " + r.getRoomType()    + "\n" +
            "Price/Day   : Rs. " + r.getPricePerDay() + "\n" +
            "Status      : " + (r.isAvailable() ? "Available" : "Booked")
        );
        alert.showAndWait();
    }

    private VBox makeStatCard(String title, Label valueLabel, String color) {
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-text-fill: #4a6fa5; -fx-font-size: 14px;");

        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        VBox card = new VBox(6, titleLbl, valueLabel);
        card.setPadding(new Insets(18, 24, 18, 24));
        card.setMinWidth(220);
        card.setStyle(
            "-fx-background-color: #060b18;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 0 0 0 4;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        return card;
    }
}