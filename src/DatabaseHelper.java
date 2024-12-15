import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:easycook.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String createSavedRecipesTable = """
                CREATE TABLE IF NOT EXISTS saved_recipes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    recipe TEXT NOT NULL
                );
            """;

            String createScheduledRecipesTable = """
                CREATE TABLE IF NOT EXISTS scheduled_recipes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    recipe TEXT NOT NULL,
                    schedule_date TEXT NOT NULL
                );
            """;

            stmt.execute(createSavedRecipesTable);
            stmt.execute(createScheduledRecipesTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveRecipe(String recipe) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO saved_recipes (recipe) VALUES (?);")) {
            pstmt.setString(1, recipe);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void scheduleRecipe(String recipe, String date) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO scheduled_recipes (recipe, schedule_date) VALUES (?, ?);")) {
            pstmt.setString(1, recipe);
            pstmt.setString(2, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getSavedRecipes() {
        List<String> recipes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT recipe FROM saved_recipes;")) {
            while (rs.next()) {
                recipes.add(rs.getString("recipe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public static List<String> getScheduledRecipes() {
        List<String> recipes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT recipe || ' (Tanggal: ' || schedule_date || ')' AS recipe_info FROM scheduled_recipes;")) {
            while (rs.next()) {
                recipes.add(rs.getString("recipe_info"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public static void deleteRecipe(String recipe) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtSaved = conn.prepareStatement("DELETE FROM saved_recipes WHERE recipe = ?");
             PreparedStatement pstmtScheduled = conn.prepareStatement("DELETE FROM scheduled_recipes WHERE recipe = ?")) {
             
            // Delete from saved recipes
            pstmtSaved.setString(1, recipe);
            pstmtSaved.executeUpdate();
    
            // Delete from scheduled recipes
            pstmtScheduled.setString(1, recipe);
            pstmtScheduled.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
