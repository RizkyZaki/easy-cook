import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class Home {

    @FXML private TextField searchField;       
    @FXML private ListView<String> searchResults; 
    @FXML private TextArea recipeDetails;     

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
