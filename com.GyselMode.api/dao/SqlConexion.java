package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConexion  {
	
	//CONEXION CON POSTGRESQL no SQL server

	private static final String URL ="jdbc:postgresql://dpg-d590qkbe5dus73e7hgvg-a.virginia-postgres.render.com:5432/gyselmode";

	private static final String USER = "gyseluser";
	private static final String PASSWORD = "aJdGMZOfWuP6jWP0sB60Nvj2IWLMp945";





    public static Connection obtenerConexion() {
        Connection conn = null;
        try {
            // Cargar driver JDBC
        	Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}

