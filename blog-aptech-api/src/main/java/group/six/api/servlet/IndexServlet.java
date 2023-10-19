package group.six.api.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "indexServlet", urlPatterns = {"/api/post", "/api/category", "/api/user"})

public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        switch (url) {
            case "/api/post":
                RequestDispatcher postDispatcher = req.getRequestDispatcher("/api_post.jsp");
                postDispatcher.forward(req, resp);
                break;
            case "/api/category":
                RequestDispatcher categoryDispatcher = req.getRequestDispatcher("/api_category.jsp");
                categoryDispatcher.forward(req, resp);
                break;
            case "/api/user":
                RequestDispatcher userDispatcher = req.getRequestDispatcher("/api_user.jsp");
                userDispatcher.forward(req, resp);
                break;
        }
    }
}
