package models;
import interfaces.IRecipe;
import utils.InvalidRecipeException;

public abstract class BaseRecipe implements IRecipe {
    protected String recipeName;
    protected String recipeDetails;

    public BaseRecipe(String recipeName, String recipeDetails) {
        this.recipeName = recipeName;
        this.recipeDetails = recipeDetails;
    }

    @Override
    public String getRecipeDetails() {
        return recipeDetails;
    }

    // Method abstrak yang harus diimplementasikan oleh child class
    protected abstract void validateRecipe() throws InvalidRecipeException;
} 