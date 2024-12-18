public class SavedRecipe extends BaseRecipe {
    public SavedRecipe(String recipeName, String recipeDetails) {
        super(recipeName, recipeDetails);
    }

    @Override
    public void save() {
        try {
            validateRecipe();
            DatabaseHelper.saveRecipe(recipeDetails);
        } catch (InvalidRecipeException e) {
            System.err.println("Error saving recipe: " + e.getMessage());
        }
    }

    @Override
    public void delete() {
        DatabaseHelper.deleteRecipe(recipeName);
    }

    @Override
    protected void validateRecipe() throws InvalidRecipeException {
        if (recipeName == null || recipeName.trim().isEmpty()) {
            throw new InvalidRecipeException("Nama resep tidak boleh kosong");
        }
        if (recipeDetails == null || recipeDetails.trim().isEmpty()) {
            throw new InvalidRecipeException("Detail resep tidak boleh kosong");
        }
    }
} 