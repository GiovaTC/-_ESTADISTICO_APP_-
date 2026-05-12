# -sistemaEstadistico_- :.
# Sistema Estadístico en Java + Oracle 19c

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/44b43819-dbee-4e06-bf4e-90d59887dc34" />    

<img width="2553" height="1079" alt="image" src="https://github.com/user-attachments/assets/30fe17ea-1d43-4155-a373-4e59d54f5d10" />    
    
```

## Descripción

Proyecto desarrollado en:

* Java SE
* Swing GUI
* Oracle Database 19c
* JDBC
* IntelliJ IDEA
* Programación Orientada a Objetos

El sistema permite:

* Registrar datos estadísticos
* Calcular promedio
* Calcular mediana
* Calcular moda
* Calcular desviación estándar
* Consultar registros almacenados
* Mostrar estadísticas generales
* Conexión directa con Oracle 19c
* Interfaz gráfica moderna

---

# Estructura del Proyecto

```text
ESTADISTICO_APP
│
├── src
│   ├── conexion
│   │     ConexionOracle.java
│   │
│   ├── modelo
│   │     DatoEstadistico.java
│   │
│   ├── dao
│   │     EstadisticaDAO.java
│   │
│   ├── vista
│   │     VentanaPrincipal.java
│   │
│   └── Main.java
│
└── ojdbc11.jar
```

---

# Script Oracle 19c

## Crear Tabla

```sql
CREATE TABLE ESTADISTICAS (
    ID NUMBER GENERATED ALWAYS AS IDENTITY,
    VALOR NUMBER(10,2),
    FECHA_REGISTRO DATE DEFAULT SYSDATE,
    CONSTRAINT PK_ESTADISTICAS PRIMARY KEY (ID)
);
```

---

# Configuración JDBC

## Descargar Driver Oracle

Descargar:

```text
ojdbc11.jar
```

Agregar el archivo JAR al proyecto en IntelliJ IDEA.

---

# Clase ConexionOracle.java

```java
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionOracle {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "SYSTEM";
    private static final String PASSWORD = "123456";

    public static Connection conectar() {

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection conexion = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Conexion exitosa a Oracle 19c");

            return conexion;

        } catch (Exception e) {

            System.out.println("Error de conexion");
            e.printStackTrace();

            return null;
        }
    }
}
```

---

# Clase DatoEstadistico.java

```java
package modelo;

public class DatoEstadistico {

    private int id;
    private double valor;

    public DatoEstadistico() {
    }

