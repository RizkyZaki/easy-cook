public class ScheduledRecipe extends BaseRecipe {
    private String scheduleDate;

    public ScheduledRecipe(String recipeName, String recipeDetails, String scheduleDate) {
        super(recipeName, recipeDetails);
        this.scheduleDate = scheduleDate;
    }

    @Override
    public void save() {
        try {
            validateRecipe();
            DatabaseHelper.scheduleRecipe(recipeDetails, scheduleDate);
        } catch (InvalidRecipeException e) {
            System.err.println("Error scheduling recipe: " + e.getMessage());
        }
    }

    @Override
    public void delete() {
        DatabaseHelper.deleteScheduleRecipe(recipeName);
    }

    @Override
    protected void validateRecipe() throws InvalidRecipeException {
        if (recipeName == null || recipeName.trim().isEmpty()) {
            throw new InvalidRecipeException("Nama resep tidak boleh kosong");
        }
        if (scheduleDate == null || scheduleDate.trim().isEmpty()) {
            throw new InvalidRecipeException("Tanggal jadwal tidak boleh kosong");
        }
    }

    public void updateSchedule(String newDate) {
        this.scheduleDate = newDate;
        DatabaseHelper.updateScheduledRecipe(recipeName, newDate);
    }
} 