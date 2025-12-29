package dao;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        Connection con = SqlConexion.obtenerConexion();
        if(con != null){
            System.out.println("Conexión exitosa ✅");
        } else {
            System.out.println("Error de conexión ❌");
        }
    }
}
