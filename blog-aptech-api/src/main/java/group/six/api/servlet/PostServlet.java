package group.six.api.servlet;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import group.six.api.config.ConnectionProvider;
import group.six.api.repository.PostRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "postServlet", urlPatterns = {"/api/post/getAllPosts", "/api/post/getPostsByCategory", "/api/post/getPostById"})
public class PostServlet extends HttpServlet {
    ConnectionProvider connectionProvider = new ConnectionProvider();
    private PostRepository postRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        postRepository = new PostRepository(connectionProvider.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURI();
        switch (url) {
            case "/api/post/getAllPosts":
                try {
                    getAllPosts(resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/api/post/getPostsByCategory":
                getPostsByCategory(req, resp);
                break;
            case "/api/post/getPostById":
                getPostById(req, resp);
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("URL không hợp lệ.");
                break;
        }
    }

    private void getAllPosts(HttpServletResponse resp) throws IOException, SQLException {
        // Thiết lập tiêu đề cho CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); // Cho phép tất cả các nguồn gốc truy cập
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // Cho phép các phương thức yêu cầu
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"); // Cho phép các tiêu đề yêu cầu
        resp.setContentType("application/json");

        try {
            JsonArray allPosts = postRepository.getAllPosts();
            resp.getWriter().write(allPosts.toString());
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Lỗi truy vấn cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }


    private void getPostsByCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Thiết lập tiêu đề cho CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); // Cho phép tất cả các nguồn gốc truy cập
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // Cho phép các phương thức yêu cầu
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"); // Cho phép các tiêu đề yêu cầu
        String categoryFilter = req.getParameter("category");
        resp.setContentType("application/json");

        if (categoryFilter != null && !categoryFilter.isEmpty()) {
            try {
                JsonArray jsonArray = postRepository.getPostsByCategory(categoryFilter);
                resp.getWriter().write(jsonArray.toString());
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Lỗi truy vấn cơ sở dữ liệu.");
                e.printStackTrace();
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Thiếu tham số 'category' trong yêu cầu.");
        }
    }

    private void getPostById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Thiết lập tiêu đề cho CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); // Cho phép tất cả các nguồn gốc truy cập
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // Cho phép các phương thức yêu cầu
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"); // Cho phép các tiêu đề yêu cầu
        String idFilter = req.getParameter("id");
        resp.setContentType("application/json");

        if (idFilter != null && !idFilter.isEmpty()) {
            try {
                int postId = Integer.parseInt(idFilter); // Chuyển đổi kiểu String sang int
                JsonObject postObject = postRepository.getPostById(postId);

                if (postObject != null) {
                    resp.getWriter().write(postObject.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Không tìm thấy bài viết với ID: " + postId);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Tham số 'id' không hợp lệ.");
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Lỗi truy vấn cơ sở dữ liệu.");
                e.printStackTrace();
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Thiếu tham số 'id' trong yêu cầu.");
        }
    }

}
