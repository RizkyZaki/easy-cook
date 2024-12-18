import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Home {

    @FXML private TextField searchField;       // Input untuk pencarian
    @FXML private ListView<String> searchResults; // Daftar hasil pencarian
    @FXML private TextArea recipeDetails;     // Detail resep

    @FXML
    private void searchRecipes() {
        String query = searchField.getText();
        if (query.isEmpty()) {
            showAlert("Error", "Masukkan bahan atau kata kunci untuk mencari resep!");
            return;
        }

        List<String> recipes = RecipeService.fetchRecipes(query); // Ambil hasil pencarian
        searchResults.getItems().clear();
        searchResults.getItems().addAll(recipes);

        searchResults.setOnMouseClicked(event -> {
            String selectedRecipe = searchResults.getSelectionModel().getSelectedItem();
            if (selectedRecipe != null) {
                recipeDetails.setText(selectedRecipe); // Tampilkan detail resep
            }
        });
    }

    @FXML
    private void saveRecipe() {
        String selectedRecipe = recipeDetails.getText();
        if (selectedRecipe.isEmpty()) {
            showAlert("Error", "Pilih resep terlebih dahulu untuk disimpan!");
            return;
        }

        DatabaseHelper.saveRecipe(selectedRecipe);
        showAlert("Success", "Resep berhasil disimpan!");
    }

    @FXML
    private void scheduleRecipe() {
        String selectedRecipe = recipeDetails.getText();
        if (selectedRecipe.isEmpty()) {
            showAlert("Error", "Pilih resep terlebih dahulu untuk dijadwalkan!");
            return;
        }

        // Membuat dialog untuk memilih tanggal
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Jadwalkan Resep");
        dialog.setHeaderText("Pilih tanggal untuk menjadwalkan resep:");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("yyyy-MM-dd");

        VBox dialogContent = new VBox();
        dialogContent.setSpacing(10);
        dialogContent.getChildren().add(datePicker);
        dialog.getDialogPane().setContent(dialogContent);

        ButtonType okButtonType = new ButtonType("Jadwalkan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Aksi saat tombol "Jadwalkan" ditekan
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return datePicker.getValue(); // Mengembalikan tanggal yang dipilih
            }
            return null;
        });

        Optional<LocalDate> result = dialog.showAndWait();

        result.ifPresent(date -> {
            DatabaseHelper.scheduleRecipe(selectedRecipe, date.toString());
            showAlert("Success", "Resep berhasil dijadwalkan untuk tanggal " + date + "!");
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
