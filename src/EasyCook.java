import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class EasyCook extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EasyCook");

        // TabPane untuk navigasi
        TabPane tabPane = new TabPane();

        // Tab Beranda
        Tab homeTab = new Tab("Beranda");
        homeTab.setContent(loadFXML("Home.fxml"));

        // Tab Resep Tersimpan
        Tab savedRecipesTab = new Tab("Resep Saya");
        savedRecipesTab.setContent(loadFXML("SavedRecipes.fxml"));

        // Tab Resep Terjadwal
        Tab scheduledRecipesTab = new Tab("Resep Terjadwal");
        scheduledRecipesTab.setContent(loadFXML("ScheduledRecipes.fxml"));

        // Tambahkan tab ke TabPane
        tabPane.getTabs().addAll(homeTab, savedRecipesTab, scheduledRecipesTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Tab tidak bisa ditutup

        // Atur scene utama
        primaryStage.setScene(new Scene(tabPane));
        primaryStage.show();

        // Initialize database
        DatabaseHelper.initializeDatabase();
    }

    private VBox loadFXML(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
