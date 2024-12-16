import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class ScheduledRecipes {
    @FXML private ListView<String> scheduledRecipeList;
    @FXML private TextArea recipeDetails;

    @FXML
    private void initialize() {
        loadScheduledRecipes();
    }

    private void loadScheduledRecipes() {
        List<String> recipes = DatabaseHelper.getScheduledRecipes();
        scheduledRecipeList.getItems().clear();
        scheduledRecipeList.getItems().addAll(recipes);

        scheduledRecipeList.setOnMouseClicked(event -> {
            String selectedRecipe = scheduledRecipeList.getSelectionModel().getSelectedItem();
            recipeDetails.setText(selectedRecipe);
        });
    }

    @FXML
    private void deleteScheduledRecipe() {
        String selectedRecipe = scheduledRecipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            DatabaseHelper.deleteRecipe(selectedRecipe);
            showAlert("Success", "Resep berhasil dihapus dari jadwal!");
            loadScheduledRecipes();
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
