package com.hotel.UI;

import com.hotel.controller.BookingController;
import com.hotel.controller.RoomController;
import com.hotel.model.Booking;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsTab {
    private BookingController bookingController;
    private RoomController roomController;

    private Label valRevenue   = new Label("Rs. 0");
    private Label valCompleted = new Label("0");
    private Label valActive    = new Label("0");
    private Label valAvg       = new Label("Rs. 0");

    public AnalyticsTab(BookingController bc, RoomController rc) {
        this.bookingController = bc;
        this.roomController    = rc;
    }

    public Tab createTab() {
        Tab tab = new Tab("📊  Analytics");
        tab.setClosable(false);

        Label header = new Label("Revenue & Analytics");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        header.setStyle("-fx-text-fill: #00d4ff;");

        VBox c1 = makeCard("💰 Total Revenue",      valRevenue,   "#00d4ff");
        VBox c2 = makeCard("✅ Completed Bookings", valCompleted, "#00d48a");
        VBox c3 = makeCard("🔥 Active Bookings",    valActive,    "#f4a261");
        VBox c4 = makeCard("📊 Avg Bill",           valAvg,       "#c77dff");

        HBox cards = new HBox(16, c1, c2, c3, c4);
        cards.setPadding(new Insets(0, 0, 8, 0));

        // Bar chart
        CategoryAxis xBar = new CategoryAxis();
        NumberAxis   yBar = new NumberAxis();
        xBar.setLabel("Room Type");
        yBar.setLabel("Revenue (Rs.)");
        BarChart<String, Number> barChart = new BarChart<>(xBar, yBar);
        barChart.setTitle("Revenue by Room Type");
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(300);
        barChart.setAnimated(false);

        // Pie chart
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Booking Distribution");
        pieChart.setPrefHeight(300);
        pieChart.setAnimated(false);

        HBox charts = new HBox(16, barChart, pieChart);
        HBox.setHgrow(barChart, Priority.ALWAYS);
        HBox.setHgrow(pieChart, Priority.ALWAYS);

        // Line chart
        CategoryAxis xLine = new CategoryAxis();
        NumberAxis   yLine = new NumberAxis();
        xLine.setLabel("Check-In Date");
        yLine.setLabel("Revenue (Rs.)");
        LineChart<String, Number> lineChart = new LineChart<>(xLine, yLine);
        lineChart.setTitle("Revenue Over Time");
        lineChart.setPrefHeight(280);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);

        Label lbl1 = new Label("Revenue Charts");
        lbl1.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lbl1.setStyle("-fx-text-fill: #7ab8d4;");

        Label lbl2 = new Label("Revenue Over Time");
        lbl2.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lbl2.setStyle("-fx-text-fill: #7ab8d4;");

        Button btnRefresh = new Button("🔄  Refresh");
        btnRefresh.setOnAction(e -> refresh(barChart, pieChart, lineChart));

        VBox inner = new VBox(16,
            header, cards,
            new Separator(),
            lbl1, charts,
            new Separator(),
            lbl2, lineChart,
            btnRefresh
        );
        inner.setPadding(new Insets(28));

        ScrollPane scroll = new ScrollPane(inner);
        scroll.setFitToWidth(true);
        scroll.setStyle(
            "-fx-background-color: #0d1426; -fx-background: #0d1426;");

        refresh(barChart, pieChart, lineChart);

        tab.setOnSelectionChanged(e -> {
            if (tab.isSelected()) refresh(barChart, pieChart, lineChart);
        });

        Timeline auto = new Timeline(new KeyFrame(Duration.seconds(10),
            e -> refresh(barChart, pieChart, lineChart)));
        auto.setCycleCount(Timeline.INDEFINITE);
        auto.play();

        tab.setContent(scroll);
        return tab;
    }

    private void refresh(BarChart<String, Number> barChart,
                         PieChart pieChart,
                         LineChart<String, Number> lineChart) {

        List<Booking> all = bookingController.getAllBookings();
        double total   = all.stream().mapToDouble(Booking::getTotalBill).sum();
        long completed = all.stream().filter(b -> !b.isActive()).count();
        long active    = all.stream().filter(Booking::isActive).count();
        double avg     = all.isEmpty() ? 0 : total / all.size();

        valRevenue.setText("Rs. " + String.format("%.0f", total));
        valCompleted.setText(String.valueOf(completed));
        valActive.setText(String.valueOf(active));
        valAvg.setText("Rs. " + String.format("%.0f", avg));

        // Group by room type
        Map<String, Double> byType = new LinkedHashMap<>();
        for (Booking b : all) {
            var room = roomController.findRoom(b.getRoomNumber());
            String type = room != null ? room.getRoomType() : "Unknown";
            byType.merge(type, b.getTotalBill(), Double::sum);
        }

        // Bar chart
        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        byType.forEach((type, rev) ->
            barSeries.getData().add(new XYChart.Data<>(type, rev)));
        barChart.getData().clear();
        barChart.getData().add(barSeries);

        // Pie chart
        pieChart.getData().clear();
        byType.forEach((type, rev) ->
            pieChart.getData().add(new PieChart.Data(type, rev)));

        // Line chart — group by check-in date
        Map<String, Double> byDate = new TreeMap<>();
        for (Booking b : all) {
            String date = b.getCheckIn().toString();
            byDate.merge(date, b.getTotalBill(), Double::sum);
        }
        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        lineSeries.setName("Revenue");
        byDate.forEach((date, rev) ->
            lineSeries.getData().add(new XYChart.Data<>(date, rev)));
        lineChart.getData().clear();
        lineChart.getData().add(lineSeries);
    }

    private VBox makeCard(String title, Label valueLabel, String color) {
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-text-fill: #4a6fa5; -fx-font-size: 13px;");
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");
        VBox card = new VBox(6, titleLbl, valueLabel);
        card.setPadding(new Insets(16, 22, 16, 22));
        card.setMinWidth(200);
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