import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class RecipeService {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";
    private static final String API_KEY = "AIzaSyCkcdEjSK_BnHumyMRGKWHO9J7iUHHdU6k";

    public static List<String> fetchRecipes(String ingredients) {
        List<String> recipes = new ArrayList<>();
        try {
            String prompt = "Menu resep apa yang cocok untuk bahan berikut: " + ingredients +
                    "? Hasilnya bentuk JSON (nama_makanan, bahan, step_masak). yang lengkap dan detail";
            String requestBody = """
                {
                    "contents": [
                    {
                        "parts": [
                        {
                            "text": "%s"
                        }
                        ]
                    }
                    ]
                }
                """.formatted(prompt);

            @SuppressWarnings("deprecation")
            URL url = new URL(API_URL + "?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.getOutputStream().write(requestBody.getBytes());

            // Baca respons dari server
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            // Cetak respons ke console untuk debugging
            String jsonResponse = response.toString();
            System.out.println("Response JSON from API:");
            System.out.println(jsonResponse);

            // Parsing JSON utama
            JSONObject responseObject = new JSONObject(jsonResponse);
            JSONArray candidates = responseObject.getJSONArray("candidates");
            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
            String textData = content.getJSONArray("parts").getJSONObject(0).getString("text");

            // Membersihkan markdown jika ada
            String cleanJson = textData.replaceAll("```json|```", "").trim();

            // Deteksi apakah respons JSON adalah array atau objek
            if (cleanJson.startsWith("[")) {
                // Jika JSON adalah array
                JSONArray recipesArray = new JSONArray(cleanJson);
                for (int i = 0; i < recipesArray.length(); i++) {
                    JSONObject recipe = recipesArray.getJSONObject(i);
                    recipes.add(formatRecipe(recipe));
                }
            } else if (cleanJson.startsWith("{")) {
                // Jika JSON adalah objek
                JSONObject parsedJson = new JSONObject(cleanJson);
                JSONArray recipesArray = parsedJson.getJSONArray("recipes");
                for (int i = 0; i < recipesArray.length(); i++) {
                    JSONObject recipe = recipesArray.getJSONObject(i);
                    recipes.add(formatRecipe(recipe));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private static String formatRecipe(JSONObject recipe) {
        StringBuilder recipeDetail = new StringBuilder();

        String namaMakanan = recipe.optString("nama_makanan", "Tanpa Nama");
        JSONArray bahan = recipe.optJSONArray("bahan");
        JSONArray stepMasak = recipe.optJSONArray("step_masak");

        recipeDetail.append("Nama Makanan: ").append(namaMakanan).append("\n");

        recipeDetail.append("Bahan:\n");
        if (bahan != null) {
            for (int i = 0; i < bahan.length(); i++) {
                recipeDetail.append("- ").append(bahan.optString(i, "Tidak diketahui")).append("\n");
            }
        } else {
            recipeDetail.append("- Tidak ada bahan yang tercantum.\n");
        }

        recipeDetail.append("Langkah Memasak:\n");
        if (stepMasak != null) {
            for (int i = 0; i < stepMasak.length(); i++) {
                recipeDetail.append((i + 1)).append(". ").append(stepMasak.optString(i, "Langkah tidak tersedia")).append("\n");
            }
        } else {
            recipeDetail.append("Langkah memasak tidak tersedia.\n");
        }

        return recipeDetail.toString();
    }

    
}
