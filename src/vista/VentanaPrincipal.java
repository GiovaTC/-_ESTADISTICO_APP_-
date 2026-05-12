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
