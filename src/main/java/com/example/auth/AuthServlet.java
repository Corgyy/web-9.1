package com.example.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {

    // LAB: lưu tạm RAM (plaintext)
    private static final ConcurrentHashMap<String, String> USERS = new ConcurrentHashMap<>();
    private static final Pattern EMAIL_RE = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    // URL đích sau đăng nhập (site ngoài)
    private static final String SUCCESS_REDIRECT_URL = "https://baotoan-9-2.onrender.com";

    // Trang nội bộ (khi cần redirect lỗi/điều hướng)
    private static final String LOGIN_PAGE    = "/login.jsp";
    private static final String REGISTER_PAGE = "/register.jsp";
    private static final String INDEX_PAGE    = "/index.jsp";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        final String action   = val(request.getParameter("action"));
        final String emailRaw = val(request.getParameter("email"));
        final String password = val(request.getParameter("password"));

        if (emailRaw.isEmpty() || password.isEmpty()) {
            redirectWithErrorMsg(request, response, action, "Missing email or password");
            return;
        }

        final String email = emailRaw.toLowerCase();

        switch (action) {
            case "register":
                handleRegister(request, response, email, password);
                break;
            case "login":
                handleLogin(request, response, email, password);
                break;
            case "logout":
                handleLogout(request, response);
                break;
            default:
                response.sendRedirect(ctx(request) + INDEX_PAGE);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if ("logout".equals(request.getParameter("action"))) {
            handleLogout(request, response);
        } else {
            response.sendRedirect(ctx(request) + INDEX_PAGE);
        }
    }

    /* ===================== HANDLERS ===================== */

    private void handleRegister(HttpServletRequest request, HttpServletResponse response,
                                String email, String password) throws IOException {
        if (!EMAIL_RE.matcher(email).matches()) {
            redirectWithErrorMsg(request, response, "register", "Invalid email format");
            return;
        }
        if (password.isBlank()) {
            redirectWithErrorMsg(request, response, "register", "Password cannot be empty");
            return;
        }
        String confirm = val(request.getParameter("confirm"));
        if (!confirm.isEmpty() && !password.equals(confirm)) {
            redirectWithErrorMsg(request, response, "register", "Passwords do not match");
            return;
        }

        if (USERS.containsKey(email)) {
            redirectWithErrorMsg(request, response, "register", "Email already exists");
        } else {
            USERS.put(email, password); // LAB: plaintext
            response.sendRedirect(ctx(request) + REGISTER_PAGE + "?ok=1");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response,
                             String email, String password) throws IOException {
        String stored = USERS.get(email);

        if (stored == null) {
            redirectWithErrorCode(request, response, "login", "email_not_exist");
            return;
        }
        if (!stored.equals(password)) {
            redirectWithErrorCode(request, response, "login", "wrong_password");
            return;
        }

        // Đăng nhập OK → tạo session (nếu muốn giữ trạng thái)
        HttpSession session = request.getSession(true);
        session.setAttribute("user", email);
        session.setMaxInactiveInterval(30 * 60);

        // CHUYỂN THẲNG RA SITE NGOÀI
        response.sendRedirect(SUCCESS_REDIRECT_URL);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        response.sendRedirect(ctx(request) + LOGIN_PAGE);
    }

    /* ===================== HELPERS ===================== */

    private static String val(String s) {
        return s == null ? "" : s.trim();
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static String ctx(HttpServletRequest req) {
        return req.getContextPath();
    }

    private void redirectWithErrorCode(HttpServletRequest req, HttpServletResponse resp,
                                       String action, String code) throws IOException {
        String q = "?error=" + enc(code);
        switch (action) {
            case "register":
                resp.sendRedirect(ctx(req) + REGISTER_PAGE + q);
                break;
            case "login":
                resp.sendRedirect(ctx(req) + LOGIN_PAGE + q);
                break;
            default:
                resp.sendRedirect(ctx(req) + INDEX_PAGE + q);
        }
    }

    private void redirectWithErrorMsg(HttpServletRequest req, HttpServletResponse resp,
                                      String action, String message) throws IOException {
        String q = "?error=" + enc(message);
        switch (action) {
            case "register":
                resp.sendRedirect(ctx(req) + REGISTER_PAGE + q);
                break;
            case "login":
                resp.sendRedirect(ctx(req) + LOGIN_PAGE + q);
                break;
            default:
                resp.sendRedirect(ctx(req) + INDEX_PAGE + q);
        }
    }
}
