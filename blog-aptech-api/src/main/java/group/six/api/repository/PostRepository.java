package group.six.api.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRepository {

    private Connection connection;

    public PostRepository(Connection connection) {
        this.connection = connection;
    }

    public JsonArray getAllPosts() throws SQLException {
        JsonArray postsArray = new JsonArray();
        String sql = "SELECT post.*, users.fullname AS author_name\n" +
                "FROM post\n" +
                "JOIN users ON post.creator_id = users.user_id;\n";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                JsonObject postObject = createPostJsonObject(resultSet);
                postsArray.add(postObject);
            }
        }

        return postsArray;
    }

    public JsonArray getPostsByCategory(String category) throws SQLException {
        JsonArray jsonArray = new JsonArray();
        String sql = "SELECT post.*, users.fullname AS author_name\n" +
                "FROM post\n" +
                "JOIN users ON post.creator_id = users.user_id\n" +
                "WHERE post.category = ?;\n";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    JsonObject postObject = createPostJsonObject(resultSet);
                    jsonArray.add(postObject);
                }
            }
        }

        return jsonArray;
    }

    public JsonObject getPostById(int postId) throws SQLException {
        String sql = "SELECT post.*, users.fullname AS author_name\n" +
                "FROM post\n" +
                "JOIN users ON post.creator_id = users.user_id\n" +
                "WHERE post.post_id = ?;\n";
        JsonObject postObject = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, postId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    postObject = createPostJsonObject(resultSet);
                }
            }
        }

        return postObject;
    }

    private JsonObject createPostJsonObject(ResultSet resultSet) throws SQLException {
        JsonObject postObject = new JsonObject();
        postObject.addProperty("post_id", resultSet.getInt("post_id"));
        postObject.addProperty("title", resultSet.getString("title"));
        postObject.addProperty("content", resultSet.getString("content"));
        postObject.addProperty("status", resultSet.getString("status"));
        postObject.addProperty("view_count", resultSet.getInt("view_count"));
        postObject.addProperty("creator_id", resultSet.getInt("creator_id"));
        postObject.addProperty("creator_name", resultSet.getString("author_name"));
        postObject.addProperty("scheduled_datetime", resultSet.getTimestamp("scheduled_datetime") != null ? resultSet.getTimestamp("scheduled_datetime").toString() : null);
        postObject.addProperty("category", resultSet.getString("category"));
        postObject.addProperty("created_at", resultSet.getTimestamp("created_at").toString());
        postObject.addProperty("updated_at", resultSet.getTimestamp("updated_at").toString());

        // Lấy danh sách ảnh liên quan đến bài viết
        JsonArray imagesArray = getImagesForPost(resultSet.getInt("post_id"));
        postObject.add("images", imagesArray);

        return postObject;
    }

    private JsonArray getImagesForPost(int postId) throws SQLException {
        JsonArray imagesArray = new JsonArray();
        String imagesQuery = "SELECT image_id, image_path FROM post_images WHERE post_id = ?";

        try (PreparedStatement imagesStatement = connection.prepareStatement(imagesQuery)) {
            imagesStatement.setInt(1, postId);

            try (ResultSet imagesResultSet = imagesStatement.executeQuery()) {
                while (imagesResultSet.next()) {
                    JsonObject imageObject = new JsonObject();
                    imageObject.addProperty("image_id", imagesResultSet.getInt("image_id"));
                    imageObject.addProperty("image_path", imagesResultSet.getString("image_path"));
                    imagesArray.add(imageObject);
                }
            }
        }

        return imagesArray;
    }
}
