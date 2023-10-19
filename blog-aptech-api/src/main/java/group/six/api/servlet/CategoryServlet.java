package group.six.api.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import group.six.api.config.ConnectionProvider;
import group.six.api.repository.CategoryRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet(name = "categoryServlet", urlPatterns = {"/api/category/getAllCategorys", "/api/category/getCategoryById"})

public class CategoryServlet extends HttpServlet {

    ConnectionProvider connectionProvider = new ConnectionProvider();

    private CategoryRepository categoryRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        categoryRepository = new CategoryRepository(connectionProvider.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        switch (url) {
            case "/api/category/getAllCategorys":
                getAllCategories(resp);
                break;
            case "/api/category/getCategoryById":
                getCategoryById(req, resp);
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("URL không hợp lệ.");
                break;
        }
    }

    private void getAllCategories(HttpServletResponse resp) throws IOException {        // Thiết lập tiêu đề cho CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); // Cho phép tất cả các nguồn gốc truy cập
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // Cho phép các phương thức yêu cầu
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"); // Cho phép các tiêu đề yêu cầu

        try {
            JsonArray allCategories = categoryRepository.getAllCategories();
            resp.setContentType("application/json");
            resp.getWriter().write(allCategories.toString());
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Lỗi truy vấn cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    private void getCategoryById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Thiết lập tiêu đề cho CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); // Cho phép tất cả các nguồn gốc truy cập
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // Cho phép các phương thức yêu cầu
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"); // Cho phép các tiêu đề yêu cầu
        String idFilter = req.getParameter("id");
        resp.setContentType("application/json");

        if (idFilter != null && !idFilter.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(idFilter);
                JsonObject categoryObject = categoryRepository.getCategoryById(categoryId);

                if (categoryObject != null) {
                    resp.getWriter().write(categoryObject.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Không tìm thấy category với ID: " + categoryId);
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
