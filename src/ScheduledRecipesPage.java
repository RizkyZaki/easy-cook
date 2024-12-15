import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class ScheduledRecipesPage {

    @FXML private ListView<String> scheduledRecipeList;
    @FXML private TextArea recipeDetails;

    public VBox getView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScheduledRecipesPage.fxml"));
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void loadScheduledRecipes() {
        List<String> recipes = DatabaseHelper.getScheduledRecipes();
        scheduledRecipeList.getItems().clear();
        scheduledRecipeList.getItems().addAll(recipes);

        scheduledRecipeList.setOnMouseClicked(event -> onRecipeSelect(event));
    }

    @FXML
    private void onRecipeSelect(MouseEvent event) {
        String selectedRecipe = scheduledRecipeList.getSelectionModel().getSelectedItem();
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
        loadScheduledRecipes(); // Perbarui tampilan
        recipeDetails.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
