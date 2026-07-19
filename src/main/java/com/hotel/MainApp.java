package com.hotel;

import com.hotel.controller.*;
import com.hotel.UI.*;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        RoomController     rc = new RoomController();
        CustomerController cc = new CustomerController();
        BookingController  bc = new BookingController();

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.LEFT);
        tabPane.setTabMinWidth(140);
        tabPane.setTabMinHeight(48);

        tabPane.getTabs().addAll(
            new DashboardTab(rc, bc).createTab(),
            new RoomTab(rc).createTab(),
            new CustomerTab(cc).createTab(),
            new BookingTab(bc, rc, cc).createTab(),
            new BillingTab(bc, cc, rc).createTab(),
            new AnalyticsTab(bc, rc).createTab()
        );

        String css =
            ".root { -fx-font-family: 'Segoe UI'; -fx-font-size: 16px;" +
            "  -fx-background-color: #0a0f1e; } " +
            ".tab-pane { -fx-background-color: #0a0f1e; } " +
            ".tab-pane .tab-content-area { -fx-background-color: #0d1426; } " +
            ".tab-pane .tab-header-background { -fx-background-color: #060b18; } " +
            ".tab-pane .tab-header-area { -fx-background-color: #060b18;" +
            "  -fx-padding: 8 0 0 0; } " +
            ".tab-pane .tab { -fx-background-color: #060b18;" +
            "  -fx-background-insets: 0; -fx-padding: 12 20;" +
            "  -fx-background-radius: 0; } " +
            ".tab-pane .tab:selected { -fx-background-color: #0d1426;" +
            "  -fx-border-color: #00d4ff; -fx-border-width: 0 0 0 4; } " +
            ".tab-pane .tab:hover { -fx-background-color: #0d1933; } " +
            ".tab .tab-label { -fx-text-fill: #4a6fa5; -fx-font-size: 16px;" +
            "  -fx-font-weight: bold; } " +
            ".tab-pane .tab:selected .tab-label { -fx-text-fill: #00d4ff; } " +
            ".tab-pane .tab:hover .tab-label { -fx-text-fill: #7ab8d4; } " +
            ".label { -fx-text-fill: #b8d4e8; } " +
            ".text-field, .date-picker, .combo-box { " +
            "  -fx-background-color: #060b18;" +
            "  -fx-text-fill: #e0f4ff;" +
            "  -fx-border-color: #1a3a5c;" +
            "  -fx-border-radius: 6;" +
            "  -fx-background-radius: 6;" +
            "  -fx-padding: 8;" +
            "  -fx-prompt-text-fill: #2a4a6a; } " +
            ".text-field:focused { -fx-border-color: #00d4ff; } " +
            ".date-picker .arrow-button { -fx-background-color: #060b18; } " +
            ".date-picker .arrow-button .arrow { -fx-background-color: #00d4ff; } " +
            ".combo-box .list-cell { -fx-background-color: #060b18;" +
            "  -fx-text-fill: #e0f4ff; } " +
            ".button { -fx-background-color: #00d4ff;" +
            "  -fx-text-fill: #0a0f1e;" +
            "  -fx-font-weight: bold;" +
            "  -fx-background-radius: 6;" +
            "  -fx-cursor: hand;" +
            "  -fx-padding: 9 22; } " +
            ".button:hover { -fx-background-color: #00b8d9; } " +
            ".button:pressed { -fx-background-color: #0099b8; } " +
            ".table-view { -fx-background-color: #060b18;" +
            "  -fx-border-color: #1a3a5c; -fx-border-radius: 8; } " +
            ".table-view .column-header-background { -fx-background-color: #08102a; } " +
            ".table-view .filler { -fx-background-color: #08102a; } " +
            ".table-view .corner { -fx-background-color: #08102a; } " +
            ".table-view .column-header { -fx-background-color: #08102a;" +
            "  -fx-border-color: #1a3a5c; -fx-padding: 10 8; } " +
            ".table-view .column-header .label { -fx-text-fill: #00d4ff;" +
            "  -fx-font-weight: bold; -fx-font-size: 15px; } " +
            ".table-row-cell { -fx-background-color: #060b18;" +
            "  -fx-border-color: #0d1a2e;" +
            "  -fx-table-cell-border-color: #0d1a2e; } " +
            ".table-row-cell:odd { -fx-background-color: #090e1c; } " +
            ".table-row-cell:selected { -fx-background-color: #0d2a4a; } " +
            ".table-cell { -fx-text-fill: #b8d4e8; -fx-padding: 8; } " +
            ".table-view .placeholder .label { -fx-text-fill: #4a6fa5; } " +
            ".check-box .box { -fx-background-color: #060b18;" +
            "  -fx-border-color: #00d4ff; -fx-border-radius: 4; } " +
            ".check-box:selected .box { -fx-background-color: #00d4ff; } " +
            ".check-box .mark { -fx-background-color: #0a0f1e; } " +
            ".check-box { -fx-text-fill: #b8d4e8; } " +
            ".separator .line { -fx-border-color: #1a3a5c; } " +
            ".scroll-bar { -fx-background-color: #060b18; } " +
            ".scroll-bar .thumb { -fx-background-color: #1a3a5c;" +
            "  -fx-background-radius: 4; } " +
            ".text-area { -fx-background-color: #060b18; -fx-text-fill: #b8d4e8;" +
            "  -fx-border-color: #1a3a5c; -fx-border-radius: 6; } " +
            ".text-area .content { -fx-background-color: #060b18; } " +
            ".combo-box-popup .list-view { -fx-background-color: #060b18;" +
            "  -fx-border-color: #1a3a5c; } " +
            ".combo-box-popup .list-cell { -fx-background-color: #060b18;" +
            "  -fx-text-fill: #b8d4e8; } " +
            ".combo-box-popup .list-cell:hover { -fx-background-color: #0d2a4a; } " +
            ".chart { -fx-background-color: transparent; } " +
            ".chart-plot-background { -fx-background-color: #060b18; } " +
            ".chart-vertical-grid-lines { -fx-stroke: #1a3a5c; } " +
            ".chart-horizontal-grid-lines { -fx-stroke: #1a3a5c; } " +
            ".axis { -fx-tick-label-fill: #4a6fa5; } " +
            ".axis-label { -fx-text-fill: #4a6fa5; } " +
            ".chart-legend { -fx-background-color: #060b18; } " +
            ".chart-legend-item { -fx-text-fill: #b8d4e8; } " +
            ".default-color0.chart-bar { -fx-bar-fill: #00d4ff; } " +
            ".default-color1.chart-bar { -fx-bar-fill: #00d48a; } " +
            ".default-color2.chart-bar { -fx-bar-fill: #f4a261; } " +
            ".default-color3.chart-bar { -fx-bar-fill: #c77dff; } " +
            ".default-color0.chart-pie { -fx-pie-color: #00d4ff; } " +
            ".default-color1.chart-pie { -fx-pie-color: #00d48a; } " +
            ".default-color2.chart-pie { -fx-pie-color: #f4a261; } " +
            ".default-color3.chart-pie { -fx-pie-color: #c77dff; } ";

        BorderPane root = new BorderPane(tabPane);
        root.setStyle("-fx-background-color: #0a0f1e;");
        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add("data:text/css," +
            css.replace("\n", "").replace("  ", " "));

        stage.setTitle("Hotel Management System");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}