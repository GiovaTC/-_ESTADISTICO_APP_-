package conexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionOracle {

    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private static final String USER = "system";
    private static final String PASSWORD = "Tapiero123";

    public static Connection conectar() {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection conexion = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Conexion exitosa a Oracle 19c! ");

            return conexion;
        } catch (Exception e) {
            System.out.println("Error de conexion");
            e.printStackTrace();

            return null;
        }
    }
}   
