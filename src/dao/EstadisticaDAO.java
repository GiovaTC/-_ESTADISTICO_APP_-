package dao;

import conexion.ConexionOracle;
import modelo.DatoEstadistico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

public class EstadisticaDAO {

    public void guardarDato(DatoEstadistico dato){

        try {

            Connection conexion = ConexionOracle.conectar();
            String sql = "INSERT INTO ESTADISTICAS (VALOR) VALUES (?)";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setDouble(1, dato.getValor());

            ps.executeUpdate();

            System.out.println("Dato registrado! ");
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Double> obtenerDatos() {
        ArrayList<Double> lista = new ArrayList<>();

        try {
            Connection conexion = ConexionOracle.conectar();
            String sql = "SELECT * FROM ESTADISTICAS";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while ( rs.next()) {
                lista.add(rs.getDouble("VALOR"));
            }

            conexion.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public double calcularPromedio() {
        ArrayList<Double> datos = obtenerDatos();
        double suma = 0;

        for (double n : datos) {
            suma += n;
        }
        return suma / datos.size();
    }
}   
