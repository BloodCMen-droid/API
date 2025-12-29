package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Producto;

public class ProductoDAO {

    // 1️⃣ Listar todos los productos
    public List<Producto> listar() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        Connection conn = SqlConexion.obtenerConexion();
        String sql = "SELECT id, nombre, descripcion, precio, adicional, talla, color, imagen FROM productos";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            lista.add(new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getString("adicional"),
                rs.getString("talla"),
                rs.getString("color"),
                rs.getString("imagen")
            ));
        }
        conn.close();
        return lista;
    }

    // 2️⃣ Agregar un producto
    public boolean agregar(Producto p) throws SQLException {
        Connection conn = SqlConexion.obtenerConexion();
        String sql = "INSERT INTO productos(nombre, descripcion, precio, adicional, talla, color, imagen) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setDouble(3, p.getPrecio());
        ps.setString(4, p.getAdicional());
        ps.setString(5, p.getTalla());
        ps.setString(6, p.getColor());
        ps.setString(7, p.getImagen());
        int filas = ps.executeUpdate();
        conn.close();
        return filas > 0;
    }

    // 3️⃣ Actualizar un producto
    public boolean actualizar(Producto p) throws SQLException {
        Connection conn = SqlConexion.obtenerConexion();
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, adicional = ?, talla = ?, color = ?, imagen = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setDouble(3, p.getPrecio());
        ps.setString(4, p.getAdicional());
        ps.setString(5, p.getTalla());
        ps.setString(6, p.getColor());
        ps.setString(7, p.getImagen());
        ps.setInt(8, p.getId());
        int filas = ps.executeUpdate();
        conn.close();
        return filas > 0;
    }

    // 4️⃣ Eliminar un producto
    public boolean eliminar(int id) throws SQLException {
        Connection conn = SqlConexion.obtenerConexion();
        String sql = "DELETE FROM productos WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        int filas = ps.executeUpdate();
        conn.close();
        return filas > 0;
    }

    // 5️⃣ Buscar un producto por ID
    public Producto buscarPorId(int id) throws SQLException {
        Connection conn = SqlConexion.obtenerConexion();
        String sql = "SELECT id, nombre, descripcion, precio, adicional, talla, color, imagen FROM productos WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Producto p = null;
        if(rs.next()) {
            p = new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getString("adicional"),
                rs.getString("talla"),
                rs.getString("color"),
                rs.getString("imagen")
            );
        }
        conn.close();
        return p;
    }
 // Dentro de ProductoDAO
    public Producto obtenerPorId(int id) throws SQLException {
        Producto p = null;
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (Connection cn = SqlConexion.obtenerConexion();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next()) {
                    p = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("adicional"),
                        rs.getString("talla"),
                        rs.getString("color"),
                        rs.getString("imagen")
                    );
                }
            }
        }
        return p;
    }

}

