import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class SavedRecipesPage {

    @FXML private ListView<String> savedRecipeList;
    @FXML private TextArea recipeDetails;
    @FXML
    private void loadSavedRecipes() {
        List<String> recipes = DatabaseHelper.getSavedRecipes();
        savedRecipeList.getItems().clear();
        savedRecipeList.getItems().addAll(recipes);

        savedRecipeList.setOnMouseClicked(event -> onRecipeSelect(event));
    }
    public VBox getView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SavedRecipesPage.fxml"));
        try {
            VBox vbox = loader.load();
            loadSavedRecipes();  
            return vbox;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    

    @FXML
    private void onRecipeSelect(MouseEvent event) {
        String selectedRecipe = savedRecipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            recipeDetails.setText(selectedRecipe);
        }
    }

    @FXML
    private void deleteSelectedRecipe() {
        String selectedRecipe = recipeDetails.getText();
        if (selectedRecipe.isEmpty()) {
            showAlert("Error", "Pilih resep yang ingin dihapus!");
            return;
        }

        DatabaseHelper.deleteRecipe(selectedRecipe);
        showAlert("Success", "Resep berhasil dihapus!");
        loadSavedRecipes(); // Perbarui tampilan setelah dihapus
        recipeDetails.clear(); // Hapus detail resep yang dipilih
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
