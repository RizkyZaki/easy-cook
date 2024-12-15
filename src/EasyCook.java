import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EasyCook extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EasyCook");

        // TabPane untuk navigasi
        TabPane tabPane = new TabPane();

        // Tab Beranda (Main Interface)
        Tab mainTab = new Tab("Beranda");
        mainTab.setContent(loadFXML("MainInterface.fxml"));
        mainTab.setClosable(false);  // Mencegah tab ini ditutup

        // Tab Resep Tersimpan
        Tab savedRecipesTab = new Tab("Resep Tersimpan");
        savedRecipesTab.setContent(loadFXML("SavedRecipesPage.fxml"));
        savedRecipesTab.setClosable(false);  // Mencegah tab ini ditutup

        // Tab Resep Terjadwal
        Tab scheduledRecipesTab = new Tab("Resep Terjadwal");
        scheduledRecipesTab.setContent(loadFXML("ScheduledRecipesPage.fxml"));
        scheduledRecipesTab.setClosable(false);  // Mencegah tab ini ditutup

        // Menambahkan tab ke TabPane
        tabPane.getTabs().addAll(mainTab, savedRecipesTab, scheduledRecipesTab);

        // Mengatur tampilan utama
        primaryStage.setScene(new Scene(tabPane, 600, 800)); // Lebar diperluas
        primaryStage.show();

        // Initialize Database
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
