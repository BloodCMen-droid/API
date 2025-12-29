package dao;

import java.sql.*;
import model.Usuario;

public class UsuarioDAO {

    public Usuario login(String usuario, String contrasena) throws SQLException {
        Connection conn = SqlConexion.obtenerConexion();
        String sql = "SELECT id, usuario, contrasena FROM usuarios WHERE usuario = ? AND contrasena = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, usuario);
        ps.setString(2, contrasena); // Nota: en producción, usa hash
        ResultSet rs = ps.executeQuery();
        Usuario user = null;
        if(rs.next()) {
            user = new Usuario(rs.getInt("id"), rs.getString("usuario"), rs.getString("contrasena"));
        }
        conn.close();
        return user; // si es null, usuario o contraseña incorrectos
    }
}
