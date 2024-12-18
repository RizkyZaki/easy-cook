import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class SavedRecipes {
    @FXML private ListView<String> savedRecipeList;
    @FXML private TextArea recipeDetails;

    @FXML
    private void initialize() {
        loadSavedRecipes();
    }

    private void loadSavedRecipes() {
        List<String> recipes = DatabaseHelper.getSavedRecipes();
        savedRecipeList.getItems().clear();
        savedRecipeList.getItems().addAll(recipes);

        savedRecipeList.setOnMouseClicked(event -> {
            String selectedRecipe = savedRecipeList.getSelectionModel().getSelectedItem();
            recipeDetails.setText(selectedRecipe);
        });
    }

    @FXML
    private void refreshSavedRecipes() {
        loadSavedRecipes(); // Memanggil ulang metode loadSavedRecipes
    }

    @FXML
    private void deleteSelectedRecipe() {
        String selectedRecipe = savedRecipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            DatabaseHelper.deleteRecipe(selectedRecipe);
            showAlert("Success", "Resep berhasil dihapus!");
            loadSavedRecipes();
            recipeDetails.clear();
        } else {
            showAlert("Error", "Pilih resep untuk dihapus!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
