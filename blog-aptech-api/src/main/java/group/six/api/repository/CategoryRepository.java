package group.six.api.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRepository {
    private Connection connection;

    public CategoryRepository(Connection connection) {
        this.connection = connection;
    }

    public JsonArray getAllCategories() throws SQLException {
        JsonArray jsonArray = new JsonArray();
        String sql = "SELECT * FROM categories";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                JsonObject userObject = createCategoryJsonObject(resultSet);
                jsonArray.add(userObject);
            }
        }
        return jsonArray;

    }

    public JsonObject getCategoryById(int categoryId) throws SQLException {
        JsonObject categoryObject = null;
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    categoryObject = createCategoryJsonObject(resultSet);
                }
            }
        }
        return categoryObject;

    }

    private JsonObject createCategoryJsonObject(ResultSet resultSet) throws SQLException {
        JsonObject categoryJsonObject = new JsonObject();
        categoryJsonObject.addProperty("category_id", resultSet.getInt("category_id"));
        categoryJsonObject.addProperty("category_name", resultSet.getString("name"));
        categoryJsonObject.addProperty("creator_id", resultSet.getInt("creator_id"));
        categoryJsonObject.addProperty("created_at", resultSet.getTimestamp("created_at").toString());
        categoryJsonObject.addProperty("updated_at", resultSet.getTimestamp("updated_at").toString());
        return categoryJsonObject;
    }
}
