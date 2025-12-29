package servlet;

import dao.UsuarioDAO;
import model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    // POST: login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CORS (igual que ProductoServlet)
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        UsuarioDAO dao = new UsuarioDAO();
        try {
            Usuario u = dao.login(usuario, contrasena);
            if(u != null) {
                out.print("{"
                        + "\"id\":" + u.getId() + ","
                        + "\"usuario\":\"" + u.getUsuario() + "\""
                        + "}");
            } else {
                response.setStatus(401);
                out.print("{\"error\":\"Usuario o contraseña incorrectos\"}");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print("{\"error\":\"Error del servidor\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    // OPTIONS: preflight CORS
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}

