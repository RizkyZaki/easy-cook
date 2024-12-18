package controllers;
import javafx.scene.control.Alert.AlertType;
import models.ScheduledRecipe;
import utils.DatabaseHelper;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ScheduledRecipeController {
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
    private void refreshScheduledRecipes() {
        loadScheduledRecipes(); // Memanggil ulang metode loadScheduledRecipes
    }

    @FXML
    private void deleteScheduledRecipe() {
        String selectedRecipe = scheduledRecipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            try {
                String recipeName = selectedRecipe.split(" \\(Tanggal: ")[0];
                ScheduledRecipe recipe = new ScheduledRecipe(recipeName, selectedRecipe, "");
                recipe.delete();
                showAlert("Success", "Resep berhasil dihapus dari jadwal!");
                loadScheduledRecipes();
            } catch (Exception e) {
                showAlert("Error", "Gagal menghapus resep: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Pilih resep untuk dihapus!");
        }
    }

    @FXML
    private void editScheduledRecipe() {
        String selectedRecipe = scheduledRecipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            String recipeName = selectedRecipe.split(" \\(Tanggal: ")[0];

            DatePicker datePicker = new DatePicker();
            Alert dateAlert = new Alert(AlertType.CONFIRMATION);
            dateAlert.setTitle("Edit Jadwal Resep");
            dateAlert.setHeaderText("Pilih tanggal baru untuk jadwal:");
            dateAlert.getDialogPane().setContent(datePicker);

            dateAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (datePicker.getValue() != null) {
                        String newDate = datePicker.getValue().toString();
                        DatabaseHelper.updateScheduledRecipe(recipeName, newDate);
                        showAlert("Success", "Jadwal resep berhasil diperbarui!");
                        loadScheduledRecipes();
                        recipeDetails.clear();
                    } else {
                        showAlert("Error", "Tanggal tidak valid!");
                    }
                }
            });
        } else {
            showAlert("Error", "Pilih resep untuk diubah jadwalnya!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
