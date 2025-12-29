package servlet;

import dao.ProductoDAO;
import model.Producto;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.SQLException;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;



@WebServlet("/ProductoServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,       // 1MB
    maxFileSize = 1024 * 1024 * 5,         // 5MB por archivo
    maxRequestSize = 1024 * 1024 * 10      // 10MB total
)
public class ProductoServlet extends HttpServlet {

    private ProductoDAO dao = new ProductoDAO();

    // ================= GET: Listar productos =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            var lista = dao.listar();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < lista.size(); i++) {
                Producto p = lista.get(i);
                json.append("{")
                    .append("\"id\":").append(p.getId()).append(",")
                    .append("\"nombre\":\"").append(p.getNombre()).append("\",")
                    .append("\"descripcion\":\"").append(p.getDescripcion()).append("\",")
                    .append("\"precio\":").append(p.getPrecio()).append(",")
                    .append("\"adicional\":\"").append(p.getAdicional()).append("\",")
                    .append("\"talla\":\"").append(p.getTalla()).append("\",")
                    .append("\"color\":\"").append(p.getColor()).append("\",")
                    .append("\"imagen\":\"").append(p.getImagen()).append("\"")
                    .append("}");
                if (i < lista.size() - 1) json.append(",");
            }
            json.append("]");
            out.print(json.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print("{\"error\":\"Error al obtener productos\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

 // ================= POST: Agregar producto =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            double precio = Double.parseDouble(request.getParameter("precio"));
            String color = request.getParameter("color");
            String talla = request.getParameter("talla");
            String adicional = request.getParameter("adicional");

            // ================= Subir imagen =================
            String urlImagen = ""; // URL por defecto
            Part filePart = request.getPart("imagen");

            if (filePart != null && filePart.getSize() > 0) {
                // Configura tu cuenta de Cloudinary
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "dmyvnafew",
                        "api_key", "256116538663764",
                        "api_secret", "9dfbMS5H1XssIc8plrjeUu8AySU",
                        "secure",     true
                ));
                @SuppressWarnings("rawtypes")
                Map uploadResult = cloudinary.uploader().upload(filePart.getInputStream(), ObjectUtils.emptyMap());
                urlImagen = (String) uploadResult.get("secure_url"); // URL pública de la imagen
            }

            Producto p = new Producto(0, nombre, descripcion, precio, adicional, talla, color, urlImagen);

            if (dao.agregar(p)) {
                out.print("{\"msg\":\"Producto agregado\"}");
            } else {
                response.setStatus(500);
                out.print("{\"error\":\"No se pudo agregar\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print("{\"error\":\"Error al procesar POST\"}");
        } finally {
            out.flush();
            out.close();
        }
    }



 // ================= PUT: Actualizar producto =================
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Obtener parámetros del formulario
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            double precio = Double.parseDouble(request.getParameter("precio"));
            String color = request.getParameter("color");
            String talla = request.getParameter("talla");
            String adicional = request.getParameter("adicional");

            // Obtener el producto existente para mantener la imagen actual si no suben una nueva
            Producto existente = dao.obtenerPorId(id);
            if (existente == null) {
                response.setStatus(404);
                out.print("{\"error\":\"Producto no encontrado\"}");
                return;
            }
            String urlImagen = existente.getImagen(); // Imagen actual por defecto

            // ================= Subir nueva imagen a Cloudinary si la envían =================
            Part filePart = request.getPart("imagen");
            if (filePart != null && filePart.getSize() > 0) {
                // Usar la misma configuración que en doPost (mejor si tienes CloudinaryConfig)
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dmyvnafew",
                    "api_key",    "256116538663764",
                    "api_secret", "9dfbMS5H1XssIc8plrjeUu8AySU",
                    "secure",     true
                ));

                @SuppressWarnings("rawtypes")
                Map uploadResult = cloudinary.uploader().upload(filePart.getInputStream(),
                    ObjectUtils.asMap(
                        "folder", "gyselmode/productos",  // opcional: organiza las imágenes
                        "overwrite", true
                    ));

                urlImagen = (String) uploadResult.get("secure_url");
            }
            // Si no envían imagen, se mantiene la anterior (urlImagen = existente.getImagen())

            // Crear objeto actualizado
            Producto p = new Producto(id, nombre, descripcion, precio, adicional, talla, color, urlImagen);

            // Actualizar en la base de datos
            if (dao.actualizar(p)) {
                out.print("{\"msg\":\"Producto actualizado con éxito\"}");
            } else {
                response.setStatus(500);
                out.print("{\"error\":\"No se pudo actualizar el producto\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print("{\"error\":\"Error al procesar PUT: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    // ================= DELETE =================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (dao.eliminar(id)) {
                out.print("{\"msg\":\"Producto eliminado\"}");
            } else {
                response.setStatus(500);
                out.print("{\"error\":\"No se pudo eliminar\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print("{\"error\":\"Error al procesar DELETE\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    // ================= OPTIONS =================
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

