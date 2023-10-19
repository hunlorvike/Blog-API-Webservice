package group.six.api.servlet;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import group.six.api.config.ConnectionProvider;
import group.six.api.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "userServlet", urlPatterns = {"/api/user/getAllUsers", "/api/user/getUsersByRole", "/api/user/getUsersById"})
public class UserServlet extends HttpServlet {
    ConnectionProvider connectionProvider = new ConnectionProvider();

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        userRepository = new UserRepository(connectionProvider.getConnection());
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String url = req.getRequestURI();
        switch (url) {
            case "/api/user/getAllUsers":
                getAllUsers(resp);
                break;
            case "/api/user/getUsersByRole":
                getUsersByRole(req, resp);
                break;
            case "/api/user/getUsersById":
                getUserById(req, resp);
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("URL không hợp lệ.");
                break;
        }
    }

    private void getAllUsers(HttpServletResponse resp) throws IOException {
        // Thiết lập tiêu đề cho CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); // Cho phép tất cả các nguồn gốc truy cập
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // Cho phép các phương thức yêu cầu
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"); // Cho phép các tiêu đề yêu cầu

        try {
            JsonArray allUsers = userRepository.getAllUsers();
            resp.setContentType("application/json");
            resp.getWriter().write(allUsers.toString());
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Lỗi truy vấn cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    private void getUsersByRole(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Thiết lập tiêu đề cho CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); // Cho phép tất cả các nguồn gốc truy cập
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // Cho phép các phương thức yêu cầu
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"); // Cho phép các tiêu đề yêu cầu

        String roleFilter = req.getParameter("role");

        if (roleFilter != null && !roleFilter.isEmpty()) {
            try {
                JsonArray jsonArray = userRepository.getUsersByRole(roleFilter);
                resp.setContentType("application/json");
                resp.getWriter().write(jsonArray.toString());
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Lỗi truy vấn cơ sở dữ liệu.");
                e.printStackTrace();
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Thiếu tham số 'role' trong yêu cầu.");
        }
    }

    private void getUserById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Thiết lập tiêu đề cho CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); // Cho phép tất cả các nguồn gốc truy cập
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // Cho phép các phương thức yêu cầu
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"); // Cho phép các tiêu đề yêu cầu

        String idFilter = req.getParameter("id");
        resp.setContentType("application/json");

        if (idFilter != null && !idFilter.isEmpty()) {
            try {
                int userId = Integer.parseInt(idFilter);
                JsonObject userObject = userRepository.getUserById(userId);

                if (userObject != null) {
                    resp.getWriter().write(userObject.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Không tìm thấy người dùng với ID: " + userId);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Tham số 'id' không hợp lệ.");
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Lỗi truy vấn cơ sở dữ liệu .");
                e.printStackTrace();
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Thiếu tham số 'id' trong yêu cầu.");
        }
    }
}