    public DatoEstadistico(double valor) {
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
```

---

# Clase EstadisticaDAO.java

```java
package dao;

import conexion.ConexionOracle;
import modelo.DatoEstadistico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

public class EstadisticaDAO {

    public void guardarDato(DatoEstadistico dato) {

        try {

            Connection conexion = ConexionOracle.conectar();

            String sql = "INSERT INTO ESTADISTICAS (VALOR) VALUES (?)";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setDouble(1, dato.getValor());

            ps.executeUpdate();

            System.out.println("Dato registrado");

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

            while (rs.next()) {
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

    public double calcularMediana() {

        ArrayList<Double> datos = obtenerDatos();

        Collections.sort(datos);

        int tamaño = datos.size();

        if (tamaño % 2 == 0) {

            return (datos.get(tamaño / 2 - 1)
                    + datos.get(tamaño / 2)) / 2;

        } else {

            return datos.get(tamaño / 2);
        }
    }

    public double calcularModa() {

        ArrayList<Double> datos = obtenerDatos();

        double moda = datos.get(0);
        int maxFrecuencia = 0;

        for (double valor : datos) {

            int frecuencia = 0;

            for (double n : datos) {

                if (n == valor) {
                    frecuencia++;
                }
            }

            if (frecuencia > maxFrecuencia) {
                maxFrecuencia = frecuencia;
                moda = valor;
            }
        }

        return moda;
    }

    public double calcularDesviacion() {

        ArrayList<Double> datos = obtenerDatos();

        double promedio = calcularPromedio();

        double suma = 0;

        for (double n : datos) {
            suma += Math.pow(n - promedio, 2);
        }

        return Math.sqrt(suma / datos.size());
    }
}
```

---

# Clase VentanaPrincipal.java

```java
package vista;

import dao.EstadisticaDAO;
import modelo.DatoEstadistico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    private JTextField txtValor;
    private JTextArea areaResultado;

    private EstadisticaDAO dao;

    public VentanaPrincipal() {

        dao = new EstadisticaDAO();

        setTitle("Sistema Estadistico");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        iniciarComponentes();
    }

    private void iniciarComponentes() {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("SISTEMA ESTADISTICO");
        lblTitulo.setBounds(180, 20, 300, 30);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(lblTitulo);

        JLabel lblValor = new JLabel("Valor:");
        lblValor.setBounds(50, 80, 100, 30);
        panel.add(lblValor);

        txtValor = new JTextField();
        txtValor.setBounds(120, 80, 200, 30);
        panel.add(txtValor);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(350, 80, 150, 30);
        panel.add(btnGuardar);

        JButton btnCalcular = new JButton("Calcular Estadisticas");
        btnCalcular.setBounds(180, 140, 220, 40);
        panel.add(btnCalcular);

        areaResultado = new JTextArea();
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBounds(50, 220, 500, 180);
        panel.add(scroll);

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    double valor = Double.parseDouble(txtValor.getText());

                    DatoEstadistico dato = new DatoEstadistico(valor);

                    dao.guardarDato(dato);

                    JOptionPane.showMessageDialog(null,
                            "Dato registrado correctamente");

                    txtValor.setText("");

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(null,
                            "Ingrese un numero valido");
                }
            }
        });

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    double promedio = dao.calcularPromedio();
                    double mediana = dao.calcularMediana();
                    double moda = dao.calcularModa();
                    double desviacion = dao.calcularDesviacion();

                    areaResultado.setText("");

                    areaResultado.append("PROMEDIO: " + promedio + "\n");
                    areaResultado.append("MEDIANA: " + mediana + "\n");
                    areaResultado.append("MODA: " + moda + "\n");
                    areaResultado.append("DESVIACION ESTANDAR: " + desviacion + "\n");

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(null,
                            "Debe registrar datos primero");
                }
            }
        });

        add(panel);
    }
}
```

---

# Clase Main.java

```java
import vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {

        VentanaPrincipal ventana = new VentanaPrincipal();

        ventana.setVisible(true);
    }
}
```

---

# Datos de Prueba

```text
10
20
20
30
40
50
```

---

# Resultado Esperado

```text
PROMEDIO: 28.333333
MEDIANA: 25
MODA: 20
DESVIACION ESTANDAR: 13.437
```

---

# Configuración Oracle 19c

## URL JDBC

```text
jdbc:oracle:thin:@localhost:1521:XE
```

## Usuario

```text
SYSTEM
```

## Password

```text
123456
```

---

# Librerías Necesarias

```text
ojdbc11.jar
```

---

# Características Técnicas

## Backend

* Java SE
* JDBC
* DAO Pattern
* Programación orientada a objetos

## Frontend

* Java Swing
* Eventos
* Componentes gráficos
* Interfaz moderna

## Base de Datos

* Oracle Database 19c
* SQL
* Persistencia de datos

---

# Mejoras Futuras

* Exportar estadísticas a PDF
* Gráficas estadísticas
* Login de usuarios
* Dashboard administrativo
* Reportes automáticos
* CRUD completo
* Filtros avanzados
* Estadísticas por fecha
* Integración con Apache POI

---

# Ejecución del Proyecto

## Paso 1

Crear la tabla en Oracle 19c.

## Paso 2

Agregar:

```text
ojdbc11.jar
```

al proyecto.

## Paso 3

Ejecutar:

```text
Main.java
```

---

# Resultado Final

El sistema permite almacenar datos estadísticos en Oracle 19c y calcular automáticamente:

* Promedio
* Mediana
* Moda
* Desviación estándar

mediante una interfaz gráfica desarrollada en Java Swing.
