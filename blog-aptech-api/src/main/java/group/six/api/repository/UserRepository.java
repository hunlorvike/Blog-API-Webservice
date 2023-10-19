package group.six.api.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    private Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public JsonArray getAllUsers() throws SQLException {
        JsonArray jsonArray = new JsonArray();
        String sql = "SELECT\n" +
                "    u.user_id,\n" +
                "    u.fullname,\n" +
                "    u.email,\n" +
                "    COUNT(DISTINCT f.follower_id) AS followers_count,\n" +
                "    COUNT(DISTINCT p.post_id) AS post_count,\n" +
                "    COUNT(DISTINCT c.category_id) AS category_count,\n" +
                "    COUNT(DISTINCT conv.conversation_id) AS active_conversations_count,\n" +
                "    u.role,\n" +
                "    u.avatar_path\n" +
                "FROM\n" +
                "    users u\n" +
                "LEFT JOIN\n" +
                "    user_followers f ON u.user_id = f.user_id\n" +
                "LEFT JOIN\n" +
                "    post p ON u.user_id = p.creator_id\n" +
                "LEFT JOIN\n" +
                "    categories c ON u.user_id = c.creator_id\n" +
                "LEFT JOIN\n" +
                "    participants part ON u.user_id = part.user_id\n" +
                "LEFT JOIN\n" +
                "    conversations conv ON part.conversation_id = conv.conversation_id\n" +
                "GROUP BY\n" +
                "    u.user_id, u.fullname, u.email, u.role, u.avatar_path;";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                JsonObject userObject = createUserJsonObject(resultSet);
                jsonArray.add(userObject);
            }
        }

        return jsonArray;
    }

    public JsonArray getUsersByRole(String role) throws SQLException {
        JsonArray jsonArray = new JsonArray();
        String sql = "SELECT\n" +
                "    u.user_id,\n" +
                "    u.fullname,\n" +
                "    u.email,\n" +
                "    COUNT(DISTINCT f.follower_id) AS followers_count,\n" +
                "    COUNT(DISTINCT p.post_id) AS post_count,\n" +
                "    COUNT(DISTINCT c.category_id) AS category_count,\n" +
                "    COUNT(DISTINCT conv.conversation_id) AS active_conversations_count,\n" +
                "    u.role,\n" +
                "    u.avatar_path\n" +
                "FROM\n" +
                "    users u\n" +
                "LEFT JOIN\n" +
                "    user_followers f ON u.user_id = f.user_id\n" +
                "LEFT JOIN\n" +
                "    post p ON u.user_id = p.creator_id\n" +
                "LEFT JOIN\n" +
                "    categories c ON u.user_id = c.creator_id\n" +
                "LEFT JOIN\n" +
                "    participants part ON u.user_id = part.user_id\n" +
                "LEFT JOIN\n" +
                "    conversations conv ON part.conversation_id = conv.conversation_id\n" +
                "WHERE\n" +
                "    u.role = ?\n" +
                "GROUP BY\n" +
                "    u.user_id, u.fullname, u.email, u.role, u.avatar_path;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, role);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    JsonObject userObject = createUserJsonObject(resultSet);
                    jsonArray.add(userObject);
                }
            }
        }

        return jsonArray;
    }


    // Bổ sung phương thức để lấy thông tin người dùng theo ID
    public JsonObject getUserById(int userId) throws SQLException {
        JsonObject userObject = null;
        String sql = "SELECT\n" +
                "    u.user_id,\n" +
                "    u.fullname,\n" +
                "    u.email,\n" +
                "    COUNT(DISTINCT f.follower_id) AS followers_count,\n" +
                "    COUNT(DISTINCT p.post_id) AS post_count,\n" +
                "    COUNT(DISTINCT c.category_id) AS category_count,\n" +
                "    COUNT(DISTINCT conv.conversation_id) AS active_conversations_count,\n" +
                "    u.role,\n" +
                "    u.avatar_path\n" +
                "FROM\n" +
                "    users u\n" +
                "LEFT JOIN\n" +
                "    user_followers f ON u.user_id = f.user_id\n" +
                "LEFT JOIN\n" +
                "    post p ON u.user_id = p.creator_id\n" +
                "LEFT JOIN\n" +
                "    categories c ON u.user_id = c.creator_id\n" +
                "LEFT JOIN\n" +
                "    participants part ON u.user_id = part.user_id\n" +
                "LEFT JOIN\n" +
                "    conversations conv ON part.conversation_id = conv.conversation_id\n" +
                "WHERE\n" +
                "    u.user_id = ?\n" + // Lọc theo ID người dùng
                "GROUP BY\n" +
                "    u.user_id, u.fullname, u.email, u.role, u.avatar_path;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userObject = createUserJsonObject(resultSet);
                }
            }
        }

        return userObject;
    }

    // Hãy đảm bảo rằng createUserJsonObject đã được triển khai dựa trên logic trong UserServlet.
    private JsonObject createUserJsonObject(ResultSet resultSet) throws SQLException {
        JsonObject userObject = new JsonObject();
        userObject.addProperty("user_id", resultSet.getInt("user_id"));
        userObject.addProperty("fullname", resultSet.getString("fullname"));
        userObject.addProperty("email", resultSet.getString("email"));
        userObject.addProperty("followers_count", resultSet.getInt("followers_count"));
        userObject.addProperty("post_count", resultSet.getInt("post_count"));
        userObject.addProperty("category_count", resultSet.getInt("category_count"));
        userObject.addProperty("active_conversations_count", resultSet.getInt("active_conversations_count"));
        userObject.addProperty("role", resultSet.getString("role"));
        userObject.addProperty("avatar_path", resultSet.getString("avatar_path"));

        return userObject;
    }
}
