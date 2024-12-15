import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MainInterface {

    @FXML private TextField ingredientsInput;
    @FXML private ListView<String> recipeList;
    @FXML private TextArea recipeDetails;

    public VBox getView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInterface.fxml"));
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void searchRecipes() {
        String ingredients = ingredientsInput.getText();
        if (ingredients.isEmpty()) {
            showAlert("Error", "Masukkan bahan-bahan terlebih dahulu!");
            return;
        }

        List<String> recipes = RecipeService.fetchRecipes(ingredients);
        recipeList.getItems().clear();
        recipeList.getItems().addAll(recipes);

        recipeList.setOnMouseClicked(event -> onRecipeSelect(event));
    }

    @FXML
    private void onRecipeSelect(MouseEvent event) {
        int selectedIndex = recipeList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            recipeDetails.setText(recipeList.getItems().get(selectedIndex));
        }
    }

    @FXML
    private void saveRecipe() {
        String selectedRecipe = recipeDetails.getText();
        if (selectedRecipe.isEmpty()) {
            showAlert("Error", "Pilih resep untuk disimpan!");
            return;
        }

        DatabaseHelper.saveRecipe(selectedRecipe);
        showAlert("Success", "Resep berhasil disimpan!");
    }

    @FXML
    private void scheduleRecipe() {
        String selectedRecipe = recipeDetails.getText();
        if (selectedRecipe.isEmpty()) {
            showAlert("Error", "Pilih resep untuk dijadwalkan!");
            return;
        }

        DatePicker datePicker = new DatePicker();
        Alert dateAlert = new Alert(Alert.AlertType.CONFIRMATION);
        dateAlert.setTitle("Jadwalkan Resep");
        dateAlert.setHeaderText("Pilih tanggal untuk menjadwalkan resep:");
        dateAlert.getDialogPane().setContent(datePicker);

        dateAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                LocalDate date = datePicker.getValue();
                if (date != null) {
                    DatabaseHelper.scheduleRecipe(selectedRecipe, date.toString());
                    showAlert("Success", "Resep berhasil dijadwalkan untuk tanggal " + date);
                } else {
                    showAlert("Error", "Tanggal tidak valid!");
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
